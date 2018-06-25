package com.xr.happyFamily.together.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    //时间戳转time
    public static String getTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(time))));   // 时间戳转换成时间
        return sd;
    }

}
