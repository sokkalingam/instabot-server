package pages;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
	
	private WebDriver driver;
	
	private WebDriverWait wait;
	
	private final String HASHTAG_URL = "https://www.instagram.com/explore/tags/";
	
	public HomePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, 2);
		PageFactory.initElements(driver, this);
	}
	
	public void performLikesOnProfile(int n, int timeMin, int timeMax) throws InterruptedException {
		_sleep(3);
		System.out.println("\nLiking Photos on your profile, " + n + " photos, Wait time between " + timeMin + " and " + timeMax + " seconds");
		
		List<WebElement> likeButtons = _getLikeButtons();
		int count = 0;
		while (likeButtons.size() != 0) {
			if (count >= n)
				return;
			WebElement likeButton = likeButtons.get(0);
			likeButton.click();
			System.out.println((++count) + ") " + _getProfileName(_getParentElement("article", likeButton)));
			_sleep(_getRandomTime(timeMin, timeMax));
			likeButtons = _getLikeButtons();
		}
	}
	
	public void performLikesOnHashTag(String hashtagName, int noOfPhotos, int timeMin, int timeMax) throws InterruptedException {
		hashtagName = hashtagName.toLowerCase();
		driver.get(HASHTAG_URL + hashtagName);
		_sleep(3);
		
		System.out.println("\nLiking Photos for #" + hashtagName + ", " + noOfPhotos + " photos, Wait time between " + timeMin + " and " + timeMax + " seconds");
		List<WebElement> photos = driver.findElements(By.cssSelector("a[href*='tagged="+ hashtagName +"']"));
		photos.get(0).click();
	
		int i = 0;
		while (i < noOfPhotos) {
			WebElement likeButton = _getLikeButton();
			if (likeButton != null) {
				i++;
				System.out.println(i + ") " + _getProfileName());
				_sleep(_getRandomTime(timeMin, timeMax));
			}
			_getRightNavArrow().click();
		}
		
	}
	
	/* 
	 * PRIVATE AREA
	 */
	
	private final String heartCss = "span.coreSpriteHeartOpen";
	private final String rightArrowCss = ".coreSpriteRightPaginationArrow";
	private final String profileNameCss = "._eeohz";
	
	private WebElement _getElement(String css) {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(css)));
		} catch (Exception e) {
			return null;
		}
	}
	
	private WebElement _getElement(WebElement element, String css) {
		try {
			return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(element, By.cssSelector(css)));
		} catch (Exception e) {
			return null;
		}
	}
	
	private List<WebElement> _getElements(String css) {
		try {
			return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(css)));
		} catch (Exception e) {
			return Collections.emptyList();
		}	
	}
	
	@SuppressWarnings("unused")
	private List<WebElement> _getElements(WebElement element, String css) {
		try {
			return wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(element, By.cssSelector(css)));
		} catch (Exception e) {
			return Collections.emptyList();
		}	
	}
	
	private int _getRandomTime(int low, int high) {
		return ThreadLocalRandom.current().nextInt(low, high);
	}
	
	private List<WebElement> _getLikeButtons() {
		return _getElements(heartCss);
	}
	
	private WebElement _getLikeButton() {
		return _getElement(heartCss);
	}
	
	private WebElement _getRightNavArrow() {
		return _getElement(rightArrowCss);
	}
	
	private String _getProfileName() {
		return _getElement(profileNameCss).getText();
	}
	
	private String _getProfileName(WebElement element) {
		return _getElement(element, profileNameCss).getText();
	}
	
	private WebElement _getParentElement(WebElement element) {
		return (WebElement) ((JavascriptExecutor) driver)
		        .executeScript("return arguments[0].parentNode;", element);
	}
	
	private WebElement _getParentElement(String tagName, WebElement element) {
		while (!element.getTagName().equalsIgnoreCase(tagName)) {
			element = _getParentElement(element);
		}
		return element;
	}
	
	private void _sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
