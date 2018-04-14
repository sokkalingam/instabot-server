package instagram.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {

    private final static int poolSize = 5;
    private static ExecutorService executorService;

    public static ExecutorService getExecutorService() {
        if (executorService == null)
            executorService = Executors.newFixedThreadPool(poolSize);
        return executorService;
    }

    public static void shutdown() {
        if (!getExecutorService().isShutdown())
            getExecutorService().shutdown();
    }

    public static void awaitTermination() {
        if (getExecutorService().isTerminated())
            return;
        try {
            getExecutorService().awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public static void execute(Thread thread) {
        getExecutorService().execute(thread);
    }
}
