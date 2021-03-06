package instagram.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Data {

	public String username;
	public String password;

	@NotEmpty(message = "Hashtags list cannot be empty")
	public List<String> hashtags = new ArrayList<>();

	@NotNull @Min(value = 1, message = "No of photos should be greater than 0")
	public Integer noOfPhotos;

	@Min(value = 1, message = "Spam like count should be greater than 0")
	public Integer spamLikeCount;

	@NotNull @Min(value = 0, message = "Wait time cannot be negative")
	public Integer timeMin;

	@NotNull @Min(value = 0, message = "Wait time cannot be negative")
	public Integer timeMax;

	@NotNull @Min(value = 0, message = "Max number of followers cannot be negative")
	public Integer maxNoOfFollowers;
	public Integer minFollowersRequiredToNotUnfollow;

	@NotEmpty
	public String sessionId;

	public Set<String> protectedProfiles = new HashSet<>();
	public List<String> comments = new ArrayList<>();

	public Integer noOfMostRecentPhotos;

	@NotNull @Min(value = 0, message = "Number of times to loop should be positive")
	public Integer noOfTimesToLoop;

	public Integer maxNoOfProfilesToUnfollow;

	public void setHashtags(List<String> list) {
		hashtags = list.stream().map(item -> item.toLowerCase()).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return "Data{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", hashtag='" + hashtags + '\'' +
				", noOfPhotos=" + noOfPhotos +
				", spamLikeCount=" + spamLikeCount +
				", timeMin=" + timeMin +
				", timeMax=" + timeMax +
				", maxFollowersRequiredToFollow=" + maxNoOfFollowers +
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
