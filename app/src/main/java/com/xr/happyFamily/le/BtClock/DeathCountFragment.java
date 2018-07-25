package com.xr.happyFamily.le.BtClock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DeathCountFragment extends Fragment {
    View view;
    Unbinder unbinder;

    @BindView(R.id.tv_sw_sm) TextView tv_sw_sm;
    @BindView(R.id.tv_sw_sjj) TextView tv_sw_sjj;
    @BindView(R.id.tv_sw_week) TextView tv_sw_week;
    @BindView(R.id.tv_sw_eat) TextView tv_sw_eat;
    SharedPreferences preferences;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_le_sgjs2, container, false);
        unbinder = ButterKnife.bind(this, view);
        preferences = getActivity().getSharedPreferences("this", Context.MODE_PRIVATE);
        SharedPreferences preferences=getActivity().getSharedPreferences("my", Context.MODE_PRIVATE);
        String birthday =preferences.getString("birthday","");
        try {
            Date d1 =new Date(Long.parseLong(birthday));
            String format = "yyyy-MM-dd";
            DateFormat sdf = new SimpleDateFormat(format);
            Date d2 = new Date(System.currentTimeMillis());//你也可以获取当前时间   long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long diff = d2.getTime() - d1.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            int year = (int)(days/365);
            tv_sw_sm.setText("你还有"+(100-year)+"余年的寿命");
            tv_sw_sjj.setText(("睡"+(100-year)*365)+"次觉");
            tv_sw_week.setText(("有"+(100-year)*365/7)+"个周末");
            tv_sw_eat.setText("吃"+(int)((100-year)*12*0.132)+"吨粮食");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
    @OnClick({R.id.iv_sgjs_fh2})
    public void  onClick(View view){
        switch (view.getId()){

            case R.id.iv_sgjs_fh2:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
