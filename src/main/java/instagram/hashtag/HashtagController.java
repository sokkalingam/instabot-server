package instagram.hashtag;

import instagram.exceptions.ExceptionService;
import instagram.model.Data;
import instagram.sessions.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/hashtag")
public class HashtagController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private HashtagService hashtagService;

    @Autowired
    private ExceptionService exceptionService;

    @RequestMapping(value = "/loop", method = RequestMethod.POST)
    public String performActionsInLoop(@Valid @RequestBody Data data) {
        try {
            return hashtagService.performActionsInLoop(data);
        } catch (Exception e) {
            exceptionService.addException(e);
            return null;
        }
    }

}
