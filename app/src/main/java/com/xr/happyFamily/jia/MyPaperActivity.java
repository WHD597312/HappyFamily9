package com.xr.happyFamily.jia;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MyPaperActivity extends AppCompatActivity {
    private ViewPager mViewpaper;
    private List<Fragment> views;
    private LayoutInflater inflater;
    private FragmentPagerAdapter adapter;
    private RoomDaoImpl roomDao;
    private List<Room> rooms;
    private Handler handler;
    String phone;
    Unbinder unbinder;
    @BindView(R.id.viewpager)
    ViewPager myViewPager;
    Room room;
    String roomName;
    String roomType;
    long roomId;
    Context context;
    ArrayList str1;
    ArrayList str2;
    ArrayList str3;
    int count=0;
    int z=0;
    int count1=0;
    int count2=0;
    int count3=0;
    int count4=0;
    int position;
    int p;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpape);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        unbinder = ButterKnife.bind(this);
      str1=new ArrayList();
       str2=new ArrayList();
        str3=new ArrayList();
        roomDao=new RoomDaoImpl(getApplicationContext());
        rooms = roomDao.findByAllRoom();
        str1.add("首页");
        str2.add("首页");
        str3.add("25");
        for(int i = 0 ; i < rooms.size() ; i++) {
          room = rooms.get(i);
          roomName= room.getRoomName();
          roomType= room.getRoomType();
          roomId=room.getRoomId();
          str1.add(roomName);
          str2.add(roomType);
          str3.add(roomId);
        }
        Intent intent=getIntent();
        Intent intent1=getIntent();
        position =intent.getIntExtra("position",0);
        int position1 =intent1.getIntExtra("weizhi",position);
        Log.i("ttttt", "---->"+position);
        Log.i("room", "---->: "+str1+".........."+str2+"...."+str3);
        views = new ArrayList<>();
        for (int i=0;i<str3.size();i++){
            Log.i("22222222", "onCreate:---> "+i+"...."+str2.get(i));
            if ("首页".equals(str2.get(i))){
                Bundle bundle=new Bundle();
                bundle.putString("roomName", "首页");
                bundle.putString("roomType", "首页");
                bundle.putString("roomId","25");
                homeFragment homeFragment=new homeFragment();
                homeFragment.setArguments(bundle);
                views.add(homeFragment);
            }
            else if ("厨房".equals(str2.get(i))) {

                Bundle bundle=new Bundle();
                bundle.putString("roomName",str1.get(i).toString());
                bundle.putString("roomType",str2.get(i).toString());
                bundle.putString("roomId",str3.get(i).toString());
                KitchenFragment kitchenFragment=new KitchenFragment();
                kitchenFragment.setArguments(bundle);
                views.add(kitchenFragment);
            }
           else if ("卧室".equals(str2.get(i))){
                Bundle bundle=new Bundle();
                bundle.putString("roomName",str1.get(i).toString());
                bundle.putString("roomType",str2.get(i).toString());
                bundle.putString("roomId",str3.get(i).toString());
                RoomFragment roomFragmen=new RoomFragment();
                roomFragmen.setArguments(bundle);
                views.add( roomFragmen);
            }
            else if ("阳台".equals(str2.get(i))){
                Bundle bundle=new Bundle();
                bundle.putString("roomName",str1.get(i).toString());
                bundle.putString("roomType",str2.get(i).toString());
                bundle.putString("roomId",str3.get(i).toString());
                BalconyFragment balconyFragment=new BalconyFragment();
                balconyFragment.setArguments(bundle);
                views.add( balconyFragment);
            }
            else if ("客厅".equals(str2.get(i))){
                Bundle bundle=new Bundle();
                bundle.putString("roomName",str1.get(i).toString());
                bundle.putString("roomType",str2.get(i).toString());
                bundle.putString("roomId",str3.get(i).toString());
                LivingFragment livingFragment=new LivingFragment();
                livingFragment.setArguments(bundle);
                views.add( livingFragment);
            }

            else if ("卫生间".equals(str2.get(i))) {
                Bundle bundle=new Bundle();
                bundle.putString("roomName",str1.get(i).toString());
                bundle.putString("roomType",str2.get(i).toString());
                bundle.putString("roomId",str3.get(i).toString());
                BathroomFragment bathroomFragment=new BathroomFragment();
                bathroomFragment.setArguments(bundle);
                    views.add(bathroomFragment);
            }
        }

//        views.add(new LivingFragment());
//        views.add(new KitchenFragment());
//        views.add(new RoomFragment());
//        views.add(new BathroomFragment());
//        views.add(new BalconyFragment());
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), views);
        myViewPager.setAdapter(adapter);
        myViewPager.setCurrentItem(0);  //初始化显示第一个页面
        myViewPager.setCurrentItem(position);
        myViewPager.setCurrentItem(position1);


        myViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            p = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==5000){
            myViewPager.setCurrentItem(0);
        }

        if (resultCode==6000){
//            String s=data.getStringExtra("roomId");
//            long roomId=Long.parseLong(s);
//            Room room=roomDao.findById(roomId);
//            String roomName=room.getRoomName();

            if (position!=0){
                myViewPager.setCurrentItem(position);
            }else {
                myViewPager.setCurrentItem(p);
            }
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }







}
