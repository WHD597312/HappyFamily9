package com.xr.happyFamily.login.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xr.happyFamily.R;

import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.adapter.FamilyAdapter;
import com.xr.happyFamily.jia.adapter.TabFragmentPagerAdapter;
import com.xr.happyFamily.login.login.GuideFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GuideActivity extends AppCompatActivity {
    Unbinder unbinder;
    ViewPager viewPager;
    List<Fragment> guildList;
    private qdActivity utils;
    private MyApplication application;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        setContentView(R.layout.activity_guild);
        if (application==null){
            application= (MyApplication) getApplication();
        }
        application.addActivity(this);
        utils = new qdActivity();
        utils.saveShared("tag", "tag", this);
        viewPager = (ViewPager) findViewById(R.id.guild_viewpager);
//            unbinder = ButterKnife.bind(this,view);
        guildList = new ArrayList<>();
        Bundle bundle = new Bundle();
        // 步骤5:往bundle中添加数据

        for (int i=0 ;i<3;i++){
            GuideFragment guideFragment = new GuideFragment();
            guideFragment.setType(i);
            guildList.add(guideFragment);
        }

        FragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), guildList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
