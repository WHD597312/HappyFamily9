package com.xr.happyFamily.jia.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.SmartWheelInfo;
import com.xr.happyFamily.jia.view_custom.SemicircleBar;
import com.xr.happyFamily.jia.view_custom.SmartWheelBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DeviceDetailActivity extends AppCompatActivity {

    private List<SmartWheelInfo> infos = new ArrayList<>();
    /**
     * 抽奖的文字
     */
    private String[] mStrs = new String[]{"5", "10", "15", "20", "25", "30", "35","40"};
    @BindView(R.id.semicBar)
    SmartWheelBar semicBar;
    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_device_detail);
        unbinder=ButterKnife.bind(this);
        getBitWheelInfos();
        semicBar.setBitInfos(infos);
    }

    @Override
    protected void onStart() {
        super.onStart();
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth()-200;
        Log.w("width","width"+width);


//        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(width,width);
//        params.leftMargin=100;
//        semicBar.setLayoutParams(params);

//        semicBar.setOnSeekBarChangeListener(new SemicircleBar.OnSeekBarChangeListener() {
//            @Override
//            public void onChanged(SemicircleBar seekbar, double curValue) {
//
//            }
//        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
    public void getBitWheelInfos() {
        for (int i = 0; i < mStrs.length; i++) {
            infos.add(new SmartWheelInfo(mStrs[i], null));
        }
    }
}
