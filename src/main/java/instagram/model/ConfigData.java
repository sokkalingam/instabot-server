package instagram.model;

public class ConfigData {

    public final static String BASE_URL = "https://www.instagram.com";
    public final static String HASHTAG_URL = BASE_URL + "/explore/tags/";

    public final static String DRIVER_PROPERTY_NAME = "webdriver.chrome.driver";
//    public final static String DRIVER_PROPERTY_VALUE = "src/main/resources/chromedriver";
    public final static String DRIVER_PROPERTY_VALUE = System.getProperty("user.home") + "/chromedriver";

}
