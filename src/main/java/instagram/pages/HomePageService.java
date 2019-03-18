package instagram.pages;

import instagram.email.EmailService;
import instagram.exceptions.ExceptionService;
import instagram.factory.DriverFactory;
import instagram.http.HttpCall;
import instagram.logger.LogService;
import instagram.model.*;
import instagram.model.enums.JobStatus;
import instagram.report.ReportService;
import instagram.sessions.SessionService;
import instagram.utils.DataUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class HomePageService extends SuperPage {

	private LogService logger;
	private Map<String, UserData> userDataMap;

	private final Integer RIGHT_ARROW_NOT_FOUND_LIMIT = 3;
	private final Integer NEW_POST_NOT_FOUND_LIMIT = 10;
	private final Integer GENERAL_LIMIT = 10;
	private final Integer BLOCKED_LIMIT = 3;

	@Autowired
	private ReportService reportService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ExceptionService exceptionService;

	@Autowired
	private SessionService sessionService;

	public HomePageService() {
	    logger = new LogService();
		userDataMap = new ConcurrentHashMap<>();
	}

	@Scheduled(initialDelay = 10 * 1000, fixedRate = 60 * 1000)
	public synchronized void execute() {

        Map<String, Session> sessionMap = sessionService.getActiveSessions();

		if (sessionMap.size() == 0) {
		    DriverFactory.closeDriverIfOpen();
            return;
        }

		logger.append("HomePageService::execute").append("activeSessionCount").append(sessionMap.size()).log();

		// Using iterator because key may be removed within iteration when job is completed or blocked
		Iterator<Map.Entry<String, Session>> iterator = sessionMap.entrySet().iterator();
		while (iterator.hasNext()) {

            Map.Entry<String, Session> entry = iterator.next();

            String sessionId = entry.getKey();
            Session session = entry.getValue();

            WebDriver driver = DriverFactory.getDriver(sessionId);
			superPage(driver);

			Data data = session.getData();
			DataUtils.processData(data);

			UserData userData = getExistingOrNewUserData(sessionId, data);

			Report report = reportService.getReport(data.username);

			isUserBlockedOrFinished(userData, data, report);

			if (isJobFinished(report.getJobStatus())) {
			    userDataMap.remove(sessionId);
				iterator.remove();
				logger.append(data).append("Job Finished").append(report.getJobStatus()).log();
			    continue;
            }

			perform(userData, data, report);

		}
	}

	private UserData getExistingOrNewUserData(String sessionId, Data data) {
		UserData userData = userDataMap.get(sessionId);
		if (userData == null) {
			userData = new UserData(data);
			userDataMap.put(sessionId, userData);
		}
		return userData;
	}

	private boolean isJobFinished(JobStatus jobStatus) {
	    return jobStatus == JobStatus.COMPLETED || jobStatus == JobStatus.BLOCKED ||
				jobStatus == JobStatus.TERMINATED;
    }

	/**
	 * Check if user is
	 * - blocked - Blocked from liking and commenting
	 * - completed - Job has finished execution
	 * - resource heavy - No new posts are found and just looping through
	 *
	 * @param userData
	 * @param data
	 * @param report
	 */
    private void isUserBlockedOrFinished(UserData userData, Data data, Report report) {
        if (_isUserBlocked(userData, data)) {
            emailService.sendUserIsBlockedEmail(data);
            report.setJobStatus(JobStatus.BLOCKED);
        }

        if (userData.getHashtags().isEmpty()) {
            report.setJobStatus(JobStatus.COMPLETED);
            emailService.sendJobFinishedEmail(data);
        }

        if (userData.getNewPostNotFoundCounter() > NEW_POST_NOT_FOUND_LIMIT) {
        	report.setJobStatus(JobStatus.TERMINATED);
        	emailService.sendJobTerminatedEmail(data);
		}
    }

	/**
	 * Go to Hashtag page and try to like or comment
	 * @param userData
	 * @param data
	 * @param report
	 */
	public void perform(UserData userData, Data data, Report report) {

		if (userData.getHashtags().size() > 0 && userData.getPostCounter() < userData.getNumberOfPosts()) {

            logger.append("HomePageService::perform").append(data).append("hashtagsLeft").append(userData.getHashtags().size())
					.append("postsCounter").append(userData.getPostCounter())
					.append("totalPosts").append(userData.getNumberOfPosts()).log();

			report.setJobStatus(JobStatus.RUNNING);

			String hashtag = userData.getHashtags().get(0);
			_gotoHashTagPage(hashtag);
			report.setCurrentHashtag(hashtag);

			scrollDown(1000);
			List<WebElement> photos = getElements("img");

			/* 	Skip top posts and open the most recent
			 *	0 is the main photo on top for the hashtag, it is not a post.
			 *	1 to 9 for top posts (Sometimes it could be less than 9 photos for top posts)
			 *	10th photo will surely be the most recent even if sometimes it may not be the first
			 */
			int indexOfFirstMostRecentPhoto = 10;

			try {
				// photos.get(indexOfFirstMostRecentPhoto).click() does not work, throws not clickable exception
				// Hence using JS click
				executeJs("arguments[0].click();", photos.get(indexOfFirstMostRecentPhoto));
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				logger.appendErr(data).appendErr("IndexOutOfBoundException on clicking photos")
						.appendErr("photosFound").appendErr(photos.size()).err();
				return;
			}

			if (isPageNotFound()) {
				logger.appendErr(data).appendErr("Page Not Found").err();
				perform(userData, data, report);
				return;
			}

			performOnNewPost(userData, data, report);
		}

	}

	/**
	 * When driver is in the post page, it tries to like or comment
	 * @param userData
	 * @param data
	 * @param report
	 */
	private void performOnNewPost(UserData userData, Data data, Report report) {

		String profileName = getProfileName();
		Profile currentProfile = HttpCall.getProfile(profileName);

		logger.append("HomePageService::performOnPost").append(data)
				.append("MaxNoOfFollowers").append(data.maxNoOfFollowers)
				.append("CurrentProfile").append(currentProfile);

		if (currentProfile.getNoOfFollowers() <= data.maxNoOfFollowers) {

			Action action = getActionFromData(data);

			/*
			 * This is where actual liking and commenting happens
			 */
			boolean liked = _performLike(action, userData, data, report);

			boolean commented = _performComment(action, userData, data, report, profileName);

			if (liked || commented) {
				userData.incrementPostCounter();
				userData.setNewPostNotFoundCounter(0);
				if (userData.getPostCounter() >= userData.getNumberOfPosts()) {
					userData.getHashtags().remove(0);
					userData.setPostCounter(0);
				}
				logger.log();
			} else {
				tryNextPostIfEligible(userData, data, report);
			}
		} else {
			tryNextPostIfEligible(userData, data, report);
		}
	}

	/**
	 * If user is eligible, we go to next post and try to like or comment
	 * @param userData
	 * @param data
	 * @param report
	 */
	private void tryNextPostIfEligible(UserData userData, Data data, Report report) {
		logger.append("Trying Next Post").append(userData.getNewPostNotFoundCounter()).log();
		if (isUserEligibleForNextPost(userData)) {
			// We click next to go the next post and addJobToQueue the action again
			goToNextPost(userData, data);
			performOnNewPost(userData, data, report);
		}
	}

	private boolean isUserEligibleForNextPost(UserData userData) {
		userData.setNewPostNotFoundCounter(userData.getNewPostNotFoundCounter() + 1);
		return userData.getNewPostNotFoundCounter() <= NEW_POST_NOT_FOUND_LIMIT;
	}

	private void goToNextPost(UserData userData, Data data) {
		// We click next to go the next post and addJobToQueue the action again
		boolean clickedNext = clickNext();
		if (!clickedNext) {
			// If next was not clicked, next post was not found or may be next arrow CSS has changed
			logger.appendErr(data.username).appendErr("Right Arrow Not Found, Retrying Hashtag").err();
			userData.setRightArrowNotFoundCounter(userData.getRightArrowNotFoundCounter() + 1);
			if (userData.getRightArrowNotFoundCounter() > RIGHT_ARROW_NOT_FOUND_LIMIT) {
				logger.appendErr(data.username).appendErr("Issue with clicking right arrow").err();
				exceptionService.addException(new Exception("Issue with clicking RIGHT ARROW for " + data.username));
			}
		} else {
			userData.setRightArrowNotFoundCounter(0);
		}
	}

	public Action getActionFromData(Data data) {
		Action action = new Action();
		if (data.commentOnly) {
			action.comment = true;
			return action;
		}

		action.like = true;
		if (data.comments != null && data.comments.size() > 0) {
			action.comment = true;
		}

		return action;
	}


	/**
	 * If LIKE is blocked and COMMENT is blocked
	 * OR LIKE is blocked and there is nothing to comment
	 * Return because the user is basically blocked
	 */
    private boolean _isUserBlocked(UserData userData, Data data) {
		return userData.isLikingBlocked() && (userData.isCommentingBlocked() || data.comments.isEmpty());
	}

	private boolean _performLike(Action action, UserData userData, Data data, Report report) {
		if (userData.isLikingBlocked() || !action.like || isAlreadyLiked())
			return false;
		like(getLikeButton());
		if (!isAlreadyLiked()) {
			logger.appendErr(data.username).appendErr("Issue with LIKE").err();
			return false;
		}

		if (checkIfLikeIsBlocked(report) || userData.getLikedBlockedCounter() > 0) {
			if (userData.getLikedBlockedCounter() > BLOCKED_LIMIT) {
			    logger.append("LIKING IS BLOCKED");
                userData.setLikingBlocked(true);
                emailService.sendLikeIsBlockedEmail(data);
                return false;
            }

			userData.setLikedBlockedCounter(userData.getLikedBlockedCounter() + 1);
            logger.append("LIKING MAY BE BLOCKED, Attempt " + userData.getLikedBlockedCounter());
		}
		userData.setLikedBlockedCounter(0);

		report.incrementPhotoLiked();
		logger.append("liked");
		return true;
	}

	private boolean _performComment(Action action, UserData userData, Data data, Report report, String profileName) {

    	// If user is blocked from commenting or input action comment is false, return false
    	if (userData.isCommentingBlocked() || !action.comment)
    		return false;

    	// If user is not doing just comments then we need to consider like to comment ratio to see
		// if it is the turn to comment. If comments only, then we do not care about the ratio.
    	if (!data.commentOnly && userData.getPostCounter() % data.getLikesToCommentRatio() != 0)
    		return false;

    	// If the profile is not already visited and not already commented, we can try to comment
		// Since we put random comments, we do not want to risk putting same comment for the same profile
		if (_isProfileNotVisitedAndAddToSet(userData, profileName) && !isAlreadyCommented(data.username)) {
			String comment = _getRandomComment(data);
			_comment(data, comment);
			if (!isAlreadyCommented(data.username)) {
				logger.appendErr(data.username).appendErr("Issue with COMMENT")
						.appendErr("Comment: " + comment).err();
				return false;
			}

			if (checkIfCommentIsBlocked(report, data.username) || userData.getCommentBlockedCounter() > 0) {
			    if (userData.getCommentBlockedCounter() > BLOCKED_LIMIT) {
					logger.appendErr(data.username).appendErr("COMMENTING IS BLOCKED").err();
                    userData.setCommentingBlocked(true);
                    emailService.sendCommentIsBlockedEmail(data);
                    return false;
                }
			    userData.setCommentBlockedCounter(userData.getCommentBlockedCounter() + 1);
			    logger.append("COMMENTING MAY BE BLOCKED, Attempt " + userData.getCommentBlockedCounter());
			}
			userData.setCommentBlockedCounter(0);

			report.incrementPhotosCommented();
			logger.append("commented").append(comment);
			return true;
		}
		return false;
	}

	/**
	 * Is it time to check if photo LIKING is BLOCKED for every <GENERAL_LIMIT> photos
	 * @return
	 */
	private boolean checkIfLikeIsBlocked(Report report) {
    	int photosLiked = report.getPhotosLiked();
    	return (photosLiked != 0) && (photosLiked % GENERAL_LIMIT == 0) && isLikeBlocked();
	}

	/**
	 * Is it time to Check if photo COMMENTING is BLOCKED for every <GENERAL_LIMIT> photos
	 * @return
	 */
	private boolean checkIfCommentIsBlocked(Report report, String username) {
		int photosLiked = report.getPhotosLiked();
		return (photosLiked != 0) && (photosLiked % GENERAL_LIMIT == 0) && isCommentBlocked(username);
	}

	private void _gotoHashTagPage(String hashtag) {
        hashtag = hashtag.toLowerCase();
		getDriver().get(ConfigData.HASHTAG_URL + hashtag);
    }

	private boolean _isProfileNotVisitedAndAddToSet(UserData userData, String profileName) {
		if (userData.getAlreadyVisitedSet().contains(profileName))
			return false;
		else
			userData.getAlreadyVisitedSet().add(profileName);
		return true;
	}

	private void _comment(Data data, String comment) {
		if (getCommentInput() == null)
			return;
		comment(getCommentInput(), comment);
		sleep(2);
		comment(getCommentInput(), Keys.ENTER);
		sleep(2);
		waitForCommentToLoad();
		logger.append(data).append("comment").append(comment);
	}

	private String _getRandomComment(Data data) {
		int index = ThreadLocalRandom.current().nextInt(0, data.comments.size());
		return data.comments.get(index);
	}
}
