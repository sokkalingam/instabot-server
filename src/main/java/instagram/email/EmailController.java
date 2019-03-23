package instagram.email;

import instagram.logger.LogService;
import instagram.messages.ResponseMessages;
import instagram.model.Report;
import instagram.report.ReportService;
import instagram.sessions.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private LogService logger;

    public EmailController() {
        logger = new LogService();
    }

    @Autowired
    private ReportService reportService;

    @Autowired
    private SessionService sessionService;

    @RequestMapping("/rerun/{username}")
    public String rerun(@PathVariable String username) {
        logger.append(username).append("Email Rerun Request").log();
        Report report = reportService.getReport(username);
        if (report != null) {
            sessionService.addNewSession(report.getData());
            return ResponseMessages.REQUEST_ACCEPTED.toString();
        }
        return ResponseMessages.REQUEST_INVALID.toString();
    }
}
