package instagram.report;

import instagram.model.Report;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private Map<String, Report> map = new HashMap<>();

    public Report getReport(String username) {
        if (StringUtils.isBlank(username))
            return null;
        return map.get(username.toLowerCase());
    }

    public Report getNewReport(String username) {
        if (StringUtils.isBlank(username))
            return null;
        username = username.toLowerCase();
        Report report = new Report(username);
        map.put(username, report);
        return report;
    }

    public Report getReportOrCreateNew(String username) {
        Report report = getReport(username);
        if (report == null)
            return getNewReport(username);
        return report;
    }

    public void clearReport(String username) {
        map.remove(username.toLowerCase());
    }

    public Map<String, Report> getAll() { return map; }

}
