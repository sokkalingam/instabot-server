package instagram;

import instagram.css.CssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class WebApp {

    @Autowired
    private CssService cssService;

    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }

    @PostConstruct
    public void postConstruct() {
        cssService.buildCssMap();
    }

}