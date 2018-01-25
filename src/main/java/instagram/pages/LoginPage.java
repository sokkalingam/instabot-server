package instagram.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends SuperPage {
	
	@FindBy(linkText = "Log in")
	private WebElement loginLink;
	
	@FindBy(name = "username")
	private WebElement usernameInput;
	
	@FindBy(name = "password")
	private WebElement passwordInput;
	
	@FindBy(tagName = "button")
	private WebElement loginButton;
	
	public LoginPage(WebDriver driver) {
		super(driver);
	}
	
	public HomePage login(String username, String password) throws InterruptedException {
		sleep(3);
		PageFactory.initElements(getDriver(), this);
		loginLink.click();
		PageFactory.initElements(getDriver(), this);
		new WebDriverWait(getDriver(), 5).until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
		fillIn(username, password);
		sleep(5);
		return new HomePage(getDriver());
	}
	
	public void fillIn(String username, String password) throws InterruptedException {
		usernameInput.sendKeys(username);
		passwordInput.sendKeys(password);
		loginButton.click();
	}

}
