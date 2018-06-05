package com.xr.happyFamily.bao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qzs.android.fuzzybackgroundlibrary.Fuzzy_Background;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.MyViewPageAdapter;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.bao.fragment.PingJiaFragment;
import com.xr.happyFamily.bao.fragment.ShopFragment;
import com.xr.happyFamily.bao.fragment.XiangQingFragment;

import java.io.FileOutputStream;
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
    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mContext = ShopXQActivity.this;
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.back, R.id.img_fenxiang, R.id.tv_shopcart, R.id.tv_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_fenxiang:
                showPopup();
                break;
            case R.id.tv_shopcart:
                Toast.makeText(ShopXQActivity.this, "已加入购物车", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_buy:
                startActivity(new Intent(this,ShopConfActivity.class));
                break;
        }
    }


    private LinearLayout rl_bg;
    public static Bitmap captureScreen(Activity activity) {

        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);

        Bitmap bmp=activity.getWindow().getDecorView().getDrawingCache();


        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.e("TAG", "" + statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height - statusBarHeight);


        return b;

    }
    private GradientDrawable mBackShadowDrawableLR;
    private ImageView img_close;
    private TextView tv_dis;
    private RelativeLayout rl_dis;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showPopup() {

        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shop_fenxiang, null);
        rl_bg = (LinearLayout) contentViewSign.findViewById(R.id.rl_bg);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        tv_dis = (TextView) contentViewSign.findViewById(R.id.tv_dis);
        rl_dis = (RelativeLayout) contentViewSign.findViewById(R.id.rl_dis);
//        tv_dingdan = (TextView) contentViewSign.findViewById(R.id.tv_dingdan);
//        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
//        tv_shopcart.setOnClickListener(this);
//        tv_dingdan.setOnClickListener(this);
//        tv_shangcheng.setOnClickListener(this);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        tv_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        rl_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        Resources res = getResources();
        Bitmap finalBitmap = Fuzzy_Background.with(this)
                .bitmap(captureScreen(this)) //要模糊的图片
                .radius(20)//模糊半径
                .blur();
        Drawable drawable2 = new BitmapDrawable(finalBitmap);
        rl_bg.setBackground(drawable2);
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
        mPopWindow.setOnDismissListener(new ShopXQActivity.poponDismissListener());
        mPopWindow.showAsDropDown(findViewById(R.id.view_pop));
    }


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
}
