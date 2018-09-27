package com.xr.happyFamily.jia.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bean.AConfDataBean;
import com.xr.happyFamily.bean.ShopPageBean;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by win7 on 2018/5/22.
 */

public class AirStateActivity extends AppCompatActivity {

    Unbinder unbinder;
    int ratedPower;
    int curdPower;
    long deviceId;
    int dataType;
    @BindView(R.id.lineChart)
    LineChart lineChart;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv_power)
    TextView tvPower;
    @BindView(R.id.tv_ya)
    TextView tvYa;
    @BindView(R.id.tv_liu)
    TextView tvLiu;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv_guige)
    TextView tvGuige;
    @BindView(R.id.ll)
    LinearLayout ll;

    int[] datas=new int[13];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_air_state);
        unbinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        ratedPower = intent.getIntExtra("ratedPower", 0);
        curdPower = intent.getIntExtra("curdPower", 0);
        deviceId = intent.getIntExtra("deviceId", 0);
        dataType = intent.getIntExtra("dataType", 0);
        tvGuige.setText("额外功率："+ratedPower+"W");
        tvPower.setText("功率："+curdPower+"W");

        initChart(lineChart);
    }


    private void findView() {


    }


    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线

    /**
     * 初始化图表
     */
    private void initChart(LineChart lineChart) {
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //是否可以拖动
        lineChart.setDragEnabled(false);
        //是否有触摸事件
        lineChart.setTouchEnabled(false);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);
//是否显示边界
        lineChart.setDrawBorders(false);
        lineChart.setBackgroundColor(Color.parseColor("#323948"));
        /***XY轴的设置***/
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYaxis = lineChart.getAxisRight();
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);


        xAxis.setAxisLineColor(Color.parseColor("#ffffff"));
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(Color.parseColor("#616879"));
        xAxis.setTextColor(Color.WHITE);
        xAxis.setLabelCount(12);
        rightYaxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(true);
        leftYAxis.setGridColor(Color.parseColor("#616879"));
        leftYAxis.setLabelCount(8);
        leftYAxis.setAxisLineColor(Color.parseColor("#ffffff"));
        leftYAxis.setTextColor(Color.WHITE);

        leftYAxis.enableGridDashedLine(10f, 0f, 0f);
        xAxis.enableGridDashedLine(10f, 0f, 0f);
        rightYaxis.setEnabled(false);
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        rightYaxis.setAxisMinimum(0f);

        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
        legend.setEnabled(false);

        Description description = new Description();
//        description.setText("需要展示的内容");
        description.setEnabled(false);
        lineChart.setDescription(description);

        new getDeviceDataAsync().execute();
    }


    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        lineDataSet.setDrawValues(false);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }


    /**
     * 展示曲线
     *
     * @param dataList 数据集合
     * @param name     曲线名称
     * @param color    曲线颜色
     */
    public void showLineChart(final List<IncomeBean> dataList, String name, int color) {
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String tradeDate = dataList.get((int) value % dataList.size()).getTradeDate();
                return tradeDate;
            }
        });
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            IncomeBean data = dataList.get(i);
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            Entry entry = new Entry(i, (float) data.getValue());
            entries.add(entry);
        }
//         每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(entries, name);
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.CUBIC_BEZIER);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
    }

    /**
     * 我的收益
     */
    public class IncomeBean {
        /**
         * tradeDate : 20180502
         * value : 0.03676598
         */
        private String tradeDate;
        private double value;

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public String getTradeDate() {
            return tradeDate;
        }

        public void setTradeDate(String tradeDate) {
            this.tradeDate = tradeDate;
        }
    }

    /**
     * 设置线条填充背景颜色
     *
     * @param drawable
     */
    public void setChartFillDrawable(Drawable drawable) {
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            lineChart.invalidate();
        }
    }


    AConfDataBean aConfDataBean;
    class getDeviceDataAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            String url = "/family/data/getDeviceData?deviceId="+deviceId+"&dataType="+dataType;
            String result = HttpUtils.doGet(AirStateActivity.this, url);

            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    Log.e("qqqqqRRSSS",url);
                    Log.e("qqqqqRRSSS",result);
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");

                    JsonObject content = new JsonParser().parse(jsonObject.getString("returnData")).getAsJsonObject();
                    Gson gson = new Gson();
                     aConfDataBean = gson.fromJson(content, AConfDataBean.class);


                }
            } catch (Exception e) {
                e.printStackTrace();

            }
//
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s)) {

                int[] datas=new int[12];
                datas[0]=aConfDataBean.getZero();
                datas[1]=aConfDataBean.getTwo();
                datas[2]=aConfDataBean.getFour();
                datas[3]=aConfDataBean.getSix();
                datas[4]=aConfDataBean.getEight();
                datas[5]=aConfDataBean.getTen();
                datas[6]=aConfDataBean.getTwelve();
                datas[7]=aConfDataBean.getFourteen();
                datas[8]=aConfDataBean.getSixteen();
                datas[9]=aConfDataBean.getEighteen();
                datas[10]=aConfDataBean.getTwenty();
                datas[11]=aConfDataBean.getTwentyTwo();

                datas[0]=22;
                datas[1]=19;
                datas[2]=30;
                datas[3]=24;
                datas[4]=15;
                datas[5]=22;
                datas[6]=17;
                datas[7]=25;
                datas[8]=22;
                datas[9]=30;
                datas[10]=21;
                datas[11]=22;
                List<IncomeBean> list = new ArrayList<>();

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                for (int i = 0; i < 12; i++) {
                    IncomeBean incomeBean = new IncomeBean();
                    incomeBean.setTradeDate(i * 2 + "");

                    if(i*2<hour||i*2==hour)
                    incomeBean.setValue(datas[i]);
                    list.add(incomeBean);
                }


                showLineChart(list, "我的收益", Color.WHITE);
                Drawable drawable = getResources().getDrawable(R.drawable.fade_blue);
                setChartFillDrawable(drawable);
            }
        }
    }



}

