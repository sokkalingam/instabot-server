package instagram.instabot;

import instagram.css.CssService;
import instagram.database.RunningJobsRepo;
import instagram.email.EmailService;
import instagram.hashtag.HashtagService;
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
    private HashtagService hashtagService;

    @Autowired
    private RunningJobsRepo runningJobsRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CssService cssService;

    public void shutdown() {
        System.out.println("Shutting down...");
        saveRunningJobs();
        sessionService.killAllSessions();
    }

    // startUp is called when server is started
    public void startup() {
        cssService.buildCssMap();
        runPendingJobs();
        clearRemainingJobs();
    }

    public void runPendingJobs() {
        List<Data> jobsToRun = runningJobsRepo.findAll();
        jobsToRun.forEach(data -> {
            hashtagService.performActionsInLoop(data);
            emailService.sendJobAutoResumedEmail(data);
        });
    }

    public void clearRemainingJobs() {
        runningJobsRepo.deleteAll();
    }

    public List<Data> getRunningJobs() {
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
    }
}
