package instagram.pages;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

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

	public void likeNewsFeed() {
		sleep(3);
		System.out.println("\nLiking Photos on your profile, " + Data.noOfPhotos + " photos, Wait time between " + Data.timeMin + " and "
				+ Data.timeMax + " seconds");

		List<WebElement> likeButtons = getLikeButtons();
		int count = 0;
		while (likeButtons.size() != 0) {
			if (count >= Data.noOfPhotos)
				return;
			WebElement likeButton = likeButtons.get(0);
			likeButton.click();
			System.out.println((++count) + ") " + getProfileName(getParentElement("article", likeButton)));
			sleep(getRandomTime(Data.timeMin, Data.timeMax));
			likeButtons = getLikeButtons();
		}
	}

	public void likeHashtag() {
		_performOnHashTag(true, false, false, 0);
	}

	public void commentHashTag() {
		_performOnHashTag(false, true, false, 0);
	}

	public void likeAndCommentHashTag() {
		_performOnHashTag(true, true, false, 0);
	}

	public void likeAndFollowHashTag() {
		_performOnHashTag(true, false, true, 0);
	}

	public void likeCommentFollowHashTag() {
		_performOnHashTag(true, true, true, 0);
	}

	private void _performOnHashTag(boolean like, boolean comment, boolean follow, int counter) {

		if (!like && !comment && !follow)
			return;

		Data.hashtag = Data.hashtag.toLowerCase();
		getDriver().get(Data.HASHTAG_URL + Data.hashtag);
		sleep(3);

		System.out.println("#" + Data.hashtag + ", " + Data.noOfPhotos + " photos, Wait time between " + Data.timeMin + " and "
				+ Data.timeMax + " seconds");
		List<WebElement> photos = getDriver().findElements(By.cssSelector("a[href*='tagged=" + Data.hashtag + "']"));

		// Skip top posts and open the most recent
		int indexOfFirstMostRecentPhoto = 9;
		photos.get(indexOfFirstMostRecentPhoto).click();

		while (counter < Data.noOfPhotos) {

			if (isPageNotFound()) {
				_performOnHashTag(like, comment, follow, counter);
				return;
			}

			String profileName = getProfileName();

			counter++;
			System.out.println("\n" + counter + ") " + profileName);
			Profile currentProfile = HttpCall.getProfile(profileName);
			System.out.println("Am I following? - " + _alreadyFollowing());

			if (like && !_alreadyLiked()) {
				_like(getLikeButton());
			} else {
				like = false;
			}

			if (comment && !_alreadyCommented(Data.username)) {
				_comment(alreadyVisited, profileName, _getRandomComment());
			} else {
				comment = false;
			}

			if (follow && !_alreadyFollowing()) {
				_follow(currentProfile);
			} else {
				follow = false;
			}

			if (like || comment || follow) {
				sleep(getRandomTime(Data.timeMin, Data.timeMax));
			}

			WebElement rightArrow = getRightNavArrow();
			if (rightArrow != null) {
				rightArrow.click();
			} else {
				System.out.println("Right Arrow Not Found, Retrying Hashtag");
				_performOnHashTag(like, comment, follow, counter);
				return;
			}
		}
	}

	private void _like(WebElement likeButton) {
		likeButton.click();
		System.out.println("Liked ");
	}

	private void _follow(Profile profile) {
		if (profile.getNoOfFollowers() > Data.maxFollowersRequiredToFollow)
			return;
		getFollowButton().click();
		System.out.println("Followed");
	}

	private void _comment(Set<String> alreadyVisited, String profileName, String comment) {
		if (getCommentInput() == null || this.alreadyVisited.contains(profileName))
			return;
		comment(getCommentInput(), comment);
		sleep(2);
		comment(getCommentInput(), Keys.ENTER);
		sleep(2);
		waitForCommentToLoad();
		alreadyVisited.add(profileName);
		System.out.println("Commented: " + comment);
	}

	private String _getRandomComment() {
		int index = ThreadLocalRandom.current().nextInt(0, Data.comments.size());
		return Data.comments.get(index);
	}

	private boolean _alreadyLiked() {
		return getLikeButton() == null;
	}

	private boolean _alreadyFollowing() {
		return !isFollowButtonVisible();
	}

	private boolean _alreadyCommented(String accountName) {
		return getCommentsAsText().contains(accountName);
	}

}
