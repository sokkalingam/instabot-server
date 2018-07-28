package instagram.controllers;

import instagram.factory.DriverFactory;
import instagram.model.Data;
import instagram.pages.HomePage;
import instagram.services.SessionService;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hashtag")
public class HashtagController {

    @Autowired
    private SessionService sessionService;

    @RequestMapping(value = "/looplike", method = RequestMethod.POST)
    public String likeHashtag(@RequestBody Data data) {
        if (sessionService.isSessionActive(data.sessionId))
            return "Job is already running for your session. Please wait till your current job is complete";
        new Thread(() -> {
            try {
                WebDriver driver = DriverFactory.getLoggedInDriver(data);
                HomePage homePage = new HomePage(driver, data);
                homePage.likeInLoop();
                driver.quit();
            } catch (Throwable e) {
                
            }
            sessionService.removeSession(data.sessionId);
        }).start();
        return "Triggered a new job for your session";
    }

}
