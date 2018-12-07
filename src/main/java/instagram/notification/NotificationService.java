package instagram.notification;

import instagram.model.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private Notification notification;

    public Notification getNotification() {
        return notification;
    }

    public void wetNotification(Notification notification) {
        this.notification = notification;
    }
}
