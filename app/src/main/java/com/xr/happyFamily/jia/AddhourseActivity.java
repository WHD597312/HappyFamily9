package com.xr.happyFamily.jia;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.activity.AddDeviceActivity;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.JsonBean;
import com.xr.happyFamily.jia.titleview.TitleView;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.JsonFileReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddhourseActivity extends AppCompatActivity {
    Unbinder unbinder;
    @BindView(R.id.title_addhourse) TitleView titleView;
    @BindView(R.id.et_house_name) EditText et_house_name;/**家庭名称*/
    @BindView(R.id.tv_house_position) TextView tv_house_position;/**家庭地址*/
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private String addHouseUrl= HttpUtils.ipAddress+"/family/house/registerHouse";
    private String houseName;
    private String houseLocation;
    
    SharedPreferences preferences;
    private String userId;
    Map<String,Object> params=new HashMap<>();
//    String url = "http://47.98.131.11:8084/login/auth";
    private HourseDaoImpl hourseDao;

    int houseId;

    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);

        setContentView(R.layout.activity_home_addhourse);
        unbinder = ButterKnife.bind(this);
        titleView.setTitleText("新建家庭");
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        userId=preferences.getString("userId","");
        hourseDao=new HourseDaoImpl(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initJsonData();
    }
    @OnClick({R.id.tv_house_position,R.id.btn_ensure})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_house_position:
                showPickerView();
                break;
            case R.id.btn_ensure:
                houseName=et_house_name.getText().toString();
                if (TextUtils.isEmpty(houseName)){
                    Toast.makeText(this,"请输入家庭名称",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (TextUtils.isEmpty(houseLocation)){
                    Toast.makeText(this,"请选择家庭地址",Toast.LENGTH_SHORT).show();
                    break;
                }

                params.put("userId",userId);
                params.put("houseAddress",houseLocation);
                params.put("houseName",houseName);
                new AddHouseAsync().execute(params);
                break;
        }
    }
    private void showPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(AddhourseActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = options2Items.get(options1).get(options2);
                if (!TextUtils.isEmpty(text)){
                    houseLocation=text;
                    tv_house_position.setText(houseLocation);
                }
                Log.i("location","-->"+text);
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(16)
                .setOutSideCancelable(false)
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {   //解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        //  获取json数据
        String JsonData = JsonFileReader.getJson(this, "province_data.json");
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);
                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }
    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    class AddHouseAsync extends AsyncTask<Map<String,Object>,Void,Integer>{

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code=0;
            Map<String,Object> params=maps[0];
            String result=HttpUtils.postOkHpptRequest(addHouseUrl,params);
            if (!TextUtils.isEmpty(result)){
                Log.i("result","-->"+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)){
                        code=Integer.parseInt(returnCode);
                        JSONObject returnData=jsonObject.getJSONObject("returnData");
                        int id=returnData.getInt("id");
                        String houseName=returnData.getString("houseName");
                        String houseAddress=returnData.getString("houseAddress");

                        Hourse hourse=new Hourse();
                        hourse.setHouseId((long)id);
                        hourse.setHouseName(houseName);
                        hourse.setHouseAddress(houseAddress);
                        hourseDao.insert(hourse);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code){
                case 100:
                    Toast.makeText(AddhourseActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                    setResult(ChooseHourseActivity.MREQUEST_CODE);
                    finish();
                    break;
                    default:
                        Toast.makeText(AddhourseActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                        break;
            }
        }
    }
}
