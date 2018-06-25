package com.xr.happyFamily.le;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.le.adapter.YouYuanAdapter;

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

public class YouYuanActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<Map<String, Object>> mDatas;
    private YouYuanAdapter youYuanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuyuan_youyuan);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        titleText.setText("有缘人");
        imgRight.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
//      获取数据，向适配器传数据，绑定适配器
        mDatas=new ArrayList<Map<String, Object>>();
        initData("初始");
        youYuanAdapter = new YouYuanAdapter(YouYuanActivity.this, mDatas);
        recyclerView.setAdapter(youYuanAdapter);
        //      调用按钮返回事件回调的方法
        youYuanAdapter.buttonSetOnclick(new YouYuanAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, int position) {
                showPopup();
            }
        });
        youYuanAdapter.setOnItemClickListener(new YouYuanAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               startActivity(new Intent(YouYuanActivity.this,XQActivity.class));

            }
        });
//        youYuanAdapter.setOnInnerItemOnClickListener(this);

    }

//    @Override
//    public void itemClick(View v) {
//        int position;
//        position = (Integer) v.getTag();
//        switch (v.getId()) {
//            case R.id.img_add:
//                showPopup();
//                break;
//
//            default:
//                break;
//        }
//    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    int[] touxiang={R.mipmap.ic_shop_pinglun_touxiang,R.mipmap.ic_pipei_touxiang,R.mipmap.ic_fabu_touxiang,R.mipmap.ic_zzpipei_touxiang};
    public void  initData(String name) {
        Map<String, Object> map = null;
        for (int i = 0; i < 4; i++) {
            map = new HashMap<String, Object>();
            map.put("name", name+i);
            map.put("old", "年龄"+i);
            map.put("address", "浙江"+i);
            map.put("id", "白领"+i);
            map.put("touxiang", touxiang[i]);
            mDatas.add(map);
        }

    }
    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;
    private TextView tv_quxiao,tv_queding,tv_context;

    private void showPopup() {
        mContext=YouYuanActivity.this;
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_main, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
        tv_context = (TextView) contentViewSign.findViewById(R.id.tv_context);
        tv_context.setText("是否添加好友");
        tv_quxiao.setText("否");
        tv_queding  .setText("是");
//        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
//        tv_shopcart.setOnClickListener(this);
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(YouYuanActivity.this,"已发送好友申请",Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
            }
        });
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
        mPopWindow.setOnDismissListener(new YouYuanActivity.poponDismissListener());
        mPopWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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