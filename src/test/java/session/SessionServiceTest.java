package session;

import instagram.WebApp;
import instagram.services.SessionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = WebApp.class)
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @Test
    public void sessionTest() {
        String session1 = "sessionOne";
        assertThat(sessionService.isSessionActive(session1), equalTo(false));
        assertThat(sessionService.isSessionActive(session1), equalTo(true));
        sessionService.removeSession(session1);
        assertThat(sessionService.isSessionActive(session1), equalTo(false));
    }
}
