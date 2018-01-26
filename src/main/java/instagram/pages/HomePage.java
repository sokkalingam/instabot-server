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

public class HomePage extends SuperPage {
	
	private final String HASHTAG_URL = "https://www.instagram.com/explore/tags/";
	
	public HomePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
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
		_performOnHashTag(accountName, hashtagName, noOfPhotos, timeMin, timeMax, true, false, 0);
	}
	
	public void commentOnlyOnHashTag(String accountName, String hashtagName, int noOfPhotos, int timeMin, int timeMax) {
		_performOnHashTag(accountName, hashtagName, noOfPhotos, timeMin, timeMax, false, true, 0);
	}
	
	public void likeAndCommentOnHashTag(String accountName, String hashtagName, int noOfPhotos, int timeMin, int timeMax) {
		_performOnHashTag(accountName, hashtagName, noOfPhotos, timeMin, timeMax, true, true, 0);
	}
	
	private void _performOnHashTag(String accountName, String hashtagName, int noOfPhotos, int timeMin, int timeMax, boolean like, boolean comment, int counter) {
		
		if (!like && !comment)
			return;
		
		hashtagName = hashtagName.toLowerCase();
		getDriver().get(HASHTAG_URL + hashtagName);
		sleep(3);
		
		System.out.println("#" + hashtagName + ", " + noOfPhotos + " photos, Wait time between " + timeMin + " and " + timeMax + " seconds");
		List<WebElement> photos = getDriver().findElements(By.cssSelector("a[href*='tagged="+ hashtagName +"']"));
		
		// Skip top posts and open the most recent
		int indexOfFirstMostRecentPhoto = 9;
		photos.get(indexOfFirstMostRecentPhoto).click();
		
		Set<String> commentedProfiles = new HashSet<String>();

		while (counter < noOfPhotos) {
			
			if (isPageNotFound()) {
				System.out.println("Retrying Hashtag");
				_performOnHashTag(accountName, hashtagName, noOfPhotos, timeMin, timeMax, like, comment, counter);
			}
			
			String profileName = getProfileName();
			
			if ((like && !alreadyLiked()) || (comment && !alreadyCommented(accountName))) {
				sleep(getRandomTime(timeMin, timeMax));
				counter++;
				System.out.println("\n" + counter + ") " + profileName);
			}
			
			if (like && !alreadyLiked())
				like(getLikeButton());
			
			if (comment && !alreadyCommented(accountName))
				comment(commentedProfiles, profileName, getRandomComment());
						
			getRightNavArrow().click();
		}
	}
	
	public void like(WebElement likeButton) {
		likeButton.click();
		System.out.print("Liked ");
	}
	
	public void comment(Set<String> commentedProfiles, String profileName, String comment) {
		if (!commentedProfiles.contains(profileName)) {
			comment(getCommentInput(), comment);
			sleep(1);
			comment(getCommentInput(), Keys.ENTER);
			sleep(1);
			waitForCommentToLoad();
			System.out.print(" Commented: " + comment);
		}
	}
	
	public String getRandomComment() {
		int index = ThreadLocalRandom.current().nextInt(0, Data.comments.size());
		return Data.comments.get(index);
	}
	
	public boolean alreadyLiked() {
		return getLikeButton() == null;
	}
	
	public boolean alreadyCommented(String accountName) {
		return getCommentsAsText().contains(accountName);
	}
	
}
