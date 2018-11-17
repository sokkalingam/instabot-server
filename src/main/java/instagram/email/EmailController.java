package instagram.email;

import instagram.hashtag.HashtagController;
import instagram.messages.ResponseMessages;
import instagram.model.Report;
import instagram.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private HashtagController hashtagController;

    @Autowired
    private ReportService reportService;

    @RequestMapping("/rerun/{username}")
    public String rerun(@PathVariable String username) {
        Report report = reportService.getReport(username);
        if (report != null)
            return hashtagController.performActionsInLoop(report.getData());
        else
            return ResponseMessages.REQUEST_INVALID.toString();
    }
}
