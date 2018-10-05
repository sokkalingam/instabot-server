package instagram.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final String TIME_ZONE = "EST";

    public static String format(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return "";
        return localDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy',' hh:mm:ss a ")) + TIME_ZONE;
    }

}
