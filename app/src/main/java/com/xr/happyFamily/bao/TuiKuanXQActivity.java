package com.xr.happyFamily.bao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class TuiKuanXQActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.tv_wuliu)
    TextView tvWuliu;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_chexiao)
    TextView tvChexiao;
    @BindView(R.id.tv_xiugai)
    TextView tvXiugai;
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

    @BindView(R.id.tv_yuanyin)
    TextView tvYuanyin;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_time_tuikuan)
    TextView tvTimeTuikuan;
    @BindView(R.id.tv_bianhao)
    TextView tvBianhao;
    @BindView(R.id.tv_tui)
    TextView tvTui;
    @BindView(R.id.ll_tuikuan)
    LinearLayout llTuikuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_tuikuan_xq);
        ButterKnife.bind(this);
        titleRightText.setVisibility(View.GONE);
        titleText.setText("退款详情");
    }


    @OnClick({R.id.back, R.id.tv_chexiao, R.id.tv_xiugai})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_chexiao:
                tvTui.setText("因您撤销退款申请，退款已关闭");
                llTuikuan.setVisibility(View.GONE);
                break;
            case R.id.tv_xiugai:
                startActivity(new Intent(this, TuiKuanActivity.class));
                break;

        }
    }



}
