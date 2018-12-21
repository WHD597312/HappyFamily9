package com.xr.happyFamily.zhen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.zhen.adapter.VersionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class VersionActivity extends AppCompatActivity{

    Unbinder unbinder;
    @BindView(R.id.rl_vers_1)
    RecyclerView recyclerView;
    VersionAdapter versionAdapter;
    private MyApplication application;
    List<String> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verson);
        unbinder= ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        list= new ArrayList<>();
        for (int i=0;i<3;i++){
            String s = i+"";
            list.add(s);
        }
        versionAdapter = new VersionAdapter(this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(versionAdapter);

    }
    @OnClick({R.id.iv_vers_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_vers_back:
                finish();
                break;
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeActivity(this);//**退出主页面*//*
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
