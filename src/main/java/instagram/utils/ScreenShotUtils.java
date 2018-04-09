package instagram.utils;

import instagram.model.ConfigData;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;

public class ScreenShotUtils {

    public static String captureScreenshot(WebDriver driver, String screenshotName){

        try {
            TakesScreenshot ts = (TakesScreenshot)driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String dest = ConfigData.SCREENSHOTS_DEST + "/" + screenshotName + ".png";
            File destination = new File(dest);
            FileHandler.copy(source, destination);
            System.out.println("Screenshot taken: " + dest);
            return dest;
        } catch (IOException e) {
            System.out.println("Could not take screenshot: " + e);
            return e.getMessage();
        }
    }
    
}
