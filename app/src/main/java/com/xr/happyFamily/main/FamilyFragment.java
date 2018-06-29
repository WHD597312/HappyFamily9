package com.xr.happyFamily.main;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.xr.happyFamily.jia.adapter.GridViewAdapter;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.together.http.HttpUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class FamilyFragment  extends Fragment{
    private String ip=HttpUtils.ipAddress;
    View view;
    Unbinder unbinder;
    public static final int MREQUEST_CODE=6000;
    private int clicked=-1;
    private long houseId;
    @BindView(R.id.rl_home_xnty) RelativeLayout rl_home_xnty;/**虚拟体验*/
    @BindView(R.id.tv_my_hourse) TextView tv_my_hourse;/**家庭名称*/
    @BindView(R.id.gv_home_my) MyGridview gv_home_my;/**常用设备*/

    private DeviceChildDaoImpl deviceChildDao;

    private List<DeviceChild> commonDevices;

    private SharedPreferences preferences;
    private int userId;
    private GridViewAdapter mGridViewAdapter;
    private HourseDaoImpl hourseDao;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_room_famliy,container,false);
        unbinder=ButterKnife.bind(this,view);

        preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
        String strUserId=preferences.getString("userId","");
        userId=Integer.parseInt(strUserId);

        deviceChildDao=new DeviceChildDaoImpl(getActivity());
        hourseDao=new HourseDaoImpl(getActivity());
        Hourse hourse=hourseDao.findById(houseId);
        String name=hourse.getHouseName();
        tv_my_hourse.setText(name);
        commonDevices=deviceChildDao.findHouseCommonDevices(houseId);
        Log.i("commonDevices","-->"+commonDevices.size());
        mGridViewAdapter = new GridViewAdapter(getActivity(), R.layout.activity_home_item, commonDevices);
        gv_home_my.setAdapter(mGridViewAdapter);
        gv_home_my.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceChild deviceChild = commonDevices.get(position);
                String deviceName = deviceChild.getName();
                long deviceId = deviceChild.getId();
                Intent intent = new Intent(getActivity(), DeviceDetailActivity.class);
                intent.putExtra("deviceName", deviceName);
                intent.putExtra("deviceId", deviceId);
                startActivityForResult(intent,6000);
            }
        });
        return view;
    }

    @OnClick({R.id.rl_home_xnty,R.id.tv_my_hourse,R.id.btn_add_room,R.id.image_change})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_home_xnty:
                Intent intent4=new Intent(getActivity(), ChangeEquipmentActivity.class);
                startActivity(intent4);
                break;
            case R.id.tv_my_hourse:
                Intent intent2=new Intent(getActivity(), ChooseHourseActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_add_room:
                Intent intent=new Intent(getActivity(), ManagementActivity.class);
                intent.putExtra("houseId",houseId);
                startActivityForResult(intent,MREQUEST_CODE);
                break;
            case R.id.image_change:
                Intent intent3 = new Intent(getActivity(), ChangeRoomActivity.class);
                intent3.putExtra("houseId",houseId);
                startActivityForResult(intent3,MREQUEST_CODE);
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
        if (receiver!=null){
            Log.i("RoomFragment","onStop");
            getActivity().unregisterReceiver(receiver);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress=intent.getStringExtra("macAddress");
            String deviceChild2=intent.getStringExtra("deviceChild");
            for (int i = 0; i < commonDevices.size(); i++) {
                DeviceChild deviceChild=commonDevices.get(i);
                String mac=deviceChild.getMacAddress();
                if (mac.equals(macAddress) && "null".equals(deviceChild2)){
                    mGridViewAdapter.remove(deviceChild);
                    mGridViewAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

}
