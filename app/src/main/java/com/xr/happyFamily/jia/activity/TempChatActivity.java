package com.xr.happyFamily.jia.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.together.chart.LineChartManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TempChatActivity extends AppCompatActivity {

    Unbinder unbinder;
    MyApplication application;
    @BindView(R.id.line_chart) LineChart line_chart;
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

        LineChartManager lineChartManager1 = new LineChartManager(line_chart);


        //设置x轴的数据
        ArrayList<Integer> xValues = new ArrayList<>();
        for (int i = 0; i <24; i++) {
            xValues.add(i);
        }

        //设置y轴的数据()
        List<List<Integer>> yValues = new ArrayList<>();
        List<Integer> doubles=new ArrayList<>();
        doubles.add(4);
        doubles.add(6);
        doubles.add(8);
        doubles.add(10);
        doubles.add(12);
        doubles.add(14);

        doubles.add(4);
        doubles.add(6);
        doubles.add(8);
        doubles.add(10);
        doubles.add(12);
        doubles.add(14);

        doubles.add(4);
        doubles.add(6);
        doubles.add(8);
        doubles.add(10);
        doubles.add(12);
        doubles.add(14);

        doubles.add(4);
        doubles.add(6);
        doubles.add(8);
        doubles.add(10);
        doubles.add(12);
        doubles.add(14);

        doubles.add(4);
        doubles.add(6);
        doubles.add(8);
        doubles.add(10);
        doubles.add(12);
        doubles.add(14);

        yValues.add(doubles);

        //颜色集合
        List<Integer> colours = new ArrayList<>();
        colours.add(Color.WHITE);
        colours.add(Color.WHITE);
        colours.add(Color.WHITE);
        colours.add(Color.WHITE);

        //线的名字集合
        List<String> names = new ArrayList<>();
        names.add("温度曲线");
        names.add("折线二");
        names.add("折线三");
        names.add("折线四");

        //创建多条折线的图表
        lineChartManager1.showLineChart(xValues, yValues.get(0), names.get(0), colours.get(3));
        lineChartManager1.setDescription("温度");
        lineChartManager1.setYAxis(60, 0, 24);
        lineChartManager1.setHightLimitLine(60,"高温报警",0);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
