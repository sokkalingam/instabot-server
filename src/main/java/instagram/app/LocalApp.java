package instagram.app;

import java.util.Date;

import instagram.factory.DriverFactory;
import instagram.utils.DataUtils;
import org.openqa.selenium.WebDriver;

import instagram.model.Data;
import instagram.pages.HomePage;

public class LocalApp {
	
	public static void main(String[] args) {

		Data data = new Data();

		data.sessionId = DataUtils.getSessionId();
		data.hashtags = DataUtils.getHashTags();
		data.noOfPhotos = DataUtils.getNoOfPhotos();
		data.timeMin = DataUtils.getTimeMin();
		data.timeMax = DataUtils.getTimeMax();
		data.maxNoOfFollowers = DataUtils.getMaxFollowers();
		data.noOfTimesToLoop = DataUtils.getNoOfLoop();
		data.comments = DataUtils.getComments();

		WebDriver driver = DriverFactory.getLoggedInDriver(data);

		HomePage homePage = new HomePage(driver, data);

//		homePage.likeHashtag();
		homePage.likeHashtagInLoop();

//		homePage.likeNewsFeed();
//		homePage.likeNewsFeedInLoop();
//		homePage.commentHashTag();
//		homePage.likeAndCommentHashTag();

		driver.quit();

		// Print Date Time in the end
		System.out.println(new Date());
		
	}
}
