package com.xr.happyFamily.le.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.le.ClockActivity;
import com.xr.happyFamily.le.adapter.ClockAddQunzuAdapter;
import com.xr.happyFamily.le.adapter.ClockQinglvAdapter;
import com.xr.happyFamily.le.adapter.ClockQunzuAdapter;
import com.xr.happyFamily.le.bean.MyClockBean;
import com.xr.happyFamily.le.clock.MsgActivity;
import com.xr.happyFamily.le.clock.QunzuAddActivity;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.UserInfo;
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
    private ClockDaoImpl clockBeanDao;
    private UserInfosDaoImpl userInfosDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_clock_qunzu, container, false);
        unbinder = ButterKnife.bind(this, view);
        timeBar = (TimeBar) view.findViewById(R.id.arcprogressBar);
        tcTime.setFormat12Hour(null);
        tcTime.setFormat24Hour("HH:mm");



        clockBeanDao=new ClockDaoImpl(getActivity().getApplicationContext());
        userInfosDao=new UserInfosDaoImpl(getActivity().getApplicationContext());



        return view;

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
                getActivity().startActivity(new Intent(getActivity(), QunzuAddActivity.class));
                break;

        }
    }


    List<ClockBean> clockBeanList;
    List<UserInfo> userInfoList;

    List<ClockBean> allClockList;
    List<UserInfo> allUserInfoList;
    private void upClock(){
        clockBeanList=new ArrayList<>();
        userInfoList=new ArrayList<>();
        allClockList=clockBeanDao.findAll();
        allUserInfoList=userInfosDao.findAll();
        Log.e("qqqqqqqqqxxxxx",allClockList.size()+"???");
        for(int i=0;i<allClockList.size();i++){
            if (allClockList.get(i).getClockType()==2) {
                clockBeanList.add(allClockList.get(i));
                for(int j=0;j<allUserInfoList.size();j++){
                    if (allUserInfoList.get(j).getClockId()==allClockList.get(i).getClockId())
                        userInfoList.add(allUserInfoList.get(j));
                }
            }
        }
        Log.e("qqqqqqqSSS",clockBeanList.size()+"????222222");
        int[][] time=new int[clockBeanList.size()][2];
        for(int i=0;i<clockBeanList.size();i++){
            time[i][0]=clockBeanList.get(i).getClockHour();
            time[i][1]=clockBeanList.get(i).getClockMinute();
        }
        timeBar.setTime(time,0);
    }
    @Override
    public void onStart() {
        super.onStart();
        upClock();
        qunzuAdapter = new ClockQunzuAdapter((ClockActivity) getActivity(), clockBeanList,userInfoList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(qunzuAdapter);
    }

    public void upData(){
        upClock();
    }
}
