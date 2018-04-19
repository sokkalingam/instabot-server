package instagram.utils;

import instagram.data.Data;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class NavigationUtils {

    public static void setup(WebDriver driver) {
        driver.get(Data.BASE_URL);
        driver.manage().addCookie(new Cookie("sessionid", Data.sessionId));
        driver.navigate().refresh();
    }

}
