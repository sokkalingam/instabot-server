package instagram.pages;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import instagram.data.CssData;
import instagram.model.Data;
import instagram.model.enums.CSS;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SuperPage {

	private WebDriverWait wait;
	private WebDriver driver;

	private static final int WAIT_TIME = 10;

	protected void superPage(WebDriver driver) {
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
		waitForElementToBeInvisible(CssData.get(CSS.COMMENT_INPUT_DISABLED));
	}

	protected boolean isPageNotFound() {
		setWait(1);
		boolean isNotFound = getElement(CssData.get(CSS.ERROR)) != null;
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
		return getElements(CssData.get(CSS.LIKE_BUTTON));
	}

	protected WebElement getLikeButton() {
		return _getButton(CssData.get(CSS.LIKE_BUTTON));
	}

	protected WebElement getUnlikeButton() {
		return _getButton(CssData.get(CSS.UNLIKE_BUTTON));
	}

	private WebElement _getButton(String css) {
		setWait(1);
		WebElement likeButton = getElement(css);
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
//		System.out.println("Scrolling Down by " + yOffset);
		executeJs("window.scrollBy(0, "+ yOffset +")");
	}

	protected void like(WebElement element) {
		if (element == null)
			return;
        moveToElement(element);
		element.click();
//		System.out.println("Liked");
	}

	protected boolean isAlreadyLiked() {
		return getUnlikeButton() != null;
	}

	protected boolean isAlreadyCommented(String username) {
		return getCommentsAsText().contains(username);
	}

	/**
	 * Refresh page and check if the post is already liked
	 * @return
	 */
	protected boolean isLikeBlocked() {
		sleep(1);
//		System.out.println("Checking if liking is blocked");
		refreshPage();
		return !isAlreadyLiked();
	}

	/**
	 * Refresh page and check if the post has your comment
	 * @return
	 */
	protected boolean isCommentBlocked(String username) {
		sleep(1);
//		System.out.println("Checking if commenting is blocked");
		refreshPage();
		return !isAlreadyCommented(username);
	}

	protected WebElement getRightNavArrow() {
		return getElement(CssData.get(CSS.RIGHT_ARROW));
	}

	protected String getProfileName() {
		return getText(getElement(CssData.get(CSS.PROFILE_NAME)));
	}

	protected WebElement getCommentInput() {
		return getElement(CssData.get(CSS.COMMENT_INPUT));
	}

	protected String getCommentsAsText() {
		return getText(getElement(CssData.get(CSS.COMMENTS)));
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

    protected boolean clickNext() {
        WebElement rightArrow = getRightNavArrow();
        if (rightArrow != null) {
        	// We sometimes get webdriver exception, hence clicking through JS
        	executeJs("arguments[0].click();", rightArrow);
            return true;
        } else {
            return false;
        }
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
//		System.out.println("Page Refreshed");
	}

	protected boolean isSessionValid(String username) {
		username = username.toLowerCase();
		return getElement("a[href*='/" + username + "/']") != null;
	}

}
