package instagram.pages;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import instagram.factory.DriverFactory;
import instagram.model.Action;
import instagram.utils.ThreadUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import instagram.data.Data;
import instagram.http.HttpCall;
import instagram.model.Profile;

public class HomePage extends SuperPage {

	private Set<String> alreadyVisited;

	public HomePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		alreadyVisited = new HashSet<String>();
	}

	public void likeNewsFeedInLoop() {
		for (int i = 1; i <= Data.noOfTimesToLoop; i++) {
			System.out.println("Loop #" + i);
			likeNewsFeed();
			refreshPage();
		}
	}

	public void likeNewsFeed() {
		sleep(3);
		System.out.println("\nLiking Photos on your profile, " + Data.noOfPhotos + " photos, Wait time between " + Data.timeMin + " and "
				+ Data.timeMax + " seconds");

		List<WebElement> likeButtons = getLikeButtons();
		int count = 0;
		while (likeButtons.size() > 0) {
			for (WebElement likeButton : likeButtons) {
                if (count >= Data.noOfPhotos)
                    return;
                count++;
                System.out.println((count) + ") " + getProfileName(getParentElement("article", likeButton)));
			    like(likeButton);
				randomSleep();
			}
			scrollDown(1000);
			likeButtons = getLikeButtons();
            System.out.println("Found more posts to Like: " + likeButtons.size());
		}
	}

	public void unfollow() {
		ProfilePage profilePage = new ProfilePage(getDriver(), Data.username);
		profilePage.unfollow();
	}

	public void likeHashtag() {
	    Action action = new Action();
	    action.like = true;
	    _performOnHashTag(action);
	}

	public void commentHashTag() {
        Action action = new Action();
        action.comment = true;
        _performOnHashTag(action);
	}

	public void likeAndCommentHashTag() {
        Action action = new Action();
        action.like = true;
        action.comment = true;
        _performOnHashTag(action);
	}

	public void likeAndFollowHashTag() {
        Action action = new Action();
        action.like = true;
        action.follow = true;
        _performOnHashTag(action);
	}

	public void likeCommentFollowHashTag() {
        Action action = new Action();
        action.like = true;
        action.comment = true;
        action.follow = true;
        _performOnHashTag(action);
	}

	public void likeInLoop() {
	    for (int i = 1; i <= Data.noOfTimesToLoop; i++) {
            System.out.println("Loop #" + i);
            likeHashtag();
        }
    }

    public void spamLike() {
        Action action = new Action();
        action.spamLike = true;
        _performOnHashTag(action);
    }

	private void _performOnHashTag(Action action) {

		if (!action.like && !action.comment && !action.follow && !action.spamLike)
			return;

		_gotoHashTagPage();

		System.out.println("#" + Data.hashtag + ", " + Data.noOfPhotos + " photos, Wait time between " + Data.timeMin + " and "
				+ Data.timeMax + " seconds");
		scrollDown(1000);
		List<WebElement> photos = getElements("a[href*='tagged=" + Data.hashtag + "']");

		// Skip top posts and open the most recent
		int indexOfFirstMostRecentPhoto = 9;
		photos.get(indexOfFirstMostRecentPhoto).click();

		while (action.counter < Data.noOfPhotos) {

			if (isPageNotFound()) {
				_performOnHashTag(action);
				return;
			}

			boolean wait = false;
			String profileName = getProfileName();

			System.out.println("\n" + (action.counter + 1) + ") " + profileName);
			Profile currentProfile = HttpCall.getProfile(profileName);
			System.out.println("Am I following? - " + _alreadyFollowing());

			if (currentProfile.getNoOfFollowers() <= Data.maxFollowersRequiredToFollow){
				if (action.like && !isAlreadyLiked()) {
					like(getLikeButton());
					wait = true;
				}

				if (action.comment && _isProfileNotVisited(profileName) && !_alreadyCommented(Data.username)) {
					_comment(_getRandomComment());
					wait = true;
				}

				if (action.follow && !_alreadyFollowing()) {
					_follow(currentProfile);
					wait = true;
				}

				if (action.spamLike
						&& currentProfile.getNoOfFollowing() > currentProfile.getNoOfFollowers()) {
					_spamLike(profileName);
					wait = true;
				}

				if (wait) {
					sleep(getRandomTime(Data.timeMin, Data.timeMax));
					action.counter++;
				}
			}

			boolean clickedNext = clickNext();
			if (!clickedNext) {
                System.out.println("Right Arrow Not Found, Retrying Hashtag");
                _performOnHashTag(action);
                return;
            }
		}

		ThreadUtils.shutdown();
		ThreadUtils.awaitTermination();
	}

	private void _spamLike(String profileName) {
        ThreadUtils.getExecutorService().execute(
                new Thread(() -> {
                    WebDriver driver = DriverFactory.getLoggedInDriver();
                    ProfilePage profilePage = new ProfilePage(driver, profileName);
                    profilePage.massLike(Data.spamLikeCount);
                    driver.quit();
                })
        );
    }

	private void _gotoHashTagPage() {
        Data.hashtag = Data.hashtag.toLowerCase();
        getDriver().get(Data.HASHTAG_URL + Data.hashtag);
        sleep(3);
    }

	private void _follow(Profile profile) {
		if (profile == null || profile.getNoOfFollowers() > Data.maxFollowersRequiredToFollow)
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
		int index = ThreadLocalRandom.current().nextInt(0, Data.comments.size());
		return Data.comments.get(index);
	}

	private boolean _alreadyFollowing() {
		return !isFollowButtonVisible();
	}

	private boolean _alreadyCommented(String accountName) {
		return getCommentsAsText().contains(accountName);
	}
}
