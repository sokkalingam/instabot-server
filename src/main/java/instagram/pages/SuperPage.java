package instagram.pages;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import instagram.model.Data;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SuperPage {

	private final String heartCss = "span.Szr5J.ptsdu ";
	private final String rightArrowCss = "a.HBoOv._1bdSS";
	private final String profileNameCss = "a.FPmhX";
	private final String errorCss = ".error-container";

	private WebDriverWait wait;
	private WebDriver driver;

	private static final int WAIT_TIME = 10;

	protected SuperPage(WebDriver driver) {
		setDriver(driver);
	}

	protected void setDriver(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, WAIT_TIME);
		PageFactory.initElements(driver, this);
	}

	protected WebDriver getDriver() {
		return this.driver;
	}

	protected void setWait(int seconds) {
		wait = new WebDriverWait(driver, seconds);
	}

	protected WebElement getElement(String css) {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(css)));
		} catch (Exception e) {
			return null;
		}
	}

	protected WebElement getElement(WebElement element, String css) {
		try {
			return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(element, By.cssSelector(css)));
		} catch (Exception e) {
			return null;
		}
	}

	protected List<WebElement> getElements(String css) {
		try {
			return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(css)));
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	protected List<WebElement> getElements(WebElement element, String css) {
		try {
			return wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(element, By.cssSelector(css)));
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	protected void waitForCommentToLoad() {
		waitForElementToBeInvisible("textarea[disabled]");
	}

	protected boolean isPageNotFound() {
		setWait(2);
		boolean isNotFound = getElement(errorCss) != null;
		setWait(WAIT_TIME);

		if (isNotFound)
			System.out.println("Page Not Found: " + getDriver().getCurrentUrl());

		return isNotFound;
	}

	protected void waitForElementToBeInvisible(String css) {
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(css)));
		} catch (Exception e) {

		}
	}

	protected int getRandomTime(int low, int high) {
		if (low < 0) low = 0;
		if (low > high) return low;
		if (low == high) high++;
		return ThreadLocalRandom.current().nextInt(low, high);
	}

	protected List<WebElement> getLikeButtons() {
		return getElements(heartCss);
	}

	protected WebElement getLikeButton() {
		setWait(2);
		WebElement likeButton = getElement(heartCss);
		setWait(WAIT_TIME);
		return likeButton;
	}

	protected void scrollDownTillEnd(WebElement element) {
		long previousVal = -1;
		while (true) {
			long currentVal = (long) executeJs("return arguments[0].scrollTop = arguments[0].scrollHeight;",
					element);
			if (currentVal == previousVal)
				return;
			previousVal = currentVal;
			sleep(1);
		}
	}

	protected void scrollDown(int yOffset) {
		System.out.println("Scrolling Down by " + yOffset);
		executeJs("window.scrollBy(0, "+ yOffset +")");
	}

	protected void like(WebElement element) {
		if (element == null)
			return;
        moveToElement(element);
		element.click();
		System.out.println("Liked");
	}

	protected boolean isAlreadyLiked() {
		return getLikeButton() == null;
	}

	protected WebElement getRightNavArrow() {
		return getElement(rightArrowCss);
	}

	protected String getProfileName() {
		return getText(getElement(profileNameCss));
	}

	protected String getProfileName(WebElement element) {
		return getElement(element, profileNameCss).getText();
	}

	protected WebElement getCommentInput() {
		return getElement("textarea[placeholder*='comment']");
	}

	protected String getCommentsAsText() {
		return getText(getElement("._b0tqa"));
	}

	protected String getText(WebElement element) {
		try {
			return element != null ? element.getText() : "";
		} catch (StaleElementReferenceException e) {
			System.out.println("Stale Element, " + e);
			return "";
		}
	}

	protected void comment(WebElement element, Object text) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.click();
		actions.sendKeys(text.toString());
		actions.build().perform();
	}

	protected void moveToElement(WebElement element) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.build().perform();
	}

	protected Object executeJs(String script, WebElement element) {
		return ((JavascriptExecutor) driver).executeScript(script, element);
	}

	protected Object executeJs(String script) {
		return ((JavascriptExecutor) driver).executeScript(script);
	}

	protected WebElement getParentElement(WebElement element) {
		return (WebElement) executeJs("return arguments[0].parentNode;", element);
	}

	protected WebElement getParentElement(String tagName, WebElement element) {
		while (!element.getTagName().equalsIgnoreCase(tagName)) {
			element = getParentElement(element);
		}
		return element;
	}

	protected void sleep(int seconds) {
		try {
            System.out.println("Sleeping for " + seconds + "s...");
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

    protected void randomSleep(Data data) {
        sleep(getRandomTime(data.timeMin, data.timeMax));
    }

    protected boolean clickNext() {
        WebElement rightArrow = getRightNavArrow();
        if (rightArrow != null) {
            rightArrow.click();
            return true;
        } else {
            return false;
        }
    }

	protected WebElement getFollowButton() {
		WebElement button = getElement("button");
		if (button != null && button.getText().equals("Follow"))
			return button;
		return null;
	}

	protected boolean isFollowButtonVisible() {
		WebElement button = getElement("button");
		return button != null && button.getText().equals("Follow");
	}

	protected boolean isUnfollowButtonVisible() {
		WebElement button = getElement("button");
		return button != null && button.getText().equals("Following");
	}

	protected WebElement getUnfollowButton() {
		WebElement button = getElement("button");
		if (button != null && button.getText().equals("Following"))
			return button;
		return null;
	}

	protected boolean hasHashTag(String hashtag) {
		return getCommentsAsText().contains(hashtag);
	}

	protected void refreshPage() {
		getDriver().navigate().refresh();
		System.out.println("Page Refreshed");
	}

	protected String getUsername() {
		WebElement element = getElement("a.gmFkV");
		String username = getText(element);
		System.out.println("Username: " + username);
		return username;
	}

}
