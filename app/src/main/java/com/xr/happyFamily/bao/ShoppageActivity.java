package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.MainTitleAdapter;
import com.xr.happyFamily.bao.adapter.ViewPagerAdapter;
import com.xr.happyFamily.bao.adapter.WaterFallAdapter;
import com.xr.happyFamily.bao.view.LinearGradientView;
import com.xr.happyFamily.bean.PersonCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShoppageActivity extends AppCompatActivity implements View.OnClickListener {
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
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.gv_more)
    GridView gvMore;

    @BindView(R.id.ll_nodata)
    LinearLayout llNodata;
    @BindView(R.id.ll_data)
    LinearLayout llData;
    @BindView(R.id.img_shang)
    ImageView imgShang;
    @BindView(R.id.ll_yinying)
    LinearGradientView llYinying;
    @BindView(R.id.view_zhe)
    View viewZhe;
    @BindView(R.id.rv_title)
    RecyclerView rvTitle;
    @BindView(R.id.rv_shop)
    RecyclerView rvShop;

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
    private int[] imgae_dots = new int[]{R.id.img1, R.id.img2, R.id.img3, R.id.img4};
    private String[] from = {"title"};
    private int[] to = {R.id.tv_search};
    String[] titles = new String[]{"推荐", "电暖器", "空调", "除尘机", "净水器", "智能传感器"};
    private MainTitleAdapter mainTitleAdapter;
    private List<String> list_title;
    private List<PersonCard> list_shop;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_mypage);
        ButterKnife.bind(this);
        mContext = ShoppageActivity.this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        unbinder = ButterKnife.bind(this);
        init();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {
        list_title = new ArrayList<>();
        list_shop = new ArrayList<>();
        list_shop = buildData("电暖器");
        for (int i = 0; i < 6; i++) {
            list_title.add(titles[i]);
        }
        mainTitleAdapter = new MainTitleAdapter(this, list_title);


        titleLayoutManager = new LinearLayoutManager(this);
        titleLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTitle.setLayoutManager(titleLayoutManager);
        rvTitle.setAdapter(mainTitleAdapter);

        //设置布局管理器为2列，纵向
        shopLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //瀑布流相关适配器
        shopAdapter = new WaterFallAdapter(this, list_shop);
        SimpleAdapter moreAdapter = new SimpleAdapter(this, getList(),
                R.layout.item_shop_more, from, to);
        rvShop.setLayoutManager(shopLayoutManager);
        rvShop.setAdapter(shopAdapter);
        shopAdapter.setItemClickListener(new WaterFallAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ShoppageActivity.this, "点击了" + position, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShoppageActivity.this, ShopXQActivity.class));
            }
        });
        mainTitleAdapter.setOnItemClickListener(new MainTitleAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mainTitleAdapter.setPosition(position); //传递当前的点击位置
                mainTitleAdapter.notifyDataSetChanged(); //通知刷新
                list_shop.clear();
                list_shop.addAll(buildData(titles[position]));
                shopAdapter.notifyDataSetChanged();
            }
        });
        //下拉选项相关适配器
        gvMore.setAdapter(moreAdapter);
        gvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ShoppageActivity.this, "i am:" + titles[position], Toast.LENGTH_SHORT).show();
                llNodata.setVisibility(View.GONE);
                rvTitle.setVisibility(View.VISIBLE);
                mainTitleAdapter.setPosition(position);
                mainTitleAdapter.notifyDataSetChanged();
                list_shop.clear();
                list_shop.addAll(buildData(titles[position]));
                shopAdapter.notifyDataSetChanged();
            }

        });

        initData();//初始化数据
        initView();//初始化View，设置适配器
        autoPlayView();//开启线程，自动播放
    }




    public List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (int i = 0; i < 6; i++) {
            map = new HashMap<String, Object>();
            map.put("title", titles[i]);
            list.add(map);
        }
        return list;
    }

    /**
     * 第二步、初始化数据（图片、标题、点击事件）
     */
    public void initData() {
        //初始化和图片
        int[] imageRess = new int[]{R.mipmap.banner_shop3x, R.mipmap.banner_shop3x, R.mipmap.banner_shop3x, R.mipmap.banner_shop3x};

        //添加图片到图片列表里
        mImageList = new ArrayList<>();
        ImageView iv;
        for (int i = 0; i < 4; i++) {
            iv = new ImageView(this);
            iv.setBackgroundResource(imageRess[i]);//设置图片
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
                    Toast.makeText(ShoppageActivity.this, "图片1被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image2:
                    Toast.makeText(ShoppageActivity.this, "图片2被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image3:
                    Toast.makeText(ShoppageActivity.this, "图片3被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image4:
                    Toast.makeText(ShoppageActivity.this, "图片4被点击", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**
     * 第三步、给PagerViw设置适配器，并实现自动轮播功能
     */
    public void initView() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mImageList, viewPager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = position % mImageList.size();
                ImageView img_new = (ImageView) findViewById(imgae_dots[newPosition]);
                for (int i = 0; i < 4; i++) {

                    if (i == newPosition) {
                        img_new.setImageResource(R.mipmap.ic_shop_banner);
                    } else {
                        ImageView img_old = (ImageView) findViewById(imgae_dots[i]);
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
                    runOnUiThread(new Runnable() {
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


    @OnClick({R.id.tv_search, R.id.image_more, R.id.img_more, R.id.img_shang, R.id.view_zhe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                startActivity(new Intent(this, ShopSearchActivity.class));
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
        mPopWindow.showAsDropDown(findViewById(R.id.image_more));
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_shopcart:
                startActivity(new Intent(this, ShopCartActivity.class));
                mPopWindow.dismiss();
                break;
            case R.id.tv_dingdan:
                startActivity(new Intent(this, ShopDingdanActivity.class));
                mPopWindow.dismiss();
                break;
            case R.id.tv_shangcheng:
                startActivity(new Intent(this, ShopShangchengActivity.class));
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
}
