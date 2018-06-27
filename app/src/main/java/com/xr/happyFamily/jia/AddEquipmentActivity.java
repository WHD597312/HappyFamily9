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

public class AddEquipmentActivity extends Activity {
    Unbinder unbinder;
    TitleView titleView;
    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);

        setContentView(R.layout.activty_home_addequipment);
        unbinder = ButterKnife.bind(this);
        titleView = (TitleView) findViewById(R.id.title_adde);
        titleView.setTitleText("添加设备");

    }
    @OnClick({R.id.bt_add_finish2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_add_finish2:
              startActivity(new Intent(this, MyPaperActivity.class));
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