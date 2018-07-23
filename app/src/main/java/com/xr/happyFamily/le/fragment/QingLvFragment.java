package com.xr.happyFamily.le.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.jia.activity.AddDeviceActivity;
import com.xr.happyFamily.le.ClockActivity;
import com.xr.happyFamily.le.adapter.ClockQinglvAdapter;

import com.xr.happyFamily.le.adapter.ClockQunzuAdapter;
import com.xr.happyFamily.le.clock.MsgActivity;
import com.xr.happyFamily.le.clock.QinglvAddActivity;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.le.view.TimeBar;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by TYQ on 2017/9/7.
 */

public class QingLvFragment extends BaseFragment {

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

    private boolean isBound = false;
    MessageReceiver receiver;
    public static boolean running = false;

    private TimeBar timeBar;
    private ClockQinglvAdapter qinglvAdapter;
    private ClockDaoImpl clockBeanDao;
    private UserInfosDaoImpl userInfosDao;
    SharedPreferences preferences;
    String userId, clockData;
    String[] clocks;
    Intent service;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_clock_qinglv, container, false);
        unbinder = ButterKnife.bind(this, view);
        timeBar = (TimeBar) view.findViewById(R.id.arcprogressBar);
        tcTime.setFormat12Hour(null);
        tcTime.setFormat24Hour("HH:mm");

        clockBeanList = new ArrayList<>();
        userInfoList = new ArrayList<>();
        preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId", "");


        service = new Intent(getActivity(), MQService.class);

        IntentFilter intentFilter = new IntentFilter("QingLvFragment");
        receiver = new MessageReceiver();
        getActivity().registerReceiver(receiver, intentFilter);


//        for(int i=1081;i<1085;i++){
//            Map<String, Object> params = new HashMap<>();
//            params.put("id", i);
//            new deleteClock().execute(params);}

        getData();


        return view;

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
                Intent intent = new Intent(getActivity(), QinglvAddActivity.class);
                intent.putExtra("type", "qinglv");
                getActivity().startActivity(intent);

                break;

        }
    }

    List<ClockBean> clockBeanList;
    List<UserInfo> userInfoList;

    List<ClockBean>  allClockList;
    List<UserInfo> allUserInfoList;

    public void upClock() {
        getData();
        clockBeanDao = new ClockDaoImpl(getActivity().getApplicationContext());
        userInfosDao = new UserInfosDaoImpl(getActivity().getApplicationContext());
        clockBeanList.clear();
        userInfoList .clear();
        allClockList = clockBeanDao.findAll();
        allUserInfoList = userInfosDao.findAll();


        for (int i = 0; i < allClockList.size(); i++) {
            if (allClockList.get(i).getClockType() == 3) {
                clockBeanList.add(allClockList.get(i));
            }
        }


        int[][] time = new int[clockBeanList.size()][2];
        for (int i = 0; i < clockBeanList.size(); i++) {
            time[i][0] = clockBeanList.get(i).getClockHour();
            time[i][1] = clockBeanList.get(i).getClockMinute();
        }
        timeBar.setTime(time, 1);
        qinglvAdapter = new ClockQinglvAdapter((ClockActivity) getActivity(), clockBeanList, userId,mqService);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(qinglvAdapter);

        qinglvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        running = true;
        isBound = getActivity().bindService(service, connection, Context.BIND_AUTO_CREATE);
        upClock();

    }




    public  MQService mqService;
    private boolean bound = false;
    private String deviceName;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
            if(mqService!=null){
               qinglvAdapter.setMqService(mqService);
            }
            bound = true;
            getData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };


    private class getClockAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... macs) {
            String macAddress = macs[0];
            if (mqService != null) {
                String topicName = "p99/" + macAddress + "/clockuniversal";
                boolean success = mqService.subscribe(topicName, 1);

            }
            return null;
        }
    }


    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
            upClock();
        }
    }


    public void getData() {
        clockData = preferences.getString("clockData", "");
        clocks = clockData.split(",");
        for (int i = 0; i < clocks.length; i++) {
            new getClockAsync().execute(clocks[i]);
        }
    }



    class deleteClock extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "/happy/clock/deleteClock";
            url = url + "?clockCreater=1031&clockId=" + params.get("id");
            String result = HttpUtils.doGet(context, url);
//            Log.e("qqqqqqqRRR", result);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                Toast.makeText(context, "删除闹钟成功", Toast.LENGTH_SHORT).show();
            }
        }
    }
}