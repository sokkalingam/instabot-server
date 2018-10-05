package instagram.email;

import instagram.messages.EmailMessages;
import instagram.model.Data;
import instagram.model.Report;
import instagram.report.ReportService;
import instagram.services.EncryptDecryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public void sendEmail(SimpleMailMessage message) {
        emailSender.setUsername(encryptDecryptService.decrypt(email));
        emailSender.setPassword(encryptDecryptService.decrypt(password));
        emailSender.send(message);
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

    public void sendJobAbortedForMaintenaceEmail(Data data) {
        SimpleMailMessage message = getBasicEmail(data);
        message.setSubject(EmailMessages.JOB_ABORTED_FOR_MAINTENACE.toString());
        sendEmail(message);
    }

    public SimpleMailMessage getBasicEmail(Data data) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(data.email);
        Report report = reportService.getReport(data.username);
        if (report != null)
            message.setText(String.valueOf(report));
        return message;
    }
}
