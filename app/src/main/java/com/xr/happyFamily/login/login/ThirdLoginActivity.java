package com.xr.happyFamily.login.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import pl.droidsonroids.gif.GifDrawable;

public class ThirdLoginActivity extends AppCompatActivity {

    private String TAG="RegistActivity";
    MyApplication application;
    Unbinder unbinder;
    @BindView(R.id.et_third_phone)
    EditText et_phone;
    @BindView(R.id.et_third_code)
    EditText et_code;
    @BindView(R.id.et_third_password)
    EditText et_password;
    @BindView(R.id.btn_third_code)
    Button btn_get_code;
    private String url="http://47.98.131.11:8084/user/thirdBindPhone";
    SharedPreferences preferences;
    @BindView(R.id.iv_third_tb)
    ImageView imageView6;
    GifDrawable gifDrawable;
    Context mContext;
    int firstClick = 1;
    String ip = "http://47.98.131.11:8084";
    private DeviceChildDaoImpl deviceChildDao;
    private HourseDaoImpl hourseDao;
    private RoomDaoImpl roomDao;
    SharedPreferences mPositionPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirdlog);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
       unbinder= ButterKnife.bind(this);
        if (application==null){
            application= (MyApplication) getApplication();
        }
        application.addActivity(this);
        mPositionPreferences=getSharedPreferences("position", Context.MODE_PRIVATE);
        deviceChildDao= new DeviceChildDaoImpl(getApplicationContext());
        hourseDao = new HourseDaoImpl(getApplicationContext());
        roomDao = new RoomDaoImpl(getApplicationContext());
        //图标动画
        try {
            gifDrawable=new GifDrawable(getResources(),R.mipmap.dtubiao);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (gifDrawable!=null){
            gifDrawable.start();
            imageView6.setImageDrawable(gifDrawable);
        }
        Intent intent= getIntent();
         userId =  intent.getIntExtra("userId",0);
    }
    int userId;

    @Override
    protected void onStart() {
        super.onStart();
        preferences=getSharedPreferences("my",MODE_PRIVATE);
        SMSSDK.registerEventHandler(eventHandler);

    }
    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            super.afterEvent(event, result, data);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    @SuppressWarnings("unchecked") HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    Log.d(TAG, "提交验证码成功--country=" + country + "--phone" + phone);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    Log.d(TAG, "获取验证码成功");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }
    };
    @OnClick({R.id.btn_third_finish,R.id.btn_third_code,})
    public void onClick(View view){
        switch (view.getId()){

            case R.id.btn_third_finish:
                if (firstClick==1){
                    String phone2 = et_phone.getText().toString().trim();
                    String code=et_code.getText().toString().trim();
                    String password=et_password.getText().toString().trim();
                    if (TextUtils.isEmpty(phone2)){
                        Utils.showToast(this,"手机号码不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(code)){
                        Utils.showToast(this,"请输入验证码");
                        return;
                    }
                    if (TextUtils.isEmpty(password)){
                        Utils.showToast(this,"请输入密码");
                        return;
                    }
                    Map<String,Object> params=new HashMap<>();
                    params.put("phone",phone2);
                    params.put("code",code);
                    params.put("password",password);
                    params.put("userId",userId);
                    new RegistAsyncTask().execute(params);
//                new getShopAsync().execute(params);
                    firstClick=0;
                }

                break;
            case R.id.btn_third_code:
                String phone = et_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Utils.showToast(this,"手机号码不能为空");
                } else {
                    SMSSDK.getVerificationCode("86", phone);
                    CountTimer countTimer=new CountTimer(60000,1000);
                    countTimer.start();
                }
                break;
        }
    }

    class RegistAsyncTask extends AsyncTask<Map<String,Object>,Void,String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String, Object> params = maps[0];
            String result = HttpUtils.postOkHpptRequest(url, params);
            Log.i("back", "--->"+result);
            if (!Utils.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                   if ("100".equals(code)){
                       new hourseAsyncTask().execute();
                   }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return code;

        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s) {
                case "10003":
                    Utils.showToast(ThirdLoginActivity.this, "手机验证码错误，请重试");
                    break;
                case "10001":
                    Utils.showToast(ThirdLoginActivity.this, "账户已存在");
                    break;
                case "100":
                    Utils.showToast(ThirdLoginActivity.this, "创建成功");
                    SharedPreferences.Editor editor = preferences.edit();
                    String phone = et_phone.getText().toString().trim();
                    String password = et_password.getText().toString().trim();
                    editor.putString("phone", phone);
                    editor.putString("password", password);
                    if (editor.commit()) {
                        Intent intent = new Intent(ThirdLoginActivity.this, MainActivity.class);
                        intent.putExtra("phone",phone);
                        intent.putExtra("password",password);
                        startActivity(intent);
                    }
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
                            JSONArray deviceCommons=houseObject.getJSONArray("deviceCommons");
                            for (int j = 0; j < deviceCommons.length(); j++) {
                                JSONObject jsonObject3=deviceCommons.getJSONObject(j);
                                int deviceId=jsonObject3.getInt("deviceId");
                                String deviceName=jsonObject3.getString("deviceName");
                                int deviceType=jsonObject3.getInt("deviceType");
                                int roomId=jsonObject3.getInt("roomId");
                                String roomName=jsonObject3.getString("roomName");
                                String deviceMacAddress=jsonObject3.getString("deviceMacAddress");
                                List<DeviceChild> deviceChildren=deviceChildDao.findAllDevice();
                                DeviceChild deviceChild2=null;
                                for (DeviceChild deviceChild:deviceChildren){
                                    int deviceId2=deviceChild.getDeviceId();
                                    if (deviceId2==deviceId){
                                        deviceChild2=deviceChild;
                                        break;
                                    }
                                }
                                if (deviceChild2!=null){
                                    deviceChild2.setRoomName(roomName);
                                    deviceChildDao.update(deviceChild2);
                                }else if (deviceChild2==null){
                                    if (deviceChild2==null){
                                        deviceChild2=new DeviceChild();
                                        deviceChild2.setUserId(userId);
                                        deviceChild2.setName(deviceName);
                                        deviceChild2.setDeviceId(deviceId);
                                        deviceChild2.setMacAddress(deviceMacAddress);
                                        deviceChild2.setType(deviceType);
                                        deviceChild2.setRoomId(roomId);
                                        deviceChild2.setRoomName(roomName);
                                        deviceChildDao.insert(deviceChild2);
                                    }
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
                                List<DeviceChild> deviceChildren=deviceChildDao.findAllDevice();
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
                                        deviceChild2.setShare("share");
                                        deviceChildDao.insert(deviceChild2);
                                    }
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
                    Utils.showToast(ThirdLoginActivity.this, "查询失败");
                    break;
                case 100:
                    Intent intent = new Intent(ThirdLoginActivity.this, MainActivity.class);
                    if (mPositionPreferences.contains("position")){
                        mPositionPreferences.edit().clear().commit();
                    }

                    Utils.showToast(ThirdLoginActivity.this, "登录成功");
                    intent.putExtra("login0","login0");

                    intent.putExtra("load","load");
                    intent.putExtra("login","login");
                    startActivity(intent);
                    break;
            }
        }
    }




