package yuanmengzeng.practice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getTime(long millis, String format) {
        Date date = new Date(millis);
        Utils.println(date.toString());
        SimpleDateFormat timeformatter = new SimpleDateFormat(format);
        return timeformatter.format(date);
    }

    public static long getTimeStamp(String date, String format){
        SimpleDateFormat formater = new SimpleDateFormat(format);
        long timeStamp = 0L;
        try {
            return formater.parse(date).getTime();
        }catch (ParseException e){
            Utils.println(""+e);
        }
        return timeStamp;
    }
}
