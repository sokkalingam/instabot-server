package instagram.report;

import instagram.model.Data;
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

    public Report getNewReport(Data data) {
        if (data == null || StringUtils.isBlank(data.username))
            return null;
        data.username = data.username.toLowerCase();
        Report report = new Report(data.username);
        report.setData(data);
        map.put(data.username, report);
        return report;
    }

    public void clearReport(String username) {
        map.remove(username.toLowerCase());
    }

    public Map<String, Report> getAll() { return map; }

}
