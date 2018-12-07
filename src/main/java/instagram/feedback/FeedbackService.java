package instagram.feedback;

import instagram.email.EmailService;
import instagram.model.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private EmailService emailService;

    public boolean sendFeedback(Feedback feedback) {
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(feedback.getBody())
                .append("\n\nFrom: " + feedback.getName())
                .append("\nEmail: " + feedback.getEmail());
        return emailService.sendEmailToBot(feedback.getSubject(), textBuilder.toString());
    }

}
