package instagram.report;

import instagram.model.Report;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ReportService {

    public Report getReport(String username) {
        return ReportManager.getReport(username.toLowerCase());
    }

}
