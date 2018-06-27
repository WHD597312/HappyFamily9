package com.xr.happyFamily.jia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.adapter.ManagementGridViewAdapter;
import com.xr.happyFamily.jia.pojo.Equipment;
import com.xr.happyFamily.jia.titleview.TitleView;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;

public class DeviceManagerActivity extends AppCompatActivity {
    private String[] localCartoonText = {"客厅", "卧室", "餐厅", "卫生间", "浴室", "厨房", "儿童房", "婴儿房", "活动室", "媒体房", "办公室", "休闲室", "书房", "工作室", "衣帽间", "后院"};
    private Integer[] img = {R.mipmap.chufang3x, R.mipmap.chufang3x, R.mipmap.chufang3x, R.mipmap.chufang3x,
            R.mipmap.chufang3x, R.mipmap.chufang3x, R.mipmap.chufang3x, R.mipmap.chufang3x,
            R.mipmap.chufang3x, R.mipmap.chufang3x, R.mipmap.chufang3x, R.mipmap.chufang3x,
            R.mipmap.chufang3x, R.mipmap.chufang3x, R.mipmap.chufang3x, R.mipmap.chufang3x};
    private GridView mGridView = null;
    private ManagementGridViewAdapter mGridViewAdapter = null;
    private ArrayList<Equipment> mGridData = null;
    Unbinder unbinder;
    TitleView titleView;


    private List<String> titleList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_management);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        unbinder = ButterKnife.bind(this);
        mGridView = (GridView) findViewById(R.id.gv_management_my);
        mGridData = new ArrayList<>();
        for (int i = 0; i < img.length; i++) {
            Equipment item = new Equipment();
            item.setName(localCartoonText[i]);
            titleList.add(localCartoonText[i]);
            item.setImgeId(img[i]);
            mGridData.add(item);
        }
        mGridViewAdapter = new ManagementGridViewAdapter(this, R.layout.activity_management_item, mGridData);
        mGridView.setAdapter(mGridViewAdapter);
        titleView = (TitleView) findViewById(R.id.title1);
        titleView.setTitleText("设备管理");
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @OnClick({R.id.bt_management_ok})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_management_ok:
                Intent intent=new Intent(this,SmartTerminalActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

}
