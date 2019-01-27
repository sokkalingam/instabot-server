package instagram.hashtag;

import instagram.exceptions.ExceptionHelper;
import instagram.factory.DriverFactory;
import instagram.messages.ResponseMessages;
import instagram.model.Data;
import instagram.model.PresetData;
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
        try {
            return hashtagService.performActionsInLoop(data);
        } catch (Exception e) {
            ExceptionHelper.addException(e);
            return null;
        }
    }

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public String likeHashTag(@Valid @RequestBody Data data) {
        if (sessionService.isSessionActive(data.sessionId))
            return ResponseMessages.REQUEST_EXISTS.toString();
        ThreadUtils.execute(new Thread(() -> {
            WebDriver driver = DriverFactory.getLoggedInDriver(data);
            sessionService.addNewSession(data, driver);
            hashtagService.likeHashTag(data, driver);
            sessionService.removeSession(data.sessionId);
            driver.quit();
        }));
        return ResponseMessages.REQUEST_ACCEPTED.toString();
    }

}
