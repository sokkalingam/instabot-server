package instagram.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WelcomeController {

    @RequestMapping("/welcome")
    public String welcome() {
        return "Welcome to INSTABOT API!";
    }

}
