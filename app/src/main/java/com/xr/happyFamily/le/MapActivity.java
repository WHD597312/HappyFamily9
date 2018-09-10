package com.xr.happyFamily.le;

import android.app.usage.UsageStats;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.Point;
import com.xr.happyFamily.R;
import com.xr.happyFamily.le.adapter.AppListAdapter;
import com.xr.happyFamily.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crossoverone.statuslib.StatusUtil;

public class MapActivity extends AppCompatActivity {
    private SensorManager mSensorManager;//方向传感器
    private LatLng mDestinationPoint;//目的地坐标点
    private LocationClient client;//定位监听
    private LocationClientOption mOption;//定位属性
    private MyLocationData locData;//定位坐标
    private InfoWindow mInfoWindow;//地图文字位置提醒
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private int mCurrentDirection = 0;
    private float mZoomScale = 0; //比例
    private LatLng mCenterPos;
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private List<UsageStats> usageStats;
    private List<UsageStats> usageStats1;
    private List<UsageStats> usageStats2;
    @BindView(R.id.bt_map_add)
    Button bt_map_add;
    @BindView(R.id.rl_map_bd)
    RelativeLayout rl_map_bd;
    @BindView(R.id.rl_map_map)
    RelativeLayout rl_map_map;
    @BindView(R.id.rl_map_wz)
    RelativeLayout rl_map_wz;
    @BindView(R.id.rl_map_rjxq)
    RelativeLayout rl_map_rjxq;
    @BindView(R.id.rl_map_rj)
    RelativeLayout rl_map_rj;
    @BindView(R.id.iv_map_rj)
    ImageView iv_map_rj;
    @BindView(R.id.iv_map_rj1)
    ImageView iv_map_rj1;
    @BindView(R.id.tv_map_mes)
    TextView tv_map_mes;
    @BindView(R.id.tv_mao_rj)
    TextView tv_mao_rj;
    @BindView(R.id.lvAppTime)
    ListView lvAppTime;
    double latt;
    double lngg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (UStats.getUsageStatsList(this).isEmpty()) {

            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);


        }

        setContentView(R.layout.activity_le_map);
        Intent intent = this.getIntent();
        latt = intent.getDoubleExtra("mLatitude", 0);//纬度
        lngg = intent.getDoubleExtra("mLongitude", 0);//经度
        ButterKnife.bind(this);//绑定framgent
        StatusUtil.setUseStatusBarColor(this, Color.TRANSPARENT, Color.parseColor("#33000000"));
        StatusUtil.setSystemStatus(this, false, true);
        initBaiduMap();
        getLocationClientOption();//2、定位开启

        // 构建Marker图标


        bt_map_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_map_bd.setVisibility(View.GONE);
                rl_map_map.setVisibility(View.VISIBLE);
            }
        });
        rl_map_rj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_map_rj.setVisibility(View.VISIBLE);
                iv_map_rj1.setVisibility(View.GONE);
                rl_map_rjxq.setVisibility(View.VISIBLE);
                rl_map_map.setVisibility(View.GONE);
                rl_map_bd.setVisibility(View.GONE);
                tv_map_mes.setTextColor(getResources().getColor(R.color.color_black3));
                tv_mao_rj.setTextColor(getResources().getColor(R.color.white));
                rl_map_wz.setBackground(getResources().getDrawable(R.drawable.bg_shape3));
                rl_map_rj.setBackground(getResources().getDrawable(R.drawable.bg_le_map));
                usageStats = UStats.getUsageStatsList(MapActivity.this);
                usageStats1 = new ArrayList<>();
                usageStats2 = new ArrayList<>();
                sortUsageStats();
                showAppTimeList();
            }
        });
        rl_map_wz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_map_rj.setVisibility(View.GONE);
                iv_map_rj1.setVisibility(View.VISIBLE);
                rl_map_map.setVisibility(View.VISIBLE);
                rl_map_rjxq.setVisibility(View.GONE);
                rl_map_bd.setVisibility(View.GONE);
                tv_map_mes.setTextColor(getResources().getColor(R.color.white));
                tv_mao_rj.setTextColor(getResources().getColor(R.color.color_black3));
                rl_map_wz.setBackground(getResources().getDrawable(R.drawable.bg_le_map));
                rl_map_rj.setBackground(getResources().getDrawable(R.drawable.bg_shape3));

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @OnClick({R.id.iv_map_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_map_back:
                finish();
                break;
        }
    }


    private void sortUsageStats() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        String nowday = sdf.format(date.getTime());
        Log.e("now", "sortUsageStats: " + nowday);
        Log.e("size", "sortUsageStats" + usageStats.size());
        // 필요없는 데이타는 삭제.
        for (int i = 0; i < usageStats.size(); i++) {

            if (usageStats.get(i).getTotalTimeInForeground() > 1000) {
//				usageStats.remove(i);
                usageStats1.add(usageStats.get(i));

            }

        }
        for (int i = 0; i < usageStats1.size(); i++) {

            long mLastTime = usageStats1.get(i).getFirstTimeStamp();
            Date timeInDate = new Date(mLastTime);
            String lastTime = sdf.format(timeInDate);
            Log.e("now", "sortUsageStats:-- ----" + lastTime);

            if (nowday.equals(lastTime)) {
                usageStats2.add(usageStats1.get(i));

            }
        }

        // DESC 내림차순
        Collections.sort(usageStats2, new Comparator<UsageStats>() {
            public int compare(UsageStats obj1, UsageStats obj2) {
                // TODO Auto-generated method stub
                return (obj1.getLastTimeUsed() > obj2.getLastTimeUsed()) ? -1 : (obj1.getLastTimeUsed() < obj2.getLastTimeUsed()) ? 1 : 0;
//				return (obj1.getTotalTimeInForeground() > obj2.getTotalTimeInForeground()) ? -1
//						: (obj1.getTotalTimeInForeground() < obj2.getTotalTimeInForeground()) ? 1 : 0;
            }
        });
    }

    private void showAppTimeList() {
        AppListAdapter adapter = new AppListAdapter(this, R.layout.activity_map_item, usageStats2);
        lvAppTime.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化地图
     */
    private void initBaiduMap() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mMapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
    }


    /***
     * 定位选项设置
     * @return
     */
    public void getLocationClientOption() {
        mOption = new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(2000);//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        mOption.setNeedDeviceDirect(true);//可选，设置是否需要设备方向结果
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mOption.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mOption.setOpenGps(true);//可选，默认false，设置是否开启Gps定位
        mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        client = new LocationClient(this);
        client.setLocOption(mOption);
        client.registerLocationListener(BDAblistener);
        client.start();
    }

    /***
     * 接收定位结果消息，并显示在地图上
     */
    private BDAbstractLocationListener BDAblistener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //定位方向
