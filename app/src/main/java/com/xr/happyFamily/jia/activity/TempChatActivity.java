package com.xr.happyFamily.jia.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.xr.database.dao.daoimpl.DeviceChildDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.together.chart.LineChartManager;
import com.xr.happyFamily.together.http.HttpUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
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
    private String tempChartLineUrl= HttpUtils.ipAddress+"/family/data/getSocketData";
    private long deviceId;
    private DeviceChildDaoImpl deviceChildDao;
    private DeviceChild deviceChild;
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
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        String s=year+"-"+month+"-"+day;
        tv_time.setText(s);
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
            int type=deviceChild.getType();
            String url=tempChartLineUrl+"?deviceId="+deviceId+"&dataType="+type;
            String result=HttpUtils.getOkHpptRequest(url);
            List<Integer> list=new ArrayList<>();
            if (result!=null){
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    code=Integer.parseInt(returnCode);
                    if (100==code){

                        JSONObject returnData=jsonObject.getJSONObject("returnData");
                        int mondayKWH=returnData.getInt("mondayKWH");
                        int tuesdayKWH=returnData.getInt("tuesdayKWH");
                        int wednesdayKWH=returnData.getInt("wednesdayKWH");
                        int thursdayKWH=returnData.getInt("thursdayKWH");
                        int fridayKWH=returnData.getInt("fridayKWH");
                        int saturdayKWH=returnData.getInt("saturdayKWH");
                        int sundayKWH=returnData.getInt("sundayKWH");
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
                ArrayList<Integer> xValues = new ArrayList<>();
                for (int i = 0; i <7; i++) {
                    xValues.add(i);
                }

                //设置y轴的数据()
                List<List<Integer>> yValues = new ArrayList<>();

                yValues.add(list);

                //颜色集合
                List<Integer> colours = new ArrayList<>();
                colours.add(Color.WHITE);


                //线的名字集合
                List<String> names = new ArrayList<>();
                names.add("温度曲线");


                //创建多条折线的图表
                lineChartManager1.showLineChart(xValues, yValues.get(0), names.get(0), colours.get(0));
                lineChartManager1.setDescription("");
                lineChartManager1.setYAxis(60, 0, 7);
                lineChartManager1.setHightLimitLine(60,"度",0);
            }
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
