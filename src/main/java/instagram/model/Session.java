package instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openqa.selenium.WebDriver;

public class Session {

    private Data data;
    private WebDriver driver;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnore
    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}
