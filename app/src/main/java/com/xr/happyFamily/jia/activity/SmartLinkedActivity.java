package com.xr.happyFamily.jia.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.http.MessageEvent;
import com.xr.happyFamily.together.http.NetWorkUtil;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SmartLinkedActivity extends AppCompatActivity {

    Unbinder unbinder;
    private MyApplication application;
    private DeviceChildDaoImpl deviceChildDao;
    @BindView(R.id.list_linked) ListView list_linked;/**可联动的设备视图列表*/
    private Map<Integer,DeviceChild> linkedMap=new LinkedHashMap<>();/**已联动的设备*/
    private String chooseDevicesIp=HttpUtils.ipAddress+"/family/device/sensors/chooseDevices";
    private List<DeviceChild> list=new ArrayList<>();/**可联动的设备*/
    private LinkdAdapter adapter;
    private int sensorId;/**传感器Id*/
    long houseId;
    long roomId;
    int linkedSensorId;
    DeviceChild sensorChild;
    MessageReceiver receiver;
    public static boolean running=false;
    private long Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_linked);
        unbinder=ButterKnife.bind(this);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        deviceChildDao=new DeviceChildDaoImpl(getApplicationContext());
//        list=deviceChildDao.findLinkDevice(3);
        Intent intent=getIntent();
        sensorId=intent.getIntExtra("sensorId",0);
        Id= intent.getLongExtra("Id",0);
        sensorChild=deviceChildDao.findById(Id);
        linkedSensorId=sensorChild.getDeviceId();
        houseId=sensorChild.getHouseId();
        roomId=sensorChild.getRoomId();
        list= (List<DeviceChild>) intent.getSerializableExtra("deviceList");
        adapter=new LinkdAdapter(this,list);
        list_linked.setAdapter(adapter);
        if (NetWorkUtil.isConn(SmartLinkedActivity.this)){
            new GetLinkedAsync().execute();
        }else {
            Utils.showToast(SmartLinkedActivity.this,"请检查网络");
        }
        IntentFilter intentFilter = new IntentFilter("SmartLinkedActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        running=true;
        sensorChild = deviceChildDao.findById(Id);
        if (sensorChild==null){
            Toast.makeText(SmartLinkedActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
            Intent data = new Intent(SmartLinkedActivity.this, MainActivity.class);
            data.putExtra("houseId", houseId);
            startActivity(data);
        }else {
            if (!list.isEmpty()){
                List<DeviceChild> removeList=new ArrayList<>();
                for (DeviceChild deviceChild2:list){
                    DeviceChild deviceChild3=deviceChildDao.findDeviceByMacAddress2(deviceChild2.getMacAddress());
                    if (deviceChild3==null){
                        String macAddress=deviceChild2.getMacAddress();
                        if (linkedMap.containsKey(macAddress)){
                            linkedMap.remove(deviceChild2);
                        }
                        removeList.add(deviceChild2);
                    }
                }
                list.removeAll(removeList);
                if (list.isEmpty()){
                    Intent intent=new Intent();
                    intent.putExtra("list",(Serializable) list);
                    setResult(100,intent);
                    finish();
                }else {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        running=false;
    }

    @OnClick({R.id.image_back,R.id.btn_ensure})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.btn_ensure:
                Map<String,Object> params=new HashMap<>();
                params.put("sensorsId",sensorId);
                List<Integer> list=new ArrayList<>();
                try {
                    for (Map.Entry<Integer, DeviceChild> entry : linkedMap.entrySet()) {
                        int deviceId=entry.getKey();
                        DeviceChild deviceChild=entry.getValue();
                        int linked=deviceChild.getLinked();
                        if (linked==1){
                            list.add(deviceId);
                        }
                    }
                    int arr[]=new int[list.size()];
                    for (int i = 0; i <list.size() ; i++) {
                        arr[i]=list.get(i);
                    }
                    params.put("deviceIds",arr);
                    new ChooseAsync().execute(params);
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }
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

    class LinkdAdapter extends BaseAdapter{
        private Context context;
        private List<DeviceChild> list;

        public LinkdAdapter(Context context, List<DeviceChild> list) {
            this.context = context;
            this.list = list;
        }
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if (convertView==null){
                convertView=View.inflate(context,R.layout.item_linked,null);
                viewHolder=new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            final CheckBox check=viewHolder.check;
            final DeviceChild deviceChild=list.get(position);
            if (deviceChild!=null){
                int type=deviceChild.getType();
                if (type==2){/**取暖器*/
                    viewHolder.image_linked.setImageResource(R.mipmap.warmer);
                    viewHolder.tv_linked.setText(deviceChild.getName());
                    int linked=deviceChild.getLinked();
                    if (linked==1){
                        check.setChecked(true);
                    }else if (linked==0){
                        check.setChecked(false);
                    }
                }
            }
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (check.isChecked()){
                        deviceChild.setLinked(1);
                        int deviceId=deviceChild.getDeviceId();
                        linkedMap.put(deviceId,deviceChild);
                    }else {
                        deviceChild.setLinked(0);
                        int deviceId=deviceChild.getDeviceId();
                        linkedMap.put(deviceId,deviceChild);
                    }
                }
            });
            return convertView;
        }
        class ViewHolder{
            @BindView(R.id.image_linked) ImageView image_linked;
            @BindView(R.id.tv_linked) TextView tv_linked;
            @BindView(R.id.check) CheckBox check;
            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
    }
    private String linkedUrl = HttpUtils.ipAddress + "/family/device/sensors/getDevicesInRoom";
    class GetLinkedAsync extends AsyncTask<Void, Void, List<DeviceChild>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<DeviceChild> doInBackground(Void... voids) {
            int code = 0;
            List<DeviceChild> list=new ArrayList<>();
            int Id = sensorChild.getDeviceId();
            int type = sensorChild.getType();
            long roomId = sensorChild.getRoomId();
            String url = linkedUrl + "?deviceId=" + Id + "&deviceType=" + type + "&roomId=" + roomId;
            String result = HttpUtils.getOkHpptRequest(url);
            Log.i("GetLinkedAsync", "-->" + result);
            try {
                if (!TextUtils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    String returnCode = jsonObject.getString("returnCode");
                    code = Integer.parseInt(returnCode);
                    if ("100".equals(returnCode)) {
                        JSONArray returnData = jsonObject.getJSONArray("returnData");
                        for (int i = 0; i < returnData.length(); i++) {
                            if (list.size()>8){
                                break;
                            }
                            JSONObject device = returnData.getJSONObject(i);
                            int deviceId = device.getInt("deviceId");
                            int isLinked = device.getInt("isLinked");
                            DeviceChild deviceChild = deviceChildDao.findDeviceByDeviceId(houseId, roomId, deviceId);
                            deviceChild.setLinked(isLinked);
                            deviceChild.setLinkedSensorId(sensorId);
                            linkedMap.put(deviceId,deviceChild);
                            deviceChildDao.update(deviceChild);
                            if (!list.contains(deviceChild)){
                                list.add(deviceChild);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<DeviceChild> linkedList) {
            super.onPostExecute(linkedList);
            if (linkedList!=null && !linkedList.isEmpty()){
                list.clear();
                list.addAll(linkedList);
                adapter.notifyDataSetChanged();
            }
        }
    }
    class ChooseAsync extends AsyncTask<Map<String,Object>,Void,Integer>{
        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code=0;
            Map<String,Object> params=maps[0];
            String result=HttpUtils.postOkHpptRequest(chooseDevicesIp, params);
            if (!TextUtils.isEmpty(result)){
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)){
                        code=100;
                        for (Map.Entry<Integer, DeviceChild> entry : linkedMap.entrySet()) {
                            DeviceChild deviceChild=entry.getValue();
                            deviceChild.setLinkedSensorId(sensorId);
                            int linked=deviceChild.getLinked();
                            deviceChild.setLinked(linked);
                            deviceChildDao.update(deviceChild);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            if (code==100){
                Toast.makeText(SmartLinkedActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                List<DeviceChild> list=new ArrayList<>();
                for (Map.Entry<Integer, DeviceChild> entry : linkedMap.entrySet()) {
                    DeviceChild deviceChild=entry.getValue();
                    list.add(deviceChild);
                }
                Intent intent=new Intent();
                intent.putExtra("list",(Serializable) list);
                setResult(100,intent);
                finish();
            }else {
                Toast.makeText(SmartLinkedActivity.this,"设置失败",Toast.LENGTH_SHORT).show();
            }
        }
    }
    class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress=intent.getStringExtra("macAddress");
            DeviceChild deviceChild= (DeviceChild) intent.getSerializableExtra("deviceChild");
            if (deviceChild==null && macAddress.equals(sensorChild.getMacAddress())){
                Toast.makeText(SmartLinkedActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
                Intent data = new Intent(SmartLinkedActivity.this, MainActivity.class);
                data.putExtra("houseId", houseId);
                startActivity(data);
            }else if (deviceChild==null&&(!macAddress.equals(sensorChild.getMacAddress()))){
                DeviceChild deviceChild3=null;
                for(DeviceChild deviceChild2:list){
                    if (macAddress.equals(deviceChild2.getMacAddress())){
                        deviceChild3=deviceChild2;
                        break;
                    }
                }
                if (deviceChild3!=null){
                    String name=deviceChild3.getName();
                    Utils.showToast(SmartLinkedActivity.this,name+"设备已重置");
                    list.remove(deviceChild3);
                    if (linkedMap.containsKey(macAddress)){
                        linkedMap.remove(deviceChild3);
                    }
                    if (!list.isEmpty()){
                        adapter.notifyDataSetChanged();
                    }else {
                        Intent intent2=new Intent();
                        intent2.putExtra("list",(Serializable) list);
                        SmartLinkedActivity.this.setResult(100,intent2);
                        finish();
                    }
                }
            }
        }
    }
}
