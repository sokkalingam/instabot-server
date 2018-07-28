package instagram.pages;

import java.util.ArrayList;
import java.util.List;

import instagram.model.ConfigData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import instagram.model.Data;
import instagram.http.HttpCall;
import instagram.model.Profile;

public class ProfilePage extends SuperPage {

	private String profileName;
	private Data data;

	public ProfilePage(WebDriver driver, Data data) {
		super(driver);
		this.data = data;
		getDriver().get(ConfigData.BASE_URL + "/" + profileName);
		this.profileName = data.username;
	}

	public ProfilePage(WebDriver driver, Data data, String profileName) {
		super(driver);
		this.data = data;
		getDriver().get(ConfigData.BASE_URL + "/" + profileName);
		this.profileName = profileName;
	}

	private void _viewFollowing() {
		WebElement followingButton = getElement("a[href='/" + profileName + "/following/']");
		followingButton.click();
		WebElement followingPopup = getElement("._gs38e");
		scrollDownTillEnd(followingPopup);
	}

	private List<String> _getFollowingList() {
		_viewFollowing();
		List<String> list = new ArrayList<>();
		List<WebElement> profileNameElements = getElements("._2nunc > a");
		int size = profileNameElements.size();
		System.out.println("No of Following: " + size);
		for (int i = size - 1; i >= 0; i--) {
			list.add(profileNameElements.get(i).getText());
		}
		return list;
	}

	private boolean _isFollowingMe() {
		Object data = executeJs("return window._sharedData");
		String dataString = String.valueOf(data);
		String isFollowingMe = dataString.split("follows_viewer=")[1].split(",")[0];
		System.out.println("Following Me: " + isFollowingMe);
		return Boolean.parseBoolean(isFollowingMe);
	}

	public void unfollow() {
		for (String name : _getFollowingList()) {
		    if (data.maxNoOfProfilesToUnfollow-- < 0)
		        return;
			Profile profile = HttpCall.getProfile(name);
			if (profile == null)
				continue;
			ProfilePage thisProfile = new ProfilePage(getDriver(), data, name);
			if (!thisProfile._isFollowingMe() && profile.getNoOfFollowers() < data.minFollowersRequiredToNotUnfollow)
				unfollow(name);
		}
	}

	public void massLike(int count) {
	    sleep(3);
	    int counter = 1;
        List<WebElement> photos = getDriver().findElements(By.cssSelector("a[href*='taken-by=" + this.profileName + "']"));
        photos.get(0).click();
        for (int i = 0; i < photos.size(); i++) {
            if (counter > count)
                return;
            if (hasHashTag(data.hashtag)) {
                like(getLikeButton());
                System.out.println((++counter) + ") Liked " + getProfileName());
                sleep(3);
            }
            clickNext();
        }
	}

	private void unfollow(String profileName) {
		if (data.protectedProfiles.contains(profileName)) {
			System.out.println("Cannot unfollow protected profile " + profileName);
			return;
		}
		getUnfollowButton().click();
		sleep(getRandomTime(15, 25));
		System.out.println("Unfollowed: " + profileName);
	}

}
