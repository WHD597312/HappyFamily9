package com.xr.happyFamily.jia.activity;

import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.together.NumberToString;
import com.xr.happyFamily.together.chart.BarChartManager;

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
    private MyApplication application;
    String time;
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


        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");

        time=format.format(date);
        tv_date.setText(time+"(本日用水量)");
        barChartManager = new BarChartManager(barChart);
        colours.add(Color.parseColor("#20e2ff"));
        colours.add(Color.BLUE);
        colours.add(Color.RED);
        colours.add(Color.CYAN);

        for (int i = 3; i <= 24; i=i+3) {
            xValuesDay.add(i+"");
            yValueDay.add((float) (Math.random() * 80));
        }
        for (int i=0;i<=6;i++){
            if (i==0){
                xValuesWeek.add("日");
            }else {
                String week=NumberToString.toChinese(i+"");
                xValuesWeek.add(week);
            }
            yValuesWeek.add((float) (Math.random() * 80));
        }
        for (int i = 1; i <=12 ; i++) {
            xValuesMonth.add(i+"");
            yValuesMonth.add((float) (Math.random() * 80));
        }
        day=1;
        setValueDay();
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
                tv_date.setText(time+"(本月用水量)");
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
        barChartManager.showBarChart(xValuesDay, yValueDay, "", colours.get(0));
        barChartManager.setDescription("时间");
    }

    /**
     * 设置week用水量
     */
    private void setValueWeek(){
        barChartManager.showBarChart(xValuesWeek, yValueDay, "", colours.get(0));
        barChartManager.setDescription("星期");
    }

    /**
     * 设置month用水量
     */
    private void setValueMonth(){
        barChartManager.showBarChart(xValuesMonth, yValuesMonth, "", colours.get(0));
        barChartManager.setDescription("月");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
