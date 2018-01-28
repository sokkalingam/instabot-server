package instagram.http;

import org.springframework.web.client.RestTemplate;

import instagram.data.Data;
import instagram.model.Profile;
import instagram.utils.ProfileUtils;

public class HttpCall {
	
	public static String getResponse(String url) {
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(url, String.class);
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
			profile.setFollowers(ProfileUtils.getNumberCount(followers));
			String following = profileText.split("Followers, ")[1].split(" ")[0];
			profile.setFollowing(ProfileUtils.getNumberCount(following));
			String posts = profileText.split("Following, ")[1].split(" ")[0];
			profile.setPosts(ProfileUtils.getNumberCount(posts));
		} catch (Exception e) {
			System.out.println("Exception in getting profile details");
		}
		return profile;
	}

}
