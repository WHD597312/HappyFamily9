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
}


