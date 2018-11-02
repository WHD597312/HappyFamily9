package com.xr.happyFamily.le;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.xr.happyFamily.R;
import com.xr.happyFamily.le.Yougui.UsageContract;
import com.xr.happyFamily.le.Yougui.UsageStatAdapter;
import com.xr.happyFamily.le.Yougui.UsageStatsWrapper;

import com.xr.happyFamily.le.pojo.AppUsing;
import com.xr.happyFamily.le.pojo.MapAdress;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

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
    String ip = "http://47.98.131.11:8084";
    /*  @BindView(R.id.lvAppTime)
      ListView lvAppTime;*/
    double latt;
    double lngg;
    UsageStatAdapter adapter;
    //    UsagePresenter presenter;
    RecyclerView recyclerView;
    String derailId;
    SharedPreferences preferences;
    List<AppUsing> appUsings;
    List<MapAdress> mapAdresses;
    List<MapAdress> mStation;
    List<LatLng> points;
    List<OverlayOptions> options;
    private MyDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_le_map);
        dialog = MyDialog.showDialog(this);
        dialog.show();
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        derailId = preferences.getString("derailId", "");
        ButterKnife.bind(this);//绑定framgent
        StatusUtil.setUseStatusBarColor(this, Color.TRANSPARENT, Color.parseColor("#33000000"));
        StatusUtil.setSystemStatus(this, false, true);
        initBaiduMap();
