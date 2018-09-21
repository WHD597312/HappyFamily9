package com.xr.happyFamily.jia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.activity.UseWaterRecordActivity;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LiveActivity extends AppCompatActivity {
    Unbinder unbinder;
    @BindView(R.id.cp_alive1)
    CircleProgressView circleProgressView1;/**滤芯寿命1 图示*/
    @BindView(R.id.cp_alive2)
    CircleProgressView circleProgressView2;/**滤芯寿命2 图示*/
    @BindView(R.id.cp_alive3)
    CircleProgressView circleProgressView3;/**滤芯寿命3 图示*/
    @BindView(R.id.cp_alive4)
    CircleProgressView circleProgressView4;/**滤芯寿命4 图示*/
    @BindView(R.id.tv_live_num1)/**滤芯寿命1 值*/
    TextView tv_live_num1;
    @BindView(R.id.tv_live_num2)/**滤芯寿命2 值*/
    TextView tv_live_num2;
    @BindView(R.id.tv_live_num3)/**滤芯寿命3 值*/
    TextView tv_live_num3;
    @BindView(R.id.tv_live_num4)/**滤芯寿命4 值*/
    TextView tv_live_num4;

    private MyApplication application;
    private DeviceChildDaoImpl deviceChildDao;
    long houseId;
    long deviceId;
    private DeviceChild deviceChild;
    MessageReceiver receiver;
    public static  boolean running=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alive);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        unbinder = ButterKnife.bind(this);

        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
        Intent intent = getIntent();
        deviceId = intent.getLongExtra("deviceId", 0);
        deviceChild = deviceChildDao.findById(deviceId);
        int wPurifierfilter1=deviceChild.getWPurifierfilter1();
        int wPurifierfilter2=deviceChild.getWPurifierfilter2();
        int wPurifierfilter3=deviceChild.getWPurifierfilter3();
        int wPurifierfilter4=deviceChild.getWPurifierfilter4();
        houseId = intent.getLongExtra("houseId", 0);
        circleProgressView1.setCurrent(wPurifierfilter1);
        circleProgressView2.setCurrent(wPurifierfilter2);
        circleProgressView3.setCurrent(wPurifierfilter3);
        circleProgressView4.setCurrent(wPurifierfilter4);
        tv_live_num1.setText(""+wPurifierfilter1);
        tv_live_num2.setText(""+wPurifierfilter2);
        tv_live_num3.setText(""+wPurifierfilter3);
        tv_live_num4.setText(""+wPurifierfilter4);
        IntentFilter intentFilter = new IntentFilter("LiveActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);
    }
    @OnClick({R.id.img_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        running=true;
        deviceChild = deviceChildDao.findById(deviceId);
        if (deviceChild==null){
            Toast.makeText(LiveActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
            Intent data = new Intent(LiveActivity.this, MainActivity.class);
            data.putExtra("houseId", houseId);
            startActivity(data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.ACTION_DOWN){
            application.removeActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        running=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
        if (receiver!=null){
            unregisterReceiver(receiver);
        }
    }
    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress = intent.getStringExtra("macAddress");
            Log.i("macAddress", "-->" + macAddress);
            DeviceChild deviceChild2 = (DeviceChild) intent.getSerializableExtra("deviceChild");
            String noNet = intent.getStringExtra("noNet");
            if (!TextUtils.isEmpty(noNet)) {

            } else {
                if (!TextUtils.isEmpty(macAddress) && macAddress.equals(deviceChild.getMacAddress())) {
                    if (deviceChild2 == null) {
                        Toast.makeText(LiveActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
                        long houseId = deviceChild.getHouseId();
                        Intent data = new Intent(LiveActivity.this, MainActivity.class);
                        data.putExtra("houseId", houseId);
                        startActivity(data);
                    } else {
                        deviceChild = deviceChild2;
                        int wPurifierfilter1=deviceChild.getWPurifierfilter1();
                        int wPurifierfilter2=deviceChild.getWPurifierfilter2();
                        int wPurifierfilter3=deviceChild.getWPurifierfilter3();
                        int wPurifierfilter4=deviceChild.getWPurifierfilter4();
                        circleProgressView1.setCurrent(wPurifierfilter1);
                        circleProgressView2.setCurrent(wPurifierfilter2);
                        circleProgressView3.setCurrent(wPurifierfilter3);
                        circleProgressView4.setCurrent(wPurifierfilter4);
                        tv_live_num1.setText(""+wPurifierfilter1);
                        tv_live_num2.setText(""+wPurifierfilter2);
                        tv_live_num3.setText(""+wPurifierfilter3);
                        tv_live_num4.setText(""+wPurifierfilter4);
                    }
                }
            }
        }
    }
}
