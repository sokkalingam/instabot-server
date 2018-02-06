package instagram.app;

import java.util.Arrays;

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
		Data.hashtag = "dogsofinstagram"; // Enter the hash tag without #
		Data.noOfPhotos = 200; // No of Photos to like, comment, etc
		Data.timeMin = 10; // Minimum time to wait between actions such as like/comment
		Data.timeMax = 20; // Maximum time to wait between actions
		Data.minFollowers = 100;
		Data.maxFollowers = 100;
		Data.sessionId = "6272911777%3AMxmMUCQsGUqtKd%3A26";
		Data.comments = Arrays.asList(
					"Woof! Woof!",
					"sooo Cute..",
					"awwww..",
					"awwww so cute..",
					"so cuteee..",
					"such a cutie...",
					"very cute...",
					"adorable..",
					"lovely..."
				);
		/**
		 * END OF DATA SETUP
		 */
		
		System.setProperty(Data.DRIVER_PROPERTY_NAME, Data.DRIVER_PROPERTY_VALUE);
		WebDriver driver = new ChromeDriver();
		driver.get(Data.BASE_URL);
		driver.manage().addCookie(new Cookie("sessionid", Data.sessionId));

		HomePage homePage = new HomePage(driver);
		
		
//		homePage.likeNewsFeed();
		homePage.likeHashtag();
//		homePage.commentHashTag();
//		homePage.likeAndCommentHashTag();
//		homePage.likeAndFollowHashTag();
//		homePage.likeCommentFollowHashTag();
		
//		ProfilePage profilePage = new ProfilePage(driver, Data.username);
//		profilePage.unfollow();
		
	}
}
