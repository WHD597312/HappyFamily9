package com.xr.happyFamily.liwenchao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.liwenchao.adapter.GridViewAdapter;
import com.xr.happyFamily.liwenchao.pojo.Equipment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MenuActivity extends AppCompatActivity {
    private String[] localCartoonText = {"客厅", "厨房", "卧室", "阳台","阳台","阳台",};
    private Integer[] img ={R.mipmap.t3x,R.mipmap.t3x,R.mipmap.t3x,R.mipmap.t3x,R.mipmap.t3x,R.mipmap.t3x};
    private GridView mGridView = null;
    private GridViewAdapter mGridViewAdapter = null;
    private ArrayList<Equipment> mGridData = null;
    Unbinder unbinder;

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_home_mypage);
        unbinder = ButterKnife.bind(this);
        mGridView = (GridView) findViewById(R.id.gv_home_my);
        mGridData = new ArrayList<>();
        for (int i=0; i<img.length; i++) {
            Equipment item = new Equipment();
            item.setName(localCartoonText[i]);
            item.setImgeId(img[i]);
            mGridData.add(item);
        }
        mGridViewAdapter = new GridViewAdapter(this, R.layout.activity_home_item, mGridData);
        mGridView.setAdapter(mGridViewAdapter);

    }

    @OnClick({R.id.image_change})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_change:
                startActivity(new Intent(this, ChangeRoomActivity.class));
                break;

        }

    }
}
