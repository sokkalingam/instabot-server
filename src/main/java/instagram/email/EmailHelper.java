package instagram.email;

import instagram.messages.EmailMessage;
import instagram.model.Report;
import instagram.model.enums.JobStatus;
import instagram.utils.DateUtils;

public class EmailHelper {

    public static String getHtmlReport(Report report, EmailMessage emailMessage) {
        StringBuilder htmlReportBuilder = new StringBuilder();
        htmlReportBuilder.append(getHtmlReport(report));
        if (emailMessage == EmailMessage.JOB_FINISHED) {
            htmlReportBuilder.append(getHtmlJobRerun(report));
        }
        return htmlReportBuilder.toString();
    }

    private static String getHtmlReport(Report report) {
        return "<h1><span style=\"color: #800080;\">InstaBot Report</span></h1>\n" +
                "<table style=\"height: 228px; width: 516px;\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td style=\"width: 178px;\"><strong><span style=\"color: #800080;\">Job Status</span></strong></td>\n" +
                "<td style=\"width: 342px;\">\n" +
                "<pre><strong>" + report.getJobStatus() + "</strong></pre>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td style=\"width: 178px;\"><strong><span style=\"color: #800080;\">Start Time</span></strong></td>\n" +
                "<td style=\"width: 342px;\">\n" +
                "<pre><strong>" + DateUtils.format(report.getStartTime()) + "</strong></pre>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td style=\"width: 178px;\"><strong><span style=\"color: #800080;\">End Time</span></strong></td>\n" +
                "<td style=\"width: 342px;\">\n" +
                "<pre><strong>" + DateUtils.format(report.getEndTime()) + "</strong></pre>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td style=\"width: 178px;\"><strong><span style=\"color: #800080;\">Loop Count</span></strong></td>\n" +
                "<td style=\"width: 342px;\">\n" +
                "<pre><strong>" + report.getCurrentLoop() + "</strong></pre>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td style=\"width: 178px;\"><strong><span style=\"color: #800080;\">Photos Liked</span></strong></td>\n" +
                "<td style=\"width: 342px;\">\n" +
                "<pre><strong>" + report.getPhotosLiked() + "</strong></pre>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td style=\"width: 178px;\"><strong><span style=\"color: #800080;\">Photos Commented</span></strong></td>\n" +
                "<td style=\"width: 342px;\">\n" +
                "<pre><strong>" + report.getPhotosCommented() + "</strong></pre>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>";
    }

    private static String getHtmlJobRerun(Report report) {
        String URL = "http://instabot.localtunnel.me/api/email/rerun/" + report.getUsername();
        return "<a href='" + URL + "'" + " target='_blank'><h2>CLICK TO RESTART JOB</h2></a>";
    }
}
