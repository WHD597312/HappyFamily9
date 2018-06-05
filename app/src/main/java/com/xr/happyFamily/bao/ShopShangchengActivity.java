package com.xr.happyFamily.bao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class ShopShangchengActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_shouhou)
    TextView tvShouhou;
    @BindView(R.id.tv_guanyu)
    TextView tvGuanyu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_shangcheng);
        ButterKnife.bind(this);

        titleText.setText("我的商城");
        titleRightText.setVisibility(View.GONE);


    }




    @OnClick({R.id.back,R.id.tv_address, R.id.tv_shouhou, R.id.tv_guanyu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_address:
                startActivity(new Intent(ShopShangchengActivity.this,ShopAddressActivity.class));
                break;
            case R.id.tv_shouhou:
                Toast.makeText(ShopShangchengActivity.this, "售后" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_guanyu:
                Toast.makeText(ShopShangchengActivity.this, "售后" , Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
