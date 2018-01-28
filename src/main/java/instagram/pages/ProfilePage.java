package instagram.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import instagram.data.Data;

public class ProfilePage extends SuperPage {
	
	private String profileName;
	
	public ProfilePage(WebDriver driver, String profileName) {
		super(driver);
		this.profileName = profileName;
		getDriver().get(Data.BASE_URL + "/" + profileName);
	}
	
	public void viewFollowing() {
		WebElement followingButton = getElement("a[href='/" + profileName + "/following/']");
		followingButton.click();
		WebElement followingPopup = getElement("._gs38e");
		long previousVal = -1;
		while (true) {
			long currentVal = (long) executeJs("return arguments[0].scrollTop = arguments[0].scrollHeight;", followingPopup);
			if (currentVal == previousVal)
				return;
			previousVal = currentVal;
			sleep(1);
		}
		
	}
	
	public List<String> getFollowingList() {
		viewFollowing();
		List<String> list = new ArrayList<>();
		List<WebElement> profileNameElements = getElements("._2nunc > a");
		System.out.println("No of Following: " + profileNameElements.size());
		for (WebElement element : profileNameElements) {
			System.out.println(element.getText());
			list.add(element.getText());
		}
		return list;
	}
	
	public void getProfileDetails() {
		
	}
	
	
	
	

}
