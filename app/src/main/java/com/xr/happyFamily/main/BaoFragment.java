package com.xr.happyFamily.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopCartActivity;
import com.xr.happyFamily.bao.ShopDingdanActivity;
import com.xr.happyFamily.bao.ShopSearchActivity;
import com.xr.happyFamily.bao.ShopShangchengActivity;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bao.adapter.MainTitleAdapter;
import com.xr.happyFamily.bao.adapter.ViewPagerAdapter;
import com.xr.happyFamily.bao.adapter.WaterFallAdapter;
import com.xr.happyFamily.bao.view.LinearGradientView;
import com.xr.happyFamily.bao.view.MyScrollview;
import com.xr.happyFamily.bean.ShopBannerBean;
import com.xr.happyFamily.bean.ShopBean;
import com.xr.happyFamily.bean.ShopPageBean;
import com.xr.happyFamily.jia.MyPaperActivity;
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
import butterknife.Unbinder;


public class BaoFragment extends Fragment implements View.OnClickListener {
    View view;
    Unbinder unbinder;
    @BindView(R.id.image_search)
    ImageView imageSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.image_more)
    ImageView imageMore;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
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
    SwipeRefreshLayout swipeContent;
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

    private List<ImageView> mImageList;//轮播的图片集合
    private boolean isStop = false;//线程是否停止
    private static int PAGER_TIOME = 2500;//间隔时间
    // 在values文件假下创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    private int[] imgae_ids = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4};
    //轮播点
    private int[] imgae_dots = new int[]{R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5, R.id.img6, R.id.img7, R.id.img8, R.id.img9, R.id.img10};
    private String[] from = {"title"};
    private int[] to = {R.id.tv_search};
    String[] titles;
    private MainTitleAdapter mainTitleAdapter;
    private List<String> list_title;
    int lastVisibleItem = 0, page = 1;
    List<ShopBean.ReturnData.MyList> list_shop;
    boolean isData = true;
    private MyDialog dialog;

    private boolean isFirst = true;
    SimpleAdapter moreAdapter;
    List<Map<String, Object>> list_more = new ArrayList<Map<String, Object>>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_shop_mypage, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getContext();
        init();
        new getHomePageAsync().execute();
        //广告
        new getAdByPageAsync().execute();
        return view;
    }



    private void init() {
        list_title = new ArrayList<>();
        list_shop = new ArrayList<>();

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
                Intent intent = new Intent(mContext, ShopXQActivity.class);
                intent.putExtra("goodsId", list_shop.get(position).getGoodsId() + "");
                startActivity(intent);
            }
        });
        myScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int height = v.getHeight();
                int scrollViewMeasuredHeight = myScroll.getChildAt(0).getMeasuredHeight();
                if (scrollY == 0) {
//                    System.out.println("滑动到了顶端 view.getScrollY()=" + scrollY);
                }
                if ((scrollY + height) == scrollViewMeasuredHeight) {
//                    System.out.println("滑动到了底部 scrollY=" + scrollY);
                    page++;

                    getShopData(lastVisibleItem, page);
                }
            }
        });

        swipeContent.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeContent.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        swipeContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list_shop.clear();
                getShopData(lastVisibleItem, 1);

            }
        });

        mainTitleAdapter.setOnItemClickListener(new MainTitleAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mainTitleAdapter.setPosition(position); //传递当前的点击位置
                mainTitleAdapter.notifyDataSetChanged(); //通知刷新
                dialog = MyDialog.showDialog(mContext);
                dialog.show();
                list_shop.clear();
                lastVisibleItem = position;
                page = 1;
                getShopData(lastVisibleItem, page);


            }
        });
        //下拉选项相关适配器
        moreAdapter = new SimpleAdapter(mContext, list_more,
                R.layout.item_shop_more, from, to);
        gvMore.setAdapter(moreAdapter);
        gvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                llNodata.setVisibility(View.GONE);
                rvTitle.setVisibility(View.VISIBLE);
                mainTitleAdapter.setPosition(position);
                mainTitleAdapter.notifyDataSetChanged();
                list_shop.clear();
                getShopData(lastVisibleItem, 1);

            }

        });


        dialog = MyDialog.showDialog(mContext);
        dialog.show();

        getShopData(lastVisibleItem, 1);

    }


    /**
     * 第二步、初始化数据（图片、标题、点击事件）
     */
    public void initData() {
        //初始化和图片
        int[] imageRess = new int[]{R.mipmap.banner_shop3x, R.mipmap.banner_shop3x, R.mipmap.banner_shop3x, R.mipmap.banner_shop3x};

        //添加图片到图片列表里
        mImageList = new ArrayList<>();
//        if (shopBannerBeans.size() == 1) {
//            isStop = true;
//        }
        ImageView iv;
        for (int i = 0; i < shopBannerBeans.size(); i++) {
            iv = new ImageView(mContext);
//            iv.setBackgroundResource(imageRess[i]);//设置图片
            Picasso.with(mContext).load(shopBannerBeans.get(i).getImage()).into(iv);
            iv.setId(imgae_ids[i]);//顺便给图片设置id
            iv.setOnClickListener(new pagerImageOnClick());//设置图片点击事件
            mImageList.add(iv);
        }
    }




    //图片点击事件
    private class pagerImageOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pager_image1:
                    Toast.makeText(mContext, "图片1被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image2:
                    Toast.makeText(mContext, "图片2被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image3:
                    Toast.makeText(mContext, "图片3被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image4:
                    Toast.makeText(mContext, "图片4被点击", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**
     * 第三步、给PagerViw设置适配器，并实现自动轮播功能
     */
    public void initView() {

        for (int i = 0; i < shopBannerBeans.size(); i++) {
            view.findViewById(imgae_dots[i]).setVisibility(View.VISIBLE);
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mContext, mImageList, viewPager, shopBannerBeans);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = position % mImageList.size();
                ImageView img_new = (ImageView) view.findViewById(imgae_dots[newPosition]);
                for (int i = 0; i < shopBannerBeans.size(); i++) {
                    if (i == newPosition) {
                        img_new.setImageResource(R.mipmap.ic_shop_banner);
                    } else {
                        ImageView img_old = (ImageView)view.findViewById(imgae_dots[i]);
                        img_old.setImageResource(R.mipmap.ic_shop_banner_wei);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 第五步: 设置自动播放,每隔PAGER_TIOME秒换一张图片
     */
    private void autoPlayView() {
        //自动播放图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }
                    });
                    SystemClock.sleep(PAGER_TIOME);
                }
            }
        }).start();
    }


    @OnClick({R.id.tv_search, R.id.image_more, R.id.img_more, R.id.img_shang, R.id.view_zhe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                startActivity(new Intent(mContext, ShopSearchActivity.class));
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


        }
    }


    private void showPopup() {

        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shopcart, null);
        tv_shopcart = (TextView) contentViewSign.findViewById(R.id.tv_shopcart);
        tv_dingdan = (TextView) contentViewSign.findViewById(R.id.tv_dingdan);
        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
        tv_shopcart.setOnClickListener(this);
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
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
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
            case R.id.tv_shopcart:
                startActivity(new Intent(mContext, ShopCartActivity.class));
                mPopWindow.dismiss();
                break;
            case R.id.tv_dingdan:
                startActivity(new Intent(mContext, ShopDingdanActivity.class));
                mPopWindow.dismiss();
                break;
            case R.id.tv_shangcheng:
                startActivity(new Intent(mContext, ShopShangchengActivity.class));
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




    private void getShopData(int id, int page) {
        Map<String, Object> params = new HashMap<>();
        if (id == 0) {
            llTuijian.setVisibility(View.VISIBLE);
        } else
            llTuijian.setVisibility(View.GONE);


        params.put("categoryId", id + "");
        params.put("pageNum", page + "");
        params.put("pageRow", "6");
        new ShopAsync().execute(params);
    }

    class ShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "goods/getGoodsByGoodsCategory";
            String result = HttpUtils.myPostOkHpptRequest(mContext, url, params);
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
                    Toast.makeText(mContext, "无更多数据", Toast.LENGTH_SHORT).show();
                }
                MyDialog.closeDialog(dialog);
            }
        }
    }


    List<ShopPageBean> homePage = new ArrayList<>();

    class getHomePageAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            String url = "/productCategory/getHomePageCategory";
            String result = HttpUtils.doGet(mContext, url);

            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JsonObject content = new JsonParser().parse(jsonObject.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("returnData");
                    Gson gson = new Gson();
                    for (JsonElement user : list) {
                        //通过反射 得到UserBean.class
                        ShopPageBean userList = gson.fromJson(user, ShopPageBean.class);
                        homePage.add(userList);
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
                titles = new String[homePage.size() + 1];
                Map<String, Object> map = null;
                list_more.clear();
                list_title.clear();

                titles[0] = "全部";
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
            }
        }
    }


    List<ShopBannerBean> shopBannerBeans = new ArrayList<>();

    class getAdByPageAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            String url = "/ad/getAdByPage?pageNum=1&pageRow=10";

            String result = HttpUtils.doGet(mContext, url);

            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    if (returnData.get("list").toString() != "null") {
                        JsonArray list = content.getAsJsonArray("list");
                        Gson gson = new Gson();
                        for (int i = 0; i < list.size(); i++) {
//                        //通过反射 得到UserBean.class
                            JsonElement user = list.get(i);
                            ShopBannerBean userList = gson.fromJson(user, ShopBannerBean.class);
                            shopBannerBeans.add(userList);
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
                Log.i("qqqqqqqqSSS", shopBannerBeans.size() + "?");
                initData();//初始化数据
                initView();//初始化View，设置适配器
                autoPlayView();//开启线程，自动播放
            }
        }
    }
}
