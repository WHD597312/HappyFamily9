package com.xr.happyFamily.login.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.http.NetWorkUtil;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class qdActivity extends Activity {
    String str;
    SharedPreferences preferences;
    private HourseDaoImpl hourseDao;
    private RoomDaoImpl roomDao;
    private DeviceChildDaoImpl deviceChildDao;
    String phone;
    String userId;
    String password;
    String url = "http://47.98.131.11:8084/login/auth";
    String ip = "http://47.98.131.11:8084";
    SharedPreferences mPositionPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdy);
         str=new qdActivity().getShared("tag", qdActivity.this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        hourseDao = new HourseDaoImpl(getApplicationContext());
        roomDao = new RoomDaoImpl(getApplicationContext());
        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());

        Integer time = 2000;  //设置等待时间，单位为毫秒
        mPositionPreferences=getSharedPreferences("position", Context.MODE_PRIVATE);
        Handler handler = new Handler();
        //当计时结束时，跳转至主界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(str.equals("")){
                    startActivity(new Intent(qdActivity.this, GuideActivity.class));

                }else {
                    if (preferences.contains("phone") && preferences.contains("password")) {
                         phone=preferences.getString("phone","");
                         password=preferences.getString("password","");
                        Map<String, Object> params = new HashMap<>();
                        params.put("phone", phone);
                        params.put("password", password);

                        if (NetWorkUtil.isConn(qdActivity.this)){
                            new LoginAsyncTask().execute(params);
                        }else {
                            Intent intent=new Intent(qdActivity.this,MainActivity.class);
                            intent.putExtra("load","load");
                            intent.putExtra("login","login");
                            startActivity(intent);
                        }
                    }else {
                        Intent intent=new Intent(qdActivity.this, LoginActivity.class);
                        startActivity(intent);
                        qdActivity.this.finish();
                    }



                }


            }
        }, time);
    }


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
                    Utils.showToast(qdActivity.this, "手机号码未注册");

                    break;
                case 10004:
                    Utils.showToast(qdActivity.this, "用户名或密码错误");


                    break;
                case 100:

                    break;
                default:

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
                        String birthday=returnData.getString("birthday");
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                        Date date=format.parse(birthday);
                        long ts = date.getTime();
                        String res = String.valueOf(ts);
                        editor.putBoolean("sex",sex);
                        editor.putString("birthday",res);
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
                    Utils.showToast(qdActivity.this, "查询失败");
                    break;
                case 100:
                    Intent intent = new Intent(qdActivity.this, MainActivity.class);
                    mPositionPreferences.edit().clear().commit();

//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("load","load");
                    intent.putExtra("login","login");
                    startActivity(intent);
                    qdActivity.this.finish();
                    break;
            }
        }
    }


    private String name="jianbao";
    private String touxiang = "touxiang";
    /*
     * 保存数据的方法
     * */
    public void saveShared(String key,String value,Context ctx){
        SharedPreferences shared=ctx.getSharedPreferences(name,0);
        SharedPreferences.  Editor edit = shared.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /*
     * 从本地获取数据
     * */
    public String getShared(String key,Context ctx){
        String str=null;
        SharedPreferences shared = ctx.getSharedPreferences(name, 0);
        str = shared.getString(key, "");
        return str;
    }

}