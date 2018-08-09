package com.xr.happyFamily.jia.activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xr.happyFamily.R;

import com.xr.happyFamily.jia.view_custom.DoubleWaveView;

import butterknife.ButterKnife;


/**
 * Created by win7 on 2018/5/22.
 */

public class TestActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);
        final DoubleWaveView doubleWaveView= (DoubleWaveView) findViewById(R.id.doubleWaveView);
        doubleWaveView.setProHeight(80);

        Button btn_queding= (Button) findViewById(R.id.btn_queding);
        final EditText editText= (EditText) findViewById(R.id.ed_num);
        btn_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doubleWaveView.setProHeight(Integer.parseInt(editText.getText().toString()));
            }
        });
    }


}