//        getLocationClientOption();//2、定位开启
        appUsings = new ArrayList<>();
        mapAdresses = new ArrayList<>();
        mStation = new ArrayList<>();
        points = new ArrayList<LatLng>();
        options = new ArrayList<>();
        // 构建Marker图标


        rl_map_rj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_map_rj.setVisibility(View.VISIBLE);
                iv_map_rj1.setVisibility(View.GONE);
                rl_map_rjxq.setVisibility(View.VISIBLE);
                rl_map_map.setVisibility(View.GONE);
                tv_map_mes.setTextColor(getResources().getColor(R.color.color_black3));
                tv_mao_rj.setTextColor(getResources().getColor(R.color.white));
                rl_map_wz.setBackground(getResources().getDrawable(R.drawable.bg_shape3));
                rl_map_rj.setBackground(getResources().getDrawable(R.drawable.bg_le_map));
                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
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
                tv_map_mes.setTextColor(getResources().getColor(R.color.white));
                tv_mao_rj.setTextColor(getResources().getColor(R.color.color_black3));
                rl_map_wz.setBackground(getResources().getDrawable(R.drawable.bg_le_map));
                rl_map_rj.setBackground(getResources().getDrawable(R.drawable.bg_shape3));

            }
        });
        new getSiteData().execute();
        new getAppAsyn().execute();

    }


    /**
     * 获取使用的app
     */
    class getAppAsyn extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code = 0;
            String url = ip + "/happy/derailed/getAppUsingList?derailId=" + derailId;
            String result = HttpUtils.getOkHpptRequest(url);
            Log.e("ressssssss", "doInBackground: -->" + result);
            try {
                if (!TextUtils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("returnCode");
                    JSONArray jsonArray = jsonObject.getJSONArray("returnData");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String appPicUrl = jsonObject1.getString("appPicUrl");
                        String appName = jsonObject1.getString("appName");
                        String appUseLastTime = jsonObject1.getString("appUseLastTime");
                        String appUseTime = jsonObject1.getString("appUseTime");
                        AppUsing appUsing = new AppUsing();
                        appUsing.setAppName(appName);
                        appUsing.setIconAdress(appPicUrl);
                        appUsing.setAppUseLastTime(appUseLastTime);
                        appUsing.setUseTime(appUseTime);
                        appUsings.add(appUsing);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            switch (integer) {
                case 100:

                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 获取经纬度
     */
    class getSiteData extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code = 0;
            String url = ip + "/happy/derailed/getSiteData?derailId=" + derailId;
            String result = HttpUtils.getOkHpptRequest(url);
            Log.e("ressssssss11", "doInBackground: -->" + result);
            try {
                if (!TextUtils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("returnCode");
                    JSONArray jsonArray = jsonObject.getJSONArray("returnData");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int dsId = jsonObject1.getInt("dsId");
                        String dsLongitude = jsonObject1.getString("dsLongitude");
                        String dsLatitude = jsonObject1.getString("dsLatitude");
                        long dsTime = jsonObject1.getLong("dsTime");
                        MapAdress mapAdress = new MapAdress();
                        mapAdress.setDsId(dsId);
                        mapAdress.setDsLatitude(dsLatitude);
                        mapAdress.setDsLongitude(dsLongitude);
                        mapAdress.setDsTime(dsTime);
                        mapAdresses.add(mapAdress);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            switch (integer) {
                case 100:
                    getStation();
                    break;
                default:
                    break;
            }
        }
    }

    MapAdress mapAdress1, mapAdress2;

    public void getStation() {
        Log.e("TTTTTTTTTS", "getStation: -->" + mapAdresses.size());
        mStation.add(mapAdresses.get(0));
        mapAdress1 = mapAdresses.get(0);
        if (mapAdresses.size() > 1) {
            for (int i = 1; i < mapAdresses.size(); i++) {
                mapAdress2 = mapAdresses.get(i);
                long lastTime = mapAdress2.getDsTime();
                double dsLongitude1 = Double.valueOf(mapAdress1.getDsLongitude());
                double dsLatitude1 = Double.valueOf(mapAdress1.getDsLatitude());
                double dsLongitude2 = Double.valueOf(mapAdress2.getDsLongitude());
                double dsLatitude2 = Double.valueOf(mapAdress2.getDsLatitude());
                double distance = Distance(dsLatitude1, dsLongitude1, dsLatitude2, dsLongitude2);
                if (distance > 50) {
                    mStation.add(mapAdresses.get(i));
                    mStation.get(mStation.size() - 2).setLastTime(mStation.get(mStation.size() - 1).getDsTime() + "");
                    mapAdress1 = mapAdresses.get(i);
                    Log.e("ddddddGGGGGG333", "getStation: -->" + i);
                } else {
                    mStation.get(mStation.size() - 1).setLastTime(mapAdresses.get(i).getDsTime() + "");
                    Log.e("ddddddGGGGGG444", "getStation: -->" + i);
                }
            }
        }else {
            mStation.get(0).setLastTime(mStation.get(0).getDsTime() + "");
        }

        Log.e("ddddddGGGGGG222", "getStation: -->" + mapAdresses.size());
        Log.e("ddddddGGGGGG", "getStation: -->" + mStation.size());
        for (int j = 0; j < mStation.size(); j++) {
            MapAdress mapAdress3 = mStation.get(j);
            Log.e("ddddddGGGGGG4444", "getStation: -->" + mStation.get(j).getLastTime() + "..." + mStation.get(j).getDsTime());
            double lat = Double.valueOf(mapAdress3.getDsLatitude());
            double lng = Double.valueOf(mapAdress3.getDsLongitude());
            Log.e("ddddddGGGGGG4444", "getStation: -->" +  lat+ "..." +lng);
            BDLocation bdLocation = new BDLocation();
            bdLocation.setLongitude(lng);
            bdLocation.setLatitude(lat);
            Message message = new Message();
            message.obj = bdLocation;
            mHandler.sendMessage(message);
        }
    }


    /*
     * 显示经纬度的位置
     * */
    private void showMap(double latitude, double longtitude, String address) {
        List<LatLng> points = new ArrayList<LatLng>();
        List<Integer> colors = new ArrayList<>();


        points.add(new LatLng(latitude, longtitude));
        colors.add(Integer.valueOf(Color.RED));

        Log.i("points", "-->" + points.size());
        if (points.size() >= 2) {
            OverlayOptions ooPolyline = new PolylineOptions().width(10)
                    .colorsValues(colors).points(points);
            mBaiduMap.addOverlay(ooPolyline);
        }
        if (!points.isEmpty()) {
            CoordinateConverter converter = new CoordinateConverter();
            converter.coord(points.get(points.size() - 1));
            converter.from(CoordinateConverter.CoordType.COMMON);
            LatLng convertLatLng = converter.convert();
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 16.0f);
            mBaiduMap.animateMapStatus(u);
            MyDialog.closeDialog(dialog);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @OnClick({R.id.iv_map_back, R.id.rl_yg_dw})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_map_back:
                finish();
                break;
            case R.id.rl_yg_dw:
   
                break;
        }
    }


    private void showAppTimeList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsageStatAdapter(this, appUsings);
        recyclerView.setAdapter(adapter);
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

    LatLng p2;
    int Cs = 0;
    /**
     * 处理连续定位的地图UI变化
     */
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BDLocation location = (BDLocation) msg.obj;
            LatLng LocationPoint = new LatLng(location.getLatitude(), location.getLongitude());
//            LatLng LocationPoint = pianyi(location.getLatitude(), location.getLongitude());
            points.add(LocationPoint);

            BitmapDescriptor bitmap;
            if (Cs == mStation.size() - 1) {
                Bundle mBundle = new Bundle();
                mBundle.putInt("id", Cs);
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.map_last);
                // 构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions().position(LocationPoint)
                        .icon(bitmap).zIndex(8).draggable(true).extraInfo(mBundle);
                options.add(option);

            } else {
                Bundle mBundle = new Bundle();
                mBundle.putInt("id", Cs);
//                bitmap = BitmapDescriptorFactory
//                        .fromResource(R.mipmap.map_first);
                bitmap = getBitmapDescriptor();
                // 构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions().position(LocationPoint)
                        .icon(bitmap).zIndex(8).draggable(true).extraInfo(mBundle);
                options.add(option);
            }

            final float f = mBaiduMap.getMaxZoomLevel();// 19.0 最小比例尺
            // 设置当前位置显示在地图中心
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(LocationPoint, f - 5);// 设置缩放比例
            mBaiduMap.animateMapStatus(u);
            // 在地图上添加Marker，并显示
            mBaiduMap.addOverlays(options);
            //点击Marker弹出地址名称
            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker arg0) {
                    // TODO Auto-generated method stub
                    int id = arg0.getExtraInfo().getInt("id");
                    String last = stampToDate(Long.valueOf(mStation.get(id).getLastTime()));
                    String first = stampToDate(mStation.get(id).getDsTime());
                    Toast.makeText(MapActivity.this, first + "--" + last, Toast.LENGTH_SHORT).show();
                    Log.e("TTTTTTFFF", "onMarkerClick: -->" + mStation.get(id).getLastTime() + "..." + id);
                    //创建InfoWindow展示的view
//                    Button button = new Button(getApplicationContext());
//                    button.setBackgroundResource(R.drawable.popup);
////定义用于显示该InfoWindow的坐标点
//                    LatLng pt = new LatLng(Double.valueOf(mStation.get(id).getDsLongitude()), Double.valueOf(mStation.get(id).getDsLatitude()));
//                    button.setText(first + "--" + last);
////创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
//                    InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
//
////显示InfoWindow
//                    mBaiduMap.showInfoWindow(mInfoWindow);
                    return true;
                }
            });
            //打卡范围
