package instagram.exceptions;

import instagram.logger.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

@Service
public class ExceptionService {

    private Queue<String> exceptionQueue;
    private LogService logger;

    public ExceptionService() {
        exceptionQueue = new LinkedList<>();
        logger = new LogService();
    }

    public synchronized Queue<String> getExceptionQueue() {
        return exceptionQueue;
    }

    public synchronized void addException(Throwable e) {
        if (e == null)
            return;
        logger.appendErr(e.getMessage()).err();
        exceptionQueue.add(new Date() + " - " + e.getMessage());
    }

    public synchronized void clearExceptions() {
        exceptionQueue.clear();
    }
}
