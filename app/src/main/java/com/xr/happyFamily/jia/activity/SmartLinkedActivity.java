package com.xr.happyFamily.jia.activity;

import android.content.Context;
import android.content.Intent;
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
import com.xr.happyFamily.together.http.HttpUtils;

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
    private long sensorId;/**传感器Id*/
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
        sensorId=intent.getLongExtra("sensorId",0);
        list= (List<DeviceChild>) intent.getSerializableExtra("deviceList");
        for (DeviceChild deviceChild:list){
            int deviceId=deviceChild.getDeviceId();
            linkedMap.put(deviceId,deviceChild);
        }
        adapter=new LinkdAdapter(this,list);
        list_linked.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("list",new ArrayList<>());
        setResult(100,intent);
        finish();
    }

    @OnClick({R.id.image_back,R.id.btn_ensure})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                Intent intent=new Intent();
                intent.putExtra("list",new ArrayList<>());
                setResult(100,intent);
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
                    viewHolder.tv_linked.setText("取暖器");
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

}
