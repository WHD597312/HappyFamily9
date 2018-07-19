package com.xr.happyFamily.main;

import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.le.BtClock.bjTimeActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.BitmapCompressUtils;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.ClockService;
import com.xr.happyFamily.together.util.mqtt.MQService;
import com.xr.happyFamily.together.util.receiver.MQTTMessageReveiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements FamilyFragmentManager.CallValueValue {

    Unbinder unbinder;
    FragmentManager fragmentManager;
    @BindView(R.id.layout_body)
    LinearLayout layoutBody;
    @BindView(R.id.id_bto_jia)
    LinearLayout idBtoJia;
    @BindView(R.id.id_bto_le_img)
    ImageButton idBtoLeImg;
    @BindView(R.id.id_bto_le)
    LinearLayout idBtoLe;
    @BindView(R.id.id_bto_bao)
    LinearLayout idBtoBao;
    @BindView(R.id.id_bto_zhen)
    LinearLayout idBtoZhen;
    private HourseDaoImpl hourseDao;
    private RoomDaoImpl roomDao;
    SharedPreferences mPositionPreferences;
    @BindView(R.id.layout_bottom)
    LinearLayout layout_bottom;
    @BindView(R.id.id_bto_jia_img)
    ImageButton id_bto_jia_img;
    @BindView(R.id.id_bto_bao_img)
    ImageButton id_bto_bao_img;
    @BindView(R.id.id_bto_zhen_img)
    ImageButton id_bto_zhen_img;/**朕*/

    private FamilyFragmentManager familyFragmentManager;
    private BaoFragment baoFragment;/**宝的页面*/
    private ZhenFragment zhenFragment;/**朕的页面*/
    private LeFragment leFragment;
    private MyApplication application;
    SharedPreferences preferences;
    private DeviceChildDaoImpl deviceChildDao;

    //其他activity跳转回主界面时的标记
    private String sign = "0";
    private long houseId;
    String share;
    private MQTTMessageReveiver myReceiver;
    private  boolean isBound;
    private  boolean clockisBound;
    String load;
    AlarmManager alarmManager;
    private String family;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main_activity);
        unbinder = ButterKnife.bind(this);
        if (application == null) {
            application = (MyApplication) getApplication();
            application.addActivity(this);
        }

        roomDao=new RoomDaoImpl(getApplicationContext());
        deviceChildDao=new DeviceChildDaoImpl(getApplicationContext());

        preferences = getSharedPreferences("my", MODE_PRIVATE);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("mqttmessage2");
        myReceiver = new MQTTMessageReveiver();
        this.registerReceiver(myReceiver, filter);

        fragmentManager = getSupportFragmentManager();
        hourseDao = new HourseDaoImpl(getApplicationContext());


        UserInfosDaoImpl userInfosDao = new UserInfosDaoImpl(getApplicationContext());
        ClockDaoImpl clockBeanDao = new ClockDaoImpl(getApplicationContext());
        clockBeanDao.deleteAll();
        userInfosDao.deleteAll();

        List<Hourse> hourses = hourseDao.findAllHouse();
        Intent intent = getIntent();
        load=intent.getStringExtra("load");
        String login=intent.getStringExtra("login");
        share=intent.getStringExtra("share");

        houseId = intent.getLongExtra("houseId", 0);
        if (houseId == 0 && hourses.size()>0) {
            Hourse hourse = hourses.get(0);
            houseId = hourse.getHouseId();
        }

        mPositionPreferences = getSharedPreferences("position", Context.MODE_PRIVATE);
        sign = intent.getStringExtra("sign");
        //从支付成功跳回主界面时，打开商城fragment
        if ("PaySuccess".equals(sign)) {
            id_bto_jia_img.setImageResource(R.mipmap.jia);
            id_bto_bao_img.setImageResource(R.mipmap.bao1);
            FragmentTransaction baoTransaction = fragmentManager.beginTransaction();
            baoTransaction.replace(R.id.layout_body, baoFragment);
            baoTransaction.commit();
            if (mPositionPreferences.contains("position")) {
                mPositionPreferences.edit().clear().commit();
            }
            family="";
        }else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            familyFragmentManager = new FamilyFragmentManager();
            leFragment = new LeFragment();
            baoFragment = new BaoFragment();
            zhenFragment=new ZhenFragment();
            Bundle bundle = new Bundle();
            bundle.putString("load","load");
            bundle.putLong("houseId", houseId);
            familyFragmentManager.setArguments(bundle);
            fragmentTransaction.replace(R.id.layout_body, familyFragmentManager);
            fragmentTransaction.commit();
        }
        if (!preferences.contains("image")){
            if (preferences.contains("headImgUrl")){
                new LoadUserImageAsync().execute();
            }
        }

