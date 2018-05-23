package com.xr.happyFamily.bao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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

public class ShopAddAddressActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.img_choose)
    ImageView imgChoose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_add_address);
        ButterKnife.bind(this);

        titleText.setText("添加地址");
        titleRightText.setText("保存");


    }




    @OnClick({R.id.back, R.id.img_choose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_choose:
                break;
        }
    }

//    @OnClick({R.id.back, R.id.img_choose})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.back:
//                break;
//            case R.id.img_choose:
//                break;
//        }
//    }
}
