package instagram.report;

import instagram.model.Report;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ReportService {

    public Report getReport(String username) {
        return ReportManager.getReport(username.toLowerCase());
    }

    public Report getNewReport(String username) {
        return ReportManager.getNewReport(username);
    }

    public Map<String, Report> getAll() {
        return ReportManager.getAll();
    }

}
