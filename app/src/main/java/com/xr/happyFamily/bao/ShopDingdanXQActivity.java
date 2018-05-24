package com.xr.happyFamily.bao;

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

public class ShopDingdanXQActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.img_wuliu)
    ImageView imgWuliu;
    @BindView(R.id.tv_wuliu)
    TextView tvWuliu;
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.img_address)
    ImageView imgAddress;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.img_shop_pic)
    ImageView imgShopPic;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_shop_type)
    TextView tvShopType;
    @BindView(R.id.tv_shop_price)
    TextView tvShopPrice;
    @BindView(R.id.tv_shop_num)
    TextView tvShopNum;
    @BindView(R.id.img_tui)
    ImageView imgTui;
    @BindView(R.id.tv_yunfei)
    TextView tvYunfei;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_dingdan)
    TextView tvDingdan;
    @BindView(R.id.tv_zhifubao)
    TextView tvZhifubao;
    @BindView(R.id.tv_changjian)
    TextView tvChangjian;
    @BindView(R.id.tv_fukuan)
    TextView tvFukuan;
    @BindView(R.id.tv_fahuo)
    TextView tvFahuo;
    @BindView(R.id.img_del)
    ImageView imgDel;
    @BindView(R.id.img_chakan)
    ImageView imgChakan;
    @BindView(R.id.img_queren)
    ImageView imgQueren;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_dingdan_xq);
        ButterKnife.bind(this);
        titleRightText.setVisibility(View.GONE);
        titleText.setText("订单详情");

    }


    @OnClick({R.id.back, R.id.img_more, R.id.img_tui, R.id.tv_dingdan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_more:
                break;
            case R.id.img_tui:
                break;
            case R.id.tv_dingdan:
                break;
        }
    }
}
