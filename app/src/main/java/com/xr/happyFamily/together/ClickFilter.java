package com.xr.happyFamily.together;

//Android防止按钮在规定时间内被连续点击的简单方法

public class ClickFilter
{
    public static final long INTERVAL = 500L; //防止连续点击的时间间隔
    private static long lastClickTime = 0L; //上一次点击的时间

    public static boolean filter()
    {
        long time = System.currentTimeMillis();
        lastClickTime = time;
        if ( ( time - lastClickTime ) > INTERVAL )
        {
            return false;
        }
        return true;
    }
}
//    在按钮onClick()方法里首先调用
//  if(ClickFilter.filter()) return;
//          即可；