package instagram.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Data {

	public String username;
	public String password;
	public String hashtag;
	public int noOfPhotos;
	public int spamLikeCount;
	public int timeMin;
	public int timeMax;
	public int maxFollowersRequiredToFollow;
	public int minFollowersRequiredToNotUnfollow;
	public String sessionId;

	public Set<String> protectedProfiles = new HashSet<>();
	public List<String> comments = new ArrayList<>();

	public int noOfMostRecentPhotos;
	public int noOfTimesToLoop;
	public int maxNoOfProfilesToUnfollow;

	@Override
	public String toString() {
		return "Data{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", hashtag='" + hashtag + '\'' +
				", noOfPhotos=" + noOfPhotos +
				", spamLikeCount=" + spamLikeCount +
				", timeMin=" + timeMin +
				", timeMax=" + timeMax +
				", maxFollowersRequiredToFollow=" + maxFollowersRequiredToFollow +
				", minFollowersRequiredToNotUnfollow=" + minFollowersRequiredToNotUnfollow +
				", sessionId='" + sessionId + '\'' +
				", protectedProfiles=" + protectedProfiles +
				", comments=" + comments +
				", noOfMostRecentPhotos=" + noOfMostRecentPhotos +
				", noOfTimesToLoop=" + noOfTimesToLoop +
				", maxNoOfProfilesToUnfollow=" + maxNoOfProfilesToUnfollow +
				'}';
	}
}
