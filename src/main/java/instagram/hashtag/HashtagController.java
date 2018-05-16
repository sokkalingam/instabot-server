package instagram.hashtag;

import instagram.factory.DriverFactory;
import instagram.messages.Messages;
import instagram.model.Data;
import instagram.model.Session;
import instagram.sessions.SessionService;
import instagram.utils.ThreadUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/hashtag")
public class HashtagController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private HashtagService hashtagService;

    @RequestMapping(value = "/loop", method = RequestMethod.POST)
    public String performActionsInLoop(@Valid @RequestBody Data data) {
        if (sessionService.isSessionActive(data.sessionId))
            return Messages.REQUEST_EXISTS.toString();
        Session session = sessionService.addNewSession(data, null);
        ThreadUtils.execute(new Thread(() -> {
            WebDriver driver = DriverFactory.getLoggedInDriver(data);
            session.setDriver(driver);
            hashtagService.performActionsInLoop(data, driver);
            sessionService.removeSession(data.sessionId);
        }));
        return Messages.REQUEST_ACCEPTED.toString();
    }

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public String likeHashTag(@Valid @RequestBody Data data) {
        if (sessionService.isSessionActive(data.sessionId))
            return Messages.REQUEST_EXISTS.toString();
        ThreadUtils.execute(new Thread(() -> {
            WebDriver driver = DriverFactory.getLoggedInDriver(data);
            sessionService.addNewSession(data, driver);
            hashtagService.likeHashTag(data, driver);
            sessionService.removeSession(data.sessionId);
        }));
        return Messages.REQUEST_ACCEPTED.toString();
    }

}
