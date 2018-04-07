package instagram.app;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import instagram.factory.DriverFactory;
import org.openqa.selenium.WebDriver;

import instagram.model.Data;
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
		data.noOfPhotos = 5; // No of Photos to like, comment, etc
		data.timeMin = 20; // Minimum time to wait between actions such as like/comment
		data.timeMax = 30; // Maximum time to wait between actions
        data.maxNoOfProfilesToUnfollow = 100; // Max no of profiles to unfollow
		data.minFollowersRequiredToNotUnfollow = 2000; // User for unFollowing
		data.maxFollowersRequiredToFollow = 300; // used for following
        data.noOfTimesToLoop = 500; // No of times to loop to like the most recent photos
        data.spamLikeCount = 10;
		data.sessionId = "IGSCc41a967edb9a4c28b470c3ccdb000456e827fd604df10099289720ad2f217ac2%3ABNkO938iavftI1Uwl7cfBrlb2nDtFLgT%3A%7B%22_auth_user_id%22%3A6272911777%2C%22_auth_user_backend%22%3A%22accounts.backends.CaseInsensitiveModelBackend%22%2C%22_token%22%3A%226272911777%3ATdYPiMfj8Ud3fjASpYv0oGCTwQcNxOhW%3A3ac7ad993cc9412205c3d523793c39a55024acf2594d46e07643f6d0147ee6d5%22%2C%22_platform%22%3A4%2C%22_remote_ip%22%3A%2298.216.80.168%22%2C%22_mid%22%3A%22WqClsgAEAAEWTrL-rWhvXPkLVjf0%22%2C%22_user_agent_md5%22%3A%2256b529b7dc2c591fc7e2844c2dc739ff%22%2C%22_token_ver%22%3A2%2C%22last_refreshed%22%3A1523022383.0199723244%7D"; // session id for logging in
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
//		homePage.likeNewsFeed();
//		homePage.likeHashtag();
//		homePage.commentHashTag();
//		homePage.likeAndCommentHashTag();
//		homePage.likeAndFollowHashTag();
//		homePage.likeCommentFollowHashTag();
        homePage.likeInLoop();
//		homePage.spamLike();
//		homePage.unfollow();



		// Print Date Time in the end
		System.out.println(new Date());
		
	}
}
