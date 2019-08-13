package com.xr.happyFamily.together.util.mqtt;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.ArraySet;
import android.util.Log;
import android.view.WindowManager;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.xr.database.dao.ClockBeanDao;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.le.Yougui.UsageStatsWrapper;
import com.xr.happyFamily.le.pojo.AppUsing;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.le.view.QinglvClockDialog;
import com.xr.happyFamily.le.view.QunzuClockDialog;
import com.xr.happyFamily.le.view.btClockjsDialog;
import com.xr.happyFamily.le.view.btClockjsDialog2;
import com.xr.happyFamily.le.view.btClockjsDialog4;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.BitmapCompressUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.os.Process.myUid;


public class ClockService extends Service {

    private TimeDaoImpl timeDao;
    private LocalBinder binder = new LocalBinder();
    private ClockDaoImpl clockDao;
    private UserInfosDaoImpl userInfosDao;
    SharedPreferences preferences;
    SharedPreferences clockPreferences;
    Boolean Ring;
    Time ti;
    ClockBean cl;
    ClockBeanDao clockBeanDao;
    ClockBean clockBean;
    List<ClockBean> clockBeanList;
    String userId;
    private LocationClient mLocationClient;
    private LocationClientOption mOption;
    private PackageManager packageManager;
    private UsageStatsManager usageStatsManager;
    private Context mContext = this;
    private static final int flags = PackageManager.GET_META_DATA |
            PackageManager.GET_SHARED_LIBRARY_FILES |
            PackageManager.GET_UNINSTALLED_PACKAGES;

    JSONArray jsonArray;
    String derailId;
    int derailPo;
     boolean YgRunning ;

    /**
     * 服务启动之后就初始化MQTT,连接MQTT
     */
    @SuppressLint("WrongConstant")
    @Override
    public void onCreate() {
        super.onCreate();
        timeDao = new TimeDaoImpl(this);
        clockDao = new ClockDaoImpl(this);
        preferences = this.getSharedPreferences("trueCount", MODE_MULTI_PROCESS);
        SharedPreferences preferences = getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        derailPo = preferences.getInt("derailPo", -1);
        derailId = preferences.getString("derailId", "");
        Log.e("DDDDDDDDDfffff", "run: -->" + derailPo);
        packageManager = getPackageManager();
        usageStatsManager = (UsageStatsManager) getSystemService("usagestats");
        YgRunning =false;
    }

    public void setDerailPo(int id) {
        derailPo=id;
    }


    class YouguiCount extends CountDownTimer {
        public YouguiCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * 倒计时过程中调用
         *
         * @param millisUntilFinished
         */
        @Override
        public void onTick(long millisUntilFinished) {

            Log.e("yougui", "倒计时=" + (millisUntilFinished / 1000));
        }

        /**
         * 倒计时完成后调用
         */

        @Override
        public void onFinish() {
            Log.e("yougui", "倒计时完成");
            if (derailPo == 1) {
                if (t != null)
                    t = null;
                t = new DownloadThread();
                t.start();
                YouguiCount youguiCount = new YouguiCount(15 * 60 * 1000, 1000);
                youguiCount.start();
            }else {
                YgRunning= false;
            }
        }
    }

    public  boolean getisYgRunning(){
        return YgRunning;
    }

    public void getDerail() {

        if (derailPo == 1) {
            jsonArray = new JSONArray();
            mLocationClient = new LocationClient(this);
            mLocationClient.setLocOption(getDefaultLocationClientOption());
            mLocationClient.registerLocationListener(mListener);
            mLocationClient.start();
            YouguiCount youguiCount = new YouguiCount(15 * 60 * 1000, 1000);
            youguiCount.start();
            YgRunning = true;

        }
    }

    DownloadThread t;
    int yCounts = 4;

