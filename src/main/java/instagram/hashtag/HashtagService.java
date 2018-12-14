package instagram.hashtag;

import instagram.email.EmailService;
import instagram.model.Data;
import instagram.model.Report;
import instagram.model.enums.EmailSubjects;
import instagram.pages.HomePage;
import instagram.report.ReportService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class HashtagService {

    @Autowired
    private EmailService emailService;

    public void performActionsInLoop(Data data, WebDriver driver) {

        _removeEmptyComments(data);
        HomePage homePage = new HomePage(driver, data);
        if (data.commentOnly)
            homePage.commentHashtagInLoop();
        else
            homePage.likeAndCommentHashtagInLoop();
        emailService.sendJobFinishedEmail(data);
    }

    public void likeHashTag(Data data, WebDriver driver) {
        _removeEmptyComments(data);
        HomePage homePage = new HomePage(driver, data);
        homePage.likeHashtag();
    }

    private void _removeEmptyComments(Data data) {
        data.comments = data.comments.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

}
