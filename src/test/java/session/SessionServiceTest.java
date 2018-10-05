package session;

import instagram.WebApp;
import instagram.model.Data;
import instagram.sessions.SessionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = WebApp.class)
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @Test
    public void sessionTest() {
        Data data = new Data();
        data.sessionId = "sessionOne";
        assertThat(sessionService.isSessionActive(data.sessionId), equalTo(false));
        assertThat(sessionService.addNewSession(data, null), is(notNullValue()));
        assertThat(sessionService.isSessionActive(data.sessionId), equalTo(true));
        sessionService.removeSession(data.sessionId);
        assertThat(sessionService.isSessionActive(data.sessionId), equalTo(false));
    }
}
