package com.xr.happyFamily.bao;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.MyViewPageAdapter;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.bao.fragment.PingJiaFragment;
import com.xr.happyFamily.bao.fragment.ShopFragment;
import com.xr.happyFamily.bao.fragment.XiangQingFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by win7 on 2018/5/22.
 */

public class ShopXQActivity extends AppCompatActivity {


    @BindView(R.id.tl_flower)
    TabLayout tl_flower;
    @BindView(R.id.vp_flower)
    ViewPager vp_flower;
    Unbinder unbinder;
    List<String> circle = new ArrayList<>();
    List<BaseFragment> fragmentList = new ArrayList<>();
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.img_fenxiang)
    ImageView imgFenxiang;
    @BindView(R.id.tv_shopcart)
    TextView tvShopcart;
    @BindView(R.id.tv_buy)
    TextView tvBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shopxq);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        circle.add("商品");
        circle.add("评价");
        circle.add("详情");

        fragmentList.add(new ShopFragment());
        fragmentList.add(new PingJiaFragment());
        fragmentList.add(new XiangQingFragment());

        for (int i = 0; i < circle.size(); i++) {
            tl_flower.addTab(tl_flower.newTab());
        }
        tl_flower.setTabGravity(TabLayout.GRAVITY_FILL);
        tl_flower.setTabMode(TabLayout.MODE_SCROLLABLE);
        vp_flower.setAdapter(new MyViewPageAdapter(getSupportFragmentManager(), fragmentList));
        initTab();
    }

    private void initTab() {
        tl_flower.setTabGravity(TabLayout.GRAVITY_FILL);
        tl_flower.setTabMode(TabLayout.MODE_FIXED);
        tl_flower.setTabTextColors(ContextCompat.getColor(this, R.color.black), ContextCompat.getColor(this, R.color.green3));
        tl_flower.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.green3));
        tl_flower.setupWithViewPager(vp_flower);
        for (int i = 0; i < circle.size(); i++) {
            tl_flower.getTabAt(i).setText(circle.get(i));
        }
        reflex(tl_flower);
    }

    public static void reflex(final TabLayout tabLayout) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
                    mTabStripField.setAccessible(true);
                    LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);
                    int dp10 = dp2px(tabLayout.getContext(), 10);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);
                        //拿到tabView的mTextView属性
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);
                        TextView mTextView = (TextView) mTextViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);
                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @OnClick({R.id.back, R.id.img_fenxiang, R.id.tv_shopcart, R.id.tv_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_fenxiang:
                Toast.makeText(ShopXQActivity.this, "分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_shopcart:
                Toast.makeText(ShopXQActivity.this, "购物车", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_buy:
                Toast.makeText(ShopXQActivity.this, "购买", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
