package instagram.factory;

import instagram.data.Data;
import instagram.utils.NavigationUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory {

    public static WebDriver getLoggedInDriver() {
        System.setProperty(Data.DRIVER_PROPERTY_NAME, Data.DRIVER_PROPERTY_VALUE);
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        NavigationUtils.setup(driver);
        return driver;
    }
}
