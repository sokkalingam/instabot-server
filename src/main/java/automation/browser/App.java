package automation.browser;

import org.openqa.selenium.chrome.ChromeDriver;

import pages.HomePage;
import pages.LoginPage;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://www.instagram.com");
        LoginPage loginPage = new LoginPage(driver);
        
        // put your username and password here
        HomePage homePage = loginPage.login("USERNAME", "PASSWORD");
        
        // Like photos on your profile
        // Parameters: noOfPhotos to like, wait time min, wait time max
        homePage.performLikesOnProfile(100, 10, 20);
        
        // example: HappyFriday, Do not use # in front
        // Like photos for this hashtag for the given count
        // Parameters: hashtagName, noOfPhotos to like, wait time min, wait time max
        homePage.performLikesOnHashTag("HASHTAG_NAME", 100, 10, 20);
    }
}
