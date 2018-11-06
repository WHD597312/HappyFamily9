package com.xr.happyFamily.jia.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.jia.view_custom.MySeekBar;
import com.xr.happyFamily.jia.view_custom.TimePickViewPopup;
import com.xr.happyFamily.jia.view_custom.Timepicker3;
import com.xr.happyFamily.jia.xnty.NoFastClickUtils;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.TenTwoUtil;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/***
 * 设定温度与功率暂时未定
 */
public class SocketActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    Unbinder unbinder;
    MyApplication application;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.relative)
    RelativeLayout relative;
    @BindView(R.id.tv_offline)
    TextView tv_offline;
    @BindView(R.id.relative4)
    RelativeLayout relative4;
    @BindView(R.id.image_more)
    ImageView image_more;
    @BindView(R.id.image_switch2)
    ImageView socket_switch;
    @BindView(R.id.image_timer)
    ImageView image_timer;
    /**
     * 定时任务
     */
    @BindView(R.id.tv_timer)
    TextView tv_timer;
    /**
     * 定时状态
     */
    @BindView(R.id.tv_close_socket)
    TextView tv_close_socket;
    /**
     * 定时开关插座
     */
    @BindView(R.id.tv_switch_state)
    TextView tv_switch_state;
    /**
     * 电源开关状态
     */
    private DeviceChild deviceChild;
    MessageReceiver receiver;
    private boolean isBound;
    public static boolean running = false;
    private String updateDeviceNameUrl = HttpUtils.ipAddress + "/family/device/changeDeviceName";
    private DeviceChildDaoImpl deviceChildDao;
    long houseId;
    long deviceId;

    private List<String> timers = new ArrayList<>();
    private List<String> hours = new ArrayList<>();
    private List<String> mins = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_socket);
        if (application == null) {
            application = (MyApplication) getApplication();
            application.addActivity(this);
        }
        unbinder = ButterKnife.bind(this);
        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
        Intent intent = getIntent();
        deviceId = intent.getLongExtra("deviceId", 0);
        deviceChild = deviceChildDao.findById(deviceId);
        houseId = intent.getLongExtra("houseId", 0);
        Intent service = new Intent(this, MQService.class);
        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
        IntentFilter intentFilter = new IntentFilter("SocketActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);
        timers.add("倒计时");
        timers.add("定时");
        timers.add("关闭");
        for (int i = 0; i < 24; i++) {
            hours.add(i + "");
        }
        for (int i = 0; i < 60; i++) {
            mins.add(i + "");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        running = true;
        deviceChild = deviceChildDao.findById(deviceId);
        if (deviceChild != null) {
            String name = deviceChild.getName();
            tv_title.setText("" + name);
            boolean online = deviceChild.getOnline();
            if (online) {
                relative.setVisibility(View.VISIBLE);
                tv_offline.setVisibility(View.GONE);
                setMode(deviceChild);
            } else {
                relative.setVisibility(View.GONE);
                tv_offline.setVisibility(View.VISIBLE);
            }
        } else {
            running = false;
            Intent intent = new Intent();
            intent.putExtra("houseId", houseId);
            setResult(6000, intent);
            finish();
        }
    }

    @OnClick({R.id.image_switch2, R.id.image_rate, R.id.image_back, R.id.image_timer, R.id.image_more, R.id.image_meter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                Intent intent = new Intent();
                intent.putExtra("houseId", houseId);
                setResult(6000, intent);
                finish();
                break;
            case R.id.image_timer:
//                popupTimerWindow();
                popupTimeWinow();
                break;
            case R.id.image_switch2:
                if (NoFastClickUtils.isFastClick()) {
                    int socketState = deviceChild.getSocketState();
                    if (socketState == 0) {
                        deviceChild.setSocketState(1);
                        deviceChild.setIsSocketTimerMode(0);
                    } else if (socketState == 1) {
                        deviceChild.setSocketState(0);
                        deviceChild.setIsSocketTimerMode(0);
                    }
                    setMode(deviceChild);
                    send(deviceChild);
                }
                break;
            case R.id.image_more:
                popupmenuWindow();
                break;
            case R.id.image_meter:
                Intent intent2 = new Intent(this, TempChatActivity.class);
                intent2.putExtra("deviceId", deviceId);
                startActivity(intent2);
                break;
            case R.id.image_rate:
                popUpWindow();
                break;
        }
    }

    Timepicker3 tv_timer_hour;
    Timepicker3 tv_timer_min;

    private void initTimer() {//设置定时时间
        tv_timer_hour.setMaxValue(24);
        tv_timer_hour.setMinValue(00);
        tv_timer_min.setMaxValue(59);
        tv_timer_min.setMinValue(00);
        //timepicker1.setBackgroundColor(Color.LTGRAY);
        tv_timer_hour.setNumberPickerDividerColor(tv_timer_hour);
        tv_timer_min.setNumberPickerDividerColor(tv_timer_min);
    }

    private PopupWindow popupWindow1;

    public void popupmenuWindow() {
        if (popupWindow1 != null && popupWindow1.isShowing()) {
            return;
        }

        View view = View.inflate(this, R.layout.popview_room_homemanerge, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        RelativeLayout rl_room_rename = (RelativeLayout) view.findViewById(R.id.rl_room_rename);
        RelativeLayout tv_timer = (RelativeLayout) view.findViewById(R.id.rl_room_del);
        TextView tv_rname_r1 = (TextView) view.findViewById(R.id.tv_rname_r1);
        TextView tv_del_r1 = (TextView) view.findViewById(R.id.tv_del_r1);
        ImageView iv_del_r1 = (ImageView) view.findViewById(R.id.iv_del_r1);
        tv_rname_r1.setText("修改名称");
        tv_del_r1.setText("分享设备");
        iv_del_r1.setImageResource(R.mipmap.pop_share);
        popupWindow1 = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow1.setFocusable(true);
        popupWindow1.setOutsideTouchable(true);
        //添加弹出、弹入的动画
        popupWindow1.setAnimationStyle(R.style.Popupwindow);

//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popupWindow.setBackgroundDrawable(dw);
        popupWindow1.showAsDropDown(image_more, 0, -20);
//        popupWindow.showAtLocation(tv_home_manager, Gravity.RIGHT, 0, 0);
        //添加按键事件监听

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_room_rename:
                        buildUpdateDeviceDialog();
                        popupWindow1.dismiss();
                        break;
                    case R.id.rl_room_del:
                        Intent intent = new Intent(SocketActivity.this, ShareDeviceActivity.class);
                        long id = deviceChild.getId();
                        intent.putExtra("deviceId", id);
                        startActivity(intent);
                        popupWindow1.dismiss();
                        break;
                }
            }
        };

        rl_room_rename.setOnClickListener(listener);
        tv_timer.setOnClickListener(listener);
    }

    private PopupWindow popupTimeWinow;
    LoopView loop_state, loop_hour, loop_min;
    int state, hour, min;

    public void popupTimeWinow() {
        if (popupTimeWinow != null && popupTimeWinow.isShowing()) {
            return;
        }

        View view = View.inflate(this, R.layout.popup_timepick, null);


        popupTimeWinow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupTimeWinow.setFocusable(true);
        popupTimeWinow.setOutsideTouchable(true);

        loop_state = (LoopView) view.findViewById(R.id.loop_state);/**模式*/
        loop_state.setItems(timers);
        int isSocketTimerMode = deviceChild.getIsSocketTimerMode();
        int socketTimer = deviceChild.getSocketTimer();
        int socketTimerHour = deviceChild.getSocketTimerHour();
        int socketTimerMin = deviceChild.getSocketTimerMin();
        if (isSocketTimerMode == 1) {
            if (socketTimer == 2) {
                loop_state.setInitPosition(0);
            } else if (socketTimer == 1) {
                loop_state.setInitPosition(1);
            } else
                loop_state.setInitPosition(2);
        }
        //设置初始位置
        loop_state.setCenterTextColor(0xff37d39e);
        //设置字体大小
        loop_state.setTextSize(18);
        //设置是否循环播放
        loop_state.setNotLoop();
        loop_state.setItemsVisibleCount(7);
        loop_state.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                loop_state.setCurrentPosition(index);
                state = index;
            }
        });

        loop_hour = (LoopView) view.findViewById(R.id.loop_hour);
        loop_hour.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                loop_hour.setCurrentPosition(index);
                hour = index;
            }
        });

        loop_hour.setItems(hours);
        loop_hour.setCenterTextColor(0xff37d39e);
        loop_hour.setTextSize(18);
        //设置是否循环播放
        loop_hour.setItemsVisibleCount(7);


        loop_min = (LoopView) view.findViewById(R.id.loop_min);
        loop_min.setItems(mins);
        loop_min.setCenterTextColor(0xff37d39e);
        loop_min.setTextSize(18);

        Calendar calendar = Calendar.getInstance();
        int hour2 = calendar.get(Calendar.HOUR_OF_DAY);
        int min2 = calendar.get(Calendar.MINUTE);
        //设置是否循环播放
        loop_min.setItemsVisibleCount(7);
        if (isSocketTimerMode == 1) {
            if (socketTimer == 1) {
                int a = socketTimerHour * 60 + socketTimerMin;

                if (a/60%24>=hour2){
                    socketTimerHour = a / 60 % 24 - hour2;
                }else {
                    socketTimerHour = a / 60 % 24;
                }

                int indexHour = hours.indexOf(socketTimerHour + "");
                loop_hour.setInitPosition(indexHour);
                socketTimerMin = a % 60;
                if (socketTimerMin>min2){
                    socketTimerMin = a % 60-min2;
                }
                int minHour = mins.indexOf(socketTimerMin + "");
                loop_min.setInitPosition(minHour);
                loop_state.setInitPosition(0);
                loop_min.setCurrentPosition(minHour);
                hour=indexHour;
                min=minHour;
                state=0;
            } else if (socketTimer == 2) {
                int indexHour = hours.indexOf(socketTimerHour + "");
                loop_hour.setInitPosition(indexHour);
                int minHour = mins.indexOf(socketTimerMin + "");
                loop_min.setInitPosition(minHour);
                loop_state.setInitPosition(1);
                hour=indexHour;
                min=minHour;
                state=1;
            }
        } else {
            int hourIndex=hours.indexOf(hour2+"");
            int minIndex=mins.indexOf(min2+"");
            loop_hour.setInitPosition(hourIndex);
            loop_min.setInitPosition(minIndex);
            loop_state.setInitPosition(2);
            hour=hourIndex;
            min=minIndex;
            state=2;
        }

        loop_min.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                loop_min.setCurrentPosition(index);
                min = index;
            }
        });


        TextView tv_quxiao = (TextView) view.findViewById(R.id.tv_quxiao);
        TextView tv_queding = (TextView) view.findViewById(R.id.tv_queding);

        //添加弹出、弹入的动画
        popupTimeWinow.setAnimationStyle(R.style.Popupwindow);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupTimeWinow.setBackgroundDrawable(dw);
        popupTimeWinow.showAtLocation(relative4, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);


        //添加按键事件监听


        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_queding:
                        if (state == 0) {
                            int socketHour = Integer.parseInt(hours.get(hour));
                            int socketMin = Integer.parseInt(mins.get(min));
                            deviceChild.setSocketTimerHour(socketHour);
                            deviceChild.setSocketTimerMin(socketMin);
                            deviceChild.setSocketTimer(1);
                            deviceChild.setIsSocketTimerMode(1);
                        } else if (state == 1) {
                            int socketTimerHour=Integer.parseInt(hours.get(hour));
                            int socketTimerMin=Integer.parseInt(mins.get(min));
                            deviceChild.setSocketTimerHour(socketTimerHour);
                            deviceChild.setSocketTimerMin(socketTimerMin);
                            deviceChild.setSocketTimer(2);
                            deviceChild.setIsSocketTimerMode(1);
                        } else if (state == 2) {
                            deviceChild.setSocketTimerHour(0);
                            deviceChild.setTimerMin(0);
                            deviceChild.setSocketTimer(0);
                            deviceChild.setIsSocketTimerMode(0);
                        }
                        setMode(deviceChild);
                        send(deviceChild);
                        popupTimeWinow.dismiss();
                        break;
                    case R.id.tv_quxiao:
                        popupTimeWinow.dismiss();
                        break;
                }
            }
        };
        tv_queding.setOnClickListener(listener);
        tv_quxiao.setOnClickListener(listener);
