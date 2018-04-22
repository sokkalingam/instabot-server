package instagram.sessions;

import instagram.model.Data;
import instagram.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @RequestMapping("/list")
    public Map<String, Session> getActiveSessions() {
        return sessionService.getActiveSessions();
    }

    @RequestMapping(value = "/kill", method = RequestMethod.POST)
    public boolean killSession(@RequestBody Data data) {
        return data != null && sessionService.killSession(data.sessionId);
    }

}
