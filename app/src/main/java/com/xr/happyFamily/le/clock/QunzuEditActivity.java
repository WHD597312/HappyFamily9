package com.xr.happyFamily.le.clock;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.le.BtClock.bqOfColckActivity;
import com.xr.happyFamily.le.adapter.ClockAddQunzuAdapter;
import com.xr.happyFamily.le.bean.ClickFriendBean;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.le.view.QunzuTimepicker;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.TimeUtils;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.ClockService;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QunzuEditActivity extends AppCompatActivity {


    @BindView(R.id.tv_lrsd_qx)
    TextView tvLrsdQx;
    @BindView(R.id.tv_lrsd_qd)
    TextView tvLrsdQd;
    @BindView(R.id.time_le1)
    QunzuTimepicker timeLe1;
    @BindView(R.id.time_le2)
    QunzuTimepicker timeLe2;
    @BindView(R.id.sdclock_layout_mian)
    LinearLayout sdclockLayoutMian;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.rl_bjtime_bq)
    RelativeLayout rlBjtimeBq;
    List<Time> times;
    Time time;
    int hour, minutes;
    SharedPreferences preferences;
    String userId;
    @BindView(R.id.tv_music)
    TextView tvMusic;
    private AlarmManager am;
    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;
    RecyclerView.LayoutManager mLayoutManager;

    ClockAddQunzuAdapter qunzuAdapter;
    MyDialog dialog;
    Context mContext = QunzuEditActivity.this;

    private ClockDaoImpl clockBeanDao;
    private UserInfosDaoImpl userInfosDao;

    private String uesr;
    private ClockBean clockBean;
    String member, clockId;
    private boolean isBound = false,isClockBound=false;

    String macAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_add_qunzu);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        clockBeanDao = new ClockDaoImpl(getApplicationContext());
        userInfosDao = new UserInfosDaoImpl(getApplicationContext());
        times = new ArrayList<>();

        preferences = this.getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        clockBean = (ClockBean) getIntent().getSerializableExtra("clock");

        Intent service = new Intent(mContext, ClockService.class);
        isClockBound = bindService(service, clockConnection, Context.BIND_AUTO_CREATE);

        uesr = getIntent().getStringExtra("uesr");
        Log.e("qqqqqUUUU", uesr);
        clockId = clockBean.getClockId() + "";
        int hour = clockBean.getClockHour();
//分钟
        int minute = clockBean.getClockMinute();
        timeLe1.setMaxValue(23);
        timeLe1.setMinValue(00);
        timeLe1.setValue(hour);
//        timepicker1.setBackgroundColor(Color.WHITE);
        timeLe1.setNumberPickerDividerColor(timeLe1);
        timeLe2.setMaxValue(59);
        timeLe2.setMinValue(00);
        timeLe2.setValue(minute);
