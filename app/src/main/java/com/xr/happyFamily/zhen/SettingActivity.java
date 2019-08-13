package com.xr.happyFamily.zhen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.xr.database.dao.daoimpl.ClockDaoImpl;
import com.xr.database.dao.daoimpl.DerailBeanDaoImpl;
import com.xr.database.dao.daoimpl.DerailResultDaoImpl;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.FriendDataDaoImpl;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.MsgDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.database.dao.daoimpl.UserInfosDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.NoFastClickUtils;
import com.xr.happyFamily.together.util.mqtt.ClockService;
import com.xr.happyFamily.together.util.mqtt.MQService;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Request;

public class SettingActivity extends AppCompatActivity{

    private SettingAdatper adatper;
    @BindView(R.id.list_set)
    ListView list_set;
    Unbinder unbinder;
    private MyApplication application;
    SharedPreferences preferences;
    private RoomDaoImpl roomDao;
    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    public static String downloadUrl = "http://app-global.pgyer.com/9b71c76cdd061313f38e223ac82c916e.apk?attname=app-release.apk&sign=a82d21a2bc09ca8585390478d1031413&t=5b9791ce";
    Timer timer;
    long id;

    /**
     * 房间数据库
     */
    private DeviceChildDaoImpl deviceChildDao;
    /**
     * 设备数据库
     */
    private HourseDaoImpl hourseDao;
    /**
     * 家庭数据库
     */
    private ClockDaoImpl clockDao;
    /**
     * 闹钟数据库
     */
    private FriendDataDaoImpl friendDataDao;
    /**
     * 好友数据库
     */
    private TimeDaoImpl timeDao;
    /**
     * 闹钟数据库
     */
    private UserInfosDaoImpl userInfosDao;
    /**
     * 联系人数据库
     */
    private MsgDaoImpl msgDao;
    /**
     * 有轨申请数据库
     */
    private DerailBeanDaoImpl derailBeanDao;
    /**
     * 有轨申请回复数据库
     */
    private DerailResultDaoImpl derailResultDao;

    private boolean isBound = false;

    private     int derailPo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (application == null) {
            application = (MyApplication) getApplication();
            application.addActivity(this);
        }
        Intent service = new Intent(this, MQService.class);
        isBound = bindService(service, connection, Context.BIND_AUTO_CREATE);
        clockintent = new Intent(SettingActivity.this, ClockService.class);
        clockisBound = bindService(clockintent, clockconnection, Context.BIND_AUTO_CREATE);
        hourseDao = new HourseDaoImpl(getApplicationContext());
        roomDao = new RoomDaoImpl(getApplicationContext());
        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
        clockDao = new ClockDaoImpl(getApplicationContext());
        friendDataDao = new FriendDataDaoImpl(getApplicationContext());
        timeDao = new TimeDaoImpl(getApplicationContext());
        userInfosDao = new UserInfosDaoImpl(getApplicationContext());
        msgDao = new MsgDaoImpl(getApplicationContext());
        derailResultDao=new DerailResultDaoImpl(getApplicationContext());
        derailBeanDao= new DerailBeanDaoImpl(getApplicationContext());

