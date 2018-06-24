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
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.SmartWheelInfo;
import com.xr.happyFamily.jia.view_custom.SemicircleBar;
import com.xr.happyFamily.jia.view_custom.SmartWheelBar;
import com.xr.happyFamily.jia.view_custom.Timepicker3;
import com.xr.happyFamily.jia.xnty.Timepicker;
import com.xr.happyFamily.jia.xnty.Timepicker2;
import com.xr.happyFamily.together.util.TenTwoUtil;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
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
    @BindView(R.id.semicBar)
    SmartWheelBar wheelBar;
    @BindView(R.id.relative4) RelativeLayout relative4;/**底部布局*/
    private List<String> list=new ArrayList<>();
    Unbinder unbinder;
    Timepicker3 tv_timer_hour;
    Timepicker3 tv_timer_min;
    private DeviceChildDaoImpl deviceChildDao;
    MessageReceiver receiver;
    MQService mqService;
    private boolean isBound=false;
    public static boolean running=false;
    private DeviceChild deviceChild;
    private float curAngle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_device_detail);
        unbinder=ButterKnife.bind(this);
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        Log.w("width","width"+width);
//
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(width,width);
        params.leftMargin=100;
        params.rightMargin=100;
        wheelBar.setLayoutParams(params);
        getBitWheelInfos();
        wheelBar.setBitInfos(infos);
        deviceChildDao=new DeviceChildDaoImpl(getApplicationContext());
        deviceChild=deviceChildDao.findById(1L);
        List<DeviceChild> deviceChildren=deviceChildDao.findAllDevice();
        Log.i("deviceChildren","-->"+deviceChildren.size());

        if (deviceChild==null){
            deviceChild=new DeviceChild();
            deviceChild.setId(1L);
            deviceChild.setMacAddress("hrrj7895ccf7f6c9fa4");
            deviceChild.setType(2);
            deviceChild.setDeviceState(1);
            deviceChild.setRateState("00");
            deviceChild.setLockState(1);
            deviceChild.setScreenState(1);
            deviceChildDao.insert(deviceChild);
        }

        Intent service = new Intent(this, MQService.class);
        startService(service);
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
            setMode(deviceChild);
        }
        curAngle=wheelBar.getmStartAngle();
//        params.leftMargin=200;
//        params.rightMargin=200;
//        wheelBar.setLayoutParams(params);