//            mCurrentLat = location.getLatitude();
//            mCurrentLon = location.getLongitude();
//            //骑手定位
//            locData = new MyLocationData.Builder()
//                    .direction(mCurrentDirection).latitude(location.getLatitude())
//                    .longitude(location.getLongitude()).build();
//
//            mBaiduMap.setMyLocationData(locData);
//            mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
//                    MyLocationConfiguration.LocationMode.NORMAL, true, null));
            //更改UI
            Message message = new Message();
            message.obj = location;
            mHandler.sendMessage(message);
        }
    };


    /**
     * 处理连续定位的地图UI变化
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BDLocation location = (BDLocation) msg.obj;

            LatLng LocationPoint = new LatLng(latt, lngg);
//            LatLng LocationPoint = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i("loc", "handleMessage:--> " + location.getLatitude());
            Log.i("loc", "handleMessage:--> " + location.getLongitude());
            Log.i("loc", "handleMessage:--> " + location.getAddrStr());

            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.map_first);
            // 构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(LocationPoint)
                    .icon(bitmap).zIndex(8).draggable(true);
            float f = mBaiduMap.getMaxZoomLevel();// 19.0 最小比例尺
            // 设置当前位置显示在地图中心
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(LocationPoint, f - 5);// 设置缩放比例
            mBaiduMap.animateMapStatus(u);
            // 在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
            //点击Marker弹出地址名称
            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker arg0) {
                    // TODO Auto-generated method stub
//                Toast.makeText(MapActivity.this, address1,
//                        Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            //打卡范围
//            mDestinationPoint = new LatLng(location.getLatitude() /** 1.0001*/, location.getLongitude()/* * 1.0001*/);//假设公司坐标

