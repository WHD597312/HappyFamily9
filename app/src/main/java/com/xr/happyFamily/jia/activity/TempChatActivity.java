package com.xr.happyFamily.jia.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineDataSet;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.NumberToString;
import com.xr.happyFamily.together.chart.LineChartManager;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TempChatActivity extends AppCompatActivity {

    Unbinder unbinder;
    MyApplication application;
    @BindView(R.id.line_chart) LineChart line_chart;
    @BindView(R.id.tv_time) TextView tv_time;
    private String tempChartLineUrl= HttpUtils.ipAddress+"/family/data/getDeviceWeekData";
    private long deviceId;
    private DeviceChildDaoImpl deviceChildDao;
    private DeviceChild deviceChild;
    private long houseId;
    //设置x轴的数据
    ArrayList<String> xValues = new ArrayList<>();
    MessageReceiver receiver;
    @BindView(R.id.tv_power) TextView tv_power;/**功率*/
    @BindView(R.id.tv_val) TextView tv_val;/**电压*/
    @BindView(R.id.tv_current) TextView tv_current;/**电流*/
    @BindView(R.id.tv_power_spec) TextView tv_power_spec;/**额定功率*/
    public static  boolean running=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        setContentView(R.layout.activity_temp_chat);
        unbinder= ButterKnife.bind(this);

        line_chart.setScaleEnabled(false);
        line_chart.setDoubleTapToZoomEnabled(false);

        deviceChildDao=new DeviceChildDaoImpl(getApplicationContext());
        Intent intent=getIntent();
        deviceId=intent.getLongExtra("deviceId",0);
        deviceChild=deviceChildDao.findById(deviceId);
        houseId=deviceChild.getHouseId();
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        String s=year+"-"+month+"-"+day;
        tv_time.setText(s);
        int  powerValue=deviceChild.getSocketPower()/10;
        float voltageValue2=deviceChild.getSocketVal();
        BigDecimal decimal2=new BigDecimal(voltageValue2);
        BigDecimal decimalScale2=decimal2.setScale(2,BigDecimal.ROUND_HALF_UP);
        float voltageValue3=Float.parseFloat(decimalScale2+"");
        int voltageValue= (int) voltageValue3;
        int currentValue=deviceChild.getSocketCurrent();
        float currentValue2=Float.parseFloat(currentValue+"");
        float currentValue3=currentValue2/1000;
        BigDecimal decimal=new BigDecimal(currentValue3);
        BigDecimal decimalScale=decimal.setScale(2,BigDecimal.ROUND_HALF_UP);
        String ss=decimalScale+"";

        powerValue=(int) (voltageValue*Float.parseFloat(ss));
        tv_power.setText("功率："+powerValue+"W");
        tv_val.setText("电压："+voltageValue+"V");
        tv_current.setText("电流："+decimalScale+"A");
        IntentFilter intentFilter = new IntentFilter("TempChatActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);
        new LoadChartAsync().execute();
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
        running=true;
        deviceChild = deviceChildDao.findById(deviceId);
        if (deviceChild==null){
            Toast.makeText(TempChatActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
            Intent data = new Intent(TempChatActivity.this, MainActivity.class);
            data.putExtra("houseId", houseId);
            startActivity(data);
        }
    }


    /**
     * 返回键功能
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    class LoadChartAsync extends AsyncTask<Void,Void,List<Integer>>{
        @Override
        protected List<Integer> doInBackground(Void... voids) {
            int code=0;
            int deviceId=deviceChild.getDeviceId();
            String url=tempChartLineUrl+"?deviceId="+deviceId+"&dataType=9";
            String result=HttpUtils.getOkHpptRequest(url);
            List<Integer> list=new ArrayList<>();
            if (result!=null){
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    code=Integer.parseInt(returnCode);
                    if (100==code){
                        JSONObject returnData=jsonObject.getJSONObject("returnData");
                        int mondayKWH=returnData.getInt("monday");
                        int tuesdayKWH=returnData.getInt("tuesday");
                        int wednesdayKWH=returnData.getInt("wednesday");
                        int thursdayKWH=returnData.getInt("thursday");
                        int fridayKWH=returnData.getInt("friday");
                        int saturdayKWH=returnData.getInt("saturday");
                        int sundayKWH=returnData.getInt("sunday");
                        list.add(mondayKWH);
                        list.add(tuesdayKWH);
                        list.add(wednesdayKWH);
                        list.add(thursdayKWH);
                        list.add(fridayKWH);
                        list.add(saturdayKWH);
                        list.add(sundayKWH);
                    }
                    Log.i("result","-->"+result);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Integer> list) {
            super.onPostExecute(list);
            if (!list.isEmpty()){
                LineChartManager lineChartManager1 = new LineChartManager(line_chart);
                //设置x轴的数据
                ArrayList<String> xValues = new ArrayList<>();
                for (int i = 1; i <=7; i++) {
                    if (i==7){
                        xValues.add("周日");
                    }else {
                        String s1=NumberToString.toChinese(i+"");
                        xValues.add("周"+s1);
                    }
                }
                int max=Collections.max(list);
                max=max+1;
                Log.i("max","-->"+max);


                //颜色集合
                List<Integer> colours = new ArrayList<>();
                colours.add(Color.WHITE);


                //线的名字集合
                List<String> names = new ArrayList<>();
                names.add("温度曲线");


                //创建多条折线的图表
                lineChartManager1.showLineChart(TempChatActivity.this,xValues, list, names.get(0), colours.get(0));
                lineChartManager1.setDescription("");
                lineChartManager1.setYAxis(max, 0, 7);
                lineChartManager1.setHightLimitLine(max,"度",0);
//                //创建多条折线的图表
//                lineChartManager1.showLineChart(TempChatActivity.this,xValues, yValues.get(0), names.get(0), colours.get(0));
//                lineChartManager1.setDescription("");
//                lineChartManager1.setYAxis(60, 0, 7);
//                lineChartManager1.setHightLimitLine(60,"度",0);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
        if (receiver!=null){
            unregisterReceiver(receiver);
        }
        running=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        running=false;
    }

    /**
     * 设置线条填充背景颜色
     *
     * @param drawable
     */
    public void setChartFillDrawable(Drawable drawable) {
        if (line_chart.getData() != null && line_chart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) line_chart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            line_chart.invalidate();
        }
    }

    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress=intent.getStringExtra("macAddress");
            String noNet=intent.getStringExtra("noNet");
            DeviceChild deviceChild2= (DeviceChild) intent.getSerializableExtra("deviceChild");
            if (!Utils.isEmpty(noNet)){
                Utils.showToast(TempChatActivity.this,"网络已断开，请设置网络");
            }else {
                if (!Utils.isEmpty(macAddress) && deviceChild2==null && deviceChild.getMacAddress().equals(macAddress)){
                    Utils.showToast(TempChatActivity.this,"该设备已被重置");
                    Intent intent2=new Intent(TempChatActivity.this,MainActivity.class);
                    intent2.putExtra("houseId",houseId);
                    startActivity(intent2);
                }else if (!Utils.isEmpty(macAddress) && deviceChild2!=null && deviceChild.getMacAddress().equals(macAddress)){
                    deviceChild=deviceChild2;
                    deviceChildDao.update(deviceChild);
                    int  powerValue=deviceChild.getSocketPower()/10;
                    float voltageValue2=deviceChild.getSocketVal();
                    BigDecimal decimal2=new BigDecimal(voltageValue2);
                    BigDecimal decimalScale2=decimal2.setScale(2,BigDecimal.ROUND_HALF_UP);
                    float voltageValue3=Float.parseFloat(decimalScale2+"");
                    int voltageValue= (int) voltageValue3;
                    int currentValue=deviceChild.getSocketCurrent();
                    float currentValue2=Float.parseFloat(currentValue+"");
                    float currentValue3=currentValue2/1000;
                    BigDecimal decimal=new BigDecimal(currentValue3);
                    BigDecimal decimalScale=decimal.setScale(2,BigDecimal.ROUND_HALF_UP);
                    String ss=decimalScale+"";

                    powerValue=(int) (voltageValue*Float.parseFloat(ss));
                    tv_power.setText("功率："+powerValue+"W");
                    tv_val.setText("电压："+voltageValue+"V");
                    tv_current.setText("电流："+decimalScale+"A");
//                    tv_version.setText(deviceChild.getWifiVersion()+","+deviceChild.getMcuVersion());
                }
            }
        }
    }
}
