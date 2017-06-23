package com.idear.move.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    /**
     * 返回unix时间戳 (1970年至今的秒数)
     * @return
     */
    public static long getUnixStamp(){
        return System.currentTimeMillis()/1000;
    }

    public static String picDateFormat(long timeMills) {
        Date date = new Date(timeMills);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        return sdf.format(date);
    }

    /**
     * 得到昨天的日期
     * @return
     */
    public static String getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String yesterday = sdf.format(calendar.getTime());
        return yesterday;
    }

    /**
     * 得到今天的日期
     * @return
     */
    public static  String getTodayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String date = sdf.format(new Date());
        return date;
    }

    /**
     * 时间转化为时间格式
     * @param timeMills
     * @return
     */
    public static String timeStampToStr(long timeMills) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String date = sdf.format(timeMills);
        return date;
    }

    public static int StrToTimeStamp(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            //添加相应的处理
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (int)(cal.getTimeInMillis() / 1000);
    }

    /**
     * 根据时间戳获取自定义时间
     * @param timeStamp 单位为秒
     * @return
     */
    public static String receiveDate(long timeStamp) {
        long curTime = System.currentTimeMillis();//获取当前时间
        long time = ((curTime / 1000) - timeStamp);//已经将单位转换成秒

        if (time >= 60 && time < 3600 * 24) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
            return sdf.format(new Date(timeStamp*1000));
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 2) {
            return "昨天";
        } else if(time >= 3600 * 24 * 2&& time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "时间为负数";
        }
    }

    /**
     * 得到日期   yyyy-MM-dd
     * @param timeMills
     * @return
     */
    public static String formatDate(long timeMills) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String date = sdf.format(timeMills);
        return date;
    }

    /**
     * 得到时间  HH:mm:ss
     * @param timeMills   时间戳
     * @return
     */
    public static String getTime(long timeMills) {
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String date = sdf.format(timeMills);
        String[] split = date.split("\\s");
        if ( split.length > 1 ){
            time = split[1];
        }
        return time;
    }

    /**
     * 将一个时间转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeMills
     * @return
     */
    public static String convertTimeToFormat(long timeMills) {
        long curTime = Calendar.getInstance().getTimeInMillis();
        long time = (curTime - timeMills) / (long)1000;//已经将单位转换成秒

        if (time > 0 && time < 60) {
            return time + "秒前";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "刚刚";
        }
    }

    /**
     * 将一个时间转换成提示性时间字符串，(多少分钟)
     *
     * @param timeMills
     * @return
     */
    public static String timeStampToFormat(long timeMills) {
        long curTime =System.currentTimeMillis();
        long time = (curTime - timeMills) / (long) 1000 ;
        return time/60 + "";
    }

}
