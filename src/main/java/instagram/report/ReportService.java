package instagram.report;

import instagram.model.Report;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private Map<String, Report> map = new HashMap<>();

    public Report getNewReport(String username) {
        Report report = new Report(username);
        map.put(username, report);
        return report;
    }

    public Report getReport(String username) {
        return map.get(username);
    }

    public void clearReport(String username) {
        map.remove(username);
    }

    public Map<String, Report> getAll() { return map; }

}
