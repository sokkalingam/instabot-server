package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
	
	private WebDriver driver;
	
	private final String HASHTAG_URL = "https://www.instagram.com/explore/tags/";
	
	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void performLikesOnProfile() throws InterruptedException {
		_sleep(3);
		List<WebElement> likeButtons = _getLikeButtons();
		int count = 0;
		System.out.println("Starting to like...");
		while (likeButtons.size() != 0) {
			likeButtons.get(0).click();
			System.out.println("Liked so far: " + ++count);
			_sleep(3);
			likeButtons = _getLikeButtons();
		}
	}
	
	public void performLikesOnHashTag(String hashtagName, int noOfPhotos) throws InterruptedException {
		hashtagName = hashtagName.toLowerCase();
		driver.get(HASHTAG_URL + hashtagName);
		_sleep(3);
		List<WebElement> photos = driver.findElements(By.cssSelector("a[href*='tagged="+ hashtagName +"']"));
		photos.get(0).click();
	
		for (int i = 0; i < noOfPhotos; i++) {
			_sleep(2);
			List<WebElement> likeButtons = _getLikeButtons();
			if (likeButtons.size() != 0) {
				likeButtons.get(0).click();
				System.out.println(i + ") " + _getProfileName());
			}
			_getRightNavArrow().click();
		}
		
	}
	
	private List<WebElement> _getLikeButtons() {
		return driver.findElements(By.cssSelector("span.coreSpriteHeartOpen"));
	}
	
	private WebElement _getRightNavArrow() {
		return driver.findElement(By.cssSelector(".coreSpriteRightPaginationArrow"));
	}
	
	private String _getProfileName() {
		return driver.findElement(By.cssSelector("._eeohz")).getText();
	}
	
	private void _sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
