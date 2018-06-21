package com.xr.happyFamily.jia;



import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.ListFragment;

import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.Fragment.BalconyFragment;
import com.xr.happyFamily.jia.Fragment.BathroomFragment;
import com.xr.happyFamily.jia.Fragment.KitchenFragment;
import com.xr.happyFamily.jia.Fragment.LivingFragment;
import com.xr.happyFamily.jia.Fragment.RoomFragment;
import com.xr.happyFamily.jia.Fragment.homeFragment;
import com.xr.happyFamily.jia.adapter.MyAdapter;
import com.xr.happyFamily.jia.adapter.TabFragmentPagerAdapter;
import com.xr.happyFamily.jia.pojo.Room;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MyPaperActivity extends AppCompatActivity {
    private ViewPager mViewpaper;
    private List<Fragment> views;
    private LayoutInflater inflater;
    private TabFragmentPagerAdapter adapter;
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
    Context context;
    ArrayList str1;
    ArrayList str2;
    int count=0;
    int count1=0;
    int count2=0;
    int count3=0;
    int count4=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpape);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        unbinder = ButterKnife.bind(this);
        str1=new ArrayList();
        str2=new ArrayList();
        roomDao=new RoomDaoImpl(getApplicationContext());
        rooms = roomDao.findByAllRoom();
        for(int i = 0 ; i < rooms.size() ; i++) {
          room = rooms.get(i);
          roomName= room.getRoomName();
          roomType= room.getRoomType();
          str1.add(roomName);
          str2.add(roomType);
        }

        Log.i("room", "---->: "+str1+".........."+str2);
        views = new ArrayList<>();
        views.add(new homeFragment());
        for (int i=0;i<str2.size();i++){
            if ("卧室".equals(str2.get(i))){
                count++;
                if (count>0){
                    count--;
                    views.add( new RoomFragment());
                }
            }
            if ("阳台".equals(str2.get(i))){
                count1++;
                if (count1>0){
                    count1--;
                    views.add( new BalconyFragment());
                }
            }
            if ("客厅".equals(str2.get(i))){
                count2++;
                if (count2>0){
                    count2--;
                    views.add( new LivingFragment());
                }

            }
                if ("厨房".equals(str2.get(i))) {
                    count3++;
                    if (count3 > 0) {
                        count3--;
                        views.add(new KitchenFragment());
                    }
                }
            if ("卫生间".equals(str2.get(i))) {
                count4++;
                if (count4 > 0) {
                    count4--;
                    views.add(new BathroomFragment());
                }
            }
        }

//        views.add(new LivingFragment());
//        views.add(new KitchenFragment());
//        views.add(new RoomFragment());
//        views.add(new BathroomFragment());
//        views.add(new BalconyFragment());
        adapter = new TabFragmentPagerAdapter(context,getSupportFragmentManager(), views);
        myViewPager.setAdapter(adapter);
        myViewPager.setCurrentItem(0);  //初始化显示第一个页面

//        Bundle bundle=getIntent().getExtras();
//        int item =bundle.getInt ("item");


    }
    public void AddFragment() {
//        views.add();
        adapter.notifyDataSetChanged();

    }

    public void DelFragment() {
        views.remove(views.size()-1);
        adapter.notifyDataSetChanged();
    }






    @Override
    protected void onRestart() {
        super.onRestart();


    }







}