        preferences = getSharedPreferences("my", MODE_PRIVATE);
        derailPo = preferences.getInt("derailPo", -1);
        unbinder = ButterKnife.bind(this);
        adatper = new SettingAdatper(this);
        roomDao = new RoomDaoImpl(this);
        list_set.setAdapter(adatper);
        list_set.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(SettingActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
//                        downLoadApp();
                        break;
                    case 2:
                        Toast.makeText(SettingActivity.this, "缓存已清理", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    TextView tv_device_ensure;
    Dialog dialog;
    private String appUrl="http://app-global.pgyer.com/9b71c76cdd061313f38e223ac82c916e.apk?attname=app-release.apk&sign=7e283b839e245d613383763013a87bff&t=5b9790f4";
    ProgressBar progressBar;
    private void downLoadApp(){
        View view = View.inflate(this, R.layout.download_layout, null);
        dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.setContentView(view);

        progressBar= (ProgressBar) view.findViewById(R.id.progress);
        TextView tv_device_cancel= (TextView) view.findViewById(R.id.tv_device_cancel);
        tv_device_ensure= (TextView) view.findViewById(R.id.tv_device_ensure);
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        request = new DownloadManager.Request(Uri.parse(downloadUrl));

        request.setTitle("P99");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI| DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setMimeType("application/vnd.android.package-archive");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //创建目录
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir() ;
        //设置文件存放路径
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS  , "app-release.apk" ) ;
        final  DownloadManager.Query query = new DownloadManager.Query();

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Cursor cursor = downloadManager.query(query.setFilterById(id));
                if (cursor != null && cursor.moveToFirst()) {
                    if (cursor.getInt(
                            cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        progressBar.setProgress(100);
                        install(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/app-release.apk" );
                        task.cancel();
                    }
                    String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                    String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    Log.i("bytes_downloaded","-->"+bytes_downloaded);
                    Log.i("bytes_total","-->"+bytes_total);
                    float x=bytes_downloaded;
                    float ss=(x/bytes_total)*100;
                    BigDecimal bigDecimal=new BigDecimal(ss);
                    BigDecimal bigDecimal2= bigDecimal.setScale(0,BigDecimal.ROUND_DOWN);
                    int pro=Integer.parseInt(bigDecimal2+"");
                    Log.i("prossssssssssssss","-->"+(bytes_downloaded) / bytes_total);
                    Message msg =Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putInt("pro",pro);
                    bundle.putString("name",title);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
                cursor.close();
            }
        };
        timer.schedule(task, 0,1000);
        tv_device_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tv_device_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = downloadManager.enqueue(request);
                task.run();
                tv_device_ensure.setClickable(false);
            }
        });

    }
    TimerTask task;
    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int pro = bundle.getInt("pro");
            progressBar.setProgress(pro);
            if (pro==100 && dialog!=null){
                dialog.dismiss();
            }
        }
    };
    private void install(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//4.0以上系统弹出安装成功打开界面
        startActivity(intent);
    }

    @OnClick({R.id.back, R.id.btn_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_exit:
                if (NoFastClickUtils.isFastClick()){
                    if (preferences.contains("password")) {
                        preferences.edit().remove("password").commit();
                    }
                    if (preferences.contains("image")) {
                        String image = preferences.getString("image", "");
                        preferences.edit().remove("image").commit();
                        File file = new File(image);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    if (preferences.contains("derailPo")){
                        preferences.edit().remove("derailPo").commit();
                        clcokservice.setDerailPo(-1);
                    }
//                    application.removeActivity(SettingActivity.this);
                    Log.e("FFFFFFFFSSSS", "onClick: -->"+preferences.getInt("derailPo",-1) );
                    if (mqService!=null){
                        mqService.cancelAllsubscibe();
                    }
                    hourseDao.deleteAll();
                    roomDao.deleteAll();
                    deviceChildDao.deleteAll();
                    clockDao.deleteAll();
                    timeDao.deleteAll();
                    userInfosDao.deleteAll();
                    friendDataDao.deleteAll();
                    msgDao.deleteAll();
                    derailBeanDao.deleteAll();
                    derailResultDao.deleteAll();

                    SharedPreferences mPositionPreferences = getSharedPreferences("position", MODE_PRIVATE);
                    mPositionPreferences.edit().clear().commit();
                    Intent exit = new Intent(this, LoginActivity.class);
                    exit.putExtra("logout","logout");
                    List<Activity> activities=application.getActivities();
                    for (Activity activity:activities){
                        Log.i("activity","-->"+activity);
                        if (!(activity instanceof SettingActivity)){
                            activity.finish();
                        }
                    }
                    startActivity(exit);
                }
                break;
        }
    }


    Intent clockintent;
    ClockService clcokservice;
    boolean boundclock;
    private  boolean clockisBound;
    ServiceConnection clockconnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ClockService.LocalBinder binder = (ClockService.LocalBinder) service;
            clcokservice = binder.getService();
            boundclock = true;

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };





    MQService mqService;
    boolean bound;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeActivity(this);
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
        if (isBound && connection != null) {
            unbindService(connection);
        }
        if (clockisBound &&clockconnection!=null){
            unbindService(clockconnection);
        }
        handler.removeCallbacksAndMessages(null);
    }

    class SettingAdatper extends BaseAdapter {

        private Context context;

        public SettingAdatper(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String[] str = {"更新", "检查更新", "通用", "隐私设置", "清理缓存"};

            ViewHolder viewHolder = null;
            ViewHolder2 viewHolder2 = null;
            switch (position) {
//                case 0:
//                    convertView=View.inflate(context,R.layout.item_set,null);
//                    viewHolder=new ViewHolder(convertView);
//                    viewHolder.tv_head.setText(str[0]);
//                    break;
//                case 1:
//                    convertView=View.inflate(context,R.layout.item_set2,null);
//                    break;
//                case 2:
//                    convertView=View.inflate(context,R.layout.view3,null);
//                    convertView.setMinimumHeight(3);
//                    break;
                case 0:
                    convertView = View.inflate(context, R.layout.item_set3, null);
                    viewHolder2 = new ViewHolder2(convertView);
                    viewHolder2.tv_head.setText(str[1]);
                    break;
                case 1:
                    convertView = View.inflate(context, R.layout.view3, null);
                    convertView.setMinimumHeight(3);
                    break;
                case 2:
                    convertView = View.inflate(context, R.layout.item_set3, null);
                    viewHolder2 = new ViewHolder2(convertView);
                    viewHolder2.tv_head.setText(str[4]);
                    break;
//                case 6:
//                    convertView=View.inflate(context,R.layout.item_set3,null);
//                    viewHolder2=new ViewHolder2(convertView);
//                    viewHolder2.tv_head.setText(str[3]);
//                    break;
//                case 7:
//                    convertView=View.inflate(context,R.layout.view3,null);
//                    convertView.setMinimumHeight(3);
//                    break;
//                case 8:
//                    convertView=View.inflate(context,R.layout.item_set3,null);
//                    viewHolder2=new ViewHolder2(convertView);
//                    viewHolder2.tv_head.setText(str[4]);
//                    break;
            }
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tv_head)
            TextView tv_head;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

        class ViewHolder2 {
            @BindView(R.id.tv_head)
            TextView tv_head;

            public ViewHolder2(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
