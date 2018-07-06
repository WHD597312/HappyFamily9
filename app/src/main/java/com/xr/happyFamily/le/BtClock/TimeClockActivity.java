package com.xr.happyFamily.le.BtClock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.main.FamilyFragmentManager;
import com.xr.happyFamily.main.RoomFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class TimeClockActivity extends AppCompatActivity {
    private TimeRemFragment timeRemFragment;
    FragmentManager fragmentManager;
    private ViewPager mViewpaper;
    private FragmentPagerAdapter adapter;
    private LeFragmentManager leFragmentManager;
    private CommonClockFragment commonClockFragment;
    Unbinder unbinder;
    @BindView(R.id.id_bto_zl_img)
    ImageView id_bto_zl_img;
    @BindView(R.id.id_bto_sg_img)
    ImageView id_bto_sg_img;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_btclock);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        unbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        int fragid = intent.getIntExtra("fragid", 0);
        fragmentManager = getSupportFragmentManager();
        leFragmentManager = new LeFragmentManager();
        commonClockFragment = new CommonClockFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragid == 1) {
            fragmentTransaction.replace(R.id.layout_le_body, commonClockFragment);
            fragmentTransaction.commit();
            id_bto_zl_img.setImageResource(R.mipmap.bt_zl1);
            id_bto_sg_img.setImageResource(R.mipmap.bt_sgjj);
        } else {
            fragmentTransaction.replace(R.id.layout_le_body, leFragmentManager);
            fragmentTransaction.commit();
        }


    }





    @OnClick({R.id.id_bto_zl,R.id.id_bto_sg})
    public  void  onClick(View view){
        switch(view.getId()){
            case R.id.id_bto_zl:
                id_bto_zl_img.setImageResource(R.mipmap.bt_zl1);
                id_bto_sg_img.setImageResource(R.mipmap.bt_sgjj);
                FragmentTransaction commonTransaction = fragmentManager.beginTransaction();
                commonTransaction.replace(R.id.layout_le_body,commonClockFragment);
                commonTransaction.commit();
                break;
            case R.id.id_bto_sg:
                id_bto_zl_img.setImageResource(R.mipmap.bt_zl);
                id_bto_sg_img.setImageResource(R.mipmap.bt_sgjj1);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.layout_le_body,leFragmentManager);
                fragmentTransaction.commit();
                break;
        }
    }






    //实现页面变化监听器OnPageChangeListener
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private Context context;
        private ViewPager viewPager;
        private int size;

        public MyOnPageChangeListener(Context context, ViewPager viewPager, int size) {
            this.context = context;
            this.viewPager = viewPager;
            this.size = size;
        }

        @Override
        //当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法会一直得到调用。
        /**
         * arg0:当前页面，及你点击滑动的页面
         * arg1:当前页面偏移的百分比
         *arg2:当前页面偏移的像素位置
         */
        public void onPageScrolled(int position, float arg1, int arg2) {

        }

        @Override
        //当页面状态改变的时候调用
        /**
         * arg0
         *  1:表示正在滑动
         *  2:表示滑动完毕
         *  0:表示什么都没做，就是停在那
         */
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            Log.i("onPageScroll", "-->" + arg0);
        }

        @Override
        //页面跳转完后调用此方法
        /**
         * arg0是页面跳转完后得到的页面的Position（位置编号）。
         */
        public void onPageSelected(int poistion) {

            if (callValueValue != null) {
                callValueValue.setPosition(poistion);
            }



        }
    }
   CallValueValue callValueValue;
    public interface CallValueValue {
        public void setPosition(int position);
    }
}
