package instagram.feedback;

import instagram.messages.ResponseMessages;
import instagram.model.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public String sendFeedback(@Valid @RequestBody Feedback feedback) {
        return feedbackService.sendFeedback(feedback) ?
                ResponseMessages.EMAIL_SENT.toString() : ResponseMessages.EMAIL_FAILED.toString();
    }

}
