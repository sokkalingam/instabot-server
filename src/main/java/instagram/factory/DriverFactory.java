package instagram.factory;

import com.google.common.collect.ImmutableMap;
import instagram.model.Data;
import instagram.model.ConfigData;
import instagram.utils.NavigationUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class DriverFactory {

    public static WebDriver getLoggedInDriver(Data data) {
//        System.setProperty(ConfigData.DRIVER_PROPERTY_NAME, ConfigData.DRIVER_PROPERTY_VALUE);
        ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                        .usingDriverExecutable(new File(ConfigData.DRIVER_PROPERTY_VALUE))
                        .usingAnyFreePort()
                        .withEnvironment(ImmutableMap.of("DISPLAY",":20"))
                        .build();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver(chromeDriverService, chromeOptions);
        NavigationUtils.setup(driver, data.sessionId);
        return driver;
    }
}
