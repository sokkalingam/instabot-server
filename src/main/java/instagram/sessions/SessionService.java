package instagram.sessions;

import instagram.email.EmailService;
import instagram.logger.LogService;
import instagram.model.Data;
import instagram.model.Report;
import instagram.model.Session;
import instagram.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class SessionService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ReportService reportService;

    private LogService logger;

    private Map<String, Session> activeSessions;

    public SessionService() {
        logger = new LogService();
        activeSessions = new LinkedHashMap<>();
    }

    public synchronized boolean isSessionActive(String sessionId) {
        return activeSessions.containsKey(sessionId);
    }

    public synchronized Session addNewSession(Data data) {
        Session session = new Session();
        session.setData(data);
        activeSessions.put(data.sessionId, session);
        reportService.getNewReport(data);
        logger.append("New Session").append(data).log();
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
            Report report = reportService.getReport(session.getData().username);
            if (report != null)
                report.setJobAsAborted();
            logger.append("Killed Session").append(session.getData()).log();
        }
        removeSession(sessionId);
        return session;
    }

    public void killAllSessions() {
        logger.append("Killing all active sessions").log();
        activeSessions.keySet().forEach(sessionId -> {
            Session session = killSession(sessionId);
            emailService.sendJobAbortedForMaintenanceEmail(session.getData());
        });
    }

}
