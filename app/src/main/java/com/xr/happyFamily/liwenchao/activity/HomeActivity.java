package com.xr.happyFamily.liwenchao.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.GridView;


import com.xr.happyFamily.R;
import com.xr.happyFamily.liwenchao.adapter.GridViewAdapter;
import com.xr.happyFamily.liwenchao.pojo.Equipment;

import java.util.ArrayList;




public class HomeActivity extends AppCompatActivity {

    private String[] localCartoonText = {"客厅", "厨房", "卧室", "阳台","阳台","阳台",};

    private Integer[] img ={R.mipmap.t3x,R.mipmap.t3x,R.mipmap.t3x,R.mipmap.t3x,R.mipmap.t3x,R.mipmap.t3x};
    private GridView mGridView = null;
    private GridViewAdapter mGridViewAdapter = null;
    private ArrayList<Equipment> mGridData = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_home_main);

        mGridView = (GridView) findViewById(R.id.gv_home);
        mGridData = new ArrayList<>();
        for (int i=0; i<img.length; i++) {
            Equipment item = new Equipment();
            item.setName(localCartoonText[i]);
            item.setImgeId(img[i]);
            mGridData.add(item);
        }
        mGridViewAdapter = new GridViewAdapter(this, R.layout.activity_home_item, mGridData);
        mGridView.setAdapter(mGridViewAdapter);


    }
}
























    /*private GridView gridView;

    private String[] from = { "image", "text1"};
    private int[] to = { R.id.image, R.id.title };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_home_main);
        GridView gridView = (GridView) findViewById(R.id.gv_home);
        SimpleAdapter pictureAdapter = new SimpleAdapter(this, getList(),
                R.layout.activity_home_item, from, to);
        gridView.setAdapter(pictureAdapter);

    }
*/




    /*public List<Map<String, Object>> getList() {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = null;

        String[] title1 = new String[] { "客厅", "智能设备", "开关"," " };


        Integer[] images = { R.mipmap.t3x, R.mipmap.t3x,
                R.mipmap.t3x, R.mipmap.t3x, };

        for (int i = 0; i < images.length; i++) {
            map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("text1", title1[i]);


            list.add(map);
        }

        return list;
    }*/


