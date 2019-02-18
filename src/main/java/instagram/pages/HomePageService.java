package instagram.pages;

import instagram.email.EmailService;
import instagram.exceptions.ExceptionService;
import instagram.http.HttpCall;
import instagram.logger.LogService;
import instagram.model.*;
import instagram.model.enums.JobStatus;
import instagram.report.ReportService;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HomePageService extends SuperPage {

	private Set<String> alreadyVisited;
	private Data data;
	private Report report;
	private LogService logger;

	private int rightArrowNotFoundCounter;
	private int newPostNotFoundCounter;
	private int likedBlockedCounter;
	private int commentBlockedCounter;
	private boolean isLikingBlocked;
	private boolean isCommentingBlocked;
	private final Integer RIGHT_ARROW_NOT_FOUND_LIMIT = 3;
	private final Integer GENERAL_LIMIT = 10;
	private final Integer BLOCKED_LIMIT = 3;

	@Autowired
	private ReportService reportService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ExceptionService exceptionService;

	public void init(WebDriver driver, Data data) {
		logger = new LogService();
		superPage(driver);
		this.data = data;
		PageFactory.initElements(driver, this);
		alreadyVisited = new HashSet<>();

		if (StringUtils.isBlank(this.data.username))
			throw new RuntimeException("Username is not present, " + this.data);

		this.data.username = this.data.username.toLowerCase();
		this.report = reportService.getNewReport(this.data.username);
		this.report.setData(this.data);
	}

	public void performActionsInLoop(Data data, WebDriver driver) {
		init(driver, data);
		if (data.commentOnly)
			commentHashtagInLoop();
		else
			likeAndCommentHashtagInLoop();
		if (_isUserBlocked())
			emailService.sendUserIsBlockedEmail(data);
		else
			emailService.sendJobFinishedEmail(data);
	}

	public void commentHashTag() {
		for (String hashtag : data.hashtags) {
			report.setCurrentHashtag(hashtag);
			_performOnHashTag(Action.getCommentAction(), hashtag);
		}
	}

	private void logLoopCount(int i) {
		logger.append(data).append("loopCount")
				.append(data.noOfTimesToLoop).append("loop").append(i).log();
	}

	public void commentHashtagInLoop() {
		for (int i = 1; i <= data.noOfTimesToLoop; i++) {
			logLoopCount(i);
			report.incrementCurrentLoop();
			commentHashTag();
		}
		report.setJobAsCompleted();
	}

	public void likeAndCommentHashTag() {
		for (String hashtag : data.hashtags) {
			report.setCurrentHashtag(hashtag);
			_performOnHashTag(Action.getLikeCommentAction(), hashtag);
		}
	}

	public void likeAndCommentHashtagInLoop() {
	    for (int i = 1; i <= data.noOfTimesToLoop; i++) {
            logLoopCount(i);
            report.incrementCurrentLoop();
            likeAndCommentHashTag();
        }
	    report.setJobAsCompleted();
    }

	/**
	 * If LIKE is blocked and COMMENT is blocked
	 * OR LIKE is blocked and there is nothing to comment
	 * Return because the user is basically blocked
	 */
    private boolean _isUserBlocked() {
		return isLikingBlocked && (isCommentingBlocked || data.comments.isEmpty());
	}

	private void _performOnHashTag(Action action, String hashtag) {

		/*
		 * If action is not LIKE and it is not COMMENT
		 * OR if it is COMMENT but there are no comments
		 * Then nothing else to do, hence return
		 */
		if (!action.like && (!action.comment || data.comments.isEmpty()))
			return;

		if (_isUserBlocked()) {
			report.setJobStatus(JobStatus.BLOCKED);
			return;
		}

		if (hashtag == null)
			return;

		hashtag = hashtag.trim();

		_gotoHashTagPage(hashtag);

		logger.appendData(data).append("#" + hashtag);

		scrollDown(1000);
		List<WebElement> photos = getElements("img");
		logger.append("photosFound").append(photos.size()).log();

		if (photos.size() == 0)
			exceptionService.addException(new Exception("No posts found for #" + hashtag + "\n" + this.data));

		/* Skip top posts and open the most recent
		   0 is the main photo on top for the hashtag, it is not a post.
		   1 to 9 for top posts (Sometimes it could be less than 9 photos for top posts)
		   10th photo will surely be the most recent even if sometimes it may not be the first
		 */
		int indexOfFirstMostRecentPhoto = 10;

		try {
			// photos.get(indexOfFirstMostRecentPhoto).click() does not work, throws not clickable exception
            // Hence using JS click
			executeJs("arguments[0].click();", photos.get(indexOfFirstMostRecentPhoto));
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return;
		}

		report.setJobStatus(JobStatus.RUNNING);

		while (action.counter < data.noOfPhotos) {

			if (isPageNotFound()) {
				_performOnHashTag(action, hashtag);
				return;
			}

			String profileName = getProfileName();
			if (StringUtils.isBlank(profileName))
				exceptionService.addException(new Exception("ISSUE with fetching Profile Name\n" + this.data));

			logger.append(data).append("maxNoOfFollowers").append(data.maxNoOfFollowers);
			Profile currentProfile = HttpCall.getProfile(profileName);
			logger.append(currentProfile);

			if (currentProfile.getNoOfFollowers() <= data.maxNoOfFollowers){

				/*
				 * This is where actual liking and commenting happens
				 */
				boolean liked = _performLike(action);

				boolean commented = false;

				if (data.commentOnly || (report.getPhotosLiked() > 0 && report.getPhotosLiked() % data.getLikesToCommentRatio() == 0))
					commented = _performComment(action, profileName);

				// Sleep only if we have successfully liked or commented on a post
				if (liked || commented) {
					sleep(getRandomTime(data.timeMin, data.timeMax));
					action.counter++;
					newPostNotFoundCounter = 0;
				} else {
					newPostNotFoundCounter++;
						if (newPostNotFoundCounter > GENERAL_LIMIT) {
						_performOnHashTag(action, hashtag);
						return;
					}
				}
			}

			logger.log();

			boolean clickedNext = clickNext();
			if (!clickedNext) {
                logger.appendErr(data.username).appendErr("Right Arrow Not Found, Retrying Hashtag").err();
                rightArrowNotFoundCounter++;
                if (rightArrowNotFoundCounter > RIGHT_ARROW_NOT_FOUND_LIMIT)
					exceptionService.addException(new Exception("Issue with clicking RIGHT ARROW\n" + this.data));
                _performOnHashTag(action, hashtag);
                return;
            } else {
				rightArrowNotFoundCounter = 0;
			}
		}
	}

	private boolean _performLike(Action action) {
		if (isLikingBlocked || !action.like || isAlreadyLiked())
			return false;
		like(getLikeButton());
		if (!isAlreadyLiked()) {
			exceptionService.addException(new Exception("Issue with LIKE\n" + this.data));
			return false;
		}

		if (checkIfLikeIsBlocked()) {
			if (likedBlockedCounter > BLOCKED_LIMIT) {
			    logger.append("LIKING IS BLOCKED");
                isLikingBlocked = true;
                emailService.sendLikeIsBlockedEmail(this.data);
                return false;
            }
            likedBlockedCounter++;
            logger.append("LIKING MAY BE BLOCKED, Attempt " + likedBlockedCounter);
		}
		likedBlockedCounter = 0;

		report.incrementPhotoLiked();
		logger.append("liked");
		return true;
	}

	private boolean _performComment(Action action, String profileName) {
		if (isCommentingBlocked || action.comment && data.comments.size() > 0
				&& _isProfileNotVisitedAndAddToSet(profileName) && !isAlreadyCommented(data.username)) {
			_comment(_getRandomComment());
			if (!isAlreadyCommented(data.username)) {
				exceptionService.addException(new Exception("Issue with COMMENT\n" + this.data));
				return false;
			}

			if (checkIfCommentIsBlocked(data.username)) {
			    if (commentBlockedCounter > BLOCKED_LIMIT) {
                    logger.append("COMMENTING IS BLOCKED");
                    isCommentingBlocked = true;
                    emailService.sendCommentIsBlockedEmail(data);
                    return false;
                }
                commentBlockedCounter++;
			    logger.append("COMMENTING MAY BE BLOCKED, Attempt " + commentBlockedCounter);
			}
			commentBlockedCounter = 0;

			report.incrementPhotosCommented();
			logger.append("commented");
			return true;
		}
		return false;
	}

	/**
	 * Is it time to check if photo LIKING is BLOCKED for every <GENERAL_LIMIT> photos
	 * @return
	 */
	private boolean checkIfLikeIsBlocked() {
    	int photosLiked = report.getPhotosLiked();
    	return (photosLiked != 0) && (photosLiked % GENERAL_LIMIT == 0) && isLikeBlocked();
	}

	/**
	 * Is it time to Check if photo COMMENTING is BLOCKED for every <GENERAL_LIMIT> photos
	 * @return
	 */
	private boolean checkIfCommentIsBlocked(String username) {
		int photosLiked = report.getPhotosLiked();
		return (photosLiked != 0) && (photosLiked % GENERAL_LIMIT == 0) && isCommentBlocked(username);
	}

	private void _gotoHashTagPage(String hashtag) {
        hashtag = hashtag.toLowerCase();
		getDriver().get(ConfigData.HASHTAG_URL + hashtag);
    }

	private boolean _isProfileNotVisitedAndAddToSet(String profileName) {
		if (this.alreadyVisited.contains(profileName))
			return false;
		else
			this.alreadyVisited.add(profileName);
		return true;
	}

	private void _comment(String comment) {
		if (getCommentInput() == null)
			return;
		comment(getCommentInput(), comment);
		sleep(2);
		comment(getCommentInput(), Keys.ENTER);
		sleep(2);
		waitForCommentToLoad();
		logger.append(data).append("comment").append(comment);
	}

	private String _getRandomComment() {
		int index = ThreadLocalRandom.current().nextInt(0, data.comments.size());
		return data.comments.get(index);
	}
}
