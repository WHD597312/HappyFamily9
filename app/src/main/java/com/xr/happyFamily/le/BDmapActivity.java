package com.xr.happyFamily.le;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;

import butterknife.ButterKnife;
import butterknife.OnClick;
import crossoverone.statuslib.StatusUtil;

public class BDmapActivity extends AppCompatActivity {
    private LocationClient mLocationClient;
    private LocationClientOption mOption;
    TextView locationResult;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_le_bdmap);
        ButterKnife.bind(this);

    }


    @OnClick({R.id.iv_bdmap_back})
    public void onClick (View view ){
        switch (view.getId()){
            case R.id.iv_bdmap_back:
                finish();
                Log.e("dddd", "onClick: -----" );
                break;
        }
    }





    @Override
    protected void onDestroy() {
        ButterKnife.bind(this).unbind();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }



}
