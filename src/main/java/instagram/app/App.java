package instagram.app;

import java.util.Arrays;
import java.util.HashSet;

import instagram.factory.DriverFactory;
import org.openqa.selenium.WebDriver;

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
		Data.maxFollowersRequiredToFollow = 1000; // used for following
        Data.noOfTimesToLoop = 20; // No of times to loop to like the most recent photos
        Data.spamLikeCount = 3;
		Data.sessionId = "SESSION_ID_FROM_APPLICATION_COOKIE_INSTAGRAM"; // session id for logging in
        // Comment will be picked randomly from the list
		Data.comments = Arrays.asList(
					"so cute...",
                    "such a cutie...",
                    "omg so cute...",
                    "awwww... so cute..."
				);
		// Profile names mentioned in the list will not be unFollowed
		Data.protectedProfiles = new HashSet<>(Arrays.asList(
		                            "profileName1",
                                    "profileName2"
                                ));
		/**
		 * END OF DATA SETUP
		 */

		WebDriver driver = DriverFactory.getLoggedInDriver();

		HomePage homePage = new HomePage(driver);
//		homePage.likeNewsFeed();
//		homePage.likeHashtag();
//		homePage.commentHashTag();
//		homePage.likeAndCommentHashTag();
//		homePage.likeAndFollowHashTag();
//		homePage.likeCommentFollowHashTag();
//        homePage.likeInLoop();
		homePage.spamLike();
//		ProfilePage profilePage = new ProfilePage(driver, "eatsumieat");
//		profilePage.massLike(20);
		
	}
}
