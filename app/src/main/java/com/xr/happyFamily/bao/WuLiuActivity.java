package com.xr.happyFamily.bao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.TimeLineAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by win7 on 2018/5/22.
 */

public class WuLiuActivity extends AppCompatActivity {

    private ListView listView;
    private TimeLineAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.test);
        listView=(ListView) findViewById(R.id.lv_list);
        listView.setDividerHeight(0);
        adapter = new TimeLineAdapter(this, initData());
        listView.setAdapter(adapter);

    }

    private List<Map<String, Object>> initData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "浙江省宁波市慈溪市宗汉街道派件员：蒋东升 13306742708正在为您派件");
        map.put("time", "2015-10-22  17:00:00");
        map.put("state","0");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "浙江省宁波市慈溪市宗汉镇 已收入");
        map.put("time", "2015-10-22  16:00:00");
        map.put("state","1");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "浙江省宁波运转中心公司 已发出");
        map.put("time", "2015-10-22  15:00:00");
        map.put("state","1");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "快件已达浙江省宁波运转中心公司");
        map.put("time", "2015-10-22  14:00:00");
        map.put("state","1");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "福建省泉州市运转中心公司 已收入");
        map.put("time", "2015-10-22  13:00:00");
        map.put("state","1");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "福建省泉州市安县公司 已打包");
        map.put("time", "2015-10-22  12:00:00");
        map.put("state","1");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "卖家已发货");
        map.put("time", "2015-10-22  11:00:00");
        map.put("state","2");
        list.add(map);

        return list;

    }


}
