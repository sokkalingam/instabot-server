package instagram.hashtag;

import instagram.email.EmailService;
import instagram.model.Data;
import instagram.model.Report;
import instagram.pages.HomePage;
import instagram.report.ReportService;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HashtagService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ReportService reportService;

    public void performActionsInLoop(Data data, WebDriver driver) {

        HomePage homePage = new HomePage(driver, data);
        if (data.commentOnly)
            homePage.commentHashtagInLoop();
        else
            homePage.likeAndCommentHashtagInLoop();
        emailService.sendJobFinishedEmail(data);
    }

    public void likeHashTag(Data data, WebDriver driver) {
        HomePage homePage = new HomePage(driver, data);
        homePage.likeHashtag();
    }

}
