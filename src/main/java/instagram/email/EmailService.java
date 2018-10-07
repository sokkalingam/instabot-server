package instagram.email;

import instagram.messages.EmailMessages;
import instagram.model.Data;
import instagram.model.Report;
import instagram.report.ReportService;
import instagram.services.EncryptDecryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Instabot");
        message.setTo(to);
        message.setSubject("Hello from Instabot");
        message.setText("This is a test email from Instabot");
        sendEmail(message);
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
        SimpleMailMessage message = getBasicEmail(data);
        message.setSubject(EmailMessages.JOB_FINISHED.toString());
        sendEmail(message);
    }

    public void sendJobAbortedEmail(Data data) {
        SimpleMailMessage message = getBasicEmail(data);
        message.setSubject(EmailMessages.JOB_ABORTED.toString());
        sendEmail(message);
    }

    public void sendJobAbortedForMaintenanceEmail(Data data) {
        SimpleMailMessage message = getBasicEmail(data);
        message.setSubject(EmailMessages.JOB_ABORTED_FOR_MAINTENACE.toString());
        sendEmail(message);
    }

    public SimpleMailMessage getBasicEmail(Data data) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(getToList(data.email));
        Report report = reportService.getReport(data.username);
        if (report != null)
            message.setText(String.valueOf(report));
        return message;
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
