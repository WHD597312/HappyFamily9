package com.xr.happyFamily.jia.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.view.DoubleWaveView;
import com.xr.happyFamily.jia.LiveActivity;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.jia.view_custom.Themometer;
import com.xr.happyFamily.jia.view_custom.VerticalProgressBar;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.GlideCircleTransform;
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

public class PurifierActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.doubleW)
    DoubleWaveView doubleWaveView;
    //    @BindView(R.id.themometer) Themometer themometer;
    @BindView(R.id.vp_progress)
    VerticalProgressBar vp_progress;
    @BindView(R.id.tv_title)
    TextView tv_title;
    /**
     * 设备名称
     */
    @BindView(R.id.image_more)
    ImageView image_more;
    @BindView(R.id.img_cold)
    ImageView img_cold;
    /**
     * 低温
     */
    @BindView(R.id.img_normal)
    ImageView img_normal;
    /**
     * 常温
     */
    @BindView(R.id.img_high)
    ImageView img_high;
    /***常温*/
    @BindView(R.id.img_water)
    ImageView img_water;
    /**
     * 热水
     */
    @BindView(R.id.tv_offline)
    TextView tv_offline;
    /**
     * 离线
     */
    @BindView(R.id.body)
    RelativeLayout body;
    /**
     * 中间布局
     */
    @BindView(R.id.layout_bottom)
    RelativeLayout layout_bottom;
    /**
     * 底部布局
     */
    @BindView(R.id.layout_head)
    RelativeLayout layout_head;
    @BindView(R.id.head)
    RelativeLayout head;
    /**
     * 头部布局
     */
    @BindView(R.id.tv_tds_value)
    TextView tv_tds_value;
    /**
     * tds值
     */
    @BindView(R.id.tv_temp_value)
    TextView tv_temp_value;
    /**
     * 温度值
     */
    @BindView(R.id.img2)
    ImageView img2;
    /**
     * 温度计背景图
     */
    private MyApplication application;
    private DeviceChildDaoImpl deviceChildDao;
    private DeviceChild deviceChild;
    private String deviceName;
    private String updateDeviceNameUrl = HttpUtils.ipAddress + "/family/device/changeDeviceName";
    private long houseId;
    MessageReceiver receiver;
    boolean isBound = false;
    public static boolean running = false;
    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_purifier);
        if (application == null) {
            application = (MyApplication) getApplication();
            application.addActivity(this);
        }
        unbinder = ButterKnife.bind(this);
        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
        Intent intent = getIntent();
        id = intent.getLongExtra("deviceId", 0);
        houseId = intent.getLongExtra("houseId", 0);
        deviceChild = deviceChildDao.findById(id);
        if (deviceChild != null) {
            deviceName = deviceChild.getName();
            tv_title.setText(deviceName);
        }

        Intent service = new Intent(this, MQService.class);
        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
        IntentFilter intentFilter = new IntentFilter("PurifierActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        running = true;
        deviceChild = deviceChildDao.findById(id);
        if (deviceChild != null) {
            boolean online = deviceChild.getOnline();
            if (online) {
                body.setVisibility(View.VISIBLE);
                layout_bottom.setVisibility(View.VISIBLE);
                tv_offline.setVisibility(View.GONE);
                layout_head.setBackgroundResource(R.mipmap.purifierback);
                vp_progress.setVisibility(View.VISIBLE);
                img2.setVisibility(View.VISIBLE);
                tv_temp_value.setVisibility(View.VISIBLE);
                setMode(deviceChild);
            } else {
                body.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                tv_offline.setVisibility(View.VISIBLE);
                vp_progress.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                tv_temp_value.setVisibility(View.GONE);
                layout_head.setBackgroundResource(0);
            }
        } else {
            running=false;
            Intent intent = new Intent();
            intent.putExtra("houseId", houseId);
            setResult(6000, intent);
            finish();
        }
    }

    @OnClick({R.id.image_back, R.id.layout_wt_record, R.id.layout_living, R.id.image_more, R.id.img_cold, R.id.img_normal, R.id.img_high, R.id.img_water})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_wt_record:
                Intent wtRecord = new Intent(this, UseWaterRecordActivity.class);
                wtRecord.putExtra("deviceId",deviceChild.getId());
                wtRecord.putExtra("houseId",houseId);
                startActivityForResult(wtRecord, 8000);
                break;
            case R.id.layout_living:
                Intent live = new Intent(this, LiveActivity.class);
                live.putExtra("deviceId",deviceChild.getId());
                live.putExtra("houseId",houseId);
                startActivityForResult(live, 8000);
                break;
            case R.id.image_back:
                Intent intent = new Intent();
                intent.putExtra("houseId", houseId);
                setResult(6000, intent);
                finish();
                break;
            case R.id.image_more:
                popupmenuWindow();
                break;
            case R.id.img_cold:
                deviceChild.setWPurifierState("000");
                setMode(deviceChild);
                send(deviceChild);
                break;
            case R.id.img_normal:
                deviceChild.setWPurifierState("001");
                setMode(deviceChild);
                send(deviceChild);
                break;
            case R.id.img_high:
                deviceChild.setWPurifierState("010");
                setMode(deviceChild);
                send(deviceChild);
                break;
            case R.id.img_water:
                deviceChild.setWPurifierState("011");
                setMode(deviceChild);
                send(deviceChild);
                break;
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
        if (popupWindow1==null)
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
                        Intent intent = new Intent(PurifierActivity.this, ShareDeviceActivity.class);
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
                    Utils.showToast(PurifierActivity.this, "设备名称不能为空");
                } else {
                    new UpdateDeviceAsync().execute();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public void setMode(DeviceChild deviceChild) {
        String wPurifierState = deviceChild.getWPurifierState();
        int wPurifierCurTemp = deviceChild.getWPurifierCurTemp();
        int wPurifierOutQuqlity = deviceChild.getWPurifierOutQuqlity();
        if ("000".equals(wPurifierState)) {
            img_cold.setImageResource(R.mipmap.img_cold);
            img_normal.setImageResource(R.mipmap.img_unnormal_temp);
            img_high.setImageResource(R.mipmap.img_unhigh_temp);
            img_water.setImageResource(R.mipmap.img_unwater);
        } else if ("001".equals(wPurifierState)) {
            img_cold.setImageResource(R.mipmap.img_uncold);
            img_normal.setImageResource(R.mipmap.img_normal_temp);
            img_high.setImageResource(R.mipmap.img_unhigh_temp);
            img_water.setImageResource(R.mipmap.img_unwater);
        } else if ("010".equals(wPurifierState)) {
            img_cold.setImageResource(R.mipmap.img_uncold);
            img_normal.setImageResource(R.mipmap.img_unnormal_temp);
            img_high.setImageResource(R.mipmap.img_high_temp);
            img_water.setImageResource(R.mipmap.img_unwater);
        } else if ("011".equals(wPurifierState)) {
            img_cold.setImageResource(R.mipmap.img_uncold);
            img_normal.setImageResource(R.mipmap.img_unnormal_temp);
            img_high.setImageResource(R.mipmap.img_unhigh_temp);
            img_water.setImageResource(R.mipmap.img_water);
        }
        doubleWaveView.setProHeight(wPurifierOutQuqlity);
        tv_tds_value.setText(wPurifierOutQuqlity + "");
        vp_progress.setProgress(wPurifierCurTemp);
        tv_temp_value.setText(wPurifierCurTemp + "℃");
    }
    public void send(DeviceChild deviceChild){
        try {
            int sum=0;
            JSONObject jsonObject=new JSONObject();
            JSONArray jsonArray=new JSONArray();
            int headCode=144;
            jsonArray.put(0,headCode);/**头码*/
            int type=deviceChild.getType();
            int typeHigh=type/256;
            int typeLow=type%256;
            int dataLength=8;
            int busMode=deviceChild.getBusModel();
            jsonArray.put(1,typeHigh);/**类型 高位*/
            jsonArray.put(2,typeLow);/**类型 低位*/
            jsonArray.put(3,busMode);/**商业模式*/
            jsonArray.put(4,dataLength);/**数据长度*/
            jsonArray.put(5,35);/**数据位*/
            sum=sum+35;
            int wPurifierEndYear=deviceChild.getWPurifierEndYear();
            int wPurifierEndYearHigh=wPurifierEndYear/256;

            jsonArray.put(6,wPurifierEndYearHigh);
            int wPurifierEndYearLow=wPurifierEndYear%256;
            jsonArray.put(7,wPurifierEndYearLow);

            int wPurifierEndMonth=deviceChild.getWPurifierEndMonth();
            jsonArray.put(8,wPurifierEndMonth);
            int wPurifierEndDay=deviceChild.getWPurifierEndDay();
            jsonArray.put(9,wPurifierEndDay);

            int wPurifierEndFlow=deviceChild.getWPurifierEndFlow();
            int wPurifierEndFlowHigh=wPurifierEndFlow/256;
            jsonArray.put(10,wPurifierEndFlowHigh);
            int wPurifierEndFlowLow=wPurifierEndFlow%256;
            jsonArray.put(11,wPurifierEndFlowLow);
            String wPurifierState=deviceChild.getWPurifierState();
            if (!TextUtils.isEmpty(wPurifierState)){
                String num1=wPurifierState.substring(0,1);
                String num2=wPurifierState.substring(1,2);
                String num3=wPurifierState.substring(2);
                int []x=new int[8];
                x[0]=Integer.parseInt(num1);
                x[1]=Integer.parseInt(num2);
                x[2]=Integer.parseInt(num3);
                int dataContent= TenTwoUtil.changeToTen(x);
                sum=sum+dataContent;
                jsonArray.put(12,dataContent);
            }else {
                jsonArray.put(12,0);
            }
            sum=headCode+type+busMode+dataLength+wPurifierEndYear+wPurifierEndMonth+wPurifierEndDay+sum;
            int checkCode=sum%256;
            jsonArray.put(13,checkCode);/**校验码*/
            jsonArray.put(14,9);/**结束码*/
            jsonObject.put("WPurifier",jsonArray);

            if (isBound){
                String topicName="p99/wPurifier1/"+deviceChild.getMacAddress()+"/set";
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
                        Utils.showToast(PurifierActivity.this, "修改成功");
                        tv_title.setText(deviceName);
                        break;
                    default:
                        Utils.showToast(PurifierActivity.this, "修改失败");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN) {
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
        running=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (isBound == true && connection != null) {
            unbindService(connection);
        }
        running = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress = intent.getStringExtra("macAddress");
            Log.i("macAddress", "-->" + macAddress);
            DeviceChild deviceChild2 = (DeviceChild) intent.getSerializableExtra("deviceChild");
            String noNet = intent.getStringExtra("noNet");
            if (!TextUtils.isEmpty(noNet)) {
                body.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                tv_offline.setVisibility(View.VISIBLE);
                vp_progress.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                tv_temp_value.setVisibility(View.GONE);
                layout_head.setBackgroundResource(0);
            } else {
                if (!TextUtils.isEmpty(macAddress) && deviceChild!=null && macAddress.equals(deviceChild.getMacAddress())) {
                    if (deviceChild2 == null) {
                        Toast.makeText(PurifierActivity.this,"该设备已重置",Toast.LENGTH_SHORT).show();
                        long houseId=deviceChild.getHouseId();
                        Intent data=new Intent();
                        data.putExtra("houseId",houseId);
                        PurifierActivity.this.setResult(6000,data);
                        finish();
                    } else {
                        deviceChild = deviceChild2;
                        boolean online = deviceChild.getOnline();
                        if (online) {
                            body.setVisibility(View.VISIBLE);
                            layout_bottom.setVisibility(View.VISIBLE);
                            tv_offline.setVisibility(View.GONE);
                            layout_head.setBackgroundResource(R.mipmap.purifierback);
                            vp_progress.setVisibility(View.VISIBLE);
                            img2.setVisibility(View.VISIBLE);
                            tv_temp_value.setVisibility(View.VISIBLE);
                            setMode(deviceChild);
                        } else {
                            body.setVisibility(View.GONE);
                            layout_bottom.setVisibility(View.GONE);
                            tv_offline.setVisibility(View.VISIBLE);
                            vp_progress.setVisibility(View.GONE);
                            img2.setVisibility(View.GONE);
                            tv_temp_value.setVisibility(View.GONE);
                            layout_head.setBackgroundResource(0);
                        }
                    }
                }
            }
        }
    }
}
