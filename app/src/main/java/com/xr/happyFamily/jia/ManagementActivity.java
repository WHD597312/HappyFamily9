package com.xr.happyFamily.jia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.activity.QRScannerActivity;
import com.xr.happyFamily.jia.adapter.ManagementGridViewAdapter;
import com.xr.happyFamily.jia.pojo.Equipment;
import com.xr.happyFamily.jia.titleview.TitleView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ManagementActivity extends AppCompatActivity {
    private String[] localCartoonText = {"厨房", "卧室", "客厅", "卫生间","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房"};
    private Integer[] img ={R.mipmap.chufang3x,R.mipmap.house_ws,R.mipmap.house_kt,R.mipmap.house_wsj,
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
    @OnClick({R.id.bt_management_ok,R.id.iv_mzxing})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_management_ok:
                startActivity(new Intent(this, AddEquipmentActivity.class));
                break;
            case R.id.iv_mzxing:
                startActivity(new Intent(this, QRScannerActivity.class));
                break;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }


}
