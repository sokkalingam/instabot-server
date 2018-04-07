package instagram.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import instagram.model.Data;

public class LoginPage extends SuperPage {

	@FindBy(linkText = "Log in")
	private WebElement loginLink;

	@FindBy(name = "username")
	private WebElement usernameInput;

	@FindBy(name = "password")
	private WebElement passwordInput;

	@FindBy(tagName = "button")
	private WebElement loginButton;

	private Data data;

	public LoginPage(WebDriver driver, Data data) {
		super(driver);
		this.data = data;
	}

	public HomePage login() {
		sleep(3);
		PageFactory.initElements(getDriver(), this);
		loginLink.click();
		PageFactory.initElements(getDriver(), this);
		new WebDriverWait(getDriver(), 5).until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
		_fillIn(data.username, data.password);
		sleep(5);
		return new HomePage(getDriver(), data);
	}

	private void _fillIn(String username, String password) {
		usernameInput.sendKeys(username);
		passwordInput.sendKeys(password);
		loginButton.click();
	}

}
