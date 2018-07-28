package instagram.utils;

import instagram.model.ConfigData;
import instagram.model.Data;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class NavigationUtils {

    public static void setup(WebDriver driver, String sessionId) {
        driver.get(ConfigData.BASE_URL);
        driver.manage().addCookie(new Cookie("sessionid", sessionId));
        driver.navigate().refresh();
    }

}
