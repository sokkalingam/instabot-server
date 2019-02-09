package instagram.exceptions;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class ExceptionService {

    private Queue<Exception> exceptionQueue;

    public ExceptionService() {
        exceptionQueue = new LinkedList<>();
    }

    public synchronized Queue<Exception> getExceptionQueue() {
        return exceptionQueue;
    }

    public synchronized void addException(Exception e) {
        exceptionQueue.add(e);
    }

    public synchronized void clearExceptions() {
        exceptionQueue.clear();
    }
}
