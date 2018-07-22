package com.xr.happyFamily.bao;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.alipay.PayResult;
import com.xr.happyFamily.bean.OrderListBean;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.xnty.ArcProgressBar;
import com.xr.happyFamily.le.view.CompletedView;
import com.xr.happyFamily.le.view.MyHorizontalScrollView;
import com.xr.happyFamily.le.view.MyHorizontalScrollViewAdapter;
import com.xr.happyFamily.le.view.TimeBar;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by win7 on 2018/5/22.
 */

public class TestActivity extends AppCompatActivity {

    String orderNumber;
    private TimeBar timeBar;
    int change = 0;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        timeBar = (TimeBar) findViewById(R.id.arcprogressBar);


    }

}