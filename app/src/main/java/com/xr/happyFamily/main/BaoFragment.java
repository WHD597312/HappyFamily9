package com.xr.happyFamily.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.squareup.picasso.Picasso;
import com.xr.database.dao.daoimpl.ShopBannerDaoImpl;
import com.xr.database.dao.daoimpl.ShopListDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopCartActivity;
import com.xr.happyFamily.bao.ShopDingdanActivity;
import com.xr.happyFamily.bao.ShopSearchActivity;
import com.xr.happyFamily.bao.ShopShangchengActivity;
import com.xr.happyFamily.bao.ShopXQActivity3;
import com.xr.happyFamily.bao.adapter.MainTitleAdapter;
import com.xr.happyFamily.bao.adapter.ViewPagerAdapter;
import com.xr.happyFamily.bao.adapter.WaterFallAdapter;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.bao.pojo.ShopBanner;
import com.xr.happyFamily.bao.pojo.ShopList;
import com.xr.happyFamily.bao.view.LinearGradientView;
import com.xr.happyFamily.bao.view.MyHeadRefreshView;
import com.xr.happyFamily.bao.view.MyLoadMoreView;
import com.xr.happyFamily.bao.view.MyScrollview;
import com.xr.happyFamily.bean.ShopBannerBean;
import com.xr.happyFamily.bean.ShopBean;
import com.xr.happyFamily.bean.ShopPageBean;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.http.NetWorkUtil;
import com.xr.happyFamily.together.util.Utils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BaoFragment extends Fragment implements View.OnClickListener {
    View view;
    Unbinder unbinder;
    public static final int MAIN_CODE = 1000;
    @BindView(R.id.image_shopcart)
    ImageView imageShopcart;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.image_search)
    ImageView imageSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.image_more)
    ImageView imageMore;
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
    @BindView(R.id.img6)
    ImageView img6;
    @BindView(R.id.img7)
    ImageView img7;
    @BindView(R.id.img8)
    ImageView img8;
    @BindView(R.id.img9)
    ImageView img9;
    @BindView(R.id.img10)
    ImageView img10;
    @BindView(R.id.lineLayout_dot)
    LinearLayout lineLayoutDot;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.ll_tuijian)
    LinearLayout llTuijian;
    @BindView(R.id.rv_shop)
    RecyclerView rvShop;
    @BindView(R.id.ll_data)
    LinearLayout llData;
    @BindView(R.id.myScroll)
    MyScrollview myScroll;
    @BindView(R.id.swipe_content)
    PullToRefreshLayout swipeContent;
    @BindView(R.id.rv_title)
    RecyclerView rvTitle;
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.ll_yinying)
    LinearGradientView llYinying;
    @BindView(R.id.img_shang)
    ImageView imgShang;
    @BindView(R.id.gv_more)
    GridView gvMore;
    @BindView(R.id.view_zhe)
    View viewZhe;
    @BindView(R.id.ll_nodata)
    LinearLayout llNodata;

    private RecyclerView.LayoutManager shopLayoutManager;
    private LinearLayoutManager titleLayoutManager;
    private WaterFallAdapter shopAdapter;
    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;
    private TextView tv_shopcart, tv_dingdan, tv_shangcheng;

    private List<ImageView> mImageList = new ArrayList<>();//轮播的图片集合
    private boolean isStop;//线程是否停止
    private static int PAGER_TIOME = 2500;//间隔时间
    // 在values文件假下创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
