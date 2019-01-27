package instagram.instabot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class InstabotController {

    @Autowired
    private InstabotService instabotService;

    @RequestMapping("/shutdown")
    public String shutdown() {
        instabotService.shutdown();
        return "Shutdown Complete!";
    }
}
