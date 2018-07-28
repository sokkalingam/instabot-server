package instagram.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class WelcomeController {

    private static Map<String, Queue<Integer>> map = Collections.synchronizedMap(new LinkedHashMap<>());
    private static Queue<Integer> jobQueue = new ConcurrentLinkedQueue<>();

    private static boolean processingMap = false;

    private static ExecutorService executor = Executors.newFixedThreadPool(5);

    @RequestMapping("/welcome")
    public String welcome() {
        return "Welcome!";
    }

    @RequestMapping("/massadd/{username}/{count}/{num}")
    public void massAdd(@PathVariable String username, @PathVariable Integer count, @PathVariable Integer num) {
        for (int i = 1; i <= count; i++)
            add(username, num);
    }

    @RequestMapping("{username}/add/{num}")
    public Queue add(@PathVariable String username, @PathVariable Integer num) {
        Queue<Integer> queue;
        if (map.containsKey(username)) {
            queue = map.get(username);
            queue.add(num);
        } else {
            queue = new LinkedList<>();
            queue.add(num);
            map.put(username, queue);
        }
        System.out.println("Request Map: " + map);
        executor.execute(WelcomeController::processMap);
        return jobQueue;
    }

    public static synchronized void processMap() {
        if (processingMap)
            return;
        processingMap = true;
        while (!map.isEmpty()) {
            Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                Queue<Integer> userQueue = map.get(key);
                if (userQueue.isEmpty()) {
                    it.remove();
                } else {
                    int val = userQueue.remove();
                    System.out.println("Process Map: " + map);
                    executor.execute(() -> processJobQueue(val));
                }
            }
        }
        processingMap = false;
    }

    public static void processJobQueue(int val) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(new Date() + " Thread: " + Thread.currentThread().getId() + " Processed: " + val);
//        System.out.println("Remaining Job Queue: " + jobQueue);
    }

}
