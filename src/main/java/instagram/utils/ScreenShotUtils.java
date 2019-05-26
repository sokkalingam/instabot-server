package instagram.utils;

import instagram.logger.LogService;
import instagram.model.ConfigData;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;

public class ScreenShotUtils {

    private static LogService logger = new LogService();

    public static String captureScreenshot(WebDriver driver, String screenshotName) {

        try {
            TakesScreenshot ts = (TakesScreenshot)driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String dest = ConfigData.SCREENSHOTS_DEST + "/" + screenshotName + ".png";
            File destination = new File(dest);
            FileHandler.copy(source, destination);
            logger.append("Screenshot taken: " + dest).log();
            return dest;
        } catch (IOException e) {
            logger.append("Could not take screenshot: " + e).log();
            return e.getMessage();
        }
    }
    
}
