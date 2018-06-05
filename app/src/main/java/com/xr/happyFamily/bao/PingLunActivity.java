package com.xr.happyFamily.bao;

import android.content.Intent;
import android.graphics.Color;
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

public class PingLunActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.img_xing1)
    ImageView imgXing1;
    @BindView(R.id.img_xing2)
    ImageView imgXing2;
    @BindView(R.id.img_xing3)
    ImageView imgXing3;
    @BindView(R.id.img_xing4)
    ImageView imgXing4;
    @BindView(R.id.img_xing5)
    ImageView imgXing5;
    @BindView(R.id.img_choose)
    ImageView imgChoose;

    boolean isChoose = true;
    @BindView(R.id.tv_level)
    TextView tvLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_pinglun_fabiao);
        ButterKnife.bind(this);
        titleRightText.setText("发布");
        titleRightText.setTextColor(Color.parseColor("#4FBA72"));
        titleText.setText("发表评价");


    }


    @OnClick({R.id.back, R.id.img_xing1, R.id.title_rightText, R.id.img_xing2, R.id.img_xing3, R.id.img_xing4, R.id.img_xing5, R.id.img_choose,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title_rightText:
                startActivity(new Intent(this,PingLunSuccessActivity.class));
                break;
            case R.id.img_xing1:
                imgXing2.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                imgXing3.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                imgXing4.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                imgXing5.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                tvLevel.setText("非常差");
                break;
            case R.id.img_xing2:
                imgXing2.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                imgXing3.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                imgXing4.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                imgXing5.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                tvLevel.setText("差");
                break;
            case R.id.img_xing3:
                imgXing2.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                imgXing3.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                imgXing4.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                imgXing5.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                tvLevel.setText("一般");
                break;
            case R.id.img_xing4:
                imgXing2.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                imgXing3.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                imgXing4.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                imgXing5.setImageResource(R.mipmap.ic_tuihuo_xingxing_false);
                tvLevel.setText("好");
                break;
            case R.id.img_xing5:
                imgXing2.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                imgXing3.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                imgXing4.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                imgXing5.setImageResource(R.mipmap.iv_tuihuo_xing_true);
                tvLevel.setText("非常好");
                break;

            case R.id.img_choose:
                if (isChoose) {
                    imgChoose.setImageResource(R.mipmap.weixuanzhong3x);
                    isChoose = false;
                } else {
                    imgChoose.setImageResource(R.mipmap.xuanzhong3x);
                    isChoose = true;
                }
                break;


        }
    }


}
