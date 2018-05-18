package com.xr.happyFamily.jia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.titleview.TitleView;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ZixingAddActivity extends Activity {
    Unbinder unbinder;
    TitleView titleView;
    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);
        setContentView(R.layout.activity_home_zxing );
        unbinder = ButterKnife.bind(this);
        titleView = (TitleView) findViewById(R.id.title);
        titleView.setTitleText("添加设备");

    }
    @OnClick({R.id.bt_add_zixing,R.id.bt_zixing_wifi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_add_zixing:
                startActivity(new Intent(this, MenuActivity.class));
                break;
            case R.id.bt_zixing_wifi:
                startActivity(new Intent(this, AddEquipmentActivity.class));
                break;

        }

    }
}
