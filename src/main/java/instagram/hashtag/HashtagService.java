package instagram.hashtag;

import instagram.factory.DriverFactory;
import instagram.model.Data;
import instagram.pages.HomePage;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
public class HashtagService {

    public void likeHashTagInLoop(Data data, WebDriver driver) {
        HomePage homePage = new HomePage(driver, data);
        homePage.likeHashtagInLoop();
        driver.quit();
    }

    public void likeHashTag(Data data, WebDriver driver) {
        HomePage homePage = new HomePage(driver, data);
        homePage.likeHashtag();
        driver.quit();
    }

}
