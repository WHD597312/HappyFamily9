package com.xr.happyFamily.bao;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.CityAdapter;
import com.xr.happyFamily.bao.view.LinearGradientView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class ShopAddAddressActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.img_choose)
    ImageView imgChoose;
    @BindView(R.id.tv_choose)
    TextView tvChoose;
    @BindView(R.id.view_line)
    View viewLine;
    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mContext = ShopAddAddressActivity.this;
        setContentView(R.layout.activity_shop_add_address);
        ButterKnife.bind(this);

        titleText.setText("添加地址");
        titleRightText.setText("保存");


    }


    @OnClick({R.id.back, R.id.img_choose, R.id.tv_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_choose:
                break;
            case R.id.tv_choose:
                showPopup();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_dis:
            case R.id.img_close:
                mPopWindow.dismiss();
                break;

        }
    }

    private RecyclerView recyclerview;
    private ImageView img_close;
    private View view_dis;

    private void showPopup() {

        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shop_city, null);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        view_dis=contentViewSign.findViewById(R.id.view_dis);
        recyclerview = (RecyclerView) contentViewSign.findViewById(R.id.recyclerview);
        ArrayList<String> datas = initData();
        CityAdapter cityAdapter = new CityAdapter(ShopAddAddressActivity.this, datas);
        img_close.setOnClickListener(this);
        view_dis.setOnClickListener(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(ShopAddAddressActivity.this));
        recyclerview.setAdapter(cityAdapter);

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
        mPopWindow.setClippingEnabled(false);
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAsDropDown(findViewById(R.id.view_line));
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

    protected ArrayList<String> initData() {
        ArrayList<String> mDatas = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            mDatas.add("A    安徽" + i);
        }
        return mDatas;
    }
}
