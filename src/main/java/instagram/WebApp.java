package instagram;

import instagram.instabot.InstabotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class WebApp {

    @Autowired
    private InstabotService instabotService;

    public static void main(String[] args) throws IOException {
//        Runtime.getRuntime().exec("src/main/resources/shell_scripts/before_startup.sh");
        SpringApplication.run(WebApp.class, args);
    }

    @PostConstruct
    public void postConstruct() {
        instabotService.startup();
    }

}