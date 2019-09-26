package instagram.factory;

import instagram.logger.LogService;
import instagram.model.ConfigData;
import instagram.utils.ConfigPropertyUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Arrays;

public class DriverFactory {

    private static WebDriver driver;
    private static final String SESSION_ID_KEY = "sessionid";
    private static LogService logger = new LogService();

    private static synchronized void setupDriver() {

        if (driver == null) {
            System.setProperty("webdriver.chrome.driver", ConfigPropertyUtils.getDriverPath());

            ChromeOptions options = new ChromeOptions();
            options.addArguments(Arrays.asList(
                    "--headless",
                    "--no-sandbox",
//                    "--disable-infobars",
//                    "--disable-browser-side-navigation",
//                    "--disable-gpu",
                    "--start-maximized"
//                    "--ignore-certificate-errors"
            ));

            driver = new ChromeDriver(options);
            driver.get(ConfigData.BASE_URL);

            logger.append("New Driver Created").log();
        }
    }

    public static WebDriver getDriver(String sessionId) {
        setupDriver();
        setupCookie(sessionId);
        return driver;
    }

    public static void setupCookie(String sessionId) {
        Cookie sessionCookie = driver.manage().getCookieNamed(SESSION_ID_KEY);
        if (sessionCookie == null || !sessionId.equals(sessionCookie.getValue())) {
            driver.manage().deleteCookieNamed(SESSION_ID_KEY);
            driver.manage().addCookie(new Cookie(SESSION_ID_KEY, sessionId));
            logger.append("Session Cookie").append(sessionId).log();
            driver.navigate().refresh();
        }
    }

    public static synchronized void closeDriverIfOpen() {
        if (driver != null) {
            driver.quit();
            // Must set driver to null after quit
            // By default if driver is not null, it is assumed to be open for tasks
            driver = null;
            logger.append("Driver is closed").log();
        }
    }
}
