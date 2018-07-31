package com.xr.happyFamily.jia.activity;

import android.animation.ObjectAnimator;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.xr.happyFamily.R;
import com.xr.happyFamily.together.util.location.CheckPermissionsActivity;
import com.xr.happyFamily.together.util.location.Utils;

import java.util.List;

public class Demo extends CheckPermissionsActivity{
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

    }



}