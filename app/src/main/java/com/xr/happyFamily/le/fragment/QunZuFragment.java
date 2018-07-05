package com.xr.happyFamily.le.fragment;

import android.content.Context;
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
import com.xr.happyFamily.le.adapter.ClockQunzuAdapter;
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

public class QunZuFragment extends BaseFragment  {

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
    private ClockQunzuAdapter qunzuAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_clock_qunzu, container, false);
        unbinder = ButterKnife.bind(this, view);
        timeBar = (TimeBar) view.findViewById(R.id.arcprogressBar);
        tcTime.setFormat12Hour(null);
        tcTime.setFormat24Hour("HH:mm");

        List<Map<String, Object>> qunzuList = new ArrayList<>();

        int[][] time = new int[3][2];
        int[] h = new int[3];
        int[] m = new int[3];
        h[0] = 6;
        m[0] = 30;
        Map<String, Object> map1 = new HashMap<>();
        map1.put("size", 3);
        map1.put("time", setStringTime(h[0], m[0]));
        map1.put("context", "起床啦，起床啦");
        map1.put("sign", 0);

        h[1] = 9;
        m[1] = 00;
        Map<String, Object> map2 = new HashMap<>();
        map2.put("size", 2);
        map2.put("time", setStringTime(h[1], m[1]));
        map2.put("context", "睡觉");
        map2.put("sign", 1);

        h[2] = 19;
        m[2] = 30;
        Map<String, Object> map3 = new HashMap<>();
        map3.put("size", 4);
        map3.put("time", setStringTime(h[2], m[2]));
        map3.put("context", "打飞机时间");
        map3.put("sign", 1);

        qunzuList.add(map1);
        qunzuList.add(map2);
        qunzuList.add(map3);

        for (int i = 0; i < 3; i++) {
            time[i][0] = h[i];
            time[i][1] = m[i];
        }

        timeBar.setTime(time,0);

        qunzuAdapter = new ClockQunzuAdapter(getActivity(), qunzuList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(qunzuAdapter);
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

    @OnClick({R.id.back, R.id.img_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.img_right:

                break;

        }
    }

}
