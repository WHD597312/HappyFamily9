package com.xr.happyFamily.bao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.NestedScrollView;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.WaterFallAdapter;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.bao.view.MyHeadRefreshView;
import com.xr.happyFamily.bao.view.MyLoadMoreView;
import com.xr.happyFamily.bao.view.MyScrollview;
import com.xr.happyFamily.bean.PersonCard;
import com.xr.happyFamily.bean.ShopBean;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.login.login.LoginActivity;
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
    PullToRefreshLayout swipeContent;
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
    int page = 1, lastState = -1;


    boolean isLoading=false,isRefresh=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        setContentView(R.layout.activity_shop_search_result);
        ButterKnife.bind(this);
        goodsName = getIntent().getExtras().getString("goodsName");

        swipeContent.setHeaderView(new MyHeadRefreshView(this));
        swipeContent.setFooterView(new MyLoadMoreView(this));
        swipeContent.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                page=1;
                isRefresh=true;
                list_shop.clear();
                if (countTimer != null)
                    countTimer.cancel();
                countTimer = new CountTimer(5000, 1000);
                countTimer.start();
                getShopData(1, lastState);
            }

            @Override
            public void loadMore() {
                isLoading=true;
                page++;
                if (countTimer != null)
                    countTimer.cancel();
                countTimer = new CountTimer(5000, 1000);
                countTimer.start();
                getShopData(page, lastState);
            }
        });


        init();

    }

    private void init() {
        img = new ImageView[]{imgZonghe, imgJiage, imgPinpai};

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        //设置布局管理器为2列，纵向
        list_shop = new ArrayList<>();
        getShopData(1, lastState);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        shopAdapter = new WaterFallAdapter(this, list_shop);
        shopAdapter.setItemClickListener(new WaterFallAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ShopSearchResultActivity.this, ShopXQActivity3.class);
                intent.putExtra("goodsId", list_shop.get(position).getGoodsId() + "");
                startActivity(intent);
            }
        });
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(shopAdapter);

        jiageAdapter = new SimpleAdapter(this, getList(),
                R.layout.item_search_result, from, to);

        gvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastState = position;
                if (position == 0) {
                    getShopData(1, 1);
                } else {
                    getShopData(1, 2);
                }

                llGv.setVisibility(View.GONE);
            }
        });
    }


    private List<PersonCard> buildData(String name) {
        String[] names = {name + "1", name + "2", name + "3", name + "4"};
        int[] imgUrs = {R.mipmap.ic_air_error, R.mipmap.ic_air_error, R.mipmap.ic_air_error,R.mipmap.ic_air_error
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
                llGv.setVisibility(View.GONE);
                img[sign].setVisibility(View.INVISIBLE);
                sign = 0;
                img[sign].setVisibility(View.VISIBLE);
                getShopData(1, -1);
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


    private void getShopData(int page, int state) {
        list_shop.clear();

        Map<String, Object> params = new HashMap<>();
        params.put("goodsName", goodsName);
        params.put("pageNum", page + "");
        params.put("pageRow", "6");
        params.put("state",state);
        new ShopAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
    }

    Boolean isData = true;

    class ShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "/goods/getGoodsByGoodsName";
            String goodsName = params.get("goodsName").toString();
            String pageNum = params.get("pageNum").toString();
            String pageRow = params.get("pageRow").toString();
            int state=Integer.parseInt(params.get("state").toString());

            url=url+"?goodsName="+goodsName+"&pageNum="+pageNum+"&pageRow="+pageRow;
            if(state==1) {
                url=url+"&asc=1";
            }else if(state==2){
                url=url+"&desc=1";
            }
            String result = HttpUtils.requestGet(url);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
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
                if(isLoading)
                    swipeContent.finishLoadMore();
                if (isRefresh)
                    swipeContent.finishRefresh();

                if (!isData) {
                    if (page == 0) {
                        llNodata.setVisibility(View.VISIBLE);
                        recyclerview.setVisibility(View.GONE);
                    } else
                        ToastUtil.showShortToast("无更多商品");
                } else {
                    llNodata.setVisibility(View.GONE);
                    recyclerview.setVisibility(View.VISIBLE);
                }
                MyDialog.closeDialog(dialog);
            }else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(ShopSearchResultActivity.this.getApplicationContext(), LoginActivity.class));
            }
        }
    }

    private static CountTimer countTimer;
    class CountTimer extends CountDownTimer {
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * 倒计时过程中调用
         *
         * @param millisUntilFinished
         */
        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("Tag", "倒计时=" + (millisUntilFinished / 1000));
        }

        /**
         * 倒计时完成后调用
         */
        @Override
        public void onFinish() {
            if (isRefresh) {
                swipeContent.finishRefresh();
                Toast.makeText(ShopSearchResultActivity.this, "加载超时请重试", Toast.LENGTH_SHORT).show();
            }
            if (isLoading) {
                swipeContent.finishLoadMore();
                Toast.makeText(ShopSearchResultActivity.this, "加载超时请重试", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
