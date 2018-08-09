package com.xr.happyFamily.zhen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AboutActivity extends AppCompatActivity {

    Unbinder unbinder;
    private MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        unbinder = ButterKnife.bind(this);
        if (application == null) {
            application = (MyApplication) getApplication();
            application.addActivity(this);
        }
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeActivity(this);//**退出主页面*//*
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
