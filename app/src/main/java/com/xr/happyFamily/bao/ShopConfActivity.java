package com.xr.happyFamily.bao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class ShopConfActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.tv_conf_name)
    TextView tvConfName;
    @BindView(R.id.tv_conf_tel)
    TextView tvConfTel;
    @BindView(R.id.tv_conf_address)
    TextView tvConfAddress;
    @BindView(R.id.tv_zhifubao)
    TextView tvZhifubao;
    @BindView(R.id.tv_weixin)
    TextView tvWeixin;
    @BindView(R.id.tv_yinlian)
    TextView tvYinlian;
    @BindView(R.id.img_shop_pic)
    ImageView imgShopPic;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_shop_type)
    TextView tvShopType;
    @BindView(R.id.tv_shop_price)
    TextView tvShopPrice;
    @BindView(R.id.tv_shopcart_submit)
    TextView tvShopcartSubmit;
    @BindView(R.id.image_more_address)
    ImageView imageMoreAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_conf);
        ButterKnife.bind(this);
        titleText.setText("确认信息");
        titleRightText.setVisibility(View.GONE);
    }

    @OnClick(R.id.image_more_address)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_more_address:
                startActivity(new Intent(this, ShopAddressActivity.class));
                break;
        }
    }
}
