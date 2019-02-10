package instagram.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

public class Data {

	@Id
	private String _id;

	@NotEmpty(message = "Please provide your instagram username")
	public String username;
	public String password;

	public String email;

	@NotEmpty(message = "Hashtags list cannot be empty")
	public List<String> hashtags = new ArrayList<>();

	@NotNull(message = "Please provide No of Posts")
	@Min(value = 1, message = "No of photos should be greater than 0")
	public Integer noOfPhotos;

	@Min(value = 1, message = "Spam like count should be greater than 0")
	public Integer spamLikeCount;

	@NotNull(message = "Please provide Min Wait Time")
	@Min(value = 0, message = "Wait time cannot be negative")
	public Integer timeMin;

	@NotNull(message = "Please provide Max Wait Time")
	@Min(value = 0, message = "Wait time cannot be negative")
	public Integer timeMax;

	@NotNull(message = "Please provide Max No of Followers")
	@Min(value = 0, message = "Max number of followers should be a positive number")
	public Integer maxNoOfFollowers;

	public Integer minFollowersRequiredToNotUnfollow;

	@NotEmpty (message = "Session Id cannot be empty")
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

	public boolean commentOnly;

	// Ex: If value is 5. For every 5 photos liked, 1 comment will be added
	@Min(value = 1, message = "Likes to Comment Ratio cannot be less than 1")
	public Integer likesToCommentRatio;

	@Override
	public String toString() {
		return "Data{" +
				"username='" + username + '\'' +
				", hashtag='" + hashtags + '\'' +
				", noOfPhotos=" + noOfPhotos +
				", timeMin=" + timeMin +
				", timeMax=" + timeMax +
				", maxFollowersRequiredToFollow=" + maxNoOfFollowers +
				", comments=" + comments +
				", noOfTimesToLoop=" + noOfTimesToLoop +
				'}';
	}

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Data data = (Data) o;
        return Objects.equals(sessionId, data.sessionId);
    }

    @Override public int hashCode() {
        return Objects.hash(sessionId);
    }
}
