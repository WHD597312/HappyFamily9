package com.xr.happyFamily.jia.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.TimerTask;
import com.xr.happyFamily.jia.view_custom.ChangeDialog;
import com.xr.happyFamily.jia.view_custom.DialogLoad;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.jia.view_custom.WarmerProgressBar;
import com.xr.happyFamily.together.http.BaseWeakAsyncTask;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.http.WeakRefHandler;
import com.xr.happyFamily.together.util.TenTwoUtil;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.jessyan.autosize.internal.CustomAdapt;

public class WamerActivity extends AppCompatActivity implements CustomAdapt, View.OnTouchListener {

    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.progressBar)
    WarmerProgressBar progressBar;
    @BindView(R.id.tv_temp_set)
    TextView tv_temp_set;
    DeviceChild device;
    private String deviceMac;
    @BindView(R.id.heater_add)
    ImageView heater_add;
    @BindView(R.id.heater_decrease)
    ImageView heater_decrease;
    @BindView(R.id.tv_cur_temp)
    TextView tv_cur_temp;
    @BindView(R.id.tv_device_mode)
    TextView tv_device_mode;
    @BindView(R.id.img_mode)
    ImageView img_mode;
    @BindView(R.id.img_mode2)
    ImageView img_mode2;
    @BindView(R.id.img_mode3)
    ImageView img_mode3;

    @BindView(R.id.tv_mode4)
    TextView tv_mode4;
    @BindView(R.id.img_state)
    ImageView img_state;
    @BindView(R.id.image_more)
    ImageView image_more;
    @BindView(R.id.rl_body)
    RelativeLayout rl_body;
    @BindView(R.id.tv_offline)
    TextView tv_offline;
    public static boolean running = false;
    private DeviceChildDaoImpl deviceChildDao;
    private long houseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wamer);
        unbinder = ButterKnife.bind(this);
        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
        Intent intent = getIntent();
        device = (DeviceChild) intent.getSerializableExtra("device");
        houseId = intent.getLongExtra("houseId", 0);
        deviceMac = device.getMacAddress();
        deviceName = device.getName();
        if (!TextUtils.isEmpty(deviceName)) {
            tv_title.setText(deviceName + "");
        }
        setMode(device);
        Intent service = new Intent(this, MQService.class);
        bind = bindService(service, connection, Context.BIND_AUTO_CREATE);
        IntentFilter filter = new IntentFilter("WamerActivity");
        registerReceiver(receiver, filter);
        heater_add.setOnTouchListener(this);
        heater_decrease.setOnTouchListener(this);
        progressBar.setOnMoveListener(new WarmerProgressBar.OnMoveListener() {
            @Override
            public void setOnMoveListener(int value) {
                tv_temp_set.setText(value + "℃");
                device.setWaramerSetTemp(value);
                int result = mqService.sendData(device, 6);
                if (result == 1) {
                    countTimer.start();
                } else if (result == 2) {
                    ToastUtil.showShortToast("当前网络不可用");
                }
            }
        });
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (bind) {
            unbindService(connection);
        }
        unregisterReceiver(receiver);
        handler.removeCallbacksAndMessages(null);
    }

    private void setMode(DeviceChild device) {
        if (device.getOnline()) {
            rl_body.setVisibility(View.VISIBLE);
            tv_offline.setVisibility(View.GONE);
            int warmerCurTemp = device.getWarmerCurTemp();
            int mode = device.getMode();
            int deviceState = device.getDeviceState();
            int rateGrade = device.getRateGrade();
            int warmerSetTemp = device.getWaramerSetTemp();
            tv_cur_temp.setText("当前温度" + warmerCurTemp + "℃");
            if (mode == 32) {
                keepWarmer = 1;
                img_mode.setImageResource(R.mipmap.img_mode_2);
            } else {
                keepWarmer = 0;
                img_mode.setImageResource(R.mipmap.img_mode_1);
            }
            int[] x = TenTwoUtil.changeToTwo(deviceState);
            open = x[7];

            if (open == 1) {
                img_state.setImageResource(R.mipmap.heater_open);
                progressBar.setMode(0);
            } else if (open == 0) {
                img_state.setImageResource(R.mipmap.heater_close);
                progressBar.setMode(1);
            }
            tempCurProgress = warmerSetTemp;
            tv_temp_set.setText(warmerSetTemp + "℃");
            if (warmerSetTemp <= 10) {
                warmerSetTemp = 10;
            } else if (warmerSetTemp > 46) {
                warmerSetTemp = 45;
            }
            if (!onClick)
                progressBar.setProgress(warmerSetTemp);
            int warmerScreen = device.getWarmerScreen();
            if (warmerScreen == 2) {
                screen = x[6];
                if (x[6] == 1) {
                    img_mode2.setImageResource(R.mipmap.img_mode2_2);
                } else {
                    img_mode2.setImageResource(R.mipmap.img_mode2_1);
                }
            }
            timer = x[5];
            if (timer == 1) {
                img_mode3.setImageResource(R.mipmap.heater_timer);
            } else {
                img_mode3.setImageResource(R.mipmap.heater_timer2);
            }

            if (rateGrade >= 1 && rateGrade <= 6) {
                if (rateGrade < 6) {
                    tv_mode4.setText(rateGrade + "档");
                } else if (rateGrade == 6) {
                    tv_mode4.setText("自动档");
                    tv_device_mode.setText("节能模式");
                }
            }
            if (isPopRateShow()) {
                if (rateGrade < 6) {
                    rateWay = 1;
                    this.rateGrade = rateGrade;
                    seekbar.setEnabled(true);
                    seekbar.setValue(rateGrade);
                    btn_hand.setBackground(getResources().getDrawable(R.drawable.shape_heater_selected));
                    btn_energy.setBackgroundResource(0);
                    tv_rate_grade2.setText(rateGrade + "档");

                } else if (rateGrade == 6) {
                    rateWay = 2;
                    seekbar.setEnabled(false);
                    btn_energy.setBackground(getResources().getDrawable(R.drawable.shape_heater_selected));
                    btn_hand.setBackgroundResource(0);
                    tv_rate_grade2.setText("自动档");
                }
            }
            if (isPopTempisShow()) {
                int tempCheck = device.getTempCheck();
                if (tempCheck >= 1 && tempCheck <= 10) {
                    tempSeekBar.setValue(tempCheck);
                    tvTempCheck.setText(tempCheck + "℃");
                }
            }
        } else {
            rl_body.setVisibility(View.GONE);
            tv_offline.setVisibility(View.VISIBLE);
        }
    }

    int open = 0;//0为设备关机，1为设备开机
    int screen = 0;//0为息屏 1为亮屏
    int timer;//0为定时关，1为定时开
    int keepWarmer = 0;//保温1为开，0为关

    int resultCode;

    @OnClick({R.id.img_back, R.id.tv_timer, R.id.image_more, R.id.tv_temp_check, R.id.tv_reset, R.id.rl_mode, R.id.img_state, R.id.rl_mode2, R.id.rl_mode3, R.id.rl_mode4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.image_more:
                popupmenuWindow();
                break;
            case R.id.tv_timer:
                Intent intent = new Intent(this, WatmerTimerActivity.class);
                intent.putExtra("deviceMac", deviceMac);
                intent.putExtra("houseId", houseId);
                intent.putExtra("weekTimerTasks", (Serializable) weekTimerTasks);
                startActivity(intent);
                break;
            case R.id.tv_temp_check://温度校准
                if (isDialogShowing())
                    break;
                popTemp();
                break;
            case R.id.img_state:
                if (isDialogShowing())
                    break;
                int deviceState2 = device.getDeviceState();
                int x2[] = TenTwoUtil.changeToTwo(deviceState2);
                if (open == 0) {
                    x2[7] = 1;
                } else if (open == 1) {
                    x2[7] = 0;
                }
                deviceState2 = TenTwoUtil.changeToTen2(x2);
                device.setDeviceState(deviceState2);
                resultCode = mqService.sendData(device, 1);
                if (resultCode == 1) {
                    countTimer.start();
                } else if (resultCode == 2) {
                    ToastUtil.showShortToast("当前网络不可用");
                }
                break;
            case R.id.rl_mode:
                if (isDialogShowing())
                    break;
                if (keepWarmer == 0) {
                    device.setMode(32);
                } else if (keepWarmer == 1) {
                    device.setMode(0);
                }

                resultCode = mqService.sendData(device, 5);
                if (resultCode == 1) {
                    countTimer.start();
                } else if (resultCode == 2) {
                    ToastUtil.showShortToast("当前网络不可用");
                }
                break;
            case R.id.rl_mode2:
                int warmerScreen = device.getWarmerScreen();
                if (warmerScreen == 2) {
                    if (isDialogShowing())
                        break;
                    int deviceState5 = device.getDeviceState();
                    int x5[] = TenTwoUtil.changeToTwo(deviceState5);
                    if (screen == 0) {
                        x5[6] = 1;
                    } else if (screen == 1) {
                        x5[6] = 0;
                    }
                    deviceState5 = TenTwoUtil.changeToTen2(x5);
                    device.setDeviceState(deviceState5);
                    resultCode = mqService.sendData(device, 2);
                    if (resultCode == 1) {
                        countTimer.start();
                    } else if (resultCode == 2) {
                        ToastUtil.showShortToast("当前网络不可用");
                    }
                } else {
                    ToastUtil.showShortToast("该设备无屏幕");
                }
                break;
            case R.id.rl_mode3:
                if (isDialogShowing())
                    break;
                int deviceState6 = device.getDeviceState();
                int x6[] = TenTwoUtil.changeToTwo(deviceState6);
                if (timer == 0) {
                    x6[5] = 1;
                } else if (timer == 1) {
                    x6[5] = 0;
                }
                deviceState6 = TenTwoUtil.changeToTen2(x6);
                device.setDeviceState(deviceState6);
                resultCode = mqService.sendData(device, 3);
                if (resultCode == 1) {
                    countTimer.start();
                } else if (resultCode == 2) {
                    ToastUtil.showShortToast("当前网络不可用");
                }
                break;
            case R.id.tv_reset:
                changeDialog();
                break;
            case R.id.rl_mode4://功率调节
                if (isDialogShowing())
                    break;
                popRate();
                break;
        }
    }

    PopupWindow popupWindow1;

    public void popupmenuWindow() {
        if (popupWindow1 != null && popupWindow1.isShowing()) {
            return;
        }
        View view = View.inflate(this, R.layout.popview_room_homemanerge, null);
        RelativeLayout rl_room_rename = (RelativeLayout) view.findViewById(R.id.rl_room_rename);
        RelativeLayout tv_timer = (RelativeLayout) view.findViewById(R.id.rl_room_del);
        TextView tv_rname_r1 = (TextView) view.findViewById(R.id.tv_rname_r1);
        TextView tv_del_r1 = (TextView) view.findViewById(R.id.tv_del_r1);
        ImageView iv_del_r1 = (ImageView) view.findViewById(R.id.iv_del_r1);
        tv_rname_r1.setText("修改名称");
        tv_del_r1.setText("分享设备");
        iv_del_r1.setImageResource(R.mipmap.pop_share);
        if (popupWindow1 == null)
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
                        popupWindow1.dismiss();
                        Intent intent = new Intent(WamerActivity.this, ShareDeviceActivity.class);
                        long id = device.getId();
                        intent.putExtra("deviceId", id);
                        intent.putExtra("type", 9);
                        startActivityForResult(intent, 8000);
                        break;
                }
            }
        };

        rl_room_rename.setOnClickListener(listener);
        tv_timer.setOnClickListener(listener);
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
                    Utils.showToast(WamerActivity.this, "设备名称不能为空");
                } else {
                    new UpdateDeviceAsync().execute();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private String updateDeviceNameUrl = HttpUtils.ipAddress + "/family/device/changeDeviceName";

    class UpdateDeviceAsync extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code = 0;
            try {
                int deviceId = device.getDeviceId();
                String url = updateDeviceNameUrl + "?deviceName=" + URLEncoder.encode(deviceName, "utf-8") + "&deviceId=" + deviceId;
                String result = HttpUtils.getOkHpptRequest(url);
                JSONObject jsonObject = new JSONObject(result);
                String returnCode = jsonObject.getString("returnCode");
                if ("100".equals(returnCode)) {
                    code = 100;
                    device.setName(deviceName);
                    deviceChildDao.update(device);
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
                        Utils.showToast(WamerActivity.this, "修改成功");
                        tv_title.setText(deviceName);
                        break;
                    default:
                        Utils.showToast(WamerActivity.this, "修改失败");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    ChangeDialog dialog;

    private void changeDialog() {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new ChangeDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMode(1);

        dialog.setTitle("恢复出厂设置");
        dialog.setTips("是否恢复出厂设置?");
        backgroundAlpha(0.6f);
        dialog.setOnNegativeClickListener(new ChangeDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new ChangeDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                if (mqService != null) {
                    mqService.sendData(device, 9);
                    countTimer.start();
                }
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                backgroundAlpha(1.0f);
            }
        });
        dialog.show();
    }

    private boolean onClick = false;

    int tempCurProgress;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.heater_add:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onClick = true;

                    new Thread() {
                        @Override
                        public void run() {
                            Log.i("MotionEvent", "-->" + onClick);
                            while (onClick) {
                                tempCurProgress = progressBar.getProgress();

                                tempCurProgress++;

                                if (tempCurProgress <= 10) {
                                    tempCurProgress = 10;
                                } else if (tempCurProgress >= 45) {
                                    tempCurProgress = 45;
                                }
                                Log.i("MotionEvent", "-->" + tempCurProgress);
                                try {
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Message tempAdd = handler.obtainMessage();
                                tempAdd.what = tempCurProgress;
                                handler.sendMessage(tempAdd);

                            }
                        }
                    }.start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    onClick = false;
                    device.setWaramerSetTemp(tempCurProgress);
                    tv_temp_set.setText(tempCurProgress + "℃");
                    resultCode = mqService.sendData(device, 6);
                    if (resultCode == 1) {
                        countTimer.start();
                    } else if (resultCode == 2) {
                        ToastUtil.showShortToast("当前网络不可用");
                    }
                }
                break;
            case R.id.heater_decrease:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onClick = true;
                    new Thread() {
                        @Override
                        public void run() {
                            Log.i("MotionEvent", "-->" + onClick);
                            while (onClick) {
                                tempCurProgress--;
                                if (tempCurProgress <= 10) {
                                    tempCurProgress = 10;
                                } else if (tempCurProgress >= 45) {
                                    tempCurProgress = 45;
                                }
                                try {
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Message tempAdd = handler.obtainMessage();
                                tempAdd.what = tempCurProgress;
                                handler.sendMessage(tempAdd);

                            }
                        }
                    }.start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    onClick = false;
                    device.setWaramerSetTemp(tempCurProgress);
                    tv_temp_set.setText(tempCurProgress + "℃");
                    resultCode = mqService.sendData(device, 6);
                    if (resultCode == 1) {
                        countTimer.start();
                    } else if (resultCode == 2) {
                        ToastUtil.showShortToast("当前网络不可用");
                    }
                }
                break;
        }
        return true;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int temp = msg.what;
            Log.i("MotionEvent", "-->handler:" + temp);
            progressBar.setProgress(temp);
        }
    };


    CountTimer countTimer = new CountTimer(2000, 1000);

    class CountTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setLoadDialog();
            Log.e("CountDownTimer", "-->" + millisUntilFinished);
        }

        @Override
        public void onFinish() {
            if (dialogLoad != null && dialogLoad.isShowing()) {
                dialogLoad.dismiss();
            }
        }
    }

    private boolean bind;
    private MQService mqService;
    private List<List<TimerTask>> weekTimerTasks;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
            weekTimerTasks = mqService.getTimerTask();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String deviceMac2 = intent.getStringExtra("deviceMac");
            if (deviceMac2.equals(deviceMac)) {
                DeviceChild deviceChild = (DeviceChild) intent.getSerializableExtra("device");
                if (deviceChild != null) {
                    device = deviceChild;
                    setMode(device);
                } else {
                    Toast.makeText(WamerActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
                    long houseId = deviceChild.getHouseId();
                    Intent data = new Intent();
                    data.putExtra("houseId", houseId);
                    WamerActivity.this.setResult(6000, data);
                    finish();
                }

            }
        }
    };

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 667;
    }

    private boolean isDialogShowing() {
        if (dialogLoad != null && dialogLoad.isShowing()) {
            ToastUtil.showShortToast("请稍后...");
            return true;
        }
        return false;
    }

    DialogLoad dialogLoad;

    private void setLoadDialog() {
        if (dialogLoad != null && dialogLoad.isShowing()) {
            return;
        }

        dialogLoad = new DialogLoad(this);
        dialogLoad.setCanceledOnTouchOutside(false);
        dialogLoad.setLoad("正在加载,请稍后");
        dialogLoad.show();
    }

    /**
     * 判断温度校准popview是否正在显示
     *
     * @return
     */
    private boolean isPopTempisShow() {
        if (popTempWindow != null && popTempWindow.isShowing()) {
            return true;
        }
        return false;
    }

    private PopupWindow popTempWindow;

    private int tempGrade;

    private RangeSeekBar tempSeekBar;
    TextView tvTempCheck;

    private void popTemp() {
        if (popTempWindow != null && popTempWindow.isShowing()) {
            return;
        }
        View view = View.inflate(this, R.layout.pop_heater_temp, null);
        if (popTempWindow == null)
            popTempWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popTempWindow.setFocusable(true);
        popTempWindow.setOutsideTouchable(false);

//        popupWindow.showAsDropDown(tv_title, 0, 0);
        popTempWindow.showAtLocation(tv_title, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        backgroundAlpha(0.6f);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        Button btn_ensure = view.findViewById(R.id.btn_ensure);
        if (tempSeekBar == null) {
            tempSeekBar = view.findViewById(R.id.seekbar);
            tempSeekBar.setIndicatorTextDecimalFormat("0");
        }
        if (tvTempCheck == null) {
            tvTempCheck = view.findViewById(R.id.tv_temp_grade2);
        }
        int tempCheck = device.getTempCheck();
        if (tempCheck >= 1 && tempCheck <= 10) {
            tempSeekBar.setValue(tempCheck);
            tvTempCheck.setText(tempCheck + "℃");
        }
        tempSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                tempGrade = Math.round(leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                tvTempCheck.setText(tempGrade + "℃");
            }
        });
        popTempWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_cancel:
                        popTempWindow.dismiss();
                        break;
                    case R.id.btn_ensure:
                        popTempWindow.dismiss();
                        device.setTempCheck(tempGrade);
                        resultCode = mqService.sendData(device, 7);
                        if (resultCode == 1) {
                            countTimer.start();
                        } else if (resultCode == 2) {
                            ToastUtil.showShortToast("当前网络不可用");
                        }
                        break;
                }
            }
        };
        btn_cancel.setOnClickListener(onClickListener);
        btn_ensure.setOnClickListener(onClickListener);
    }

    private PopupWindow popRateWindow;

    private int rateGrade = 1;
    private int rateWay = 1;

    private boolean isPopRateShow() {
        if (popRateWindow != null && popRateWindow.isShowing()) {
            return true;
        }
        return false;
    }

    RangeSeekBar seekbar;
    TextView btn_hand;
    TextView btn_energy;
    TextView tv_rate_grade2;

    private void popRate() {
        if (popRateWindow != null && popRateWindow.isShowing()) {
            return;
        }
        View view = View.inflate(this, R.layout.pop_heater_rate, null);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        Button btn_ensure = view.findViewById(R.id.btn_ensure);
        if (btn_hand == null)
            btn_hand = view.findViewById(R.id.btn_hand);
        if (btn_energy == null)
            btn_energy = view.findViewById(R.id.btn_energy);
        if (seekbar == null) {
            seekbar = view.findViewById(R.id.seekbar);
            seekbar.setIndicatorTextDecimalFormat("0");
        }
        if (tv_rate_grade2 == null) {
            tv_rate_grade2 = view.findViewById(R.id.tv_rate_grade2);
        }
        if (rateWay == 2) {
            seekbar.setEnabled(false);
            btn_energy.setBackground(getResources().getDrawable(R.drawable.shape_heater_selected));
            btn_hand.setBackgroundResource(0);
            tv_rate_grade2.setText("自动档");
        } else {
            seekbar.setEnabled(true);
            btn_hand.setBackground(getResources().getDrawable(R.drawable.shape_heater_selected));
            btn_energy.setBackgroundResource(0);
            tv_rate_grade2.setText(rateGrade + "档");
            seekbar.setValue(rateGrade);
        }
        if (popRateWindow == null)
            popRateWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popRateWindow.setFocusable(false);
        popRateWindow.setOutsideTouchable(false);
//        popupWindow.showAsDropDown(tv_title, 0, 0);
        popRateWindow.showAtLocation(tv_title, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        backgroundAlpha(0.6f);
        seekbar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                rateGrade = Math.round(leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                tv_rate_grade2.setText(rateGrade + "档");
            }
        });
        popRateWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_cancel:
                        popRateWindow.dismiss();
                        break;
                    case R.id.btn_ensure:
                        popRateWindow.dismiss();
                        if (rateWay == 2) {
                            device.setRateGrade(6);
                        } else if (rateWay == 1) {
                            device.setRateGrade(rateGrade);
                        }
                        resultCode = mqService.sendData(device, 8);
                        if (resultCode == 1) {
                            countTimer.start();
                        } else if (resultCode == 2) {
                            ToastUtil.showShortToast("当前网络不可用");
                        }
                        break;
                    case R.id.btn_energy:
                        rateWay = 2;
                        btn_energy.setBackground(getResources().getDrawable(R.drawable.shape_heater_selected));
                        btn_hand.setBackgroundResource(0);
                        tv_rate_grade2.setText("自动档");
                        seekbar.setEnabled(false);
                        break;
                    case R.id.btn_hand:
                        rateWay = 1;
                        seekbar.setEnabled(true);
                        btn_hand.setBackground(getResources().getDrawable(R.drawable.shape_heater_selected));
                        btn_energy.setBackgroundResource(0);
                        tv_rate_grade2.setText(rateGrade + "档");
                        break;
                }
            }
        };
        btn_energy.setOnClickListener(onClickListener);
        btn_hand.setOnClickListener(onClickListener);
        btn_cancel.setOnClickListener(onClickListener);
        btn_ensure.setOnClickListener(onClickListener);
    }

    //设置蒙版
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }
}
