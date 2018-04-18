package instagram.utils;

import java.io.IOException;
import java.util.Properties;

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

    public static String get(String property) {
        return properties.getProperty(property);
    }
}
