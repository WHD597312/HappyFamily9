package com.xr.happyFamily.bao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class TuiKuanSuccessActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.img_choose)
    ImageView imgChoose;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_tuikuan)
    TextView tvTuikuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        setContentView(R.layout.activity_shop_tuikuan);
        ButterKnife.bind(this);
        titleText.setText("退款成功");

        Bundle bundle=getIntent().getExtras();
        int sign=bundle.getInt("sign");
        int money=bundle.getInt("money");
        String time=bundle.getString("time");
        if(sign==2){
            tvTuikuan.setText("退款失败");
            titleText.setText("退款失败");
            imgChoose.setImageResource(R.mipmap.ic_pay_result_fail);
        }
        tvPrice.setText("退款金额：¥"+money);
        tvTime.setText(time);

    }


    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;


        }
    }


}