//        wheelBar.setOnSeekBarChangeListener(new SemicircleBar.OnSeekBarChangeListener() {
//            @Override
//            public void onChanged(SemicircleBar seekbar, double curValue) {
//
//            }
//        });
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

                deviceChild.setWarmerCurTemp(warmerTempSet);
                send(deviceChild);
            }
        });
    }
    @OnClick({R.id.image_switch,R.id.image_timer,R.id.image_rate,R.id.image_lock,R.id.image_screen})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_switch:
                int deviceState=deviceChild.getDeviceState();
                if (deviceState==0){
                    deviceChild.setDeviceState(1);
                }else if (deviceState==1){
                    deviceChild.setDeviceState(0);
                }
                setMode(deviceChild);
                send(deviceChild);
                break;
            case R.id.image_timer:
                popupTimerWindow();
                break;
            case R.id.image_rate:
                popupRateView();
                break;
            case R.id.image_lock:
                int lockState=deviceChild.getLockState();
                if (lockState==1){
                    deviceChild.setLockState(0);
                }else if (lockState==0){
                    deviceChild.setLockState(1);
                }
                setMode(deviceChild);
                send(deviceChild);
                break;
            case R.id.image_screen:
                int screenState=deviceChild.getScreenState();
                if (screenState==1){
                    deviceChild.setScreenState(0);
                }else if (screenState==0){
                    deviceChild.setScreenState(1);
                }
                setMode(deviceChild);
                send(deviceChild);
                break;
        }
    }
    private void initTimer(){//设置定时时间
        tv_timer_hour.setMaxValue(23);
        tv_timer_hour.setMinValue(00);
        tv_timer_hour.setValue(49);
        //timepicker1.setBackgroundColor(Color.LTGRAY);
        tv_timer_hour.setNumberPickerDividerColor(tv_timer_hour);
        tv_timer_min.setMaxValue(59);
        tv_timer_min.setMinValue(00);
        tv_timer_min.setValue(49);
        //timepicker2.setBackgroundColor(Color.LTGRAY);
        tv_timer_min.setNumberPickerDividerColor(tv_timer_min);


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
       tv_timer_min= (Timepicker3) view.findViewById(R.id.tv_min);




        initTimer();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        tv_timer_hour.setValue(hour);

        int hour2=tv_timer_hour.getValue();
        Log.i("hour2", "popupWindow: "+hour2);

        int minute = calendar.get(Calendar.MINUTE);
        tv_timer_min.setValue(minute);
        int min=tv_timer_min.getValue();
        Log.i("min", "popupWindow: "+min);

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
                        int sh= tv_timer_hour.getValue();
                        int sm= tv_timer_min.getValue();
                        deviceChild.setTimerHour(sh);
                        deviceChild.setTimerMin(sm);
                        send(deviceChild);
                        Log.i("time", "--->: "+sh+",,,"+sm);
                        popupWindow.dismiss();
                        break;
                }
            }
        };

        image_cancle.setOnClickListener(listener);
        image_ensure.setOnClickListener(listener);
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
            DeviceChild deviceChild2= (DeviceChild) intent.getSerializableExtra("deviceChild");
            Log.i("deviceChild2","-->"+deviceChild2.getMacAddress());
            Log.i("warmerCurTemp","-->"+deviceChild2.getWarmerCurTemp());
            if (!TextUtils.isEmpty(macAddress) && macAddress.equals(deviceChild.getMacAddress())){
                deviceChild=deviceChild2;
                Log.i("warmerCurTemp","-->"+deviceChild.getWarmerCurTemp());
                setMode(deviceChild);
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
            image_switch.setImageResource(R.mipmap.image_switch);
            image_rate.setImageResource(R.mipmap.rate_power);
        }else if (deviceState==1){
            image_switch.setImageResource(R.mipmap.image_open);
            image_rate.setImageResource(R.mipmap.rate_open);
        }
        if (timerHour==0 && timerMin==0){
            Log.i("timer","-->"+"timer");
            image_timer.setImageResource(R.mipmap.timer_task);
        }else if (timerHour!=0 || timerMin!=0){
            Log.i("timer","-->"+"timer3");
            image_timer.setImageResource(R.mipmap.timer_open);
            tv_timer.setText(timerHour+":"+timerMin);
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
            image_lock.setImageResource(R.mipmap.lock_open);
        }
        /**屏幕开启状态*/
        if (0==screenState){
            image_screen.setImageResource(R.mipmap.img_screen);
        }else if (1==screenState){
            image_screen.setImageResource(R.mipmap.screen_open);
        }
    }
    int sum=0;
    public void send(DeviceChild deviceChild){
        try {
            JSONObject jsonObject=new JSONObject();
            JSONArray jsonArray=new JSONArray();
            int headCode=144;
            jsonArray.put(0,headCode);/**头码*/
            sum+=headCode;
            int type=deviceChild.getType();
            int typeHigh=type/256;
            sum+=typeHigh;
            int typeLow=type%256;
            sum+=typeLow;
            int dataLength=6;
            sum+=dataLength;

            int busMode=deviceChild.getBusModel();
            sum+=busMode;
            jsonArray.put(1,typeHigh);/**类型 高位*/
            jsonArray.put(2,typeLow);/**类型 低位*/
            jsonArray.put(3,busMode);/**商业模式*/
            jsonArray.put(4,dataLength);/**数据长度*/
            int deviceState=deviceChild.getDeviceState();
            String rateStateStr=deviceChild.getRateState();
            int rateState=Integer.parseInt(rateStateStr);
            int rateStateHigh=rateState/10;
            int rateStateLow=rateState%10;
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
            sum+=dataContent;
            jsonArray.put(5,dataContent);/**数据内容 开关，功率状态，屏幕状态，屏保状态*/
            int runState2=0;
            sum+=runState2;
            jsonArray.put(6,runState2);/**机器当前运行状态2  (保留*/
            int runState3=0;
            sum+=runState3;
            jsonArray.put(7,runState3);/**机器当前运行状态3  (保留*/

            int timeHour=deviceChild.getTimerHour();
            sum+=timeHour;
            jsonArray.put(8,timeHour);/**定时时间 小时*/
            int timeMin=deviceChild.getTimerMin();
            sum+=timeMin;
            jsonArray.put(9,0);/**定时时间 分*/
            int waramerSetTemp=deviceChild.getWaramerSetTemp();
            sum+=waramerSetTemp;

            int checkCode=sum%256;
            jsonArray.put(10,waramerSetTemp);/**设定温度*/
            jsonArray.put(11,checkCode);/**校验码*/

            jsonArray.put(12,9);/**结束码*/
            jsonObject.put("Warmer",jsonArray);

            if (isBound){
                String topicName="p99/warmer/"+deviceChild.getMacAddress()+"/set";
                String s=jsonObject.toString();
                boolean success=mqService.publish(topicName,1,s);
                if (success){
                    Log.i("success","-->"+success);
                    int timerHour=deviceChild.getTimerHour();
                    int timerMin=deviceChild.getTimerMin();
                    if (timerHour==0 && timerMin==0){
                        Log.i("timer","-->"+"timer");
                        image_timer.setImageResource(R.mipmap.timer_task);
                    }else if (timerHour!=0 || timerMin!=0){
                        Log.i("timer","-->"+"timer3");
                        image_timer.setImageResource(R.mipmap.timer_open);
                    }
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

    @Override
    protected void onStop() {
        super.onStop();
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
        running=false;
    }

    public void getBitWheelInfos() {
        for (int i = 0; i < mStrs.length; i++) {
            infos.add(new SmartWheelInfo(mStrs[i], null));
        }
    }
}
