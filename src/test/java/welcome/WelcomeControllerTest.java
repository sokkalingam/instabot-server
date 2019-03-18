package welcome;

import instagram.WebApp;
import org.hamcrest.Matchers;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = WebApp.class)
public class WelcomeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @org.junit.Test
    public void welcomeTest() {
        assertThat(this.restTemplate.getForObject("/api/welcome", String.class),
                equalTo("Welcome to INSTABOT API!"));
    }
}
