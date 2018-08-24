package com.xr.happyFamily.login.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mob.tools.utils.UIHandler;
import com.xr.database.dao.HourseDao;
import com.xr.database.dao.RoomDao;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.login.rigest.ForgetPswdActivity;
import com.xr.happyFamily.login.rigest.RegistActivity;
import android.os.Handler.Callback;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.http.NetWorkUtil;
import com.xr.happyFamily.together.util.Mobile;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.location.CheckPermissionsActivity;
import com.xr.happyFamily.together.util.mqtt.ClockService;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.framework.ShareSDK;
import static android.R.attr.action;
import pl.droidsonroids.gif.GifDrawable;
import cn.sharesdk.framework.PlatformActionListener;
public class LoginActivity extends CheckPermissionsActivity implements Callback,
        PlatformActionListener {
    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR= 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    Unbinder unbinder;
    MyApplication application;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_pswd)
    EditText et_pswd;
    String url = "http://47.98.131.11:8084/login/auth";
    String ip = "http://47.98.131.11:8084";
    @BindView(R.id.image_seepwd)
    ImageView imageView;
    @BindView(R.id.image_wx)
    ImageView imageViewwx;
    @BindView(R.id.imageView6)
    ImageView imageView6;
    boolean isHideFirst = true;
    private HourseDaoImpl hourseDao;
    private RoomDaoImpl roomDao;
    private DeviceChildDaoImpl deviceChildDao;
    GifDrawable gifDrawable;
    @BindView(R.id.rl_login) RelativeLayout rl_login;
    Animation rotate;
    SharedPreferences mPositionPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        imageView.setImageResource(R.mipmap.yanjing13x);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        preferences = getSharedPreferences("my", MODE_PRIVATE);

        if (application == null) {
            application = (MyApplication) getApplication();
        }
        mPositionPreferences=getSharedPreferences("position", Context.MODE_PRIVATE);

        application.addActivity(this);
        et_name.setText(preferences.getString("phone", ""));
        et_pswd.setText(preferences.getString("password", ""));
        hourseDao = new HourseDaoImpl(getApplicationContext());
        roomDao = new RoomDaoImpl(getApplicationContext());
        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
    }

    SharedPreferences preferences;
    private String login;
    @Override
    protected void onStart() {
        super.onStart();
        if (preferences.contains("phone") && !preferences.contains("password")) {
            String phone = preferences.getString("phone", "");
            et_name.setText(phone);
            et_pswd.setText("");
        }
//        try {
//            gifDrawable = new GifDrawable(getResources(), R.mipmap.dtubiao);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (gifDrawable != null) {
//            gifDrawable.start();
//            imageView6.setImageDrawable(gifDrawable);
//        }
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        /*imagefs.setAnimation(rotate);
        imagefs.startAnimation(rotate);*/
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        rotate.setInterpolator(lin);
        imageView6.startAnimation(rotate);



    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    class CountTimer extends CountDownTimer {
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * 倒计时过程中调用
         *
         * @param millisUntilFinished
         */
        @Override
        public void onTick(long millisUntilFinished) {

            Log.e("Tag", "倒计时=" + (millisUntilFinished / 1000));
        }

        /**
         * 倒计时完成后调用
         */

        @Override
        public void onFinish() {
            Log.e("Tag", "倒计时完成");
            hideProgressDialog();
        }
    }
    String phone;
    String password;
    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_pswd, R.id.image_seepwd, R.id.image_wx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(this, RegistActivity.class));
                break;
            case R.id.btn_login:
                phone = et_name.getText().toString().trim();
                password = et_pswd.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Utils.showToast(this, "账号码不能为空");
                    break;
                } else if (!Mobile.isMobile(phone)) {
                    Utils.showToast(this, "手机号码不合法");
                    break;
                }
                if (TextUtils.isEmpty(password)) {
                    Utils.showToast(this, "请输入密码");
                    break;
                }
                boolean isConn = NetWorkUtil.isConn(MyApplication.getContext());
                if (isConn){
                    showProgressDialog("正在登录，请稍后...");
                    CountTimer countTimer = new CountTimer(6000, 1000);
                    countTimer.start();
                    Map<String, Object> params = new HashMap<>();
                    params.put("phone", phone);
                    params.put("password", password);
                    new LoginAsyncTask().execute(params);
                }else {
                    Utils.showToast(this, "无网络可用，请检查网络");
                }



                break;
            case R.id.tv_forget_pswd:
                startActivity(new Intent(this, ForgetPswdActivity.class));
                break;
            case R.id.image_seepwd:
                if (isHideFirst == true) {
                    imageView.setImageResource(R.mipmap.yanjing);
                    //密文
                    HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
                    et_pswd.setTransformationMethod(method1);
                    isHideFirst = false;
                } else {
                    imageView.setImageResource(R.mipmap.yanjing13x);
                    //密文
                    TransformationMethod method = PasswordTransformationMethod.getInstance();
                    et_pswd.setTransformationMethod(method);
                    isHideFirst = true;

                }
                // 光标的位置
                int index = et_pswd.getText().toString().length();
                et_pswd.setSelection(index);
                break;
            case R.id.image_wx:
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(this);
                wechat.SSOSetting(false);
                authorize(wechat, 1);
                break;
        }
    }

    private static final int MSG_ACTION_CCALLBACK = 0;
    private ProgressDialog progressDialog;

    /******三方登录逻辑*******/
    private void authorize(Platform plat, int type) {
        switch (type) {
            case 1:
                showProgressDialog("正在打开微信，请稍后...");
                break;
            case 2:
                showProgressDialog("正在打开QQ，请稍后...");
                break;

        }
        if (plat.isValid()) { //如果授权就删除授权资料
            plat.removeAccount();
        }
        plat.showUser(null);//授权并获取用户信息
    }

    //登陆授权成功的回调
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);   //发送消息
        Log.e("test", "授权成功" );
        Log.e("msg", "onComplete:--> "+msg.toString());
    }

    //登陆授权错误的回调
    @Override
    public void onError(Platform platform, int i, Throwable t) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    //登陆授权取消的回调
    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    //登陆发送的handle消息在这里处理
    String oAuthId;
    String accessToken;
    @Override
    public boolean handleMessage(Message message) {
        hideProgressDialog();
        switch (message.arg1) {
            case 1: { // 成功
                Toast.makeText(LoginActivity.this, "授权登陆成功", Toast.LENGTH_SHORT).show();
                //获取用户资料
                Platform platform = (Platform) message.obj;
                Log.e("platform", "handleMessage: -->"+platform);
                oAuthId = platform.getDb().getUserId();//获取用户账号
                accessToken = platform.getDb().getToken();
                Log.e("token", "handleMessage: -->"+oAuthId+"...."+accessToken );
                Map<String,Object> params=new HashMap<>();
                params.put("oAuthId",oAuthId);
                params.put("oAuthType","weixin");
                params.put("accessToken",accessToken);
                new  WxLoginAsyncTask().execute(params);
            }
            break;
            case 2: { // 失败
                Toast.makeText(LoginActivity.this, "授权登陆失败", Toast.LENGTH_SHORT).show();
            }
            break;
            case 3: { // 取消
                Toast.makeText(LoginActivity.this, "授权登陆取消", Toast.LENGTH_SHORT).show();
            }
            break;
        }
        return false;
    }


    int isFirst=-1;
    int ThirduserId;
    class WxLoginAsyncTask extends AsyncTask<Map<String, Object>, Void, Integer> {

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code = 0;
            Map<String,Object> params=maps[0];
            String url = ip+"/login/auth/third";
            String result = HttpUtils.postOkHpptRequest(url,params);
            try {
                if (!Utils.isEmpty(result)) {
                    Log.e("result", "doInBackground: -->"+result );
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                     isFirst = returnData.getInt("isFirst");
                     ThirduserId = returnData.getInt("userId");
                    String token = returnData.getString("token");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", token);
                    editor.putInt("ThirduserId", ThirduserId);
                    editor.commit();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code) {

                case 100:
                   if (isFirst==1){
                       Intent intent = new Intent(LoginActivity.this,ThirdLoginActivity.class);
                       intent.putExtra("userId",ThirduserId);
                       startActivity(intent);
                   }else {
                       userId= String.valueOf(ThirduserId);
                       new hourseAsyncTask().execute();
                   }
                    break;
            }
        }
    }

    //显示dialog
    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //隐藏dialog
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeAllActivity();//**退出主页面*//*
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    String userId;

    class LoginAsyncTask extends AsyncTask<Map<String, Object>, Void, Integer> {
        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {


            int code = 0;
            Map<String, Object> params = maps[0];
            String result = HttpUtils.postOkHpptRequest(url, params);
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    if (code == 100) {
                        String username=returnData.getString("username");
                        userId = returnData.getString("userId");
                        String token = returnData.getString("token");
                        SharedPreferences.Editor editor = preferences.edit();
                        Log.i("phone", "---->: " + phone + ",,,," + password);
                        editor.putString("phone", phone);
                        editor.putString("username", returnData.getString("username"));
                        editor.putString("userId", userId);
                        editor.putString("password", password);
                        editor.putString("token", token);
                        editor.putString("username",username);
                        boolean success=editor.commit();
                        if (success){
                            new hourseAsyncTask().execute();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }


        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code) {
                case 10002:
                    Utils.showToast(LoginActivity.this, "手机号码未注册");
                    hideProgressDialog();
                    break;
                case 10004:
                    Utils.showToast(LoginActivity.this, "用户名或密码错误");
                    et_pswd.setText("");
                    hideProgressDialog();
                    break;
                case 100:
                    hideProgressDialog();
                    break;
                    default:
                        hideProgressDialog();
                        break;
            }
        }
    }
    long Id = -1;
    int img[] = {R.mipmap.t};

    class hourseAsyncTask extends AsyncTask<Map<String, Object>, Void, Integer> {

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code = 0;
//            Map<String, Object> params = maps[0];
            String url = ip + "/family/house/getHouseDeviceByUser?userId=" + userId;
            String result = HttpUtils.getOkHpptRequest(url);
            Log.i("ffffffff", "--->: " + result);
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("returnCode");
                    Log.i("fffffffft", "--->: " + code);
                    if (code == 100) {
                        hourseDao.deleteAll();
                        roomDao.deleteAll();
                        deviceChildDao.deleteAll();
                        JSONObject returnData=jsonObject.getJSONObject("returnData");
                        SharedPreferences.Editor editor=preferences.edit();
                        boolean sex=returnData.getBoolean("sex");
                        if (returnData.has("headImgUrl")){
                            String headImgUrl=returnData.getString("headImgUrl");
                            editor.putString("headImgUrl",headImgUrl);
                        }
                        String userId3=returnData.getString("userId");
                        String username=returnData.getString("userName");
                        String phone=returnData.getString("phone");
                        String birthday=returnData.getString("birthday");
                        editor.putString("username",username);
                        editor.putString("userId",userId3);
                        editor.putString("phone",phone);
                        if (!TextUtils.isEmpty(birthday)){
                            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                            Date date=format.parse(birthday);
                            long ts = date.getTime();
                            String res = String.valueOf(ts);
                            editor.putString("birthday",res);
                        }else {
                            editor.putString("birthday","");
                        }
                        editor.putBoolean("sex",sex);
                        editor.commit();
                        JSONArray houseDevices = returnData.getJSONArray("houseDevices");
                        for (int i = 0; i < houseDevices.length(); i++) {
                            JSONObject houseObject = houseDevices.getJSONObject(i);
                            int id = houseObject.getInt("id");
                            Log.i("rrrr", "doInBackground:--> " + id);
                            String houseName = houseObject.getString("houseName");
                            String houseAddress = houseObject.getString("houseAddress");
                            int userId = houseObject.getInt("userId");
//                            Hourse hourse = hourseDao.findById((long) id);
//                            if (hourse != null) {
//                                hourse.setHouseName(houseName);
//                                hourse.setHouseAddress(houseAddress);
//                                hourse.setUserId(userId);
//                                hourseDao.update(hourse);
//                            } else {
                              Hourse  hourse = new Hourse((long) id, houseName, houseAddress, userId);
                                hourseDao.insert(hourse);
                                Log.i("dddddd1", "doInBackground:---> " + hourse);
//                            }
                            JSONArray roomDevices = houseObject.getJSONArray("roomDevices");

                            Log.i("dddddd11qqq1", "doInBackground:---> " + roomDevices.length());
                            for (int j = 0; j < roomDevices.length(); j++) {
                                JSONObject roomObject = roomDevices.getJSONObject(j);
                                int roomId = roomObject.getInt("roomId");
                                Id = roomId;
                                String roomName = roomObject.getString("roomName");
                                int houseId = roomObject.getInt("houseId");
                                String roomType = roomObject.getString("roomType");

                                Room room = new Room((long) roomId, roomName, houseId, roomType, 0,"");
                                Log.i("dddddd11qqq1", "doInBackground:---> " + room.getRoomName() + "," + room.getRoomType());
                                roomDao.insert(room);

                                JSONArray deviceList = roomObject.getJSONArray("deviceList");
                                for (int k = 0; k < deviceList.length(); k++) {
                                    JSONObject device = deviceList.getJSONObject(k);
                                    int deviceId = device.getInt("deviceId");
                                    String deviceName = device.getString("deviceName");
                                    int deviceType = device.getInt("deviceType");
                                    String deviceMacAddress = device.getString("deviceMacAddress");
                                    int houseId2 = device.getInt("houseId");
                                    int userId2 = device.getInt("userId");
                                    int roomId2 = device.getInt("roomId");
                                    int deviceUsedCount = device.getInt("deviceUsedCount");
                                    DeviceChild deviceChild = new DeviceChild((long) houseId2, (long) roomId2, deviceUsedCount, deviceType, deviceMacAddress, deviceName, userId2);
                                    deviceChild.setDeviceId(deviceId);
                                    deviceChild.setImg(img[0]);
                                    deviceChildDao.insert(deviceChild);
                                }
                            }
//                            JSONArray deviceCommons=houseObject.getJSONArray("deviceCommons");
//                            for (int j = 0; j < deviceCommons.length(); j++) {
//                                JSONObject jsonObject3=deviceCommons.getJSONObject(j);
//                                int deviceId=jsonObject3.getInt("deviceId");
//                                String deviceName=jsonObject3.getString("deviceName");
//                                int deviceType=jsonObject3.getInt("deviceType");
//                                int roomId=jsonObject3.getInt("roomId");
//                                String roomName=jsonObject3.getString("roomName");
//                                String deviceMacAddress=jsonObject3.getString("deviceMacAddress");
////                                List<DeviceChild> deviceChildren=deviceChildDao.findAllDevice();
//                                DeviceChild deviceChild2=deviceChildDao.findDeviceByMacAddress2(deviceMacAddress);
////                                for (DeviceChild deviceChild:deviceChildren){
////                                    int deviceId2=deviceChild.getDeviceId();
////                                    if (deviceId2==deviceId){
////                                        deviceChild2=deviceChild;
////                                        break;
////                                    }
////                                }
//                                if (deviceChild2!=null){
//                                    deviceChild2.setRoomName(roomName);
//                                    deviceChildDao.update(deviceChild2);
//                                }else if (deviceChild2==null){
////                                    if (deviceChild2==null){
//                                        deviceChild2=new DeviceChild();
//                                        deviceChild2.setUserId(userId);
//                                        deviceChild2.setName(deviceName);
//                                        deviceChild2.setDeviceId(deviceId);
//                                        deviceChild2.setMacAddress(deviceMacAddress);
//                                        deviceChild2.setType(deviceType);
//                                        deviceChild2.setRoomId(roomId);
//                                        deviceChild2.setRoomName(roomName);
//                                        deviceChildDao.insert(deviceChild2);
////                                    }
//                                }
//                            }

                            JSONArray deviceShareds=houseObject.getJSONArray("deviceShareds");
                            for (int x = 0; x < deviceShareds.length(); x++) {
                                JSONObject jsonObject2=deviceShareds.getJSONObject(x);
                                int deviceId=jsonObject2.getInt("deviceId");
                                String deviceName=jsonObject2.getString("deviceName");
                                int deviceType=jsonObject2.getInt("deviceType");
                                int roomId=jsonObject2.getInt("roomId");
                                String deviceMacAddress=jsonObject2.getString("deviceMacAddress");
//                                List<DeviceChild> deviceChildren=deviceChildDao.findAllDevice();
                                DeviceChild deviceChild2=deviceChildDao.findDeviceByMacAddress2(deviceMacAddress);
//                                for (DeviceChild deviceChild:deviceChildren){
//                                    int deviceId2=deviceChild.getDeviceId();
//                                    if (deviceId2==deviceId){
//                                        deviceChild2=deviceChild;
//                                        break;
//                                    }
//                                }
                               if (deviceChild2!=null){
                                   deviceChildDao.update(deviceChild2);
                               }else if (deviceChild2==null){
//                                   if (deviceChild2==null){
                                       deviceChild2=new DeviceChild();
                                       deviceChild2.setUserId(userId);
                                       deviceChild2.setShareId(Long.MAX_VALUE);
                                       deviceChild2.setName(deviceName);
                                       deviceChild2.setDeviceId(deviceId);
                                       deviceChild2.setMacAddress(deviceMacAddress);
                                       deviceChild2.setType(deviceType);
                                       deviceChild2.setRoomId(roomId);
                                       deviceChild2.setShare("share");
                                       deviceChildDao.insert(deviceChild2);
//                                   }
                               }

                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code) {
                case 1005:
                    Utils.showToast(LoginActivity.this, "查询失败");
                    break;
                case 100:
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    if (mPositionPreferences.contains("position")){
                        mPositionPreferences.edit().clear().commit();
                    }
                    if (TextUtils.isEmpty(login)){
                        Utils.showToast(LoginActivity.this, "登录成功");
                        intent.putExtra("login0","login0");
                    }
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("load","load");
                    intent.putExtra("login","login");
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
