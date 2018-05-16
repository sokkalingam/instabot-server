package instagram.hashtag;

import instagram.model.Data;
import instagram.pages.HomePage;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
public class HashtagService {

    public void performActionsInLoop(Data data, WebDriver driver) {
        HomePage homePage = new HomePage(driver, data);
        if (data.commentOnly)
            homePage.commentHashtagInLoop();
        else
            homePage.likeAndCommentHashtagInLoop();
        driver.quit();
    }

    public void likeHashTag(Data data, WebDriver driver) {
        HomePage homePage = new HomePage(driver, data);
        homePage.likeHashtag();
        driver.quit();
    }

}
