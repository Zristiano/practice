package yuanmengzeng.practice;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getTime(long millis, String format) {
        Date date = new Date(millis);
        Utils.println(date.toString());
        SimpleDateFormat timeformatter = new SimpleDateFormat(format);
        return timeformatter.format(date);
    }
}
