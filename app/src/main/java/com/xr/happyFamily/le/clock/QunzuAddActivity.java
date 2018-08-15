package com.xr.happyFamily.le.clock;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.le.BtClock.bqOfColckActivity;
import com.xr.happyFamily.le.adapter.ClockAddQunzuAdapter;
import com.xr.happyFamily.le.bean.ClickFriendBean;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.view.QunzuTimepicker;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
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

public class QunzuAddActivity extends AppCompatActivity {


    @BindView(R.id.tv_lrsd_qx)
    TextView tvLrsdQx;
    @BindView(R.id.tv_lrsd_qd)
    TextView tvLrsdQd;
    @BindView(R.id.time_le1)
    QunzuTimepicker timeLe1;
    @BindView(R.id.time_le2)
    QunzuTimepicker timeLe2;

    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.sdclock_layout_mian)
    LinearLayout sdclockLayoutMian;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.rl_bjtime_bq)
    RelativeLayout rlBjtimeBq;
    @BindView(R.id.tv_music)
    TextView tvMusic;
    @BindView(R.id.rl_music)
    RelativeLayout rlMusic;
    private TimeDaoImpl timeDao;
    List<Time> times;
    Time time;
    int hour, minutes;
    SharedPreferences preferences;
    String userId;
    private AlarmManager am;
    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;
    RecyclerView.LayoutManager mLayoutManager;

    ClockAddQunzuAdapter qunzuAdapter;
    MyDialog dialog;
    Context mContext = QunzuAddActivity.this;
    private boolean isBound = false,isClockBound=false;
    ClockDaoImpl clockDao;

    public static boolean running = false;

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
        timeDao = new TimeDaoImpl(getApplicationContext());
        times = new ArrayList<>();

        clockDao=new ClockDaoImpl(this);
        preferences = this.getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        Intent service = new Intent(mContext, ClockService.class);
        isBound = bindService(service, clockConnection, Context.BIND_AUTO_CREATE);
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
        qunzuAdapter = new ClockAddQunzuAdapter(this, list_friend);
        mLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(qunzuAdapter);

           IntentFilter intentFilter = new IntentFilter("QunzuAddActivity");
        MessageReceiver receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);
    }


    String user="0";
    @Override
    protected void onStart() {
        super.onStart();
        running = true;
        list_friend.clear();
        dialog = MyDialog.showDialog(this);
        dialog.show();
        new getClockFriends().execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
        user=qunzuAdapter.getMember();
        Log.e("qqqqUUUU1111",user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(clockConnection);
        }
    }

    @OnClick({R.id.tv_lrsd_qx, R.id.tv_lrsd_qd, R.id.img_add, R.id.rl_bjtime_bq, R.id.rl_music})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_add:
                startActivity(new Intent(mContext, FriendFindActivity.class));
                break;
            case R.id.tv_lrsd_qx:
                finish();
                break;
            case R.id.rl_music:
                Intent intent3 = new Intent(this, MusicActivity.class);
                startActivityForResult(intent3, 111);
                break;
            case R.id.rl_bjtime_bq:
                Intent intent2 = new Intent(this, bqOfColckActivity.class);
                startActivityForResult(intent2, 101);
                break;
            case R.id.tv_lrsd_qd:

                hour = timeLe1.getValue();
                minutes = timeLe2.getValue();
                Map map = new HashMap();
                map.put("clockHour", hour);
                map.put("clockMinute", minutes);
                map.put("clockDay", "0");
                if ("请填写标签".equals(tvTag.getText().toString())) {
                    ToastUtil.showShortToast("请添加标签");
                    break;
                } else
                    map.put("flag", tvTag.getText().toString());
                map.put("music", tvMusic.getText().toString());
                map.put("switchs", 1);
                String member = qunzuAdapter.getMember();
                if ("0".equals(member)) {
                    ToastUtil.showShortToast("请选择添加成员");
                    break;
                } else

                    map.put("clockMember", userId + "," + member);
                Log.e("qqqqqqqMMMM", member);
                map.put("clockCreater", userId);
                map.put("clockType", 2);
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
                Log.e("qqqqUUUU2222",user);
                qunzuAdapter.notifyDataSetChanged();
                if(!"0".equals(user)){
                    qunzuAdapter.setUserInfoList(user);
                }

            }else if (!Utils.isEmpty(s) && "401".equals(s)) {
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
                    if (result.length() < 6) {
                        code=result;
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

            }else if (!Utils.isEmpty(s) && "401".equals(s)) {
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


//    MQService mqService;
//    private boolean bound = false;
//    private String deviceName;
//    ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            MQService.LocalBinder binder = (MQService.LocalBinder) service;
//            mqService = binder.getService();
//            bound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            bound = false;
//        }
//    };


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


    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            MyDialog.closeDialog(dialog);
            Toast.makeText(mContext, "添加闹钟成功", Toast.LENGTH_SHORT).show();
            if(clockService!=null)
                clockService.startClock();
            finish();
        }
    }
}
