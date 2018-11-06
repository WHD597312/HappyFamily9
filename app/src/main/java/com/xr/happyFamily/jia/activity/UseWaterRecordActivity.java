package com.xr.happyFamily.jia.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.NumberToString;
import com.xr.happyFamily.together.chart.BarChartManager;
import com.xr.happyFamily.together.http.HttpUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UseWaterRecordActivity extends AppCompatActivity {

    ArrayList<String> xValuesDay = new ArrayList<>();/**X轴day值*/
    List<Float> yValueDay = new ArrayList<>();/**Y轴day值*/

    ArrayList<String> xValuesWeek=new ArrayList<>();/**X轴week值*/
    List<Float> yValuesWeek=new ArrayList<>();/**Y轴week值*/
    ArrayList<String> xValuesMonth=new ArrayList<>();/**X轴month值*/
    private ArrayList<Float> yValuesMonth=new ArrayList<>();
    //颜色集合
    List<Integer> colours = new ArrayList<>();
    Unbinder unbinder;

    @BindView(R.id.bar_chart) BarChart barChart;
    BarChartManager barChartManager;
    @BindView(R.id.tv_day) TextView tv_day;/**day用水量*/
    @BindView(R.id.tv_week) TextView tv_week;/**week用水量*/
    @BindView(R.id.tv_month) TextView tv_month;/**month用水量*/
    @BindView(R.id.tv_date) TextView tv_date;
    @BindView(R.id.tv_num) TextView tv_num;
    @BindView(R.id.textView16) TextView textView16;
    private MyApplication application;
    String tempChartLineUrl= HttpUtils.ipAddress+"/family/data/getDeviceAllData";
    String time;
    private DeviceChildDaoImpl deviceChildDao;
    long houseId;
    long deviceId;
    private DeviceChild deviceChild;
    private float dayUseWater;
    private float weekUseWater;
    private float yearUserWater;
    MessageReceiver receiver;
    public static boolean running=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_use_water_record);
        unbinder=ButterKnife.bind(this);
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
//        BarChart barChart = (BarChart) findViewById(R.id.bar_chart);



        deviceChildDao = new DeviceChildDaoImpl(getApplicationContext());
        Intent intent = getIntent();
        deviceId = intent.getLongExtra("deviceId", 0);
        deviceChild = deviceChildDao.findById(deviceId);
        houseId = intent.getLongExtra("houseId", 0);
        new GetDeviceAllDataAsync().execute();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");

        time=format.format(date);
        tv_date.setText(time+"(本日用水量)");
        int wPurifierOutQuqlity=deviceChild.getWPurifierOutQuqlity();
        barChartManager = new BarChartManager(barChart);

        IntentFilter intentFilter = new IntentFilter("UseWaterRecordActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);
//        colours.add(Color.parseColor("#20e2ff"));
//        colours.add(Color.BLUE);
//        colours.add(Color.RED);
//        colours.add(Color.CYAN);

//        for (int i = 1; i <= 24; i++) {
//            xValuesDay.add(i+"");
//            yValueDay.add((float) (Math.random() * 80));
//        }
//        for (int i=0;i<=6;i++){
//            if (i==0){
//                xValuesWeek.add("日");
//            }else {
//                String week=NumberToString.toChinese(i+"");
//                xValuesWeek.add(week);
//            }
//            yValuesWeek.add((float) (Math.random() * 80));
//        }
//        for (int i = 1; i <=12 ; i++) {
//            xValuesMonth.add(i+"");
//            yValuesMonth.add((float) (Math.random() * 80));
//        }
//        day=1;
//        setValueDay();
    }
    private int day=0;
    private int week=0;
    private int month;
    @OnClick({R.id.image_back,R.id.tv_day,R.id.tv_week,R.id.tv_month})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.tv_day:
                if (day==1){
                    break;
                }
                tv_day.setTextColor(Color.WHITE);
                tv_week.setTextColor(Color.parseColor("#808080"));
                tv_month.setTextColor(Color.parseColor("#808080"));
                tv_day.setBackgroundResource(R.drawable.shape_blue_cirlce);
                tv_week.setBackgroundResource(R.drawable.shape_white_circle);
                tv_month.setBackgroundResource(R.drawable.shape_white_circle);
                setValueDay();
                day=1;
                week=0;
                month=0;
                tv_date.setText(time+"(本日用水量)");
                tv_num.setText(""+dayUseWater);
                textView16.setText("本日用水量(L)");
                break;
            case R.id.tv_week:
                if (week==1){
                    break;
                }
                tv_day.setTextColor(Color.parseColor("#808080"));
                tv_week.setTextColor(Color.WHITE);
                tv_month.setTextColor(Color.parseColor("#808080"));
                tv_day.setBackgroundResource(R.drawable.shape_white_circle);
                tv_week.setBackgroundResource(R.drawable.shape_blue_cirlce);
                tv_month.setBackgroundResource(R.drawable.shape_white_circle);
                setValueWeek();
                day=0;
                week=1;
                month=0;
                tv_date.setText(time+"(本周用水量)");
                tv_num.setText(""+weekUseWater);
                textView16.setText("本周用水量(L)");
                break;
            case R.id.tv_month:
                if (month==1){
                    break;
                }
                tv_day.setTextColor(Color.parseColor("#808080"));
                tv_week.setTextColor(Color.parseColor("#808080"));
                tv_month.setTextColor(Color.WHITE);
                tv_day.setBackgroundResource(R.drawable.shape_white_circle);
                tv_week.setBackgroundResource(R.drawable.shape_white_circle);
                tv_month.setBackgroundResource(R.drawable.shape_blue_cirlce);
                setValueMonth();
                day=0;
                week=0;
                month=1;
                tv_date.setText(time+"(本年用水量)");
                tv_num.setText(""+yearUserWater);
                textView16.setText("本年用水量(L)");
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.ACTION_DOWN){
            application.removeActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置day用水量
     */
    private void setValueDay(){
        if (!yValueDay.isEmpty()){
            barChartManager.showBarChart(xValuesDay, yValueDay, "", colours.get(0));
            barChartManager.setDescription("时间");
        }

    }

    /**
     * 设置week用水量
     */
    private void setValueWeek(){
        if (!yValuesWeek.isEmpty()){
            barChartManager.showBarChart(xValuesWeek, yValuesWeek, "", colours.get(0));
            barChartManager.setDescription("星期");
        }
    }

    /**
     * 设置month用水量
     */
    private void setValueMonth(){
        if (!yValuesMonth.isEmpty()){
            barChartManager.showBarChart(xValuesMonth, yValuesMonth, "", colours.get(0));
            barChartManager.setDescription("月");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        running=true;
        deviceChild = deviceChildDao.findById(deviceId);
        if (deviceChild==null){
            Toast.makeText(UseWaterRecordActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
            Intent data = new Intent(UseWaterRecordActivity.this, MainActivity.class);
            data.putExtra("houseId", houseId);
            startActivity(data);
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
    class GetDeviceAllDataAsync extends AsyncTask<Void,Void,List<List<Float>>>{

        @Override
        protected List<List<Float>> doInBackground(Void... voids) {
            List<List<Float>> list=new ArrayList<>();
            try {
                List<Float> yValuesDays=new ArrayList<>();
                List<Float> yValuesWeek=new ArrayList<>();
                List<Float> yValuesMonths=new ArrayList<>();

                int deviceId=deviceChild.getDeviceId();
                String url=tempChartLineUrl+"?deviceId="+deviceId+"&dataType=8";
                Log.i("url","-->"+url);
                String result=HttpUtils.getOkHpptRequest(url);
                Log.i("result","-->"+result);
                if (!TextUtils.isEmpty(result)){
                    JSONObject jsonObject=new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)){
                        JSONObject returnData=jsonObject.getJSONObject("returnData");
                        JSONObject deviceData=returnData.getJSONObject("deviceData");
                        double zero=deviceData.getDouble("zero");
                        double one=deviceData.getDouble("one");
                        double two=deviceData.getDouble("two");
                        double three=deviceData.getDouble("three");
                        double four=deviceData.getDouble("four");
                        double five=deviceData.getDouble("five");
                        double six=deviceData.getDouble("six");
                        double seven=deviceData.getDouble("seven");
                        double eight=deviceData.getDouble("eight");
                        double nine=deviceData.getDouble("nine");
                        double ten=deviceData.getDouble("ten");
                        double eleven=deviceData.getDouble("eleven");
                        double twelve=deviceData.getDouble("twelve");
                        double thirteen=deviceData.getDouble("thirteen");
                        double fourteen=deviceData.getDouble("fourteen");
                        double fifteen=deviceData.getDouble("fifteen");
                        double sixteen=deviceData.getDouble("sixteen");
                        double seventeen=deviceData.getDouble("seventeen");
                        double eighteen=deviceData.getDouble("eighteen");
                        double nineteen=deviceData.getDouble("nineteen");
                        double twenty=deviceData.getDouble("twenty");
                        double twentyOne=deviceData.getDouble("twentyOne");
                        double twentyTwo=deviceData.getDouble("twentyTwo");
                        double twentyThree=deviceData.getDouble("twentyThree");


//                        yValuesDays.add((float)one);
//                        yValuesDays.add((float)two);
                        yValuesDays.add((float)three);
//                        yValuesDays.add((float)four);
//                        yValuesDays.add((float)five);
                        yValuesDays.add((float)six);
//                        yValuesDays.add((float)seven);
//                        yValuesDays.add((float)eight);
                        yValuesDays.add((float)nine);
//                        yValuesDays.add((float)ten);
//                        yValuesDays.add((float)eleven);
                        yValuesDays.add((float)twelve);
//                        yValuesDays.add((float)thirteen);
//                        yValuesDays.add((float)fourteen);
                        yValuesDays.add((float)fifteen);
//                        yValuesDays.add((float)sixteen);
//                        yValuesDays.add((float)seventeen);
                        yValuesDays.add((float)eighteen);
//                        yValuesDays.add((float)nineteen);
//                        yValuesDays.add((float)twenty);
                        yValuesDays.add((float)twentyOne);
//                        yValuesDays.add((float)twentyTwo);
//                        yValuesDays.add((float)twentyThree);
                        yValuesDays.add((float)zero);

                        JSONObject deviceWeekData=returnData.getJSONObject("deviceWeekData");
                        double sunday=deviceWeekData.getDouble("sunday");
                        double monday=deviceWeekData.getDouble("monday");
                        double tuesday=deviceWeekData.getDouble("tuesday");
                        double wednesday=deviceWeekData.getDouble("wednesday");
                        double thursday=deviceWeekData.getDouble("thursday");
                        double friday=deviceWeekData.getDouble("friday");
                        double saturday=deviceWeekData.getDouble("saturday");

                        yValuesWeek.add((float) sunday);
                        yValuesWeek.add((float) monday);
                        yValuesWeek.add((float) tuesday);
                        yValuesWeek.add((float) wednesday);
                        yValuesWeek.add((float) thursday);
                        yValuesWeek.add((float) friday);
                        yValuesWeek.add((float) saturday);

                        JSONObject deviceMonthData=returnData.getJSONObject("deviceMonthData");
                        double wJanuary=deviceMonthData.getDouble("wJanuary");
                        double wFebruary=deviceMonthData.getDouble("wFebruary");
                        double wMarch=deviceMonthData.getDouble("wMarch");
                        double wApril=deviceMonthData.getDouble("wApril");
                        double wMay=deviceMonthData.getDouble("wMay");
                        double wJune=deviceMonthData.getDouble("wJune");
                        double wJuly=deviceMonthData.getDouble("wJuly");
                        double wAugust=deviceMonthData.getDouble("wAugust");
                        double wSeptember=deviceMonthData.getDouble("wSeptember");
                        double wOctober=deviceMonthData.getDouble("wOctober");
                        double wNovember=deviceMonthData.getDouble("wNovember");
                        double wDecember=deviceMonthData.getDouble("wDecember");
                        yValuesMonths.add((float)wJanuary);
                        yValuesMonths.add((float)wFebruary);
                        yValuesMonths.add((float)wMarch);
                        yValuesMonths.add((float)wApril);
                        yValuesMonths.add((float)wMay);
                        yValuesMonths.add((float)wJune);
                        yValuesMonths.add((float)wJuly);
                        yValuesMonths.add((float)wAugust);
                        yValuesMonths.add((float)wSeptember);
                        yValuesMonths.add((float)wOctober);
                        yValuesMonths.add((float)wNovember);
                        yValuesMonths.add((float)wDecember);

                        list.add(0,yValuesDays);
                        list.add(1,yValuesWeek);
                        list.add(2,yValuesMonths);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<List<Float>> lists) {
            super.onPostExecute(lists);
            if (!lists.isEmpty()){
                colours.add(Color.parseColor("#20e2ff"));
                colours.add(Color.BLUE);
                colours.add(Color.RED);
                colours.add(Color.CYAN);

                for (int i = 1; i <= 8; i++) {
                    xValuesDay.add((i*3)+"");
//                    yValueDay.add((float) (Math.random() * 80));
                }
                yValueDay.addAll(lists.get(0));
                dayUseWater=getValue(yValueDay);
                for (int i=0;i<=6;i++){
                    if (i==0){
                        xValuesWeek.add("日");
                    }else {
                        String week=NumberToString.toChinese(i+"");
                        xValuesWeek.add(week);
                    }
//                    yValuesWeek.add((float) (Math.random() * 80));
                }
                yValuesWeek.addAll(lists.get(1));
                weekUseWater=getValue(yValuesWeek);
                for (int i = 1; i <=12 ; i++) {
                    xValuesMonth.add(i+"");
//                    yValuesMonth.add((float) (Math.random() * 80));
                }
                yValuesMonth.addAll(lists.get(2));
                yearUserWater=getValue(yValuesMonth);
                day=1;
                setValueDay();
                tv_num.setText(""+dayUseWater);
                textView16.setText("今日用水量(L)");
            }
        }
    }
    private float getValue(List<Float> list){
        float x=0;
        for (int i = 0; i <list.size() ; i++) {
            x=x+list.get(i);
        }
        return x;
    }
    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String macAddress = intent.getStringExtra("macAddress");
            Log.i("macAddress", "-->" + macAddress);
            DeviceChild deviceChild2 = (DeviceChild) intent.getSerializableExtra("deviceChild");
            String noNet = intent.getStringExtra("noNet");
            if (!TextUtils.isEmpty(noNet)) {

            } else {
                if (!TextUtils.isEmpty(macAddress) && deviceChild!=null && macAddress.equals(deviceChild.getMacAddress())) {
                    if (deviceChild2 == null) {
                        Toast.makeText(UseWaterRecordActivity.this, "该设备已重置", Toast.LENGTH_SHORT).show();
                        Intent data = new Intent(UseWaterRecordActivity.this, MainActivity.class);
                        data.putExtra("houseId", houseId);
                        startActivity(data);
                    } else {
                        deviceChild = deviceChild2;
                        int wPurifierOutQuqlity=deviceChild.getWPurifierOutQuqlity();
                    }
                }
            }
        }
    }
}
