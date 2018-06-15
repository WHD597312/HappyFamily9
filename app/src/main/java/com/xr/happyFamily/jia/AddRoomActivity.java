package com.xr.happyFamily.jia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.adapter.ManagementGridViewAdapter;
import com.xr.happyFamily.jia.pojo.Equipment;
import com.xr.happyFamily.jia.titleview.TitleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddRoomActivity extends AppCompatActivity {
    private String[] localCartoonText = {"厨房", "厨房", "厨房", "厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房","厨房"};
    private Integer[] img ={R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,
            R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,
            R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,
            R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x,R.mipmap.chufang3x};
    private GridView mGridView = null;
    private ManagementGridViewAdapter mGridViewAdapter = null;
    private ArrayList<Equipment> mGridData = null;
    Unbinder unbinder;
//    @BindView(R.id.title_addroom)
    TitleView titleView;

    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);
        setContentView(R.layout.activity_home_addroom);
        unbinder = ButterKnife.bind(this);
        titleView = (TitleView) findViewById(R.id.title_addroom);
        titleView.setTitleText("添加房间");
        mGridView = (GridView) findViewById(R.id.gv_management_room);
        mGridData = new ArrayList<>();
        for (int i=0; i<img.length; i++) {
            Equipment item = new Equipment();
            item.setName(localCartoonText[i]);
            item.setImgeId(img[i]);
            mGridData.add(item);
        }
        mGridViewAdapter = new ManagementGridViewAdapter(this, R.layout.activity_management_item, mGridData);
        mGridView.setAdapter(mGridViewAdapter);
    }
}
