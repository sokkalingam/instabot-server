package instagram.factory;

import instagram.data.Data;
import instagram.utils.NavigationUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    public static WebDriver getLoggedInDriver(Data data) {
        System.setProperty(data.DRIVER_PROPERTY_NAME, data.DRIVER_PROPERTY_VALUE);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        NavigationUtils.setup(driver, data.sessionId);
        return driver;
    }
}
