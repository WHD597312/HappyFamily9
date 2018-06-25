package com.xr.happyFamily.le;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.EditAddressActivity;
import com.xr.happyFamily.le.adapter.HistoryAdapter;
import com.xr.happyFamily.le.adapter.YouYuanAdapter;

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

public class HistoryActivity extends AppCompatActivity implements YouYuanAdapter.InnerItemOnclickListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<Map<String, Object>> mDatas;
    private HistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipei_history);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        titleText.setVisibility(View.GONE);
        imgRight.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
//      获取数据，向适配器传数据，绑定适配器
        mDatas = new ArrayList<Map<String, Object>>();
        initData();
        mAdapter = new HistoryAdapter(HistoryActivity.this, mDatas);
        recyclerView.setAdapter(mAdapter);
        //      调用按钮返回事件回调的方法
//        mAdapter.buttonSetOnclick(new YouYuanAdapter.ButtonInterface() {
//            @Override
//            public void onclick(View view, int position) {
//                mAdapter.setDefSelect(position);
//            }
//        });
//        mAdapter.setOnItemClickListener(new YouYuanAdapter.MyItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                startActivity(new Intent(HistoryActivity.this, XQActivity.class));
//
//            }
//        });
//        youYuanAdapter.setOnInnerItemOnClickListener(this);

    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.img_add:
                Intent intent = new Intent(this, EditAddressActivity.class);
                intent.putExtra("name", mDatas.get(position).get("name").toString());
                intent.putExtra("tel", mDatas.get(position).get("tel").toString());
                intent.putExtra("address", mDatas.get(position).get("address").toString());
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    int[] touxiang = {R.mipmap.ic_touxiang_pipei_history, R.mipmap.ic_touxiang_pipei_history, R.mipmap.ic_touxiang_pipei_history, R.mipmap.ic_touxiang_pipei_history};

    public void initData() {
        Map<String, Object> map = null;
        for (int i = 0; i < 4; i++) {
            map = new HashMap<String, Object>();
            map.put("time", "11:30" + i);
            map.put("day",  i);
            map.put("month",i+"月");
            map.put("touxiang", touxiang[i]);
            mDatas.add(map);
        }

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