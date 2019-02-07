package instagram.sessions;

import instagram.email.EmailService;
import instagram.messages.ResponseMessages;
import instagram.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private EmailService emailService;

    @RequestMapping("/list")
    public Map<String, Session> getActiveSessions() {
        return sessionService.getActiveSessions();
    }

    @RequestMapping(value = "/kill", method = RequestMethod.POST)
    public String killSession(@RequestBody String sessionId) {
        Session session = sessionService.killSession(sessionId);
        if (session == null)
            return ResponseMessages.SESSION_DOES_NOT_EXIST.toString();

        emailService.sendJobAbortedEmail(session.getData());
        return ResponseMessages.SESSION_ABORTED.toString();
    }

    @RequestMapping("/killAll")
    public void killAllSessions() {
        sessionService.killAllSessions();
    }

}
