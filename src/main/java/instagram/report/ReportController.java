package instagram.report;

import instagram.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/")
    @ResponseBody
    public Report getReportByCookie(@CookieValue(value = "username", defaultValue = "") String username) {
        return reportService.getReport(username);
    }

    @RequestMapping(value = "/{username}")
    @ResponseBody
    public Report getReport(@PathVariable String username, HttpServletResponse response) {
        response.addCookie(new Cookie("username", username));
        return reportService.getReport(username);
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Map<String, Report> list() {
        return reportService.getAll();
    }

}
