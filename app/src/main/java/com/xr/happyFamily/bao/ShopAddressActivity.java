package com.xr.happyFamily.bao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.AddressAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class ShopAddressActivity extends AppCompatActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.img_add)
    ImageView imgAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_address);
        ButterKnife.bind(this);

        titleText.setText("收货地址");
        titleRightText.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
//      获取数据，向适配器传数据，绑定适配器
        ArrayList<String> datas = initData();
        final AddressAdapter honmeAdapter = new AddressAdapter(ShopAddressActivity.this, datas);
        recyclerView.setAdapter(honmeAdapter);
        //      调用按钮返回事件回调的方法
        honmeAdapter.buttonSetOnclick(new AddressAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, int position) {
                honmeAdapter.setDefSelect(position);
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


    @OnClick({R.id.back, R.id.img_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_add:
                startActivity(new Intent(ShopAddressActivity.this, ShopAddAddressActivity.class));
                break;
        }
    }
}
