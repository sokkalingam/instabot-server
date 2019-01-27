package instagram.utils;

import instagram.model.Data;
import instagram.model.Report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataUtils {

    private static Properties properties = new Properties();

    static {
        try {
            properties.load(DataUtils.class.getClassLoader().getResourceAsStream("properties/data.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSessionId() {
        return properties.getProperty("session_id");
    }

    public static Integer getTimeMin() {
        return Integer.parseInt(properties.getProperty("time_min"));
    }

    public static Integer getTimeMax() {
        return Integer.parseInt(properties.getProperty("time_max"));
    }

    public static Integer getMaxFollowers() {
        return Integer.parseInt(properties.getProperty("max_followers"));
    }

    public static Integer getNoOfLoop() {
        return Integer.parseInt(properties.getProperty("no_of_loops"));
    }

    public static Integer getNoOfPhotos() {
        return Integer.parseInt(properties.getProperty("no_of_photos"));
    }

    public static List<String> getHashTags() {
        String val = properties.getProperty("hashtags");
        String[] tags = val.split(",");
        List<String> hastags = new ArrayList<>();
        for (String tag : tags) {
            tag = tag.replaceAll(" ", "").toLowerCase();
            hastags.add(tag);
        }
        return hastags;
    }

    public static List<String> getComments() {
        String val = properties.getProperty("comments");
        String[] tags = val.split(",");
        List<String> comments = new ArrayList<>();
        for (String tag : tags) {
            tag = tag.trim();
            comments.add(tag);
        }
        return comments;
    }

    public static Data getRemainingJobData(Data data, Report report) {

        if (report == null)
            return data;

        // If there was only one hashtag then subtract already run number of times to loop from report
        if (data.hashtags.size() == 1)
            data.noOfTimesToLoop = data.noOfTimesToLoop - report.getCurrentLoop();

        // Remove already finished hashtags
        List<String> hashtags = new ArrayList<>();
        int index = data.hashtags.indexOf(report.getCurrentHashtag());
        index = Math.max(index, 0);
        for (int i = index; i < data.hashtags.size(); i++) {
            hashtags.add(data.hashtags.get(i));
        }
        data.hashtags = hashtags;

        return data;
    }
}
