package com.xr.happyFamily.zhen;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingActivity extends AppCompatActivity {

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
                        break;
                    case 2:
                        Toast.makeText(SettingActivity.this, "缓存已清理", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
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
