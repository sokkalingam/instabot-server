package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
	
	private WebDriver driver;
	
	@FindBy(linkText = "Log in")
	private WebElement loginLink;
	
	@FindBy(name = "username")
	private WebElement usernameInput;
	
	@FindBy(name = "password")
	private WebElement passwordInput;
	
	@FindBy(tagName = "button")
	private WebElement loginButton;
	
	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public HomePage login(String username, String password) throws InterruptedException {
		Thread.sleep(3000);
		PageFactory.initElements(driver, this);
		loginLink.click();
		PageFactory.initElements(driver, this);
		new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
		fillIn(username, password);
		return new HomePage(driver);
	}
	
	public void fillIn(String username, String password) throws InterruptedException {
		usernameInput.sendKeys(username);
		passwordInput.sendKeys(password);
		loginButton.click();
		Thread.sleep(1000);
		try {
			if (usernameInput.isDisplayed())
				fillIn(username, password);
		} catch (Exception e) {
			
		}
	}

}
