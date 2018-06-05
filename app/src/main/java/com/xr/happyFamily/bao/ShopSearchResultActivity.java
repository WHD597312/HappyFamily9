package com.xr.happyFamily.bao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
    private WaterFallAdapter shopAdapter;

    private String[] from = {"title"};
    private int[] to = {R.id.tv_search};
    String[] titles = new String[]{"从低到高", "从高到低"};
    String[] titles2 = new String[]{"松下", "小米", "海尔", "格力", "松下"};
    SimpleAdapter jiageAdapter;
    SimpleAdapter pinpaiAdapter;
    private boolean isMore = false;
    private int sign=0;
    private ImageView[] img;
    private List<PersonCard> list_shop;
    
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
        img= new ImageView[]{imgZonghe, imgJiage, imgPinpai};
        list_shop = new ArrayList<>();
        list_shop = buildData("电暖器");
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        shopAdapter = new WaterFallAdapter(this, list_shop);

        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(shopAdapter);

        jiageAdapter = new SimpleAdapter(this, getList(),
                R.layout.item_search_result, from, to);
        pinpaiAdapter = new SimpleAdapter(this, getList2(),
                R.layout.item_search_result, from, to);
        gvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(sign==1){
                    list_shop.clear();
                    list_shop.addAll(buildData(titles[position]));
                    shopAdapter.notifyDataSetChanged();

                }else {
                    list_shop.clear();
                    list_shop.addAll(buildData(titles2[position]));
                    shopAdapter.notifyDataSetChanged();
                }
                llGv.setVisibility(View.GONE);
            }
        });
    }


    private List<PersonCard> buildData(String name) {
        String[] names = {name + "1", name + "2", name + "3", name + "4"};
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
                img[sign].setVisibility(View.INVISIBLE);
                sign=0;
                img[sign].setVisibility(View.VISIBLE);
                break;
            case R.id.rl_jiage:
                if(sign==1){
                    if(llGv.getVisibility()==View.VISIBLE){
                        llGv.setVisibility(View.GONE);
                    }else
                        llGv.setVisibility(View.VISIBLE);
            }
               else  {
                    gvMore.setNumColumns(3);
                    gvMore.setAdapter(jiageAdapter);
                    llGv.setVisibility(View.VISIBLE);
                    img[sign].setVisibility(View.INVISIBLE);
                    sign=1;
                    img[sign].setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_pinpai:
                Log.e("qqqqqqqqqqq",sign+"???");
                if(sign==2){

                    if(llGv.getVisibility()==View.VISIBLE){
                        llGv.setVisibility(View.GONE);
                        Log.e("qqqqqqqqqqq","!!!!!");
                    }else {
                        llGv.setVisibility(View.VISIBLE);
                        Log.e("qqqqqqqqqqq", "?????");
                    }
                }else {
                    gvMore.setNumColumns(4);
                    gvMore.setAdapter(pinpaiAdapter);
                    llGv.setVisibility(View.VISIBLE);
                    img[sign].setVisibility(View.INVISIBLE);
                    sign=2;
                    img[sign].setVisibility(View.VISIBLE);
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
