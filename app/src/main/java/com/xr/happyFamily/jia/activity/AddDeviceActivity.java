package com.xr.happyFamily.jia.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddDeviceActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.wifi_layout) RelativeLayout wifi_layout;/**wifi添加设备的布局*/
    @BindView(R.id.scan_layout) RelativeLayout scan_layout;/**二维码添加设备的布局*/
    @BindView(R.id.btn_wifi) Button btn_wifi;/**wifi添加设备按钮*/
    @BindView(R.id.btn_scan) Button btn_scan;/**扫描二维码添加设备*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        unbinder=ButterKnife.bind(this);

    }

    private int position=0;
    @OnClick({R.id.back,R.id.btn_wifi,R.id.btn_scan,R.id.bt_add_finish})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_wifi:
                position=0;
                btn_wifi.setBackgroundResource(R.drawable.bg_shape);
                wifi_layout.setVisibility(View.VISIBLE);
                scan_layout.setVisibility(View.GONE);
                btn_wifi.setTextColor(getResources().getColor(R.color.green2));
                btn_scan.setTextColor(getResources().getColor(R.color.color_gray2));
                btn_scan.setBackgroundResource(R.color.white);
                break;
            case R.id.btn_scan:
                position=1;
                btn_scan.setBackgroundResource(R.drawable.bg_shape);
                wifi_layout.setVisibility(View.GONE);
                scan_layout.setVisibility(View.VISIBLE);
                btn_wifi.setTextColor(getResources().getColor(R.color.color_gray2));
                btn_scan.setTextColor(getResources().getColor(R.color.green2));
                btn_wifi.setBackgroundResource(R.color.white);
                break;
            case R.id.bt_add_finish:
                switch (position){
                    case 0:
                        Intent wifiIntent=new Intent(this,DeviceManagerActivity.class);
                        startActivity(wifiIntent);
                        break;
                    case 1:
                        Intent scanIntent=new Intent(this,QRScannerActivity.class);
                        startActivity(scanIntent);
                        break;
                }
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
