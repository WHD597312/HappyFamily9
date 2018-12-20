package com.xr.happyFamily.together.util;

import android.widget.Toast;
import android.content.Context;


public class Utils {

    public static void showToast(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
    public static boolean isEmpty(String s){
        boolean flag=false;
        if (s==null || "".equals(s)){
            flag=true;
        }
        return flag;
    }


    /**
     * 得到某一天的星期几
     * @return
     */
    public static int getWeek(int week){
        int mWeek=0;
        switch (week) {
            case (1):
                mWeek=7;
                break;
            case (2):
                mWeek=1;
                break;
            case 3:
                mWeek=2;
                break;
            case 4:
                mWeek=3;
                break;
            case 5:
                mWeek=4;
                break;
            case 6:
                mWeek=5;
                break;
            case 7:
                mWeek=6;
                break;
        }
        return mWeek;
    }
}