//    private int[] imgae_ids = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4};
    //轮播点
    private int[] imgae_dots = new int[]{R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5, R.id.img6, R.id.img7, R.id.img8, R.id.img9, R.id.img10};
    private String[] from = {"title"};
    private int[] to = {R.id.tv_search};
    String[] titles;
    private MainTitleAdapter mainTitleAdapter;
    private List<String> list_title = new ArrayList<>();
    int lastVisibleItem = 0, page = 1;
    List<ShopBean.ReturnData.MyList> list_shop = new ArrayList<>();
    ;
    boolean isData = true;
    //    private MyDialog dialog;
    private static CountTimer countTimer;
    private boolean isFirst = true;
    SimpleAdapter moreAdapter;
    List<Map<String, Object>> list_more = new ArrayList<>();

    int lastBanner = 0;

    boolean isLoading = false, isRefresh = false;

    //判断三个后台数据是否获取完成
    boolean isBanner = false, isShopData = false, isPage = false;
    ShopListDaoImpl shopListDao;
    ShopBannerDaoImpl shopBannerDao;

    getHomePageAsync getHomePageAsync;
    getAdByPageAsync getAdByPageAsync;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_shop_mypage, container, false);
        unbinder = ButterKnife.bind(this, view);
        running = true;
        mContext = getContext();
        isStop = false;
        isShopData = false;

        shopListDao = new ShopListDaoImpl(getActivity());
        shopBannerBeans = new ArrayList<>();
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        init();
        getDataBase();
        homePage = new ArrayList<>();

        if (shopBannerDao.findAll().size() == 0) {
            getAdByPageAsync = new getAdByPageAsync();
            getAdByPageAsync.execute();
        }
        if (shopListDao.findAll().size() == 0) {
            getHomePageAsync = new getHomePageAsync();
            getHomePageAsync.execute();
        }
        return view;
    }

    boolean running = false;

    @Override
    public void onStart() {

        super.onStart();
        running = true;
        banner.startAutoPlay();

    }

    @Override
    public void onPause() {
        super.onPause();
        running = false;
        banner.stopAutoPlay();
        if (isRefresh)
            swipeContent.finishRefresh();
        if (isLoading)
            swipeContent.finishLoadMore();
        if (getAdByPageAsync != null && !getAdByPageAsync.isCancelled() && getAdByPageAsync.getStatus() == AsyncTask.Status.RUNNING) {
            getAdByPageAsync.cancel(true);
            getAdByPageAsync = null;
        }
        if (getHomePageAsync != null && !getHomePageAsync.isCancelled() && getHomePageAsync.getStatus() == AsyncTask.Status.RUNNING) {
            getHomePageAsync.cancel(true);
            getHomePageAsync = null;
        }
        if (shopAsync != null && shopAsync.getStatus() == AsyncTask.Status.RUNNING) {
            shopAsync.cancel(true);
            shopAsync = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    private void getDataBase() {
        Log.e("qqqqqqLLLL",lastVisibleItem+"??");
        shopListDao = new ShopListDaoImpl(getActivity());
        List<ShopList> shopLists = shopListDao.findAll();
        data = new String[shopLists.size()];
        list_title.clear();
        list_more.clear();
        newShopList.clear();
        if (shopLists.size() > 0) {
            for (int i = 0; i < shopLists.size(); i++) {
                Map map = new HashMap<String, Object>();
                map.put("title", shopLists.get(i).getTypeName());
                list_more.add(map);
                list_title.add(shopLists.get(i).getTypeName());
                newShop = new ShopList();
                newShop.setId(shopLists.get(i).getId());
                newShop.setTypeName(shopLists.get(i).getTypeName());
                newShop.setCategoryId(shopLists.get(i).getCategoryId());
                newShopList.add(newShop);


                if (!Utils.isEmpty(shopLists.get(i).getData())) {
                    data[i] = shopLists.get(i).getData();
                    Log.e("qqqqqList2222", data[i]);
                }
            }
            mainTitleAdapter.notifyDataSetChanged();
            mainTitleAdapter.setPosition(lastVisibleItem);
            moreAdapter.notifyDataSetChanged();
            upData(lastVisibleItem);
        }

        shopBannerDao = new ShopBannerDaoImpl(getActivity());
        List<ShopBanner> shopBannerList = shopBannerDao.findAll();
        if (shopBannerList.size() > 0) {

            List<String> imageList = new ArrayList<>();

            try {
                Log.e("qqqqqqBBBB111111", shopBannerList.get(0).getData());
                JSONObject jsonObject = new JSONObject(shopBannerList.get(0).getData());
                JSONObject returnData = jsonObject.getJSONObject("returnData");
                JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                if (returnData.get("list").toString() != "null") {
                    JsonArray list = content.getAsJsonArray("list");
                    Gson gson = new Gson();
                    shopBannerBeans.clear();
                    imageList.clear();
                    for (int i = 0; i < list.size(); i++) {
//                        //通过反射 得到UserBean.class
                        JsonElement user = list.get(i);
                        ShopBannerBean userList = gson.fromJson(user, ShopBannerBean.class);
                        shopBannerBeans.add(userList);
                        imageList.add(userList.getImage());
                    }
                }
                Log.e("qqqqqqBBBB", shopBannerList.size() + "," + shopBannerBeans.get(0).getImage());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (running) {
                Log.e("qqqqqqIIII", imageList.size() + "??");
                setBanner(imageList);

            }



        }

    }

    private void setBanner(final List<String> imageList) {
        if (banner != null) {
            for (int i = 0; i < imageList.size(); i++) {
                view.findViewById(imgae_dots[i]).setVisibility(View.VISIBLE);
            }
            //设置图片加载器，图片加载器在下方
            banner.setImageLoader(new MyLoader());
            banner.setBannerAnimation(Transformer.Default);
            banner.setDelayTime(2500);
            //设置图片网址或地址的集合
            banner.setImages(imageList);
            banner.isAutoPlay(true);
            //设置指示器的位置，小点点，左中右。
            banner.setIndicatorGravity(BannerConfig.CENTER);
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    for (int i = 0; i < shopBannerBeans.size(); i++) {
                        if (imageList.get(position).equals(shopBannerBeans.get(i).getImage())) {
                            Intent intent = new Intent(mContext, ShopXQActivity3.class);
                            intent.putExtra("goodsId", shopBannerBeans.get(i).getGoodsId() + "");
                            Log.e("qqqqqqqqqqIIII", shopBannerBeans.get(i).getGoodsId() + "????");
                            mContext.startActivity(intent);
                        }
                    }
                }
            });
            banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    Log.e("qqqqqqPPP", position + "??");
                    for (int i = 0; i < imageList.size(); i++) {
                        if (position == i + 1) {
                            ((ImageView) view.findViewById(imgae_dots[i])).setImageResource(R.mipmap.ic_shop_banner);
                        } else {
                            ((ImageView) view.findViewById(imgae_dots[i])).setImageResource(R.mipmap.ic_shop_banner_wei);
                        }
                        if (position > imageList.size()) {
                            ((ImageView) view.findViewById(imgae_dots[0])).setImageResource(R.mipmap.ic_shop_banner);
                        }
                        if (position == 0) {
                            ((ImageView) view.findViewById(imgae_dots[imageList.size() - 1])).setImageResource(R.mipmap.ic_shop_banner);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            banner.start();
        }
    }


    private void init() {
        page = 1;

        mainTitleAdapter = new MainTitleAdapter(mContext, list_title);
        titleLayoutManager = new LinearLayoutManager(mContext);
        titleLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTitle.setLayoutManager(titleLayoutManager);
        rvTitle.setAdapter(mainTitleAdapter);

        //设置布局管理器为2列，纵向
        shopLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //瀑布流相关适配器
        shopAdapter = new WaterFallAdapter(mContext, list_shop);

        rvShop.setLayoutManager(shopLayoutManager);
        rvShop.setAdapter(shopAdapter);
        shopAdapter.setItemClickListener(new WaterFallAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (list_shop.size() > 0) {
                    Intent intent = new Intent(mContext, ShopXQActivity3.class);
                    intent.putExtra("goodsId", list_shop.get(position).getGoodsId() + "");
                    startActivityForResult(intent, MAIN_CODE);
                }
            }
        });


        swipeContent.setHeaderView(new MyHeadRefreshView(getActivity()));
        swipeContent.setFooterView(new MyLoadMoreView(getActivity()));
        swipeContent.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                Log.e("qqqqqqLLLL222",lastVisibleItem+"??");
                if(NetWorkUtil.isConn(mContext)) {
                    page = 1;
                    isRefresh = true;
                    if (countTimer != null)
                        countTimer.cancel();
                    countTimer = new CountTimer(5000, 1000);
                    countTimer.start();
                    getHomePageAsync = new getHomePageAsync();
                    getHomePageAsync.execute();
                }else {
                    ToastUtil.showShortToast("请检查网络是否可用");
                    swipeContent.finishRefresh();
                }
            }

            @Override
            public void loadMore() {
                if(NetWorkUtil.isConn(mContext)) {
                Log.e("qqqqqqLLLL3333",lastVisibleItem+"??");
                page++;
                isLoading = true;
                if (countTimer != null)
                    countTimer.cancel();
                countTimer = new CountTimer(5000, 1000);
                countTimer.start();
                getShopData(lastVisibleItem, page);
                }else {
                    ToastUtil.showShortToast("请检查网络是否可用");
                    swipeContent.finishLoadMore();
                }
            }
        });


        mainTitleAdapter.setOnItemClickListener(new MainTitleAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!isRefresh) {
                    mainTitleAdapter.setPosition(position); //传递当前的点击位置
                    mainTitleAdapter.notifyDataSetChanged(); //通知刷新
                    list_shop.clear();
                    lastVisibleItem = position;
                    page = 1;
                    upData(lastVisibleItem);
                }
            }
        });
        //下拉选项相关适配器
        moreAdapter = new SimpleAdapter(mContext, list_more,
                R.layout.item_shop_more, from, to);
        gvMore.setAdapter(moreAdapter);
        gvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isRefresh) {
                    llNodata.setVisibility(View.GONE);
                    rvTitle.setVisibility(View.VISIBLE);

                    MoveToPosition(titleLayoutManager, rvTitle, position);

                    mainTitleAdapter.setPosition(position);
                    mainTitleAdapter.notifyDataSetChanged();
                    lastVisibleItem = position;
                    list_shop.clear();
                    upData(lastVisibleItem);

                }
            }

        });


        ((ImageView) view.findViewById(imgae_dots[lastBanner])).setImageResource(R.mipmap.ic_shop_banner);
        ;

    }


    @OnClick({R.id.tv_search, R.id.image_more, R.id.img_more, R.id.img_shang, R.id.view_zhe, R.id.image_shopcart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                startActivityForResult(new Intent(mContext, ShopSearchActivity.class), MAIN_CODE);
                break;
            case R.id.image_more:
                showPopup();
                break;
            case R.id.img_more:
                llNodata.setVisibility(View.VISIBLE);
                rvTitle.setVisibility(View.INVISIBLE);
                llYinying.setVisibility(View.INVISIBLE);
                break;
            case R.id.img_shang:
            case R.id.view_zhe:
                llNodata.setVisibility(View.GONE);
                rvTitle.setVisibility(View.VISIBLE);
                llYinying.setVisibility(View.VISIBLE);
                break;
            case R.id.image_shopcart:
                startActivityForResult(new Intent(mContext, ShopCartActivity.class), MAIN_CODE);
                break;

        }
    }


    private void showPopup() {
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shopcart, null);
        tv_dingdan = (TextView) contentViewSign.findViewById(R.id.tv_dingdan);
        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
        tv_dingdan.setOnClickListener(this);
        tv_shangcheng.setOnClickListener(this);
        mPopWindow = new PopupWindow(contentViewSign);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable(
                getActivity().getApplicationContext().getResources(),
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        ));
        mPopWindow.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAsDropDown(view.findViewById(R.id.image_more));
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dingdan:
                startActivityForResult(new Intent(mContext, ShopDingdanActivity.class), MAIN_CODE);
                mPopWindow.dismiss();
                break;
            case R.id.tv_shangcheng:
