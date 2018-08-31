package com.xr.happyFamily.jia.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.xr.happyFamily.bao.view.DoubleWaveView;
import com.xr.happyFamily.bao.view.TouchScrollView;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.view_custom.FengSuViewPopup;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.jia.view_custom.HumSeekBar;
import com.xr.happyFamily.jia.view_custom.TimePickViewPopup;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.TenTwoUtil;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by win7 on 2018/5/22.
 */

public class DehumidifierActivity extends AppCompatActivity {


    Unbinder unbinder;
    MyApplication application;
    Animation circle_anim;
    boolean isAnim = false;
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.image_more)
    ImageView imageMore;
    @BindView(R.id.head)
    RelativeLayout head;
    @BindView(R.id.tv_now)
    TextView tvNow;
    @BindView(R.id.tv_set)
    TextView tvSet;
    @BindView(R.id.ll_wendu)
    LinearLayout llWendu;
    @BindView(R.id.doubleWaveView)
    DoubleWaveView doubleWaveView;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.tv_water)
    TextView tvWater;
    @BindView(R.id.image_kai)
    ImageView imageKai;
    @BindView(R.id.tv_switch)
    TextView tvSwitch;
    @BindView(R.id.ll_kai)
    LinearLayout llKai;
    @BindView(R.id.image_timer)
    ImageView imageTimer;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.image_hum)
    ImageView imageHum;
    @BindView(R.id.tv_hum)
    TextView tvHum;
    @BindView(R.id.ll_hum)
    LinearLayout llHum;
    @BindView(R.id.image_wind)
    ImageView imageWind;
    @BindView(R.id.tv_wind)
    TextView tvWind;
    @BindView(R.id.ll_fengsu)
    LinearLayout llFengsu;
    @BindView(R.id.img_shui)
    ImageView imgShui;
    @BindView(R.id.ll_shui)
    LinearLayout llShui;
    @BindView(R.id.img_shuang)
    ImageView imgShuang;
    @BindView(R.id.ll_shuang)
    LinearLayout llShuang;
    @BindView(R.id.img_fu)
    ImageView imgFu;
    @BindView(R.id.ll_fu)
    LinearLayout llFu;
    @BindView(R.id.img_ganyi)
    ImageView imgGanyi;
    @BindView(R.id.ll_ganyi)
    LinearLayout llGanyi;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.relative)
    LinearLayout relative;
    @BindView(R.id.scrollView)
    TouchScrollView scrollView;
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.img_more2)
    ImageView imgMore2;
    @BindView(R.id.ll0)
    RelativeLayout ll0;
    @BindView(R.id.tv_offline)
    TextView tvOffline;
    @BindView(R.id.rl_body)
    RelativeLayout rlBody;




    private TimePickViewPopup customViewPopipup;
    private FengSuViewPopup fengSuViewPopup;
    public static boolean running = false;
    MessageReceiver receiver;
    private boolean isBound;
    private DeviceChild deviceChild;
    private DeviceChildDaoImpl deviceChildDao;
    long houseId;
    long deviceId;


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_dehumidifier);
        application = (MyApplication) getApplicationContext();
        if (application != null) {
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
        IntentFilter intentFilter = new IntentFilter("DehumidifierActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);

        ((DoubleWaveView) findViewById(R.id.doubleWaveView)).setProHeight(30);


        circle_anim = AnimationUtils.loadAnimation(DehumidifierActivity.this, R.anim.anim_90);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
//                circle_anim.setFillAfter(true);
        circle_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnim = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnim = false;
                if (isMore) {
                    imgMore.setVisibility(View.GONE);
                    imgMore2.setVisibility(View.VISIBLE);
                } else {
                    imgMore2.setVisibility(View.GONE);
                    imgMore.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                //不能滑动
                return true;
                //可以滑动
                //return false;
            }
        });
        scrollView.requestDisallowInterceptTouchEvent(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        running = true;
        deviceChild = deviceChildDao.findById(deviceId);
        if (deviceChild == null) {
            Toast.makeText(DehumidifierActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
            Intent data = new Intent(DehumidifierActivity.this, MainActivity.class);
            data.putExtra("houseId", houseId);
            startActivity(data);
        } else {
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

    boolean isMore = false;
    boolean isOpen = false, isSleep = false, isShuang = false, isFu = false, isGanyi = false;
    int state = 0;
    int[] timeData = new int[2];

    @OnClick({R.id.image_back, R.id.image_more, R.id.img_more, R.id.img_more2, R.id.ll_kai, R.id.ll_time, R.id.ll_hum, R.id.ll_fengsu, R.id.ll_shui, R.id.ll_shuang, R.id.ll_fu, R.id.ll_ganyi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                Intent intent2=new Intent();
                intent2.putExtra("houseId",houseId);
                setResult(6000,intent2);
                finish();
                break;
            case R.id.image_more:
                popupmenuWindow();
                break;
            case R.id.img_more:
                isMore = true;
                imgMore.startAnimation(circle_anim);
                llMore.setVisibility(View.VISIBLE);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
                break;
            case R.id.img_more2:
                isMore = false;
                imgMore2.startAnimation(circle_anim);
                llMore.setVisibility(View.GONE);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_UP);
                    }
                });
                break;
            case R.id.ll_kai:
                Log.e("qqqqqOOO", isOpen + "?");
                if (isOpen) {
                    isOpen = false;
                    deviceChild.setDeviceState(0);
                    setMode(deviceChild);
                    send(deviceChild);
                } else {
                    isOpen = true;
                    deviceChild.setDeviceState(1);
                    setMode(deviceChild);
                    send(deviceChild);
                }
                break;
            case R.id.ll_time:
                int state;
                Log.e("qqqqqTime", timerSwitch + "," + timerMoudle);
                if (timerSwitch == 0)
                    state = 2;
                else {
                    if (timerMoudle == 1)
                        state = 0;
                    else
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
                        if (timeData[0] == 0) {
                            deviceChild.setTimerSwitch(1);
                            deviceChild.setTimerMoudle(1);
                        } else if (timeData[0] == 1) {
                            deviceChild.setTimerSwitch(1);
                            deviceChild.setTimerMoudle(2);
                        } else if (timeData[0] == 2) {
                            deviceChild.setTimerSwitch(0);
                        }
                        deviceChild.setTimerHour(timeData[1]);
                        deviceChild.setTimerMin(timeData[2]);
                        send(deviceChild);
                        setMode(deviceChild);
                    }
                });
                break;
            case R.id.ll_hum:
                showPopup();
                break;
            case R.id.ll_fengsu:
                if (isOpen) {
                    fengSuViewPopup = new FengSuViewPopup(this, windLevel);
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
                            deviceChild.setWindLevel(fengSuViewPopup.getData());
                            fengSuViewPopup.dismiss();
                            send(deviceChild);
                            setMode(deviceChild);
                        }
                    });
                }
                break;
            case R.id.ll_shui:
                if (isOpen) {
                    initState();
                    deviceChild.setDehumSleep(1);
                    setMode(deviceChild);
                    send(deviceChild);
                }
                break;
            case R.id.ll_shuang:
                if (isOpen) {
                    initState();
                    deviceChild.setDehumDefrost(1);
                    setMode(deviceChild);
                    send(deviceChild);
                }
                break;
            case R.id.ll_fu:
                if (isOpen) {

                    initState();
                    deviceChild.setDehumAnion(1);

                    setMode(deviceChild);
                    send(deviceChild);
                }
                break;
            case R.id.ll_ganyi:
                if (isOpen) {
                    initState();
                    deviceChild.setDehumDrying(1);
                    setMode(deviceChild);
                    send(deviceChild);
                }
                break;

        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
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
        popupWindow1.showAsDropDown(imgMore, 0, -20);
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
                        Intent intent = new Intent(DehumidifierActivity.this, ShareDeviceActivity.class);
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

    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }
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
                    Utils.showToast(DehumidifierActivity.this, "设备名称不能为空");
                } else {
                    new UpdateDeviceAsync().execute();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
    private String updateDeviceNameUrl= HttpUtils.ipAddress+"/family/device/changeDeviceName";
    class UpdateDeviceAsync extends AsyncTask<Void,Void,Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code=0;
            try {
                int deviceId=deviceChild.getDeviceId();
                String url=updateDeviceNameUrl+"?deviceName="+ URLEncoder.encode(deviceName,"utf-8")+"&deviceId="+deviceId;
                String result= HttpUtils.getOkHpptRequest(url);
                JSONObject jsonObject=new JSONObject(result);
                String returnCode=jsonObject.getString("returnCode");
                if ("100".equals(returnCode)){
                    code=100;
                    deviceChild.setName(deviceName);
                    deviceChildDao.update(deviceChild);
                }
                Log.i("result","-->"+result);
            }catch (Exception e){
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            try {
                switch (code){
                    case 100:
                        Utils.showToast(DehumidifierActivity.this, "修改成功");
                        tvTitle.setText(deviceName);
                        break;
                    default:
                        Utils.showToast(DehumidifierActivity.this, "修改失败");
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

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
                rlBody.setVisibility(View.GONE);
                tvOffline.setVisibility(View.VISIBLE);
            } else {
                if (!TextUtils.isEmpty(macAddress) && macAddress.equals(deviceChild.getMacAddress())) {
                    if (deviceChild2 == null) {
                        Toast.makeText(DehumidifierActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
                        long houseId = deviceChild.getHouseId();
                        Intent data = new Intent();
                        data.putExtra("houseId", houseId);
                        DehumidifierActivity.this.setResult(6000, data);
                        DehumidifierActivity.this.finish();
                    } else {
                        deviceChild = deviceChild2;
                        boolean online = deviceChild.getOnline();
                        if (online) {
                            rlBody.setVisibility(View.VISIBLE);
                            tvOffline.setVisibility(View.GONE);
                            setMode(deviceChild);
                        } else {
                            rlBody.setVisibility(View.GONE);
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

    int timerMoudle;
    /**
     * 定时模式
     */
    int timerSwitch;
    /**
     * 定时开关
     */
    int timerHour;
    /**
     * 定时 时
     */
    int timerMin;
    /**
     * 定时 分
     */
    String windLevel = null;//风速等级
    int waterLevel = -1;//水位量
    int faultCode;

    private void setMode(DeviceChild deviceChild) {

        int deviceState = deviceChild.getDeviceState();/**开关状态*/
        Log.e("qqqqqqMMMM", deviceState + "?");
        timerMoudle = deviceChild.getTimerMoudle();/**定时模式*/
        timerSwitch = deviceChild.getTimerSwitch();/**定时开关*/
        timerHour = deviceChild.getTimerHour();/**定时 时*/
        timerMin = deviceChild.getTimerMin();/**定时 分*/
        windLevel = deviceChild.getWindLevel();
        waterLevel = deviceChild.getWaterLevel();
        int dehumSetTemp = deviceChild.getDehumSetTemp();/**除湿机设定温度*/
        int dehumSetHum = deviceChild.getDehumSetHum();/**除湿机设定湿度*/
        int dehumInnerTemp = deviceChild.getDehumInnerTemp();//除湿机内盘管温度
        int dehumOuterTemp = deviceChild.getDehumOuterTemp();//除湿机外盘管温度
        int equipRatedPowerHigh = deviceChild.getEquipRatedPowerHigh();/**除湿机额定高功率参数*/
        int equipRatedPowerLow = deviceChild.getEquipRatedPowerLow();/**除湿机额定低功率参数*/
        int equipCurdPowerHigh = deviceChild.getEquipCurdPowerHigh();/**除湿机当前高功率参数*/
        int equipCurdPowerLow = deviceChild.getEquipCurdPowerLow();/**除湿机当前低功率参数*/
        faultCode = deviceChild.getFaultCode();/**除湿机故障代码*/
        int dehumSleep = deviceChild.getDehumSleep();//除湿机睡眠模式 0关闭 1开启
        int dehumAnion = deviceChild.getDehumAnion();//除湿机负离子模式 0关闭 1开启
        int dehumDrying = deviceChild.getDehumDrying();//除湿机干衣模式 0关闭 1开启
        int dehumDefrost = deviceChild.getDehumDefrost();//除湿机除霜模式 0关闭 1开启
        int sensorSimpleTemp = deviceChild.getSensorSimpleTemp();//温度采样数据
        int sensorSimpleHum = deviceChild.getSensorSimpleHum();//湿度采样数据
        if (deviceState == 1) {/**插座当前状态开*/
            isOpen = true;
            imageKai.setImageResource(R.mipmap.ic_csj_kai1);
            imageHum.setImageResource(R.mipmap.ic_csj_shi1);
            imageWind.setImageResource(R.mipmap.ic_csj_fengsu1);
            if (dehumSleep == 0)
                imgShui.setImageResource(R.mipmap.ic_csj_shui);
            else
                imgShui.setImageResource(R.mipmap.ic_csj_shui1);
            if (dehumAnion == 0)
                imgFu.setImageResource(R.mipmap.ic_csj_fulizi);
            else
                imgFu.setImageResource(R.mipmap.ic_csj_fulizi1);
            if (dehumDefrost == 0)
                imgShuang.setImageResource(R.mipmap.ic_csj_shuang);
            else
                imgShuang.setImageResource(R.mipmap.ic_csj_shuang1);
            if (dehumDrying == 0)
                imgGanyi.setImageResource(R.mipmap.ic_csj_ganyi);
            else
                imgGanyi.setImageResource(R.mipmap.ic_csj_ganyi1);
        } else if (deviceState == 0) {
            isOpen = false;
            imageKai.setImageResource(R.mipmap.ic_csj_kai);
            imageHum.setImageResource(R.mipmap.ic_csj_shi);
            imageWind.setImageResource(R.mipmap.ic_csj_fengsu);
            imgShui.setImageResource(R.mipmap.ic_csj_shui);
            imgShuang.setImageResource(R.mipmap.ic_csj_shuang);
            imgFu.setImageResource(R.mipmap.ic_csj_fulizi);
            imgGanyi.setImageResource(R.mipmap.ic_csj_ganyi);
        }
        if (timerSwitch == 1)
            imageTimer.setImageResource(R.mipmap.ic_csj_time1);
        else
            imageTimer.setImageResource(R.mipmap.ic_csj_time);
        tvNow.setText(sensorSimpleTemp + "");
        tvSet.setText(dehumSetHum + "");
        tvWater.setText(waterLevel + "");
        if (isMore)
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
    }

    private void send(DeviceChild deviceChild) {
        try {
            int sum = 0;
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            int warmerRunState = 0;
            int curRunState2;
            int curRunState3 = 0;
            int deviceState = deviceChild.getDeviceState();
            int timerSwitch = deviceChild.getTimerSwitch();
            String windLevel = deviceChild.getWindLevel();
            int dehumSleep = deviceChild.getDehumSleep();//除湿机睡眠模式 0关闭 1开启
            int dehumAnion = deviceChild.getDehumAnion();//除湿机负离子模式 0关闭 1开启
            int dehumDrying = deviceChild.getDehumDrying();//除湿机干衣模式 0关闭 1开启
            int dehumDefrost = deviceChild.getDehumDefrost();//除湿机除霜模式 0关闭 1开启


            int headCode = 144;
            sum = sum + headCode;
            int type = deviceChild.getType();
            int typeHigh = type / 256;
            sum = sum + typeHigh;
            int typeLow = type % 256;
            sum = sum + typeLow;
            int dataLength = 8;
            sum = sum + dataLength;
            int busMode = deviceChild.getBusModel();
            sum = sum + busMode;

            int[] x = new int[8];
            x[0] = deviceState;
            x[1] = timerSwitch;
            x[2] = Integer.parseInt(windLevel.substring(0, 1));
            x[3] = Integer.parseInt(windLevel.substring(1, 2));
            x[4] = Integer.parseInt(windLevel.substring(2, 3));
            x[5] = dehumSleep;
            warmerRunState = TenTwoUtil.changeToTen(x);
            sum = sum + warmerRunState;
            Log.e("qqqqqqXX", warmerRunState + "??" + x[0] + "," + x[1]);

            int[] x2 = new int[8];
            x2[0] = dehumAnion;
            x2[1] = dehumDrying;
            x2[2] = dehumDefrost;
            curRunState2 = TenTwoUtil.changeToTen(x2);
            sum = sum + curRunState2;
            sum = sum + curRunState3;
            int dehumSetTemp = deviceChild.getDehumSetTemp();/**除湿机设定温度*/
            sum = sum + dehumSetTemp;
            int dehumSetHum = deviceChild.getDehumSetHum();/**除湿机设定湿度*/
            sum = sum + dehumSetHum;
            int timerMoudle = deviceChild.getTimerMoudle();
            sum = sum + timerMoudle;
            int timeHour = deviceChild.getTimerHour();
            sum = sum + timeHour;
            int timeMin = deviceChild.getTimerMin();
            sum = sum + timeMin;

            int checkCode = sum % 256;

            jsonArray.put(0, headCode);/**头码*/
            jsonArray.put(1, typeHigh);/**类型 高位*/
            jsonArray.put(2, typeLow);/**类型 低位*/
            jsonArray.put(3, busMode);/**商业模式*/
            jsonArray.put(4, dataLength);/**数据长度*/
            jsonArray.put(5, warmerRunState);
            jsonArray.put(6, curRunState2);
            jsonArray.put(7, curRunState3);
            jsonArray.put(8, dehumSetTemp);
            jsonArray.put(9, dehumSetHum);
            jsonArray.put(10, timerMoudle);
            jsonArray.put(11, timeHour);
            jsonArray.put(12, timeMin);
            jsonArray.put(13, checkCode);/**校验码*/
            jsonArray.put(14, 9);/**结束码*/
            jsonObject.put("Dehumidifier", jsonArray);

            if (isBound) {
                String topicName = "p99/dehumidifier1/" + deviceChild.getMacAddress() + "/set";
                String s = jsonObject.toString();
                boolean success = mqService.publish(topicName, 1, s);
                if (success) {
                    deviceChildDao.update(deviceChild);
                }
                Log.e("qqqqqqSend", success + "," + topicName);
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
            Intent intent=new Intent();
            intent.putExtra("houseId",houseId);
            setResult(6000,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext = DehumidifierActivity.this;
    private HumSeekBar humSeekBar;
    private TextView tv_queding;

    private void showPopup() {

        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_dehumidifier_hum, null);
        humSeekBar = (HumSeekBar) contentViewSign.findViewById(R.id.humSeekBar);
        humSeekBar.setProgress(deviceChild.getDehumSetHum() - 30);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queding);
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceChild.setDehumSetHum(humSeekBar.getHum());
                mPopWindow.dismiss();
                setMode(deviceChild);
                send(deviceChild);
            }
        });
        mPopWindow = new PopupWindow(contentViewSign);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        mPopWindow.setFocusable(true);
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    private ImageView img_close;
    private TextView tv_error;

    private void showErrorPopup() {
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_aconf_error, null);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        tv_error = (TextView) contentViewSign.findViewById(R.id.tv_error);
        String str_err = "";
        int[] x = TenTwoUtil.changeToTwo(faultCode);
        if (x[7] == 1)
            str_err = str_err + "缺氟，请检查" + "\n";
        if (x[6] == 1)
            str_err = str_err + "湿敏传感器故障，请检查" + "\n";
        if (x[5] == 1)
            str_err = str_err + "室内温度传感器故障，请检查" + "\n";
        if (x[4] == 1)
            str_err = str_err + "室内盘管故障，请检查" + "\n";
        if (x[3] == 1)
            str_err = str_err + "室外盘管故障，请检查" + "\n";
        if (x[2] == 1)
            str_err = str_err + "水满故障，请检查" + "\n";
        if (x[1] == 1)
            str_err = str_err + "过热故障，请检查" + "\n";
        if (x[0] == 1)
            str_err = str_err + "设备倾斜，请检查" + "\n";
        tv_error.setText(str_err.substring(0, str_err.length() - 1));

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        mPopWindow = new PopupWindow(contentViewSign);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        mPopWindow.setFocusable(true);
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    public void initState() {
        deviceChild.setDehumSleep(0);
        deviceChild.setDehumDefrost(0);
        deviceChild.setDehumAnion(0);
        deviceChild.setDehumDrying(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        running=false;
    }
}

