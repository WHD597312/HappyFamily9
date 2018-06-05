package com.xr.happyFamily.jia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.adapter.ManagementGridViewAdapter;
import com.xr.happyFamily.jia.pojo.Equipment;
import com.xr.happyFamily.jia.titleview.TitleView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ManagementActivity extends AppCompatActivity {
    private String[] localCartoonText = {"厨房", "厨房", "厨房", "厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房"};
    private Integer[] img ={R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,
            R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,
            R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,
            R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x};
    private GridView mGridView = null;
    private ManagementGridViewAdapter mGridViewAdapter = null;
    private ArrayList<Equipment> mGridData = null;
    Unbinder unbinder;
    TitleView titleView;
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_management);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        unbinder = ButterKnife.bind(this);
        mGridView = (GridView) findViewById(R.id.gv_management_my);
        mGridData = new ArrayList<>();
        for (int i=0; i<img.length; i++) {
            Equipment item = new Equipment();
            item.setName(localCartoonText[i]);
            item.setImgeId(img[i]);
            mGridData.add(item);
        }
        mGridViewAdapter = new ManagementGridViewAdapter(this, R.layout.activity_management_item, mGridData);
        mGridView.setAdapter(mGridViewAdapter);
        titleView = (TitleView) findViewById(R.id.title1);

        titleView.setTitleText("设备管理");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }


}
