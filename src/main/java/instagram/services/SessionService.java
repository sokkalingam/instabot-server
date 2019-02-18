package instagram.services;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SessionService {

    private static Set<String> activeSessions;

    public SessionService() {
        activeSessions = new HashSet<>();
    }

    public synchronized boolean isSessionActive(String sessionId) {
        return !activeSessions.add(sessionId);
    }

    public synchronized void removeSession(String sessionId) {
        activeSessions.remove(sessionId);
    }

}
