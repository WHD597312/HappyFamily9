package com.xr.happyFamily.jia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alive);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        unbinder = ButterKnife.bind(this);
        circleProgressView1.setCurrent(95);
        circleProgressView2.setCurrent(70);
        circleProgressView3.setCurrent(40);
        circleProgressView4.setCurrent(15);

    }
}