//        if (TextUtils.isEmpty(sign) && TextUtils.isEmpty(login)){
//            new hourseAsyncTask().execute();
//        }
        //启动闹铃服务根据是否在倒计时判断是否开启服务
//        preferencesclock = getSharedPreferences("trueCount", MODE_MULTI_PROCESS);
//            Boolean trueCount =preferencesclock.getBoolean("trueCount",false);
//            Log.i("Boolean","-->"+trueCount);
//            if(false==trueCount){
            clockintent = new Intent(MainActivity.this, ClockService.class);
            startService(clockintent);
            clockisBound = bindService(clockintent, clockconnection, Context.BIND_AUTO_CREATE);
//        }
    }
    
    SharedPreferences preferencesclock;
    Intent clockintent;
    ClockService clcokservice;
    boolean boundclock;
    ServiceConnection clockconnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ClockService.LocalBinder binder = (ClockService.LocalBinder) service;
            clcokservice = binder.getService();
            clcokservice.acquireWakeLock(MainActivity.this);
            boundclock = true;
            clcokservice.startClock();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


//    MQService mqService;
//    private boolean bound=false;
//    ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            MQService.LocalBinder binder = (MQService.LocalBinder) service;
//            mqService = binder.getService();
//            bound = true;
//            List<DeviceChild> deviceChildren = deviceChildDao.findAllDevice();
//            for (DeviceChild deviceChild : deviceChildren) {
//                String macAddress = deviceChild.getMacAddress();
//                int type=deviceChild.getType();
//                String onlineTopicName="";
//                String offlineTopicName="";
//                switch (type){
//                    case 2:
//                        onlineTopicName = "p99/warmer/" + macAddress + "/transfer";
//                        offlineTopicName="p99/warmer/"+macAddress+"/lwt";
//                        mqService.subscribe(onlineTopicName,1);
//                        mqService.subscribe(offlineTopicName,1);
//                        break;
//                }
//
//            }
//        }
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            bound = false;
//        }
//    };
    class LoadAync extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("family","-->"+family);
        if (!TextUtils.isEmpty(family)){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            familyFragmentManager = new FamilyFragmentManager();
            leFragment = new LeFragment();
            baoFragment = new BaoFragment();
            zhenFragment=new ZhenFragment();
            Bundle bundle = new Bundle();
            bundle.putString("load","load");
            bundle.putLong("houseId", houseId);
            familyFragmentManager.setArguments(bundle);
            fragmentTransaction.replace(R.id.layout_body, familyFragmentManager);
            fragmentTransaction.commit();
            family="family";
        }
        if (TextUtils.isEmpty(share)){
            Intent service=new Intent(this,MQService.class);
            startService(service);
        }
    }

    @OnClick({R.id.id_bto_jia, R.id.id_bto_bao,R.id.id_bto_le,R.id.id_bto_zhen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_bto_jia:
                if (mPositionPreferences.contains("position")) {
                    mPositionPreferences.edit().clear().commit();
                }
                List<Hourse> hourses = hourseDao.findAllHouse();
                Hourse hourse = hourses.get(0);
                long houseId = hourse.getHouseId();
                Bundle bundle = new Bundle();
                bundle.putLong("houseId", houseId);
                bundle.putString("load","");
                familyFragmentManager = new FamilyFragmentManager();
                familyFragmentManager.setArguments(bundle);
                FragmentTransaction familyTransaction = fragmentManager.beginTransaction();
                familyTransaction.replace(R.id.layout_body, familyFragmentManager);
                familyTransaction.commit();
                id_bto_jia_img.setImageResource(R.mipmap.jia1);
                id_bto_bao_img.setImageResource(R.mipmap.bao);
                id_bto_zhen_img.setImageResource(R.mipmap.zhen);
                idBtoLeImg.setImageResource(R.mipmap.le);
                family="family";
                break;
            case R.id.id_bto_bao:
                id_bto_jia_img.setImageResource(R.mipmap.jia);
                id_bto_bao_img.setImageResource(R.mipmap.bao1);
                id_bto_zhen_img.setImageResource(R.mipmap.zhen);
                idBtoLeImg.setImageResource(R.mipmap.le);
                FragmentTransaction baoTransaction = fragmentManager.beginTransaction();
                baoTransaction.replace(R.id.layout_body, baoFragment);
                baoTransaction.commit();
                if (mPositionPreferences.contains("position")) {
                    mPositionPreferences.edit().clear().commit();
                }
                family="";
                break;
            case R.id.id_bto_le:
                id_bto_jia_img.setImageResource(R.mipmap.jia);
                id_bto_bao_img.setImageResource(R.mipmap.bao);
                idBtoLeImg.setImageResource(R.mipmap.le1);
                id_bto_zhen_img.setImageResource(R.mipmap.zhen);
                FragmentTransaction leTransaction = fragmentManager.beginTransaction();
                leTransaction.replace(R.id.layout_body, leFragment);
                leTransaction.commit();
                if (mPositionPreferences.contains("position")) {
                    mPositionPreferences.edit().clear().commit();
                }
                family="";
                break;
            case R.id.id_bto_zhen:
                id_bto_jia_img.setImageResource(R.mipmap.jia);
                id_bto_bao_img.setImageResource(R.mipmap.bao);
                id_bto_zhen_img.setImageResource(R.mipmap.zhen1);
                idBtoLeImg.setImageResource(R.mipmap.le);
                FragmentTransaction zhenTransaction = fragmentManager.beginTransaction();
                zhenTransaction.replace(R.id.layout_body, zhenFragment);
                zhenTransaction.commit();
                if (mPositionPreferences.contains("position")) {
                    mPositionPreferences.edit().clear().commit();
                }
                family="";
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeAllActivity();
            family="";
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        
        Log.e("close", "onDestroy: ---->" );
//        mPositionPreferences.edit().clear().commit();
//        //闹铃 退出将倒计时状态改为f
//        SharedPreferences.Editor editor = preferencesclock.edit();
//        editor.putBoolean("trueCount",false);
//        editor.commit();
        if (myReceiver!=null){
            unregisterReceiver(myReceiver);
        }
        if (clockisBound){
            unbindService(clockconnection);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 6000) {
            long houseId = data.getLongExtra("houseId", 0);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FamilyFragmentManager familyFragmentManager = new FamilyFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putLong("houseId", houseId);
            bundle.putString("load","");
            familyFragmentManager.setArguments(bundle);
            fragmentTransaction.replace(R.id.layout_body, familyFragmentManager);
            fragmentTransaction.commit();
            family="family";
        }
    }

    @Override
    public void setPosition(int position) {
        Log.i("position222", "-->" + position);
        if (position >= 1) {
            SharedPreferences.Editor editor = mPositionPreferences.edit();
            editor.putInt("position", position);
            editor.commit();
            layout_bottom.setVisibility(View.GONE);
        } else if (position == 0) {
            layout_bottom.setVisibility(View.VISIBLE);
        }
    }
    class LoadUserImageAsync extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap bitmap=null;
            try {
                String token = preferences.getString("token", "token");
                String url=preferences.getString("headImgUrl","");
                GlideUrl glideUrl=new GlideUrl(url,new LazyHeaders.Builder().addHeader("authorization",token).build());
                bitmap= Glide.with(MainActivity.this)
                        .load(glideUrl)
                        .asBitmap()
                        .centerCrop()
                        .into(180,180)
                        .get();
                if (bitmap!=null){
                    File file= BitmapCompressUtils.compressImage(bitmap);
                    preferences.edit().putString("image",file.getPath()).commit();
                    BitmapCompressUtils.recycleBitmap(bitmap);

                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    int img[] = {R.mipmap.t};
    class hourseAsyncTask extends AsyncTask<Map<String, Object>, Void, Integer> {

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code = 0;
//            Map<String, Object> params = maps[0];
            String userId3=preferences.getString("userId","");
            String url =HttpUtils.ipAddress + "/family/house/getHouseDeviceByUser?userId=" + userId3;
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
                            JSONArray deviceCommons=houseObject.getJSONArray("deviceCommons");
                            for (int j = 0; j < deviceCommons.length(); j++) {
                                JSONObject jsonObject3=deviceCommons.getJSONObject(j);
                                int deviceId=jsonObject3.getInt("deviceId");
                                String deviceName=jsonObject3.getString("deviceName");
                                int deviceType=jsonObject3.getInt("deviceType");
                                int roomId=jsonObject3.getInt("roomId");
                                String deviceMacAddress=jsonObject3.getString("deviceMacAddress");
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
                    Utils.showToast(MainActivity.this, "加载失败");
                    break;
                case 100:
                    if (mPositionPreferences.contains("position")){
                        mPositionPreferences.edit().clear().commit();
                    }
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    familyFragmentManager = new FamilyFragmentManager();
                    Bundle bundle = new Bundle();
                    List<Hourse> hourses = hourseDao.findAllHouse();
                    Hourse hourse = hourses.get(0);
                    houseId = hourse.getHouseId();
                    bundle.putLong("houseId", houseId);
                    familyFragmentManager.setArguments(bundle);
                    fragmentTransaction.replace(R.id.layout_body, familyFragmentManager);
                    fragmentTransaction.commit();
//                    Intent service = new Intent(MainActivity.this, MQService.class);
//                    startService(service);
//                    isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
                    break;
            }
        }
    }
}
