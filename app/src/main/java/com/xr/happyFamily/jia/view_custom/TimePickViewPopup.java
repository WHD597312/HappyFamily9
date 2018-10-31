package com.xr.happyFamily.jia.view_custom;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.xr.happyFamily.R;
import com.xr.happyFamily.le.pojo.Time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by YongLiu on 2017/7/20.
 */

public class TimePickViewPopup extends PopupWindow {

    private Context mContext;
    private final View view;

    private TextView tv_quxiao, tv_queding;
    private LoopView loop_state, loop_hour, loop_min;
    int[] data = {0, 0, 0};
    boolean isTouch=false;

    public TimePickViewPopup(final Context context,int state,int hour,int min) {
        data[0]=state;
        data[1]=hour;
        data[2]=min;

        /**
         * 注意：我们的接口同时作为成员变量传入，因为我们用于监听子Item的数据监听
         * */

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_timepick, null);
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


        tv_queding = (TextView) view.findViewById(R.id.tv_queding);
        tv_quxiao = (TextView) view.findViewById(R.id.tv_quxiao);
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        loop_state= (LoopView) view.findViewById(R.id.loop_state);
        loop_hour= (LoopView) view.findViewById(R.id.loop_hour);
        loop_min= (LoopView) view.findViewById(R.id.loop_min);
        ArrayList<String> statelist = new ArrayList();
        ArrayList<String> hourlist = new ArrayList();
        ArrayList<String> minlist = new ArrayList();
        statelist.add("倒计时");
        statelist.add("定时");
        statelist.add("关闭");
        for (int i = 0; i < 24; i++) {
            hourlist.add( i+"");
        }
        for (int i = 0; i < 60; i++) {
            minlist.add( i+"");
        }
        //设置是否循环播放
        loop_state.setNotLoop();
        loop_state.setItemsVisibleCount(7);
        //滚动监听
        loop_state.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                data[0]=index;
            }
        });

        loop_state.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isTouch=true;
                return false;
            }
        });

        loop_hour.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isTouch=true;
                return false;
            }
        });

        loop_min.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isTouch=true;
                return false;
            }
        });

        //设置原始数据
        loop_state.setItems(statelist);
        //设置初始位置
        loop_state.setCenterTextColor(0xff37d39e);
        //设置字体大小
        loop_state.setTextSize(18);

//        //设置是否循环播放
//        loop_hour.setNotLoop();
        //滚动监听

        loop_hour.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                data[1]=index;
            }
        });
        //设置原始数据
        loop_hour.setItems(hourlist);
        loop_hour.setItemsVisibleCount(7);

        //设置初始位置
        loop_hour.setCenterTextColor(0xff37d39e);
        //设置字体大小
        loop_hour.setTextSize(18);

//        //设置是否循环播放
//        loop_min.setNotLoop();
        //滚动监听
        loop_min.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                data[2]=index;
            }
        });
        //设置原始数据
        loop_min.setItems(minlist);

        loop_min.setItemsVisibleCount(7);
        //设置初始位置
        loop_min.setCenterTextColor(0xff37d39e);
        //设置字体大小
        loop_min.setTextSize(18);
        loop_hour.setInitPosition(0);
        loop_min.setInitPosition(0);
        loop_state.setInitPosition(state);

        Calendar c = Calendar.getInstance();//
        int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        int mMinute = c.get(Calendar.MINUTE);//分
        if (state==0){
            hour=hour-mHour;
            min=min-mMinute;
            data[1]=data[1]-mHour;
            data[2]=data[2]-mMinute;
        }else if(state==2){

        }
        Log.e("qqqqqTime",hour+","+min+"<"+mHour+","+mMinute+",,,,,,,,,,"+TimeUnit.HOURS);

        loop_hour.setInitPosition(hour);
        loop_min.setInitPosition(min);
    }


    //关闭dialog
    public static void closeDialog(TimePickViewPopup mDialogUtils) {
        if (mDialogUtils != null && mDialogUtils.isShowing()) {
            mDialogUtils.dismiss();
        }
    }


    public void setOnPublishListener(View.OnClickListener listener) {
        tv_queding.setOnClickListener(listener);
    }

    public TextView getPublishView() {
        return tv_queding;
    }

    public int[] getData() {
        Calendar c = Calendar.getInstance();//
        int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        int mMinute = c.get(Calendar.MINUTE);//分
        if(data[0]==0) {
            data[2] = data[2] + mMinute;
            if(data[2]>59){
                data[2]=data[2]-60;
                data[1]=data[1]+1;
            }
            data[1] = data[1] + mHour;
        }
        return data;
    }


    public void setData(int state,int hour,int min){
        if(!isTouch) {
            int mHour = hour;
            int mMin = min;
            Calendar c = Calendar.getInstance();//
            int mHour2 = c.get(Calendar.HOUR_OF_DAY);//时
            int mMinute2 = c.get(Calendar.MINUTE);//分
            if (state == 2) {
                loop_hour.setCurrentPosition(0);
                loop_min.setCurrentPosition(0);
                loop_state.setCurrentPosition(2);
            } else if (state == 3) {
                loop_state.setCurrentPosition(2);
                loop_hour.setCurrentPosition(mHour);
                loop_min.setCurrentPosition(mMin);
            } else if (state == 0) {
                loop_state.setCurrentPosition(0);
                loop_hour.setCurrentPosition(mHour - mHour2);
                loop_min.setCurrentPosition(mMin - mMinute2);
                Log.e("qqqqqqDDDDD22", mMin + "," + mMinute2);
            } else if (state == 1) {
                loop_state.setCurrentPosition(1);
                loop_hour.setCurrentPosition(mHour);
                loop_min.setCurrentPosition(mMin);
            }

        }
    }
}