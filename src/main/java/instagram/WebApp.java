package instagram;

import instagram.utils.ConfigPropertyUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class WebApp {

    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
        _runDns();
    }

    private static void _runDns() {
        try {
            new ProcessBuilder(new String[]{ConfigPropertyUtils.get("git_path"),
                    "./src/main/resources/scripts/dns.sh", "instabot", "80"}).start();
        } catch (IOException e) {
            System.out.println("DNS could not be started\n" + e);
        }
    }

}
