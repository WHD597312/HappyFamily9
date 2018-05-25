package com.xr.happyFamily.bao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.DingdanAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class ShopDingdanActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.view5)
    View view5;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_dingdan);
        ButterKnife.bind(this);

        titleText.setText("我的订单");
        titleRightText.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
//      获取数据，向适配器传数据，绑定适配器
        ArrayList<String> datas = initData();
        final DingdanAdapter dingdanAdapter = new DingdanAdapter(ShopDingdanActivity.this, datas);
        recyclerview.setAdapter(dingdanAdapter);
        //      调用按钮返回事件回调的方法
        dingdanAdapter.buttonSetOnclick(new DingdanAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, int position) {
                dingdanAdapter.setDefSelect(position);
            }
        });
//        honmeAdapter.setOnItemListener(new AddressAdapter.OnItemListener() {
//            @Override
//            public void onClick(View v, int pos, String projectc) {
//                honmeAdapter.setDefSelect(pos);
//
//            }
//        });

    }

    protected ArrayList<String> initData() {
        ArrayList<String> mDatas = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            mDatas.add("姓名" + i);
        }
        return mDatas;
    }


    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

        }
    }
}
