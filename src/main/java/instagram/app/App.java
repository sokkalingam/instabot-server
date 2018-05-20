package instagram.app;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import instagram.factory.DriverFactory;
import instagram.pages.SuperPage;
import org.openqa.selenium.WebDriver;

import instagram.data.Data;
import instagram.pages.HomePage;

public class App {
	
	public static void main(String[] args) {
		
		/**
		 * DATA YOU NEED TO SET
		 */
		Data data = new Data();
		data.username = "goldenpawsofecho";
		data.password = "mypassword";
		data.hashtag = "dogsofinstagram"; // Enter the hash tag without #
		data.noOfPhotos = 25; // No of Photos to like, comment, etc
		data.timeMin = 25; // Minimum time to wait between actions such as like/comment
		data.timeMax = 30; // Maximum time to wait between actions
        data.maxNoOfProfilesToUnfollow = 100; // Max no of profiles to unfollow
		data.minFollowersRequiredToNotUnfollow = 2000; // User for unFollowing
		data.maxFollowersRequiredToFollow = 300; // used for following
        data.noOfTimesToLoop = 500; // No of times to loop to like the most recent photos
        data.spamLikeCount = 10;
		data.sessionId = "SESSION_ID_FROM_BROWSER_AFTER_LOGIN"; // session id for logging in
        // Comment will be picked randomly from the list
		data.comments = Arrays.asList(
					"so cute..!",
                    "such a cutie!",
                    "omg so cute!",
                    "awwww... so cute!"
				);
		// Profile names mentioned in the list will not be unFollowed
		data.protectedProfiles = new HashSet<>(Arrays.asList(
		                            "profilename1",
                                    "profilename2"
                                ));
		/**
		 * END OF DATA SETUP
		 */

		WebDriver driver = DriverFactory.getLoggedInDriver(data);

		HomePage homePage = new HomePage(driver, data);
//		homePage.likeNewsFeedInLoop();
		homePage.likeNewsFeed();
//		homePage.likeHashtag();
//		homePage.commentHashTag();
//		homePage.likeAndCommentHashTag();
//		homePage.likeAndFollowHashTag();
//		homePage.likeCommentFollowHashTag();
//        homePage.likeInLoop();
//		homePage.spamLike();
//		homePage.unfollow();



		// Print Date Time in the end
		System.out.println(new Date());
		
	}
}
