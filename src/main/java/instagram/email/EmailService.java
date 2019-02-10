package instagram.email;

import instagram.exceptions.ExceptionService;
import instagram.logger.LogService;
import instagram.messages.EmailSubject;
import instagram.model.Data;
import instagram.model.Report;
import instagram.model.enums.EmailSubjects;
import instagram.model.enums.JobStatus;
import instagram.report.ReportService;
import instagram.services.EncryptDecryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String email;

    @Value("${spring.mail.password}")
    private String password;

    @Autowired
    private JavaMailSenderImpl emailSender;

    @Autowired
    private ReportService reportService;

    @Autowired
    private EncryptDecryptService encryptDecryptService;
    
    @Autowired
    private ExceptionService exceptionService;

    @Autowired
    private LogService logger;

    public void testEmail(String to) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            Report report = new Report("username");
            report.setJobStatus(JobStatus.COMPLETED);
            report.setStartTime(LocalDateTime.now());
            report.setEndTimeAsNow();
            report.setCurrentLoop(24);
            mimeMessage.setContent(EmailHelper.getHtmlReport("username", report, EmailSubject.JOB_FINISHED), "text/html");
            mimeMessageHelper.setTo("lings24@gmail.com");
            mimeMessageHelper.setSubject("Test Email for HTML from Instabot");
            Data data = new Data();
            data.username = "TestEmail";
            sendEmail(mimeMessage, data);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean sendEmail(MimeMessage message, Data data) {

        if (message == null)
            return false;

        emailSender.setUsername(encryptDecryptService.decrypt(email));
        emailSender.setPassword(encryptDecryptService.decrypt(password));
        try {
            emailSender.send(message);
            logger.append(data.username).append("Email Sent to: " + Arrays.asList(message.getAllRecipients())).log();
            return true;
        } catch (MailException | MessagingException e) {
            logger.appendErr(data.username).appendErr("Could not send email, SimpleMailMessage: " + message).err();
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendEmail(SimpleMailMessage message, Data data) {
        emailSender.setUsername(encryptDecryptService.decrypt(email));
        emailSender.setPassword(encryptDecryptService.decrypt(password));
        try {
            emailSender.send(message);
            return true;
        } catch (MailException e) {
            logger.appendErr(data.username).appendErr("Could not send email, SimpleMailMessage: " + message).err();
            e.printStackTrace();
            return false;
        }
    }

    public void sendEmail(Data data, EmailSubject emailSubject) {
        sendEmail(getBasicEmail(data, emailSubject), data);
    }

    public void sendJobFinishedEmail(Data data) {
        sendEmail(data, EmailSubject.JOB_FINISHED);
    }

    public void sendJobAbortedEmail(Data data) {
        sendEmail(data, EmailSubject.JOB_ABORTED);
    }

    public void sendJobAbortedForMaintenanceEmail(Data data) {
        sendEmail(data, EmailSubject.JOB_ABORTED_WILL_AUTO_RESUME);
    }

    public void sendJobAutoResumedEmail(Data data) {
        sendEmail(data, EmailSubject.JOB_RESUMED);
    }

    public void sendLikeIsBlockedEmail(Data data) {
        sendEmail(data, EmailSubject.LIKE_IS_BLOCKED);
    }

    public void sendCommentIsBlockedEmail(Data data) {
        sendEmail(data, EmailSubject.COMMENT_IS_BLOCKED);
    }

    public void sendUserIsBlockedEmail(Data data) {
        sendEmail(data, EmailSubject.USER_IS_BLOCKED);
    }

    public boolean sendEmailToBot(String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(encryptDecryptService.decrypt(getEmail()));
        message.setSubject(subject);
        message.setText(body);
        Data data = new Data();
        data.username = "InstaBot";
        return sendEmail(message, null);
    }

    public MimeMessage getBasicEmail(Data data, EmailSubject emailSubject) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessageHelper.setTo(getToList(data.email));
            mimeMessage.setSubject(emailSubject.toString());

            Report report = reportService.getReport(data.username);
            mimeMessage.setContent(EmailHelper.getHtmlReport(data.username, report, emailSubject), "text/html");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return mimeMessage;
    }

    // Runs every 10 minutes
    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public boolean sendExceptionEmailsFromQueue() {
        if (exceptionService.getExceptionQueue().size() == 0)
            return false;
        logger.append("Running sendExceptionEmailsFromQueue").log();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(encryptDecryptService.decrypt(getEmail()));
        message.setSubject(EmailSubjects.FAILURE.toString());
        StringBuilder sb = new StringBuilder();
        for (Exception exception : exceptionService.getExceptionQueue())
            sb.append(exception.getMessage()).append("\n\n");
        exceptionService.clearExceptions();
        message.setText(sb.toString());
        Data data = new Data();
        data.username = "Exceptions";
        return sendEmail(message, data);
    }

    public String getEmail() {
        return email;
    }

    private static String[] getToList(String email) {
        String[] emails = null;
        if (email != null) {
            emails = email.split(",");
            for (int i = 0; i < emails.length; i++)
                emails[i] = emails[i].trim();
        }
        return emails;
    }

}
