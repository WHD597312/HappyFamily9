package com.xr.happyFamily.bao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;

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

public class ShopSearchActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.gv_hot)
    GridView gvHot;
    @BindView(R.id.gv_history)
    GridView gvHistory;

    private String[] from = {"title"};
    private int[] to = {R.id.tv_search};
    String[] titles = new String[]{"电暖器", "空调", "智能传感器", "净水器", "除尘机"};
    String[] titles2 = new String[]{"电暖器2", "空调2", "智能传感器2", "净水器2", "除尘机2"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_search);
        ButterKnife.bind(this);
        SimpleAdapter searchAdapter_hot = new SimpleAdapter(this, getList(),
                R.layout.item_search, from, to);
        SimpleAdapter searchAdapter_his = new SimpleAdapter(this, getList2(),
                R.layout.item_search, from, to);
        gvHot.setAdapter(searchAdapter_hot);
        gvHistory.setAdapter(searchAdapter_his);

        gvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("qqqqqqqqqqqq",titles[position]);
                Toast.makeText(ShopSearchActivity.this, "i am:"+titles2[position], Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShopSearchActivity.this,ShopSearchResultActivity.class));
            }

        });

        gvHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("qqqqqqqqqqqq",titles[position]);
                Toast.makeText(ShopSearchActivity.this, "i am:"+titles[position], Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShopSearchActivity.this,ShopSearchResultActivity.class));
            }

        });



    }


    @OnClick({R.id.back, R.id.title_rightText})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title_rightText:
                break;
        }
    }


    public List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;


        for (int i = 0; i < 5; i++) {
            map = new HashMap<String, Object>();
            map.put("title", titles[i]);
            list.add(map);
        }
        return list;
    }

    public List<Map<String, Object>> getList2() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;


        for (int i = 0; i < 5; i++) {
            map = new HashMap<String, Object>();
            map.put("title", titles2[i]);
            list.add(map);
        }
        return list;
    }
}
