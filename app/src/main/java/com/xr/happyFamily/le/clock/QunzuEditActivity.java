package com.xr.happyFamily.le.clock;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
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
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
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

    private List<UserInfo> myUserInfoList = new ArrayList<>();
    private ClockBean clockBean;

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
        myUserInfoList = (ArrayList<UserInfo>) getIntent().getSerializableExtra("uesr");

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
        qunzuAdapter.setUserInfoList(myUserInfoList);
        mLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(qunzuAdapter);
        dialog = MyDialog.showDialog(this);
        dialog.show();
        new getClockFriends().execute();
    }

    @OnClick({R.id.tv_lrsd_qx, R.id.tv_lrsd_qd, R.id.img_add, R.id.rl_bjtime_bq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_add:
                startActivity(new Intent(mContext, FriendFindActivity.class));
                break;
            case R.id.tv_lrsd_qx:
                finish();
                break;
            case R.id.rl_bjtime_bq:
                Intent intent2 = new Intent(this, bqOfColckActivity.class);
                startActivityForResult(intent2, 101);
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
                map.put("clockId", clockBean.getClockId());
                map.put("clockHour", hour);
                map.put("clockMinute", minutes);
                map.put("clockDay", "0");
                if ("请填写标签".equals(tvTag.getText().toString())) {
                    Toast.makeText(mContext, "请添加标签", Toast.LENGTH_SHORT).show();
                    break;
                } else
                    map.put("flag", tvTag.getText().toString());
                map.put("music", tvMusic.getText().toString());
                map.put("switchs", 1);
                String member = qunzuAdapter.getMember();
                if ("0".equals(member)) {
                    break;
                } else

                    map.put("clockMember", userId + "," + member);
                Log.e("qqqqqqqMMMM", member);
                map.put("clockCreater", userId);
                dialog.show();
                new changeClockInfo().execute(map);
                break;
        }
    }


