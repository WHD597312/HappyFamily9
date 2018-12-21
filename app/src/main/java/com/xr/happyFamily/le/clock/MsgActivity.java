package com.xr.happyFamily.le.clock;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.database.dao.daoimpl.DerailBeanDaoImpl;
import com.xr.database.dao.daoimpl.FriendDataDaoImpl;
import com.xr.database.dao.daoimpl.MsgDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.le.adapter.MsgClockAdapter;
import com.xr.happyFamily.le.adapter.MsgDerailAdapter;
import com.xr.happyFamily.le.adapter.MsgFriendAdapter;
import com.xr.happyFamily.le.pojo.DerailBean;
import com.xr.happyFamily.le.pojo.FriendData;
import com.xr.happyFamily.le.pojo.MsgData;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.mqtt.ClockService;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MsgActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.rv_msg)
    RecyclerView rvMsg;
    @BindView(R.id.rv_friend)
    RecyclerView rvFriend;
    MsgClockAdapter msgClockAdapter;
    MsgFriendAdapter msgFriendAdapter;
    MsgDerailAdapter msgDerailAdapter;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.tv_friend)
    TextView tvFriend;
    String ip = "http://47.98.131.11:8084";
    Drawable draw_msg_true, draw_msg_false, draw_friend_true, draw_friend_false, draw_bangding_true, draw_bangding_false;
    public static boolean running = false;
    public static boolean inRunning = false;
    MessageReceiver receiver;
    @BindView(R.id.view_msg)
    View viewMsg;
    @BindView(R.id.view_friend)
    View viewFriend;
    @BindView(R.id.tv_bangding)
    TextView tvBangding;
    @BindView(R.id.view_bangding)
    View viewBangding;
    @BindView(R.id.ll_bangding)
    RelativeLayout llBangding;
    @BindView(R.id.ll_friend)
    RelativeLayout llFriend;
    @BindView(R.id.rv_bangding)
    RecyclerView rvBangding;
    @BindView(R.id.ll_msg)
    RelativeLayout llMsg;
    private boolean isBound = false;
    SharedPreferences preferences;
    String userId, userName;
    List<FriendData> msgFriendList;
    List<MsgData> msgDataList;
    List<DerailBean> msgDerailList;
    FriendDataDaoImpl friendDataDao;
    MsgDaoImpl msgDao;
    DerailBeanDaoImpl derailBeanDao;

    Intent clockintent;
    ClockService clcokservice;
    private  boolean clockisBound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_msg);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        preferences = this.getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        userName = preferences.getString("username", "");
        userName = "大王";

        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        long timeStampSec = System.currentTimeMillis() / 1000;
        String timestamp = String.format("%010d", timeStampSec);
        long createTime = Long.parseLong(timestamp);
        editor.putLong("msgTime", createTime);
        editor.commit();

        titleText.setText("消息");

        friendDataDao = new FriendDataDaoImpl(getApplicationContext());
        msgFriendList = friendDataDao.findAll();
        msgDao = new MsgDaoImpl(getApplicationContext());
        msgDataList = msgDao.findMsgByTime();
        derailBeanDao=new DerailBeanDaoImpl(getApplicationContext());
        msgDerailList= derailBeanDao.findAll();

        msgClockAdapter = new MsgClockAdapter(this, msgDataList);
        msgFriendAdapter = new MsgFriendAdapter(this, msgFriendList);
        msgDerailAdapter=new MsgDerailAdapter(this,msgDerailList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvFriend.setLayoutManager(linearLayoutManager);
        rvFriend.setAdapter(msgFriendAdapter);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        rvMsg.setLayoutManager(linearLayoutManager2);
        rvMsg.setAdapter(msgClockAdapter);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        rvBangding.setLayoutManager(linearLayoutManager3);
        rvBangding.setAdapter(msgDerailAdapter);

        draw_msg_true = getResources().getDrawable(R.mipmap.ic_clock_msg_clock1);
        draw_msg_true.setBounds(0, 0, draw_msg_true.getMinimumWidth(), draw_msg_true.getMinimumHeight());
        draw_msg_false = getResources().getDrawable(R.mipmap.ic_clock_msg_clock);
        draw_msg_false.setBounds(0, 0, draw_msg_false.getMinimumWidth(), draw_msg_false.getMinimumHeight());
        draw_friend_true = getResources().getDrawable(R.mipmap.ic_clock_msg_friend1);
        draw_friend_true.setBounds(0, 0, draw_friend_true.getMinimumWidth(), draw_friend_true.getMinimumHeight());
        draw_friend_false = getResources().getDrawable(R.mipmap.ic_clock_msg_friend);
        draw_friend_false.setBounds(0, 0, draw_friend_false.getMinimumWidth(), draw_friend_false.getMinimumHeight());
        draw_bangding_true = getResources().getDrawable(R.mipmap.ic_clock_msg_bangding1);
        draw_bangding_true.setBounds(0, 0, draw_bangding_true.getMinimumWidth(), draw_bangding_true.getMinimumHeight());
        draw_bangding_false = getResources().getDrawable(R.mipmap.ic_clock_msg_bangding);
        draw_bangding_false.setBounds(0, 0, draw_bangding_false.getMinimumWidth(), draw_bangding_false.getMinimumHeight());


        Intent service = new Intent(MsgActivity.this, MQService.class);
//        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);

        IntentFilter intentFilter = new IntentFilter("Friend");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);

        clockintent = new Intent(MsgActivity.this, ClockService.class);
        clockisBound = bindService(clockintent, clockconnection, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection clockconnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ClockService.LocalBinder binder = (ClockService.LocalBinder) service;
            clcokservice = binder.getService();

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    public void starDerail(){
        clcokservice.getDerail();
    }


    public void setderailPos(int derailPo){
        new YouguiAsync().execute();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("derailPo", derailPo);
        editor.putString("derailId",derailId);
        editor.commit();

    }
    /*****
     *  有轨 查询用户身份
     * ****/
    String derailId;
    class YouguiAsync extends AsyncTask<Void,Void,Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code = 0 ;
            String url = ip+"/happy/derailed/getDerailStatus?adminId="+userId;
            String result = HttpUtils.getOkHpptRequest(url);
            Log.i("youguires", "doInBackground: -->"+result);
            try {
                if (!TextUtils.isEmpty(result)){
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("returnCode");
                    String derailPos = jsonObject.getString("returnData");
                    int derailPo = Integer.valueOf(derailPos.substring(0,1));
                    derailId = derailPos.substring(derailPos.indexOf("_") + 1);
                    if (derailPo==1){
                        clcokservice.getDerail();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            switch (integer){
                case 100:

                    break;
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        running = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
        inRunning = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (isBound) {
//            unbindService(connection);
//        }

        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (clockisBound){
            unbindService(clockconnection);
        }

    }


    @OnClick({R.id.back, R.id.ll_msg, R.id.ll_friend, R.id.ll_bangding})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back:
                finish();
                break;
            case R.id.ll_msg:
                if (rvMsg.getVisibility() != View.VISIBLE) {
                    initView();
                    tvMsg.setCompoundDrawables(draw_msg_true, null, null, null);
                    tvMsg.setTextColor(getResources().getColor(R.color.color_msg));
                    rvMsg.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ll_friend:
                if (rvFriend.getVisibility() != View.VISIBLE) {
                    initView();
                    tvFriend.setCompoundDrawables(draw_friend_true, null, null, null);
                    tvFriend.setTextColor(getResources().getColor(R.color.color_msg));
                    rvFriend.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ll_bangding:
                if (rvBangding.getVisibility() != View.VISIBLE) {
                    initView();
                    tvBangding.setCompoundDrawables(draw_bangding_true, null, null, null);
                    tvBangding.setTextColor(getResources().getColor(R.color.color_msg));
                    rvBangding.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    private void initView() {
        rvFriend.setVisibility(View.GONE);
        rvBangding.setVisibility(View.GONE);
        rvMsg.setVisibility(View.GONE);
        viewFriend.setVisibility(View.GONE);
        viewMsg.setVisibility(View.GONE);
        viewBangding.setVisibility(View.GONE);
        tvMsg.setTextColor(Color.parseColor("#9e9e9e"));
        tvMsg.setCompoundDrawables(draw_msg_false, null, null, null);
        tvFriend.setCompoundDrawables(draw_friend_false, null, null, null);
        tvFriend.setTextColor(Color.parseColor("#9e9e9e"));
        tvBangding.setCompoundDrawables(draw_bangding_false, null, null, null);
        tvBangding.setTextColor(Color.parseColor("#9e9e9e"));

    }


    MQService mqService;
//    ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            MQService.LocalBinder binder = (MQService.LocalBinder) service;
//            mqService = binder.getService();
//            String str = "+/acceptorId_" + userId;
//            new FriendAsync().execute(str);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//        }
//    };

//    private class FriendAsync extends AsyncTask<String, Void, Void> {
//        @Override
//        protected Void doInBackground(String... macs) {
//            String macAddress = macs[0];
//            Log.i("deviceChild3", "-->" + "yes");
//            String topicName2 = "p99/" + macAddress + "/friend";
//
//            if (mqService != null) {
////                boolean success = mqService.subscribe(topicName2, 1);
//            }
//            return null;
//        }
//    }


    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
            Log.e("qqqqqqqqqqqMSGNNN", msg);
            Gson gson = new Gson();
            FriendData user;
            if (msg.contains("senderRemark") && msg.contains("senderAge") && msg.contains("senderSex")) {
                user = gson.fromJson(msg, FriendData.class);
                msgFriendList.add(user);
                msgFriendAdapter.notifyDataSetChanged();
            } else if (msg.contains("createrName") && msg.contains("music") && msg.contains("userInfos")) {
                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(msg);
                    JsonObject content = new JsonParser().parse(msg).getAsJsonObject();
                    //添加闹钟
                    MsgData msgData = new MsgData();
                    msgData.setCreateTime(jsonObject.getLong("createTime"));
                    msgData.setUserName(jsonObject.getString("createrName"));
                    Log.e("qqqqqqSSSSaaa2222", jsonObject.getInt("state") + "????");
                    msgData.setState(jsonObject.getInt("state"));
                    msgDataList.add(0, msgData);
                    msgClockAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.contains("您的好友请求")) {
                String str[] = msg.split(",");
                MsgData msgData = new MsgData();
                int state;
                if (str[1].contains("同意"))
                    state = 5;
                else
                    state = 6;
                msgData.setUserName(str[0]);
                Log.e("qqqqqqSSSSaaa", state + "????");
                msgData.setState(state);
                msgData.setCreateTime(Long.parseLong(str[2]));
                msgDataList.add(0, msgData);
            }


        }
    }


    public void setMsgData() {

    }
}
