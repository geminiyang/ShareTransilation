package com.idear.move.util;

/**
 * Created by user on 2017/5/15.
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 返回unix时间戳 (1970年至今的秒数)
     * @return
     */
    public static long getUnixStamp(){
        return System.currentTimeMillis()/1000;
    }

    /**
     * 得到昨天的日期
     * @return
     */
    public static String getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = sdf.format(calendar.getTime());
        return yesterday;
    }

    /**
     * 得到今天的日期
     * @return
     */
    public static  String getTodayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        return date;
    }

    /**
     * 时间转化为时间格式
     * @param timeMills
     * @return
     */
    public static String timeStampToStr(long timeMills) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(timeMills);
        return date;
    }

    /**
     * 得到日期   yyyy-MM-dd
     * @param timeMills
     * @return
     */
    public static String formatDate(long timeMills) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            return "刚刚";
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
