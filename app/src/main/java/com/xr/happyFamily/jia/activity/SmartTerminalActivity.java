package com.xr.happyFamily.jia.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.view_custom.SmartTerminalBar;
import com.xr.happyFamily.jia.view_custom.SmartTerminalHumBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**智能终端*/
public class SmartTerminalActivity extends AppCompatActivity {

    Unbinder unbinder;
    MyApplication myApplication;
    /**
     *  -12 到6 为寒冷 / 潮湿
     *  8 到 17为舒适 / 舒适
     *  19 到 34为酷热 / 干燥
     * */
    @BindView(R.id.smartTerminalBar) SmartTerminalBar smartTerminalBar;

    @BindView(R.id.smartTerminalHumBar) SmartTerminalHumBar  smartTerminalHumBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_smart_terminal);
        unbinder=ButterKnife.bind(this);
        if (myApplication==null){
            myApplication= (MyApplication) getApplication();
            myApplication.addActivity(this);
        }
    }
    double tempCurProgress=0;/**温度进度*/
    double humCurProgress=0;/**湿度进度*/
    @Override
    protected void onStart() {
        super.onStart();
        tempCurProgress=smartTerminalBar.getCurProcess();
        humCurProgress=smartTerminalHumBar.getCurProcess();
    }

    @OnClick({R.id.image_back,R.id.smart_temp_decrease,R.id.smart_temp_add,R.id.smart_hum_decrease,R.id.smart_hum_add})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.smart_temp_decrease:
                tempCurProgress=smartTerminalBar.getCurProcess();
                tempCurProgress=tempCurProgress-1;
                if (tempCurProgress<=-12){
                    tempCurProgress=-12;
                }
                Message tempDecrease=handler.obtainMessage();
                tempDecrease.arg1=0;/**减温度标记*/
                handler.sendMessage(tempDecrease);
                break;
            case R.id.smart_temp_add:
                tempCurProgress=smartTerminalBar.getCurProcess();
                tempCurProgress=tempCurProgress+1;
                if (tempCurProgress>=34){
                    tempCurProgress=34;
                }
                Message tempAdd=handler.obtainMessage();
                tempAdd.arg1=1;/**加温度标记*/
                handler.sendMessage(tempAdd);
                break;
            case R.id.smart_hum_decrease:
                humCurProgress=smartTerminalHumBar.getCurProcess();
                humCurProgress=humCurProgress-1;
                if (humCurProgress<=-12){
                    humCurProgress=-12;
                }
                Message humDecrease=handler.obtainMessage();
                humDecrease.arg1=2;/**减温度标记*/
                handler.sendMessage(humDecrease);
                break;
            case R.id.smart_hum_add:
                humCurProgress=smartTerminalHumBar.getCurProcess();
                humCurProgress=humCurProgress+1;
                if (humCurProgress>=34){
                    humCurProgress=34;
                }
                Message humAdd=handler.obtainMessage();
                humAdd.arg1=3;/**加温度标记*/
                handler.sendMessage(humAdd);
                break;
        }
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                case 0:
                    smartTerminalBar.setmCurProcess(tempCurProgress);
                    smartTerminalBar.invalidate();
                    break;
                case 1:
                    smartTerminalBar.setmCurProcess(tempCurProgress);
                    smartTerminalBar.invalidate();
                    break;
                case 2:
                    smartTerminalHumBar.setmCurProcess(humCurProgress);
                    smartTerminalHumBar.invalidate();
                    break;
                case 3:
                    smartTerminalHumBar.setmCurProcess(humCurProgress);
                    smartTerminalHumBar.invalidate();
                    break;

            }
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (myApplication!=null){
            myApplication.removeActivity(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
