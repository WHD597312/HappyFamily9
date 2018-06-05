package com.xr.happyFamily.bao;

import android.content.Intent;
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

public class FuWuActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
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
    @BindView(R.id.img_tuikuan)
    ImageView imgTuikuan;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.rl_tuikuan)
    RelativeLayout rlTuikuan;
    @BindView(R.id.img_tuihuo)
    ImageView imgTuihuo;
    @BindView(R.id.tv_type2)
    TextView tvType2;
    @BindView(R.id.rl_tuihuo)
    RelativeLayout rlTuihuo;
    int type_tui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_fuwu);
        ButterKnife.bind(this);
        titleRightText.setVisibility(View.GONE);
        titleText.setText("服务类型");

    }


    @OnClick({R.id.back, R.id.rl_tuihuo, R.id.rl_tuikuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rl_tuikuan:
                type_tui=1;
                Intent intent=new Intent(this,TuiKuanActivity.class);
                intent.putExtra("type",type_tui);

                startActivity(intent);
                break;
            case R.id.rl_tuihuo:
                type_tui=2;
                Intent intent2=new Intent(this,TuiKuanActivity.class);
                intent2.putExtra("type",type_tui);
                startActivity(intent2);
                break;
        }
    }
}
