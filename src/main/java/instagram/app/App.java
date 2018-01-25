package instagram.app;

import java.util.Arrays;

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
        
        // put your userName and password here
        HomePage homePage = loginPage.login("USERNAME", "PASSWORD");
        
        /**
         * Like Photos on your feed
         * Parameters: No of Photos, Min wait time, Max wait time
         */
        // homePage.performLikesOnProfile(200, 20, 30);
        
        /**
         * Like and comment on hashtag
         * Parameters: Hashtag, No of Photos, Min wait time, Max wait time
         */
        Data.comments = Arrays.asList(
        		"Woof! Woof!",
        		"So Cute",
        		"Awww");
        // homePage.likeOnlyOnHashTag("goldenretrieverpuppy", 3, 20, 30);
        // homePage.commentOnlyOnHashTag("goldenretrieverpuppy", 3, 20, 30);
        homePage.likeAndCommentOnHashTag("goldenretriever", 4, 20, 30);
    }
}
