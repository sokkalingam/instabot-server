package instagram.data;

import java.util.ArrayList;
import java.util.List;

public class Data {

	public final static String BASE_URL = "https://www.instagram.com";
	public final static String HASHTAG_URL = BASE_URL + "/explore/tags/";
	
	public final static String DRIVER_PROPERTY_NAME = "webdriver.chrome.driver";
	public final static String DRIVER_PROPERTY_VALUE = "src/main/resources/chromedriver.exe";
	
	public static String username;
	public static String password;
	public static String hashtag;
	public static int noOfPhotos;
	public static int timeMin;
	public static int timeMax;
	public static int maxFollowers;
	public static int minFollowers;
	public static String sessionId;

	public static List<String> allowedProfiles = new ArrayList<>();
	public static List<String> comments = new ArrayList<>();

}
