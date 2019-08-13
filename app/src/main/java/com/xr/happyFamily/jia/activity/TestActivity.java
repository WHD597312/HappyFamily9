package com.xr.happyFamily.jia.activity;


import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

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


//    @BindView(R.id.editText) EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);
    }

}