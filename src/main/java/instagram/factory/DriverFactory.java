package instagram.factory;

import instagram.data.Data;
import instagram.utils.NavigationUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory {

    public static WebDriver getLoggedInDriver(Data data) {
        System.setProperty(data.DRIVER_PROPERTY_NAME, data.DRIVER_PROPERTY_VALUE);
        WebDriver driver = new ChromeDriver();
        NavigationUtils.setup(driver, data.sessionId);
        return driver;
    }
}
