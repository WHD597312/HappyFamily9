package com.xr.happyFamily.bao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.PingLunListAdapter;

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

public class WoDePingLunActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    ArrayList<Map<String, Object>> mDatas;
    PingLunListAdapter pingLunListAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_wode_pinglun);
        ButterKnife.bind(this);
        titleText.setText("我的评论");
        mDatas=new ArrayList<Map<String, Object>>();
        mDatas = initData("姓名");
        pingLunListAdapter = new PingLunListAdapter(WoDePingLunActivity.this, mDatas);
        //      调用按钮返回事件回调的方法
        pingLunListAdapter.buttonSetOnclick(new PingLunListAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, int position) {
                pingLunListAdapter.setDefSelect(position);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(pingLunListAdapter);


    }


    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;


        }
    }

    protected ArrayList<Map<String, Object>> initData(String name) {
        ArrayList<Map<String, Object>> mDatas = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < 3; i++) {
            Map<String, Object> maps = new HashMap<>();
            maps.put("name", name + i);
            maps.put("type", i);
            mDatas.add(maps);
        }
        return mDatas;
    }
}
