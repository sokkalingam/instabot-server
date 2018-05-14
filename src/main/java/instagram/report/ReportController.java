package instagram.report;

import instagram.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/{username}")
    @ResponseBody
    public Report getReport(@PathVariable String username) {
        return reportService.getReport(username);
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Map<String, Report> list() {
        return reportService.getAll();
    }

}
