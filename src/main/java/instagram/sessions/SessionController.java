package instagram.sessions;

import instagram.email.EmailService;
import instagram.messages.ResponseMessages;
import instagram.model.Data;
import instagram.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addSessionToQueue(@Valid @RequestBody Data data) {
        if (data == null)
            return ResponseMessages.REQUEST_INVALID.toString();
        if (sessionService.isSessionActive(data.sessionId))
            return ResponseMessages.REQUEST_EXISTS.toString();
        sessionService.addNewSession(data);
        return ResponseMessages.REQUEST_ACCEPTED.toString();
    }

}
