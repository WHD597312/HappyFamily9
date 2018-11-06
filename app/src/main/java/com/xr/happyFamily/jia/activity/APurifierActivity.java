package com.xr.happyFamily.jia.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.view_custom.FengSuViewPopup;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.jia.view_custom.TimePickViewPopup;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.TenTwoUtil;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by win7 on 2018/5/22.
 */

public class APurifierActivity extends AppCompatActivity {


    Unbinder unbinder;


    MyApplication application;
    public static boolean running = false;
    MessageReceiver receiver;
    @BindView(R.id.relative)
    LinearLayout relative;
    @BindView(R.id.tv_offline)
    TextView tvOffline;
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.image_more)
    ImageView imageMore;
    @BindView(R.id.head)
    RelativeLayout head;
    @BindView(R.id.img_quan)
    ImageView imgQuan;
    @BindView(R.id.tv_pm1)
    TextView tvPm1;
    @BindView(R.id.tv_pm2)
    TextView tvPm2;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.tv_kongqi)
    TextView tvKongqi;
    @BindView(R.id.tv_shidu)
    TextView tvShidu;
    @BindView(R.id.tv_wendu)
    TextView tvWendu;
    @BindView(R.id.tv_jiaquan)
    TextView tvJiaquan;
    @BindView(R.id.img_kai)
    ImageView imgKai;
    @BindView(R.id.ll_kai)
    LinearLayout llKai;
    @BindView(R.id.img_zi)
    ImageView imgZi;
    @BindView(R.id.ll_zi)
    LinearLayout llZi;
    @BindView(R.id.img_shui)
    ImageView imgShui;
    @BindView(R.id.ll_shui)
    LinearLayout llShui;
    @BindView(R.id.img_feng)
    ImageView imgFeng;
    @BindView(R.id.ll_feng)
    LinearLayout llFeng;
    @BindView(R.id.img_time)
    ImageView imgTime;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.ll)
    RelativeLayout ll;
    private boolean isBound;
    private DeviceChild deviceChild;
    private DeviceChildDaoImpl deviceChildDao;
    long houseId;
    long deviceId;

    private TimePickViewPopup customViewPopipup;
    private FengSuViewPopup fengSuViewPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_atm);
        application = (MyApplication) getApplicationContext();
        if (application != null) {
            application = (MyApplication) getApplication();
            application.addActivity(this);
        }
        unbinder = ButterKnife.bind(this);
        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
        Intent intent = getIntent();
        deviceId = intent.getLongExtra("deviceId", 0);
        Log.e("qqqqID", deviceId + "?");
        deviceChild = deviceChildDao.findById(deviceId);
        Log.e("qqqqID", deviceChild.getOnline() + "?");
        houseId = intent.getLongExtra("houseId", 0);
        Intent service = new Intent(this, MQService.class);
        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
        IntentFilter intentFilter = new IntentFilter("APurifierActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);
        Animation circle_anim = AnimationUtils.loadAnimation(this, R.anim.anim_atm);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            imgQuan.startAnimation(circle_anim);  //开始动画
        }
    }


    int atmState = -1;
    boolean isOpen = false;
    int state = 0;
    int[] timeData = new int[2];

    @OnClick({R.id.image_back, R.id.image_more, R.id.ll_feng, R.id.ll_kai, R.id.ll_shui, R.id.ll_time, R.id.ll_zi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                Intent intent2 = new Intent();
                intent2.putExtra("houseId", houseId);
                setResult(6000, intent2);
                finish();
                break;
            case R.id.image_more:
                popupmenuWindow();
                break;
            case R.id.ll_kai:
                if (deviceChild.getDeviceState() == 1) {
                    deviceChild.setDeviceState(0);
                } else {
                    deviceChild.setDeviceState(1);
                }
                send(deviceChild);
                setMode(deviceChild);
                break;
            case R.id.ll_shui:
                if (deviceChild.getDeviceState() == 1) {
                    atmState = 1;
//                    imgShui.setImageResource(R.mipmap.ic_atm_shui1);
//                    imgZi.setImageResource(R.mipmap.ic_atm_zi);
                    deviceChild.setPurifierState("01");
                    send(deviceChild);
                    setMode(deviceChild);
                }
                break;
            case R.id.ll_zi:
                if (deviceChild.getDeviceState() == 1) {
                    atmState = 0;
//                    imgZi.setImageResource(R.mipmap.ic_atm_zi1);
//                    imgShui.setImageResource(R.mipmap.ic_atm_shui);
                    deviceChild.setPurifierState("00");
                    send(deviceChild);
                    setMode(deviceChild);
                }
                break;
            case R.id.ll_time:
                int state=4;
                if (timerSwitch == 0) {
                    if (timerMoudle == 1)
                        state = 2;
                    else if(timerMoudle==2)
                        state = 3;
                } else {
                    if (timerMoudle == 1)
                        state = 0;
                    else if(timerMoudle ==2)
                        state = 1;
                }
                customViewPopipup = new TimePickViewPopup(this, state, timerHour, timerMin);
                if (customViewPopipup.isShowing()) {
                    customViewPopipup.dismiss();
                } else {
                    backgroundAlpha(0.5f);
                    //添加pop窗口关闭事件
                    customViewPopipup.setOnDismissListener(new poponDismissListener());
                    //设置我们弹出的PopupWindow的位置，基于某个视图之下
                    customViewPopipup.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                }
                customViewPopipup.setOnPublishListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeData = customViewPopipup.getData();
                        customViewPopipup.dismiss();
                        int hour = timeData[1];
                        int minuter = timeData[2];


                        if (timeData[0] == 0) {
                            deviceChild.setTimerSwitch(1);
                            deviceChild.setTimerMoudle(1);
                        } else if (timeData[0] == 1) {
                            deviceChild.setTimerSwitch(1);
                            deviceChild.setTimerMoudle(2);
                        } else if (timeData[0] == 2) {
                            deviceChild.setTimerSwitch(0);
                        }

                        Log.e("qqqqqTime2222", hour + "," + minuter);
                        deviceChild.setTimerHour(hour);
                        deviceChild.setTimerMin(minuter);
                        send(deviceChild);
                        setMode(deviceChild);
                    }
                });
                break;
            case R.id.ll_feng:
                setWindPop();
                break;

        }
    }


    public void setWindPop() {
        if (deviceChild.getDeviceState() == 1) {
            Log.e("qqqqqFFFF", rateState);
            fengSuViewPopup = new FengSuViewPopup(this, rateState);
            if (fengSuViewPopup.isShowing()) {
                fengSuViewPopup.dismiss();
            } else {
                backgroundAlpha(0.5f);
                //添加pop窗口关闭事件
                fengSuViewPopup.setOnDismissListener(new poponDismissListener());
                //设置我们弹出的PopupWindow的位置，基于某个视图之下
                fengSuViewPopup.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            }
            fengSuViewPopup.setOnPublishListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deviceChild.setRateState(fengSuViewPopup.getData());
                    fengSuViewPopup.dismiss();
                    send(deviceChild);
                    setMode(deviceChild);
                }
            });
        }
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }
    }


    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress = intent.getStringExtra("macAddress");
            Log.i("macAddress", "-->" + macAddress);
            DeviceChild deviceChild2 = (DeviceChild) intent.getSerializableExtra("deviceChild");
            String noNet = intent.getStringExtra("noNet");
            if (!TextUtils.isEmpty(noNet)) {
                relative.setVisibility(View.GONE);
                tvOffline.setVisibility(View.VISIBLE);
            } else {
                if (!TextUtils.isEmpty(macAddress) && deviceChild != null && macAddress.equals(deviceChild.getMacAddress())) {
                    if (deviceChild2 == null) {
                        Toast.makeText(APurifierActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
                        long houseId = deviceChild.getHouseId();
                        Intent data = new Intent();
                        data.putExtra("houseId", houseId);
                        APurifierActivity.this.setResult(6000, data);
                        APurifierActivity.this.finish();
                    } else {
                        deviceChild = deviceChild2;
                        boolean online = deviceChild.getOnline();
                        if (online) {
                            relative.setVisibility(View.VISIBLE);
                            tvOffline.setVisibility(View.GONE);
                            setMode(deviceChild);
                        } else {
                            relative.setVisibility(View.GONE);
                            tvOffline.setVisibility(View.VISIBLE);
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

    int timerHour = 0, timerMin = 0, timerSwitch, timerMoudle;
    String rateState;

    private void setMode(DeviceChild deviceChild) {
        int socketState = deviceChild.getDeviceState();/**开关状态*/
        timerMoudle = deviceChild.getTimerMoudle();/**定时模式*/
        timerSwitch = deviceChild.getTimerSwitch();/**定时开关*/
        timerHour = deviceChild.getTimerHour();/**定时 时*/
        timerMin = deviceChild.getTimerMin();/**定时 分*/
        rateState = deviceChild.getRateState();
        String purifierState = deviceChild.getPurifierState();
        int sorsorPm = deviceChild.getSorsorPm();
        int sensorSimpleTemp = deviceChild.getSensorSimpleTemp();
        int sensorSimpleHum = deviceChild.getSensorSimpleHum();
        int sensorHcho = deviceChild.getSensorHcho();

        Log.e("qqqqqMsg33333", sensorSimpleTemp + ",,,," + sensorSimpleHum);
        if (socketState == 1) {/**当前状态开*/
            isOpen = true;
            imgKai.setImageResource(R.mipmap.ic_atm_kai1);
            imgFeng.setImageResource(R.mipmap.ic_atm_feng1);

            Log.e("qqqqqqState",purifierState);
            if ("00".equals(purifierState)) {
                imgZi.setImageResource(R.mipmap.ic_atm_zi1);
                imgShui.setImageResource(R.mipmap.ic_atm_shui);
            } else {
                imgZi.setImageResource(R.mipmap.ic_atm_zi);
                imgShui.setImageResource(R.mipmap.ic_atm_shui1);
            }

        } else if (socketState == 0) {/**插座当前状态关*/
            isOpen = false;
            imgKai.setImageResource(R.mipmap.ic_atm_kai);
            imgZi.setImageResource(R.mipmap.ic_atm_zi);
            imgShui.setImageResource(R.mipmap.ic_atm_shui);
            imgFeng.setImageResource(R.mipmap.ic_atm_feng);
        }
        if (timerSwitch == 1)
            imgTime.setImageResource(R.mipmap.ic_atm_time1);
        else
            imgTime.setImageResource(R.mipmap.ic_atm_time);
        if (sorsorPm > 99)
            tvPm1.setText(sorsorPm / 100 + "");
        else
            tvPm1.setVisibility(View.GONE);
        int pm2 = sorsorPm % 100;
        if (pm2 > 10)
            tvPm2.setText(pm2 + "");
        else
            tvPm2.setText("0" + pm2);
        if (sensorSimpleHum > 0)
            tvShidu.setText(sensorSimpleHum + "");
        else
            tvShidu.setText("--");
        if (sensorSimpleTemp > 0)
            tvWendu.setText(sensorSimpleTemp + "");
        else
            tvWendu.setText("--");
        if (!(sensorHcho < 0))
            tvJiaquan.setText(sensorHcho + "");
        else
            tvJiaquan.setText("--");

        if (fengSuViewPopup != null) {
            if (fengSuViewPopup.isShowing()) {
                fengSuViewPopup.setWind(rateState);
            }
        }
        if (customViewPopipup != null) {
            if (customViewPopipup.isShowing()) {
                int state=4;
                if (timerSwitch == 0) {
                    if (timerMoudle == 1)
                        state = 2;
                    else if(timerMoudle==2)
                        state = 3;
                } else {
                    if (timerMoudle == 1)
                        state = 0;
                    else if(timerMoudle ==2)
                        state = 1;
                }
                customViewPopipup.setData(state, timerHour, timerMin);
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
            int dataLength = 6;
            sum = sum + dataLength;
            int busMode = deviceChild.getBusModel();
            sum = sum + busMode;

            int curRunState2 = deviceChild.getCurRunState2();
            sum = sum + curRunState2;
            int curRunState3 = deviceChild.getCurRunState3();
            sum = sum + curRunState3;
            int timerMoudle = deviceChild.getTimerMoudle();
            sum = sum + timerMoudle;
            int timeHour = deviceChild.getTimerHour();
            sum = sum + timeHour;
            int timeMin = deviceChild.getTimerMin();
            sum = sum + timeMin;
            int warmerRunState;/**机器当前运行状态*/
            int socketState = deviceChild.getDeviceState();
            String rateState = deviceChild.getRateState();
            String atmState = deviceChild.getPurifierState();
            int timerSwitch = deviceChild.getTimerSwitch();
            int[] x = new int[8];
            x[0] = socketState;
            x[1] = Integer.parseInt(rateState.substring(0, 1));
            x[2] = Integer.parseInt(rateState.substring(1, 2));
            x[3] = Integer.parseInt(rateState.substring(2, 3));
            x[4] = Integer.parseInt(atmState.substring(0, 1));
            x[5] = Integer.parseInt(atmState.substring(1, 2));
            x[6] = timerSwitch;
            warmerRunState = TenTwoUtil.changeToTen(x);
            sum = sum + warmerRunState;
            int checkCode = sum % 256;
            jsonArray.put(0, headCode);/**头码*/
            jsonArray.put(1, typeHigh);/**类型 高位*/
            jsonArray.put(2, typeLow);/**类型 低位*/
            jsonArray.put(3, busMode);/**商业模式*/
            jsonArray.put(4, dataLength);/**数据长度*/
            jsonArray.put(5, warmerRunState);
            jsonArray.put(6, curRunState2);
            jsonArray.put(7, curRunState3);
            jsonArray.put(8, timerMoudle);
            jsonArray.put(9, timeHour);
            jsonArray.put(10, timeMin);
            jsonArray.put(11, checkCode);/**校验码*/
            jsonArray.put(12, 9);/**结束码*/
            jsonObject.put("APurifier", jsonArray);

            if (isBound) {
                String topicName = "p99/aPurifier1/" + deviceChild.getMacAddress() + "/set";
                Log.e("qqqT", topicName);
                String s = jsonObject.toString();
                boolean success = mqService.publish(topicName, 1, s);
                if (success) {
                    deviceChildDao.update(deviceChild);
                }
                Log.e("qqqqqSSS", success + "," + topicName);
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
            Intent intent2 = new Intent();
            intent2.putExtra("houseId", houseId);
            setResult(6000, intent2);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        running = true;
        deviceChild = deviceChildDao.findById(deviceId);
        if (deviceChild != null) {
            boolean online = deviceChild.getOnline();
            if (online) {
                relative.setVisibility(View.VISIBLE);
                tvOffline.setVisibility(View.GONE);
                setMode(deviceChild);
            } else {
                relative.setVisibility(View.GONE);
                tvOffline.setVisibility(View.VISIBLE);
            }
        } else {
            Utils.showToast(this, "该设备已重置");
            Intent intent2 = new Intent();
            intent2.putExtra("houseId", houseId);
            setResult(6000, intent2);
            finish();
        }
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
        if (popupWindow1 == null)
            popupWindow1 = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow1.setFocusable(true);
        popupWindow1.setOutsideTouchable(true);
        //添加弹出、弹入的动画
        popupWindow1.setAnimationStyle(R.style.Popupwindow);

//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popupWindow.setBackgroundDrawable(dw);
        popupWindow1.showAsDropDown(imageMore, 0, -20);
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
                        Intent intent = new Intent(APurifierActivity.this, ShareDeviceActivity.class);
                        long id = deviceChild.getId();
                        intent.putExtra("deviceId", id);
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
                    Utils.showToast(APurifierActivity.this, "设备名称不能为空");
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
                        Utils.showToast(APurifierActivity.this, "修改成功");
                        tvTitle.setText(deviceName);
                        break;
                    default:
                        Utils.showToast(APurifierActivity.this, "修改失败");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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

