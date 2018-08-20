package com.xr.happyFamily.le.BtClock;


import java.text.DateFormat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xr.happyFamily.R;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TimeRemFragment extends Fragment {
    View view;
    Unbinder unbinder;
    String ip = "http://47.98.131.11:8084";

    @BindView(R.id.tv_sgjs_year)
    TextView tv_sgjs_year;
    @BindView(R.id.tv_sgjs_month)
    TextView tv_sgjs_month;
    @BindView(R.id.tv_sgjs_day)
    TextView tv_sgjs_day;
    @BindView(R.id.tv_sgjs_hour)
    TextView tv_sgjs_hour;
    @BindView(R.id.tv_sgjs_minutes)
    TextView tv_sgjs_minutes;
    @BindView(R.id.tv_sgjs_liveyear)
    TextView tv_sgjs_liveyear;
    @BindView(R.id.tv_sjjs_canlive)
    TextView tv_sjjs_canlive;
    int mDay;
    int mMonth;
    int mYear;
SharedPreferences preferences;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_le_sgjs1, container, false);
        unbinder = ButterKnife.bind(this, view);
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        tv_sgjs_hour.setText(hour+"");
        tv_sgjs_minutes.setText(minutes+"");
        preferences = getActivity().getSharedPreferences("this", Context.MODE_PRIVATE);
        SharedPreferences preferences=getActivity().getSharedPreferences("my", Context.MODE_PRIVATE);
        String birthday =preferences.getString("birthday","");
        Log.e("birthday", "onCreateView: "+birthday );
        try {
            Date d1 =new Date(Long.parseLong(birthday));
            String format = "yyyy-MM-dd";
            DateFormat sdf = new SimpleDateFormat(format);
            Date d2 = new Date(System.currentTimeMillis());//你也可以获取当前时间   long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long diff = d2.getTime() - d1.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            int years = (int)(days/365);
            int months= (int)( days%365/30);
            int day = (int) (days%365%30);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("years",years);
            editor.apply();
            tv_sgjs_year.setText(years+"");
            tv_sgjs_liveyear.setText("你已经活了"+years+"年了");
            tv_sjjs_canlive.setText("如果你的生活规律，在健康的状态下，你还可以活"+(89-years)+"年");
            tv_sgjs_month.setText(months+"");
            tv_sgjs_day.setText(day+"");
        } catch (Exception e) {
        }


        return view;
    }

    @OnClick({R.id.iv_sgjs_fh})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_sgjs_fh:
                getActivity().finish();
                break;
        }
    }


}
