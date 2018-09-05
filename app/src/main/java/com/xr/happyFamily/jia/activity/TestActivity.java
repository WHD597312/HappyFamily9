package com.xr.happyFamily.jia.activity;


import android.os.Bundle;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.BarChart;
import com.xr.happyFamily.R;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by win7 on 2018/5/22.
 */

public class TestActivity extends AppCompatActivity {


    @BindView(R.id.editText) EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.button})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button:
                String id=editText.getText().toString();
                Map<String,Object> params=new HashMap<>();
                params.put("id",id);
                break;
        }
    }


}