package com.xr.happyFamily.le.utils;

import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;

import com.xr.happyFamily.jia.MyApplication;

public class WakeAndLock {
    PowerManager pm;
    PowerManager.WakeLock wakeLock;

    public WakeAndLock(Context context) {

        pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.SCREEN_DIM_WAKE_LOCK, "WakeAndLock");
    }

    /**
     * 唤醒屏幕
     */
    public void screenOn() {
        wakeLock.acquire();
        android.util.Log.i("cxq", "screenOn");

    }




}
