package instagram.pages;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SuperPage {
	
	private final String heartCss = "span.coreSpriteHeartOpen";
	private final String rightArrowCss = ".coreSpriteRightPaginationArrow";
	private final String profileNameCss = "._eeohz";
	private final String errorCss = ".error-container";
	
	private WebDriverWait wait;
	private WebDriver driver;
	
	private static final int WAIT_TIME = 10;
	
	public SuperPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, WAIT_TIME);
		PageFactory.initElements(driver, this);
	}
	
	public WebDriver getDriver() {
		return this.driver;
	}
	
	public void setWait(int seconds) {
		wait = new WebDriverWait(driver, seconds);
	}
	
	public WebElement getElement(String css) {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(css)));
		} catch (Exception e) {
			return null;
		}
	}
	
	public WebElement getElement(WebElement element, String css) {
		try {
			return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(element, By.cssSelector(css)));
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<WebElement> getElements(String css) {
		try {
			return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(css)));
		} catch (Exception e) {
			return Collections.emptyList();
		}	
	}
	
	@SuppressWarnings("unused")
	public List<WebElement> getElements(WebElement element, String css) {
		try {
			return wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(element, By.cssSelector(css)));
		} catch (Exception e) {
			return Collections.emptyList();
		}	
	}
	
	public void waitForCommentToLoad() {
		waitForElementToBeInvisible("textarea[disabled]");
	}
	
	public boolean isPageNotFound() {
		setWait(2);
		boolean isNotFound = getElement(errorCss) != null;
		setWait(WAIT_TIME);
		
		if (isNotFound)
			System.out.println("Page Not Found: " + getDriver().getCurrentUrl());
		
		return isNotFound;
	}
	
	public void waitForElementToBeInvisible(String css) {
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(css)));
		} catch (Exception e) {
			
		}
	}
	
	public int getRandomTime(int low, int high) {
		return ThreadLocalRandom.current().nextInt(low, high);
	}
	
	public List<WebElement> getLikeButtons() {
		return getElements(heartCss);
	}
	
	public WebElement getLikeButton() {
		setWait(2);
		WebElement likeButton = getElement(heartCss);
		setWait(WAIT_TIME);
		return likeButton;
	}
	
	public WebElement getRightNavArrow() {
		return getElement(rightArrowCss);
	}
	
	public String getProfileName() {
		return getElement(profileNameCss).getText();
	}
	
	public String getProfileName(WebElement element) {
		return getElement(element, profileNameCss).getText();
	}
	
	public WebElement getCommentInput() {
		return getElement("textarea[placeholder*='comment']");
	}
	
	public String getCommentsAsText() {
		return getElement("._b0tqa").getText();
	}
	
	public void comment(WebElement element, Object text) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.click();
		actions.sendKeys(text.toString());
		actions.build().perform();
	}
	
	public Object executeJs(String script, WebElement element) {
		return ((JavascriptExecutor) driver)
		        .executeScript(script, element);
	}
	
	public Object executeJs(String script) {
		return ((JavascriptExecutor) driver)
        .executeScript(script);
	}
	
	public WebElement getParentElement(WebElement element) {
		return (WebElement) executeJs("return arguments[0].parentNode;", element);
	}
	
	public WebElement getParentElement(String tagName, WebElement element) {
		while (!element.getTagName().equalsIgnoreCase(tagName)) {
			element = getParentElement(element);
		}
		return element;
	}
	
	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public WebElement getFollowButton() {
		WebElement button = getElement("button");
		if (button != null && button.getText().equals("Follow"))
			return button;
		return null;
	}
	
	public boolean isFollowButtonVisible() {
		WebElement button = getElement("button");
		return button != null && button.getText().equals("Follow");
	}
	
	public boolean isUnfollowButtonVisible() {
		WebElement button = getElement("button");
		return button != null && button.getText().equals("Following");
	}
	
	public WebElement getUnfollowButton() {
		WebElement button = getElement("button");
		if (button != null && button.getText().equals("Following"))
			return button;
		return null;
	}

}
