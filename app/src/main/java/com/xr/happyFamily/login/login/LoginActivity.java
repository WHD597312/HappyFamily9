package com.xr.happyFamily.login.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mob.tools.utils.UIHandler;
import com.tencent.connect.auth.QQToken;
import com.xr.database.dao.HourseDao;
import com.xr.database.dao.RoomDao;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.HomepageActivity;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.MyPaperActivity;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.login.rigest.ForgetPswdActivity;
import com.xr.happyFamily.login.rigest.RegistActivity;

import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Mobile;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import pl.droidsonroids.gif.GifDrawable;

import static android.R.attr.action;

public class LoginActivity extends AppCompatActivity implements PlatformActionListener, Handler.Callback, View.OnClickListener {

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
    int firstClick = 1;
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

    @Override
    protected void onStart() {
        super.onStart();
        try {
            gifDrawable = new GifDrawable(getResources(), R.mipmap.dtubiao);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (gifDrawable != null) {
            gifDrawable.start();
            imageView6.setImageDrawable(gifDrawable);
        }
        if (preferences.contains("phone")) {
            String phone = preferences.getString("phone", "");
            et_name.setText(phone);
            et_pswd.setText("");
        }
        if (preferences.contains("phone") && preferences.contains("password")) {
            String phone = preferences.getString("phone", "");
            String password = preferences.getString("password", "");
            et_name.setText(phone);
            et_pswd.setText(password);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.contains("phone") && preferences.contains("password")) {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            if (preferences.contains("my")) {
                startActivity(new Intent(this, HomepageActivity.class));
            }
        }
    }

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_pswd, R.id.image_seepwd, R.id.image_wx,R.id.image_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(this, RegistActivity.class));
                break;
            case R.id.btn_login:

                if (firstClick==1){
                    String phone = et_name.getText().toString().trim();
                    String password = et_pswd.getText().toString().trim();
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

                    Map<String, Object> params = new HashMap<>();
                    params.put("phone", phone);
                    params.put("password", password);
                    new LoginAsyncTask().execute(params);
                    firstClick=0;
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
                    imageView.setImageResource(R.mipmap.yanjing1);
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
            case R.id.image_qq:
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(this);
                qq.SSOSetting(false);
                authorize(qq, 2);
                break;
        }
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
                        userId = returnData.getString("userId");
                        String token = returnData.getString("token");
                        SharedPreferences.Editor editor = preferences.edit();
                        String phone = et_name.getText().toString().trim();
                        String password = et_pswd.getText().toString().trim();
                        Log.i("phone", "---->: " + phone + ",,,," + password);
                        editor.putString("phone", phone);
                        editor.putString("userId", userId);
                        editor.putString("password", password);
                        editor.putString("token", token);
                        editor.commit();
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
                    break;
                case 10004:
                    Utils.showToast(LoginActivity.this, "用户名或密码错误");
                    et_pswd.setText("");
                    break;
                case 100:

                    new hourseAsyncTask().execute();

                    //每次登陆后清除上次登录信息里闹钟的数据
                    ClockDaoImpl clockBeanDao=new ClockDaoImpl(LoginActivity.this.getApplicationContext());
                    UserInfosDaoImpl userInfosDao=new UserInfosDaoImpl(LoginActivity.this.getApplicationContext());
                    clockBeanDao.deleteAll();
                    userInfosDao.deleteAll();

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
//                    JSONObject roomDevices = jsonObject.getJSONObject("roomDevices");

                    if (code == 100) {
                        hourseDao.deleteAll();
                        roomDao.deleteAll();
                        deviceChildDao.deleteAll();
                        JSONArray returnData = jsonObject.getJSONArray("returnData");
                        for (int i = 0; i < returnData.length(); i++) {
                            JSONObject houseObject = returnData.getJSONObject(i);
                            int id = houseObject.getInt("id");
                            Log.i("rrrr", "doInBackground:--> " + id);
                            String houseName = houseObject.getString("houseName");
                            String houseAddress = houseObject.getString("houseAddress");
                            int userId = houseObject.getInt("userId");
                            Hourse hourse = hourseDao.findById((long) id);
                            if (hourse != null) {
                                hourse.setHouseName(houseName);
                                hourse.setHouseAddress(houseAddress);
                                hourse.setUserId(userId);
                                hourseDao.update(hourse);
                            } else {
                                hourse = new Hourse((long) id, houseName, houseAddress, userId);
                                hourseDao.insert(hourse);
                                Log.i("dddddd1", "doInBackground:---> " + hourse);
                            }
                            JSONArray roomDevices = houseObject.getJSONArray("roomDevices");

                            Log.i("dddddd11qqq1", "doInBackground:---> " + roomDevices.length());
                            for (int j = 0; j < roomDevices.length(); j++) {
                                JSONObject roomObject = roomDevices.getJSONObject(j);
                                int roomId = roomObject.getInt("roomId");
                                Id = roomId;
                                String roomName = roomObject.getString("roomName");
                                int houseId = roomObject.getInt("houseId");
                                String roomType = roomObject.getString("roomType");

                                Room room = new Room((long) roomId, roomName, houseId, roomType, 0);
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
                        }
//                        JSONArray roomDevices=returnData.getJSONArray("roomDevices");


//                        id = returnData.getInt("id");
//                        String houseAddress = returnData.getString("houseAddress");
//                        String houseName = returnData.getString("houseName");
////                        Integer userId = returnData.getInt("userId");
////                        Integer roomId = roomDevices.getInt("roomId");
////                        SharedPreferences.Editor editor = preferences.edit();
////                        editor.putInt("id",
//////                        editor.putString("houseAddress", houseAddress);
//////                        editor.putInt("roomId", roomId);
//////                        editor.putString("houseName", houseName);
//////                        editor.commit();


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
                    if (mPositionPreferences.contains("position")){
                        mPositionPreferences.edit().clear().commit();
                    }
                    Utils.showToast(LoginActivity.this, "请求成功");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;

            }
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
    @Override
    public boolean handleMessage(Message message) {
        hideProgressDialog();
        switch (message.arg1) {
            case 1: { // 成功
                Toast.makeText(LoginActivity.this, "授权登陆成功", Toast.LENGTH_SHORT).show();

                //获取用户资料
                Platform platform = (Platform) message.obj;
                String userId = platform.getDb().getUserId();//获取用户账号
                String userName = platform.getDb().getUserName();//获取用户名字
                String userIcon = platform.getDb().getUserIcon();//获取用户头像
                String userGender = platform.getDb().getUserGender(); //获取用户性别，m = 男, f = 女，如果微信没有设置性别,默认返回null
                Toast.makeText(LoginActivity.this, "用户信息为--用户名：" + userName + "  性别：" + userGender, Toast.LENGTH_SHORT).show();

                //下面就可以利用获取的用户信息登录自己的服务器或者做自己想做的事啦!
                //。。。

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

    //显示dialog
    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    //隐藏dialog
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
