package instagram.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openqa.selenium.WebDriver;

import java.util.Objects;

public class Session {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Session session = (Session) o;
        return Objects.equals(data, session.data);
    }

    @Override public int hashCode() {
        return Objects.hash(data);
    }
}
