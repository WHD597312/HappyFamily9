package com.xr.happyFamily.le;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.print.PrinterId;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.le.BtClock.CommonClockFragment;
import com.xr.happyFamily.le.BtClock.LeFragmentManager;
import com.xr.happyFamily.le.BtClock.addTimeActivity;
import com.xr.happyFamily.le.adapter.ClickViewPageAdapter;
import com.xr.happyFamily.le.fragment.PuTongFragment;
import com.xr.happyFamily.le.fragment.QingLvFragment;
import com.xr.happyFamily.le.fragment.QunZuFragment;
import com.xr.happyFamily.le.fragment.ShiGuangFragment;
import com.xr.happyFamily.le.fragment.ZhiLaiFragment;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.le.view.NoSrcollViewPage;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.permission.FloatWindowManager;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.ClockService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by win7 on 2018/5/22.
 */

public class ClockActivity extends AppCompatActivity implements LeFragmentManager.CallValueValue {
    Unbinder unbinder;
    List<String> circle = new ArrayList<>();
    List<BaseFragment> fragmentList = new ArrayList<>();
    @BindView(R.id.vp_flower)
    NoSrcollViewPage vp_flower;
    @BindView(R.id.tl_flower)
    TabLayout tl_flower;

    public static boolean running = false;

    private Context mContext = ClockActivity.this;
    SharedPreferences preferences;
    String userId;

    QunZuFragment qunZuFragment = new QunZuFragment();
    QingLvFragment qingLvFragment = new QingLvFragment();

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        mContext = ClockActivity.this;
        setContentView(R.layout.activity_clock);
        ButterKnife.bind(this);

        initView();
        initData();
        FloatWindowManager.getInstance().applyOrShowFloatWindow(ClockActivity.this);
        Intent intent = getIntent();
        int fragid = intent.getIntExtra("fragid", 0);
//        service = new Intent(this, ClockService.class);
//        startService(service);
//        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
        FloatWindowManager.getInstance().applyOrShowFloatWindow(ClockActivity.this);
        Intent clockintent = new Intent(this, ClockService.class);
        isBound = bindService(clockintent, connection, Context.BIND_AUTO_CREATE);
    }


    private void initView() {
        circle.add("时光简记");
        circle.add("制懒模式");
        circle.add("情侣模式");
        circle.add("群组模式");
        Bundle extras = getIntent().getExtras();
        fragmentList.add(new LeFragmentManager());
        fragmentList.add(new CommonClockFragment());
        fragmentList.add(qingLvFragment);
        fragmentList.add(qunZuFragment);
        ClickViewPageAdapter tabAdapter = new ClickViewPageAdapter(getSupportFragmentManager(), fragmentList, this);
        vp_flower.setAdapter(tabAdapter);
        tl_flower.setupWithViewPager(vp_flower);
        for (int i = 0; i < circle.size(); i++) {
            TabLayout.Tab tab = tl_flower.getTabAt(i);
            //注意！！！这里就是添加我们自定义的布局
            tab.setCustomView(tabAdapter.getCustomView(i));
            //这里是初始化时，默认item0被选中，setSelected（true）是为了给图片和文字设置选中效果，代码在文章最后贴出
            if (i == 0) {
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(true);
                ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#33c62b"));
            }

        }
        tl_flower.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(true);
                switch (tab.getPosition()) {
                    case 0:
                        ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#33c62b"));
                        break;
                    case 1:
                        ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#33c62b"));
                        break;
                    case 2:
                        ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#ff7a73"));
                        break;
                    case 3:
                        ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#3682ff"));
                }


                vp_flower.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(false);
                switch (tab.getPosition()) {
                    case 0:
                        ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#8c8c8c"));
                        break;
                    case 1:
                        ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#8c8c8c"));
                        break;
                    case 2:
                        ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#8c8c8c"));
                        break;
                    case 3:
                        ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#8c8c8c"));
                        break;
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        initTab();
    }


    public void initData() {
        preferences = this.getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId", "");

    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    @Override
    public void setPosition(int position) {
        if (position >= 1) {
            tl_flower.setVisibility(View.GONE);
        } else if (position == 0) {
            tl_flower.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        running = true;
        type = getIntent().getStringExtra("type");
        if (type.equals("MQService")) {
            if (clockService != null) {
                clockService.startClock();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
        }
        Log.e("qqqqqqqqSSSSSS", "22222222");
    }

    //    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (isBound) {
//            unbindService(connection);
//        }
//    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        String type=intent.getStringExtra("type");
//        if("MQServer".equals(type)){
//            if(mqService!=null){
//                mqService.startClock();
//            }else {
//                Log.e("qqqqqqqqqqqqIIII","??????");
//            }
//        }
//    }

    //响铃
    ClockService clockService;
    boolean isBound;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            ClockService.LocalBinder binder = (ClockService.LocalBinder) service;
            clockService = binder.getService();
            if (getIntent().getStringExtra("type").equals("MQService")) {
                clockService.startClock();
            }
            Log.e("qqqqqqqSSSS", "!111111");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
//
//


}
