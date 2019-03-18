package instagram.instabot;

import instagram.css.CssService;
import instagram.database.RunningJobsRepo;
import instagram.email.EmailService;
import instagram.logger.LogService;
import instagram.model.Data;
import instagram.model.Report;
import instagram.model.Session;
import instagram.report.ReportService;
import instagram.sessions.SessionService;
import instagram.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstabotService {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private RunningJobsRepo runningJobsRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CssService cssService;

    private LogService logger;

    public InstabotService() {
        logger = new LogService();
    }

    public void shutdown() {
        logger.append("Shutting Down").log();
        saveRunningJobs();
        sessionService.killAllSessions();
        logger.append("Shutdown Complete").log();
    }

    // startUp is called when server is started
    public void startup() {
        logger.append("Startup").log();
        cssService.buildCssMap();
        runPendingJobs();
        clearRemainingJobs();
    }

    public void runPendingJobs() {
        logger.append("Run Pending Jobs").log();
        List<Data> jobsToRun = runningJobsRepo.findAll();
        jobsToRun.forEach(data -> {
            sessionService.addNewSession(data);
            emailService.sendJobAutoResumedEmail(data);
        });
    }

    public void clearRemainingJobs() {
        runningJobsRepo.deleteAll();
        logger.append("Cleared Remaining Jobs").log();
    }

    public List<Data> getRunningJobs() {
        logger.append("Get Running Jobs").log();
        List<Data> dataList = new ArrayList<>();
        for (Session session : sessionService.getActiveSessions().values()) {
            Data data = session.getData();
            Report report = reportService.getReport(data.username);
            data = DataUtils.getRemainingJobData(data, report);
            dataList.add(data);
        }
        return dataList;
    }

    public void saveRunningJobs() {
        runningJobsRepo.saveAll(getRunningJobs());
        logger.append("Saved Running Jobs").log();
    }
}