//        class getShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
//            @Override
//            protected String doInBackground(Map<String, Object>... maps) {
//                Map<String, Object> params = maps[0];
//                String result = HttpUtils.myPostOkHpptRequest(mContext, url, params);
//                String code = "";
//                Log.e("qqqqqqqXQ",result);
//                try {
//                    if (!Utils.isEmpty(result)) {
//                        JSONObject jsonObject = new JSONObject(result);
//                        code = jsonObject.getString("returnCode");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return code;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
////                if (!Utils.isEmpty(s) && "100".equals(s)) {
////                    Toast.makeText(mContext, "编辑地址成功", Toast.LENGTH_SHORT).show();
////
////                }
//                switch (s){
//                    case "10003":
//                        Utils.showToast(RegistActivity.this,"手机验证码错误，请重试");
//                        break;
//                    case "10001":
//                        Utils.showToast(RegistActivity.this,"手机帐号已被注册");
//                        break;
//                    case "100":
//                        Utils.showToast(RegistActivity.this,"创建成功");
//                        SharedPreferences.Editor editor=preferences.edit();
//                        String phone = et_phone.getText().toString().trim();
//                        String password=et_password.getText().toString().trim();
//
//                        editor.putString("phone",phone);
//                        editor.putString("password",password);
//                        if (editor.commit()){
//                            Intent intent=new Intent(RegistActivity.this,RegistFinishActivity.class);
//                            startActivity(intent);
//                        }
//                        break;
//                }
//            }
//        }
//    }

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
            Log.e("Tag", "倒计时=" + (millisUntilFinished/1000));
            if (btn_get_code!=null){
                btn_get_code.setText(millisUntilFinished / 1000 + "s");
                //设置倒计时中的按钮外观
                btn_get_code.setClickable(false);//倒计时过程中将按钮设置为不可点击
//            btn_get_code.setBackgroundColor(Color.parseColor("#c7c7c7"));
                btn_get_code.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.darker_gray));
                btn_get_code.setTextSize(16);
            }
        }

        /**
         * 倒计时完成后调用
         */
        @Override
        public void onFinish() {
            Log.e("Tag", "倒计时完成");
            //设置倒计时结束之后的按钮样式
//            btn_get_code.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_light));
//            btn_get_code.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
            if (btn_get_code!=null){
                btn_get_code.setTextSize(18);
                btn_get_code.setText("重新发送");
                btn_get_code.setClickable(true);
            }

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}

