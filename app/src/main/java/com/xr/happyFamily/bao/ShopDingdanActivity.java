package com.xr.happyFamily.bao;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.DingdanAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xr.happyFamily.R.color.black;

/**
 * Created by win7 on 2018/5/22.
 */

public class ShopDingdanActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.view5)
    View view5;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    int sing_title=0;
    View[] view_title;
    TextView[] tv_title;
    ArrayList<String> datas;
    DingdanAdapter dingdanAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_dingdan);
        ButterKnife.bind(this);

        titleText.setText("我的订单");
        titleRightText.setVisibility(View.GONE);
        view_title= new View[]{view1,view2,view3,view4,view5};
        tv_title=new TextView[]{tv1,tv2,tv3,tv4,tv5};
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
//      获取数据，向适配器传数据，绑定适配器
        datas = initData("姓名");
        dingdanAdapter = new DingdanAdapter(ShopDingdanActivity.this, datas);
        recyclerview.setAdapter(dingdanAdapter);
        //      调用按钮返回事件回调的方法
        dingdanAdapter.buttonSetOnclick(new DingdanAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, int position) {
                dingdanAdapter.setDefSelect(position);
            }
        });
//        honmeAdapter.setOnItemListener(new AddressAdapter.OnItemListener() {
//            @Override
//            public void onClick(View v, int pos, String projectc) {
//                honmeAdapter.setDefSelect(pos);
//
//            }
//        });

    }

    protected ArrayList<String> initData(String name) {
        ArrayList<String> mDatas = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            mDatas.add(name+ i);
        }
        return mDatas;
    }


    @OnClick({R.id.back,R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv1:
                upData(0,"全部");
                break;
            case R.id.tv2:
                upData(1,"待付款");
                break;
            case R.id.tv3:
                upData(2,"待收货");
                break;
            case R.id.tv4:
                upData(3,"待评价");
                break;
            case R.id.tv5:
                upData(4,"退款/售后");
                break;

        }
    }

    private void upData(int i,String title){
        view_title[sing_title].setVisibility(View.INVISIBLE);
        tv_title[sing_title].setTextColor(Color.parseColor("#000000"));
        sing_title=i;
        view_title[sing_title].setVisibility(View.VISIBLE);
        tv_title[sing_title].setTextColor(Color.parseColor("#4FBA72"));
        datas.clear();
        datas.addAll(initData(title));
        dingdanAdapter.notifyDataSetChanged();
    }

}
