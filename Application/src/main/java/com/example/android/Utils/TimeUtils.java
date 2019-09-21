package com.example.android.Utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;




public class TimeUtils {

    public enum TimeType {
        YMDH,
        YMDHmS,
        YMDHm,
        Hm,
        YMD

    }

    public static String getTime(TimeType type) {
        String setTime = "";
        SimpleDateFormat s;
        switch (type) {
            case YMDH:
                s = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINESE);
                setTime = s.format(new Date());
                break;
            case YMDHmS:
                s = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss", Locale.CHINESE);
                setTime = s.format(new Date());
                break;
            case YMDHm:
                s = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINESE);
                setTime = s.format(new Date());
                break;
            case Hm:
                s = new SimpleDateFormat("HHmm", Locale.CHINESE);
                setTime = s.format(new Date());
                break;
            case YMD:
                s = new SimpleDateFormat("yyyyMMdd", Locale.CHINESE);
                setTime = s.format(new Date());
                break;
            default:
                break;

        }
        return setTime;
    }

    public static long timeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }
}
