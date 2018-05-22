package cn.sunshine.basenetwork.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NetWorkDateUtil {

    public static final String Pattern_1 = "yyyy-MM-dd HH:mm:ss";

    public static final String Pattern_11 = "EEE, dd MMM yyyy HH:mm:ss Z"; // Mon, 10 Oct 2016 09:20:00 GMT

    /**
     * @return 根据一定的格式获取 系统时间
     */
    public static String getSystemTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(Pattern_1);
        return sDateFormat.format(new Date());
    }

    /**
     * @param time （暂时用于http 请求返回的服务器时间）
     * @return 格式后的时间
     */
    public static String convertToLocal(String time) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(Pattern_11, Locale.ENGLISH);
        String date = null;
        try {
            if (time != null) {
                Date parseDate = sDateFormat.parse(time);
                sDateFormat = new SimpleDateFormat(Pattern_1, Locale.SIMPLIFIED_CHINESE);
                date = sDateFormat.format(parseDate);
            }
        } catch (ParseException e) {
//            e.printStackTrace();
        }
        return date;
    }

    /////////////////////////
    //private
    //

    /**
     * get local date by date string
     *
     * @param timeStr
     * @return
     */
    public static Date getDateByStr(String timeStr) {
        Date date = null;
        DateFormat format = new SimpleDateFormat(Pattern_1);
        try {
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 计算时间差
     *
     * @param time1
     * @param time2
     * @return
     */
    public static long getTimeDelay(Date time1, Date time2) {
        return time1.getTime() - time2.getTime();
    }

}
