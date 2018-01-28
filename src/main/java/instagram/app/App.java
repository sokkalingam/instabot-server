package instagram.app;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import instagram.data.Data;
import instagram.pages.HomePage;
import instagram.pages.LoginPage;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        driver.get("http://www.instagram.com");
        LoginPage loginPage = new LoginPage(driver);
        
    	/**
         * DATA YOU NEED TO SET
         */
        String username = "USERNAME";
        String password = "PASSWORD";
        String hashtag = "HASHTAG";
        int noOfPhotos = 5;
        int minTime = 20;
        int maxTime = 30;
        List<String> comments = Arrays.asList(
					        		"Woof! Woof!",
					        		"So Cute",
					        		"Awww"
				        		);
        /**
         * END OF DATA SETUP
         */
    	
    	HomePage homePage = loginPage.login(username, password);
        
        /**
         * Like Photos on your feed
         * Parameters: No of Photos, Min wait time, Max wait time
         */
//         homePage.performLikesOnProfile(noOfPhotos, minTime, maxTime);
        
        /**
         * Like and comment on hashtag
         * Parameters: Hashtag, No of Photos, Min wait time, Max wait time
         */
        Data.comments = comments;
        homePage.likeOnlyOnHashTag(username, hashtag, noOfPhotos, minTime, maxTime);
//        homePage.commentOnlyOnHashTag(username, hashtag, noOfPhotos, minTime, maxTime);
//        homePage.likeAndCommentOnHashTag(username, hashtag, noOfPhotos, minTime, maxTime);
    }
}
