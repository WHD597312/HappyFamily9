package com.xr.happyFamily.le.BtClock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.xr.database.dao.daoimpl.TimeDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.le.adapter.ChooseTimeAdapter;
import com.xr.happyFamily.le.pojo.Time;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.together.util.mqtt.ClockService;

import android.support.v7.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class CommonClockFragment extends BaseFragment {
    View view;
    Unbinder unbinder;
    Context context;
    @BindView(R.id.commonclock_recyc)
    RecyclerView recyclerView;
   @BindView(R.id.iv_le_clock_add)
   ImageView iv_le_clock_add;
   private TimeDaoImpl timeDao;
   List<Time> times;
   Time time;
   SharedPreferences preferences;
   ChooseTimeAdapter adapter;
   String userId;
    Intent service;
    Boolean isBound;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }
    //绑定服务
    ClockService mqService;
    boolean bound;
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ClockService.LocalBinder binder = (ClockService.LocalBinder) service;
            mqService = binder.getService();
            bound = true;
            adapter.setClockService(mqService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_le_comonclock, container, false);
        unbinder = ButterKnife.bind(this, view);
        timeDao= new TimeDaoImpl(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
//        preferences= getActivity().getSharedPreferences("my",MODE_PRIVATE);
//        userId= preferences.getString("userId","");
//        Log.i("userid", "onCreateView: "+userId);
//        time.setUserId(Long.parseLong(userId));
        times = timeDao.findByAllTime();
        for (int i=0;i<times.size();i++){
            time= times.get(i);
            Log.e("time", "onCreateView:--> "+time.getOpen() );
        }
        adapter = new ChooseTimeAdapter(getActivity(),times);
        recyclerView.setAdapter(adapter);

        service = new Intent(context, ClockService.class);
        isBound = context.bindService(service, connection, Context.BIND_AUTO_CREATE);
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        if (times.size()>0){
            timeDao= new TimeDaoImpl(getActivity());
            times = timeDao.findByAllTime();
            Log.e("qqqqqqqqqNNN",times.get(0).getHour()+"");
            adapter = new ChooseTimeAdapter(getActivity(),times);
            recyclerView.setAdapter(adapter);
        }

    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isBound){
            getActivity().unbindService(connection);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.iv_le_clock_add,R.id.iv_comm_fh})
    public void  onClick(View view){
        switch (view.getId()){
            case R.id.iv_le_clock_add:
                Intent intent = new Intent(getActivity(),addTimeActivity.class);
                startActivityForResult(intent,600);
                break;
            case R.id.iv_comm_fh:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("qqqqqQQ","??????????");
        if (resultCode==600){
            times = timeDao.findByAllTime();
            adapter = new ChooseTimeAdapter(getActivity(),times);
            recyclerView.setAdapter(adapter);
        }
    }

    //控制item的间距
//    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
//        int mSpace;
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            super.getItemOffsets(outRect, view, parent, state);
////            outRect.left = mSpace;
////            outRect.right = mSpace;
//            outRect.bottom = mSpace;
//            if (parent.getChildAdapterPosition(view) == 0) {
//                outRect.top = mSpace;
//            }
//
//        }
//
//        public SpaceItemDecoration(int space) {
//            this.mSpace = space;
//        }
//    }
}