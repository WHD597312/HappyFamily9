package com.xr.happyFamily.bao;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.CityAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class CityActivity extends Activity {

    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.tv_sheng)
    TextView tvSheng;
    @BindView(R.id.img_tuijian)
    ImageView imgTuijian;
    @BindView(R.id.rl_sheng)
    RelativeLayout rlSheng;
    @BindView(R.id.tv_shi)
    TextView tvShi;
    @BindView(R.id.img_shi)
    ImageView imgShi;
    @BindView(R.id.rl_shi)
    RelativeLayout rlShi;
    @BindView(R.id.tv_qu)
    TextView tvQu;
    @BindView(R.id.img_qu)
    ImageView imgQu;
    @BindView(R.id.rl_qu)
    RelativeLayout rlQu;
    @BindView(R.id.rl_xuanze)
    RelativeLayout rlXuanze;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_shop_city);
        mContext = CityActivity.this;
        ButterKnife.bind(this);

        ArrayList<String> datas = initData();
        CityAdapter cityAdapter = new CityAdapter(mContext, datas);
        recyclerview.setAdapter(cityAdapter);

    }


    protected ArrayList<String> initData() {
        ArrayList<String> mDatas = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            mDatas.add("A    安徽" + i);
        }
        return mDatas;
    }


    @OnClick(R.id.img_close)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                finish();
                break;

        }
    }
}
