package instagram.hashtag;

import instagram.messages.Messages;
import instagram.model.Data;
import instagram.services.SessionService;
import instagram.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hashtag")
public class HashtagController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private HashtagService hashtagService;

    @RequestMapping(value = "/looplike", method = RequestMethod.POST)
    public String likeHashTagInLoop(@RequestBody Data data) {
        if (sessionService.isSessionActive(data.sessionId))
            return Messages.REQUEST_EXISTS.toString();
        ThreadUtils.execute(new Thread(() -> {
            hashtagService.likeHashTagInLoop(data);
            sessionService.removeSession(data.sessionId);
        }));
        return Messages.REQUEST_ACCEPTED.toString();
    }

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public String likeHashTag(@RequestBody Data data) {
        if (sessionService.isSessionActive(data.sessionId))
            return Messages.REQUEST_EXISTS.toString();
        ThreadUtils.execute(new Thread(() -> {
            hashtagService.likeHashTag(data);
            sessionService.removeSession(data.sessionId);
        }));
        return Messages.REQUEST_ACCEPTED.toString();
    }

}
