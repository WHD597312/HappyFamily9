package com.xr.happyFamily.bao;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.WaterFallAdapter;
import com.xr.happyFamily.bao.view.MyScrollview;
import com.xr.happyFamily.bean.PersonCard;
import com.xr.happyFamily.bean.ShopBean;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

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
    @BindView(R.id.myScroll)
    MyScrollview myScroll;
    @BindView(R.id.swipe_content)
    SwipeRefreshLayout swipeContent;
    private RecyclerView.LayoutManager mLayoutManager;
    private WaterFallAdapter shopAdapter;

    private String[] from = {"title"};
    private int[] to = {R.id.tv_search};
    String[] titles = new String[]{"从低到高", "从高到低"};
    SimpleAdapter jiageAdapter;
    private boolean isMore = false;
    private int sign = 0;
    private ImageView[] img;
    private List<ShopBean.ReturnData.MyList> list_shop;

    String goodsName;
    private MyDialog dialog;
    int page=1;
    boolean isXiala=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_search_result);
        ButterKnife.bind(this);
        goodsName = getIntent().getExtras().getString("goodsName");

        swipeContent.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeContent.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        swipeContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list_shop.clear();
                isXiala=true;
                getShopData( 1);

            }
        });

        myScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int height = v.getHeight();
                int scrollViewMeasuredHeight = myScroll.getChildAt(0).getMeasuredHeight();
                if (scrollY == 0) {
                }
                if ((scrollY + height) == scrollViewMeasuredHeight) {
                    page++;
                    getShopData( page);
                }
            }
        });

        init();

    }

    private void init() {
        img = new ImageView[]{imgZonghe, imgJiage, imgPinpai};

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        //设置布局管理器为2列，纵向
        list_shop = new ArrayList<>();
        getShopData( 1);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        shopAdapter = new WaterFallAdapter(this, list_shop);

        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(shopAdapter);

        jiageAdapter = new SimpleAdapter(this, getList(),
                R.layout.item_search_result, from, to);

//        gvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(sign==1){
//                    list_shop.clear();
//                    list_shop.addAll(buildData(titles[position]));
//                    shopAdapter.notifyDataSetChanged();
//
//                }else {
//                    list_shop.clear();
//                    list_shop.addAll(buildData(titles2[position]));
//                    shopAdapter.notifyDataSetChanged();
//                }
//                llGv.setVisibility(View.GONE);
//            }
//        });
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

    @OnClick({R.id.tv_search, R.id.back, R.id.rl_zonghe, R.id.rl_jiage, R.id.rl_pinpai, R.id.view_zhe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                startActivity(new Intent(this, ShopSearchActivity.class));
                break;
            case R.id.back:
                finish();
                break;
            case R.id.rl_zonghe:

                img[sign].setVisibility(View.INVISIBLE);
                sign = 0;
                img[sign].setVisibility(View.VISIBLE);
                getShopData(1);
                break;
            case R.id.rl_jiage:
                if (sign == 1) {
                    if (llGv.getVisibility() == View.VISIBLE) {
                        llGv.setVisibility(View.GONE);
                    } else
                        llGv.setVisibility(View.VISIBLE);
                } else {
                    gvMore.setNumColumns(3);
                    gvMore.setAdapter(jiageAdapter);
                    llGv.setVisibility(View.VISIBLE);
                    img[sign].setVisibility(View.INVISIBLE);
                    sign = 1;
                    img[sign].setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_pinpai:
                if (sign == 2) {

                    if (llGv.getVisibility() == View.VISIBLE) {
                        llGv.setVisibility(View.GONE);

                    } else {
                        llGv.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.view_zhe:
                llGv.setVisibility(View.GONE);
                isMore = false;
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




    private void getShopData( int page) {

        if(!isXiala){
        dialog = MyDialog.showDialog(ShopSearchResultActivity.this);
        dialog.show();}
        Map<String, Object> params = new HashMap<>();
        params.put("goodsName", goodsName);
        params.put("pageNum", page + "");
        params.put("pageRow", "6");
        new ShopAsync().execute(params);
    }

    Boolean isData = true;

    class ShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "goods/getGoodsByGoodsName";
            String result = HttpUtils.myPostOkHpptRequest(ShopSearchResultActivity.this, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("list");
                    if ("100".equals(code)) {
                        Gson gson = new Gson();
                        if (list.size() > 0) {
                            isData = true;
                            for (JsonElement user : list) {
                                //通过反射 得到UserBean.class
                                ShopBean.ReturnData.MyList userList = gson.fromJson(user, ShopBean.ReturnData.MyList.class);
                                list_shop.add(userList);
                            }
                        } else {
                            page--;
                            isData = false;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                shopAdapter.notifyDataSetChanged();
                swipeContent.setRefreshing(false);
                if (!isData) {
                    if (page==0){
                        llNodata.setVisibility(View.VISIBLE);
                        recyclerview.setVisibility(View.GONE);
                    }else
                    Toast.makeText(ShopSearchResultActivity.this, "无更多数据", Toast.LENGTH_SHORT).show();
                }else {
                    llNodata.setVisibility(View.GONE);
                    recyclerview.setVisibility(View.VISIBLE);
                }
                MyDialog.closeDialog(dialog);
            }
        }
    }
}
