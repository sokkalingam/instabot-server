package instagram.feedback;

import instagram.email.EmailService;
import instagram.model.Feedback;
import instagram.services.EncryptDecryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EncryptDecryptService encryptDecryptService;

    public boolean sendFeedback(Feedback feedback) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(encryptDecryptService.decrypt(emailService.getEmail()));
        message.setSubject("Feedback: " + feedback.getSubject());

        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(feedback.getBody())
                .append("\n\nFrom: " + feedback.getName())
                .append("\nEmail: " + feedback.getEmail());

        message.setText(textBuilder.toString());
        return emailService.sendEmail(message);
    }

}
