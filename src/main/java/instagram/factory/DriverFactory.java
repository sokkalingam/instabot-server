package instagram.factory;

import instagram.model.Data;
import instagram.utils.ConfigPropertyUtils;
import instagram.utils.NavigationUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    public static WebDriver getLoggedInDriver(Data data) {
        System.setProperty("webdriver.gecko.driver", ConfigPropertyUtils.getDriverPath());

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        options.setLogLevel(FirefoxDriverLogLevel.FATAL);

        WebDriver driver = new FirefoxDriver(options);

        NavigationUtils.setup(driver, data.sessionId);
        return driver;
    }
}
