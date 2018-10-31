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
import com.xr.happyFamily.jia.view_custom.AirProgress;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by win7 on 2018/5/22.
 */

public class AConfActivity extends AppCompatActivity {


    Unbinder unbinder;


    int colorGreen = 0xFF4fe4a2;
    int colorWhite = 0xFFFFFFFF;
    Animation circle_anim;
    boolean isMore;

    int[] timeData = new int[3];
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.image_more)
    ImageView imageMore;
    @BindView(R.id.head)
    RelativeLayout head;
    @BindView(R.id.ll_state)
    TextView llState;
    @BindView(R.id.tv_shiwen)
    TextView tvShiwen;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.tv_feng)
    TextView tvFeng;
    @BindView(R.id.ll4)
    LinearLayout ll4;
    @BindView(R.id.ll_state2)
    RelativeLayout llState2;
    @BindView(R.id.arcprogressBar)
    AirProgress arcprogressBar;
    @BindView(R.id.tv_power)
    TextView tvPower;
    @BindView(R.id.tv_sheding2)
    TextView tvSheding2;
    @BindView(R.id.img_open)
    ImageView imgOpen;
    @BindView(R.id.rl_pro)
    RelativeLayout rlPro;
    @BindView(R.id.ll_context)
    LinearLayout llContext;
    @BindView(R.id.img_zi)
    ImageView imgZi;
    @BindView(R.id.img_leng)
    ImageView imgLeng;
    @BindView(R.id.img_re)
    ImageView imgRe;
    @BindView(R.id.img_tongfeng)
    ImageView imgTongfeng;
    @BindView(R.id.img_shi)
    ImageView imgShi;
    @BindView(R.id.ll_btn)
    LinearLayout llBtn;
    @BindView(R.id.tv_zi)
    TextView tvZi;
    @BindView(R.id.tv_leng)
    TextView tvLeng;
    @BindView(R.id.tv_re)
    TextView tvRe;
    @BindView(R.id.tv_tongfeng)
    TextView tvTongfeng;
    @BindView(R.id.tv_shi)
    TextView tvShi;
    @BindView(R.id.img_shang)
    ImageView imgShang;
    @BindView(R.id.img_zuo)
    ImageView imgZuo;
    @BindView(R.id.img_shui)
    ImageView imgShui;
    @BindView(R.id.img_su)
    ImageView imgSu;
    @BindView(R.id.tv_shang)
    TextView tvShang;
    @BindView(R.id.tv_zuo)
    TextView tvZuo;
    @BindView(R.id.tv_shui)
    TextView tvShui;
    @BindView(R.id.tv_su)
    TextView tvSu;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.img_more2)
    ImageView imgMore2;
    @BindView(R.id.rl_more)
    RelativeLayout rlMore;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view_left)
    View viewLeft;
    @BindView(R.id.view_right)
    View viewRight;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.relative)
    RelativeLayout relative;
    @BindView(R.id.tv_offline)
    TextView tvOffline;
    @BindView(R.id.ll)
    RelativeLayout ll;


    private boolean isBound;
    private DeviceChild deviceChild;
    private DeviceChildDaoImpl deviceChildDao;
    long houseId;
    long deviceId;
    MessageReceiver receiver;

    MyApplication application;
    public static boolean running = false;

    private TimePickViewPopup customViewPopipup;
    private FengSuViewPopup fengSuViewPopup;


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_air);
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
        IntentFilter intentFilter = new IntentFilter("AConfActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);
//        scrollView.requestDisallowInterceptTouchEvent(true);
        arcprogressBar = (AirProgress) findViewById(R.id.arcprogressBar);
        arcprogressBar.setOnSeekBarChangeListener(new AirProgress.OnSeekBarChangeListener() {
            @Override
            public void onChanged(AirProgress seekbar, int curValue) {
                tvSheding2.setText((curValue + 16) + "℃");
            }
        });
        arcprogressBar.setOnActionUpListener(new AirProgress.onActionUpListener() {
            @Override
            public void onFinish(int curValue) {
                deviceChild.setACondSetTemp1(curValue+16);
                setMode(deviceChild);
                send(deviceChild);
            }
        });


        circle_anim = AnimationUtils.loadAnimation(this, R.anim.anim_90);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);

        circle_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

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


        hei = unDisplayViewSize(llMore)[1];

    }




    boolean isShui = false;
    int[] y1 = new int[2];
    int[] y2 = new int[2];
    int dex, hei;

    @OnClick({R.id.image_back, R.id.image_more, R.id.img_more, R.id.img_more2, R.id.view_left, R.id.view_right, R.id.img_open, R.id.img_zi, R.id.img_re, R.id.img_leng, R.id.img_tongfeng, R.id.img_shi, R.id.img_shang, R.id.img_zuo, R.id.img_shui, R.id.img_su
            , R.id.tv_zi, R.id.tv_re, R.id.tv_leng, R.id.tv_tongfeng, R.id.tv_shi, R.id.tv_shang, R.id.tv_zuo, R.id.tv_shui, R.id.tv_su})
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

            case R.id.img_more:
                isMore = true;
                imgMore.startAnimation(circle_anim);
                dex = rlPro.getMeasuredHeight() - arcprogressBar.getMeasuredHeight();

                if (dex > hei) {
                    llContext.scrollTo(0, hei / 2);
                } else {
                    llContext.scrollTo(0, hei - dex * 2);

                }
                llMore.setVisibility(View.VISIBLE);

                break;
            case R.id.img_more2:
                isMore = false;
                imgMore2.startAnimation(circle_anim);
                llMore.setVisibility(View.GONE);
                llContext.scrollTo(0, 0);
