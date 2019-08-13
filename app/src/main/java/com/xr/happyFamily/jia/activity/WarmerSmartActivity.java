package com.xr.happyFamily.jia.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.bean.OrderBean;
import com.xr.happyFamily.jia.adapter.MyAdapter;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.view_custom.DialogLoad;
import com.xr.happyFamily.jia.view_custom.MyDecoration;
import com.xr.happyFamily.together.http.BaseWeakAsyncTask;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.mqtt.MQService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 该activity用来设置设备的主从关系，
 * 注意：
 * 1.设置主设备的条件
 * a.主控设备只能有一个
 * b.可设置主控的设备只能在设备列表中单选
 * c.可设置的主控设备必须都在线
 * <p>
 * 2.设置从设备的条件
 * a.设置受控设备之前必须先设置主控设备
 * b.可设置的从设备，无论在线与否，都可以设置
 */
public class WarmerSmartActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.elv)
    ExpandableListView elv;
    @BindView(R.id.tv_title)
    TextView tv_title;
    private MyAdapter adapter;
    private List<List<DeviceChild>> childern = new ArrayList<>();//存储主，控设备集合的集合
    private List<DeviceChild> masterList = new ArrayList<>();//主控设备集合
    private List<DeviceChild> controlList = new ArrayList<>();//受控设备集合

    private List<DeviceChild> master = new ArrayList<>();
    private List<DeviceChild> control = new ArrayList<>();
    private MasterAdapter masterAdapter;
    private ControlAdapter controlAdapter;
    private List<Integer> ids = new ArrayList<>();//存储设备id，设置主受控设备

    private MyDecoration decoration;
    private long houseId;
    private long roomId;
    private Map<String, Object> params = new HashMap<>();//设备请求参数
    private int typeSole = 2;//2为主控设备，3为受控设备，1为普通设备
    private DeviceChildDaoImpl deviceChildDao;

    public static boolean running = false;//表示该页面正在与用户正在交互
    DeviceChild masterDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warmer_smart);
        unbinder = ButterKnife.bind(this);
        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
        Intent intent = getIntent();
        houseId = intent.getLongExtra("houseId", 0);
        roomId = intent.getLongExtra("roomId", 0);

        masterDevice = deviceChildDao.findHeaderMasterDevice(houseId, roomId);
        if (masterDevice != null) {
            masterList.add(masterDevice);
        }
        controlList = deviceChildDao.findHeaderControlDevice(houseId, roomId);
        childern.add(masterList);
        childern.add(controlList);
        adapter = new MyAdapter(this, childern);
        elv.setAdapter(adapter);
        for (int i = 0; i < 3; i++) {
            elv.expandGroup(i);
        }
        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        master = deviceChildDao.findHeaterSmartMayMasterDevcice(houseId, roomId);
        masterAdapter = new MasterAdapter(master, this);
        control = deviceChildDao.findHeaterSmartMayControlDevice(houseId, roomId);
        controlAdapter = new ControlAdapter(control, this);

        decoration = new MyDecoration();
        decoration.setMargin(getDimen(R.dimen.dp_jy_15))
                .setColor(Color.parseColor("#ACACAC"));
        Intent service = new Intent(this, MQService.class);
        bind = bindService(service, connection, Context.BIND_AUTO_CREATE);
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
        running = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind)
            unbindService(connection);
        unbinder.unbind();
    }

    CountTimer countTimer = new CountTimer(2000, 1000);

    class CountTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setLoadDialog();
            Log.e("CountDownTimer", "-->" + millisUntilFinished);
        }

        @Override
        public void onFinish() {
            if (dialogLoad != null && dialogLoad.isShowing()) {
                dialogLoad.dismiss();
            }
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    }

    //加载数据对话框
    DialogLoad dialogLoad;

    private void setLoadDialog() {
        if (dialogLoad != null && dialogLoad.isShowing()) {
            return;
        }

        dialogLoad = new DialogLoad(this);
        dialogLoad.setCanceledOnTouchOutside(false);
        dialogLoad.setLoad("正在加载,请稍后");
        dialogLoad.show();
    }

    private float getDimen(int dimenId) {
        return getResources().getDimension(dimenId);
    }

    private PopupWindow popupWindow;

    private int commit = 0;//是否提交提交过数据 2为向服务器提交选中的主控设备 3为向服务器提交的受控设备 0表示未提交过

    private boolean isPopviewShowing() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return true;
        }
        return false;
    }

    /**
     * @param type 2 设置主设备，3设置从设备
     */
    private void popup(final int type) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View view = View.inflate(this, R.layout.popview_smart, null);
        TextView tv_pop_header = view.findViewById(R.id.tv_pop_header);
        if (type == 2) {
            tv_pop_header.setText("主控设备");
        } else if (type == 3) {
            tv_pop_header.setText("受控设备");
        }
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(false);
        ImageView img_pop_cancel = view.findViewById(R.id.img_pop_cancel);
        RecyclerView rl_pop = view.findViewById(R.id.rl_pop);
        Button btn_submit = view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit = type;
                params.clear();
                ids.clear();
                if (type==2){
                    for (DeviceChild deviceChild : master) {
                        if (deviceChild.getHeaterCheck() == 1) {
                            ids.add(deviceChild.getDeviceId());
                        }
                    }
                }else if (type==3){
                    for (DeviceChild deviceChild : control) {
                        if (deviceChild.getHeaterCheck() == 1) {
                            ids.add(deviceChild.getDeviceId());
                        }
                    }
                }
                params.put("roomId", roomId);
                params.put("typeSole", type);
                params.put("ids", ids);
                new SmartDeviceAsync(WarmerSmartActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
            }
        });
        img_pop_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        if (type == 2) {
            rl_pop.setLayoutManager(new LinearLayoutManager(this));
            rl_pop.addItemDecoration(decoration);
            rl_pop.setAdapter(masterAdapter);
        } else if (type == 3) {
            rl_pop.setLayoutManager(new LinearLayoutManager(this));
            rl_pop.addItemDecoration(decoration);
            rl_pop.setAdapter(controlAdapter);
        }

        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
