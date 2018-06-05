package com.xr.happyFamily.bao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.AddressAdapter;

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

public class ShopAddressActivity extends AppCompatActivity implements AddressAdapter.InnerItemOnclickListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    private AddressAdapter addressAdapter;
    private  List<Map<String, Object>> mDatas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_address);
        ButterKnife.bind(this);

        titleText.setText("收货地址");
        titleRightText.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
//      获取数据，向适配器传数据，绑定适配器
        mDatas=new ArrayList<Map<String, Object>>();
        initData("初始");
        addressAdapter = new AddressAdapter(ShopAddressActivity.this, mDatas);
        recyclerView.setAdapter(addressAdapter);
        //      调用按钮返回事件回调的方法
        addressAdapter.buttonSetOnclick(new AddressAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, int position) {
                addressAdapter.setDefSelect(position);
            }
        });
        addressAdapter.setOnItemClickListener(new AddressAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("name", mDatas.get(position).get("name").toString());
                intent.putExtra("tel", mDatas.get(position).get("tel").toString());
                intent.putExtra("address", mDatas.get(position).get("address").toString());
    /*
     * 调用setResult方法表示我将Intent对象返回给之前的那个Activity，这样就可以在onActivityResult方法中得到Intent对象，
     * 参数1：resultCode返回码，跳转之前的activity根据是这个resultCode，区分是哪一个activity返回的
     * 参数2：数据源
     */
                setResult(111, intent);
                finish();//结束当前activity
            }
        });
        addressAdapter.setOnInnerItemOnClickListener(this);
    }

    public void  initData(String name) {
        Map<String, Object> map = null;
        for (int i = 0; i < 3; i++) {
            map = new HashMap<String, Object>();
            map.put("name", name+i);
            map.put("tel", "电话"+i);
            map.put("address", "地址"+i+i+i+i+i+i+i+i+i+i+i);
            mDatas.add(map);
        }

    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.img_bianji:
                Intent intent=new Intent(this,EditAddressActivity.class);
                intent.putExtra("name",mDatas.get(position).get("name").toString());
                intent.putExtra("tel",mDatas.get(position).get("tel").toString());
                intent.putExtra("address",mDatas.get(position).get("address").toString());
                startActivity(intent);
                break;
            case R.id.img_del:
                Toast.makeText(this,"删除"+v.getTag()+"成功",Toast.LENGTH_SHORT).show();
                mDatas.clear();
                Map<String, Object> map = null;
                for (int i = 0; i < 2; i++) {
                    map = new HashMap<String, Object>();
                    map.put("name", "删除"+i);
                    map.put("tel", "电话"+i);
                    map.put("address", "地址"+i+i+i+i+i+i+i+i+i+i+i);
                    mDatas.add(map);
                }
                addressAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }


    @OnClick({R.id.back, R.id.img_add})
    public void onViewClicked(View view) {
        Log.e("qqqqqqqq..","22222");
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_add:
                startActivity(new Intent(ShopAddressActivity.this, ShopAddAddressActivity.class));
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mDatas.clear();
        initData("刷新");
        addressAdapter.notifyDataSetChanged();
    }
}
