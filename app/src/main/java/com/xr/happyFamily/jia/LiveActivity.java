package com.xr.happyFamily.jia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LiveActivity extends AppCompatActivity {
    Unbinder unbinder;
    @BindView(R.id.cp_alive1)
    CircleProgressView circleProgressView1;
    @BindView(R.id.cp_alive2)
    CircleProgressView circleProgressView2;
    @BindView(R.id.cp_alive3)
    CircleProgressView circleProgressView3;
    @BindView(R.id.cp_alive4)
    CircleProgressView circleProgressView4;
    @BindView(R.id.tv_live_num1)
    TextView tv_live_num1;
    @BindView(R.id.tv_live_num2)
    TextView tv_live_num2;
    @BindView(R.id.tv_live_num3)
    TextView tv_live_num3;
    @BindView(R.id.tv_live_num4)
    TextView tv_live_num4;

    private MyApplication application;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alive);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        unbinder = ButterKnife.bind(this);
        circleProgressView1.setCurrent(95);
        circleProgressView2.setCurrent(70);
        circleProgressView3.setCurrent(40);
        circleProgressView4.setCurrent(15);
    }
    @OnClick({R.id.image_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                finish();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.ACTION_DOWN){
            application.removeActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
