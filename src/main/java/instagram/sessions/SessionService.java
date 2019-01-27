package instagram.sessions;

import instagram.email.EmailService;
import instagram.model.Data;
import instagram.model.Report;
import instagram.model.Session;
import instagram.report.ReportManager;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    @Autowired
    private EmailService emailService;

    private Map<String, Session> activeSessions;

    public SessionService() {
        activeSessions = new ConcurrentHashMap<>();
    }

    public synchronized boolean isSessionActive(String sessionId) {
        return activeSessions.containsKey(sessionId);
    }

    public synchronized Session addNewSession(Data data, WebDriver driver) {
        Session session = new Session();
        session.setData(data);
        session.setDriver(driver);
        activeSessions.put(data.sessionId, session);
        return session;
    }

    public synchronized void removeSession(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public synchronized Map<String, Session> getActiveSessions() {
        return activeSessions;
    }

    public Session killSession(String sessionId) {
        if (!isSessionActive(sessionId))
            return null;
        Session session = activeSessions.get(sessionId);
        if (session != null) {
            WebDriver driver = session.getDriver();
            if (driver != null)
                driver.quit();
            Report report = ReportManager.getReport(session.getData().username);
            if (report != null)
                report.setJobAsAborted();
        }
        removeSession(sessionId);
        return session;
    }

    public void killAllSessions() {
        System.out.println("Killing all active sessions");
        activeSessions.keySet().forEach(sessionId -> {
            Session session = killSession(sessionId);
            emailService.sendJobAbortedForMaintenanceEmail(session.getData());
        });
    }

}
