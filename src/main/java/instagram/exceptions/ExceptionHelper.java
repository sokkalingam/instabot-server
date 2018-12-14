package instagram.exceptions;

import java.util.LinkedList;
import java.util.Queue;

public class ExceptionHelper {

    private static Queue<Exception> exceptionQueue = new LinkedList<>();

    public static synchronized Queue<Exception> getExceptionQueue() {
        return exceptionQueue;
    }

    public static synchronized void addException(Exception e) {
        exceptionQueue.add(e);
    }

    public static synchronized void clearExceptions() {
        exceptionQueue.clear();
    }
}
