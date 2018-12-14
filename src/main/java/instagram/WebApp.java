package instagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebApp {

    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }

}