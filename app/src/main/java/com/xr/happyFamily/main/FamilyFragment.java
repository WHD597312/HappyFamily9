package com.xr.happyFamily.main;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.DeviceChildDao;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.ChangeEquipmentActivity;
import com.xr.happyFamily.jia.ChangeRoomActivity;
import com.xr.happyFamily.jia.ChooseHourseActivity;
import com.xr.happyFamily.jia.ManagementActivity;
import com.xr.happyFamily.jia.MyGridview;
import com.xr.happyFamily.jia.activity.DeviceDetailActivity;
import com.xr.happyFamily.jia.activity.SmartTerminalActivity;
import com.xr.happyFamily.jia.adapter.GridViewAdapter;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.view_custom.DeleteDeviceDialog;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.together.http.HttpUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class FamilyFragment extends Fragment {
    private String ip = HttpUtils.ipAddress;
    View view;
    Unbinder unbinder;
    public static final int MREQUEST_CODE = 6000;
    private int clicked = -1;
    private long houseId;
    @BindView(R.id.rl_home_xnty)
    RelativeLayout rl_home_xnty;
    /**
     * 虚拟体验
     */
    @BindView(R.id.tv_my_hourse)
    TextView tv_my_hourse;
    /**
     * 家庭名称
     */
    @BindView(R.id.gv_home_my)
    MyGridview gv_home_my;
    /**
     * 常用设备
     */
    @BindView(R.id.gv_share_view)
    MyGridview gv_share_view;
    /**
     * 分享设备
     */

    private DeviceChildDaoImpl deviceChildDao;

    private List<DeviceChild> commonDevices = new ArrayList<>();
    private List<DeviceChild> shareDevices = new ArrayList<>();

    private SharedPreferences preferences;
    private int userId;
    private GridViewAdapter mGridViewAdapter;
    /**
     * 常用设备适配器
     */
    private GridViewAdapter shareViewAdapter;
    /**
     * 分享设备适配器
     */
    private HourseDaoImpl hourseDao;

    int mPosition;
    private DeviceChild mdeledeviceChild;

    private String deleteDeviceUrl = HttpUtils.ipAddress + "/family/device/deleteDevice";
    private boolean isLongClicked = false;
    SharedPreferences mPositionPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_room_famliy, container, false);
        unbinder = ButterKnife.bind(this, view);

        preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
        String strUserId = preferences.getString("userId", "");
        userId = Integer.parseInt(strUserId);

        deviceChildDao = new DeviceChildDaoImpl(getActivity());
        hourseDao = new HourseDaoImpl(getActivity());
        Hourse hourse = hourseDao.findById(houseId);
        if (hourse != null) {
            String name = hourse.getHouseName();
            tv_my_hourse.setText(name);
            commonDevices = deviceChildDao.findHouseCommonDevices(houseId);
            shareDevices = deviceChildDao.findShareDevice(userId);

            Log.i("commonDevices", "-->" + commonDevices.size());
            shareViewAdapter = new GridViewAdapter(getActivity(), R.layout.activity_home_item, shareDevices);
            mGridViewAdapter = new GridViewAdapter(getActivity(), R.layout.activity_home_item, commonDevices);

            gv_home_my.setAdapter(mGridViewAdapter);
            gv_share_view.setAdapter(shareViewAdapter);
            gv_home_my.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DeviceChild deviceChild = commonDevices.get(position);
                    int type=deviceChild.getType();
                    boolean online=deviceChild.getOnline();
                    mPositionPreferences = getActivity().getSharedPreferences("position", MODE_PRIVATE);
                    mPositionPreferences.edit().clear().commit();
                    if (online){
                        if (type==2){
                            String deviceName = deviceChild.getName();
                            long deviceId = deviceChild.getId();
                            Intent intent = new Intent(getActivity(), DeviceDetailActivity.class);
                            intent.putExtra("deviceName", deviceName);
                            intent.putExtra("deviceId", deviceId);
                            intent.putExtra("houseId",houseId);
                            startActivityForResult(intent,6000);
                        }else if (type==3){

                            Intent intent=new Intent(getActivity(), SmartTerminalActivity.class);
                            String deviceName = deviceChild.getName();
                            long deviceId = deviceChild.getId();
                            intent.putExtra("deviceName", deviceName);
                            intent.putExtra("deviceId", deviceId);
                            intent.putExtra("houseId",houseId);
                            startActivityForResult(intent,6000);
                        }
                    }else {
                        Toast.makeText(getActivity(),"该设备离线",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            gv_share_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    mPositionPreferences = getActivity().getSharedPreferences("position", MODE_PRIVATE);
                    mPositionPreferences.edit().clear().commit();
                    mPosition = position;
                    mdeledeviceChild = shareDevices.get(position);
                    Log.i("mdeledeviceChild", "-->" + mdeledeviceChild.getDeviceId());
                    deleteDeviceDialog();
                    return true;
                }
            });

            gv_share_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DeviceChild deviceChild = shareDevices.get(position);
                    int type=deviceChild.getType();
                    boolean online=deviceChild.getOnline();
                    mPositionPreferences = getActivity().getSharedPreferences("position", MODE_PRIVATE);
                    mPositionPreferences.edit().clear().commit();
                    if (online){
                        if (type==2){
                            String deviceName = deviceChild.getName();
                            long deviceId = deviceChild.getId();
                            Intent intent = new Intent(getActivity(), DeviceDetailActivity.class);
                            intent.putExtra("deviceName", deviceName);
                            intent.putExtra("deviceId", deviceId);
                            intent.putExtra("houseId",houseId);
                            startActivityForResult(intent,6000);
                        }else if (type==3){
                            Intent intent=new Intent(getActivity(), SmartTerminalActivity.class);
                            String deviceName = deviceChild.getName();
                            long deviceId = deviceChild.getId();
                            intent.putExtra("deviceName", deviceName);
                            intent.putExtra("deviceId", deviceId);
                            intent.putExtra("houseId",houseId);
                            startActivityForResult(intent,6000);
                        }
                    }else {
                        Toast.makeText(getActivity(),"该设备离线",Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        return view;
    }

    private void deleteDeviceDialog() {
        final DeleteDeviceDialog dialog = new DeleteDeviceDialog(getActivity());
        dialog.setOnNegativeClickListener(new DeleteDeviceDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new DeleteDeviceDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                new DeleteDeviceAsync().execute();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    class DeleteDeviceAsync extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            int code = 0;
            long deviceId = 0;
            long roomId = 0;
            String userId = preferences.getString("userId", "");
            if (mdeledeviceChild != null) {
                deviceId = mdeledeviceChild.getDeviceId();
                roomId = mdeledeviceChild.getRoomId();
            }
            String url = deleteDeviceUrl + "?userId=" + userId + "&roomId=" + roomId + "&deviceId=" + deviceId;
            String result = HttpUtils.getOkHpptRequest(url);
            Log.i("result", "-->" + result);
            try {
                if (!TextUtils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    String returnCode = jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)) {
                        code = 100;
                        if (mdeledeviceChild != null) {
                            deviceChildDao.delete(mdeledeviceChild);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code) {
                case 100:
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    shareDevices.remove(mdeledeviceChild);
                    shareViewAdapter.notifyDataSetChanged();
                    break;
                default:
                    Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @OnClick({R.id.rl_home_xnty, R.id.tv_my_hourse, R.id.btn_add_room, R.id.image_change})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_home_xnty:
                Intent intent4 = new Intent(getActivity(), ChangeEquipmentActivity.class);
                startActivity(intent4);
                break;
            case R.id.tv_my_hourse:
                Intent intent2 = new Intent(getActivity(), ChooseHourseActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_add_room:
                Intent intent = new Intent(getActivity(), ManagementActivity.class);
                intent.putExtra("houseId", houseId);
                startActivityForResult(intent, MREQUEST_CODE);
                break;
            case R.id.image_change:
                Intent intent3 = new Intent(getActivity(), ChangeRoomActivity.class);
                intent3.putExtra("houseId", houseId);
                startActivityForResult(intent3, MREQUEST_CODE);
                break;
        }
    }

    public void setHouseId(long houseId) {
        this.houseId = houseId;
    }

    MessageReceiver receiver;

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("RoomFragment");
        receiver = new MessageReceiver();
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (receiver != null) {
            Log.i("RoomFragment", "onStop");
            getActivity().unregisterReceiver(receiver);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress = intent.getStringExtra("macAddress");
            String noNet = intent.getStringExtra("noNet");
            DeviceChild deviceChild2 = (DeviceChild) intent.getSerializableExtra("deviceChild");
            long sharedId = intent.getLongExtra("sharedId", 0);
            if (!TextUtils.isEmpty(noNet)) {
                for (int i = 0; i < commonDevices.size(); i++) {
                    DeviceChild deviceChild = commonDevices.get(i);
                    deviceChild.setOnline(false);
                    commonDevices.set(i, deviceChild);
                }
                mGridViewAdapter.notifyDataSetChanged();
                for (int i = 0; i < shareDevices.size(); i++) {
                    DeviceChild deviceChild = shareDevices.get(i);
                    deviceChild.setOnline(false);
                    shareDevices.set(i, deviceChild);
                }
                shareViewAdapter.notifyDataSetChanged();
            } else {
                if (deviceChild2 == null) {
                    if (sharedId == Long.MAX_VALUE) {
                        for (int i = 0; i < shareDevices.size(); i++) {
                            DeviceChild deviceChild = shareDevices.get(i);
                            String mac = deviceChild.getMacAddress();
                            if (mac.equals(macAddress) && deviceChild2 == null) {
                                Toast.makeText(getActivity(), "该设备已重置", Toast.LENGTH_SHORT).show();
                                shareViewAdapter.remove(deviceChild);
                                shareViewAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < commonDevices.size(); i++) {
                            DeviceChild deviceChild = commonDevices.get(i);
                            String mac = deviceChild.getMacAddress();
                            if (mac.equals(macAddress) && deviceChild2 != null) {
                                commonDevices.set(i, deviceChild2);
                                mGridViewAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                } else if (deviceChild2 != null) {
                    if (sharedId == Long.MAX_VALUE) {
                        for (int i = 0; i < shareDevices.size(); i++) {
                            DeviceChild deviceChild = shareDevices.get(i);
                            String mac = deviceChild.getMacAddress();
                            if (mac.equals(macAddress) && deviceChild2 != null) {
                                shareDevices.set(i, deviceChild2);
                                shareViewAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < commonDevices.size(); i++) {
                            DeviceChild deviceChild = commonDevices.get(i);
                            String mac = deviceChild.getMacAddress();
                            if (mac.equals(macAddress) && deviceChild2 != null) {
                                commonDevices.set(i, deviceChild2);
                                mGridViewAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
