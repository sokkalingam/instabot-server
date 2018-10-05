package instagram.utils;

import java.io.IOException;
import java.util.Properties;

public class ConfigPropertyUtils {

    private static Properties properties;

    static {
        properties = PropertyUtils.loadFile("properties/config.properties");
    }

    public static String getDriverPath() {
        return properties.getProperty("driver_location");
    }

    public static String get(String property) {
        return properties.getProperty(property);
    }
}
