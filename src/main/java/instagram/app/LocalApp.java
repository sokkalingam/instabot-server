package instagram.app;

import java.util.Arrays;
import java.util.Date;

import instagram.factory.DriverFactory;
import org.openqa.selenium.WebDriver;

import instagram.model.Data;
import instagram.pages.HomePage;

public class LocalApp {
	
	public static void main(String[] args) {

		Data data = new Data();

		/**
		 * DATA YOU NEED TO SET
		 */

		// Instagram user name
		data.username = "username";

		// Instagram password
		data.password = "mypassword";

		/*
        	Session ID of the user
        	If sessionId is provided, no need of username and password

        	How to get sessionId?
        	Open Google Chrome, Login into Instagram with your userename and password
        	Press F12, Chrome Developer tools will open
        	Select the Application tab, Under Storage expand Cookies
        	From Cookies, Click on https://www.instagram.com
        	Find the value for the key sessionid
         */
		data.sessionId = "SESSION_ID";
		/*
			List of Hashtags
			For every hashtag, the app will like/comment for N number of photos. (N = 'noOfPhotos')
		 */
		data.hashtags = Arrays.asList(
						"travel",
						"weekend",
						"happyfriday"
						);

		// No of photos to like/comment
		data.noOfPhotos = 2;

		// Minimum time to wait between actions such as like/comment
		data.timeMin = 1;

		// Maximum time to wait between actions
		data.timeMax = 2;

		// Perform actions of photos only whose profiles have followers less than maxNoOfFollowers
		data.maxNoOfFollowers = 300;

		// No of times to loop on the most recent photos. Used in likeInLoop() and likeNewsFeedInLoop()
        data.noOfTimesToLoop = 2;

        // No of photos to like in a profile when using spamLike()
        data.spamLikeCount = 5;

		// List of comments. Comment will be picked randomly from the list
		data.comments = Arrays.asList(
					"so cute..!",
                    "such a cutie!",
                    "omg so cute!",
                    "awwww... so cute!"
				);

		/**
		 * END OF DATA SETUP
		 */

		WebDriver driver = DriverFactory.getLoggedInDriver(data);

		HomePage homePage = new HomePage(driver, data);

//		homePage.likeHashtag();
		homePage.likeHashtagInLoop();
		
//		homePage.likeNewsFeed();
//		homePage.likeNewsFeedInLoop();
//		homePage.commentHashTag();
//		homePage.likeAndCommentHashTag();

		driver.quit();

		// Print Date Time in the end
		System.out.println(new Date());
		
	}
}
