package instagram.notification;

import instagram.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/")
    public Notification getNotification() {
        return notificationService.getNotification();
    }

    @PostMapping("/")
    public void setNotification(@RequestBody Notification notification) {
        notificationService.wetNotification(notification);
    }
    
}
