package instagram.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

	@NotNull(message = "Please provide Max No of Followers")
	@Min(value = 0, message = "Max number of followers should be a positive number")
	public Integer maxNoOfFollowers;

	public Integer minFollowersRequiredToNotUnfollow;

	@NotEmpty (message = "Session Id cannot be empty")
	public String sessionId;

	public List<String> comments = new ArrayList<>();

	public void setHashtags(List<String> list) {
		hashtags = list.stream().map(String::toLowerCase).collect(Collectors.toList());
	}

	public boolean commentOnly;

	// Ex: If value is 5. For every 5 photos liked, 1 comment will be added
	@Min(value = 1, message = "Likes to Comment Ratio cannot be less than 1")
	private Integer likesToCommentRatio;

	public Integer getLikesToCommentRatio() {
		return likesToCommentRatio == null ? 1 : likesToCommentRatio;
	}

	public void setLikesToCommentRatio(Integer likesToCommentRatio) {
		this.likesToCommentRatio = likesToCommentRatio;
	}

	@Override
	public String toString() {
		return "Data{" +
				"username='" + username + '\'' +
				", hashtag='" + hashtags + '\'' +
				", noOfPhotos=" + noOfPhotos +
				", maxFollowersRequiredToFollow=" + maxNoOfFollowers +
				", comments=" + comments +
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
