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

    @Autowired
    private LogService logService;

    private Queue<String> exceptionQueue;

    public ExceptionService() {
        exceptionQueue = new LinkedList<>();
    }

    public synchronized Queue<String> getExceptionQueue() {
        return exceptionQueue;
    }

    public synchronized void addException(Throwable e) {
        if (e == null)
            return;
        logService.appendErr(e.getMessage()).err();
        exceptionQueue.add(new Date() + " - " + e.getMessage());
    }

    public synchronized void clearExceptions() {
        exceptionQueue.clear();
    }
}
