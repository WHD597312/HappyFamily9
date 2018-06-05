package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xr.happyFamily.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class ShopDingdanXQActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.img_wuliu)
    ImageView imgWuliu;
    @BindView(R.id.tv_wuliu)
    TextView tvWuliu;
    @BindView(R.id.img_address)
    ImageView imgAddress;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.img_shop_pic)
    ImageView imgShopPic;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_shop_type)
    TextView tvShopType;
    @BindView(R.id.tv_shop_price)
    TextView tvShopPrice;
    @BindView(R.id.tv_shop_num)
    TextView tvShopNum;
    @BindView(R.id.img_tui)
    ImageView imgTui;
    @BindView(R.id.tv_yunfei)
    TextView tvYunfei;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_dingdan)
    TextView tvDingdan;
    @BindView(R.id.tv_zhifubao)
    TextView tvZhifubao;
    @BindView(R.id.tv_changjian)
    TextView tvChangjian;
    @BindView(R.id.tv_fukuan)
    TextView tvFukuan;
    @BindView(R.id.tv_fahuo)
    TextView tvFahuo;
    @BindView(R.id.img_del)
    ImageView imgDel;
    @BindView(R.id.img_chakan)
    ImageView imgChakan;
    @BindView(R.id.img_queren)
    ImageView imgQueren;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_dingdan_xq);
        ButterKnife.bind(this);
        titleRightText.setVisibility(View.GONE);
        titleText.setText("订单详情");

    }


    @OnClick({R.id.back,  R.id.img_tui, R.id.tv_dingdan,R.id.img_del,R.id.img_chakan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.img_tui:
                startActivity(new Intent(this,FuWuActivity.class));
                break;
            case R.id.tv_dingdan:
                break;
            case R.id.img_del:
                showPopup();
                break;
            case R.id.img_chakan:
                startActivity(new Intent(this,WuLiuActivity.class));
                break;
        }
    }


    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;
    private TextView tv_quxiao,tv_queding,tv_context;

    private void showPopup() {
        mContext=ShopDingdanXQActivity.this;
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shop_search, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
        tv_context = (TextView) contentViewSign.findViewById(R.id.tv_context);
        tv_quxiao.setOnClickListener(this);
        tv_queding.setOnClickListener(this);
        tv_context.setText("是否确认删除订单？");
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
        mPopWindow.setOnDismissListener(new ShopDingdanXQActivity.poponDismissListener());
        mPopWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_quxiao:
                mPopWindow.dismiss();
                break;
            case R.id.tv_queren:
                mPopWindow.dismiss();
                finish();
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
