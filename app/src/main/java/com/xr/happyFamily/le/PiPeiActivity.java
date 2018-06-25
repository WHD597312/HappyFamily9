package com.xr.happyFamily.le;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by win7 on 2018/5/22.
 */

public class PiPeiActivity extends AppCompatActivity {


    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.img_touxiang_to)
    ImageView imgTouxiangTo;
    @BindView(R.id.tv_to)
    TextView tvTo;
    @BindView(R.id.img_touxiang_from)
    ImageView imgTouxiangFrom;
    @BindView(R.id.tv_from)
    TextView tvFrom;
    @BindView(R.id.ll2)
    LinearLayout ll2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuyuan_pipei);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        new CountDownTimer(3 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv.setText( 1+millisUntilFinished / 1000+"");
            }

            @Override
            public void onFinish() {
                //倒计时结束时回调该函数
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        startActivity(new Intent(PiPeiActivity.this,YouYuanActivity.class));
                        finish();
                    }
                }, 2000);

            }
        }.start();
    }
}