//
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha(1.0f);
//            }
//        });

    }

    public void popUpWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }

        View view = View.inflate(this, R.layout.pop_power, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;


        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        MySeekBar beautySeekBar1 = (MySeekBar) view.findViewById(R.id.beautySeekBar1);
        beautySeekBar1.setOnSeekBarChangeListener(this);
        Button btn_ensure = (Button) view.findViewById(R.id.btn_ensure);

        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(relative4, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //添加按键事件监听


        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_ensure:
                        send(deviceChild);
                        popupWindow.dismiss();
                        backgroundAlpha(1.0f);
                        break;
                }
            }
        };

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        btn_ensure.setOnClickListener(listener);
    }

    String deviceName;

    private void buildUpdateDeviceDialog() {
        final HomeDialog dialog = new HomeDialog(this);
        dialog.setOnNegativeClickListener(new HomeDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new HomeDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                deviceName = dialog.getName();
                if (TextUtils.isEmpty(deviceName)) {
                    Utils.showToast(SocketActivity.this, "设备名称不能为空");
                } else {
                    new UpdateDeviceAsync().execute();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.i("SeekBar", "-->" + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    class UpdateDeviceAsync extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code = 0;
            try {
                int deviceId = deviceChild.getDeviceId();
                String url = updateDeviceNameUrl + "?deviceName=" + URLEncoder.encode(deviceName, "utf-8") + "&deviceId=" + deviceId;
                String result = HttpUtils.getOkHpptRequest(url);
                JSONObject jsonObject = new JSONObject(result);
                String returnCode = jsonObject.getString("returnCode");
                if ("100".equals(returnCode)) {
                    code = 100;
                    deviceChild.setName(deviceName);
                    deviceChildDao.update(deviceChild);
                }
                Log.i("result", "-->" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            try {
                switch (code) {
                    case 100:
                        Utils.showToast(SocketActivity.this, "修改成功");
                        tv_title.setText(deviceName);
                        break;
                    default:
                        Utils.showToast(SocketActivity.this, "修改失败");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private PopupWindow popupWindow;
    TimePickViewPopup timePickViewPopup;

    public void popupTimerWindow() {
        if (timePickViewPopup != null && timePickViewPopup.isShowing()) {
            return;
        }

        int socketTimerHour = deviceChild.getSocketTimerHour();/**插座定时模式开的 时*/
        int socketTimerMin = deviceChild.getSocketTimerMin();/**插座定时模式开的 分*/
        int socketTimer = deviceChild.getSocketTimer();
        int isSocketTimerMode = deviceChild.getIsSocketTimerMode();
//        int socketState=deviceChild.getSocketState();
        int state = 0;

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        if (isSocketTimerMode == 1) {
            if (socketTimer == 1) {
                state = 0;
                socketTimerHour = socketTimerHour * 2 + hour;
                socketTimerMin = socketTimerMin + min;
            } else if (socketTimer == 2) {
                state = 1;
            }
        } else if (isSocketTimerMode == 0) {
            if (socketTimer == 1) {
                state = 0;
                socketTimerHour = socketTimerHour * 2 + hour;
                socketTimerMin = socketTimerMin + min;
            } else if (socketTimer == 2) {
                state = 3;
            }
        }
        timePickViewPopup = new TimePickViewPopup(SocketActivity.this, state, socketTimerHour, socketTimerMin);
//        timePickViewPopup.setData(state,socketTimerHour,socketTimerMin);
        timePickViewPopup.showAtLocation(relative4, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        TextView tv_ensure = timePickViewPopup.getPublishView();
        tv_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] timeData = timePickViewPopup.getData();
                if (timeData[0] == 0) {
                    deviceChild.setSocketTimer(1);
                    deviceChild.setSocketTimerHour(timeData[1]);
                    deviceChild.setSocketTimerMin(timeData[2]);
                    deviceChild.setIsSocketTimerMode(1);
                    setMode(deviceChild);
                    send(deviceChild);
                } else if (timeData[0] == 1) {
                    deviceChild.setSocketTimer(2);
                    deviceChild.setSocketTimerHour(timeData[1]);
                    deviceChild.setSocketTimerMin(timeData[2]);
                    deviceChild.setIsSocketTimerMode(1);
                    setMode(deviceChild);
                    send(deviceChild);
                } else if (timeData[0] == 2) {
                    deviceChild.setIsSocketTimerMode(0);
                    deviceChild.setSocketTimer(0);
                    setMode(deviceChild);
                    send(deviceChild);
                }
                timePickViewPopup.dismiss();
            }
        });
        //添加按键事件监听

//        View.OnClickListener listener = new View.OnClickListener() {
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.image_cancle:
//                        popupWindow.dismiss();
//                        backgroundAlpha(1f);
//                        break;
//                    case R.id.image_ensure:
//                        int sh= tv_timer_hour.getValue();
//                        int min=tv_timer_min.getValue();
//                        deviceChild.setSocketTimerOpenHour(sh);
//                        deviceChild.setSocketTimerOpenMin(min);
//                        setMode(deviceChild);
//                        send(deviceChild);
//                        popupWindow.dismiss();
//                        backgroundAlpha(1f);
//                        break;
//                }
//            }
//        };
//
//        image_cancle.setOnClickListener(listener);
//        image_ensure.setOnClickListener(listener);
    }

    //设置蒙版
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }

    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress = intent.getStringExtra("macAddress");
            Log.i("macAddress", "-->" + macAddress);
            DeviceChild deviceChild2 = (DeviceChild) intent.getSerializableExtra("deviceChild");
            String noNet = intent.getStringExtra("noNet");
            if (!TextUtils.isEmpty(noNet)) {
                if (popupTimeWinow != null && popupTimeWinow.isShowing()) {
                    popupTimeWinow.dismiss();
                }
                relative.setVisibility(View.GONE);
                tv_offline.setVisibility(View.VISIBLE);
            } else {
                if (!TextUtils.isEmpty(macAddress) && deviceChild != null && macAddress.equals(deviceChild.getMacAddress())) {
                    if (deviceChild2 == null) {
                        if (popupTimeWinow != null && popupTimeWinow.isShowing()) {
                            popupTimeWinow.dismiss();
                        }
                        Toast.makeText(SocketActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
                        long houseId = deviceChild.getHouseId();
                        Intent data = new Intent(SocketActivity.this, MainActivity.class);
                        data.putExtra("houseId", houseId);
                        startActivity(data);
                    } else {
                        deviceChild = deviceChild2;
                        boolean online = deviceChild.getOnline();
                        if (online) {
                            relative.setVisibility(View.VISIBLE);
                            tv_offline.setVisibility(View.GONE);
                            setMode(deviceChild);
                        } else {
                            if (popupTimeWinow != null && popupTimeWinow.isShowing()) {
                                popupTimeWinow.dismiss();
                            }
                            relative.setVisibility(View.GONE);
                            tv_offline.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }

    private MQService mqService;
    private boolean bound = false;
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

    private void setMode(DeviceChild deviceChild) {
        int socketState = deviceChild.getSocketState();/**插座开关状态*/
        int socketTimerHour = deviceChild.getSocketTimerHour();/**插座定时模式开的 时*/
        int socketTimerMin = deviceChild.getSocketTimerMin();/**插座定时模式开的 分*/
        int isSocketTimerMode = deviceChild.getIsSocketTimerMode();
        int socketTimer=deviceChild.getSocketTimer();
        if (socketState == 1) {/**插座当前状态开*/
            tv_switch_state.setText("插座电源已开启");
            socket_switch.setImageResource(R.mipmap.socket_switch);
            if (isSocketTimerMode == 1) {
                tv_close_socket.setVisibility(View.VISIBLE);
                if (socketTimer==1){
                    Calendar calendar = Calendar.getInstance();
                    int hour2 = calendar.get(Calendar.HOUR_OF_DAY);
                    int min2 = calendar.get(Calendar.MINUTE);
                    int a=hour2*60+min2+socketTimerHour*60+socketTimerMin;
                    socketTimerHour = a / 60 % 24;
                    socketTimerMin = a % 60;
                }
                tv_close_socket.setText(socketTimerHour + ":" + socketTimerMin + "关闭插座");
                tv_timer.setText(socketTimerHour + ":" + socketTimerMin);
            } else if (isSocketTimerMode == 0) {
                tv_timer.setText("定时");
                tv_close_socket.setVisibility(View.GONE);
            }
        } else if (socketState == 0) {/**插座当前状态关*/
            socket_switch.setImageResource(R.mipmap.socket_switch_close);
            tv_switch_state.setText("插座电源已关闭");
            if (isSocketTimerMode == 1) {
                tv_close_socket.setVisibility(View.VISIBLE);
                if (socketTimer==1){
                    Calendar calendar = Calendar.getInstance();
                    int hour2 = calendar.get(Calendar.HOUR_OF_DAY);
                    int min2 = calendar.get(Calendar.MINUTE);
                    int a=hour2*60+min2+socketTimerHour*60+socketTimerMin;
                    socketTimerHour = a / 60 % 24;
                    socketTimerMin = a % 60;
                }
                tv_close_socket.setText(socketTimerHour + ":" + socketTimerMin + "开启插座");
                tv_timer.setText(socketTimerHour + ":" + socketTimerMin);
            } else {
                tv_timer.setText("定时");
                tv_close_socket.setVisibility(View.GONE);
            }
        }
        if (isSocketTimerMode == 0) {
            image_timer.setImageResource(R.mipmap.socket_timer_close);
        } else if (isSocketTimerMode == 1) {
            image_timer.setImageResource(R.mipmap.socket_timer);
        }

        if (popupTimeWinow!=null && popupTimeWinow.isShowing()){
            Calendar calendar = Calendar.getInstance();
            int hour2 = calendar.get(Calendar.HOUR_OF_DAY);
            int min2 = calendar.get(Calendar.MINUTE);
            if (isSocketTimerMode == 1) {
                if (socketTimer == 1) {
                    int a = socketTimerHour * 60 + socketTimerMin;
                    if (a/60%24>=hour2){
                        socketTimerHour = a / 60 % 24 - hour2;
                    }else {
                        socketTimerHour = a / 60 % 24;
                    }
                    int indexHour = hours.indexOf(socketTimerHour + "");
                    loop_hour.setCurrentPosition(indexHour);
                    socketTimerMin = a % 60;
                    if (socketTimerMin>min2){
                        socketTimerMin = a % 60-min2;
                    }
                    int minHour = mins.indexOf(socketTimerMin + "");
                    loop_min.setCurrentPosition(minHour);
                    loop_state.setCurrentPosition(0);
                } else if (socketTimer == 2) {
                    int indexHour = hours.indexOf(socketTimerHour + "");
                    loop_hour.setCurrentPosition(indexHour);
                    int minHour = mins.indexOf(socketTimerMin + "");
                    loop_min.setCurrentPosition(minHour);
                    loop_state.setCurrentPosition(1);
                }
            } else {
                int hourIndex=hours.indexOf(hour2+"");
                int minIndex=mins.indexOf(min2+"");
                loop_hour.setCurrentPosition(hourIndex);
                loop_min.setCurrentPosition(minIndex);
                loop_state.setCurrentPosition(2);
            }
        }
    }

    private void send(DeviceChild deviceChild) {
        try {
            int sum = 0;
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            int headCode = 144;
            sum = sum + headCode;
            int type = deviceChild.getType();
            int typeHigh = type / 256;
            sum = sum + typeHigh;
            int typeLow = type % 256;
            sum = sum + typeLow;
            int busMode = deviceChild.getBusModel();
            sum = sum + busMode;
            int dataLength = 7;
            sum = sum + dataLength;
            int socketPower = deviceChild.getSocketPower();
            int socketPowerHigh = socketPower / 256;
            sum = sum + socketPowerHigh;
            int socketPowerLow = socketPower % 256;
            sum = sum + socketPowerLow;
            int socketTemp = deviceChild.getSocketTemp();/**温度*/
            sum = sum + 0;
            int socketState = deviceChild.getSocketState();
            int isSocketTimerMode = deviceChild.getIsSocketTimerMode();
            int[] x = new int[8];
            x[0] = socketState;
            x[1] = isSocketTimerMode;
            int dataContent = TenTwoUtil.changeToTen(x);
            sum = sum + dataContent;
            int socketTimer = deviceChild.getSocketTimer();
            sum = sum + socketTimer;
            int socketTimerHour = deviceChild.getSocketTimerHour();
            sum = sum + socketTimerHour;
            int socketTimerMin = deviceChild.getSocketTimerMin();
            sum = sum + socketTimerMin;

            int checkCode = sum % 256;
            jsonArray.put(0, headCode);/**头码*/
            jsonArray.put(1, typeHigh);/**类型 高位*/
            jsonArray.put(2, typeLow);/**类型 低位*/
            jsonArray.put(3, busMode);/**商业模式*/
            jsonArray.put(4, dataLength);/**数据长度*/
            jsonArray.put(5, socketPowerHigh);
            jsonArray.put(6, socketPowerLow);
            jsonArray.put(7, 0);
            jsonArray.put(8, dataContent);
            jsonArray.put(9, socketTimer);
            jsonArray.put(10, socketTimerHour);
            jsonArray.put(11, socketTimerMin);
            jsonArray.put(12, checkCode);/**校验码*/
            jsonArray.put(13, 9);/**结束码*/
            jsonObject.put("Socket", jsonArray);

            if (isBound) {
                String macAddress = deviceChild.getMacAddress();
                String topicName = "p99/socket1/" + macAddress + "/set";
                String s = jsonObject.toString();
                boolean success = mqService.publish(topicName, 1, s);
                if (success) {
                    deviceChildDao.update(deviceChild);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回键功能
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("houseId", houseId);
            setResult(6000, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        running = false;
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (isBound && connection != null) {
            unbindService(connection);
        }
    }
}
