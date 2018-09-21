package com.xr.happyFamily.le.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.MsgDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.le.ClockActivity;
import com.xr.happyFamily.le.adapter.ClockQunzuAdapter;
import com.xr.happyFamily.le.clock.MsgActivity;
import com.xr.happyFamily.le.clock.QunzuAddActivity;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.le.view.TimeBar;
import com.xr.happyFamily.together.util.mqtt.ClockService;
import com.xr.happyFamily.together.util.mqtt.MQService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by TYQ on 2017/9/7.
 */

public class QunZuFragment extends BaseFragment {

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
    public static boolean running = false;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_flag)
    TextView tvFlag;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.tv_add_qz)
    TextView tvAddQz;

    private TimeBar timeBar;
    private ClockQunzuAdapter qunzuAdapter;
    private ClockDaoImpl clockBeanDao;
    private UserInfosDaoImpl userInfosDao;
    private boolean isBound = false, isClockBound = false;
    MessageReceiver receiver;
    SharedPreferences preferences;
    String userId;
    Intent service, clockIntent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_clock_qunzu, container, false);
        unbinder = ButterKnife.bind(this, view);
        timeBar = (TimeBar) view.findViewById(R.id.arcprogressBar);
        tcTime.setFormat12Hour(null);
        tcTime.setFormat24Hour("HH:mm");

        clockBeanList = new ArrayList<>();
        userInfoList = new ArrayList<>();
        preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        service = new Intent(getActivity(), MQService.class);
        clockIntent = new Intent(getActivity(), ClockService.class);


        IntentFilter intentFilter = new IntentFilter("QunzuFragment");
        receiver = new MessageReceiver();
        getActivity().registerReceiver(receiver, intentFilter);


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        running = true;
        Log.e("qqqqqZZZZ", "111111");
        isBound = getActivity().bindService(service, connection, Context.BIND_AUTO_CREATE);
        isClockBound = getActivity().bindService(clockIntent, clockConnection, Context.BIND_AUTO_CREATE);
        upClock();


    }

    @Override
    public void onStop() {
        super.onStop();
        running = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (isBound) {
            getActivity().unbindService(connection);
        }
        if (isClockBound) {
            getActivity().unbindService(clockConnection);
        }
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }

    boolean isDel = false;

    @OnClick({R.id.back, R.id.img_right, R.id.img_add})
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

    private void upClock() {
        clockBeanDao = new ClockDaoImpl(getActivity().getApplicationContext());
        userInfosDao = new UserInfosDaoImpl(getActivity().getApplicationContext());
        clockBeanList.clear();
        userInfoList.clear();
        allClockList = clockBeanDao.findAll();
        allUserInfoList = userInfosDao.findAll();

        for (int i = 0; i < allClockList.size(); i++) {
            if (allClockList.get(i).getClockType() == 2) {
                clockBeanList.add(allClockList.get(i));
            }
        }


        int[][] time = new int[clockBeanList.size()][2];
        for (int i = 0; i < clockBeanList.size(); i++) {
            time[i][0] = clockBeanList.get(i).getClockHour();
            time[i][1] = clockBeanList.get(i).getClockMinute();
        }
        timeBar.setTime(time, 2);
        if (clockBeanList.size() > 0)
            tvFlag.setText(clockBeanList.get(0).getFlag());
        qunzuAdapter = new ClockQunzuAdapter((ClockActivity) getActivity(), clockBeanList, userId, mqService, clockService);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(qunzuAdapter);
        long msgTime = preferences.getLong("msgTime", 0);
        MsgDaoImpl msgDao = new MsgDaoImpl(getActivity());
        int msgNum = msgDao.findNumbByTime(msgTime);
        if (msgNum == 0) {
            tvNum.setVisibility(View.GONE);
        } else
            tvNum.setText(msgNum + "");

        if (clockBeanList.size() == 0)
            tvAddQz.setVisibility(View.VISIBLE);
        else
            tvAddQz.setVisibility(View.GONE);
    }


    public MQService mqService;
    private boolean bound = false;
    private String deviceName;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
            if (mqService != null) {
                qunzuAdapter.setMqService(mqService);
            }
            bound = true;
//            getData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };


    ClockService clockService;
    ServiceConnection clockConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ClockService.LocalBinder binder = (ClockService.LocalBinder) service;
            clockService = binder.getService();
            if (clockService != null)
                qunzuAdapter.setClockService(clockService);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("qqqqqZZZZ???", "11111");
            String msg = intent.getStringExtra("msg");
            upClock();
        }
    }
}
