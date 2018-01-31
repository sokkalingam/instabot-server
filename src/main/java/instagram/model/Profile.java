package instagram.model;

public class Profile {
	
	private String name;
	private int posts;
	private int followers;
	private int following;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPosts() {
		return posts;
	}
	public void setPosts(int posts) {
		this.posts = posts;
	}
	public int getFollowers() {
		return followers;
	}
	public void setFollowers(int followers) {
		this.followers = followers;
	}
	public int getFollowing() {
		return following;
	}
	public void setFollowing(int following) {
		this.following = following;
	}
	@Override
	public String toString() {
		return "Profile [name=" + name + ", posts=" + posts + ", followers=" + followers + ", following=" + following
				+ "]";
	}

}
