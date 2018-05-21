package com.xr.happyFamily.jia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.titleview.TitleView;



public class ZnPm25Activity extends AppCompatActivity {


    TitleView titleView;
    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);
        setContentView(R.layout.activty_xnty_pm25);
        titleView = (TitleView) findViewById(R.id.title);
        titleView.setTitleText("智能终端");
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }


    }


}
