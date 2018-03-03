package instagram.http;

import org.springframework.web.client.RestTemplate;

import instagram.data.Data;
import instagram.model.Profile;
import instagram.utils.ProfileUtils;

public class HttpCall {

	public static String getResponse(String url) {
		RestTemplate restTemplate = new RestTemplate();
		String response = "";
		try {
			response = restTemplate.getForObject(url, String.class);
		} catch (Exception e) {
			System.out.println("Error fetching data for URL: " + url);
			return "";
		}
		return response;
	}

	public static String getProfileAsText(String profileName) {
		return getResponse(Data.BASE_URL + "/" + profileName);
	}

	public static Profile getProfile(String profileName) {
		Profile profile = new Profile();
		profile.setName(profileName);
		String profileText = getProfileAsText(profileName);
		try {
			String followers = profileText.split("meta content=\"")[1].split(" ")[0];
			profile.setNoOfFollowers(ProfileUtils.getNumberCount(followers));
			String following = profileText.split("Followers, ")[1].split(" ")[0];
			profile.setNoOfFollowing(ProfileUtils.getNumberCount(following));
			String posts = profileText.split("Following, ")[1].split(" ")[0];
			profile.setPosts(ProfileUtils.getNumberCount(posts));
		} catch (Exception e) {
			System.out.println("Exception in getting profile details");
			return profile;
		}
		System.out.println(profile);
		return profile;
	}

}
