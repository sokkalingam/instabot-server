package instagram.report;

import instagram.model.Report;
import instagram.model.enums.JobStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ReportManager {

    private static Map<String, Report> map = new HashMap<>();

    public synchronized static Report getNewReport(String username) {
        Report report = new Report(username);
        map.put(username, report);
        return report;
    }

    public synchronized static Report getReport(String username) {
        return map.get(username);
    }

    public synchronized static void clearReport(String username) {
        map.remove(username);
    }

    public synchronized static Map<String, Report> getAll() { return map; }
}
