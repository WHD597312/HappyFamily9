package com.xr.happyFamily.liwenchao.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.xr.happyFamily.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.gv_home)
    GridView gridView;
    private Context mContext;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        mContext = HomeActivity.this;
        initData();

        String[] from={"text1","text2","text3","img"};

        int[] to={R.id.tv_home_1,R.id.tv_home_2,R.id.tv_home_3,R.id.iv_home};

        adapter=new SimpleAdapter(this, dataList, R.layout.activity_home_item, from, to);

        gridView.setAdapter(adapter);


    }
    private void initData() {
        //图标
        int icno[] = { R.mipmap.t3x, R.mipmap.t3x, R.mipmap.t3x,R.mipmap.t3x
        };

        String text1[]={"卧室","客厅","阳台","厨房"};
        String text2[]={"卧室","客厅","阳台","厨房"};
        String text3[]={"卧室","客厅","阳台","厨房"};
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <icno.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", icno[i]);
            map.put("text1",text1[i]);
            map.put("text2",text1[i]);
            map.put("text3",text1[i]);

            dataList.add(map);
        }
    }
}
