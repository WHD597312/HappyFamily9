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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.activity.AddDeviceActivity;
import com.xr.happyFamily.le.BtClock.bqOfColckActivity;
import com.xr.happyFamily.le.adapter.ClockAddQinglvAdapter;
import com.xr.happyFamily.le.bean.ClickFriendBean;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.le.view.QinglvTimepicker;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;
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

public class QinglvAddActivity extends AppCompatActivity {


    @BindView(R.id.tv_lrsd_qx)
    TextView tvLrsdQx;
    @BindView(R.id.tv_lrsd_qd)
    TextView tvLrsdQd;
    @BindView(R.id.time_le1)
    QinglvTimepicker timeLe1;
    @BindView(R.id.time_le2)
    QinglvTimepicker timeLe2;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.sdclock_layout_mian)
    LinearLayout sdclockLayoutMian;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.rl_bjtime_bq)
    RelativeLayout rlBjtimeBq;
    private TimeDaoImpl timeDao;
    List<Time> times;
    Time time;
    int hour, minutes;
    SharedPreferences preferences;
    String userId;
    private AlarmManager am;
    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;
    ClockAddQinglvAdapter qinglvAdapter;
    String uesrId;
    MyDialog dialog;
    Context mContext = QinglvAddActivity.this;
    private ClockDaoImpl clockBeanDao;
    private UserInfosDaoImpl userInfosDao;
    private boolean isBound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_add_qinglv);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        timeDao = new TimeDaoImpl(getApplicationContext());
        clockBeanDao = new ClockDaoImpl(getApplicationContext());
        userInfosDao = new UserInfosDaoImpl(getApplicationContext());

        times = new ArrayList<>();

        preferences = this.getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId", "");

        Intent service = new Intent(mContext, MQService.class);
        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);

        times = timeDao.findByAllTime();
        Calendar calendar = Calendar.getInstance();
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
        int minute = calendar.get(Calendar.MINUTE);
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
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //获取通知管理器
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        dialog = MyDialog.showDialog(this);
        dialog.show();
        new getClockFriends().execute();
        qinglvAdapter = new ClockAddQinglvAdapter(this, list_friend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(qinglvAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
        }
    }

    @OnClick({R.id.tv_lrsd_qx, R.id.tv_lrsd_qd, R.id.img_add, R.id.rl_bjtime_bq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_add:
                startActivity(new Intent(QinglvAddActivity.this, FriendFindActivity.class));
                break;
            case R.id.tv_lrsd_qx:
                finish();
                break;
            case R.id.rl_bjtime_bq:
                Intent intent2 = new Intent(this, bqOfColckActivity.class);
                startActivityForResult(intent2, 666);
                break;
            case R.id.tv_lrsd_qd:
                hour = timeLe1.getValue();
                minutes = timeLe2.getValue();
//                Log.i("zzzzzzzzz", "onClick:--> " + hour + "...." + minutes);
//                if (time == null) {
//                    time = new Time();
//                }
//                time.setHour(hour);
//                time.setMinutes(minutes);
//                timeDao.insert(time);
//                Calendar c = Calendar.getInstance();//c：当前系统时间
//                c.set(Calendar.HOUR_OF_DAY, hour);//把小时设为你选择的小时
//                c.set(Calendar.MINUTE, minutes);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0x101, new Intent("com.zking.android29_alarm_notification.RING"), 0);//上下文 请求码  启动哪一个广播 标志位
//                am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);//RTC_WAKEUP:唤醒屏幕  getTimeInMillis():拿到这个时间点的毫秒值 pendingIntent:发送广播
//                am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 60 * 60 * 24 * 1000, pendingIntent);
//                setResult(600);

                Map map = new HashMap();
                map.put("clockHour", hour);
                map.put("clockMinute", minutes);
                map.put("clockDay", "0");
                if ("请填写标签".equals(tvTag.getText().toString())) {
                    Toast.makeText(mContext, "请添加标签", Toast.LENGTH_SHORT).show();
                    break;
                } else
                    map.put("flag", tvTag.getText().toString());
                map.put("music", "狼爱上羊");
                map.put("switchs", 1);
                String member = qinglvAdapter.getMember();
                if ("0".equals(member)) {
                    Toast.makeText(mContext, "请选择添加成员", Toast.LENGTH_SHORT).show();
                    break;
                } else

                    map.put("clockMember", userId + "," + member);
                Log.e("qqqqqqqMMMM", member);
                map.put("clockCreater", userId);
                map.put("clockType", 3);
                dialog = MyDialog.showDialog(this);
                dialog.show();
                new addClock().execute(map);
                break;
        }
    }


    List<ClickFriendBean> list_friend = new ArrayList<>();

    class getClockFriends extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {


            String url = "/happy/clock/getClockFriends";
            url = url + "?userId=" + userId;
            String result = HttpUtils.doGet(QinglvAddActivity.this, url);
            Log.e("qqqqqqqqRRR", userId + "?" + result);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
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
                qinglvAdapter.notifyDataSetChanged();

            }
        }
    }


    int clockId;
    String macAddress;

    class addClock extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "/happy/clock/addClock";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            Log.e("qqqqqqqRRR", result);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = new JSONObject(jsonObject.getString("returnData"));
                    clockId = returnData.getInt("clockId");
                    Log.e("qqqqqqqqIIII", clockId + "?");
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
                Toast.makeText(mContext,"添加闹钟成功",Toast.LENGTH_SHORT).show();
                String image = preferences.getString("image", "");
                String username = preferences.getString("username", "");
                String phone = preferences.getString("phone", "");
                boolean sex = preferences.getBoolean("sex", false);
                String birthday = preferences.getString("birthday", "");
                String str=birthday.substring(0,4);
                Calendar c = Calendar.getInstance();//
                int age= c.get(Calendar.YEAR)-Integer.parseInt(str)+1;

                List<ClickFriendBean> userInfos = new ArrayList<>();
                ClickFriendBean userInfo = list_friend.get(qinglvAdapter.getItem());
                ClickFriendBean myInfo = new ClickFriendBean();
                if (Utils.isEmpty(userInfo.getHeadImgUrl())) {
                    userInfo.setHeadImgUrl("null");
                }
                Map map = new HashMap();
                map.put("state", 1);
                map.put("clockId", clockId);
                map.put("clockHour", hour);
                map.put("clockMinute", minutes);
                map.put("clockDay", "0");
                map.put("flag", tvTag.getText().toString());
                map.put("music", "狼爱上羊");
                map.put("switchs", 1);
                String member = qinglvAdapter.getMember();

                if (Utils.isEmpty(image)) {
                    myInfo.setHeadImgUrl("null");
                }
                myInfo.setMemSign(0);
                myInfo.setAge(age);
                myInfo.setPhone(phone);
                myInfo.setSex(sex);
                myInfo.setUserId(Integer.parseInt(userId));
                myInfo.setUsername(username);
                userInfos.add(myInfo);
                userInfos.add(userInfo);

                map.put("userInfos", userInfos);
                map.put("clockCreater", userId);
                map.put("clockType", 3);
                macAddress = JSON.toJSONString(map, true);


                new addMqttAsync().execute(macAddress);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 666) {
            String text = data.getStringExtra("text");
            tvTag.setText(text);
        }
    }

    private class addMqttAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... macs) {
            String macAddress = macs[0];
            if (mqService != null) {
                String topicName = "p99/3_" + clockId + "/clockuniversal";
                boolean step2 = mqService.publish(topicName, 1, macAddress);
            }
            return null;
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


}
