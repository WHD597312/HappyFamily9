package com.xr.happyFamily.together.util.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.activity.DeviceDetailActivity;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.main.FamilyFragmentManager;
import com.xr.happyFamily.together.http.NetWorkUtil;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.MQService;

import java.util.List;

public class MQTTMessageReveiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConn = NetWorkUtil.isConn(MyApplication.getContext());
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        Log.i("MQTTMessageReveiver","MQTTMessageReveiver");
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            Utils.showToast(context, "无网络可用");
            //改变背景或者 处理网络的全局变量
        }else if (mobNetInfo.isConnected() || wifiNetInfo.isConnected()){
//            Utils.showToast(context,"网络已连接");
//            DeviceChildDaoImpl deviceChildDao = new DeviceChildDaoImpl(context);
//            List<DeviceChild> deviceChildren = deviceChildDao.findAllDevice();

            Intent mqttIntent = new Intent(context,MQService.class);
            mqttIntent.putExtra("reconnect","reconnect");
            context.startService(mqttIntent);
        }

        if (!isConn){
            DeviceChildDaoImpl deviceChildDao=new DeviceChildDaoImpl(context);
            List<DeviceChild> deviceChildren=deviceChildDao.findAllDevice();
            for (DeviceChild deviceChild:deviceChildren){
                deviceChild.setOnline(false);
                deviceChildDao.update(deviceChild);
            }
            if (FamilyFragmentManager.running){
                Intent mqttIntent=new Intent("RoomFragment");
                mqttIntent.putExtra("noNet","noNet");
                context.sendBroadcast(mqttIntent);
            }else if (DeviceDetailActivity.running){
                Intent mqttIntent=new Intent("DeviceDetailActivity");
                mqttIntent.putExtra("noNet","noNet");
                context.sendBroadcast(mqttIntent);
            }
        }
    }
}
