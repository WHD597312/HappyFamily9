package com.xr.happyFamily.jia.activity;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.SmartTerminalInfo;
import com.xr.happyFamily.jia.view_custom.SmartTerminalCircle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Demo extends AppCompatActivity {

    Unbinder unbinder;
    private List<SmartTerminalInfo> list=new ArrayList<>();
    private String[] mStrs = new String[]{"", "","",
            "", "","","",""};
    @BindView(R.id.smartTerminalCircle) SmartTerminalCircle smartTerminalCircle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        unbinder=ButterKnife.bind(this);
        getBitWheelInfos();
        smartTerminalCircle.setBitInfos(list);
    }

    @Override
    protected void onStart() {
        super.onStart();

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
            list.add(new SmartTerminalInfo(mStrs[i], BitmapFactory.decodeResource(getResources(), R.mipmap.humidifier)));
        }
    }
}
