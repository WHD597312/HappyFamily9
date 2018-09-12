package com.xr.happyFamily.together.util.mqtt;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

public class VibratorUtil {
    /**
     * final Activity activity ：调用该方法的Activity实例 long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */
// 一直震动多少秒
    public static void Vibrate(Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity
                .getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    // 按照我们传进去的数组进行间歇性的震动
    public static void Vibrate(Context context, long[] pattern,
                               boolean isRepeat) {
        Vibrator vib = (Vibrator) context
                .getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }


    // 停止震动
    public static void StopVibrate(Context context) {
        Vibrator vib = (Vibrator) context
                .getSystemService(Service.VIBRATOR_SERVICE);
        vib.cancel();
    }
}