//            mDestinationPoint = new LatLng(location.getLatitude() /** 1.0001*/, location.getLongitude()/* * 1.0001*/);//假设公司坐标

//            mDestinationPoint = new LatLng(location1.getLatitude() /** 1.0001*/, location1.getLongitude()/* * 1.0001*/);//假设公司坐标

            //缩放地图
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(19.0f));
//            setMapZoomScale(LocationPoint);
//            BitmapDescriptor bitmap1 = BitmapDescriptorFactory
//                    .fromResource(R.mipmap.map_last);
//            // 构建MarkerOption，用于在地图上添加Marker
//            OverlayOptions option1 = new MarkerOptions().position(p2)
//                    .icon(bitmap1).zIndex(8).draggable(true);
//            mBaiduMap.animateMapStatus(u);
//            // 在地图上添加Marker，并显示
//            mBaiduMap.addOverlay(option1);
//            //点击Marker弹出地址名称
//            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//
//                @Override
//                public boolean onMarkerClick(Marker arg0) {
//                    // TODO Auto-generated method stub
//                    Toast.makeText(MapActivity.this, "10:00",
//                            Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//            });
            Cs++;
            if (Cs == mStation.size()) {//画线
//                DrawLines();
                MyDialog.closeDialog(dialog);
            }

        }
    };
    int i = 0;

    public BitmapDescriptor getBitmapDescriptor() {

        i++;
        BitmapDescriptor bttmap = null;
        View item_view = LayoutInflater.from(this).inflate(R.layout.activity_yougui_marker, null);
        TextView tv_storeName = (TextView) item_view.findViewById(R.id.tv_yg_mark);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.iv_yg_mark);

// 设置布局中文字
        tv_storeName.setText(i + "");

// 设置图标
        imageView.setImageResource(R.mipmap.map_first);
        bttmap = BitmapDescriptorFactory.fromView(item_view);
        return bttmap;

    }


    /**
     * 将时间戳转换为时间
     */
    public String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }


    /**
     * 计算两点间的距离
     */
    public Double Distance(double lat1, double lng1, double lat2, double lng2) {


        Double R = 6370996.81;  //地球的半径

        /*
         * 获取两点间x,y轴之间的距离
         */
        Double x = (lng2 - lng1) * Math.PI * R * Math.cos(((lat1 + lat2) / 2) * Math.PI / 180) / 180;
        Double y = (lat2 - lat1) * Math.PI * R / 180;
        Double distance = Math.hypot(x, y);   //得到两点之间的直线距离
        return distance;

    }

    public void DrawLines() {
        int size = points.size();
        if (size > 1) {
            for (int i = 1; i < size; i++) {
                List<LatLng> point = new ArrayList<>();
                LatLng p1 = points.get(i - 1);
                LatLng p2 = points.get(i);
                point.add(p1);
                point.add(p2);
                OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xAAFF0000).points(point);
                mBaiduMap.addOverlay(ooPolyline);
            }
        }
        MyDialog.closeDialog(dialog);
        /*
         * 调用Distance方法获取两点间x,y轴之间的距离
         */
//        double cc = Distance(latt, lngg, latline, lngline);
//
//        int length = (int) cc;
//
//        Toast.makeText(this, "您与终端距离" + length + "米", Toast.LENGTH_SHORT).show();

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
