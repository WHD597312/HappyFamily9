package com.xr.happyFamily.login.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.xr.database.dao.HourseDao;
import com.xr.database.dao.RoomDao;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
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
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;

public class LoginActivity extends AppCompatActivity {

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
    int firstClick=1;
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
//        if (preferences.contains("phone") && preferences.contains("password")){
////            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(new Intent(this,MainActivity.class));
//        }
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

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_pswd, R.id.image_seepwd, R.id.image_wx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(this, RegistActivity.class));
                break;
            case R.id.btn_login:
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
               if (firstClick==1){
                    
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
                        String username=returnData.getString("username");
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
                        editor.putString("username",username);
                        boolean success=editor.commit();

                        if (success){
                            Map<String,Object> params2=new HashMap<>();
                            params2.put("userId",userId);
                            new LoadUserAsync().execute(params2);
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
                    firstClick=1;
                    break;
                case 10004:
                    Utils.showToast(LoginActivity.this, "用户名或密码错误");
                    firstClick=1;
                    et_pswd.setText("");
                    break;
                case 100:
                    break;
            }
        }
    }

    class LoadUserAsync extends AsyncTask<Map<String,Object>,Void,Void>{

        @Override
        protected Void doInBackground(Map<String, Object>... maps) {
            Map<String,Object> params=maps[0];
            String url=HttpUtils.ipAddress+"/login/getInfo";
            String result=HttpUtils.postOkHpptRequest(url,params);
            try {
                Log.i("result","-->"+result);
                if (!TextUtils.isEmpty(result)){
                    JSONObject jsonObject=new JSONObject(result);
                    SharedPreferences.Editor editor=preferences.edit();
                    boolean sex=jsonObject.getBoolean("sex");
                    if (jsonObject.has("headImgUrl")){
                        String headImgUrl=jsonObject.getString("headImgUrl");
                        editor.putString("headImgUrl",headImgUrl);
                    }
                    String birthday=jsonObject.getString("birthday");
                    editor.putBoolean("sex",sex);
                    editor.putString("birthday",birthday);
                    boolean success=editor.commit();
                    if (success){
                        new hourseAsyncTask().execute();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
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
                            JSONArray deviceShareds=houseObject.getJSONArray("deviceShareds");
                            for (int x = 0; x < deviceShareds.length(); x++) {
                                JSONObject jsonObject2=deviceShareds.getJSONObject(x);
                                int deviceId=jsonObject2.getInt("deviceId");
                                String deviceName=jsonObject2.getString("deviceName");
                                int deviceType=jsonObject2.getInt("deviceType");
                                int roomId=jsonObject2.getInt("roomId");
                                String deviceMacAddress=jsonObject2.getString("deviceMacAddress");
                                List<DeviceChild> deviceChildren=deviceChildDao.findShareDevice(userId);
                                DeviceChild deviceChild2=null;
                                for (DeviceChild deviceChild:deviceChildren){
                                    int deviceId2=deviceChild.getDeviceId();
                                    if (deviceId2==deviceId){
                                        deviceChild2=deviceChild;
                                        break;
                                    }
                                }
                               if (deviceChild2!=null){
                                   deviceChildDao.update(deviceChild2);
                               }else if (deviceChild2==null){
                                   if (deviceChild2==null){
                                       deviceChild2=new DeviceChild();
                                       deviceChild2.setUserId(userId);
                                       deviceChild2.setShareId(Long.MAX_VALUE);
                                       deviceChild2.setName(deviceName);
                                       deviceChild2.setDeviceId(deviceId);
                                       deviceChild2.setMacAddress(deviceMacAddress);
                                       deviceChild2.setType(deviceType);
                                       deviceChild2.setRoomId(roomId);
                                       deviceChildDao.insert(deviceChild2);
                                   }
                               }

                            }
                        }
//                        JSONArray deviceShareds=jsonObject.getJSONArray("deviceCommons");

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
