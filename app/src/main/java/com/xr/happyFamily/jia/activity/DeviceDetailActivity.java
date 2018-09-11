package com.xr.happyFamily.jia.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.SmartWheelInfo;
import com.xr.happyFamily.jia.view_custom.DeviceChildDialog;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.jia.view_custom.SemicircleBar;
import com.xr.happyFamily.jia.view_custom.SmartWheelBar;
import com.xr.happyFamily.jia.view_custom.Timepicker3;
import com.xr.happyFamily.jia.view_custom.UpdateDeviceDialog;
import com.xr.happyFamily.jia.xnty.NoFastClickUtils;
import com.xr.happyFamily.jia.xnty.Timepicker;
import com.xr.happyFamily.jia.xnty.Timepicker2;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.TenTwoUtil;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.MQService;
import com.xr.happyFamily.together.util.mqtt.VibratorUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DeviceDetailActivity extends AppCompatActivity {

    private List<SmartWheelInfo> infos = new ArrayList<>();
    /**
     * 旋转的数字
     */
    private String[] mStrs = new String[]{"5", "10", "15", "20", "25", "30", "35","40"};
    @BindView(R.id.image_switch) ImageView image_switch;/**开关状态*/
    @BindView(R.id.image_rate) ImageView image_rate;/**功率状态*/
    @BindView(R.id.tv_rate_state) TextView tv_rate_state;/**功率状态*/
    @BindView(R.id.image_lock) ImageView image_lock;/**屏幕锁定状态*/
    @BindView(R.id.image_screen) ImageView image_screen;/**屏幕开启状态*/
    @BindView(R.id.tv_set_temp) TextView tv_set_temp;/**设定温度*/

    @BindView(R.id.layout_body) RelativeLayout layout_body;/**屏幕中间布局*/
    @BindView(R.id.image_timer) ImageView image_timer;/**定时任务*/
    @BindView(R.id.tv_cur_temp) TextView tv_cur_temp;/**当前温度*/
    @BindView(R.id.tv_timer) TextView tv_timer;/**定时*/
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.semicBar)
    SmartWheelBar wheelBar;
    @BindView(R.id.relative4) RelativeLayout relative4;/**底部布局*/
    @BindView(R.id.image_more) ImageView image_more;/**修改设备名称 分享设备*/
    @BindView(R.id.relative) RelativeLayout relative;/**设备详情*/
    @BindView(R.id.tv_offline) TextView tv_offline;/**设备离线*/
    private List<String> list=new ArrayList<>();
    Unbinder unbinder;
    Timepicker3 tv_timer_hour;
    private DeviceChildDaoImpl deviceChildDao;
    MessageReceiver receiver;
    MQService mqService;
    private boolean isBound=false;
    public static boolean running=false;
    private DeviceChild deviceChild;
    private float curAngle;
    @BindView(R.id.tv_lock)
    TextView tv_lock;
    @BindView(R.id.tv_screen)
    TextView tv_screen;
    private long houseId;
    private MyApplication application;
    private String updateDeviceNameUrl= HttpUtils.ipAddress+"/family/device/changeDeviceName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_device_detail);
        unbinder=ButterKnife.bind(this);
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        Log.w("width","width"+width);

        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(width,width);
        params.leftMargin=100;
        params.rightMargin=100;
        wheelBar.setLayoutParams(params);
        getBitWheelInfos();
        wheelBar.setBitInfos(infos);
        deviceChildDao=new DeviceChildDaoImpl(getApplicationContext());
        Intent intent=getIntent();
        String deviceName=intent.getStringExtra("deviceName");
        tv_title.setText(deviceName);
        long deviceId=intent.getLongExtra("deviceId",0);
        deviceChild=deviceChildDao.findById(deviceId);
        houseId=intent.getLongExtra("houseId",0);
        List<DeviceChild> deviceChildren=deviceChildDao.findAllDevice();
        Log.i("deviceChildren","-->"+deviceChildren.size());

        Intent service = new Intent(this, MQService.class);
        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);

        IntentFilter intentFilter = new IntentFilter("DeviceDetailActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        running=true;
        if (deviceChild!=null){
            boolean online=deviceChild.getOnline();
            if (online){
                if (deviceChild.getWarmerFall()==1){
                    relative.setVisibility(View.GONE);
                    tv_offline.setVisibility(View.VISIBLE);
                    tv_offline.setText("设备倾倒");
                }else {
                    relative.setVisibility(View.VISIBLE);
                    tv_offline.setVisibility(View.GONE);
                    tv_offline.setText("离线");
                    setMode(deviceChild);
                }
            }else {
                relative.setVisibility(View.GONE);
                tv_offline.setVisibility(View.VISIBLE);
                tv_offline.setText("离线");
            }
        }else {
            Intent intent=new Intent();
            intent.putExtra("houseId",houseId);
            setResult(6000,intent);
            finish();
        }
        curAngle=wheelBar.getmStartAngle();
        if (curAngle>0){/**顺时钟转*/
            int temp2=0;
//                    float tempSet=(curAngle2/4.5f * 0.5f);
            if (curAngle % 4.5f != 0){
                int t = (int) (curAngle / 4.5f);
                curAngle = t * 4.5f;
            }
            float temp=45-curAngle/4.5f * 0.5f;
            Log.i("cur","-->"+temp);
            temp2=(int)temp;
            if (temp2>=42){
                temp2=42;
            }
            tv_set_temp.setText(temp2+"");
        }else if (curAngle<=0){/**逆时针转*/
            if (curAngle % 4.5f != 0){
                int t = (int) (curAngle / 4.5f);
                curAngle = t * 4.5f;
            }
            float tempSet= (-curAngle/4.5f) * 0.5f+5;
            int temp=(int) tempSet;
            Log.i("cur2","-->"+temp);
            if (temp>=42){
                temp=42;
            }
            tv_set_temp.setText(temp+"");
        }
        wheelBar.setOnWheelCheckListener(new SmartWheelBar.OnWheelCheckListener() {
            @Override
            public void onChanged(SmartWheelBar wheelBar, float curAngle) {
                Log.i("SmartWheelBar","-->"+curAngle);
                int warmerTempSet=0;
                if (curAngle>0){/**顺时钟转*/
                    int temp2=0;
//                    float tempSet=(curAngle2/4.5f * 0.5f);
                    if (curAngle % 4.5f != 0){
                        int t = (int) (curAngle / 4.5f);
                        curAngle = t * 4.5f;
                    }
                    float temp=45-curAngle/4.5f * 0.5f;
                    Log.i("cur","-->"+temp);
                    temp2=(int)temp;
                    if (temp2>=42){
                        temp2=42;
                    }
                    warmerTempSet=temp2;
                    tv_set_temp.setText(temp2+"");
                }else if (curAngle<=0){/**逆时针转*/
                    if (curAngle % 4.5f != 0){
                        int t = (int) (curAngle / 4.5f);
                        curAngle = t * 4.5f;
                    }
                    float tempSet= (-curAngle/4.5f) * 0.5f+5;
                    int temp=(int) tempSet;
                    Log.i("cur2","-->"+temp);
                    if (temp>=42){
                        temp=42;
                    }
                    warmerTempSet=temp;
                    tv_set_temp.setText(temp+"");
                }

                deviceChild.setWaramerSetTemp(warmerTempSet);
                send(deviceChild);
            }
        });
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
                        Intent intent=new Intent(DeviceDetailActivity.this,ShareDeviceActivity.class);
                        long id=deviceChild.getId();
                        intent.putExtra("deviceId",id);
                        startActivity(intent);
                        popupWindow1.dismiss();
                        break;
                }
            }
        };

        rl_room_rename.setOnClickListener(listener);
        tv_timer.setOnClickListener(listener);
    }

    int deviceState;
    @OnClick({R.id.image_more,R.id.image_back,R.id.image_switch,R.id.image_timer,R.id.image_rate,R.id.image_lock,R.id.image_screen})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_more:
                popupmenuWindow();
                break;
            case R.id.image_back:
                VibratorUtil.StopVibrate(DeviceDetailActivity.this);
                Intent intent=new Intent();
                intent.putExtra("houseId",houseId);
                setResult(6000,intent);
                finish();
                break;
            case R.id.image_switch:
                if (NoFastClickUtils.isFastClick()){
                    deviceState=deviceChild.getDeviceState();
                    if (deviceState==0){
                        deviceChild.setDeviceState(1);
                    }else if (deviceState==1){
                        deviceChild.setDeviceState(0);
                    }
                    setMode(deviceChild);
                    send(deviceChild);
                }

                break;
            case R.id.image_timer:
                if (NoFastClickUtils.isFastClick()){
                    deviceState=deviceChild.getDeviceState();
                    if (deviceState==0){
                        Toast.makeText(this,"设备已关机",Toast.LENGTH_SHORT).show();
                    }else if (deviceState==1){
                        popupTimerWindow();
                    }
                }

                break;
            case R.id.image_rate:
                if (NoFastClickUtils.isFastClick()){
                    deviceState=deviceChild.getDeviceState();
                    if (deviceState==0){
                        Toast.makeText(this,"设备已关机",Toast.LENGTH_SHORT).show();
                    }else if (deviceState==1){
                        popupRateView();
                    }
                }
                break;
            case R.id.image_lock:
                if (NoFastClickUtils.isFastClick()){
                    int lockState=deviceChild.getLockState();
                    deviceState=deviceChild.getDeviceState();
                    if (deviceState==0){
                        Toast.makeText(this,"设备已关机",Toast.LENGTH_SHORT).show();
                    }else if (deviceState==1){
                        if (lockState==1){
                            deviceChild.setLockState(0);
                        }else if (lockState==0){
                            deviceChild.setLockState(1);
                        }
                        setMode(deviceChild);
                        send(deviceChild);
                    }
                }

                break;
            case R.id.image_screen:
                if (NoFastClickUtils.isFastClick()){
                    int screenState=deviceChild.getScreenState();
                    deviceState=deviceChild.getDeviceState();
                    if (deviceState==0){
                        Toast.makeText(this,"设备已关机",Toast.LENGTH_SHORT).show();
                    }else if (deviceState==1) {
                        if (screenState==1){
                            deviceChild.setScreenState(0);
                        }else if (screenState==0){
                            deviceChild.setScreenState(1);
                        }
                        setMode(deviceChild);
                        send(deviceChild);
                    }
                }
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow!=null && popupWindow.isShowing()){
                backgroundAlpha(1f);
                return false;
            }
            if (popupWindow1!=null && popupWindow1.isShowing()){
                backgroundAlpha(1f);
                return false;
            }
            VibratorUtil.StopVibrate(DeviceDetailActivity.this);
            Intent intent=new Intent();
            intent.putExtra("houseId",houseId);
            setResult(6000,intent);
            finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    private void initTimer(){//设置定时时间
        tv_timer_hour.setMaxValue(24);
        tv_timer_hour.setMinValue(00);
        //timepicker1.setBackgroundColor(Color.LTGRAY);
        tv_timer_hour.setNumberPickerDividerColor(tv_timer_hour);
    }

    private PopupWindow popupWindow;
    public void popupTimerWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }

        View view = View.inflate(this, R.layout.popview_timetask, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        ImageView image_cancle= (ImageView) view.findViewById(R.id.image_cancle);
        TextView tv_timer= (TextView) view.findViewById(R.id.tv_timer);
        ImageView image_ensure= (ImageView) view.findViewById(R.id.image_ensure);
       tv_timer_hour= (Timepicker3) view.findViewById(R.id.tv_hour);


        initTimer();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        tv_timer_hour.setValue(hour);

        int hour2=tv_timer_hour.getValue();
        Log.i("hour2", "popupWindow: "+hour2);


        int min=0;
        Log.i("min", "popupWindow: "+min);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);

        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);
        backgroundAlpha(0.4f);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(relative4, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //添加按键事件监听

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.image_cancle:
                        popupWindow.dismiss();
                        backgroundAlpha(1f);
                        break;
                    case R.id.image_ensure:
                        int sh= tv_timer_hour.getValue();
                        deviceChild.setTimerHour(sh);
                        deviceChild.setTimerMin(0);
                        setMode(deviceChild);
                        send(deviceChild);
                        popupWindow.dismiss();
                        backgroundAlpha(1f);
                        break;
                }
            }
        };

        image_cancle.setOnClickListener(listener);
        image_ensure.setOnClickListener(listener);
    }







    //设置蒙版
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp =getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }




    ImageView image_low_rate;
    ImageView image_middle_rate;
    ImageView image_high_rate;
    public void popupRateView(){
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }

        View view = View.inflate(this, R.layout.popview_rate, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        ImageView image_cancle= (ImageView) view.findViewById(R.id.image_cancle);

        ImageView image_ensure= (ImageView) view.findViewById(R.id.image_ensure);
        image_low_rate= (ImageView) view.findViewById(R.id.image_low_rate);
        image_middle_rate= (ImageView) view.findViewById(R.id.image_middle_rate);
        image_high_rate= (ImageView) view.findViewById(R.id.image_high_rate);

        String rateState=deviceChild.getRateState();
        if ("01".equals(rateState)){
            image_low_rate.setImageResource(R.mipmap.rate_open);
        }else if ("10".equals(rateState)){
            image_middle_rate.setImageResource(R.mipmap.rate_open);
        }else if ("11".equals(rateState)){
            image_high_rate.setImageResource(R.mipmap.rate_open);
        }

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(relative4, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //添加按键事件监听

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.image_cancle:
                        popupWindow.dismiss();
                        break;
                    case R.id.image_ensure:
                        setMode(deviceChild);
                        send(deviceChild);
                        popupWindow.dismiss();
                        break;
                    case R.id.image_low_rate:
                        deviceChild.setRateState("01");
                        image_low_rate.setImageResource(R.mipmap.rate_open);
                        image_middle_rate.setImageResource(R.mipmap.rate_close);
                        image_high_rate.setImageResource(R.mipmap.rate_close);
                        break;
                    case R.id.image_middle_rate:
                        deviceChild.setRateState("10");
                        image_low_rate.setImageResource(R.mipmap.rate_close);
                        image_middle_rate.setImageResource(R.mipmap.rate_open);
                        image_high_rate.setImageResource(R.mipmap.rate_close);
                        break;
                    case R.id.image_high_rate:
                        deviceChild.setRateState("11");
                        image_low_rate.setImageResource(R.mipmap.rate_close);
                        image_middle_rate.setImageResource(R.mipmap.rate_close);
                        image_high_rate.setImageResource(R.mipmap.rate_open);
                        break;
                }
            }
        };

        image_cancle.setOnClickListener(listener);
        image_ensure.setOnClickListener(listener);
        image_low_rate.setOnClickListener(listener);
        image_middle_rate.setOnClickListener(listener);
        image_high_rate.setOnClickListener(listener);
    }

    class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress=intent.getStringExtra("macAddress");
            Log.i("macAddress","-->"+macAddress);
            DeviceChild deviceChild2= (DeviceChild) intent.getSerializableExtra("deviceChild");
            String noNet=intent.getStringExtra("noNet");
            if (!TextUtils.isEmpty(noNet)){
                relative.setVisibility(View.GONE);
                tv_offline.setVisibility(View.VISIBLE);
            }else {
                if (!TextUtils.isEmpty(macAddress) && macAddress.equals(deviceChild.getMacAddress())){
                    if (deviceChild2==null){
                        Toast.makeText(DeviceDetailActivity.this,"该设备已重置",Toast.LENGTH_SHORT).show();
                        long houseId=deviceChild.getHouseId();
                        Intent data=new Intent();
                        data.putExtra("houseId",houseId);
                        DeviceDetailActivity.this.setResult(6000,data);
                        DeviceDetailActivity.this.finish();
                    }else {
                        deviceChild=deviceChild2;
                        boolean online=deviceChild.getOnline();
                        if (online){
                            if(deviceChild.getWarmerFall()==1){
                                VibratorUtil.Vibrate(DeviceDetailActivity.this, new long[]{1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000},false);   //震动10s  //震动10s
                                relative.setVisibility(View.GONE);
                                tv_offline.setVisibility(View.VISIBLE);
                                tv_offline.setText("设备倾倒");
                            }else {
                                VibratorUtil.StopVibrate(DeviceDetailActivity.this);
                                relative.setVisibility(View.VISIBLE);
                                tv_offline.setVisibility(View.GONE);
                                setMode(deviceChild);
                            }
                        }else {
                            relative.setVisibility(View.GONE);
                            tv_offline.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }
    private void setMode(DeviceChild deviceChild){

        int deviceState=deviceChild.getDeviceState();/**设备状态 0表示关机，1表示开*/
        int warmerCurTemp=deviceChild.getWarmerCurTemp();/**当前温度*/
        int waramerSetTemp=deviceChild.getWaramerSetTemp();/**设定温度*/
        String rateState=deviceChild.getRateState();/**功率状态*/

        int timerHour=deviceChild.getTimerHour();/**定时功能 时*/
        int timerMin=deviceChild.getTimerMin();/**定时功能 分*/
        int lockState=deviceChild.getLockState();/**屏幕锁定*/
        int screenState=deviceChild.getScreenState();

        /**开关机状态*/
        if (deviceState==0){
            image_switch.setImageResource(R.mipmap.image_close);
            image_rate.setImageResource(R.mipmap.rate_power);
            Message msg=handler.obtainMessage();
            msg.arg1=0;
            msg.what=0;
            handler.sendMessage(msg);
        }else if (deviceState==1){
            image_switch.setImageResource(R.mipmap.image_open);
            image_rate.setImageResource(R.mipmap.rate_open);
            Message msg=handler.obtainMessage();
            msg.arg1=0;
            msg.what=1;
            handler.sendMessage(msg);
        }
        if (timerHour==0){
            Log.i("timer","-->"+"timer");
            image_timer.setImageResource(R.mipmap.timer_task);
        }else if (timerHour!=0){
            if (deviceState==1){
                image_timer.setImageResource(R.mipmap.timer_open);
            }else if (deviceState==0){
                image_timer.setImageResource(R.mipmap.timer_task);
            }
            tv_timer.setText(timerHour+":00");
        }
        if (warmerCurTemp!=0){
            tv_cur_temp.setText(warmerCurTemp+"");
        }
        if (waramerSetTemp!=0){
            tv_set_temp.setText(waramerSetTemp+"");
            Message msg=handler.obtainMessage();
            msg.arg1=1;
            msg.what=waramerSetTemp;
            handler.sendMessage(msg);
        }
        /**功率状态*/
        if ("01".equals(rateState)){
            tv_rate_state.setText("低");
        }else if ("10".equals(rateState)){
            tv_rate_state.setText("中");
        }else if ("11".equals(rateState)){
            tv_rate_state.setText("高");
        }

        /**屏幕锁定状态*/
        if (0==lockState){
            image_lock.setImageResource(R.mipmap.img_lock);
        }else if (1==lockState){
            if (deviceState==0){
                image_lock.setImageResource(R.mipmap.img_lock);
            }else if (deviceState==1){
                image_lock.setImageResource(R.mipmap.lock_open);
            }
        }
        /**屏幕开启状态*/
        if (0==screenState){
            image_screen.setImageResource(R.mipmap.img_screen);
        }else if (1==screenState){
            if (deviceState==0){
                image_screen.setImageResource(R.mipmap.screen_close);
            }else if (deviceState==1){
                image_screen.setImageResource(R.mipmap.screen_open);
            }
        }
    }

    public void send(DeviceChild deviceChild){
        try {
            int sum=0;
            JSONObject jsonObject=new JSONObject();
            JSONArray jsonArray=new JSONArray();
            int headCode=144;
            jsonArray.put(0,headCode);/**头码*/
            sum=sum+headCode;
            int type=deviceChild.getType();
            int typeHigh=type/256;
            sum=sum+typeHigh;
            int typeLow=type%256;
            sum=sum+typeLow;
            int dataLength=6;
            sum=sum+dataLength;

            int busMode=deviceChild.getBusModel();
            sum=sum+busMode;
            jsonArray.put(1,typeHigh);/**类型 高位*/
            jsonArray.put(2,typeLow);/**类型 低位*/
            jsonArray.put(3,busMode);/**商业模式*/
            jsonArray.put(4,dataLength);/**数据长度*/
            int deviceState=deviceChild.getDeviceState();
            String rateStateStr=deviceChild.getRateState();
            int rateStateHigh=Integer.parseInt(rateStateStr.substring(0,1));
            int rateStateLow=Integer.parseInt(rateStateStr.substring(1));
            int lockState=deviceChild.getLockState();
            int screenState=deviceChild.getScreenState();
            int []x=new int[8];
            x[0]=deviceState;
            x[1]=rateStateHigh;
            x[2]=rateStateLow;
            x[3]=lockState;
            x[4]=screenState;
            x[5]=0;
            x[6]=0;
            x[7]=0;

            int dataContent=TenTwoUtil.changeToTen(x);
            sum=sum+dataContent;
            jsonArray.put(5,dataContent);/**数据内容 开关，功率状态，屏幕状态，屏保状态*/
            int runState2=0;
            sum=sum+runState2;
            jsonArray.put(6,runState2);/**机器当前运行状态2  (保留*/
            int runState3=0;
            sum=sum+runState3;
            jsonArray.put(7,runState3);/**机器当前运行状态3  (保留*/

            int timeHour=deviceChild.getTimerHour();
            sum=sum+timeHour;
            jsonArray.put(8,timeHour);/**定时时间 小时*/
            int timeMin=deviceChild.getTimerMin();
//            sum=sum+timeMin;
            jsonArray.put(9,0);/**定时时间 分*/
            int waramerSetTemp=deviceChild.getWaramerSetTemp();
            sum=sum+waramerSetTemp;

            int checkCode=sum%256;
            jsonArray.put(10,(waramerSetTemp));/**设定温度*/
            jsonArray.put(11,checkCode);/**校验码*/

            jsonArray.put(12,9);/**结束码*/
            jsonObject.put("Warmer",jsonArray);

            if (isBound){
                String topicName="p99/warmer1/"+deviceChild.getMacAddress()+"/set";
                String s=jsonObject.toString();
                boolean success=mqService.publish(topicName,1,s);
                if (success){
                    Log.i("success","-->"+success);
                    int deviceState2=deviceChild.getDeviceState();
                    Log.i("deviceState2","-->"+deviceState2);
                    deviceChildDao.update(deviceChild);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                case 0:
                    int flag=msg.what;
                    if (flag==1){
                        wheelBar.setCanTouch(true);
                    }else if (flag==0){
                        wheelBar.setCanTouch(false);
                    }
                    wheelBar.invalidate();
                    break;
                case 1:
                    int temp=msg.what;
                    tv_set_temp.setText(temp+"");
                    float mStartAngle=(45-temp)*9;
                    wheelBar.setmStartAngle(mStartAngle);
                    wheelBar.invalidate();
                    break;
            }
        }
    };

    private boolean bound=false;
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
                    Utils.showToast(DeviceDetailActivity.this, "设备名称不能为空");
                } else {
                    new UpdateDeviceAsync().execute();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    class UpdateDeviceAsync extends AsyncTask<Void,Void,Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {
            int code=0;
            try {
                int deviceId=deviceChild.getDeviceId();
                String url=updateDeviceNameUrl+"?deviceName="+ URLEncoder.encode(deviceName,"utf-8")+"&deviceId="+deviceId;
                String result=HttpUtils.getOkHpptRequest(url);
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
                        Utils.showToast(DeviceDetailActivity.this, "修改成功");
                        tv_title.setText(deviceName);
                        break;
                    default:
                        Utils.showToast(DeviceDetailActivity.this, "修改失败");
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        running=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
        if (receiver!=null){
            unregisterReceiver(receiver);
        }
        if (isBound && connection!=null){
            unbindService(connection);
        }

    }

    public void getBitWheelInfos() {
        for (int i = 0; i < mStrs.length; i++) {
            infos.add(new SmartWheelInfo(mStrs[i], null));
        }
    }
}
