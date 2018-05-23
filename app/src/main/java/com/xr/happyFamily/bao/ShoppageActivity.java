package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.WaterFallAdapter;
import com.xr.happyFamily.bean.PersonCard;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.ll2)
    LinearLayout ll2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.ll3)
    LinearLayout ll3;
    @BindView(R.id.img4)
    ImageView img4;
    @BindView(R.id.ll4)
    LinearLayout ll4;
    @BindView(R.id.img5)
    ImageView img5;
    @BindView(R.id.ll5)
    LinearLayout ll5;
    @BindView(R.id.img6)
    ImageView img6;
    @BindView(R.id.ll6)
    LinearLayout ll6;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private WaterFallAdapter mAdapter;
    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;
    private TextView tv_shopcart,tv_dingdan,tv_shangcheng;

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


//    @OnClick({R.id.bt_add_equipment})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.bt_add_equipment:
//                startActivity(new Intent(this, AddEquipmentActivity.class));
//                break;
//
//        }
//
//    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new WaterFallAdapter(this, buildData());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    private List<PersonCard> buildData() {

        String[] names = {"电暖气1", "电暖气2", "电暖气3", "电暖气4"};
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


    @OnClick({R.id.tv_search, R.id.image_more, R.id.img1, R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5, R.id.ll6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                startActivity(new Intent(this, ShopSearchActivity.class));
                break;
            case R.id.image_more:
                showPopup();
                break;
            case R.id.img1:
                break;
            case R.id.ll1:
                break;
            case R.id.ll2:
                break;
            case R.id.ll3:
                break;
            case R.id.ll4:
                break;
            case R.id.ll5:
                break;
            case R.id.ll6:
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
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
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
                break;
            case R.id.tv_dingdan:
                startActivity(new Intent(this, ShopDingdanActivity.class));
                break;
            case R.id.tv_shangcheng:
                startActivity(new Intent(this, ShopShangchengActivity.class));
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
