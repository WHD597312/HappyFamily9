package com.xr.happyFamily.bao;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.AddressAdapter;
import com.xr.happyFamily.bao.adapter.ViewPagerAdapter;
import com.xr.happyFamily.le.view.KeywordsFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by win7 on 2018/5/22.
 */

public class TestActivity extends AppCompatActivity {

    private KeywordsFlow keywordsFlow;
    private String[] keywords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        initView();
        refreshTags();
    }

    private void initView() {
        keywords= new String[]{"我想放假", "我想看电影", "我想去旅游", "我想啪啪啪", "我想放假", "我想看电影", "我想去旅游", "我想啪啪啪", "我想放假", "我想看电影", "我想去旅游", "我想啪啪啪"};
        keywordsFlow = (KeywordsFlow) findViewById(R.id.keywordsflow);

    }

    private void refreshTags() {
        keywordsFlow.setDuration(800l);
        keywordsFlow.setOnItemClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String keyword = ((TextView) v).getText().toString();// 获得点击的标签
                Log.e("qqqqqqqqqqqqq",keyword);
            }
        });
        // 添加
        feedKeywordsFlow(keywordsFlow, keywords);
        keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
    }

    private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {
        Random random = new Random();
        for (int i = 0; i < KeywordsFlow.MAX; i++) {
            int ran = random.nextInt(arr.length);
            String tmp = arr[ran];
            keywordsFlow.feedKeyword(tmp);
        }
    }

}