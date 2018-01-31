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
	
	public void performLikesOnProfile(int n, int timeMin, int timeMax) throws InterruptedException {
		sleep(3);
		System.out.println("\nLiking Photos on your profile, " + n + " photos, Wait time between " + timeMin + " and " + timeMax + " seconds");
		
		List<WebElement> likeButtons = getLikeButtons();
		int count = 0;
		while (likeButtons.size() != 0) {
			if (count >= n)
				return;
			WebElement likeButton = likeButtons.get(0);
			likeButton.click();
			System.out.println((++count) + ") " + getProfileName(getParentElement("article", likeButton)));
			sleep(getRandomTime(timeMin, timeMax));
			likeButtons = getLikeButtons();
		}
	}
	
	public void likeOnlyOnHashTag(String accountName, String hashtagName, int noOfPhotos, int timeMin, int timeMax) {
		_performOnHashTag(accountName, hashtagName, noOfPhotos, timeMin, timeMax, true, false, false, 0);
	}
	
	public void commentOnlyOnHashTag(String accountName, String hashtagName, int noOfPhotos, int timeMin, int timeMax) {
		_performOnHashTag(accountName, hashtagName, noOfPhotos, timeMin, timeMax, false, true, false, 0);
	}
	
	public void likeAndCommentOnHashTag(String accountName, String hashtagName, int noOfPhotos, int timeMin, int timeMax) {
		_performOnHashTag(accountName, hashtagName, noOfPhotos, timeMin, timeMax, true, true, false, 0);
	}
	
	public void likeAndFollowOnHashTag(String accountName, String hashtagName, int noOfPhotos, int timeMin, int timeMax) {
		_performOnHashTag(accountName, hashtagName, noOfPhotos, timeMin, timeMax, true, false, true, 0);
	}
	
	public void likeCommentFollowOnHashTag(String accountName, String hashtagName, int noOfPhotos, int timeMin, int timeMax) {
		_performOnHashTag(accountName, hashtagName, noOfPhotos, timeMin, timeMax, true, true, true, 0);
	}
	
	private void _performOnHashTag(String accountName, String hashtagName, int noOfPhotos, int timeMin, int timeMax, boolean like, boolean comment, boolean follow, int counter) {
		
		if (!like && !comment && !follow)
			return;
		
		hashtagName = hashtagName.toLowerCase();
		getDriver().get(Data.HASHTAG_URL + hashtagName);
		sleep(3);
		
		System.out.println("#" + hashtagName + ", " + noOfPhotos + " photos, Wait time between " + timeMin + " and " + timeMax + " seconds");
		List<WebElement> photos = getDriver().findElements(By.cssSelector("a[href*='tagged="+ hashtagName +"']"));
		
		// Skip top posts and open the most recent
		int indexOfFirstMostRecentPhoto = 9;
		photos.get(indexOfFirstMostRecentPhoto).click();

		while (counter < noOfPhotos) {
			
			if (isPageNotFound()) {
				System.out.println("Retrying Hashtag");
				_performOnHashTag(accountName, hashtagName, noOfPhotos, timeMin, timeMax, like, comment, follow, counter);
				return;
			}
			
			String profileName = getProfileName();
			
			counter++;
			System.out.println("\n" + counter + ") " + profileName);
			System.out.println("Following: " + alreadyFollowing());
			Profile currentProfile = HttpCall.getProfile(profileName);
			System.out.println(currentProfile);
			
			if (like && !alreadyLiked()) {
				like(getLikeButton());
			} else {
				like = false;
			}
			
			if (comment && !alreadyCommented(accountName)) {
				comment(alreadyVisited, profileName, getRandomComment());
			} else {
				comment = false;
			}
				
			if (follow && !alreadyFollowing()) {
				follow(currentProfile);
			} else {
				follow = false;
			}
			
			if (like || comment || follow) {
				sleep(getRandomTime(timeMin, timeMax));
			}
						
			WebElement rightArrow = getRightNavArrow();
			if (rightArrow != null) {
				rightArrow.click();
			} else {
				System.out.println("Right Arrow Not Found, Retrying Hashtag");
				_performOnHashTag(accountName, hashtagName, noOfPhotos, timeMin, timeMax, like, comment, follow, counter);
				return;
			}
		}
	}
	
	public void like(WebElement likeButton) {
		likeButton.click();
		System.out.println("Liked ");
	}
	
	public void follow(Profile profile) {
		if (profile.getFollowers() > Data.maxFollowers)
			return;
		getFollowButton().click();
		System.out.println("Followed");
	}
	
	public void comment(Set<String> alreadyVisited, String profileName, String comment) {
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
	
	public String getRandomComment() {
		int index = ThreadLocalRandom.current().nextInt(0, Data.comments.size());
		return Data.comments.get(index);
	}
	
	public boolean alreadyLiked() {
		return getLikeButton() == null;
	}
	
	public boolean alreadyFollowing() {
		return !isFollowButtonVisible();
	}
	
	public boolean alreadyCommented(String accountName) {
		return getCommentsAsText().contains(accountName);
	}
	
}
