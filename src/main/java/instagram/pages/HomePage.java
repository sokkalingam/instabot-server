package instagram.pages;

import instagram.factory.DriverFactory;
import instagram.http.HttpCall;
import instagram.model.*;
import instagram.report.ReportManager;
import instagram.utils.ThreadUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class HomePage extends SuperPage {

	private Set<String> alreadyVisited;
	private Data data;
	private Report report;

	public HomePage(WebDriver driver, Data data) {
		super(driver);
		this.data = data;
		PageFactory.initElements(driver, this);
		alreadyVisited = new HashSet<>();
		this.data.username = getUsername();
	}

	public void likeNewsFeedInLoop() {
		for (int i = 1; i <= data.noOfTimesToLoop; i++) {
			System.out.println("Loop #" + i);
			likeNewsFeed();
			refreshPage();
		}
	}

	public void likeNewsFeed() {
		sleep(3);
		System.out.println("\nLiking Photos on your profile, " + data.noOfPhotos + " photos, Wait time between " + data.timeMin + " and "
				+ data.timeMax + " seconds");

		List<WebElement> likeButtons = getLikeButtons();
		int count = 0;
		while (likeButtons.size() > 0) {
			for (WebElement likeButton : likeButtons) {
                if (count >= data.noOfPhotos)
                    return;
                count++;
                System.out.println((count) + ") " + getProfileName(getParentElement("article", likeButton)));
			    like(likeButton);
				randomSleep(data);
			}
			scrollDown(1000);
			likeButtons = getLikeButtons();
            System.out.println("Found more posts to Like: " + likeButtons.size());
		}
	}

	public void unfollow() {
		ProfilePage profilePage = new ProfilePage(getDriver(), data);
		profilePage.unfollow();
	}

	public void likeHashtag() {
	    data.hashtags.forEach(hashtag -> {
	    	report.setCurrentHashtag(hashtag);
			_performOnHashTag(Action.getLikeAction(), hashtag);
		});
	}

	public void commentHashTag() {
		data.hashtags.forEach(hashtag -> _performOnHashTag(Action.getCommentAction(), hashtag));
	}

	public void likeAndCommentHashTag() {
		data.hashtags.forEach(hashtag -> {
			report.setCurrentHashtag(hashtag);
			_performOnHashTag(Action.getLikeCommentAction(), hashtag);
		});
	}

	public void likeAndFollowHashTag() {
		data.hashtags.forEach(hashtag -> {
			Action action = new Action();
			action.like = true;
			action.follow = true;
			_performOnHashTag(action, hashtag);
		});
	}

	public void likeCommentFollowHashTag() {
		data.hashtags.forEach(hashtag -> {
			Action action = new Action();
			action.like = true;
			action.comment = true;
			action.follow = true;
			_performOnHashTag(action, hashtag);
		});
	}

	public void likeAndCommentHashtagInLoop() {
		report = ReportManager.getNewReport(data.username);
	    for (int i = 1; i <= data.noOfTimesToLoop; i++) {
            System.out.println("Loop #" + i);
            report.incrementCurrentLoop();
            likeAndCommentHashTag();
        }
		ReportManager.clearReport(data.username);
    }

    public void spamLike() {
        Action action = new Action();
        action.spamLike = true;
		data.hashtags.forEach(hashtag -> _performOnHashTag(action, hashtag));
    }

	private void _performOnHashTag(Action action, String hashtag) {

		if (!action.like && !action.comment && !action.follow && !action.spamLike)
			return;

		_gotoHashTagPage(hashtag);

		System.out.println("\n#" + hashtag + ", " + data.noOfPhotos + " photos, Wait time between " + data.timeMin + " and "
				+ data.timeMax + " seconds");
		scrollDown(1000);
		List<WebElement> photos = getElements("a[href*='tagged=" + hashtag + "']");

		// Skip top posts and open the most recent
		int indexOfFirstMostRecentPhoto = 9;

		try {
			photos.get(indexOfFirstMostRecentPhoto).click();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return;
		}

		while (action.counter < data.noOfPhotos) {

			if (isPageNotFound()) {
				_performOnHashTag(action, hashtag);
				return;
			}

			boolean wait = false;
			String profileName = getProfileName();

			System.out.println("\n" + (action.counter + 1) + ") " + profileName);
			Profile currentProfile = HttpCall.getProfile(profileName);
			System.out.println("Am I following? - " + _alreadyFollowing());

			if (currentProfile.getNoOfFollowers() <= data.maxNoOfFollowers){
				if (action.like && !isAlreadyLiked()) {
					like(getLikeButton());
					report.incrementPhotoLiked();
					wait = true;
				}

				if (action.comment && data.comments.size() > 0
						&& _isProfileNotVisited(profileName) && !_alreadyCommented(data.username)) {
					_comment(_getRandomComment());
					report.incrementPhotosCommented();
					wait = true;
				}

				if (action.follow && !_alreadyFollowing()) {
					_follow(currentProfile);
					wait = true;
				}

				if (action.spamLike
						&& currentProfile.getNoOfFollowing() > currentProfile.getNoOfFollowers()) {
					_spamLike(profileName, hashtag);
					wait = true;
				}

				if (wait) {
					sleep(getRandomTime(data.timeMin, data.timeMax));
					action.counter++;
				}
			}

			boolean clickedNext = clickNext();
			if (!clickedNext) {
                System.out.println("Right Arrow Not Found, Retrying Hashtag");
                _performOnHashTag(action, hashtag);
                return;
            }
		}
	}

	private void _spamLike(String profileName, String hashtag) {
        ThreadUtils.execute(
                new Thread(() -> {
                    WebDriver driver = DriverFactory.getLoggedInDriver(data);
                    ProfilePage profilePage = new ProfilePage(driver, data, profileName);
                    profilePage.massLike(data.spamLikeCount, hashtag);
                    driver.quit();
                })
        );
    }

	private void _gotoHashTagPage(String hashtag) {
        hashtag = hashtag.toLowerCase();
        getDriver().get(ConfigData.HASHTAG_URL + hashtag);
    }

	private void _follow(Profile profile) {
		if (profile == null || profile.getNoOfFollowers() > data.maxNoOfFollowers)
			return;
		getFollowButton().click();
		System.out.println("Followed");
	}

	private boolean _isProfileNotVisited(String profileName) {
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
		System.out.println("Commented: " + comment);
	}

	private String _getRandomComment() {
		int index = ThreadLocalRandom.current().nextInt(0, data.comments.size());
		return data.comments.get(index);
	}

	private boolean _alreadyFollowing() {
		return !isFollowButtonVisible();
	}

	private boolean _alreadyCommented(String accountName) {
		return getCommentsAsText().contains(accountName);
	}
}
