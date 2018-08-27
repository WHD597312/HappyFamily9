package com.xr.happyFamily.together.util.mqtt;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.xr.happyFamily.jia.activity.TestActivity;

public class UUID {


    public static String getUUID(Context context){
        SharedPreferences preference =context.getSharedPreferences("p99",Context.MODE_PRIVATE);
        String identity = preference.getString("identity", "");
        if (TextUtils.isEmpty(identity)) {
            identity = java.util.UUID.randomUUID().toString();
            preference.edit().putString("identity", identity).commit();
        }
        return identity;
    }
}
