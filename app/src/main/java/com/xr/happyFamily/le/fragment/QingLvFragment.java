package com.xr.happyFamily.le.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.le.adapter.ClockQinglvAdapter;

import com.xr.happyFamily.le.clock.MsgActivity;
import com.xr.happyFamily.le.clock.QinglvAddActivity;
import com.xr.happyFamily.le.view.TimeBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by TYQ on 2017/9/7.
 */

public class QingLvFragment extends BaseFragment  {

    Unbinder unbinder;
    Context mContext;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.arcprogressBar)
    TimeBar arcprogressBar;
    @BindView(R.id.tc_time)
    TextClock tcTime;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img_add)
    ImageView imgAdd;


    private TimeBar timeBar;
    private ClockQinglvAdapter qinglvAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_clock_qinglv, container, false);
        unbinder = ButterKnife.bind(this, view);
        timeBar = (TimeBar) view.findViewById(R.id.arcprogressBar);
        List<Map<String, Object>> qinglvList = new ArrayList<>();
        tcTime.setFormat12Hour(null);
        tcTime.setFormat24Hour("HH:mm");
        int[][] time = new int[3][2];
        int[] h = new int[3];
        int[] m = new int[3];
        h[0] = 8;
        m[0] = 30;
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "亲爱的");
        map1.put("time", setStringTime(h[0], m[0]));
        map1.put("context", "起床啦，起床啦");
        map1.put("day", "周一 周二 周三 周四 周五");
        map1.put("sign", 1);

        h[1] = 12;
        m[1] = 00;
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "二宝");
        map2.put("time", setStringTime(h[1], m[1]));
        map2.put("context", "睡觉");
        map2.put("day", "周一");
        map2.put("sign", 0);

        h[2] = 22;
        m[2] = 30;
        Map<String, Object> map3 = new HashMap<>();
        map3.put("name", "二宝");
        map3.put("time", setStringTime(h[2], m[2]));
        map3.put("context", "打飞机时间");
        map3.put("day", "每天");
        map3.put("sign", 1);

        qinglvList.add(map1);
        qinglvList.add(map2);
        qinglvList.add(map3);

        for (int i = 0; i < 3; i++) {
            time[i][0] = h[i];
            time[i][1] = m[i];
        }

        timeBar.setTime(time,1);

        qinglvAdapter = new ClockQinglvAdapter(getActivity(), qinglvList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(qinglvAdapter);
        return view;

    }


    public String setStringTime(int h, int m) {
        String str = "";
        if (h < 10)
            str = "0" + h;
        else
            str = h + "";
        if (m < 10)
            str = str + ":0" + m;
        else
            str = str + ":" + m;
        return str;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    boolean isDel=false;

    @OnClick({R.id.back, R.id.img_right,R.id.img_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.img_right:
                getActivity().startActivity(new Intent(getActivity(), MsgActivity.class));
                break;
            case R.id.img_add:
                Intent intent=new Intent(getActivity(), QinglvAddActivity.class);
                intent.putExtra("type","qinglv");
                getActivity().startActivity(intent);

                break;

        }
    }

}
