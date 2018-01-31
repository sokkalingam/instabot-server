package instagram.app;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import instagram.data.Data;
import instagram.pages.ProfilePage;

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
        driver.get(Data.BASE_URL);
        driver.manage().addCookie(new Cookie("sessionid", "6272911777%3AMxmMUCQsGUqtKd%3A26"));
//        LoginPage loginPage = new LoginPage(driver);
        driver.navigate().refresh();
        
    	/**
         * DATA YOU NEED TO SET
         */
        String username = "goldenpawsofecho";
        String password = "Chennai1990!";
        String hashtag = "dogsofinstagram";
        int noOfPhotos = 200;
        int minTime = 10;
        int maxTime = 20;
        Data.maxFollowers = 100;
        List<String> comments = Arrays.asList(
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
    	
//    	HomePage homePage = loginPage.login(username, password);
    	ProfilePage profilePage = new ProfilePage(driver, username);
    	profilePage.getFollowingList();
        
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
//        homePage.likeOnlyOnHashTag(username, hashtag, noOfPhotos, minTime, maxTime);
//        homePage.commentOnlyOnHashTag(username, hashtag, noOfPhotos, minTime, maxTime);
//        homePage.likeAndCommentOnHashTag(username, hashtag, noOfPhotos, minTime, maxTime);
//        homePage.likeAndFollowOnHashTag(username, hashtag, noOfPhotos, minTime, maxTime);
        
    }
}
