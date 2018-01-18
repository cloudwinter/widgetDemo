package com.summer.demo.exception;


import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * <时间戳操作类分享的类>
 * 
 * @author zhangxiuliang
 */
public class TimestampUtil {

    /**
     * 获得当前时间戳
     *
     * @param format 格式"yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String getCurrentTimeLable(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.getDefault());
        return sdf.format(getIntTimestamp());
    }

    /**
     * 获得当前时间戳
     *
     * @return
     */
    public static long getIntTimestamp() {
        long unixTimeGMT = 0;
        try {
            unixTimeGMT = System.currentTimeMillis();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return unixTimeGMT;
    }
}