    class DownloadThread extends Thread {
        @Override
        public void run() {
            super.run();
            Log.e("DDDDDDtttttt1111", "run: --->");

//               这里是读文件流 和写文件流的操作
            try {
                Log.e("youguitttttt", "run: --->");
                String result = HttpUtils.getOkHpptRequest(HttpUtils.ipAddress + "/happy/derailed/updateSiteData?derailId=" + derailId + "&longitude=" + mLongitude + "&latitude=" + mLatitude);
                Log.e("youguifffffzzz", "run: -->" + result);

                Log.e("DDDDDDtttttt1111", "run: --->"+yCounts);
                if (yCounts == 4) {
                    retrieveUsageStats();
                    yCounts = 0;
                }
                yCounts++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //获取软件使用详情
    private void sortUsageStats(List<UsageStatsWrapper> finalList) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        String nowday = sdf.format(date.getTime());
        Log.e("now", "sortUsageStats: " + nowday);

        List<UsageStatsWrapper> usageStats1 = new ArrayList<>();
        List<UsageStatsWrapper> usageStats2 = new ArrayList<>();

        //判断是否为今天使用的软件
        for (int i = 0; i < finalList.size(); i++) {

            if (finalList.get(i).getUsageStats() == null) {

            } else if (finalList.get(i).getUsageStats().getLastTimeUsed() == 0L) {

            } else {
                long lastTime = finalList.get(i).getUsageStats().getLastTimeUsed();
                String lastTime1 = sdf.format(lastTime);
                Log.e("now", "sortUsageStats:-- ----" + lastTime1);
                if (nowday.equals(lastTime1)) {
                    usageStats1.add(finalList.get(i));
                }

            }
        }

        Log.e("now111", "sortUsageStats:-- ----" + usageStats1.size());

        //判断使用时间大于1s的
        for (int i = 0; i < usageStats1.size(); i++) {
            UsageStatsWrapper usageStat = usageStats1.get(i);
            if (Integer.valueOf(usageStat.TotleTime()) > 1) {
//				usageStats.remove(i);
                usageStats2.add(usageStats1.get(i));

            }

        }
        List<AppUsing> appUsings = new ArrayList<>();
        JSONArray jsonArray1 = new JSONArray();
        for (int i = 0; i < usageStats2.size(); i++) {
            Log.e("Apppppppppppp2222222", "sortUsageStats: -->" + usageStats2.size());
            try {
                String appName = usageStats2.get(i).getAppName();
                String appUseLastTime = usageStats2.get(i).lastTime();
                String appUseTime = usageStats2.get(i).TotleTime();
                Log.e("Apppppppppppp", "sortUsageStats: -->" + appName + "......" + appUseLastTime);
                Map<String, Object> params = new HashMap<>();
                params.put("appName", appName);
                params.put("appUseLastTime", appUseLastTime);
                params.put("appUseTime", appUseTime);
                params.put("appDerailId", derailId);
                JSONObject jsonObject = new JSONObject(params);
                jsonArray1.put(jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            JSONObject jsonObjectt = new JSONObject();
            Map<String, Object> fileMap = new HashMap<>();
            Log.e("TTTTTTSSS", "sortUsageStats: -->" + jsonArray1.length());
            jsonObjectt.put("appUsingList", jsonArray1);
            String url = HttpUtils.ipAddress + "/happy/derailed/updateAppUsingList";
            String result = HttpUtils.postOkHpptRequest3(url, jsonObjectt);
            Log.e("youguifffff222", "run: -->" + result);
            if (!Utils.isEmpty(result)) {
                Log.e("result", "doInBackground: -->" + result);
                JSONObject jsonObject1 = new JSONObject(result);
                JSONArray name = jsonObject1.getJSONArray("returnData");
                if (name.length() > 0) {
                    Log.e("namae", "doInBackground: " + name.toString());
                    for (int i = 0; i < name.length(); i++) {
                        for (int j = 0; j < usageStats2.size(); j++) {
                            if (name.get(i).equals(usageStats2.get(j).getAppName())) {
                                Drawable drawable = usageStats2.get(j).getAppIcon();
                                Log.i("youguiTTTTTTddff", "doInBackground: " + drawable);
                                Bitmap bitmap = drawableToBitmap(drawable);
                                Thread.sleep(1000);
                                File appFile = BitmapCompressUtils.compressImage(bitmap);
                                fileMap.put(usageStats2.get(j).getAppName(), appFile);
                                Log.i("youguiTTTTTTTTTT111", "doInBackground: " + fileMap.size() + usageStats2.get(j).getAppName());
                            }
                        }
                    }
                    String result1 = HttpUtils.upFileAndDesc1("http://47.98.131.11:8084/happy/derailed/updateAppUsingPics", fileMap);
                    Log.i("youguiTTTTTTTTTT222", "-->" + result1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }


    public void retrieveUsageStats() {

        List<String> installedApps = getInstalledAppList();
        Map<String, UsageStats> usageStats = usageStatsManager.queryAndAggregateUsageStats(getStartTime(), System.currentTimeMillis());
        List<UsageStats> stats = new ArrayList<>();
        stats.addAll(usageStats.values());
        List<UsageStatsWrapper> finalList = buildUsageStatsWrapper(installedApps, stats);
        sortUsageStats(finalList);
        Log.e("GGGetlist", "retrieveUsageStats: -->" + finalList.size());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private List<UsageStatsWrapper> buildUsageStatsWrapper(List<String> packageNames, List<UsageStats> usageStatses) {
        List<UsageStatsWrapper> list = new ArrayList<>();
        for (String name : packageNames) {
            boolean added = false;
            for (UsageStats stat : usageStatses) {
                if (name.equals(stat.getPackageName())) {
                    added = true;
                    list.add(fromUsageStat(stat));
                }
            }
            if (!added) {
                list.add(fromUsageStat(name));
            }
        }
        Collections.sort(list);
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private UsageStatsWrapper fromUsageStat(UsageStats usageStats) throws IllegalArgumentException {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(usageStats.getPackageName(), PackageManager.MATCH_UNINSTALLED_PACKAGES);
            return new UsageStatsWrapper(usageStats, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private UsageStatsWrapper fromUsageStat(String packageName) throws IllegalArgumentException {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, PackageManager.MATCH_UNINSTALLED_PACKAGES);
            return new UsageStatsWrapper(null, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
//        catch (PackageManager.NameNotFoundException e) {
//            throw new IllegalArgumentException(e);
//        }
    }

    private List<String> getInstalledAppList() {
        List<ApplicationInfo> infos = packageManager.getInstalledApplications(flags);
        List<String> installedApps = new ArrayList<>();
        for (ApplicationInfo info : infos) {
            installedApps.add(info.packageName);
        }
        return installedApps;
    }

    private long getStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        return calendar.getTimeInMillis();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        assert appOps != null;
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), context.getPackageName());
        return mode == MODE_ALLOWED;
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    double mLongitude;
    double mLatitude;
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {


//            StringBuffer sb = new StringBuffer(256);
//            sb.append("Thread : " + Thread.currentThread().getName());
//            sb.append("\nphone : " + System.currentTimeMillis());
//            sb.append("\ntime : ");
//            sb.append(location.getTime());
//            sb.append("\nlocType : ");// 定位类型
//            sb.append(location.getLocType());
//
//            sb.append("\nlatitude : ");// 纬度
//            sb.append(location.getLatitude());
            mLatitude = location.getLatitude();
//            sb.append("\nlontitude : ");// 经度
//            sb.append(location.getLongitude());
            mLongitude = location.getLongitude();
//            sb.append("\nradius : ");// 半径
//            sb.append(location.getRadius());
//            sb.append("\nCountryCode : ");// 国家码
//            sb.append(location.getCountryCode());
//            sb.append("\nCountry : ");// 国家名称
//            sb.append(location.getCountry());
//            sb.append("\ncitycode : ");// 城市编码
//            sb.append(location.getCityCode());
//            sb.append("\ncity : ");// 城市
//            sb.append(location.getCity());
//            sb.append("\nDistrict : ");// 区
//            sb.append(location.getDistrict());
//            sb.append("\nStreet : ");// 街道
//            sb.append(location.getStreet());
//            sb.append("\naddr : ");// 地址信息
//            sb.append(location.getAddrStr());
//            sb.append("\nDirection(not all devices have value): ");
//            sb.append(location.getDirection());// 方向
//            sb.append("\nlocationdescribe: ");
//            sb.append(location.getLocationDescribe());// 位置语义化信息
//            sb.append("\nPoi: ");// POI信息
//            if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
//                for (int i = 0; i < location.getPoiList().size(); i++) {
//                    Poi poi = (Poi) location.getPoiList().get(i);
//                    sb.append(poi.getName() + ";");
//                }
//            }
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());// 速度 单位：km/h
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());// 卫星数目
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());// 海拔高度 单位：米
//                sb.append("\ndescribe : ");
//                sb.append("gps定位成功");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                // 运营商信息
//                if (location.hasAltitude()) {// *****如果有海拔高度*****
//                    sb.append("\nheight : ");
//                    sb.append(location.getAltitude());// 单位：米
//                }
//                sb.append("\noperationers : ");// 运营商信息
//                sb.append(location.getOperators());
//                sb.append("方向1：" + location.getDerect());
//                sb.append("方向2：" + location.getDirection());
//                sb.append("\ndescribe : ");
//                sb.append("网络定位成功");
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                sb.append("\ndescribe : ");
//                sb.append("离线定位成功，离线定位结果也是有效的");
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                sb.append("\ndescribe : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//            }
////            logMsg(sb.toString());
            Log.e("sbtostring", "onReceiveLocation: " + "lng" + mLongitude + "..." + "lat" + mLatitude);
        }

    };

    public LocationClientOption getDefaultLocationClientOption() {
        if (mOption == null) {
            mOption = new LocationClientOption();
            /**
             * 默认高精度，设置定位模式
             * 15705851109
             * LocationMode.Hight_Accuracy 高精度定位模式：这种定位模式下，会同时使用网络定位和GPS定位，优先返回最高精度的定位结果
             * LocationMode.Battery_Saving 低功耗定位模式：这种定位模式下，不会使用GPS，只会使用网络定位（Wi-Fi和基站定位）
             * LocationMode.Device_Sensors 仅用设备定位模式：这种定位模式下，不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位
             */
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

            /**
             * 默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
             * 目前国内主要有以下三种坐标系：
             1. wgs84：目前广泛使用的GPS全球卫星定位系统使用的标准坐标系；
             2. gcj02：经过国测局加密的坐标；
             3. bd09：为百度坐标系，其中bd09ll表示百度经纬度坐标，bd09mc表示百度墨卡托米制坐标；
             * 海外地区定位结果默认、且只能是wgs84类型坐标
             */
            mOption.setCoorType("bd09ll");

            /**
             * 默认0，即仅定位一次；设置间隔需大于等于1000ms，表示周期性定位
             * 如果不在AndroidManifest.xml声明百度指定的Service，周期性请求无法正常工作
             * 这里需要注意的是：如果是室外gps定位，不用访问服务器，设置的间隔是1秒，那么就是1秒返回一次位置
             如果是WiFi基站定位，需要访问服务器，这个时候每次网络请求时间差异很大，设置的间隔是3秒，只能大概保证3秒左右会返回就一次位置，有时某次定位可能会5秒返回
             */
            mOption.setScanSpan(14*60 * 1000);

            /**
             * 默认false，设置是否需要地址信息
             * 返回省市区等地址信息，这个api用处很大，很多新闻类api会根据定位返回的市区信息推送用户所在市的新闻
             */
            mOption.setIsNeedAddress(true);

            /**
             * 默认是true，设置是否使用gps定位
             * 如果设置为false，即使mOption.setLocationMode(LocationMode.Hight_Accuracy)也不会gps定位
             */
            mOption.setOpenGps(true);

            /**
             * 默认false，设置是否需要位置语义化结果
             * 可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
             */
            mOption.setIsNeedLocationDescribe(true);//

            /**
             * 默认false,设置是否需要设备方向传感器的方向结果
             * 一般在室外gps定位，时返回的位置信息是带有方向的，但是有时候gps返回的位置也不带方向，这个时候可以获取设备方向传感器的方向
             * wifi基站定位的位置信息是不带方向的，如果需要可以获取设备方向传感器的方向
             */
            mOption.setNeedDeviceDirect(false);

            /**
             * 默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
             * 室外gps有效时，周期性1秒返回一次位置信息，其实就是设置了
             locationManager.requestLocationUpdates中的minTime参数为1000ms，1秒回调一个gps位置
             */
            mOption.setLocationNotify(false);

            /**
             * 默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
             * 如果你已经拿到了你要的位置信息，不需要再定位了，不杀死留着干嘛
             */
            mOption.setIgnoreKillProcess(true);

            /**
             * 默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
             * POI就是获取到的位置附近的一些商场、饭店、银行等信息
             */
            mOption.setIsNeedLocationPoiList(false);

            /**
             * 默认false，设置是否收集CRASH信息，默认收集
             */
            mOption.SetIgnoreCacheException(false);

        }
        return mOption;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Ibind", "-->onBind");
        Notification notification = new Notification(R.mipmap.app, "title", System.currentTimeMillis());
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        notification.contentIntent = pintent;
        startForeground(0, notification);
        return binder;
    }


    PowerManager.WakeLock mWakeLock;

    //申请设备电源锁
    public void acquireWakeLock(Context context) {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "WakeLock");
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    //释放设备电源锁
    public void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }


    public class LocalBinder extends Binder {

        public ClockService getService() {

            return ClockService.this;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        clockPreferences = this.getSharedPreferences("password", MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor2 = clockPreferences.edit();
        Set<String> setList = new ArraySet<>();
        editor2.putStringSet("clockSignTime", setList);

        editor2.commit();
//        Notification notification = new Notification.Builder(getApplicationContext())
//                .setWhen(System.currentTimeMillis())
//                .build();
//        startForeground(110, notification);
//        startClock();
        return START_NOT_STICKY;
    }

    /**
     * 销毁服务，则断开MQTT,释放资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countTimer != null) {
            countTimer.cancel();
            countTimer = null;
        }

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
            //设置倒计时结束之后的按钮样式
            clockDao = new ClockDaoImpl(ClockService.this);
            List<ClockBean> clockFinishList = clockDao.findTimeByMin();
            Ring = preferences.getBoolean("ring", false);
            boolean isBt = false;
            boolean isJy = false;
            List<Time> times = timeDao.findTimeByMin();
            for (int i = 0; i < times.size(); i++) {
                Log.e("open", "onFinish:... " + times.size());
                ti = times.get(i);
                boolean open = ti.getOpen();
                sumMin = ti.getSumMin();
                Log.e("open", "onFinish:--> " + open);
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                int nowminutes = hour * 60 + minutes;
                Log.e("qqqqqLLLLLL", sumMin + ",,,," + nowminutes);
                if (sumMin == nowminutes && true == open) {
                    isBt = true;
                    Log.e("qqqqqLLLLLL", isBt + "2222222");
//                    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//                    PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "wjc");
//                    wakeLock.acquire();
////wakeLock.acquire(1000);
//                    wakeLock.release();
//                    if (Ring == false) {
//                        ring();
//                        Ring = true;
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putBoolean("trueCount", false);
//                        editor.commit();
//                    }

                    break;
                }
            }

            if (!isBt) {
                for (int i = 0; i < clockFinishList.size(); i++) {
                    cl = clockFinishList.get(i);

                    if (!(cl.getClockType() == 3 && userId.equals(cl.getClockCreater()))) {
                        int switchs = cl.getSwitchs();
                        boolean open;
                        if (switchs == 1) {
                            open = true;
                        } else {
                            open = false;
                        }
                        sumMin = cl.getClockHour() * 60 + cl.getClockMinute();
                        Log.e("open", "onFinish:--> " + open);
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        int nowminutes = hour * 60 + minutes;
                        if (sumMin == nowminutes && true == open) {
                            isJy = true;
                            isBt = false;
                            break;
                        }
                    }
                }
            }

            if (isBt || isJy) {
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "wjc");
                wakeLock.acquire();
//wakeLock.acquire(1000);
                wakeLock.release();
                if (Ring == false) {
                    if (isBt)
                        ring();
                    else if (isJy) {
                        Log.e("qqqqqCCC", cl.getClockType() + "???");
                        if (cl.getClockType() == 2)
                            qunzuDialog();
                        else
                            qinglvDialog();
                    }

                    Ring = true;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("trueCount", false);
                    editor.putBoolean("ring", true);
                    editor.commit();
                }
            }


        }
    }
//    Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            if (countTimer!=null){
//                countTimer.cancel();
//            }
//        closeCountTimer();
//        }
//    };
//    public void closeCountTimer(){
//
//        close=2;
//        countTimer1 = new CountTimer(counttime * 1000, 1000);
//        countTimer1.start();
//    }

    public void ring() {

        if (ti.getOpen()) {
            if (ti.getFlag() == 1) {
//                time.setOpen(false);
//                timeDao.update(time);
                clolkDialog1();

            } else if (ti.getFlag() == 2) {
//                time.setOpen(false);
//                timeDao.update(time);
                clolkDialog2();

            } else if (ti.getFlag() == 3) {
//                time.setOpen(false);
//                timeDao.update(time);
                clolkDialog3();

            }
        }
    }


    private void qinglvDialog() {
        qinglvClockDialog = new QinglvClockDialog(this);

        qinglvClockDialog.setOnNegativeClickListener(new QinglvClockDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
//                dialog.dismiss();
            }
        });
        qinglvClockDialog.setOnPositiveClickListener(new QinglvClockDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                Ring = false;
            }
        });
        qinglvClockDialog.setCanceledOnTouchOutside(false);
        qinglvClockDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.screenOrientation = Configuration.ORIENTATION_PORTRAIT;
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

            qinglvClockDialog.getWindow().setAttributes(params);
        } else {
            qinglvClockDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        qinglvClockDialog.show();
    }


    private void qunzuDialog() {
        qunzuClockDialog = new QunzuClockDialog(this);

        qunzuClockDialog.setOnNegativeClickListener(new QunzuClockDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
//                dialog.dismiss();
            }
        });
        qunzuClockDialog.setOnPositiveClickListener(new QunzuClockDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                Ring = false;
            }
        });
        qunzuClockDialog.setCanceledOnTouchOutside(false);
        qunzuClockDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.screenOrientation = Configuration.ORIENTATION_PORTRAIT;
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

            qunzuClockDialog.getWindow().setAttributes(params);
        } else {
            qunzuClockDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        qunzuClockDialog.show();
    }

    btClockjsDialog dialog;
    btClockjsDialog2 dialog2;
    btClockjsDialog4 dialog4;
    QinglvClockDialog qinglvClockDialog;
    QunzuClockDialog qunzuClockDialog;

    private void clolkDialog1() {//听歌识曲
        dialog4 = new btClockjsDialog4(this);


        dialog4.setOnNegativeClickListener(new btClockjsDialog4.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
//                dialog.dismiss();
            }
        });
        dialog4.setOnPositiveClickListener(new btClockjsDialog4.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {

            }
        });

        dialog4.setCanceledOnTouchOutside(false);
        dialog4.setCancelable(false);
        dialog4.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.screenOrientation = Configuration.ORIENTATION_PORTRAIT;
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

            dialog4.getWindow().setAttributes(params);
        } else {
            dialog4.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog4.show();

    }

    private void clolkDialog2() {//脑筋急转弯
        dialog2 = new btClockjsDialog2(this);


        dialog2.setOnNegativeClickListener(new btClockjsDialog2.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
//                dialog.dismiss();
            }
        });
        dialog2.setOnPositiveClickListener(new btClockjsDialog2.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {

            }
        });
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.screenOrientation = Configuration.ORIENTATION_PORTRAIT;
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

            dialog2.getWindow().setAttributes(params);
        } else {
            dialog2.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog2.show();
    }

    private void clolkDialog3() {//算一算
        dialog = new btClockjsDialog(this);
        int x = dialog.getX();
        int y = dialog.getY();
        final String text1 = dialog.getText();
        int z = x * y;

        dialog.setOnNegativeClickListener(new btClockjsDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
//                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new btClockjsDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {

            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.screenOrientation = Configuration.ORIENTATION_PORTRAIT;
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

            dialog.getWindow().setAttributes(params);
        } else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

    Time time;
    CountTimer countTimer;
    int counttime;
    int sumMin;
    List<Time> times;
    private AlarmManager am;

    public void startClock() {
        int firstHour = 0;
        int firstMinutes = 0;

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int nowminutes = hour * 60 * 60 + minutes * 60 + second;
        int finishTime = 0;
        times = timeDao.findTimeByMin();
        for (int i = 0; i < times.size(); i++) {
            time = times.get(i);

            boolean open = time.getOpen();
            sumMin = time.getSumMin() * 60;
            if (sumMin < nowminutes) {
                sumMin = sumMin + 24 * 60 * 60;
            }
            if (sumMin >= nowminutes && true == open) {
//                finishTime = sumMin;
                if (finishTime == 0) {
                    finishTime = sumMin;
                } else if (sumMin < finishTime) {
                    finishTime = sumMin;
                }
            }
        }

        clockDao = new ClockDaoImpl(this);
        clockBeanList = clockDao.findTimeByMin();
        Log.e("QqqqqqqqLLL", clockBeanList.size() + "???");
        for (int i = 0; i < clockBeanList.size(); i++) {
            clockBean = clockBeanList.get(i);
            if (!(clockBean.getClockCreater() + "").equals(userId) || (clockBean.getClockType() == 2)) {
                Log.e("qqqqqqqqqqqLLLLpppp", clockBean.getClockHour() + "????" + clockBean.getClockMinute());
                int switchs = clockBean.getSwitchs();
                boolean open;
                if (switchs == 1) {
                    open = true;
                } else {
                    open = false;
                }
                sumMin = ((clockBean.getClockHour() * 60) + clockBean.getClockMinute()) * 60;
                Log.e("qqqqqqqqqqqLLLL22222", sumMin + "," + nowminutes);
                if (sumMin < nowminutes) {
                    sumMin = sumMin + 24 * 60 * 60;
                }


                if (sumMin >= nowminutes && true == open) {
                    if (finishTime == 0) {
                        finishTime = sumMin;
                    } else if (sumMin < finishTime) {
                        finishTime = sumMin;
                    }
                }
            }
        }
        Log.e("qqqqqqqqqqMMMMM", "3333333");
        Set<String> timesSet = new HashSet<>();
        timesSet = clockPreferences.getStringSet("clockSignTime", timesSet);
        String[] timeData = (String[]) timesSet.toArray(new String[timesSet.size()]);   //将SET转换为数组
        boolean isTime = false;
        for (int i = 0; i < timeData.length; i++) {
            if (timeData[i].equals("" + finishTime))
                isTime = true;
        }
        Log.e("qqqqqqqqqqMMMMM", "222222_" + finishTime);
        if (!isTime) {
            SharedPreferences.Editor editor2 = clockPreferences.edit();
            timesSet.add(finishTime + "");
            editor2.putStringSet("clockSignTime", timesSet);
            editor2.commit();

            Log.e("qqqqqqqqqqMMMMM", "11111111");

            counttime = finishTime - nowminutes;
            countTimer = new CountTimer(counttime * 1000, 1000);
            countTimer.start();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("trueCount", true);
            editor.putBoolean("ring", false);
            editor.commit();
        }

        int time = counttime * 1000;
//        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        Intent intent1=new Intent("com.zking.android29_alarm_notification.RING");
//        intent1.setFlags( Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);//3.1以后的版本需要设置Intent.FLAG_INCLUDE_STOPPED_PACKAGES
//        PendingIntent sender = PendingIntent.getService(this, 0x101, intent1,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            am.setExact(AlarmManager.RTC_WAKEUP, counttime*1000, sender);
//        } else {
//            am.set(AlarmManager.RTC_WAKEUP, counttime, sender);
//        }
//        PendingIntent sender = PendingIntent.getBroadcast(this, 0x101, intent1,
//                        PendingIntent.FLAG_CANCEL_CURRENT);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    am.setWindow(AlarmManager.RTC_WAKEUP, time,0, sender);
//
//                    Log.e("time", "startClock: "+time);
//                } else {
//                        am.set(AlarmManager.RTC_WAKEUP, time, sender);
//                    }

    }

    public void update(Time time) {
        timeDao.update(time);
    }


}
