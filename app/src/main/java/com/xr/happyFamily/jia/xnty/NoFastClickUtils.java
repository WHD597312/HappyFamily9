package com.xr.happyFamily.jia.xnty;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NoFastClickUtils  {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 300;
    private static long lastClickTime=0;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        Log.i("diffClickTime","-->"+(curClickTime - lastClickTime));
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {

            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

}
