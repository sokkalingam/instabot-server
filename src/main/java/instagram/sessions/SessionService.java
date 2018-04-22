package instagram.sessions;

import instagram.model.Data;
import instagram.model.Session;
import instagram.report.ReportManager;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class SessionService {

    private Map<String, Session> activeSessions;

    public SessionService() {
        activeSessions = new HashMap<>();
    }

    public synchronized boolean isSessionActive(String sessionId) {
        return activeSessions.containsKey(sessionId);
    }

    public synchronized boolean addNewSession(Data data, WebDriver driver) {
        if (!isSessionActive(data.sessionId)) {
            Session session = new Session();
            session.setData(data);
            session.setDriver(driver);
            activeSessions.put(data.sessionId, session);
            return true;
        }
        return false;
    }

    public synchronized void removeSession(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public Map<String, Session> getActiveSessions() {
        return activeSessions;
    }

    public boolean killSession(String sessionId) {
        if (!isSessionActive(sessionId))
            return false;
        Session session = activeSessions.get(sessionId);
        if (session != null) {
            WebDriver driver = session.getDriver();
            if (driver != null)
                driver.quit();
            ReportManager.clearReport(session.getData().username);
        }
        activeSessions.remove(sessionId);
        return true;
    }
}