//                startActivityForResult(new Intent(mContext, APurifierActivity.class), MAIN_CODE);
                startActivityForResult(new Intent(mContext, ShopShangchengActivity.class), MAIN_CODE);
                mPopWindow.dismiss();
                break;
        }
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }

    ShopAsync shopAsync;

    private void getShopData(int id, int page) {

//        if (lastVisibleItem == 1)
////        homePage.get(id-1)
//
//            Log.e("qqqqqRRRR", isRefresh + "???");
//        if (!isRefresh && !isLoading) {
//            if (page != 1) {
//                dialog = MyDialog.showDialog(mContext);
//                dialog.show();
//            }
//        }

        Log.e("qqqqqZZZ", newShopList.get(id).getCategoryId()+","+newShopList.get(id).getTypeName());
        Map<String, Object> params = new HashMap<>();
        if (id == 0) {
            params.put("categoryId", 0 + "");
        } else {
            int cate;
            if (homePage.size() > 0)
                cate = homePage.get(id - 1).getCategoryId();
            else
                cate = newShopList.get(id).getCategoryId();
            params.put("categoryId", cate + "");
        }
        params.put("pageNum", page + "");
        params.put("pageRow", "6");
        if (isLoading) {
            if (shopAsync != null && shopAsync.getStatus() == AsyncTask.Status.RUNNING) {
                shopAsync.cancel(true);
            } else {
                shopAsync = new ShopAsync();
                shopAsync.execute(params);
            }
        } else
            new ShopAsync().execute(params);
    }

    class ShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            if (isCancelled()) {
                return null;
            }

            Map<String, Object> params = maps[0];
            String url = "goods/getGoodsByGoodsCategory";
            String result = HttpUtils.myPostOkHpptRequest(mContext, url, params);
            String code = "";
            int page = Integer.parseInt("" + params.get("pageNum"));
            int categoryId = Integer.parseInt("" + params.get("categoryId"));
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");

                    ShopList newShop;
                    if (page == 1) {
                        if (categoryId == 0) {
                            data[0] = returnData.toString();
                            newShop = newShopList.get(0);
                            newShop.setData(returnData.toString());
                            newShopList.set(0, newShop);
                            dataSign[0] = true;
                        }
                        for (int i = 0; i < homePage.size(); i++) {
                            if (categoryId == homePage.get(i).getCategoryId()) {
                                data[i + 1] = returnData.toString();
                                dataSign[i + 1] = true;

                                newShop = newShopList.get(i + 1);
                                newShop.setData(returnData.toString());
                                newShopList.set(i + 1, newShop);
                            }
                        }
                    } else {
                        JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                        JsonArray list = content.getAsJsonArray("list");
                        if ("100".equals(code)) {
                            code = "1000";
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s) && running) {
                isShopData = true;
                for (int i = 0; i < dataSign.length; i++) {
                    if (dataSign[i] == false) {
                        isShopData = false;
                    }
                }
                if (isShopData) {
                    try {
                        if (isRefresh) {
                            isRefresh = false;
                            swipeContent.finishRefresh();
                        }
                        shopListDao.deleteAll();
                        for (int i = 0; i < newShopList.size(); i++) {
                            if (newShopList.get(i) != null && newShopList.get(i).getData() != null) {
                                shopListDao.insert(newShopList.get(i));
                            }
                        }

                        mainTitleAdapter.setPosition(lastVisibleItem); //传递当前的点击位置
                        mainTitleAdapter.notifyDataSetChanged(); //通知刷新
                        upData(lastVisibleItem);
                        shopAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


//                if (isShopData && isBanner && isPage)
//                    MyDialog.closeDialog(dialog);
            } else if ("1000".equals(s)) {
                if (isLoading) {
                    swipeContent.finishLoadMore();
                    shopAdapter.notifyDataSetChanged();
                }
                isLoading = false;
                if (!isData) {
                    ToastUtil.showShortToast("无更多商品");
                    page--;
                }
            }
        }
    }


    List<ShopPageBean> homePage;

    List<ShopList> newShopList = new ArrayList<>();
    ShopList newShop;

    class getHomePageAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            if (isCancelled()) {
                return null;
            }
            String url = "/productCategory/getHomePageCategory";
            String result = HttpUtils.doGet(mContext, url);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JsonObject content = new JsonParser().parse(jsonObject.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("returnData");
                    Gson gson = new Gson();
                    shopListDao.deleteAll();
                    newShopList.clear();
                    homePage.clear();
                    ShopList newShop = new ShopList();
                    newShop.setId(Long.parseLong(0 + ""));
                    newShop.setTypeName("推荐");
                    newShop.setCategoryId(0);

                    newShopList.add(newShop);
                    for (JsonElement user : list) {
                        //通过反射 得到UserBean.class
                        ShopPageBean userList = gson.fromJson(user, ShopPageBean.class);
                        homePage.add(userList);
                        newShop = new ShopList();
                        Log.e("qqqqqqqHome", homePage.size() + "," + userList.getCategoryName());
                        newShop.setId(Long.parseLong(homePage.size() + ""));
                        newShop.setTypeName(userList.getCategoryName());
                        newShop.setCategoryId(userList.getCategoryId());
                        newShopList.add(newShop);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();

            }
//
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s) && running) {
                isPage = true;
                titles = new String[homePage.size() + 1];
                Map<String, Object> map = null;
                list_more.clear();
                list_title.clear();
                titles[0] = "推荐";
                list_title.add(titles[0]);
                map = new HashMap<String, Object>();
                map.put("title", titles[0]);
                list_more.add(map);
                for (int i = 1; i < homePage.size() + 1; i++) {
                    titles[i] = homePage.get(i - 1).getCategoryName();
                    list_title.add(titles[i]);
                    map = new HashMap<String, Object>();
                    map.put("title", titles[i]);
                    list_more.add(map);
                }

                mainTitleAdapter.notifyDataSetChanged();
                moreAdapter.notifyDataSetChanged();


//                if (isShopData && isBanner && isPage)
//                    MyDialog.closeDialog(dialog);
                getData();

            }
        }
    }


    List<ShopBannerBean> shopBannerBeans;
    List<String> shopBannerImage = new ArrayList<>();

    class getAdByPageAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            if (isCancelled()) {
                return null;
            }

            String url = "/ad/getAdByPage?pageNum=1&pageRow=10";

            String result = HttpUtils.doGet(mContext, url);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    } else {
                        shopBannerDao.deleteAll();
                        ShopBanner shopBanner = new ShopBanner();
                        shopBanner.setData(result);
                        shopBannerDao.insert(shopBanner);
                        JSONObject jsonObject = new JSONObject(result);
                        code = jsonObject.getString("returnCode");
                        JSONObject returnData = jsonObject.getJSONObject("returnData");
                        JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                        if (returnData.get("list").toString() != "null") {
                            JsonArray list = content.getAsJsonArray("list");
                            Gson gson = new Gson();
                            shopBannerBeans.clear();
                            shopBannerImage.clear();
                            for (int i = 0; i < list.size(); i++) {
//                        //通过反射 得到UserBean.class
                                JsonElement user = list.get(i);
                                ShopBannerBean userList = gson.fromJson(user, ShopBannerBean.class);
                                shopBannerBeans.add(userList);
                                shopBannerImage.add(userList.getImage());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
//
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                if (running) {
                    Log.e("qqqqqqIIII2222", shopBannerImage.size() + "??");

                    setBanner(shopBannerImage);

                }
//                if (isShopData && isBanner && isPage)
//                    MyDialog.closeDialog(dialog);
            }
        }
    }


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
                Toast.makeText(mContext, "加载超时请重试", Toast.LENGTH_SHORT).show();
            }
            if (isLoading) {
                swipeContent.finishLoadMore();
                Toast.makeText(mContext, "加载超时请重试", Toast.LENGTH_SHORT).show();
                page--;
            }
        }
    }


    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {


        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }

    String[] data;
    boolean[] dataSign;

    public void getData() {
        if (!isRefresh) {
//            dialog = MyDialog.showDialog(mContext);
//            dialog.show();
        }
        data = new String[homePage.size() + 1];
        dataSign = new boolean[homePage.size() + 1];
        for (int i = 0; i < homePage.size() + 1; i++) {
            dataSign[i] = false;
            getShopData(i, 1);
        }
    }

    public void upData(int i) {
        if (i == 0) {
            llTuijian.setVisibility(View.VISIBLE);
        } else {
            llTuijian.setVisibility(View.GONE);
        }
        list_shop.clear();


        if (data.length > 0) {
            JsonObject content = new JsonParser().parse(data[i]).getAsJsonObject();
            JsonArray list = content.getAsJsonArray("list");
            Gson gson = new Gson();
            if (list != null) {
                for (JsonElement user : list) {
                    //通过反射 得到UserBean.class
                    ShopBean.ReturnData.MyList userList = gson.fromJson(user, ShopBean.ReturnData.MyList.class);
                    list_shop.add(userList);
                }
            }
            shopAdapter.notifyDataSetChanged();
        }
    }


    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            if (running)
                Glide.with(context).load((String) path).into(imageView);
        }
    }
}
