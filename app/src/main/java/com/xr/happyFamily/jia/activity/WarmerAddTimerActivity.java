package com.xr.happyFamily.jia.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.weigan.loopview.LoopView;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.jia.pojo.TimerTask;
import com.xr.happyFamily.together.util.mqtt.MQService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.jessyan.autosize.internal.CustomAdapt;

public class WarmerAddTimerActivity extends AppCompatActivity implements CustomAdapt {

    Unbinder unbinder;
    private List<String> hours=new ArrayList<>();
    private List<String> mins=new ArrayList<>();
    private List<String> temps=new ArrayList<>();
    @BindView(R.id.lv_start_hour)
    LoopView lv_start_hour;
    @BindView(R.id.lv_start_min) LoopView lv_start_min;
    @BindView(R.id.lv_end_hour) LoopView lv_end_hour;
    @BindView(R.id.lv_end_min) LoopView lv_end_min;
    @BindView(R.id.lv_temp) LoopView lv_temp;
    private String deviceMac;
    private int week;
    private List<TimerTask> weekTimerTask;
    private int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warmer_add_timer);
        unbinder= ButterKnife.bind(this);
        Intent intent=getIntent();
        index=intent.getIntExtra("index",0);
        week=intent.getIntExtra("week",0);
        deviceMac=intent.getStringExtra("deviceMac");
        weekTimerTask= (List<TimerTask>) intent.getSerializableExtra("weekTimerTask");
        for (int i = 0; i <24 ; i++) {
            hours.add(i+"");
        }

        for (int i = 0; i <60 ; i++) {
            mins.add(i+"");
        }
        for (int i = 10; i <46 ; i++) {
            temps.add(i+"");
        }

        lv_start_hour.setItems(hours);
        lv_start_hour.setCenterTextColor(getResources().getColor(R.color.heater_orange));
//        lv_start_hour.setTextSize(getResources().getDimension(R.dimen.sp_16));
        lv_start_hour.setOuterTextColor(getResources().getColor(R.color.heater_black));
        lv_start_hour.setItemsVisibleCount(5);

        lv_start_min.setItems(mins);
        lv_start_min.setCenterTextColor(getResources().getColor(R.color.heater_orange));
//        lv_start_min.setTextSize(getResources().getDimension(R.dimen.sp_16));
        lv_start_min.setOuterTextColor(getResources().getColor(R.color.heater_black));
        lv_start_min.setItemsVisibleCount(5);

        lv_end_hour.setItems(hours);
        lv_end_hour.setCenterTextColor(getResources().getColor(R.color.heater_orange));
//        lv_end_hour.setTextSize(getResources().getDimension(R.dimen.sp_16));
        lv_end_hour.setOuterTextColor(getResources().getColor(R.color.heater_black));
        lv_end_hour.setItemsVisibleCount(5);

        lv_end_min.setItems(mins);
        lv_end_min.setCenterTextColor(getResources().getColor(R.color.heater_orange));
//        lv_end_min.setTextSize(getResources().getDimension(R.dimen.sp_16));
        lv_end_min.setOuterTextColor(getResources().getColor(R.color.heater_black));
        lv_end_min.setItemsVisibleCount(5);

        lv_temp.setItems(temps);
        lv_temp.setCenterTextColor(getResources().getColor(R.color.heater_orange));
//        lv_temp.setTextSize(getResources().getDimension(R.dimen.sp_16));
        lv_temp.setOuterTextColor(getResources().getColor(R.color.heater_black));
        lv_temp.setItemsVisibleCount(5);
    }


    @OnClick({R.id.img_back,R.id.btn_submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_submit:
                int startHour=Integer.parseInt(hours.get(lv_start_hour.getSelectedItem()));
                int startMin=Integer.parseInt(mins.get(lv_start_min.getSelectedItem()));
                int endHour=Integer.parseInt(hours.get(lv_end_hour.getSelectedItem()));
                int endMin=Integer.parseInt(mins.get(lv_end_min.getSelectedItem()));
                int temp=Integer.parseInt(temps.get(lv_temp.getSelectedItem()));
                int start=startHour*60+startMin;
                int end=endHour*60+endMin;
                if (end<=start){
                    ToastUtil.showShortToast("定时结束时间要大于开始时间");
                    break;
                }
                int flag=0;//1为定时没有重叠可添加或者修改，2为定时有重叠不能添加
                for (int i = 0; i <4 ; i++) {
                    TimerTask timerTask=weekTimerTask.get(i);
                    int hour=timerTask.getHour();
                    int min=timerTask.getMin();
                    int hour2=timerTask.getHour2();
                    int min2=timerTask.getMin2();
                    int start2=hour*60+min;
                    int end2=hour2*60+min2;
                    if (end<start2 || end2<start){
                        flag=1;
                    }else {
                        flag=2;
                        break;
                    }
                }
                if (flag==2){
                    ToastUtil.showShortToast("该定时时间段已存在");
                    break;
                }
                TimerTask timerTask=weekTimerTask.get(index);
                timerTask.setDeviceMac(deviceMac);
                timerTask.setOpen(1);
                timerTask.setHour(startHour);
                timerTask.setMin(startMin);
                timerTask.setHour2(endHour);
                timerTask.setMin2(endMin);
                timerTask.setTemp(temp);

                weekTimerTask.set(index,timerTask);
                Intent intent=new Intent();
                intent.putExtra("weekTimerTask", (Serializable) weekTimerTask);
                intent.putExtra("index",index);
                setResult(100,intent);
                finish();
                break;
        }
    }




    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 667;
    }
}