//            mDestinationPoint = new LatLng(location1.getLatitude() /** 1.0001*/, location1.getLongitude()/* * 1.0001*/);//假设公司坐标

            //缩放地图
            setMapZoomScale(LocationPoint);
        }
    };


    //改变地图缩放
    private void setMapZoomScale(LatLng ll) {
        if (mDestinationPoint == null) {
            mZoomScale = getZoomScale(ll);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(ll, mZoomScale));//缩放
        } else {
            mZoomScale = getZoomScale(ll);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(mCenterPos, mZoomScale));//缩放
        }
    }

    /**
     * 获取地图的中心点和缩放比例
     *
     * @return float
     */
    private float getZoomScale(LatLng LocationPoint) {
        double maxLong;    //最大经度
        double minLong;    //最小经度
        double maxLat;     //最大纬度
        double minLat;     //最小纬度
        List<Double> longItems = new ArrayList<Double>();    //经度集合
        List<Double> latItems = new ArrayList<Double>();     //纬度集合

        if (null != LocationPoint) {
            longItems.add(LocationPoint.longitude);
            latItems.add(LocationPoint.latitude);
        }
        if (null != mDestinationPoint) {
            longItems.add(mDestinationPoint.longitude);
            latItems.add(mDestinationPoint.latitude);
        }

        maxLong = longItems.get(0);    //最大经度
        minLong = longItems.get(0);    //最小经度
        maxLat = latItems.get(0);     //最大纬度
        minLat = latItems.get(0);     //最小纬度

        for (int i = 0; i < longItems.size(); i++) {
            maxLong = Math.max(maxLong, longItems.get(i));   //获取集合中的最大经度
            minLong = Math.min(minLong, longItems.get(i));   //获取集合中的最小经度
        }

        for (int i = 0; i < latItems.size(); i++) {
            maxLat = Math.max(maxLat, latItems.get(i));   //获取集合中的最大纬度
            minLat = Math.min(minLat, latItems.get(i));   //获取集合中的最小纬度
        }
        double latCenter = (maxLat + minLat) / 2;
        double longCenter = (maxLong + minLong) / 2;
        int jl = (int) getDistance(new LatLng(maxLat, maxLong), new LatLng(minLat, minLong));//缩放比例参数
        mCenterPos = new LatLng(latCenter, longCenter);   //获取中心点经纬度
        int zoomLevel[] = {2500000, 2000000, 1000000, 500000, 200000, 100000,
                50000, 25000, 20000, 10000, 5000, 2000, 1000, 500, 100, 50, 20, 0};
        int i;
        for (i = 0; i < 18; i++) {
            if (zoomLevel[i] < jl) {
                break;
            }
        }
        float zoom = i + 4;
        return zoom;
    }

    /**
     * 缩放比例参数
     *
     * @param var0
     * @param var1
     * @return
     */
    public double getDistance(LatLng var0, LatLng var1) {
        if (var0 != null && var1 != null) {
            Point var2 = CoordUtil.ll2point(var0);
            Point var3 = CoordUtil.ll2point(var1);
            return var2 != null && var3 != null ? CoordUtil.getDistance(var2, var3) : -1.0D;
        } else {
            return -1.0D;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        //调用LocationClient的start()方法，便可发起定位请求
//        client.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();//解绑
    }
}