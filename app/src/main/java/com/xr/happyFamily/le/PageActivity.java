package com.xr.happyFamily.le;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopSearchActivity;
import com.xr.happyFamily.bao.ShoppageActivity;
import com.xr.happyFamily.bao.TestActivity;
import com.xr.happyFamily.bao.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PageActivity extends AppCompatActivity {

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
    @BindView(R.id.lineLayout_dot)
    LinearLayout lineLayoutDot;


    private List<ImageView> mImageList;//轮播的图片集合
    private boolean isStop = false;//线程是否停止
    private static int PAGER_TIOME = 2500;//间隔时间
    // 在values文件假下创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    private int[] imgae_ids = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4};
    //轮播点
    private int[] imgae_dots = new int[]{R.id.img1, R.id.img2, R.id.img3, R.id.img4};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy_mypage);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initData();//初始化数据
        initView();//初始化View，设置适配器
        autoPlayView();//开启线程，自动播放
    }


    /**
     * 第二步、初始化数据（图片、标题、点击事件）
     */
    public void initData() {
        //初始化和图片
        int[] imageRess = new int[]{R.mipmap.banner_happy, R.mipmap.banner_happy, R.mipmap.banner_happy, R.mipmap.banner_happy};

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
                    Toast.makeText(PageActivity.this, "图片1被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image2:
                    Toast.makeText(PageActivity.this, "图片2被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image3:
                    Toast.makeText(PageActivity.this, "图片3被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image4:
                    Toast.makeText(PageActivity.this, "图片4被点击", Toast.LENGTH_SHORT).show();
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


    @OnClick({R.id.ll_xuyuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_xuyuan:
                startActivity(new Intent(this, XuYuanActivity.class));
                break;
        }
    }
}
