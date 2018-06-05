package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.qzs.android.fuzzybackgroundlibrary.Fuzzy_Background;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.EvaluateAdapter;
import com.xr.happyFamily.bao.view.FlowTagView;
import com.xr.happyFamily.bao.view.LinearGradientView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class ShopSearchActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_rightText)
    TextView titleRightText;


    @BindView(R.id.img_clear)
    ImageView imgClear;
    @BindView(R.id.ll_his)
    LinearLayout llHis;
    @BindView(R.id.lg_his)
    LinearGradientView lgHis;
    @BindView(R.id.ft_hot)
    FlowTagView ftHot;
    @BindView(R.id.ft_his)
    FlowTagView ftHis;



    //标签类相关
    private EvaluateAdapter adapter_hot,adapter_his;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_search);
        ButterKnife.bind(this);


        initView();
        initData();


    }

    private void initData() {
        List<String> list = new ArrayList();

        list.add("电暖器");
        list.add("空调");
        list.add("智能传感器");
        list.add("净水器");
        list.add("除尘器");
        adapter_hot.setItems(list);
        List<String> list2 = new ArrayList();
        list2.add("werewrewrwerwe");
        list2.add("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
        list2.add("智能传感器");
        list2.add("净水器");
        list2.add("除尘器");
        adapter_his.setItems(list2);

    }

    private void initView() {
        adapter_hot = new EvaluateAdapter(this,R.layout.item_search);
        ftHot.setAdapter(adapter_hot);
        ftHot.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                String e = adapter_hot.getItem(position).toString();
                Toast.makeText(ShopSearchActivity.this, "i am:" + e, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShopSearchActivity.this, ShopSearchResultActivity.class));
            }
        });
        adapter_his = new EvaluateAdapter(this,R.layout.item_search);
        ftHis.setAdapter(adapter_his);
        ftHis.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                String e = adapter_his.getItem(position).toString();
                Toast.makeText(ShopSearchActivity.this, "i am:" + e, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShopSearchActivity.this, ShopSearchResultActivity.class));
            }
        });
    }

    @OnClick({R.id.back, R.id.title_rightText, R.id.img_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title_rightText:
                if ("".equals(titleRightText.getText())) {
                    Toast.makeText(ShopSearchActivity.this, "请输入关键词", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(ShopSearchActivity.this, ShopSearchResultActivity.class));
                }
                break;
            case R.id.img_clear:
                showPopup();
                break;
        }
    }

    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;
    private TextView tv_quxiao,tv_queding;

    private void showPopup() {
        mContext=ShopSearchActivity.this;
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shop_search, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
//        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
//        tv_shopcart.setOnClickListener(this);
        tv_quxiao.setOnClickListener(this);
        tv_queding.setOnClickListener(this);
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
        mPopWindow.setOnDismissListener(new ShopSearchActivity.poponDismissListener());
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
                llHis.setVisibility(View.GONE);
                lgHis.setVisibility(View.GONE);
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
