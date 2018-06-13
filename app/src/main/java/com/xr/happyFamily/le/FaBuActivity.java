package com.xr.happyFamily.le;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.le.adapter.EvaluateAdapter;
import com.xr.happyFamily.bao.view.FlowTagView;
import com.xr.happyFamily.le.adapter.GridViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class FaBuActivity extends AppCompatActivity {


    @BindView(R.id.view_dis)
    View viewDis;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_old)
    TextView tvOld;
    @BindView(R.id.tv_xingzuo)
    TextView tvXingzuo;
    @BindView(R.id.tv_shengri)
    TextView tvShengri;
    @BindView(R.id.img_touxiang)
    ImageView imgTouxiang;
    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.gv_type)
    GridView gvType;
    @BindView(R.id.img_fabu)
    ImageView imgFabu;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.ft_price)
    FlowTagView ftPrice;

    private EvaluateAdapter adapter_price;
    String[] titles = new String[]{"旅游类", "产品类", "旅游类", "旅游类", "旅游类","  其他  "};
    GridViewAdapter typeAdapter;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy_fabu);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initView();
        initData();
    }

    private void initData() {
        List<String> list = new ArrayList();

        list.add("100以内");
        list.add("100-300元");
        list.add("300-500元");
        list.add("500-1000元");
        list.add("1000元以上");
        adapter_price.setItems(list);

    }

    private void initView() {
        adapter_price = new EvaluateAdapter(this, R.layout.item_fabu_yuanwang);
        ftPrice.setAdapter(adapter_price);
        ftPrice.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                String e = adapter_price.getItem(position).toString();
                Toast.makeText(FaBuActivity.this, "i am:" + e, Toast.LENGTH_SHORT).show();
                adapter_price.setSelection(position);
            }
        });
        typeAdapter = new GridViewAdapter(this, getList(),R.layout.item_fabu_yuanwang);
        gvType.setAdapter(typeAdapter);
        gvType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FaBuActivity.this,titles[position],Toast.LENGTH_SHORT).show();
                typeAdapter.changeState(position);
            }
        });


    }

    public List<String> getList() {
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < 6; i++) {


            list.add(titles[i]);
        }
        return list;
    }

    @OnClick({R.id.img_close, R.id.view_dis, R.id.img_fabu,R.id.img_touxiang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_dis:
            case R.id.img_close:
                finish();
                break;

            case R.id.img_fabu:

                break;
            case R.id.img_touxiang:

                Log.e("qqqqqqqqq","1111111111");
                break;
        }
    }
}