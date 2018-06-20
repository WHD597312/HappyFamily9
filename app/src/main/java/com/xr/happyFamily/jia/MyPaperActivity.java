package com.xr.happyFamily.jia;



import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;

import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.Fragment.BalconyFragment;
import com.xr.happyFamily.jia.Fragment.BathroomFragment;
import com.xr.happyFamily.jia.Fragment.KitchenFragment;
import com.xr.happyFamily.jia.Fragment.LivingFragment;
import com.xr.happyFamily.jia.Fragment.RoomFragment;
import com.xr.happyFamily.jia.Fragment.homeFragment;
import com.xr.happyFamily.jia.adapter.TabFragmentPagerAdapter;
import com.xr.happyFamily.jia.pojo.Room;

import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MyPaperActivity extends AppCompatActivity {
    private ViewPager mViewpaper;
    private List<Fragment> views;
    private LayoutInflater inflater;
    private TabFragmentPagerAdapter adapter;
    private RoomDaoImpl roomDao;
    private List<Room> rooms;
    private Handler handler;
    String phone;
    @BindView(R.id.viewpager)
    ViewPager myViewPager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpape);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
//        roomDao=new RoomDaoImpl(getApplicationContext());
//        rooms = roomDao.findByAllRoom();
        Log.i("room", "---->: "+rooms);
        views = new ArrayList<>();
        views.add(new homeFragment());
        views.add(new LivingFragment());
        views.add(new KitchenFragment());
        views.add(new RoomFragment());
        views.add(new BathroomFragment());
        views.add(new BalconyFragment());
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), views);
        myViewPager.setAdapter(adapter);
        myViewPager.setCurrentItem(0);  //初始化显示第一个页面

    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }







}
