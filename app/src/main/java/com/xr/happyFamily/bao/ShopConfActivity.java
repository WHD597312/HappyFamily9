package com.xr.happyFamily.bao;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

    int sign_pay = 0;
    TextView[] tv_pay;
    Drawable pay_true, pay_false;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;

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
        tv_pay = new TextView[]{tvZhifubao, tvWeixin, tvYinlian};
        pay_true = getResources().getDrawable(R.mipmap.xuanzhong_shop3x);
        pay_false = getResources().getDrawable(R.mipmap.weixuanzhong3x);
        pay_true.setBounds(0, 0, pay_true.getMinimumWidth(), pay_true.getMinimumHeight());
        pay_false.setBounds(0, 0, pay_false.getMinimumWidth(), pay_false.getMinimumHeight());
    }

    @OnClick({R.id.rl_address, R.id.back, R.id.tv_zhifubao, R.id.tv_weixin, R.id.tv_yinlian,R.id.tv_shopcart_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_address:
                startActivityForResult(new Intent(this, ShopAddressActivity.class), 101);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_zhifubao:
                tvZhifubao.setCompoundDrawables(null, null, pay_true, null);
                tv_pay[sign_pay].setCompoundDrawables(null, null, pay_false, null);
                sign_pay = 0;
                break;
            case R.id.tv_weixin:
                tvWeixin.setCompoundDrawables(null, null, pay_true, null);
                tv_pay[sign_pay].setCompoundDrawables(null, null, pay_false, null);
                sign_pay = 1;
                break;
            case R.id.tv_yinlian:
                tvYinlian.setCompoundDrawables(null, null, pay_true, null);
                tv_pay[sign_pay].setCompoundDrawables(null, null, pay_false, null);
                sign_pay = 2;
                break;
            case R.id.tv_shopcart_submit:
                startActivityForResult(new Intent(this, PaySuccessActivity.class), 101);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 111) {
            String address = data.getStringExtra("address");// 拿到返回过来的地址
            // 把得到的数据显示到输入框内
            tvConfAddress.setText(address);

        }

    }
}
