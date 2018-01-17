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
        HomePage homePage = loginPage.login("USERNAME", "PASSWORD");
//        homePage.performLikesOnProfile();
        homePage.performLikesOnHashTag("HASHTAG", 1000);
    }
}
