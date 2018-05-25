package com.xr.happyFamily.bao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.WaterFallAdapter;
import com.xr.happyFamily.bean.PersonCard;

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

public class ShopSearchResultActivity extends AppCompatActivity {

    @BindView(R.id.image_search)
    ImageView imageSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.img_zonghe)
    ImageView imgZonghe;
    @BindView(R.id.rl_zonghe)
    RelativeLayout rlZonghe;
    @BindView(R.id.img_jiage)
    ImageView imgJiage;
    @BindView(R.id.rl_jiage)
    RelativeLayout rlJiage;
    @BindView(R.id.img_pinpai)
    ImageView imgPinpai;
    @BindView(R.id.rl_pinpai)
    RelativeLayout rlPinpai;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.ll_nodata)
    LinearLayout llNodata;
    @BindView(R.id.gv_more)
    GridView gvMore;
    @BindView(R.id.ll_gv)
    LinearLayout llGv;
    @BindView(R.id.view_zhe)
    View viewZhe;
    private RecyclerView.LayoutManager mLayoutManager;
    private WaterFallAdapter mAdapter;

    private String[] from = {"title"};
    private int[] to = {R.id.tv_search};
    String[] titles = new String[]{"从低到高", "从高到低"};
    String[] titles2 = new String[]{"松下", "小米", "海尔", "格力", "松下"};
    SimpleAdapter jiageAdapter;
    SimpleAdapter pinpaiAdapter;
    private boolean isMore = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_search_result);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new WaterFallAdapter(this, buildData());

        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(mAdapter);

        jiageAdapter = new SimpleAdapter(this, getList(),
                R.layout.item_search_result, from, to);
        pinpaiAdapter = new SimpleAdapter(this, getList2(),
                R.layout.item_search_result, from, to);
    }


    private List<PersonCard> buildData() {

        String[] names = {"电暖气1", "电暖气2", "电暖气3", "电暖气4"};
        int[] imgUrs = {R.mipmap.chanpin1, R.mipmap.chanpin2, R.mipmap.chanpin3, R.mipmap.chanpin4
        };

        List<PersonCard> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            PersonCard p = new PersonCard();
            p.avatarUrl = imgUrs[i];
            p.name = names[i];
            list.add(p);
        }

        return list;
    }

    @OnClick({R.id.tv_search, R.id.back, R.id.rl_zonghe, R.id.rl_jiage, R.id.rl_pinpai,R.id.view_zhe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                startActivity(new Intent(this, ShopSearchActivity.class));
                break;
            case R.id.back:
                finish();
                break;
            case R.id.rl_zonghe:
                llNodata.setVisibility(View.GONE);
                recyclerview.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_jiage:
                if (!isMore) {
                    gvMore.setNumColumns(3);
                    gvMore.setAdapter(jiageAdapter);
                    llGv.setVisibility(View.VISIBLE);
                    isMore = true;
                } else {
                    llGv.setVisibility(View.GONE);
                    isMore = false;
                }
//                if(llGv.getVisibility()==View.VISIBLE){
//                    llGv.setVisibility(View.GONE);
//                }else
//                llGv.setVisibility(View.VISIBLE);
//                gvMore.setNumColumns(3);
//                gvMore.setAdapter(jiageAdapter);
                break;
            case R.id.rl_pinpai:
                if (!isMore) {
                    gvMore.setNumColumns(4);
                    gvMore.setAdapter(pinpaiAdapter);
                    llGv.setVisibility(View.VISIBLE);
                    isMore = true;
                } else {
                    llGv.setVisibility(View.GONE);
                    isMore = false;
                }
                break;
            case R.id.view_zhe:
                llGv.setVisibility(View.GONE);
                isMore=false;
                break;

        }
    }

    public List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (int i = 0; i < 2; i++) {
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
