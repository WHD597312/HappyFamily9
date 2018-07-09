package com.xr.happyFamily.le;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.print.PrinterId;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qzs.android.fuzzybackgroundlibrary.Fuzzy_Background;
import com.xr.database.dao.ClockBeanDao;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopConfActivity;
import com.xr.happyFamily.bao.adapter.EvaluateAdapter;
import com.xr.happyFamily.bao.adapter.EvaluateXhAdapter;
import com.xr.happyFamily.bao.adapter.MyViewPageAdapter;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.bao.bean.GoodsPrice;
import com.xr.happyFamily.bao.fragment.PingJiaFragment;
import com.xr.happyFamily.bao.fragment.ShopFragment;
import com.xr.happyFamily.bao.fragment.XiangQingFragment;
import com.xr.happyFamily.bao.view.FlowTagView;
import com.xr.happyFamily.le.BtClock.CommonClockFragment;
import com.xr.happyFamily.le.BtClock.LeFragmentManager;
import com.xr.happyFamily.le.adapter.ClickViewPageAdapter;
import com.xr.happyFamily.le.fragment.PuTongFragment;
import com.xr.happyFamily.le.fragment.QingLvFragment;
import com.xr.happyFamily.le.fragment.QunZuFragment;
import com.xr.happyFamily.le.fragment.ShiGuangFragment;
import com.xr.happyFamily.le.fragment.ZhiLaiFragment;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.le.view.NoSrcollViewPage;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
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

public class ClockActivity extends AppCompatActivity implements LeFragmentManager.CallValueValue{
    Unbinder unbinder;
    List<String> circle = new ArrayList<>();
    List<BaseFragment> fragmentList = new ArrayList<>();
    @BindView(R.id.vp_flower)
    NoSrcollViewPage vp_flower;
    @BindView(R.id.tl_flower)
    TabLayout tl_flower;


    private Context mContext=ClockActivity.this;
    SharedPreferences preferences;
    String userId;

    QunZuFragment qunZuFragment=new QunZuFragment();
    QingLvFragment qingLvFragment=new QingLvFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mContext = ClockActivity.this;
        setContentView(R.layout.activity_clock);
        ButterKnife.bind(this);

        initView();
        initData();

        Intent intent = getIntent();
        int fragid = intent.getIntExtra("fragid", 0);
    }

    private void initView() {
        circle.add("时光简记");
        circle.add("群组模式");
        circle.add("情侣模式");
        circle.add("制赖模式");
        Bundle extras = getIntent().getExtras();
        fragmentList.add(new LeFragmentManager());
        fragmentList.add(qunZuFragment);
        fragmentList.add(qingLvFragment);
        fragmentList.add(new CommonClockFragment());
        ClickViewPageAdapter tabAdapter = new ClickViewPageAdapter(getSupportFragmentManager(), fragmentList,this);
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
//        tl_flower.setTabGravity(TabLayout.GRAVITY_FILL);
//        tl_flower.setTabMode(TabLayout.MODE_SCROLLABLE);

        tl_flower.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(true);
                switch (tab.getPosition()){
                    case 0: ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#33c62b"));
                        break;
                    case 1: ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#3682ff"));
                        break;
                    case 2: ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#ff7a73"));
                        break;
                    case 3: ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#33c62b"));
                        break;
                }


                vp_flower.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(false);
                switch (tab.getPosition()){
                    case 0: ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#8c8c8c"));
                        break;
                    case 1: ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#8c8c8c"));
                        break;
                    case 2: ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#8c8c8c"));
                        break;
                    case 3: ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#8c8c8c"));
                        break;
                }

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        initTab();
    }


    public void initData(){
        preferences = this.getSharedPreferences("my", MODE_PRIVATE);
        userId= preferences.getString("userId","");

    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    public void upData(){
        qunZuFragment.upData();
    }
    public void upQinglvData(){
        qingLvFragment.upData();
    }


    @Override
    public void setPosition(int position) {
        if (position>=1){
            tl_flower.setVisibility(View.GONE);
        }else if(position==0){
            tl_flower.setVisibility(View.VISIBLE);
        }
    }
}
