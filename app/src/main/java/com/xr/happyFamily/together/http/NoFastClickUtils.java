package com.xr.happyFamily.together.http;

import android.util.Log;
import android.view.View;
import android.widget.Button;

public class NoFastClickUtils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 200;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        Log.i("sssssssss","-->"+flag);
        return flag;
    }
}
