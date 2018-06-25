package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.EvaluateAdapter;
import com.xr.happyFamily.bao.view.FlowTagView;
import com.xr.happyFamily.bao.view.LinearGradientView;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.ed_goods)
    EditText edGoods;


    //标签类相关
    private EvaluateAdapter adapter_hot, adapter_his;


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
        getHistory();


    }

    private void initData() {
        List<String> list = new ArrayList();

        list.add("电暖器");
        list.add("空调");
        list.add("智能传感器");
        list.add("净水器");
        list.add("除尘器");
        adapter_hot.setItems(list);


    }

    private void initView() {
        adapter_hot = new EvaluateAdapter(this, R.layout.item_search);
        ftHot.setAdapter(adapter_hot);
        ftHot.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                String e = adapter_hot.getItem(position).toString();
                Intent intent = new Intent(ShopSearchActivity.this, ShopSearchResultActivity.class);
                intent.putExtra("goodsName", e);
                setHistory(e);
                startActivity(intent);
            }
        });
        adapter_his = new EvaluateAdapter(this, R.layout.item_search);
        ftHis.setAdapter(adapter_his);
        ftHis.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                String e = adapter_his.getItem(position).toString();
                Intent intent = new Intent(ShopSearchActivity.this, ShopSearchResultActivity.class);
                intent.putExtra("goodsName", e);
                startActivity(intent);
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
                if (edGoods.getText().toString().length() == 0) {
                    Toast.makeText(ShopSearchActivity.this, "请输入关键词", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(ShopSearchActivity.this, ShopSearchResultActivity.class);
                    intent.putExtra("goodsName", edGoods.getText().toString());
                    setHistory(edGoods.getText().toString());
                    startActivity(intent);
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
    private TextView tv_quxiao, tv_queding;

    private void showPopup() {
        mContext = ShopSearchActivity.this;
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_main, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
//        tv_context = (TextView) contentViewSign.findViewById(R.id.tv_context);
//        tv_context.setText("确定删除收货地址？");
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
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    List<String> loadList ;

    public void getHistory() {
        loadList= new ArrayList<String>();
        SharedPreferences getDataList = getSharedPreferences("SearchHistoryList", MODE_PRIVATE);
        int environNums = getDataList.getInt("EnvironNums", 0);
        for (int i = environNums; i >0; i--) {
            String environItem = getDataList.getString("history_" + (i-1), null);
            loadList.add(environItem);
        }
        adapter_his.setItems(loadList);
    }

    //将搜索记录保存到历史记录

    public void setHistory(String search) {

        if (loadList.size() > 5)
            loadList.remove(0);
        //遍历list，如果有相同搜索则不添加
        Boolean isSet=true;
        for(int i=0;i<loadList.size();i++){
            if(search.equals(loadList.get(i))){
                isSet=false;
            }
        }
        if (isSet)
        loadList.add(search);
        SharedPreferences.Editor editor = getSharedPreferences("SearchHistoryList", MODE_PRIVATE).edit();
        editor.putInt("EnvironNums", loadList.size());
        for (int i = 0; i < loadList.size(); i++) {
            editor.putString("history_" + i, loadList.get(i));
        }
        editor.commit();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        getHistory();
    }
}
