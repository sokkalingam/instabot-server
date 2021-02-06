package instagram.pages;

import instagram.data.CssData;
import instagram.logger.LogService;
import instagram.model.enums.CSS;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class SuperPage {

	private WebDriverWait wait;
	private WebDriver driver;
	private LogService logger;

	private static final int WAIT_TIME = 10;

	protected void superPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, WAIT_TIME);
		logger = new LogService();
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

	protected List<WebElement> getElementsPresent(String css) {
		try {
			return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(css)));
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	/**
	 * Get photos
	 * Photos are not hidden inside a div and hence can't be found by visibilityOfAllElementsLocatedBy
	 * @return
	 */
	protected List<WebElement> getPhotos() {
		try {
			return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("img")));
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
		if (low > high) high = low + 1;
		if (low == high) high++;
		return ThreadLocalRandom.current().nextInt(low, high);
	}

	protected List<WebElement> getLikeButtons() {
		return getElements(CssData.get(CSS.LIKE_BUTTON));
	}

	protected WebElement getLikeButton() {
		WebElement likeButton = _getButton(CssData.get(CSS.LIKE_BUTTON));
		return getElement(likeButton, "[aria-label=Like]") != null ? likeButton : null;
	}

	protected WebElement getUnlikeButton() {
		WebElement unlikeButton = _getButton(CssData.get(CSS.UNLIKE_BUTTON));
		return getElement(unlikeButton, "[aria-label=Unlike]") != null ? unlikeButton : null;
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

	protected void click(WebElement element) {
		if (element == null)
			return;
		moveToElement(element);
		executeJs("arguments[0].scrollIntoView();", element);
		executeJs("arguments[0].click();", element);

	}

	protected void scrollDown(int yOffset) {
//		System.out.println("Scrolling Down by " + yOffset);
		executeJs("window.scrollBy(0, "+ yOffset +")");
	}

	protected void like(WebElement element) {
		click(element);
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
		return getText(getElement(getElement(CssData.get(CSS.ARTICLE)), CssData.get(CSS.PROFILE_NAME)));
	}

	protected WebElement getCommentInput() {
		return getElement(CssData.get(CSS.COMMENT_INPUT));
	}

	protected String getCommentsAsText() {
		return getText(getElement(CssData.get(CSS.ARTICLE)));
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
        	click(rightArrow);
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
		WebElement element = getElement("a[href*='/" + username + "/']");
		logger.append(username).append("isSessionValid").append(element != null).log();
		return true;
	}

	protected void reportAProblem() {
		sleep(2);
		List<WebElement> buttons = getElementsPresent(CssData.get(CSS.REPORT_A_PROBLEM));
		setWait(WAIT_TIME);
		WebElement reportButton = null;
		for (WebElement button : buttons) {
			if (getText(button).equalsIgnoreCase("report a problem"))
				reportButton = button;
		}

		if (reportButton == null)
			return;

		click(reportButton);
	}

}
