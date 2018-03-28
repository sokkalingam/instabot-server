package instagram.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import instagram.data.Data;
import instagram.http.HttpCall;
import instagram.model.Profile;

public class ProfilePage extends SuperPage {

	private String profileName;

	public ProfilePage(WebDriver driver, String profileName) {
		super(driver);
		getDriver().get(Data.BASE_URL + "/" + profileName);
		this.profileName = profileName;
	}

	private void _viewFollowing() {
		WebElement followingButton = getElement("a[href='/" + profileName + "/following/']");
		followingButton.click();
		WebElement followingPopup = getElement("._gs38e");
		long previousVal = -1;
		while (true) {
			long currentVal = (long) executeJs("return arguments[0].scrollTop = arguments[0].scrollHeight;",
					followingPopup);
			if (currentVal == previousVal)
				return;
			previousVal = currentVal;
			sleep(1);
		}

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
		    if (Data.maxNoOfProfilesToUnfollow-- <= 0)
		        return;
			Profile profile = HttpCall.getProfile(name);
			if (profile == null)
				continue;
			ProfilePage thisProfile = new ProfilePage(getDriver(), name);
			if (!thisProfile._isFollowingMe() && profile.getNoOfFollowers() < Data.minFollowersRequiredToNotUnfollow)
				unfollow(name);
		}
	}

	private void unfollow(String profileName) {
		if (Data.protectedProfiles.contains(profileName)) {
			System.out.println("Cannot unfollow protected profile " + profileName);
			return;
		}
		getUnfollowButton().click();
		sleep(getRandomTime(15, 25));
		System.out.println("Unfollowed: " + profileName);
	}

}
