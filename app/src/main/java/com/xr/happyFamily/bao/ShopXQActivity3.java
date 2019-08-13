package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.EvaluateAdapter;
import com.xr.happyFamily.bao.adapter.EvaluateXhAdapter;
import com.xr.happyFamily.bao.adapter.PinglunAdapter;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.bao.bean.GoodsPrice;
import com.xr.happyFamily.bao.bean.Receive;
import com.xr.happyFamily.bao.fragment.PingJiaFragment;
import com.xr.happyFamily.bao.fragment.ShopFragment;
import com.xr.happyFamily.bao.fragment.XiangQingFragment;
import com.xr.happyFamily.bao.view.FlowTagView;
import com.xr.happyFamily.bao.view.ShopBanner;
import com.xr.happyFamily.bao.view.TouchScrollView;
import com.xr.happyFamily.bean.PostFreeBean;
import com.xr.happyFamily.bean.ShopCartBean;
import com.xr.happyFamily.bean.ShopPinglunBean;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.GlideCircleTransform;
import com.xr.happyFamily.together.util.TimeUtils;
import com.xr.happyFamily.together.util.Utils;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by win7 on 2018/5/22.
 */

public class ShopXQActivity3 extends AppCompatActivity {


    Unbinder unbinder;
    List<String> circle = new ArrayList<>();
    List<BaseFragment> fragmentList = new ArrayList<>();
    @BindView(R.id.view_pop)
    View viewPop;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.rl_title_shop)
    RelativeLayout rlTitleShop;
    @BindView(R.id.rl_title_pingjia)
    RelativeLayout rlTitlePingjia;
    @BindView(R.id.rl_title_xiangqing)
    RelativeLayout rlTitleXiangqing;
    @BindView(R.id.img_fenxiang)
    ImageView imgFenxiang;
    @BindView(R.id.banner)
    ShopBanner banner;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_xinghao)
    TextView tvXinghao;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_haoping)
    TextView tvHaoping;
    @BindView(R.id.viewLine)
    View viewLine;
    @BindView(R.id.img_touxiang)
    ImageView imgTouxiang;
    @BindView(R.id.tv_pinglun_name)
    TextView tvPinglunName;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.img4)
    ImageView img4;
    @BindView(R.id.img5)
    ImageView img5;
    @BindView(R.id.tv_pinglun_time)
    TextView tvPinglunTime;
    @BindView(R.id.rl_dingdan)
    LinearLayout rlDingdan;
    @BindView(R.id.tv_pinglun)
    TextView tvPinglun;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.rl_more)
    RelativeLayout rlMore;
    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.scrollView)
    TouchScrollView scrollView;
    @BindView(R.id.tv_shopcart)
    TextView tvShopcart;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_title_shop)
    TextView tvTitleShop;
    @BindView(R.id.view_title_shop)
    View viewTitleShop;
    @BindView(R.id.tv_title_xq)
    TextView tvTitleXq;
    @BindView(R.id.view_title_xq)
    View viewTitleXq;
    @BindView(R.id.tv_title_pingjia)
    TextView tvTitlePingjia;
    @BindView(R.id.view_title_pingjia)
    View viewTitlePingjia;
    @BindView(R.id.bottom)
    LinearLayout bottom;
    @BindView(R.id.ll_pingjia_data)
    LinearLayout llPingjiaData;
    @BindView(R.id.tv_pinglun_nodata)
    TextView tvPinglunNodata;
    @BindView(R.id.cl_pinglun)
    ConstraintLayout clPinglun;
    @BindView(R.id.ll_shop)
    LinearLayout llShop;
    @BindView(R.id.pj_back)
    ImageView pjBack;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tv_pj_pinglun)
    TextView tvPjPinglun;
    @BindView(R.id.pj_img1)
    ImageView pjImg1;
    @BindView(R.id.pj_img2)
    ImageView pjImg2;
    @BindView(R.id.pj_img3)
    ImageView pjImg3;
    @BindView(R.id.pj_img4)
    ImageView pjImg4;
    @BindView(R.id.pj_img5)
    ImageView pjImg5;
    @BindView(R.id.tv_pj_haoping)
    TextView tvPjHaoping;
    @BindView(R.id.ft_pinglun)
    FlowTagView ftPinglun;
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.ll_pingjia)
    LinearLayout llPingjia;
    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;
    String goodsId;
    ShopFragment shopFragment;
    PingJiaFragment pingJiaFragment;
    XiangQingFragment xiangQingFragment;
    int priceId = -1;
    String receiveId = "0";
    int pos = 0;
    String userId;
    ArrayList<ShopCartBean> mGoPayList = new ArrayList<>();
    private MyDialog dialog;

    private boolean isMore = false;
    private EvaluateAdapter adapter_pinglun;
    private ArrayList<Map<String, Object>> datas;
    private PinglunAdapter pinglunAdapter;
    List<String> pjList = new ArrayList();
    String[] tag={"全部","美观","性价比高","包装好","做工精细","使用舒服"};

    boolean isTitleMove=false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        mContext = ShopXQActivity3.this;
        setContentView(R.layout.activity_shop_xq3);
        ButterKnife.bind(this);
        SharedPreferences preferences = this.getSharedPreferences("my", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        userId = preferences.getString("userId", "1000");
        editor.putString("jyShopPrice", "");
        editor.putString("jyShopPower", "");
        editor.commit();
        initView();
        setmTitle();
    }

    boolean running=false;
    @Override
    protected void onStart() {
        super.onStart();
        running=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        running=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        Map<String, Object> params2 = new HashMap<>();
        params2.put("userId", userId);
        new getAddressAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params2);
    }


    int scrollTitle=0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {

        Bundle extras = getIntent().getExtras();
        goodsId = extras.getString("goodsId");
        imgs = new ImageView[]{img1, img2, img3, img4, img5};
        imgs2 = new ImageView[]{pjImg1, pjImg2, pjImg3, pjImg4, pjImg5};
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        web.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        web.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        web.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片
        web.loadUrl("http://47.98.131.11:8084/admin/goods/detail/show?goodsId=" + goodsId);
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
//      获取数据，向适配器传数据，绑定适配器
        pinglunAdapter = new PinglunAdapter(mContext, shopPinglunBeanList);
        recyclerview.setAdapter(pinglunAdapter);
        //      调用按钮返回事件回调的方法
        adapter_pinglun = new EvaluateAdapter(mContext, R.layout.item_pingjia);
//        ftPinglun.setOne();
        ftPinglun.setAdapter(adapter_pinglun);
        ftPinglun.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                adapter_pinglun.setSelection(position);
                adapter_pinglun.notifyDataSetChanged();
                getPingLun(tag[position]);
            }
        });

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(!isTitleMove) {
                    if (scrollY > web.getTop()) {
                        if (scrollTitle != 2) {
                            Log.e("qqqqqqqqqqqqSS0000000", scrollX + "," + scrollY + "," + oldScrollX + "," + oldScrollY);
                            scrollTitle = 2;
                            initTitle();
                            tvTitleXq.setTextColor(Color.parseColor("#65CB90"));
                            viewTitleXq.setVisibility(View.VISIBLE);
                        }
                    } else if (scrollY > clPinglun.getTop()) {
                        if (scrollTitle != 1) {
                            Log.e("qqqqqqqqqqqqSS1111", scrollX + "," + scrollY + "," + oldScrollX + "," + oldScrollY);
                            initTitle();
                            scrollTitle = 1;
                            tvTitlePingjia.setTextColor(Color.parseColor("#65CB90"));
                            viewTitlePingjia.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (scrollTitle != 0) {
                            Log.e("qqqqqqqqqqqqSS22222", scrollX + "," + scrollY + "," + oldScrollX + "," + oldScrollY);
                            scrollTitle = 0;
                            initTitle();
                            tvTitleShop.setTextColor(Color.parseColor("#65CB90"));
                            viewTitleShop.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        ftPinglun.setOne();
        pjList.add("全部（0）");
        pjList.add("美观（0）");
        pjList.add("性价比高（0）");
        pjList.add("包装好（0）");
        pjList.add("做工精细（0）");
        pjList.add("使用舒服（0）");
        adapter_pinglun.setItems(pjList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.back, R.id.img_fenxiang, R.id.tv_shopcart, R.id.tv_buy, R.id.rl_title_pingjia, R.id.rl_title_shop, R.id.rl_title_xiangqing, R.id.tv_xinghao, R.id.tv_address,R.id.img_more,R.id.rl_more,R.id.pj_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_address:
                startActivityForResult(new Intent(mContext, ShopAddressActivity.class), 101);
                break;
            case R.id.tv_xinghao:
                showTypePopup();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.img_fenxiang:
//                showPopup();
                Intent fenxiang = new Intent(Intent.ACTION_SEND);
                fenxiang.setType("text/plain");
                fenxiang.putExtra(Intent.EXTRA_TEXT, "http://47.98.131.11:8084/admin/goods/detail/show?goodsId=" + goodsId);
                startActivity(Intent.createChooser(fenxiang, "P99"));
                break;
            case R.id.tv_shopcart:
                if (priceId == -1) {
                    showTypePopup();
                } else {
                    Map<String, Object> params = new HashMap<>();
                    params.put("priceId", priceId);
                    params.put("quantity", num);
                    new addShopAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
                }

                break;
            case R.id.tv_buy:
                Log.e("qqqqqNNNNN22", priceId + "!!!!!!");
                if (priceId == -1) {
                    showTypePopup();
                } else {
                    Intent intent = new Intent(this, ShopConfActivity.class);
                    intent.putExtra("type", (Serializable) "XQ");
                    intent.putExtra("goodsId", goodsId);
                    intent.putExtra("num", num + "");
                    intent.putExtra("img", img);
                    intent.putExtra("priceId", priceId + "");

                    Log.e("qqqqqqqNNN", num + "," + priceId);
                    intent.putExtra("money", Integer.parseInt(price) * num);
                    intent.putExtra("weight", weight + "");
                    intent.putExtra("price", price);
                    intent.putExtra("context", type);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }

                break;

            case R.id.rl_title_shop:
//                isTitleMove=true;
//                scrollTitle=0;
//                initTitle();
//                tvTitleShop.setTextColor(Color.parseColor("#65CB90"));
//                viewTitleShop.setVisibility(View.VISIBLE);
                scrollView.smoothScrollTo(0, banner.getTop()+1);

                break;
            case R.id.rl_title_pingjia:
//                isTitleMove=true;
//                scrollTitle=1;
//                initTitle();
//                tvTitlePingjia.setTextColor(Color.parseColor("#65CB90"));
//                viewTitlePingjia.setVisibility(View.VISIBLE);
                scrollView.smoothScrollTo(0, clPinglun.getTop()+1);

                break;
            case R.id.rl_title_xiangqing:
//                isTitleMove=true;
//                scrollTitle=2;
//                initTitle();
//                tvTitleXq.setTextColor(Color.parseColor("#65CB90"));
//                viewTitleXq.setVisibility(View.VISIBLE);
                scrollView.smoothScrollTo(0, web.getTop()+1);
                break;

            case R.id.img_more:
                if (!isMore) {
                    ftPinglun.setMore();
                    adapter_pinglun.notifyDataSetChanged();
                    isMore = true;
                    imgMore.setImageResource(R.mipmap.ic_pingjia_more);
                } else {
                    ftPinglun.setOne();
                    adapter_pinglun.notifyDataSetChanged();
                    isMore = false;

                    imgMore.setImageResource(R.mipmap.ic_pingjia_more_xia);
                }
                break;
            case R.id.rl_more:
                scrollY=scrollView.getScrollY();
                llShop.setVisibility(View.GONE);
                llPingjia.setVisibility(View.VISIBLE);
                isPingJia=true;
                break;
            case R.id.pj_back:
                llPingjia.setVisibility(View.GONE);
                llShop.setVisibility(View.VISIBLE);
                scrollView.scrollTo(0, scrollY);
                isPingJia=false;
                break;
        }
    }

    //重置标题
    public void initTitle(){
        tvTitlePingjia.setTextColor(Color.BLACK);
        tvTitleXq.setTextColor(Color.BLACK);
        tvTitleShop.setTextColor(Color.BLACK);
        viewTitlePingjia.setVisibility(View.GONE);
        viewTitleXq.setVisibility(View.GONE);
        viewTitleShop.setVisibility(View.GONE);
    }


    private ImageView img_close;


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }

    String addressReturnData = "";
    Receive receive;

    class getAddressAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "receive/getDefaultReceive";
            String result = HttpUtils.requestPost(url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    addressReturnData = jsonObject.getString("returnData");
                    if (!Utils.isEmpty(addressReturnData)) {

                        Gson gson = new Gson();
                        receive = gson.fromJson(jsonObject.getString("returnData"), Receive.class);
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
            Log.e("qqqqqLLL",addressReturnData);
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                if (!"null".equals(addressReturnData)) {
                    Log.e("qqqqqLLL","11111");
                    if (tvAddress != null)
                        tvAddress.setText(receive.getReceiveProvince() + " " + receive.getReceiveCity() + " " + receive.getReceiveCounty() + " " + receive.getReceiveAddress());
                    isAddress = true;
                    receiveId = receive.getReceiveId() + "";
                    if (isWeight)
                        getTime();
                } else {
                    Log.e("qqqqqLLL","22222");

                    if (tvAddress != null)
                        tvAddress.setText("没有地址信息，请点击后添加地址");
                }
            }
        }
    }


    class addShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "shoppingcart/addOneToShoppingCart";
            String result = HttpUtils.requestPost(url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
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
                ToastUtil.showShortToast("已加入购物车");
                if (mPopWindow != null) {
                    mPopWindow.dismiss();
                }
            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
            }
        }
    }


    private View view_dis;
    private EvaluateAdapter adapter_xinghao;
    private FlowTagView labelsView;
    private TextView tv_price, tv_name, tv_type_cart, tv_type_bug, tv_jia, tv_jian, tv_num, tv_cart, tv_buy;
    private ImageView img_shop_pic;
    int num = 1;
    private EvaluateXhAdapter adapter_xh;
    int sign = -1;

    private void showTypePopup() {
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shop_xinghao, null);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        view_dis = contentViewSign.findViewById(R.id.view_dis);
        tv_price = (TextView) contentViewSign.findViewById(R.id.tv_price);
        labelsView = (FlowTagView) contentViewSign.findViewById(R.id.labels);
        tv_name = (TextView) contentViewSign.findViewById(R.id.tv_name);
        tv_name.setText(goodsName);
        img_shop_pic = (ImageView) contentViewSign.findViewById(R.id.img_shop_pic);
        if (!Utils.isEmpty(img))
            Picasso.with(this).load(img).into(img_shop_pic);
        adapter_xh = new EvaluateXhAdapter(this, R.layout.item_xinghao);
        list = new ArrayList<>();
        for (int i = 0; i < list_price.size(); i++) {
            list.add(list_price.get(i).getPower() + "");
        }
        adapter_xh.setItems(list);
        labelsView.setAdapter(adapter_xh);
        labelsView.setOne();
        labelsView.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                sign = position;
                adapter_xh.setSelection(position);
                adapter_xh.notifyDataSetChanged();
                price = list_price.get(position).getPrice() + "";
                tv_price.setText("¥" + price);
                tvXinghao.setText(list_price.get(position).getPower());
                priceId = list_price.get(position).getPriceId();
                goodsId = list_price.get(position).getGoodsId() + "";
                returnPrice = list_price.get(position).getPower();
            }
        });


        if (sign != -1) {
            adapter_xh.setSelection(sign);
            tv_price.setText("¥" + getList().get(sign).getPrice());
        } else {
            tv_price.setText("");
        }
        tv_name = (TextView) contentViewSign.findViewById(R.id.tv_name);
        tv_type_cart = (TextView) contentViewSign.findViewById(R.id.tv_type_cart);
        tv_type_bug = (TextView) contentViewSign.findViewById(R.id.tv_type_bug);

        tv_jian = (TextView) contentViewSign.findViewById(R.id.tv_shop_reduce);
        tv_num = (TextView) contentViewSign.findViewById(R.id.tv_shop_num);
        tv_jia = (TextView) contentViewSign.findViewById(R.id.tv_shop_add);
        tv_cart = (TextView) contentViewSign.findViewById(R.id.tv_type_cart);
        tv_buy = (TextView) contentViewSign.findViewById(R.id.tv_type_bug);
        tv_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > 1) {
                    num--;
                    tv_num.setText(num + "");
                }
            }
        });
        tv_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                tv_num.setText(num + "");
            }
        });
        tv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priceId != -1) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("priceId", priceId);
                    params.put("quantity", num);
                    new addShopAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
                } else {
                    Toast.makeText(mContext, "请选择商品规格", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sign != -1) {
                    Intent intent = new Intent(mContext, ShopConfActivity.class);
                    intent.putExtra("type", "XQ");
                    intent.putExtra("goodsId", goodsId);
                    intent.putExtra("num", num + "");
                    intent.putExtra("priceId", priceId + "");
                    intent.putExtra("money", Integer.parseInt(price) * num);
                    intent.putExtra("weight", weight * num + "");
                    intent.putExtra("price", price);
                    intent.putExtra("context", type);
                    intent.putExtra("img", img);
                    intent.putExtra("name", name);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "请选择商品规格", Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
            if (jsonObject != null) {
                if (!Utils.isEmpty(jsonObject.getString("goodsName")))
                    tv_name.setText(jsonObject.getString("goodsName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setBiaoqian();
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        view_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        mPopWindow = new PopupWindow(contentViewSign);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        mPopWindow.setFocusable(true);
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//        initView();
//        initData();

    }

    List<String> list;
    String choosePrice;

    private void setBiaoqian() {

        list = new ArrayList<>();
        for (int i = 0; i < list_price.size(); i++) {
            list.add(list_price.get(i).getPower() + "");
        }

    }

    private List<GoodsPrice> list_price = new ArrayList<>();


    public List<GoodsPrice> getList() {
        return list_price;
    }
    Map<String, Object> params = new ConcurrentHashMap<>();
    Map<String, Object> params2 = new ConcurrentHashMap<>();
    public void setmTitle() {
        params.clear();
        params.put("goodsId", goodsId);
        dialog = MyDialog.showDialog(mContext);
        dialog.show();
        new getShopAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
        params2.clear();
        params2.put("userId", userId);
        new getAddressAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params2);

        new getRateAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
        new getCountAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);

    }

    JSONObject jsonObject;
    String name, type, img, price = "0", detailDescribe, goodsName;
    String returnPrice = "0";
    double weight;
    String headerImg;

    class getShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "goods/getGoodsById";
            String result = HttpUtils.requestPost(url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("goodsPrice");
                    headerImg = returnData.getString("headerImg");
                    goodsName = returnData.getString("goodsName");
                    if ("100".equals(code)) {
                        Gson gson = new Gson();
                        for (JsonElement user : list) {
                            //通过反射 得到UserBean.class
                            GoodsPrice userList = gson.fromJson(user, GoodsPrice.class);
                            list_price.add(userList);
                            img = returnData.getString("image");
                            name = returnData.getString("goodsName");
                            type = returnData.getString("simpleDescribe");
                            weight = returnData.getDouble("weight");
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
                MyDialog.closeDialog(dialog);
                int maxPrice = 0;
                int minPrice = list_price.get(0).getPrice();
                for (int i = 0; i < list_price.size(); i++) {
                    if (list_price.get(i).getPrice() < minPrice)
                        minPrice = list_price.get(i).getPrice();
                    if (list_price.get(i).getPrice() > maxPrice)
                        maxPrice = list_price.get(i).getPrice();
                }
                if (minPrice == maxPrice)
                    tvPrice.setText("¥" + minPrice);
                else
                    tvPrice.setText("¥" + minPrice + "-" + maxPrice);
                if (tvName != null)
                    tvName.setText(name);
                if (tvWeight != null)
                    tvWeight.setText(weight + "kg");
                if (tvType != null)
                    tvType.setText(type);

                Log.e("qqqqqqqPPPP", headerImg + "?");
                String imgStr[] = headerImg.split(",");
                List<String> stringB = Arrays.asList(imgStr);
                if (banner != null) {
                    //设置图片加载器，图片加载器在下方
                    banner.setImageLoader(new MyLoader());
                    //设置图片网址或地址的集合
                    banner.setImages(stringB);
                    banner.isAutoPlay(false);
                    //设置指示器的位置，小点点，左中右。
                    banner.setIndicatorGravity(BannerConfig.CENTER)
                            //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
//                            .setOnBannerListener(this)
                            //必须最后调用的方法，启动轮播图。
                            .start();
                }
            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(mContext, "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = mContext.getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
            }

            isWeight = true;
            if (isAddress)
                getTime();
        }
        //轮播图的监听方法
//        @Override
//        public void OnBannerClick(int position) {
//            Log.i("tag", "你点了第"+position+"张轮播图");
//        }
    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            if (running)
            Glide.with(context).load((String) path).into(imageView);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    static String sign_1, sing_2, sing_3;





    boolean isWeight = false, isAddress = false;

    public void getTime() {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsName", "hello");
        map.put("receiveId", receive.getReceiveId());
        map.put("weight", weight);
        new getPostFeeAsync().execute(map);
    }

    long time, recLen;
    List<PostFreeBean> postFreeBeans = new ArrayList<>();

    class getPostFeeAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "logistics/getPostFee";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    String returnData = jsonObject.getString("returnData");
                    if (!Utils.isEmpty(returnData)) {
                        JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                        JsonArray list = content.getAsJsonArray("RecommendDetail");
                        Gson gson = new Gson();

                        for (int i = 0; i < list.size(); i++) {
                            JsonElement use = list.get(i);
                            PostFreeBean userList = gson.fromJson(use, PostFreeBean.class);
                            postFreeBeans.add(userList);
                        }
                        time = (long) (((int) postFreeBeans.get(0).getExpressList().get(0).getEstimatedDeliveryTime()) + 1) * 60 * 60 * 1000;
                        long getNowTimeLong = System.currentTimeMillis();

                        recLen = time + getNowTimeLong;//这样得到的差值是级别

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
                Date date = new Date(recLen);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
                String time = format.format(date);
                if (tvTime != null)
                    tvTime.setText("23.00前下单，预计（" + time.substring(5, time.length()) + "）送达");
            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(mContext, "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
            }
        }
    }



    //评价相关
    boolean isPingJia=false;
    int scrollY;

    private void getPingLun(String tag) {
        shopPinglunBeanList.clear();
        Map<String, Object> params = new HashMap<>();
        params.put("goodsId", goodsId);
        if (!"全部".equals(tag))
            params.put("tag", tag);
        new getRateAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
    }


    ImageView[] imgs,imgs2;
    String countReturnData = "";

    String  average,beautiful="0", total="0", cost="0", fine="0", comfortable="0", satisfaction="0", packing="0";

    class getCountAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "order/getCountRate";
            String result = HttpUtils.requestPost(url, params);
            Log.i("result","-->"+result);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code=result;
                    }else {
                        code = "100";
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject returnData = jsonObject.getJSONObject("returnData");
                        average = returnData.getString("average");
                        Log.i("average2", "-->" + average);
                        total = returnData.getString("total");
                        beautiful = returnData.getString("beautiful");
                        cost = returnData.getString("cost");
                        fine = returnData.getString("fine");
                        comfortable = returnData.getString("comfortable");
                        //满意度
                        satisfaction = returnData.getString("satisfaction");
                        packing = returnData.getString("packing");

//        list.add("质量好（100）");
                        pjList.clear();
                        pjList.add("全部（" + total + "）");
                        pjList.add("美观（" + beautiful + "）");
                        pjList.add("性价比高（" + cost + "）");
                        pjList.add("包装好（" + packing + "）");
                        pjList.add("做工精细（" + fine + "）");
                        pjList.add("使用舒服（" + comfortable + "）");
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
                Log.i("average","-->"+average);

                    if(!Utils.isEmpty(average+"")) {
                        int ave = (int)(Float.parseFloat(average)*10 + 5)/10;
                        for (int i = 0; i < ave; i++) {
                            imgs2[i].setImageResource(R.mipmap.ic_pl_xx_true);
                        }
                    }
                    adapter_pinglun.notifyDataSetChanged();
                    if (tvHaoping!=null) {
                        if (total.equals("0"))

                            tvHaoping.setText("满意度:100%");

                        else
                            tvHaoping.setText("满意度:" + Integer.parseInt(satisfaction) * 100 / Integer.parseInt(total) + "%");
                    }
//                    tvAddress.setText(receive.getReceiveProvince() + " " + receive.getReceiveCity() + " " + receive.getReceiveCounty() + " " + receive.getReceiveAddress());
                }else if (!Utils.isEmpty(s) && "401".equals(s)) {
                    Toast.makeText(mContext, "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences;
                    preferences = mContext.getSharedPreferences("my", MODE_PRIVATE);
                    MyDialog.setStart(false);
                    if (preferences.contains("password")) {
                        preferences.edit().remove("password").commit();
                    }
                    startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
                }

        }
    }

    List<ShopPinglunBean> shopPinglunBeanList = new ArrayList<>();

    class getRateAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "order/getRateByGoodsIdAndTag";
            String result = HttpUtils.requestPost(url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    String returnData = jsonObject.getString("returnData");
                    if (!Utils.isEmpty(returnData)) {
                        JsonObject content = new JsonParser().parse(jsonObject.toString()).getAsJsonObject();
                        JsonArray list = content.getAsJsonArray("returnData");
                        Gson gson = new Gson();
                        if (list.size() > 0) {
                            for (JsonElement user : list) {
                                //通过反射 得到UserBean.class
                                ShopPinglunBean shopPinglunBean = gson.fromJson(user, ShopPinglunBean.class);
                                shopPinglunBeanList.add(shopPinglunBean);
                            }
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
            try {
                if (!Utils.isEmpty(s) && "100".equals(s)) {
    //                Log.e("qqqqqqqqqqqRece",receive.getContact()+"!");
    //                pinglunAdapter.notifyDataSetChanged();
                    pinglunAdapter.notifyDataSetChanged();
                    if (shopPinglunBeanList.size() == 0) {
                        tvPinglunNodata.setVisibility(View.VISIBLE);
                        llPingjiaData.setVisibility(View.GONE);
                    } else {
                        int last = shopPinglunBeanList.size() - 1;
                        tvNumber.setText("评论（" + shopPinglunBeanList.size() + "）");
                        llPingjiaData.setVisibility(View.VISIBLE);
                        tvPinglunNodata.setVisibility(View.GONE);
                        if (shopPinglunBeanList.get(last).getHeadImgUrl() != null)
                            Glide.with(mContext).load(shopPinglunBeanList.get(last).getHeadImgUrl().toString()).transform(new GlideCircleTransform(getApplicationContext())).error(R.mipmap.ic_touxiang_moren).into(imgTouxiang);
                        for (int i = 0; i < shopPinglunBeanList.get(last).getBuyerRate(); i++) {
                            imgs[i].setImageResource(R.mipmap.ic_pl_xx_true);
                        }
                        if(shopPinglunBeanList.get(last).getUsername()!=null) {
                            if (shopPinglunBeanList.get(last).getAnonymous())
                                tvPinglunName.setText("匿名");
                            else
                                tvPinglunName.setText(shopPinglunBeanList.get(last).getUsername());
                        }
                        tvPinglun.setText(shopPinglunBeanList.get(last).getComment());
                        tvPinglunTime.setText(TimeUtils.getTime(shopPinglunBeanList.get(last).getCreateTime() + ""));
                    }
    //                    tvAddress.setText(receive.getReceiveProvince() + " " + receive.getReceiveCity() + " " + receive.getReceiveCounty() + " " + receive.getReceiveAddress());
                } else if (!Utils.isEmpty(s) && "401".equals(s)) {
                    Toast.makeText(mContext, "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences;
                    preferences = mContext.getSharedPreferences("my", MODE_PRIVATE);
                    MyDialog.setStart(false);
                    if (preferences.contains("password")) {
                        preferences.edit().remove("password").commit();
                    }
                    startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(isPingJia){
                isPingJia=false;
                llPingjia.setVisibility(View.GONE);
                llShop.setVisibility(View.VISIBLE);
                scrollView.scrollTo(0, scrollY);

            }else
                finish();
        }
        return true;

    }
}
