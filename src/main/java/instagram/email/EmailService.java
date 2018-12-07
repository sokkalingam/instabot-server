package instagram.email;

import instagram.messages.EmailMessages;
import instagram.model.Data;
import instagram.model.Report;
import instagram.model.enums.JobStatus;
import instagram.report.ReportService;
import instagram.services.EncryptDecryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
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
            mimeMessage.setContent(EmailHelper.getHtmlReport(report), "text/html");
            mimeMessageHelper.setTo("lings24@gmail.com");
            mimeMessageHelper.setSubject("Test Email for HTML from Instabot");
            sendEmail(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean sendEmail(MimeMessage message) {
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
        try {
            MimeMessage message = getBasicEmail(data);
            message.setSubject(EmailMessages.JOB_FINISHED.toString());
            sendEmail(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendJobAbortedEmail(Data data) {
        try {
            MimeMessage message = getBasicEmail(data);
            message.setSubject(EmailMessages.JOB_ABORTED.toString());
            sendEmail(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public void sendJobAbortedForMaintenanceEmail(Data data) {
        try {
            MimeMessage message = getBasicEmail(data);
            message.setSubject(EmailMessages.JOB_ABORTED_FOR_MAINTENACE.toString());
            sendEmail(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean sendEmailToBot(String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(encryptDecryptService.decrypt(getEmail()));
        message.setSubject(subject);
        message.setText(body);
        return sendEmail(message);
    }

    public MimeMessage getBasicEmail(Data data) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        Report report = reportService.getReport(data.username);
        if (report != null) {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessageHelper.setTo(getToList(data.email));
            mimeMessage.setContent(EmailHelper.getHtmlReport(report), "text/html");
        }
        return mimeMessage;
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
