package com.xr.happyFamily.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyGridview;
import com.xr.happyFamily.jia.activity.AddDeviceActivity;
import com.xr.happyFamily.jia.activity.DeviceDetailActivity;
import com.xr.happyFamily.jia.adapter.GridViewAdapter;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.jia.view_custom.DeleteHomeDialog;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.together.http.HttpUtils;

import org.json.JSONObject;

import java.util.List;

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
    private List<DeviceChild> mGridData;
    GridViewAdapter mGridViewAdapter;
    @BindView(R.id.tv_home_manager)
    TextView tv_home_manager;/**房间管理*/
    SharedPreferences roomPreferences;
    public static int index;
    private SharedPreferences mPositionPreferences;
    Room room;
    private String deleteRoomUrl= HttpUtils.ipAddress+"/family/room/deleteRoom";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_room,container,false);
        unbinder=ButterKnife.bind(this,view);
        roomDao=new RoomDaoImpl(getActivity());
        deviceChildDao=new DeviceChildDaoImpl(getActivity());
        Log.i("index","-->"+index);
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
                    String deviceName = deviceChild.getName();
                    long deviceId = deviceChild.getId();
                    Intent intent = new Intent(getActivity(), DeviceDetailActivity.class);
                    intent.putExtra("deviceName", deviceName);
                    intent.putExtra("deviceId", deviceId);
                    startActivity(intent);
                }
            });
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @OnClick({R.id.btn_add_device,R.id.tv_home_manager})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_home_manager:
                showPopupMenu(tv_home_manager);
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
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("sssss", "onDestroyView");
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
    String title;
    Dialog dia ;
    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                title = String.valueOf(item.getTitle());
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                if ("更改房间名".equals(title)){
                    buildUpdateHomeDialog();
                }
                if ("删除房间".equals(title)){
                    deleteHomeDialog();
                }
            }
        });

        popupMenu.show();
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

//                getActivity().setResult(7000);
//                getActivity().finish();
//                Intent intent=new Intent(getActivity(), MyPaperActivity.class);
//                intent.putExtra("result","7000");
//                intent.putExtra("roomId",roomId);
//                startActivity(intent);
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
                if (com.xr.happyFamily.login.util.Utils.isEmpty(roomName)) {
                    com.xr.happyFamily.login.util.Utils.showToast(getActivity(), "住所名称不能为空");
                } else {
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
                        Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
                        break;
            }
        }
    }




}
