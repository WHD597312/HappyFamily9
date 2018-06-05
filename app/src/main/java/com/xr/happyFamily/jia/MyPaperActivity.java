package com.xr.happyFamily.jia;



import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.Fragment.BalconyFragment;
import com.xr.happyFamily.jia.Fragment.BathroomFragment;
import com.xr.happyFamily.jia.Fragment.KitchenFragment;
import com.xr.happyFamily.jia.Fragment.LivingFragment;
import com.xr.happyFamily.jia.Fragment.RoomFragment;
import com.xr.happyFamily.jia.Fragment.homeFragment;
import com.xr.happyFamily.jia.adapter.TabFragmentPagerAdapter;
import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.List;


public class MyPaperActivity extends AppCompatActivity {
    private ViewPager mViewpaper;
    private List<Fragment> views;
    private LayoutInflater inflater;
    private ViewPager myViewPager;
    private TabFragmentPagerAdapter adapter;
    private Handler handler;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpape);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        init();
        views = new ArrayList<>();
        views.add(new homeFragment());
        views.add(new LivingFragment());
        views.add(new KitchenFragment());
        views.add(new RoomFragment());
        views.add(new BathroomFragment());
        views.add(new BalconyFragment());

        myViewPager.setCurrentItem(0);  //初始化显示第一个页面

        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), views);
        myViewPager.setAdapter(adapter);

    }

    private void init() {//初始化list集合
        // TODO Auto-generated method stub
        myViewPager = (ViewPager) findViewById(R.id.viewpager);


            }



}