//        popupWindow.showAsDropDown(tv_title, 0, 0);
        popupWindow.showAtLocation(tv_title, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        backgroundAlpha(0.6f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
                if (commit == 0) {//如果没有提交过数据，无论设备是否之前是否选中过，就将这个设备设置为未选中状态
                    int size = master.size();
                    for (int i = 0; i < size; i++) {
                        DeviceChild deviceChild = master.get(i);
                        if (deviceChild.getHeaterCheck() == 1) {
                            deviceChild.setHeaterCheck(0);
                            master.set(i, deviceChild);
                        }
                    }
                    masterAdapter.notifyDataSetChanged();
                    if (commit == 0) {
                        int size2 = control.size();
                        for (int i = 0; i < size2; i++) {
                            DeviceChild deviceChild = control.get(i);
                            if (deviceChild.getHeaterCheck() == 1) {
                                deviceChild.setHeaterCheck(0);
                                control.set(i, deviceChild);
                            }
                        }
                        masterAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    //设置蒙版
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }

    class MasterAdapter extends RecyclerView.Adapter<SmartHolder> {

        private List<DeviceChild> list;
        private Context context;

        public MasterAdapter(List<DeviceChild> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @NonNull
        @Override
        public SmartHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = View.inflate(context, R.layout.item_warmer_body, null);
            return new SmartHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SmartHolder holder, final int i) {
            final DeviceChild deviceChild = list.get(i);
            int heaterCheck = deviceChild.getHeaterCheck();
            String name = deviceChild.getName();
            holder.tv_device.setText(name + "");
            holder.tv_offline.setText("在线");
            if (heaterCheck == 1) {
                holder.img_arrow.setImageResource(R.mipmap.heater_selected);
            } else {
                holder.img_arrow.setImageResource(R.mipmap.heater_unselected);
            }
            ImageView imgCheck = holder.img_arrow;
            imgCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = getItemCount();
                    for (int j = 0; j < count; j++) {
                        if (j == i)
                            continue;
                        else {
                            DeviceChild deviceChild2 = list.get(j);
                            deviceChild2.setHeaterCheck(0);
                            list.set(j, deviceChild2);
                        }
                    }
                    if (deviceChild.getHeaterCheck() == 0) {
                        deviceChild.setHeaterCheck(1);
                    } else if (deviceChild.getHeaterCheck() == 1) {
                        deviceChild.setHeaterCheck(0);
                        list.set(i, deviceChild);
                    }

                    masterAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class ControlAdapter extends RecyclerView.Adapter<SmartHolder> {

        private List<DeviceChild> list;
        private Context context;

        public ControlAdapter(List<DeviceChild> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @NonNull
        @Override
        public SmartHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = View.inflate(context, R.layout.item_warmer_body, null);
            return new SmartHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SmartHolder holder, final int i) {
            final DeviceChild deviceChild = list.get(i);
            int heaterCheck = deviceChild.getHeaterCheck();
            String name = deviceChild.getName();
            holder.tv_device.setText(name + "");
            boolean online = deviceChild.getOnline();
            if (online) {
                holder.tv_offline.setText("在线");
            } else {
                holder.tv_offline.setText("离线");
            }

            if (heaterCheck == 1) {
                holder.img_arrow.setImageResource(R.mipmap.heater_selected);
            } else {
                holder.img_arrow.setImageResource(R.mipmap.heater_unselected);
            }
            ImageView imgCheck = holder.img_arrow;
            imgCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deviceChild.getHeaterCheck() == 0) {
                        deviceChild.setHeaterCheck(1);
                    } else if (deviceChild.getHeaterCheck() == 1) {
                        deviceChild.setHeaterCheck(0);
                        list.set(i, deviceChild);
                    }
                    controlAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private int operatePosition;//1表示正在设置主控设备，2表示正在设置从设备

    class MyAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<List<DeviceChild>> childern;

        public MyAdapter(Context context, List<List<DeviceChild>> childern) {
            this.context = context;
            this.childern = childern;
        }

        @Override
        public int getGroupCount() {
            return 3;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            try {
                if (groupPosition == 0) {
                    return childern.get(0).size();
                } else if (groupPosition == 1) {
                    return 0;
                } else if (groupPosition == 2) {
                    return childern.get(1).size();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public String getGroup(int groupPosition) {
            if (groupPosition == 0) {
                return "主控设备";
            } else if (groupPosition == 1) {
                return "";
            } else if (groupPosition == 2) {
                return "受控设备";
            }
            return null;
        }

        @Override
        public DeviceChild getChild(int groupPosition, int childPosition) {
            try {
                if (groupPosition == 0) {
                    return childern.get(0).get(childPosition);
                } else if (groupPosition == 2) {
                    return childern.get(1).get(childPosition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder = null;

            if (groupPosition == 0 || groupPosition == 2) {
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_warmer_header, null);
                    holder = new GroupHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (GroupHolder) convertView.getTag();
                }

                String group = getGroup(groupPosition);
                holder.tv_header.setText(group);
                holder.img_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (groupPosition == 0) {
                            if (master.isEmpty()) {
                                ToastUtil.showShortToast("在线设备数量不足");
                            } else {
                                operatePosition = 1;
                                master.clear();
                                master.addAll(deviceChildDao.findHeaterSmartMayMasterDevcice(houseId, roomId));
                                popup(2);
                            }
                        } else if (groupPosition == 2) {
                            masterDevice = deviceChildDao.findHeaderMasterDevice(houseId, roomId);
                            control.clear();
                            control.addAll(deviceChildDao.findHeaterSmartMayControlDevice(houseId, roomId));
                            if (masterDevice != null) {
                                operatePosition = 2;
                                popup(3);
                            } else {
                                ToastUtil.showShortToast("请先设置主控设备");
                            }
                        }
                    }
                });
                if (groupPosition == 0) {
                    holder.rl_header.setBackground(context.getResources().getDrawable(R.drawable.shape_master));
                } else if (groupPosition == 2) {
                    holder.rl_header.setBackground(context.getResources().getDrawable(R.drawable.shape_control));
                }
            } else if (groupPosition == 1) {
                if (convertView == null)
                    convertView = View.inflate(context, R.layout.item_driver, null);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_warmer_body, null);
                holder = new ChildHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            holder.img_arrow.setImageResource(R.mipmap.heater_arrow);
            DeviceChild deviceChild = null;
            if (groupPosition == 0) {
                deviceChild = childern.get(0).get(childPosition);
            } else if (groupPosition == 2) {
                deviceChild = childern.get(1).get(childPosition);
            }
            if (deviceChild != null) {
                String name = deviceChild.getName();
                holder.tv_device.setText(name + "");
                boolean online = deviceChild.getOnline();
                if (online) {
                    holder.tv_offline.setText("在线");
                } else {
                    holder.tv_offline.setText("离线");
                }
            }
            if (isLastChild) {
                holder.view2.setVisibility(View.GONE);
            } else {
                holder.view2.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    class GroupHolder {
        @BindView(R.id.rl_header)
        RelativeLayout rl_header;
        @BindView(R.id.tv_header)
        TextView tv_header;
        @BindView(R.id.img_add)
        ImageView img_add;

        public GroupHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ChildHolder {

        @BindView(R.id.tv_device)
        TextView tv_device;
        @BindView(R.id.tv_offline)
        TextView tv_offline;
        @BindView(R.id.view2)
        View view2;
        @BindView(R.id.img_arrow)
        ImageView img_arrow;

        public ChildHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class SmartHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_device)
        TextView tv_device;
        @BindView(R.id.tv_offline)
        TextView tv_offline;
        @BindView(R.id.img_arrow)
        ImageView img_arrow;
        @BindView(R.id.view2)
        View view2;

        public SmartHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class GetSmartDeviceAsync extends BaseWeakAsyncTask<Map<String, Object>, Void, Integer, WarmerSmartActivity> {

        public GetSmartDeviceAsync(WarmerSmartActivity warmerSmartActivity) {
            super(warmerSmartActivity);
        }

        @Override
        protected Integer doInBackground(WarmerSmartActivity warmerSmartActivity, Map<String, Object>... maps) {
            int code = 0;
            try {
                code = 0;
                Map<String, Object> map = maps[0];
                String url = HttpUtils.ipAddress + "/family/device/getDeviceSoleList";
                String result = HttpUtils.requestPost(url, map);
                if (!TextUtils.isEmpty(result)) {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray returnData = jsonObject.getJSONArray("returnData");
                    int size = returnData.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject jsonObject2 = returnData.getJSONObject(i);
                        String deviceMac = jsonObject2.getString("deviceMac");
                        DeviceChild deviceChild = deviceChildDao.findDeviceByMacAddress2(deviceMac);
                        if (typeSole == 1) {
                            masterList.clear();
                            masterList.add(deviceChild);
                        } else if (typeSole == 2) {
                            controlList.clear();
                            controlList.add(deviceChild);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(WarmerSmartActivity warmerSmartActivity, Integer integer) {
            if (typeSole == 2) {
                masterAdapter.notifyDataSetChanged();
                typeSole = 3;
                params.put("roomId", roomId);
                params.put("typeSole", typeSole);
                new GetSmartDeviceAsync(WarmerSmartActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
            } else if (typeSole == 3) {
                controlAdapter.notifyDataSetChanged();
            }
        }
    }

    class SmartDeviceAsync extends BaseWeakAsyncTask<Map<String, Object>, Void, Integer, WarmerSmartActivity> {

        public SmartDeviceAsync(WarmerSmartActivity warmerSmartActivity) {
            super(warmerSmartActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            countTimer.start();
        }

        @Override
        protected Integer doInBackground(WarmerSmartActivity warmerSmartActivity, Map<String, Object>... maps) {
            int code = 0;
            Map<String, Object> map = maps[0];
            String url = HttpUtils.ipAddress + "/family/device/updateDeviceSole";
            String result = HttpUtils.requestPost(url, map);
            try {
                if (!TextUtils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("returnCode");
                    if (code == 100) {
                        if (commit == 2) {
                            if (masterDevice != null && masterDevice.getOnline() == false) {//如果之前设置的主控设备不在线，
                                masterDevice.setHeaterControl(1);
                                deviceChildDao.update(masterDevice);
                            }
                            int size = master.size();
                            masterList.clear();//清除掉非受控的在线设备列表，重新设置
                            DeviceChild newMaserDevice=null;//新的主控设备
                            for (int i = 0; i < size; i++) {
                                DeviceChild deviceChild = master.get(i);
                                if (masterDevice!=null &&deviceChild.getHeaterCheck()==1 && !deviceChild.getMacAddress().equals(masterDevice.getMacAddress())){
                                    newMaserDevice=deviceChild;//如果之前的主控设备和现在设置主控设备不一样，就停止循环
                                    break;
                                }
                                if (deviceChild.getHeaterCheck() == 1) {//当该设备被设置为主控设备，那么就发主控的主控的设备的命令
                                    deviceChild.setHeaterControl(2);
                                    mqService.sendData(deviceChild, 4);
                                    masterList.add(deviceChild);
                                } else {
                                    deviceChild.setHeaterControl(1);
                                    mqService.sendData(deviceChild, 4);
                                }
                                deviceChildDao.update(deviceChild);
                                master.set(i, deviceChild);
                            }
                            if (newMaserDevice!=null){//如果更换了新的主控设备，就将这个房间下的其他设备变为普通设备，将该设备设为主控设备，并且将该主设备添加到主控设备列表中去,将受控设备列表清空
                                List<DeviceChild> list=deviceChildDao.findHeaterDevice(houseId,roomId);
                                controlList.clear();
                                for(DeviceChild deviceChild:list){
                                    if (newMaserDevice.getMacAddress().equals(deviceChild.getMacAddress())){
                                        deviceChild.setHeaterControl(2);
                                        mqService.sendData(deviceChild, 4);
                                        masterList.add(deviceChild);
                                        deviceChildDao.update(deviceChild);
                                    }else {
                                        deviceChild.setHeaterControl(1);
                                        mqService.sendData(deviceChild, 4);
                                        deviceChildDao.update(deviceChild);
                                    }
                                }
                            }
                            if (masterList.isEmpty()){
                                controlList.clear();
                                List<DeviceChild> list=deviceChildDao.findHeaterDevice(houseId,roomId);
                                for(DeviceChild deviceChild:list){
                                    deviceChild.setHeaterControl(1);
                                    mqService.sendData(deviceChild, 4);
                                    deviceChildDao.update(deviceChild);
                                }
                            }
                        } else if (commit == 3) {
                            int size = control.size();
                            controlList.clear();
                            for (int i = 0; i < size; i++) {
                                DeviceChild deviceChild = control.get(i);
                                if (deviceChild.getHeaterCheck() == 1) {
                                    deviceChild.setHeaterControl(3);
                                    mqService.sendData(deviceChild, 4);
                                    controlList.add(deviceChild);
                                } else {
                                    deviceChild.setHeaterControl(1);
                                    deviceChild.setHeaterCheck(0);
                                    mqService.sendData(deviceChild, 4);
                                }
                                deviceChildDao.update(deviceChild);
                                control.set(i, deviceChild);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(WarmerSmartActivity warmerSmartActivity, Integer code) {
            if (code == 100) {
                ToastUtil.showShortToast("设置成功");
                if (commit == 2) {
                    childern.set(0, masterList);
                    childern.set(1,controlList);
                    adapter.notifyDataSetChanged();
                } else if (commit == 3) {
                    childern.set(1, controlList);
                    adapter.notifyDataSetChanged();
                }
                commit = 0;
            }else {
                ToastUtil.showShortToast("设置失败");
            }
        }
    }

    private MQService mqService;
    private boolean bind;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
