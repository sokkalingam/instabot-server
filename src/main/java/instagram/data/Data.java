package instagram.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Data {

	public final static String BASE_URL = "https://www.instagram.com";
	public final static String HASHTAG_URL = BASE_URL + "/explore/tags/";
	
	public final static String DRIVER_PROPERTY_NAME = "webdriver.chrome.driver";
	public final static String DRIVER_PROPERTY_VALUE = "src/main/resources/chromedriver.exe";
	
	public static String username;
	public static String password;
	public static String hashtag;
	public static int noOfPhotos;
	public static int spamLikeCount;
	public static int timeMin;
	public static int timeMax;
	public static int maxFollowersRequiredToFollow;
	public static int minFollowersRequiredToNotUnfollow;
	public static String sessionId;

	public static Set<String> protectedProfiles = new HashSet<>();
	public static List<String> comments = new ArrayList<>();

	public static int noOfMostRecentPhotos;
	public static int noOfTimesToLoop;
	public static int maxNoOfProfilesToUnfollow;
}
