package instagram.email;

import instagram.exceptions.ExceptionHelper;
import instagram.messages.EmailSubject;
import instagram.model.Data;
import instagram.model.Report;
import instagram.model.enums.EmailSubjects;
import instagram.model.enums.JobStatus;
import instagram.report.ReportService;
import instagram.services.EncryptDecryptService;
import org.apache.commons.lang.exception.ExceptionUtils;
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
            sendEmail(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean sendEmail(MimeMessage message) {

        if (message == null)
            return false;

        emailSender.setUsername(encryptDecryptService.decrypt(email));
        emailSender.setPassword(encryptDecryptService.decrypt(password));
        try {
            emailSender.send(message);
            System.out.println("Email Sent to: " + Arrays.asList(message.getAllRecipients()));
            return true;
        } catch (MailException | MessagingException e) {
            System.out.println("Could not send email, SimpleMailMessage: " + message);
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendEmail(SimpleMailMessage message) {
        emailSender.setUsername(encryptDecryptService.decrypt(email));
        emailSender.setPassword(encryptDecryptService.decrypt(password));
        try {
            emailSender.send(message);
            return true;
        } catch (MailException e) {
            System.out.println("Could not send email, SimpleMailMessage: " + message);
            e.printStackTrace();
            return false;
        }
    }

    public void sendJobFinishedEmail(Data data) {
        MimeMessage message = getBasicEmail(data, EmailSubject.JOB_FINISHED);
        sendEmail(message);
    }

    public void sendJobAbortedEmail(Data data) {
        MimeMessage message = getBasicEmail(data, EmailSubject.JOB_ABORTED);
        sendEmail(message);
    }

    public void sendJobAbortedForMaintenanceEmail(Data data) {
        MimeMessage message = getBasicEmail(data, EmailSubject.JOB_ABORTED_WILL_AUTO_RESUME);
        sendEmail(message);
    }

    public void sendJobAutoResumedEmail(Data data) {
        MimeMessage message = getBasicEmail(data, EmailSubject.JOB_RESUMED);
        sendEmail(message);
    }

    public boolean sendEmailToBot(String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(encryptDecryptService.decrypt(getEmail()));
        message.setSubject(subject);
        message.setText(body);
        return sendEmail(message);
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
        if (ExceptionHelper.getExceptionQueue().size() == 0)
            return false;
        System.out.println("Running sendExceptionEmailsFromQueue");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(encryptDecryptService.decrypt(getEmail()));
        message.setSubject(EmailSubjects.FAILURE.toString());
        StringBuilder sb = new StringBuilder();
        for (Exception exception : ExceptionHelper.getExceptionQueue())
            sb.append(exception.getMessage()).append("\n\n");
        ExceptionHelper.clearExceptions();
        message.setText(sb.toString());
        return sendEmail(message);
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
