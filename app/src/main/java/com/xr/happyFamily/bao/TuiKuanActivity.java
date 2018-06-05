package com.xr.happyFamily.bao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xr.happyFamily.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class TuiKuanActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
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
    @BindView(R.id.tv_choose)
    TextView tvChoose;
    @BindView(R.id.tv_money)
    TextView tvMoney;

    int type_tui;
    @BindView(R.id.tv_tui)
    TextView tvTui;
    @BindView(R.id.tv_tijiao)
    TextView tvTijiao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shenqing_tuikuan);
        ButterKnife.bind(this);
        titleRightText.setVisibility(View.GONE);
        titleText.setText("申请退款");
        type_tui = getIntent().getIntExtra("type", 0);
        if (type_tui == 1) {
            tvTui.setText("退款原因");
        }else {
            tvTui.setText("退货原因");
        }

    }


    @OnClick({R.id.back, R.id.tv_choose, R.id.tv_tijiao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_choose:
                showPopup();
                break;
            case R.id.tv_tijiao:
                startActivity(new Intent(this,TuiKuanXQActivity.class));
                break;

        }
    }


    private View contentViewSign;
    private PopupWindow mPopWindow;
    private ImageView img_close;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;
    private View view_dis;

    private void showPopup() {
        contentViewSign = LayoutInflater.from(this).inflate(R.layout.popup_tuihuo, null);
        view_dis =  contentViewSign.findViewById(R.id.view_dis);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        tv1 = (TextView) contentViewSign.findViewById(R.id.tv1);
        tv2 = (TextView) contentViewSign.findViewById(R.id.tv2);
        tv3 = (TextView) contentViewSign.findViewById(R.id.tv3);
        tv4 = (TextView) contentViewSign.findViewById(R.id.tv4);
        tv5 = (TextView) contentViewSign.findViewById(R.id.tv5);
        tv6 = (TextView) contentViewSign.findViewById(R.id.tv6);
        tv7 = (TextView) contentViewSign.findViewById(R.id.tv7);
//        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
        view_dis.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        img_close.setOnClickListener(this);

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

    }

    List<String> list;


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_dis:
                mPopWindow.dismiss();
                break;
            case R.id.tv1:
                tvChoose.setText(tv1.getText());
                mPopWindow.dismiss();
                break;
            case R.id.tv2:
                tvChoose.setText(tv2.getText());
                mPopWindow.dismiss();
                break;
            case R.id.tv3:
                tvChoose.setText(tv3.getText());
                mPopWindow.dismiss();
                break;
            case R.id.tv4:
                tvChoose.setText(tv4.getText());
                mPopWindow.dismiss();
                break;
            case R.id.tv5:
                tvChoose.setText(tv5.getText());
                mPopWindow.dismiss();
                break;
            case R.id.tv6:
                tvChoose.setText(tv6.getText());
                mPopWindow.dismiss();
                break;
            case R.id.tv7:
                tvChoose.setText(tv7.getText());
                mPopWindow.dismiss();
                break;
            case R.id.img_close:
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tvChoose.setText("修改原因");
    }
}
