package instagram.model;

public class Profile {
	
	private String name;
	private int posts;
	private int noOfFollowers;
	private int noOfFollowing;
	
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
	public int getNoOfFollowers() {
		return noOfFollowers;
	}
	public void setNoOfFollowers(int followers) {
		this.noOfFollowers = followers;
	}
	public int getNoOfFollowing() {
		return noOfFollowing;
	}
	public void setNoOfFollowing(int following) {
		this.noOfFollowing = following;
	}
	@Override
	public String toString() {
		return "Profile [name=" + name + ", posts=" + posts + ", noOfFollowers=" + noOfFollowers + ", noOfFollowing="
				+ noOfFollowing + "]";
	}
	

}
