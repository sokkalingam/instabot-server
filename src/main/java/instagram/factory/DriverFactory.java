package instagram.factory;

import instagram.model.Data;
import instagram.utils.ConfigPropertyUtils;
import instagram.utils.NavigationUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Arrays;

public class DriverFactory {

    public static WebDriver getLoggedInDriver(Data data) {
        System.setProperty("webdriver.chrome.driver", ConfigPropertyUtils.getDriverPath());
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(Arrays.asList(
           "--headless",
                "--no-sandbox"
        ));
        WebDriver driver = new ChromeDriver(chromeOptions);
        NavigationUtils.setup(driver, data.sessionId);
        return driver;
    }
}