//
//    private boolean mIsShowing = false;
//    private PopupWindow popupWindow;
//    ImageView image1, image2, image3, image4, image5, image6, image7, image8;
//    RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3, relativeLayout4, relativeLayout5,
//            relativeLayout6, relativeLayout7, relativeLayout8;
//    Button buttonqx, buttonqd;
//    List<String> weeks;
//    String week=" ";
//
//    private void initPopup() {
//        if (popupWindow != null && popupWindow.isShowing()) {
//            return;
//        }
//        weeks=new ArrayList<>();
//        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
//        View pop = View.inflate(this, R.layout.fragment_addday_poup, null);
//        image1 = (ImageView) pop.findViewById(R.id.iv_add_day1);
//        image2 = (ImageView) pop.findViewById(R.id.iv_add_day2);
//        image3 = (ImageView) pop.findViewById(R.id.iv_add_day3);
//        image4 = (ImageView) pop.findViewById(R.id.iv_add_day4);
//        image5 = (ImageView) pop.findViewById(R.id.iv_add_day5);
//        image6 = (ImageView) pop.findViewById(R.id.iv_add_day6);
//        image7 = (ImageView) pop.findViewById(R.id.iv_add_day7);
//        image8 = (ImageView) pop.findViewById(R.id.iv_add_no);
//        relativeLayout1 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r1);
//        relativeLayout2 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r2);
//        relativeLayout3 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r3);
//        relativeLayout4 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r4);
//        relativeLayout5 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r5);
//        relativeLayout6 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r6);
//        relativeLayout7 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r7);
//        relativeLayout8 = (RelativeLayout) pop.findViewById(R.id.rl_addday_r8);
//        buttonqd = (Button) pop.findViewById(R.id.bt_addday_qd);
//        buttonqx = (Button) pop.findViewById(R.id.bt_addday_qx);
//        image1.setTag("close");
//        image2.setTag("close");
//        image3.setTag("close");
//        image4.setTag("close");
//        image5.setTag("close");
//        image6.setTag("close");
//        image7.setTag("close");
//        image8.setTag("open");
//        image8.setImageResource(R.mipmap.lrclock_dh);
////        popupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        //点击空白处时，隐藏掉pop窗口
//        popupWindow.setFocusable(true);
//        popupWindow.setTouchable(true);
//        popupWindow.setOutsideTouchable(true);
////        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popupWindow.setBackgroundDrawable(dw);
//        popupWindow.setAnimationStyle(R.style.Popupwindow);
//        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
////        mIsShowing = false;
//        View.OnClickListener listener = new View.OnClickListener() {
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.bt_addday_qx:
//                        popupWindow.dismiss();
//                        weeks.clear();
//                        break;
//                    case R.id.bt_addday_qd:
//                        weeks.clear();
//                        if ("open".equals(image1.getTag())) {
//                            weeks.add(" 周一 ");
//                        }
//                        if ("open".equals(image2.getTag())) {
//                            weeks.add(" 周二 ");
//                        }
//                        if ("open".equals(image3.getTag())) {
//                            weeks.add(" 周三 ");
//                        }
//                        if ("open".equals(image4.getTag())) {
//                            weeks.add(" 周四 ");
//                        }
//                        if ("open".equals(image5.getTag())) {
//                            weeks.add(" 周五 ");
//                        }
//                        if ("open".equals(image6.getTag())) {
//                            weeks.add(" 周六 ");
//                        }
//                        if ("open".equals(image7.getTag())) {
//                            weeks.add(" 周日 ");
//                        }
//                        if ("open".equals(image8.getTag())) {
//                            weeks.add(" 不重复 ");
//                        }
//                        for (int i=0;i<weeks.size();i++){
//                            week += weeks.get(i);
//                        }
//                        tv_lesd_week.setText(week);
//                        popupWindow.dismiss();
//                        break;
//                    case R.id.rl_addday_r8:
//                        if ("close".equals(image8.getTag())) {
//                            image8.setImageResource(R.mipmap.lrclock_dh);
//                            image1.setImageResource(0);
//                            image2.setImageResource(0);
//                            image3.setImageResource(0);
//                            image4.setImageResource(0);
//                            image5.setImageResource(0);
//                            image6.setImageResource(0);
//                            image7.setImageResource(0);
//                            image8.setTag("open");
//                            image7.setTag("close");
//                            image6.setTag("close");
//                            image5.setTag("close");
//                            image4.setTag("close");
//                            image3.setTag("close");
//                            image2.setTag("close");
//                            image1.setTag("close");
//
//                        } else if ("open".equals(image8.getTag())) {
//                            image8.setImageResource(0);
//                            image8.setTag("close");
//                        }
//
//                        break;
//                    case R.id.rl_addday_r7:
//                        if ("close".equals(image7.getTag())) {
//                            image8.setImageResource(0);
//                            image7.setImageResource(R.mipmap.lrclock_dh);
//                            image7.setTag("open");
//                        } else if ("open".equals(image7.getTag())) {
//                            image7.setImageResource(0);
//                            image7.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r1:
//                        if ("close".equals(image1.getTag())) {
//                            image8.setImageResource(0);
//                            image1.setImageResource(R.mipmap.lrclock_dh);
//                            image1.setTag("open");
//                        } else if ("open".equals(image1.getTag())) {
//                            image1.setImageResource(0);
//                            image1.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r2:
//                        if ("close".equals(image2.getTag())) {
//                            image8.setImageResource(0);
//                            image2.setImageResource(R.mipmap.lrclock_dh);
//                            image2.setTag("open");
//                        } else if ("open".equals(image2.getTag())) {
//                            image2.setImageResource(0);
//                            image2.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r3:
//                        if ("close".equals(image3.getTag())) {
//                            image8.setImageResource(0);
//                            image3.setImageResource(R.mipmap.lrclock_dh);
//                            image3.setTag("open");
//                        } else if ("open".equals(image3.getTag())) {
//                            image3.setImageResource(0);
//                            image3.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r4:
//                        if ("close".equals(image4.getTag())) {
//                            image8.setImageResource(0);
//                            image4.setImageResource(R.mipmap.lrclock_dh);
//                            image4.setTag("open");
//                        } else if ("open".equals(image4.getTag())) {
//                            image4.setImageResource(0);
//                            image4.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r5:
//                        if ("close".equals(image5.getTag())) {
//                            image8.setImageResource(0);
//                            image5.setImageResource(R.mipmap.lrclock_dh);
//                            image5.setTag("open");
//                        } else if ("open".equals(image5.getTag())) {
//                            image5.setImageResource(0);
//                            image5.setTag("close");
//                        }
//                        break;
//                    case R.id.rl_addday_r6:
//                        if ("close".equals(image6.getTag())) {
//                            image8.setImageResource(0);
//                            image6.setImageResource(R.mipmap.lrclock_dh);
//                            image6.setTag("open");
//                        } else if ("open".equals(image6.getTag())) {
//                            image6.setImageResource(0);
//                            image6.setTag("close");
//                        }
//                        break;
//                }
//            }
//        };
//        relativeLayout1.setOnClickListener(listener);
//        relativeLayout2.setOnClickListener(listener);
//        relativeLayout3.setOnClickListener(listener);
//        relativeLayout4.setOnClickListener(listener);
//        relativeLayout5.setOnClickListener(listener);
//        relativeLayout6.setOnClickListener(listener);
//        relativeLayout7.setOnClickListener(listener);
//        relativeLayout8.setOnClickListener(listener);
//        buttonqd.setOnClickListener(listener);
//        buttonqx.setOnClickListener(listener);
//    }

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

    class changeClockInfo extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "/happy/clock/changeClockInfo";
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
                new getClocksByUserId().execute();
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


    class getClocksByUserId extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String url = "/happy/clock/getClocksByUserId";
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
                    String retrunData = jsonObject.getString("returnData");
                    JsonObject content = new JsonParser().parse(retrunData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("clockGroup");
                    Gson gson = new Gson();
                    clockBeanDao.deleteAll();
                    userInfosDao.deleteAll();
                    for (JsonElement user : list) {
                        ClockBean userList = gson.fromJson(user, ClockBean.class);
                        userList.setSumMinute(userList.getClockHour()*60+userList.getClockMinute());
                        clockBeanDao.insert(userList);
                        JsonObject userInfo = new JsonParser().parse(user.toString()).getAsJsonObject();
                        JsonArray userInfoList = userInfo.getAsJsonArray("userInfos");
                        for (JsonElement myUserInfo : userInfoList) {
                            UserInfo userInfo1 = gson.fromJson(myUserInfo, UserInfo.class);
                            userInfo1.setClockId(userList.getClockId());
                            userInfosDao.insert(userInfo1);
                        }
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
                Toast.makeText(mContext, "添加闹钟成功", Toast.LENGTH_SHORT).show();
                finish();
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
    }
}
