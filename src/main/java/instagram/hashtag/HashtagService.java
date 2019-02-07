package instagram.hashtag;

import instagram.exceptions.ExceptionHelper;
import instagram.factory.DriverFactory;
import instagram.messages.ResponseMessages;
import instagram.model.Data;
import instagram.model.Session;
import instagram.pages.HomePageService;
import instagram.sessions.SessionService;
import instagram.utils.ThreadUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class HashtagService {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private HomePageService homePageService;

    public String performActionsInLoop(Data data) {
        if (data == null)
            return ResponseMessages.REQUEST_INVALID.toString();
        if (sessionService.isSessionActive(data.sessionId))
            return ResponseMessages.REQUEST_EXISTS.toString();
        Session session = sessionService.addNewSession(data, null);
        ThreadUtils.execute(new Thread(() -> {
            WebDriver driver = null;
            try {
                driver = DriverFactory.getLoggedInDriver(data);
                session.setDriver(driver);
                _removeEmptyComments(data);
                homePageService.performActionsInLoop(data, driver);
            } catch (Exception e) {
                ExceptionHelper.addException(e);
                e.printStackTrace();
            } finally {
                sessionService.removeSession(data.sessionId);
                if (driver != null) driver.quit();
            }
        }));
        return ResponseMessages.REQUEST_ACCEPTED.toString();
    }

    private void _removeEmptyComments(Data data) {
        data.comments = data.comments.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

}
