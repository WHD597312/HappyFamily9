package com.xr.happyFamily.bao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.WaterFallAdapter;
import com.xr.happyFamily.bean.PersonCard;
import com.xr.happyFamily.jia.AddEquipmentActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShoppageActivity extends AppCompatActivity {
    Unbinder unbinder;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private WaterFallAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_mypage);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        unbinder = ButterKnife.bind(this);
        init();
    }


//    @OnClick({R.id.bt_add_equipment})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.bt_add_equipment:
//                startActivity(new Intent(this, AddEquipmentActivity.class));
//                break;
//
//        }
//
//    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new WaterFallAdapter(this, buildData());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.image_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShoppageActivity.this, ShopCartActivity.class));
            }
        });
    }

    //生成6个明星数据，这些Url地址都来源于网络
    private List<PersonCard> buildData() {

        String[] names = {"电暖气1","电暖气2","电暖气3","电暖气4"};
        int[] imgUrs = {R.mipmap.chanpin1,R.mipmap.chanpin2,R.mipmap.chanpin3,R.mipmap.chanpin4
        };

        List<PersonCard> list = new ArrayList<>();
        for(int i=0;i<4;i++) {
            PersonCard p = new PersonCard();
            p.avatarUrl = imgUrs[i];
            p.name = names[i];
            list.add(p);
        }

        return list;
    }

}
