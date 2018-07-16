package com.xr.happyFamily.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.adapter.FamilyAdapter;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.together.util.mqtt.MQService;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FamilyFragmentManager extends Fragment {
    View view;
    List<Fragment> fragmentList;
    private RoomDaoImpl roomDao;
    private long houseId;
    private long roomId;
    ViewPager viewPager;
    List<Room> rooms;
    private SharedPreferences mPositionPreferences;
    SharedPreferences roomPreferences;
    public static boolean running=false;
    private DeviceChildDaoImpl deviceChildDao;
    private List<DeviceChild> shareChildren;
    SharedPreferences preferences;
    boolean isBound;
    String load;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("FamilyFragmentManager","-->onCreateView");
        if (view==null){
            view = inflater.inflate(R.layout.fragment_family_manager, container, false);
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            roomPreferences = getActivity().getSharedPreferences("room", MODE_PRIVATE);
            mPositionPreferences = getActivity().getSharedPreferences("position", MODE_PRIVATE);
            Bundle bundle = getArguments();
            houseId = bundle.getLong("houseId");
            load=bundle.getString("load");
            roomDao = new RoomDaoImpl(getActivity());
            deviceChildDao=new DeviceChildDaoImpl(getActivity());
            rooms = new ArrayList<>();
            List<Room> allRoomInHouse = roomDao.findAllRoomInHouse(houseId);
            preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
            String userIdStr=preferences.getString("userId","");
            if (!TextUtils.isEmpty(userIdStr)){
                int userId=Integer.parseInt(userIdStr);
                shareChildren=deviceChildDao.findShareDevice(userId);
            }
            for (int i = 0; i < allRoomInHouse.size(); i++) {
                Room room = allRoomInHouse.get(i);
                rooms.add(room);
            }
            fragmentList = new ArrayList<>();
            if (rooms.isEmpty() && shareChildren.isEmpty()) {
                NoRoomFragment noRoomFragment = new NoRoomFragment();
                noRoomFragment.setHouseId(houseId);
                fragmentList.add(noRoomFragment);
            } else {
                FamilyFragment familyFragment = new FamilyFragment();
                familyFragment.setHouseId(houseId);
                fragmentList.add(familyFragment);
            }
            Log.i("roomtList", "-->" + rooms.size());
            for (int i = 0; i < rooms.size(); i++) {
                RoomFragment roomFragment=new RoomFragment();
                roomFragment.setRoomId(rooms.get(i).getRoomId());
                fragmentList.add(roomFragment);
                Log.i("qqqqqq",rooms.get(i).getRoomName());
            }
            Log.i("fragmentList", "-->" + fragmentList.size());
            FragmentStatePagerAdapter adapter = new FamilyAdapter(getFragmentManager(), fragmentList);
            viewPager.setAdapter(adapter);
            MyOnPageChangeListener listener = new MyOnPageChangeListener(getActivity(), viewPager, fragmentList.size());
            viewPager.addOnPageChangeListener(listener);
            if (mPositionPreferences.contains("position")) {
                int position = mPositionPreferences.getInt("position", 0);
                Log.i("mPositionPreferences","-->"+position);
                viewPager.setCurrentItem(position);
                if (callValueValue != null) {
                    callValueValue.setPosition(position);
                }
                listener.onPageSelected(position);
            } else {
                viewPager.setCurrentItem(0);
                listener.onPageSelected(0);
            }
        }
        Intent service = new Intent(getActivity(), MQService.class);
        isBound = getActivity().bindService(service, connection, Context.BIND_AUTO_CREATE);
        return view;
    }
    MQService mqService;
    boolean bound;
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
            bound = true;
//            if (!TextUtils.isEmpty(load)){
//                List<DeviceChild> deviceChildren=deviceChildDao.findHouseDevices(houseId);
//                new LoadDevicesAsync().execute(deviceChildren);
//            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    class
    LoadDevicesAsync extends AsyncTask<List<DeviceChild>,Void,Void>{

        @Override
        protected Void doInBackground(List<DeviceChild>... lists) {
            List<DeviceChild> list=lists[0];
            for (DeviceChild deviceChild : list) {
                String macAddress = deviceChild.getMacAddress();
                int type=deviceChild.getType();
                String onlineTopicName="";
                String offlineTopicName="";
                switch (type){
                    case 2:
                        onlineTopicName = "p99/warmer/" + macAddress + "/transfer";
                        offlineTopicName="p99/warmer/"+macAddress+"/lwt";
                        break;
                    case 3:
                        onlineTopicName="p99/sensor1/"+macAddress+"/transfer";
                        offlineTopicName="p99/sensor1/"+macAddress+"/lwt";
                        break;
                }
                try {
                    if (bound){
                        Log.i("topic","-->"+onlineTopicName);
                        boolean step1=mqService.subscribe(onlineTopicName,1);
                        boolean step2=mqService.subscribe(offlineTopicName,1);
                        Log.i("step","-->"+step1+","+step2);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("FamilyFragmentManager","-->onStart");
        running=true;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callValueValue = (CallValueValue) getActivity();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view!=null){
            if (null != view) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
        if (isBound){
            getActivity().unbindService(connection);
        }
        running=false;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("FamilyFragmentManager","-->onStop");
        running=false;
    }

    //实现页面变化监听器OnPageChangeListener
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private Context context;
        private ViewPager viewPager;
        private int size;

        public MyOnPageChangeListener(Context context, ViewPager viewPager, int size) {
            this.context = context;
            this.viewPager = viewPager;
            this.size = size;
        }

        @Override
        //当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法会一直得到调用。
        /**
         * arg0:当前页面，及你点击滑动的页面
         * arg1:当前页面偏移的百分比
         *arg2:当前页面偏移的像素位置
         */
        public void onPageScrolled(int position, float arg1, int arg2) {

        }

        @Override
        //当页面状态改变的时候调用
        /**
         * arg0
         *  1:表示正在滑动
         *  2:表示滑动完毕
         *  0:表示什么都没做，就是停在那
         */
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            Log.i("onPageScroll", "-->" + arg0);
        }

        @Override
        //页面跳转完后调用此方法
        /**
         * arg0是页面跳转完后得到的页面的Position（位置编号）。
         */
        public void onPageSelected(int poistion) {
            Log.i("position","-->"+poistion);
            Log.i("size","-->"+fragmentList.size());
            if (callValueValue != null) {
                callValueValue.setPosition(poistion);
            }

            if (poistion>0){
//                Room room=rooms.get(poistion-1);
//                long houseId=room.getHouseId();
//                long roomId=room.getRoomId();
//                RoomFragment.houseId=houseId;
//                RoomFragment.roomId=roomId;
                Message message=handler.obtainMessage();
                message.what=poistion;
                handler.sendMessage(message);
//                SharedPreferences.Editor editor=mPositionPreferences.edit();
//                editor.putInt("position",poistion-1);
//                editor.commit();
            }

        }
    }
    CallValueValue callValueValue;
    public interface CallValueValue {
        public void setPosition(int position);
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                int position=msg.what;
                int sum=rooms.size();
                Log.i("mPosition","-->"+position);
                SharedPreferences.Editor editor=mPositionPreferences.edit();
                editor.putInt("position",position);
                editor.commit();
                RoomFragment.index=position-1;
                Room room=rooms.get(position-1);
                if (room!=null){
                    RoomFragment roomFragment= (RoomFragment) fragmentList.get(position);
                    RoomFragment.index=position-1;
                    long houseId=room.getHouseId();
                    long roomId=room.getRoomId();
                    String name=room.getRoomName();
                    Log.i("handleMessage", "handleMessage: "+name);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}
