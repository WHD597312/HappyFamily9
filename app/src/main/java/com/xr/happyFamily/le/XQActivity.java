package com.xr.happyFamily.le;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.view.FlowTagView;
import com.xr.happyFamily.jia.xnty.WaterPurifierActivity;
import com.xr.happyFamily.le.adapter.RadioEvaluateAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class XQActivity extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.img_touxiang)
    ImageView imgTouxiang;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ft_sign)
    FlowTagView ftSign;
    @BindView(R.id.rl_info)
    RelativeLayout rlInfo;
    @BindView(R.id.tv_myInfo)
    TextView tvMyInfo;
    @BindView(R.id.ll_yuanwang)
    LinearLayout llYuanwang;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.ft_love)
    FlowTagView ftLove;



    private RadioEvaluateAdapter adapter_sign,adapter_love;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuyuan_xq);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initView();
        initData();
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
               finish();
                break;

        }
    }

    private void initData() {
        List<String> list = new ArrayList();
        list.add("22岁");
        list.add("白羊座");
        list.add("5km");
        list.add("3分钟");
        adapter_sign.setItems(list);

        List<String> list2 = new ArrayList();
        list2.add("跳舞");
        list2.add("唱歌");
        list2.add("看书");
        list2.add("爬山");
        list2.add("爬山");
        adapter_love.setItems(list2);


    }

    private void initView() {
        adapter_sign = new RadioEvaluateAdapter(this, R.layout.item_xuyuan_xq);
        ftSign.setAdapter(adapter_sign);
        adapter_love = new RadioEvaluateAdapter(this, R.layout.item_xuyuan_xq);
        ftLove.setAdapter(adapter_love);
        tvMyInfo.setText("我想去杭州。");


    }




}