package com.xr.happyFamily.main;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.BitmapCompressUtils;
import com.xr.happyFamily.together.util.mqtt.MQService;
import com.xr.happyFamily.together.util.receiver.MQTTMessageReveiver;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements FamilyFragmentManager.CallValueValue {

    Unbinder unbinder;
    FragmentManager fragmentManager;
    private HourseDaoImpl hourseDao;
    SharedPreferences mPositionPreferences;
    @BindView(R.id.layout_bottom)
    LinearLayout layout_bottom;
    @BindView(R.id.id_bto_jia_img)
    ImageButton id_bto_jia_img;
    @BindView(R.id.id_bto_bao_img)
    ImageButton id_bto_bao_img;
    @BindView(R.id.id_bto_zhen_img) ImageButton id_bto_zhen_img;/**朕*/
    private FamilyFragmentManager familyFragmentManager;
    private BaoFragment baoFragment;/**宝的页面*/
    private ZhenFragment zhenFragment;/**朕的页面*/
    private MyApplication application;
    SharedPreferences preferences;

    //其他activity跳转回主界面时的标记
    private String sign = "0";

    private MQTTMessageReveiver myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main_activity);
        unbinder = ButterKnife.bind(this);
        if (application == null) {
            application = (MyApplication) getApplication();
            application.addActivity(this);
            Intent service=new Intent(this, MQService.class);
            startService(service);
        }


        preferences = getSharedPreferences("my", MODE_PRIVATE);


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new MQTTMessageReveiver();
        this.registerReceiver(myReceiver, filter);

        fragmentManager = getSupportFragmentManager();
        hourseDao = new HourseDaoImpl(getApplicationContext());
        List<Hourse> hourses = hourseDao.findAllHouse();
        Intent intent = getIntent();
        long houseId = intent.getLongExtra("houseId", 0);
        if (houseId == 0) {
            Hourse hourse = hourses.get(0);

            houseId = hourse.getHouseId();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        familyFragmentManager = new FamilyFragmentManager();
        baoFragment = new BaoFragment();
        zhenFragment=new ZhenFragment();


        Bundle bundle = new Bundle();
        bundle.putLong("houseId", houseId);
        familyFragmentManager.setArguments(bundle);
        fragmentTransaction.replace(R.id.layout_body, familyFragmentManager);
        fragmentTransaction.commit();
        mPositionPreferences = getSharedPreferences("position", Context.MODE_PRIVATE);
        sign = intent.getStringExtra("sign");
        //从支付成功跳回主界面时，打开商城fragment
        if ("PaySuccess".equals(sign)) {
            id_bto_jia_img.setImageResource(R.mipmap.jia);
            id_bto_bao_img.setImageResource(R.mipmap.bao1);
            FragmentTransaction baoTransaction = fragmentManager.beginTransaction();
            baoTransaction.replace(R.id.layout_body, baoFragment);
            baoTransaction.commit();
            if (mPositionPreferences.contains("position")) {
                mPositionPreferences.edit().clear().commit();
            }
        }
        if (preferences.contains("headImgUrl")){
            new LoadUserImageAsync().execute();
        }
    }

    @OnClick({R.id.id_bto_jia, R.id.id_bto_bao,R.id.id_bto_zhen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_bto_jia:
                if (mPositionPreferences.contains("position")) {
                    mPositionPreferences.edit().clear().commit();
                }
                List<Hourse> hourses = hourseDao.findAllHouse();
                Hourse hourse = hourses.get(0);
                long houseId = hourse.getHouseId();
                Bundle bundle = new Bundle();
                bundle.putLong("houseId", houseId);
                familyFragmentManager = new FamilyFragmentManager();
                familyFragmentManager.setArguments(bundle);
                FragmentTransaction familyTransaction = fragmentManager.beginTransaction();
                familyTransaction.replace(R.id.layout_body, familyFragmentManager);
                familyTransaction.commit();
                id_bto_jia_img.setImageResource(R.mipmap.jia1);
                id_bto_bao_img.setImageResource(R.mipmap.bao);
                id_bto_zhen_img.setImageResource(R.mipmap.zhen);
                break;
            case R.id.id_bto_bao:
                id_bto_jia_img.setImageResource(R.mipmap.jia);
                id_bto_bao_img.setImageResource(R.mipmap.bao1);
                id_bto_zhen_img.setImageResource(R.mipmap.zhen);
                FragmentTransaction baoTransaction = fragmentManager.beginTransaction();
                baoTransaction.replace(R.id.layout_body, baoFragment);
                baoTransaction.commit();
                if (mPositionPreferences.contains("position")) {
                    mPositionPreferences.edit().clear().commit();
                }
                break;
            case R.id.id_bto_zhen:
                id_bto_jia_img.setImageResource(R.mipmap.jia);
                id_bto_bao_img.setImageResource(R.mipmap.bao);
                id_bto_zhen_img.setImageResource(R.mipmap.zhen1);
                FragmentTransaction zhenTransaction = fragmentManager.beginTransaction();
                zhenTransaction.replace(R.id.layout_body, zhenFragment);
                zhenTransaction.commit();
                if (mPositionPreferences.contains("position")) {
                    mPositionPreferences.edit().clear().commit();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeAllActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mPositionPreferences.contains("position")) {
            mPositionPreferences.edit().clear().commit();
        }
        if (myReceiver!=null){
            unregisterReceiver(myReceiver);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 6000) {
            String room = data.getStringExtra("room");
            if (!TextUtils.isEmpty(room)) {
                mPositionPreferences = getSharedPreferences("position", Context.MODE_PRIVATE);
                if (mPositionPreferences.contains("position")) {
                    mPositionPreferences.edit().clear().commit();
                }
            }
            long houseId = data.getLongExtra("houseId", 0);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FamilyFragmentManager familyFragmentManager = new FamilyFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putLong("houseId", houseId);
            familyFragmentManager.setArguments(bundle);
            fragmentTransaction.replace(R.id.layout_body, familyFragmentManager);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void setPosition(int position) {
        Log.i("position222", "-->" + position);
        if (position >= 1) {
            SharedPreferences.Editor editor = mPositionPreferences.edit();
            editor.putInt("position", position);
            editor.commit();
            layout_bottom.setVisibility(View.GONE);
        } else if (position == 0) {
            layout_bottom.setVisibility(View.VISIBLE);
        }
    }
    class LoadUserImageAsync extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap bitmap=null;
            try {
                String token = preferences.getString("token", "token");
                String url=preferences.getString("headImgUrl","");
                GlideUrl glideUrl=new GlideUrl(url,new LazyHeaders.Builder().addHeader("authorization",token).build());
                bitmap= Glide.with(MainActivity.this)
                        .load(glideUrl)
                        .asBitmap()
                        .centerCrop()
                        .into(180,180)
                        .get();
                if (bitmap!=null){
                    File file= BitmapCompressUtils.compressImage(bitmap);
                    preferences.edit().putString("image",file.getPath()).commit();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
