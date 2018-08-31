package com.xr.happyFamily.jia.view_custom;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.xr.happyFamily.R;

import java.util.ArrayList;

/**
 * Created by YongLiu on 2017/7/20.
 */

public class FengSuViewPopup extends PopupWindow {

    private Context mContext;
    private final View view;


    String state="-1";
    private TextView tv_low,tv_mid,tv_height,tv_queding;
    private LinearLayout ll_low,ll_mid,ll_height;
    Drawable dra_fengsu1,dra_fengsu11,dra_fengsu2,dra_fengsu22,dra_fengsu3,dra_fengsu33;



    public FengSuViewPopup(final Context context, String state2) {
        state=state2;
        /**
         * 注意：我们的接口同时作为成员变量传入，因为我们用于监听子Item的数据监听
         * */

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_dehumidifier_wind, null);
        mContext = context;
        this.setContentView(view);
        //自定义基础，设置我们显示控件的宽，高，焦点，点击外部关闭PopupWindow操作
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //更新试图
        this.update();
        //设置背景
        ColorDrawable colorDrawable = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(colorDrawable);
        ll_low = (LinearLayout) view.findViewById(R.id.ll_wind_low);
        ll_mid = (LinearLayout) view.findViewById(R.id.ll_wind_mid);
        ll_height = (LinearLayout) view.findViewById(R.id.ll_wind_height);
        tv_low = (TextView) view.findViewById(R.id.tv_wind_low);
        tv_mid = (TextView) view.findViewById(R.id.tv_wind_mid);
        tv_height = (TextView) view.findViewById(R.id.tv_wind_height);
        tv_queding = (TextView) view.findViewById(R.id.tv_queding);

        dra_fengsu1= context.getResources().getDrawable(R.mipmap.ic_fengsu1);
        dra_fengsu1.setBounds(0, 0, dra_fengsu1.getMinimumWidth(), dra_fengsu1.getMinimumHeight());
        dra_fengsu11= context.getResources().getDrawable(R.mipmap.ic_fengsu11);
        dra_fengsu11.setBounds(0, 0, dra_fengsu11.getMinimumWidth(), dra_fengsu11.getMinimumHeight());
        dra_fengsu2= context.getResources().getDrawable(R.mipmap.ic_fengsu2);
        dra_fengsu2.setBounds(0, 0, dra_fengsu2.getMinimumWidth(), dra_fengsu2.getMinimumHeight());
        dra_fengsu22= context.getResources().getDrawable(R.mipmap.ic_fengsu22);
        dra_fengsu22.setBounds(0, 0, dra_fengsu22.getMinimumWidth(), dra_fengsu22.getMinimumHeight());
        dra_fengsu3= context.getResources().getDrawable(R.mipmap.ic_fengsu3);
        dra_fengsu3.setBounds(0, 0, dra_fengsu3.getMinimumWidth(), dra_fengsu3.getMinimumHeight());
        dra_fengsu33= context.getResources().getDrawable(R.mipmap.ic_fengsu33);
        dra_fengsu33.setBounds(0, 0, dra_fengsu33.getMinimumWidth(), dra_fengsu33.getMinimumHeight());


            if (state.equals("000"))
                tv_low.setCompoundDrawables(dra_fengsu11, null, null, null);
            else if (state.equals("001"))
                tv_mid.setCompoundDrawables(dra_fengsu22, null, null, null);
            else if (state.equals("010"))
                tv_height.setCompoundDrawables(dra_fengsu33, null, null, null);

        ll_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    state="000";
                initDraw();
                tv_low.setCompoundDrawables(dra_fengsu11,null,null,null);
            }
        });

        ll_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    state="001";
                initDraw();
                initDraw();
                tv_mid.setCompoundDrawables(dra_fengsu22,null,null,null);
            }
        });

        ll_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    state="010";
                initDraw();
                initDraw();
                tv_height.setCompoundDrawables(dra_fengsu33,null,null,null);
            }
        });


    }


    //关闭dialog
    public static void closeDialog(FengSuViewPopup mDialogUtils) {
        if (mDialogUtils != null && mDialogUtils.isShowing()) {
            mDialogUtils.dismiss();
        }
    }


    public void setOnPublishListener(View.OnClickListener listener) {
        tv_queding.setOnClickListener(listener);
    }


    public String getData() {
        return state;
    }


    public void initDraw(){
        tv_low.setCompoundDrawables(dra_fengsu1,null,null,null);
        tv_mid.setCompoundDrawables(dra_fengsu2,null,null,null);
        tv_height.setCompoundDrawables(dra_fengsu3,null,null,null);
    }

}