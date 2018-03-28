package instagram.app;

import java.util.Arrays;
import java.util.HashSet;

import instagram.pages.ProfilePage;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import instagram.data.Data;
import instagram.pages.HomePage;

public class App {
	
	public static void main(String[] args) {
		
		/**
		 * DATA YOU NEED TO SET
		 */
		Data.username = "myusername";
		Data.password = "mypassword";
		Data.hashtag = "hastagName"; // Enter the hash tag without #
		Data.noOfPhotos = 200; // No of Photos to like, comment, etc
		Data.timeMin = 10; // Minimum time to wait between actions such as like/comment
		Data.timeMax = 20; // Maximum time to wait between actions
        Data.maxNoOfProfilesToUnfollow = 10; // Max no of profiles to unfollow
		Data.minFollowersRequiredToNotUnfollow = 100; // User for unFollowing
		Data.maxFollowersRequiredToFollow = 100; // used for following
        Data.noOfMostRecentPhotos = 20; // No of most recent photos to like
        Data.noOfTimesToLoop = 20; // No of times to loop to like the most recent photos
		Data.sessionId = "SESSION_ID_FROM_APPLICATION_COOKIE_INSTAGRAM"; // session id for logging in
        // Comment will be picked randomly from the list
		Data.comments = Arrays.asList(
					"so cute...",
                    "what a pic...",
                    "this is so awesome..."
				);
		// Profile names mentioned in the list will not be unFollowed
		Data.protectedProfiles = new HashSet<>(Arrays.asList(
		                            "profileName1",
                                    "profileName2"
                                ));
		/**
		 * END OF DATA SETUP
		 */
		
		System.setProperty(Data.DRIVER_PROPERTY_NAME, Data.DRIVER_PROPERTY_VALUE);
		WebDriver driver = new ChromeDriver();
		driver.get(Data.BASE_URL);
		driver.manage().addCookie(new Cookie("sessionid", Data.sessionId));

		HomePage homePage = new HomePage(driver);
		homePage.likeNewsFeed();
		homePage.likeHashtag();
		homePage.commentHashTag();
		homePage.likeAndCommentHashTag();
		homePage.likeAndFollowHashTag();
		homePage.likeCommentFollowHashTag();
		
		ProfilePage profilePage = new ProfilePage(driver, Data.username);
		profilePage.unfollow();
		
	}
}
