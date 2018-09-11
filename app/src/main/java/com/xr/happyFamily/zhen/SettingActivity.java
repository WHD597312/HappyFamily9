package com.xr.happyFamily.zhen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
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
import com.xr.happyFamily.together.http.NoFastClickUtils;
import com.xr.happyFamily.together.util.mqtt.ClockService;
import com.xr.happyFamily.together.util.mqtt.MQService;
import com.xr.happyFamily.together.util.reloadapp.UpdateManager;
import com.xuexiang.xupdate.XUpdate;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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

    private boolean isBound = false;

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
        hourseDao = new HourseDaoImpl(getApplicationContext());
        roomDao = new RoomDaoImpl(getApplicationContext());
        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
        clockDao = new ClockDaoImpl(getApplicationContext());
        friendDataDao = new FriendDataDaoImpl(getApplicationContext());
        timeDao = new TimeDaoImpl(getApplicationContext());
        userInfosDao = new UserInfosDaoImpl(getApplicationContext());
        msgDao = new MsgDaoImpl(getApplicationContext());

        preferences = getSharedPreferences("my", MODE_PRIVATE);
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


    private String appUrl="http://app-global.pgyer.com/79778d2ebb78ca21ca945bf69f52f8cd.apk?attname=app-release.apk&sign=6f09f4445a10b256c1a61b1a2483df5e&t=5b976ab4";
    ProgressBar progressBar;
    private void downLoadApp(){
        View view = View.inflate(this, R.layout.download_layout, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.setContentView(view);

        progressBar= (ProgressBar) view.findViewById(R.id.progress);
        TextView tv_device_cancel= (TextView) view.findViewById(R.id.tv_device_cancel);
        TextView tv_device_ensure= (TextView) view.findViewById(R.id.tv_device_ensure);
        tv_device_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_device_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                OkHttpUtils.get()
//                        .url(appUrl)
//                        .build()
//                        .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),"p99.apk") {
//                            @Override
//                            public void inProgress(float progress) {
//                                Log.i("progress","-->"+progress);
//                                progressBar.setProgress((int) progress*10);
//                            }
//
//                            @Override
//                            public void onError(com.squareup.okhttp.Request request, Exception e) {
//
//                                Toast.makeText(SettingActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            }
//
//                            @Override
//                            public void onResponse(File file) {
//                                Toast.makeText(SettingActivity.this, "下载完成",Toast.LENGTH_SHORT).show();
//                                Log.e("apkpath",file.getAbsolutePath());
//                                Intent intent = new Intent(Intent.ACTION_VIEW);
//                                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                                startActivity(intent);
//                            }
//                        });



            }
        });

    }

    Thread downLoadThread;
    /**
     * 从服务器下载APK安装包
     */
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }
    boolean intercept=false;
    int progress=0;

    private Runnable mdownApkRunnable = new Runnable() {

        @Override
        public void run() {
            URL url;
            try {
                url = new URL(appUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream ins = conn.getInputStream();
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                if (!file.exists()) {
                    file.mkdir();
                }
                File apkFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"p99.apk");
                FileOutputStream fos = new FileOutputStream(apkFile);
                int count = 0;
                byte[] buf = new byte[1024];
                while (!intercept) {
                    int numread = ins.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);

                    // 下载进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                }
                fos.close();
                ins.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 安装APK内容
     */
    private void installAPK() {
        File apkFile = new File(Environment.getExternalStorageDirectory(),"P99.apk");
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
                "application/vnd.android.package-archive");
        startActivity(intent);
    };

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    progressBar.setProgress(progress);
                    break;
                case DOWN_OVER:

                    installAPK();
                    break;

                default:
                    break;
            }
        }

    };

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

                    SharedPreferences mPositionPreferences = getSharedPreferences("position", MODE_PRIVATE);
                    mPositionPreferences.edit().clear().commit();
                    Intent exit = new Intent(this, LoginActivity.class);
                    exit.putExtra("logout","logout");
                    startActivity(exit);
                }
                break;
        }
    }

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
