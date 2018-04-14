package instagram.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

public class ConfigPropertyUtils {

    private static Properties properties = new Properties();

    static {
        try {
            properties.load(ConfigPropertyUtils.class.getClassLoader().getResourceAsStream("properties/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDriverPath() {
        return properties.getProperty("driver_location");
    }
}
