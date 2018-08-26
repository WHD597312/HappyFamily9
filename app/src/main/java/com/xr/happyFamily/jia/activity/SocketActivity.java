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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.jia.view_custom.Timepicker3;
import com.xr.happyFamily.jia.xnty.NoFastClickUtils;
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

public class SocketActivity extends AppCompatActivity {

    Unbinder unbinder;
    MyApplication application;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.relative) RelativeLayout relative;
    @BindView(R.id.tv_offline) TextView tv_offline;
    @BindView(R.id.relative4) RelativeLayout relative4;
    @BindView(R.id.image_more) ImageView image_more;
    @BindView(R.id.socket_switch) ImageView socket_switch;
    @BindView(R.id.image_timer) ImageView image_timer;/**定时任务*/
    @BindView(R.id.tv_timer) TextView tv_timer;/**定时状态*/
    @BindView(R.id.tv_close_socket) TextView tv_close_socket;/**定时开关插座*/
    @BindView(R.id.tv_switch_state) TextView tv_switch_state;/**电源开关状态*/
    private DeviceChild deviceChild;
    MessageReceiver receiver;
    private boolean isBound;
    public static boolean running=false;
    private String updateDeviceNameUrl= HttpUtils.ipAddress+"/family/device/changeDeviceName";
    private DeviceChildDaoImpl deviceChildDao;
    long houseId;
    long deviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_socket);
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        unbinder=ButterKnife.bind(this);


        deviceChildDao=new DeviceChildDaoImpl(getApplicationContext());
        Intent intent=getIntent();
        deviceId=intent.getLongExtra("deviceId",0);
        deviceChild=deviceChildDao.findById(deviceId);
        houseId=intent.getLongExtra("houseId",0);
        Intent service = new Intent(this, MQService.class);
        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
        IntentFilter intentFilter = new IntentFilter("SocketActivity");
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
                relative.setVisibility(View.VISIBLE);
                tv_offline.setVisibility(View.GONE);
                setMode(deviceChild);
            }else {
                relative.setVisibility(View.GONE);
                tv_offline.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.socket_switch,R.id.image_back,R.id.image_timer,R.id.image_more,R.id.image_meter})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                Intent intent=new Intent();
                intent.putExtra("houseId",houseId);
                setResult(6000,intent);
                finish();
                break;
            case R.id.image_timer:
                popupTimerWindow();
                break;
            case R.id.socket_switch:
                if (NoFastClickUtils.isFastClick()){
                    int socketState=deviceChild.getSocketState();
                    if (socketState==0){
                        deviceChild.setSocketState(1);
                        deviceChild.setSocketTimerOpenHour(0);
                        deviceChild.setSocketTimerOpenMin(0);
                    }else if (socketState==1){
                        deviceChild.setSocketTimerOpenHour(0);
                        deviceChild.setSocketTimerOpenMin(0);
                        deviceChild.setSocketState(0);
                    }
                    setMode(deviceChild);
                    send(deviceChild);
                }

                break;
            case R.id.image_more:
                popupmenuWindow();
                break;
            case R.id.image_meter:
                Intent intent2=new Intent(this,TempChatActivity.class);
                intent2.putExtra("deviceId",deviceId);
                startActivity(intent2);
                break;
        }
    }
    Timepicker3 tv_timer_hour;
    Timepicker3 tv_timer_min;
    private void initTimer(){//设置定时时间
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
                        Intent intent=new Intent(SocketActivity.this,ShareDeviceActivity.class);
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
                        Utils.showToast(SocketActivity.this, "修改成功");
                        tv_title.setText(deviceName);
                        break;
                    default:
                        Utils.showToast(SocketActivity.this, "修改失败");
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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
        tv_timer_min.setVisibility(View.VISIBLE);

        initTimer();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min2=calendar.get(Calendar.MINUTE);
        tv_timer_hour.setValue(hour);
        tv_timer_min.setValue(min2);

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
                        int min=tv_timer_min.getValue();
                        deviceChild.setSocketTimerOpenHour(sh);
                        deviceChild.setSocketTimerOpenMin(min);
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

    class MessageReceiver extends BroadcastReceiver {

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
                        Toast.makeText(SocketActivity.this,"该设备已重置",Toast.LENGTH_SHORT).show();
                        long houseId=deviceChild.getHouseId();
                        Intent data=new Intent();
                        data.putExtra("houseId",houseId);
                        SocketActivity.this.setResult(6000,data);
                        SocketActivity.this.finish();
                    }else {
                        deviceChild=deviceChild2;
                        boolean online=deviceChild.getOnline();
                        if (online){
                            relative.setVisibility(View.VISIBLE);
                            tv_offline.setVisibility(View.GONE);
                            setMode(deviceChild);
                        }else {
                            relative.setVisibility(View.GONE);
                            tv_offline.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }

    }
    private MQService mqService;
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

    private void setMode(DeviceChild deviceChild){
        int socketState=deviceChild.getSocketState();/**插座开关状态*/
        int socketTimer=deviceChild.getSocketTimer();/**插座定时状态*/
        int socketTimerOpenHour=deviceChild.getSocketTimerOpenHour();/**插座定时模式开的 时*/
        int socketTimerOpenMin=deviceChild.getSocketTimerOpenMin();/**插座定时模式开的 分*/
        int socketTimerCloseHour=deviceChild.getSocketTimerCloseHour();/**插座定时模式关的 时*/
        int socketTimerCloseMin=deviceChild.getSocketTimerCloseMin();/**插座定时模式关的 分*/
        if (socketState==1){/**插座当前状态开*/
            tv_switch_state.setText("插座电源已开启");
            socket_switch.setImageResource(R.mipmap.socket_switch);
            if (socketTimerOpenHour==0 && socketTimerOpenMin==0){
                tv_timer.setText("定时");
                tv_close_socket.setVisibility(View.GONE);
            }else {
                tv_close_socket.setVisibility(View.VISIBLE);
                tv_close_socket.setText(socketTimerOpenHour+":"+socketTimerOpenMin+"关闭插座");
                tv_timer.setText(socketTimerOpenHour+":"+socketTimerOpenMin);
            }
        }else if (socketState==0){/**插座当前状态关*/
            socket_switch.setImageResource(R.mipmap.socket_switch_close);
            tv_switch_state.setText("插座电源已关闭");
            if (socketTimerOpenHour==0 && socketTimerOpenMin==0){
                tv_timer.setText("定时");
                tv_close_socket.setVisibility(View.GONE);
            }else {
                tv_close_socket.setVisibility(View.VISIBLE);
                tv_close_socket.setText(socketTimerOpenHour+":"+socketTimerOpenMin+"开启插座");
                tv_timer.setText(socketTimerOpenHour+":"+socketTimerOpenMin);
            }
        }
        if (socketTimer==0){
            image_timer.setImageResource(R.mipmap.socket_timer_close);
        }else if (socketTimer==1){
            image_timer.setImageResource(R.mipmap.socket_timer);
        }
    }
    private void send(DeviceChild deviceChild){
        try {
            int sum=0;
            JSONObject jsonObject=new JSONObject();
            JSONArray jsonArray=new JSONArray();
            int headCode=144;
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
            int socketPowerHigh=deviceChild.getSocketPowerHigh();/**功率高位*/
            sum=sum+socketPowerHigh;
            int socketPowerLow=deviceChild.getSocketPowerLow();/**功率低位*/
            sum=sum+socketPowerLow;
            int socketTemp=deviceChild.getSocketTemp();/**温度*/
            sum=sum+socketTemp;
            int socketState=deviceChild.getSocketState();
            int []x=new int[8];
            x[0]=socketState;
            int dataContent= TenTwoUtil.changeToTen(x);
            sum=sum+dataContent;
            int socketTimerOpenHour=deviceChild.getSocketTimerOpenHour();
            sum=sum+socketTimerOpenHour;
            int socketTimerOpenMin=deviceChild.getSocketTimerOpenMin();
            sum=sum+socketTimerOpenMin;
            int checkCode=sum%256;

            jsonArray.put(0,headCode);/**头码*/
            jsonArray.put(1,typeHigh);/**类型 高位*/
            jsonArray.put(2,typeLow);/**类型 低位*/
            jsonArray.put(3,busMode);/**商业模式*/
            jsonArray.put(4,dataLength);/**数据长度*/
            jsonArray.put(5,socketPowerHigh);
            jsonArray.put(6,socketPowerLow);
            jsonArray.put(7,socketTemp);
            jsonArray.put(8,dataContent);
            jsonArray.put(9,socketTimerOpenHour);
            jsonArray.put(10,socketTimerOpenMin);
            jsonArray.put(11,checkCode);/**校验码*/
            jsonArray.put(12,9);/**结束码*/
            jsonObject.put("Socket",jsonArray);

            if (isBound){
                String topicName="p99/socket1/"+deviceChild.getMacAddress()+"/set";
                String s=jsonObject.toString();
                boolean success=mqService.publish(topicName,1,s);
                if (success){
                    deviceChildDao.update(deviceChild);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 返回键功能
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (application!=null){
                application.removeActivity(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
        running=false;
        if (receiver!=null){
            unregisterReceiver(receiver);
        }
        if (isBound && connection!=null){
            unbindService(connection);
        }
    }
}