//                scrollView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        scrollView.fullScroll(View.FOCUS_UP);
//                    }
//
//                });
                break;
            case R.id.img_open:
                if (deviceChild.getDeviceState() == 0) {
                    deviceChild.setDeviceState(1);
                } else {
                    deviceChild.setDeviceState(0);
                }
                send(deviceChild);
                setMode(deviceChild);
                break;
            case R.id.img_zi:
            case R.id.tv_zi:
                if (deviceChild.getDeviceState() == 1) {
                    initState();
                    deviceChild.setACondState("000");
                    send(deviceChild);
                    setMode(deviceChild);
                }
                break;
            case R.id.img_leng:
            case R.id.tv_leng:
                if (deviceChild.getDeviceState() == 1) {
                    initState();
                    deviceChild.setACondState("001");
                    send(deviceChild);
                    setMode(deviceChild);
                }
                break;
            case R.id.img_re:
            case R.id.tv_re:
                if (deviceChild.getDeviceState() == 1) {
                    initState();
                    deviceChild.setACondState("010");
                    send(deviceChild);
                    setMode(deviceChild);
                }
                break;
            case R.id.img_tongfeng:
            case R.id.tv_tongfeng:
                if (deviceChild.getDeviceState() == 1) {
                    initState();
                    deviceChild.setACondState("011");
                    send(deviceChild);
                    setMode(deviceChild);
                }
                break;
            case R.id.img_shi:
            case R.id.tv_shi:
                if (deviceChild.getDeviceState() == 1) {
                    initState();
                    deviceChild.setACondState("100");
                    send(deviceChild);
                    setMode(deviceChild);
                }
                break;
            case R.id.img_shang:
            case R.id.tv_shang:
                if (deviceChild.getDeviceState() == 1) {
                    if (deviceChild.getACondSUpDown() == 0)
                        deviceChild.setACondSUpDown(1);
                    else
                        deviceChild.setACondSUpDown(0);
                    send(deviceChild);
                    setMode(deviceChild);
                }
                break;
            case R.id.img_zuo:
            case R.id.tv_zuo:
                if (deviceChild.getDeviceState() == 1) {
                    if (deviceChild.getACondSLeftRight() == 0)
                        deviceChild.setACondSLeftRight(1);
                    else
                        deviceChild.setACondSLeftRight(0);
                    send(deviceChild);
                    setMode(deviceChild);
                }
                break;
            case R.id.img_shui:
            case R.id.tv_shui:
                if (deviceChild.getDeviceState() == 1) {
                    isShui = !isShui;
                    if (deviceChild.getACondSleep() == 0)
                        deviceChild.setACondSleep(1);
                    else
                        deviceChild.setACondSleep(0);
                    send(deviceChild);
                    setMode(deviceChild);
                }
                break;
            case R.id.img_su:
            case R.id.tv_su:
                if (deviceChild.getDeviceState() == 1) {
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
            case R.id.view_left:
                int state;
                Log.e("qqqqqTime", timerSwitch + "," + timerMoudle);
                if (timerSwitch == 0) {
                    if (timerMoudle == 1)
                        state = 2;
                    else
                        state = 3;
                } else {
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
            case R.id.view_right:
                Intent intent=new Intent(AConfActivity.this,AConfStateActivity.class);
                int ratedPower=equipRatedPowerHigh*256+equipRatedPowerLow;
                int curdPower=equipCurdPowerHigh*256+equipCurdPowerLow;
                intent.putExtra("ratedPower",ratedPower);
                intent.putExtra("curdPower",curdPower);
                intent.putExtra("deviceId",  deviceChild.getDeviceId());
                intent.putExtra("dataType",7);
                startActivity(intent);
                break;
        }
    }


    public void initState() {
        imgZi.setImageResource(R.mipmap.ic_air_zi);
        imgLeng.setImageResource(R.mipmap.ic_air_leng);
        imgRe.setImageResource(R.mipmap.ic_air_re);
        imgTongfeng.setImageResource(R.mipmap.ic_air_tongfeng);
        imgShi.setImageResource(R.mipmap.ic_air_chushi);
    }

    public void initBai() {
        imgShang.setImageResource(R.mipmap.ic_air_shang);
        imgZuo.setImageResource(R.mipmap.ic_air_zuo);
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
                        Intent intent=new Intent(AConfActivity.this,ShareDeviceActivity.class);
                        long id=deviceChild.getId();
                        intent.putExtra("deviceId",id);
                        startActivityForResult(intent,8000);
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
                    Utils.showToast(AConfActivity.this, "设备名称不能为空");
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
                        Utils.showToast(AConfActivity.this, "修改成功");
                        tvTitle.setText(deviceName);
                        break;
                    default:
                        Utils.showToast(AConfActivity.this, "修改失败");
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    public int[] unDisplayViewSize(View view) {
        int size[] = new int[2];
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        size[0] = view.getMeasuredWidth();
        size[1] = view.getMeasuredHeight();
        return size;
    }

    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext = AConfActivity.this;
    private ImageView img_close;
    private TextView tv_error;

    private void showPopup() {

        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_aconf_error, null);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        tv_error= (TextView) contentViewSign.findViewById(R.id.tv_error);
        String str_err="";
        int[] x = TenTwoUtil.changeToTwo(faultCode);
        if(x[7]==1)
            str_err=str_err+"缺氟，请检查"+"，";
        if(x[6]==1)
            str_err=str_err+"湿敏传感器故障，请检查"+"，";
        if(x[5]==1)
            str_err=str_err+"室内温度传感器故障，请检查"+"，";
        if(x[4]==1)
            str_err=str_err+"室内盘管故障，请检查"+"，";
        if(x[3]==1)
            str_err=str_err+"室外盘管故障，请检查"+"，";
        if(x[2]==1)
            str_err=str_err+"水满故障，请检查"+"，";
        if(x[1]==1)
            str_err=str_err+"过热故障，请检查"+"，";
        if(x[0]==1)
            str_err=str_err+"设备倾斜，请检查"+"，";
        tv_error.setText(str_err.substring(0,str_err.length()-1));

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        mPopWindow = new PopupWindow(contentViewSign);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
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
        mPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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
                if (!TextUtils.isEmpty(macAddress) && deviceChild!=null && macAddress.equals(deviceChild.getMacAddress())) {
                    if (deviceChild2 == null) {
                        Toast.makeText(AConfActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
                        long houseId = deviceChild.getHouseId();
                        Intent data = new Intent();
                        data.putExtra("houseId", houseId);
                        AConfActivity.this.setResult(6000, data);
                        AConfActivity.this.finish();
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
    String windLevel;
    int equipRatedPowerHigh;//设备额定高功率参数
    int equipRatedPowerLow;//设备额定低功率参数
    int equipCurdPowerHigh;//设备当前高功率参数
    int equipCurdPowerLow;//设备当前低功率参数
    int faultCode;

    private void setMode(DeviceChild deviceChild) {
        int socketState = deviceChild.getDeviceState();/**开关状态*/
        timerMoudle = deviceChild.getTimerMoudle();/**定时模式*/
        timerSwitch = deviceChild.getTimerSwitch();/**定时开关*/
        timerHour = deviceChild.getTimerHour();/**定时 时*/
        timerMin = deviceChild.getTimerMin();/**定时 分*/
        rateState = deviceChild.getRateState();
        String aCondState = deviceChild.getACondState();//空调当前状态 000:  自动模式；001： 制冷模式；010： 制热模式；011： 通风模式；100： 除湿模式；
        int aCondSetTemp1 = deviceChild.getACondSetTemp1();/**空调设定温度1*/
        int aCondSetTemp2 = deviceChild.getACondSetTemp2();/**空调设定温度2*/
        int aCondSetData = deviceChild.getACondSetData();/**空调设定参数*/
        int aCondSimpleTemp1 = deviceChild.getACondSimpleTemp1();/**空调采样温度1*/
        int aCondSimpleTemp2 = deviceChild.getACondSimpleTemp2();/**空调采样温度2*/
        int aCondInnerTemp = deviceChild.getACondInnerTemp();//空调内盘管温度
        int aCondOuterTemp = deviceChild.getACondOuterTemp();//空调外盘管温度

        int aCondSleep = deviceChild.getACondSleep();//空调睡眠模式 0关闭 1开启
        int aCondSUpDown = deviceChild.getACondSUpDown();// 0:上下摆叶关闭   1：摆叶开启
        int aCondSLeftRight = deviceChild.getACondSLeftRight();//0:左右摆叶关闭   1：摆叶开启
        windLevel = deviceChild.getWindLevel(); //风速等级
        equipRatedPowerHigh = deviceChild.getEquipRatedPowerHigh();/**设备额定高功率参数*/
        equipRatedPowerLow = deviceChild.getEquipRatedPowerLow();/**设备额定低功率参数*/
        equipCurdPowerHigh = deviceChild.getEquipCurdPowerHigh();/**设备当前高功率参数*/
        equipCurdPowerLow = deviceChild.getEquipCurdPowerLow();/**设备当前低功率参数*/
         faultCode = deviceChild.getFaultCode();/**空调故障代码*/

        if (socketState == 1) {/**当前状态开*/
            imgOpen.setImageResource(R.mipmap.ic_air_kai1);
            llState.setVisibility(View.VISIBLE);
            llState2.setVisibility(View.VISIBLE);
            arcprogressBar.setSign(aCondSetTemp1 - 16);
            tvSheding2.setText(aCondSetTemp1+"℃");
            arcprogressBar.setOpen(true);
            initState();
            if(aCondState!=null)
            switch (aCondState) {
                case "000":
                    imgZi.setImageResource(R.mipmap.ic_air_zi1);
                    break;
                case "001":
                    imgLeng.setImageResource(R.mipmap.ic_air_leng1);
                    break;
                case "010":
                    imgRe.setImageResource(R.mipmap.ic_air_re1);
                    break;
                case "011":
                    imgTongfeng.setImageResource(R.mipmap.ic_air_tongfeng1);
                    break;
                case "100":
                    imgShi.setImageResource(R.mipmap.ic_air_chushi1);
                    break;

                default:
            }
            imgSu.setImageResource(R.mipmap.ic_air_wind1);
            switch (windLevel) {
                case "000":
                    tvFeng.setText("弱风");
                    break;
                case "001":
                    tvFeng.setText("中风");
                    break;
                case "010":
                    tvFeng.setText("强风");
                    break;
                default:
            }
            if (aCondSleep == 1)
                imgShui.setImageResource(R.mipmap.ic_air_shui1);
            else
                imgShui.setImageResource(R.mipmap.ic_air_shui);
            if (aCondSUpDown == 1)
                imgShang.setImageResource(R.mipmap.ic_air_shang1);
            else
                imgShang.setImageResource(R.mipmap.ic_air_shang);
            if (aCondSLeftRight == 1)
                imgZuo.setImageResource(R.mipmap.ic_air_zuo1);
            else
                imgZuo.setImageResource(R.mipmap.ic_air_zuo);
            if (aCondSimpleTemp1>0)
            tvShiwen.setText(aCondSimpleTemp1 + "℃");
            else
            tvShiwen.setText("——℃");
        } else if (socketState == 0) {/**插座当前状态关*/
            initState();
            initBai();
            arcprogressBar.setOpen(false);
            imgShui.setImageResource(R.mipmap.ic_air_shui);
            imgSu.setImageResource(R.mipmap.ic_air_wind);
            imgOpen.setImageResource(R.mipmap.ic_air_kai);
            tvSheding2.setText("——");
            tvShiwen.setText("——");
            tvFeng.setText("——");
        }
        tvPower.setText(equipCurdPowerHigh*256+equipCurdPowerLow+"W");
        if(faultCode!=0){
            if(mPopWindow!=null) {
                if (mPopWindow.isShowing())
                    mPopWindow.dismiss();
            }
            else
            showPopup();
        }


        if (fengSuViewPopup != null) {
            if (fengSuViewPopup.isShowing()) {
                fengSuViewPopup.setWind(rateState);
            }
        }
        if (customViewPopipup != null) {
            if (customViewPopipup.isShowing()) {
                int state;
                if (timerSwitch == 0) {
                    if (timerMoudle == 1)
                        state = 2;
                    else
                        state = 3;
                } else {
                    if (timerMoudle == 1)
                        state = 0;
                    else
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
            int dataLength = 9;
            sum = sum + dataLength;
            int busMode = deviceChild.getBusModel();
            sum = sum + busMode;

            int curRunState3 = deviceChild.getCurRunState3();
            sum = sum + curRunState3;
            int timerMoudle = deviceChild.getTimerMoudle();
            sum = sum + timerMoudle;
            int timeHour = deviceChild.getTimerHour();
            sum = sum + timeHour;
            int timeMin = deviceChild.getTimerMin();
            sum = sum + timeMin;
            int warmerRunState;/**机器当前运行状态*/
            int deviceState = deviceChild.getDeviceState();
            String aCondState = deviceChild.getACondState();
            int aCondSleep = deviceChild.getACondSleep();
            int timerSwitch = deviceChild.getTimerSwitch();
            String windLevel = deviceChild.getWindLevel();
            int aCondSUpDown = deviceChild.getACondSUpDown();// 0:上下摆叶关闭   1：摆叶开启
            int aCondSLeftRight = deviceChild.getACondSLeftRight();//0:左右摆叶关闭   1：摆叶开启
            int aCondSetTemp1 = deviceChild.getACondSetTemp1();/**空调设定温度1*/
            sum=sum+aCondSetTemp1;
            int aCondSetTemp2 = deviceChild.getACondSetTemp2();/**空调设定温度2*/
            sum=sum+aCondSetTemp2;
            int aCondSetData = deviceChild.getACondSetData();/**空调设定参数*/
            sum=sum+aCondSetData;

            int[] x = new int[8];
            x[0] = deviceState;
            x[1] = Integer.parseInt(aCondState.substring(0, 1));
            x[2] = Integer.parseInt(aCondState.substring(1, 2));
            x[3] = Integer.parseInt(aCondState.substring(2, 3));
            x[4] = aCondSleep;
            x[5] = timerSwitch;
            warmerRunState = TenTwoUtil.changeToTen(x);
            sum = sum + warmerRunState;

            int[] x2 = new int[8];
            x2[0] = Integer.parseInt(windLevel.substring(0, 1));

            x2[1] = Integer.parseInt(windLevel.substring(1, 2));
            x2[2] = Integer.parseInt(windLevel.substring(2, 3));
            x2[3] = aCondSUpDown;
            x2[4] = aCondSLeftRight;
            int curRunState2 = TenTwoUtil.changeToTen(x2);
            sum = sum + curRunState2;

            int checkCode = sum % 256;
            jsonArray.put(0, headCode);/**头码*/
            jsonArray.put(1, typeHigh);/**类型 高位*/
            jsonArray.put(2, typeLow);/**类型 低位*/
            jsonArray.put(3, busMode);/**商业模式*/
            jsonArray.put(4, dataLength);/**数据长度*/
            jsonArray.put(5, warmerRunState);
            jsonArray.put(6, curRunState2);
            jsonArray.put(7, curRunState3);
            jsonArray.put(8, aCondSetTemp1);
            jsonArray.put(9, aCondSetTemp2);
            jsonArray.put(10, aCondSetData);
            jsonArray.put(11, timerMoudle);
            jsonArray.put(12, timeHour);
            jsonArray.put(13, timeMin);
            jsonArray.put(14, checkCode);/**校验码*/
            jsonArray.put(15, 9);/**结束码*/
            jsonObject.put("AConditioning", jsonArray);

            if (isBound) {
                String topicName = "p99/aConditioning1/" + deviceChild.getMacAddress() + "/set";
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
            Intent intent=new Intent();
            intent.putExtra("houseId",houseId);
            setResult(6000,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
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
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        running = true;
        deviceChild=deviceChildDao.findById(deviceId);
        if (deviceChild==null){
            Utils.showToast(this,"该设备已重置");
            Intent intent=new Intent();
            intent.putExtra("houseId",houseId);
            setResult(6000,intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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