//        timepicker2.setBackgroundColor(Color.WHITE);
        timeLe2.setNumberPickerDividerColor(timeLe2);
        tvTag.setText(clockBean.getFlag());
        tvMusic.setText(clockBean.getMusic());
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //获取通知管理器
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        qunzuAdapter = new ClockAddQunzuAdapter(this, list_friend);
        qunzuAdapter.setUserInfoList(uesr);
        mLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(qunzuAdapter);
        Intent service2 = new Intent(mContext, MQService.class);
        isBound = bindService(service2, connection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        list_friend.clear();
        dialog = MyDialog.showDialog(this);
        dialog.show();
        new getClockFriends().execute();
        qunzuAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
        }
        if (isClockBound)
            unbindService(clockConnection);
    }

    ClockBean editClock;
    @OnClick({R.id.tv_lrsd_qx, R.id.tv_lrsd_qd, R.id.rl_bjtime_bq,R.id.rl_music})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_lrsd_qx:
                finish();
                break;
            case R.id.rl_bjtime_bq:
                Intent intent2 = new Intent(this, bqOfColckActivity.class);
                startActivityForResult(intent2, 101);
                break;
            case R.id.rl_music:
                Intent intent3 = new Intent(this, MusicActivity.class);
                startActivityForResult(intent3, 111);
                break;
            case R.id.tv_lrsd_qd:

                member = qunzuAdapter.getMember();
                if ("0".equals(member)) {
                    ToastUtil.showShortToast("请添加闹钟成员");
                    break;
                } else {
                    int state = 2;
                    if (!uesr.equals(userId + "," + member)) {
                        dialog.show();
                        new changeClockInfo().execute();
                        state = 4;
                    }
                    hour = timeLe1.getValue();
                    minutes = timeLe2.getValue();
                    Map map = new HashMap();
                    map.put("state", state);
                    map.put("clockId", clockId);
                    map.put("clockHour", hour);
                    map.put("clockMinute", minutes);
                    map.put("clockDay", "0");
                    if ("请填写标签".equals(tvTag.getText().toString())) {
                        map.put("flag","群组闹钟");
                    }else
                        map.put("flag", tvTag.getText().toString());
                    map.put("music", tvMusic.getText().toString());
                    map.put("switchs", clockBean.getSwitchs());
                    List<ClockBean> findLish = clockBeanDao.findClockByClockId(Integer.parseInt(clockId));
                    editClock=findLish.get(0);
                    editClock.setClockHour(hour);
                    editClock.setClockMinute(minutes);
                    editClock.setFlag(tvTag.getText().toString());
                    editClock.setClockDay("0");
                    editClock.setMusic(tvMusic.getText().toString());
                    List<ClickFriendBean> userInfos = new ArrayList<>();
                    String image = preferences.getString("headImgUrl", "");
                    Log.e("qqqqIIImg",image);
                    String username = preferences.getString("username", "");
                    String phone = preferences.getString("phone", "");
                    String birthday = preferences.getString("birthday", "");
                    String str = TimeUtils.getTime(birthday);
                    Calendar c = Calendar.getInstance();//
                    int age = c.get(Calendar.YEAR) - Integer.parseInt(str.substring(0,4)) + 1;
                    boolean sex = preferences.getBoolean("sex", false);
                    ClickFriendBean myInfo = new ClickFriendBean();
                    if (Utils.isEmpty(image)) {
                        myInfo.setHeadImgUrl("null");
                    }else
                        myInfo.setHeadImgUrl(image);
                    myInfo.setMemSign(0);
                    myInfo.setAge(age);
                    myInfo.setPhone(phone);
                    myInfo.setSex(sex);
                    myInfo.setUserId(Integer.parseInt(userId));
                    myInfo.setUsername(username);
                    userInfos.add(myInfo);
                    String mums[]=qunzuAdapter.getMemSign().split(",");
                    for(int i=0;i<mums.length;i++){
                        Log.e("qqqqqMMMM",mums[i]);
                        ClickFriendBean userInfo = list_friend.get(Integer.parseInt(mums[i]));
                        if (Utils.isEmpty(userInfo.getHeadImgUrl())) {
                            userInfo.setHeadImgUrl("null");
                        }
                        userInfos.add(userInfo);
                    }
                    map.put("userInfos", userInfos);
                    map.put("clockCreater", userId);
                    map.put("createrName", username);
                    map.put("clockType", 2);
                    long timeStampSec = System.currentTimeMillis()/1000;
                    String timestamp = String.format("%010d", timeStampSec);
                    long createTime=Long.parseLong(timestamp);
                    map.put("createTime", createTime);
                    macAddress = JSON.toJSONString(map, true);
                    new addMqttAsync().execute(macAddress);
                    dialog.show();
                }
                break;
        }
    }


    List<ClickFriendBean> list_friend = new ArrayList<>();

    class getClockFriends extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {


            String url = "/happy/clock/getClockFriends";
            url = url + "?userId=" + userId;
            String result = HttpUtils.doGet(mContext, url);
            Log.e("qqqqqqqqRRR", userId + "?" + result);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");

                    JsonObject content = new JsonParser().parse(jsonObject.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("returnData");
                    Gson gson = new Gson();
                    for (JsonElement user : list) {
                        //通过反射 得到UserBean.class
                        ClickFriendBean userList = gson.fromJson(user, ClickFriendBean.class);
                        userList.setMemSign(0);
                        list_friend.add(userList);
                    }


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
                MyDialog.closeDialog(dialog);
                qunzuAdapter.notifyDataSetChanged();
            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
            }
        }
    }

    class changeClockInfo extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String url = "/happy/clock/changeClockMember";
            url = url + "?clockMember=" + userId + "," + member + "&clockCreater=" + userId + "&clockId=" + clockId;
            String result = HttpUtils.doGet(mContext, url);
            Log.e("qqqqqqqRRR1111", url);
            Log.e("qqqqqqqRRR", result);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
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
//                new getClocksByUserId().execute();
            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 666 && requestCode == 101) {
            String text = data.getStringExtra("text");
            Log.i("text", "onCreate: -->22222" + text);
            tvTag.setText(text);
        }
        if (resultCode == 111) {
            String[] str = {"阿里郎", "浪人琵琶", "学猫叫", "芙蓉雨", "七月上", "佛系少女", "离人愁", "不仅仅是喜欢", "纸短情长", "远走高飞"};
            int pos = 0;
            pos = data.getIntExtra("pos", 0);
            String text = str[pos];
            tvMusic.setText(text);
//            time.setRingName(text);
        }
    }


    MQService mqService;
    private boolean bound = false;
    private String deviceName;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    private class addMqttAsync extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... macs) {
            String macAddress = macs[0];
            boolean step2 = false;
            if (mqService != null) {
                String topicName = "p99/2_" + clockId + "/clockuniversal";
                step2 = mqService.publish(topicName, 1, macAddress);
                Log.e("qqqqqqSSSSS", macAddress);
            }
            return step2;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (s) {
                if(clockService!=null)
                    clockService.startClock();
                MyDialog.closeDialog(dialog);
                Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                clockBeanDao.update(editClock);
                finish();
            } else
                Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
        }
    }


    ClockService clockService;
    ServiceConnection clockConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ClockService.LocalBinder binder = (ClockService.LocalBinder) service;
            clockService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
