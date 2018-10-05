package instagram.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {

    public static Properties loadFile(String filePath) {
        Properties properties = new Properties();
        try {
            properties.load(ConfigPropertyUtils.class.getClassLoader().getResourceAsStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Exception in loading property file for path " + filePath);
        }
        return properties;
    }

}
