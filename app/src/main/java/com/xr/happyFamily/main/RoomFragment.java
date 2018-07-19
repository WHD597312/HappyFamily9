package com.xr.happyFamily.main;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.DeviceChildDao;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.ChangeRoomActivity;
import com.xr.happyFamily.jia.MyGridview;
import com.xr.happyFamily.jia.activity.AddDeviceActivity;
import com.xr.happyFamily.jia.activity.DeviceDetailActivity;
import com.xr.happyFamily.jia.activity.SmartTerminalActivity;
import com.xr.happyFamily.jia.adapter.GridViewAdapter;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.jia.view_custom.DeleteDeviceDialog;
import com.xr.happyFamily.jia.view_custom.DeleteHomeDialog;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.jia.view_custom.Timepicker3;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RoomFragment extends Fragment{
    Unbinder unbinder;
    @BindView(R.id.tv_roomname) TextView tv_roomname;
    View view;
    private long roomId;
    public static long houseId;
    int mPosition;
    private RoomDaoImpl roomDao;
    private DeviceChildDaoImpl deviceChildDao;

    private List<DeviceChild> deviceChildren;
    @BindView(R.id.gv_balcony_home)
    MyGridview mGridView;
    private List<DeviceChild> mGridData=new ArrayList<>();
    GridViewAdapter mGridViewAdapter;
    @BindView(R.id.tv_home_manager)
    TextView tv_home_manager;/**房间管理*/
    SharedPreferences roomPreferences;
    public static int index;
    private SharedPreferences mPositionPreferences;
    Room room;
    private String deleteRoomUrl= HttpUtils.ipAddress+"/family/room/deleteRoom";
    private String updateRoomURl=HttpUtils.ipAddress+"/family/room/changeRoomName";
    private String deleteDeviceUrl= HttpUtils.ipAddress+"/family/device/deleteDevice";
    int mposition;
    DeviceChild mdeledeviceChild;
    SharedPreferences my;
    public static boolean running=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("RoomFragment","onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("RoomFragment","onDestroy");
        Log.i("RoomFragment","-->"+room.getRoomType());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_room,container,false);
        unbinder=ButterKnife.bind(this,view);
        roomDao=new RoomDaoImpl(getActivity());
        deviceChildDao=new DeviceChildDaoImpl(getActivity());
        Log.i("index","-->"+index);
        mPositionPreferences = getActivity().getSharedPreferences("position", Context.MODE_PRIVATE);
        my=getActivity().getSharedPreferences("my",Context.MODE_PRIVATE);
        room=roomDao.findById(roomId);
        Log.i("roomDao","houseId:"+houseId+",roomId:"+roomId);
        if (room!=null){
            Log.i("room33333","-->"+room.getRoomId()+","+room.getRoomName());
            String name=room.getRoomName();
            houseId=room.getHouseId();
//            tv_roomname.setText(myName);
            tv_roomname.setText(name);
            deviceChildren=deviceChildDao.findHouseInRoomDevices(houseId,roomId);
            Log.i("deviceSize","-->"+deviceChildren.size());
            mGridData=deviceChildDao.findHouseInRoomDevices(houseId,roomId);
            int size=mGridData.size();
            Log.i("size","size:"+size);
            mGridViewAdapter = new GridViewAdapter(getActivity(), R.layout.activity_home_item, mGridData);
            mGridView.setAdapter(mGridViewAdapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DeviceChild deviceChild = mGridData.get(position);
                    int type=deviceChild.getType();
                    boolean online=deviceChild.getOnline();
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
            mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    mPosition=position;
                    mdeledeviceChild=mGridData.get(position);
                    Log.i("mdeledeviceChild","-->"+mdeledeviceChild.getDeviceId());
                    deleteDeviceDialog();
                    return false;
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
    MessageReceiver receiver;
    @Override
    public void onStart() {
        super.onStart();
        running=true;
        Log.i("RoomFragment","onStart");
        IntentFilter intentFilter = new IntentFilter("RoomFragment");
        receiver = new MessageReceiver();
        getActivity().registerReceiver(receiver, intentFilter);
    }
    @OnClick({R.id.balcony_li,R.id.iv_home_fh,R.id.btn_add_device,R.id.tv_home_manager})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.balcony_li:
                Intent intent3 = new Intent(getActivity(), ChangeRoomActivity.class);
                intent3.putExtra("houseId",houseId);
                startActivityForResult(intent3,6000);
                break;
            case R.id.iv_home_fh:
                Intent intent2=new Intent(getActivity(),MainActivity.class);
                intent2.putExtra("houseId",houseId);
                mPositionPreferences.edit().clear().commit();
                startActivity(intent2);
                break;
            case R.id.tv_home_manager:
                popupTimerWindow();
                break;
            case R.id.btn_add_device:
                Intent intent=new Intent(getActivity(), AddDeviceActivity.class);
                intent.putExtra("houseId",houseId);
                intent.putExtra("roomId",roomId);
                startActivityForResult(intent,6000);
                break;
        }
    }

    public void setHouseId(long houseId) {
        this.houseId = houseId;
    }

    public long getHouseId() {
        return houseId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public int getmPosition() {
        return mPosition;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        running=false;
        if (receiver!=null){
            Log.i("RoomFragment","onStop");
            getActivity().unregisterReceiver(receiver);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("sssss", "onDestroyView");
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
    String title;
    Dialog dia ;
//    private void showPopupMenu(View view) {
//        // View当前PopupMenu显示的相对View的位置
//        PopupMenu popupMenu = new PopupMenu(getContext(), view);
//        // menu布局
//        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
//        // menu的item点击事件
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                title = String.valueOf(item.getTitle());
//                return false;
//            }
//        });
//        // PopupMenu关闭事件
//        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
//            @Override
//            public void onDismiss(PopupMenu menu) {
//                if ("更改房间名".equals(title)){
//                    buildUpdateHomeDialog();
//                }
//                if ("删除房间".equals(title)){
//                    deleteHomeDialog();
//                }
//            }
//        });
//        popupMenu.show();
//    }










    private PopupWindow popupWindow;
    public void popupTimerWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }

        View view = View.inflate(getActivity(), R.layout.popview_room_homemanerge, null);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        RelativeLayout rl_room_rename= (RelativeLayout) view.findViewById(R.id.rl_room_rename);
        RelativeLayout tv_timer= (RelativeLayout) view.findViewById(R.id.rl_room_del);


        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);

//        ColorDrawable dw = new ColorDrawable(0x30000000);
//        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAsDropDown(tv_home_manager,0,20);
//        popupWindow.showAtLocation(tv_home_manager, Gravity.RIGHT, 0, 0);
        //添加按键事件监听

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_room_rename:
                        buildUpdateHomeDialog();
                        popupWindow.dismiss();
                        break;
                    case R.id.rl_room_del:
                        deleteHomeDialog();
                        popupWindow.dismiss();
                        break;
                }
            }
        };

        rl_room_rename.setOnClickListener(listener);
        tv_timer.setOnClickListener(listener);
    }










    private String houseName;
    private void deleteHomeDialog() {
        final DeleteHomeDialog dialog = new DeleteHomeDialog(getActivity());
        dialog.setOnNegativeClickListener(new DeleteHomeDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new DeleteHomeDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                new DeleteRoomAsync().execute();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    String roomName;
    private void buildUpdateHomeDialog() {
        final HomeDialog dialog = new HomeDialog(getActivity());
        dialog.setOnNegativeClickListener(new HomeDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new HomeDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                roomName = dialog.getName();
                Log.i("roomName","-->"+roomName);
                if (TextUtils.isEmpty(roomName)) {
                    Utils.showToast(getActivity(), "住所名称不能为空");
                } else {
                    new ChangeRoomNameAsyncTask().execute();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    class DeleteRoomAsync extends AsyncTask<Void,Void,Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {
            int code=0;
            String url=deleteRoomUrl+"?houseId="+houseId+"&roomId="+roomId;
            String result=HttpUtils.getOkHpptRequest(url);
            Log.i("result","-->"+result);
            try {
                if (!TextUtils.isEmpty(result)){
                    JSONObject jsonObject=new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)){
                        code=100;
                        Room room=roomDao.findById(roomId);
                        if (room!=null){
                            roomDao.delete(room);
                        }

                        List<Room> rooms=roomDao.findAllRoomInHouse(houseId);
                        if (rooms.isEmpty()){
                            SharedPreferences.Editor editor=mPositionPreferences.edit();
                            editor.clear();
                            editor.commit();
                        }else {
                            if (mPositionPreferences.contains("position")){
                                int position=mPositionPreferences.getInt("position",0);
                                if (position>1){
                                    SharedPreferences.Editor editor=mPositionPreferences.edit();
                                    editor.putInt("position",position-1);
                                    editor.commit();
                                }else if (position==1){
                                    SharedPreferences.Editor editor=mPositionPreferences.edit();
                                    editor.putInt("position",1);
                                    editor.commit();
                                }
                            }

                        }
                        deviceChildDao.deleteDeviceInHouseRoom(houseId,roomId);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return code;
        }
        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code){
                case 100:
                    Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(),MainActivity.class);
                    intent.putExtra("houseId", houseId);
                    startActivity(intent);
                    break;
                    default:
                        Toast.makeText(getActivity(),"删除失败",Toast.LENGTH_SHORT).show();
                        break;
            }
        }
    }

    class DeleteDeviceAsync extends AsyncTask<Void,Void,Integer>{
        @Override
        protected Integer doInBackground(Void... voids) {
            int code=0;
            long deviceId=0;
            String userId=my.getString("userId","");
            if (mdeledeviceChild!=null){
                deviceId= mdeledeviceChild.getDeviceId();
            }
            String url=deleteDeviceUrl+"?userId="+userId+"&roomId="+roomId+"&deviceId="+deviceId;
            String result=HttpUtils.getOkHpptRequest(url);
            Log.i("result","-->"+result);
            try {
                if (!TextUtils.isEmpty(result)){
                    JSONObject jsonObject=new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)){
                        code=100;
                        if (mdeledeviceChild!=null){
                            deviceChildDao.delete(mdeledeviceChild);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return code;
        }
        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code){
                case 100:
                    Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
                    mGridData.remove(mdeledeviceChild);
                    mGridViewAdapter.notifyDataSetChanged();
                    break;
                default:
                    Toast.makeText(getActivity(),"删除失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    class ChangeRoomNameAsyncTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code = 0;
            String url = updateRoomURl+"?roomName="+roomName +"&roomId="+roomId;
            String result = HttpUtils.getOkHpptRequest(url);
            Log.i("result2","-->"+result);
            try {
                if (!TextUtils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)){
                        code=100;
                        if (room!=null){
                            room.setRoomName(roomName);
                            roomDao.update(room);
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
            switch (code){
                case 100:
                    Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                    tv_roomname.setText(roomName);
                    break;
                    default:
                        Toast.makeText(getActivity(),"修改失败",Toast.LENGTH_SHORT).show();
                        break;
            }
        }
    }
    class MessageReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress=intent.getStringExtra("macAddress");
            String noNet=intent.getStringExtra("noNet");
            DeviceChild deviceChild2= (DeviceChild) intent.getSerializableExtra("deviceChild");
            if (!TextUtils.isEmpty(noNet)){
                for (int i = 0; i < mGridData.size(); i++) {
                    DeviceChild deviceChild=mGridData.get(i);
                    deviceChild.setOnline(false);
                    mGridData.set(i,deviceChild);
                }
                mGridViewAdapter.notifyDataSetChanged();
            }else {
                for (int i = 0; i < mGridData.size(); i++) {
                    DeviceChild deviceChild=mGridData.get(i);
                    String mac=deviceChild.getMacAddress();
                    if (mac.equals(macAddress) && deviceChild2==null){
                        Toast.makeText(getActivity(),"该设备已重置",Toast.LENGTH_SHORT).show();
                        mGridViewAdapter.remove(deviceChild);
                        mGridViewAdapter.notifyDataSetChanged();
                        break;
                    }else if (mac.equals(macAddress) && deviceChild2!=null){
                        mGridData.set(i,deviceChild2);
                        mGridViewAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

        }
    }
}
