package instagram.model;

public class ConfigData {

    public final static String BASE_URL = "https://www.instagram.com";
    public final static String HASHTAG_URL = BASE_URL + "/explore/tags/";

    public final static String DRIVER_PROPERTY_NAME = "webdriver.chrome.driver";

    // LOCAL
    public final static String DRIVER_PROPERTY_VALUE = "src/main/resources/chromedriver.exe";
    public final static String SCREENSHOTS_DEST = "screenshots";

    // AWS
//    public final static String DRIVER_PROPERTY_VALUE = "/home/ec2-user/bin/chromedriver";
//    public final static String SCREENSHOTS_DEST = "/home/ec2-user/screenshots";


}
