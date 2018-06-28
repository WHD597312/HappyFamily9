package com.xr.happyFamily.jia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.Fragment.RoomFragment;
import com.xr.happyFamily.jia.adapter.ChooseHouseAdapter;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.JsonBean;
import com.xr.happyFamily.jia.titleview.TitleView;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.JsonFileReader;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.xr.happyFamily.jia.ChooseHourseActivity.MREQUEST_CODE;

public class RenameHourseActivity extends AppCompatActivity {
    Unbinder unbinder;
    String ip = "http://47.98.131.11:8084";
    String houseName;
    long houseId;
    String houseAddress;
    private HourseDaoImpl hourseDao;
    Hourse hourse;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();


    @BindView(R.id.rl_rename_it1)
    RelativeLayout relativeLayout1;
    @BindView(R.id.rl_rename_it2)
    RelativeLayout relativeLayout2;
    @BindView(R.id.tv_rename_choosep)
    TextView textViewn;
    @BindView(R.id.tv_rename_choosep2)
    TextView textViewa;
    @BindView(R.id.iv_rename_back)
    ImageView imageViewb;


    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);

        setContentView(R.layout.activity_hourse_rename);
        unbinder = ButterKnife.bind(this);

        hourseDao= new HourseDaoImpl(getApplicationContext());

        Intent intent = getIntent();
          String houseName=  intent.getStringExtra("houseName");
        String houseAddress =  intent.getStringExtra("houseAddress");
        houseId= intent.getLongExtra("houseId",0);
        hourse = hourseDao.findById(houseId);
        if (!Utils.isEmpty(houseName) &&!Utils.isEmpty(houseAddress)){
            textViewn.setText(houseName);
            textViewa.setText(houseAddress);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        initJsonData();
    }

    @Override
    protected void onRestart() {

        super.onRestart();
    }

    @OnClick({R.id.rl_rename_it1,R.id.rl_rename_it2,R.id.iv_rename_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_rename_it1:
                buildUpdateHomeDialog();
                break;
            case R.id.rl_rename_it2:
                showPickerView();
                break;
            case R.id.iv_rename_back:
                Intent intent = new Intent(RenameHourseActivity.this, ChooseHourseActivity.class);
                startActivity(intent);

                break;
        }
    }
    private void showPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(RenameHourseActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = options2Items.get(options1).get(options2);
                if (!TextUtils.isEmpty(text)){

                    houseAddress=text;
                    new RenameHourseActivity.ChangeAddressAsync().execute();
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
    private void buildUpdateHomeDialog() {
        final HomeDialog dialog = new HomeDialog(this);
        dialog.setOnNegativeClickListener(new HomeDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new HomeDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                houseName = dialog.getName();
                if (Utils.isEmpty(houseName)) {
                    Utils.showToast(RenameHourseActivity.this, "住所名称不能为空");
                } else {

                            new RenameHourseActivity.ChangeHouseNameAsyncTask().execute();

                            dialog.dismiss();


                }
            }
        });
        dialog.show();
    }
    class ChangeHouseNameAsyncTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int code = 0;
            String url = ip+"/family/house/changeName?houseId="+houseId +"&houseName="+ houseName;
            String result = HttpUtils.getOkHpptRequest(url);
            Log.i("result2","-->"+result);
            try {
                if (!TextUtils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    String returnCode=jsonObject.getString("returnCode");
                    if ("100".equals(returnCode)){
                        code=100;
                        if (hourse!=null){
                            hourse.setHouseName(houseName);

                            hourseDao.update(hourse);

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer code) {
            super.onPostExecute(code);
            switch (code){
                case 100:
                    Toast.makeText(RenameHourseActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    textViewn.setText(houseName);
//                    startActivity(new Intent(RenameHourseActivity.this,ChooseHourseActivity.class));
                    break;
                default:
                    Toast.makeText(RenameHourseActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    class ChangeAddressAsync extends AsyncTask<Map<String,Object>,Void,Integer>{

        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code=0;
            String url = ip+"/family/house/changeAddress?houseId="+houseId +"&houseAddress="+houseAddress;
            String result = HttpUtils.getOkHpptRequest(url);
            if (!TextUtils.isEmpty(result)){
                Log.i("result","-->"+result);
                try {
                    if (!TextUtils.isEmpty(result)) {
                        JSONObject jsonObject = new JSONObject(result);
                        String returnCode=jsonObject.getString("returnCode");
                        if ("100".equals(returnCode)){
                            code=100;
                            if (hourse!=null){
                                hourse.setHouseAddress(houseAddress);
                                hourseDao.update(hourse);
                                Log.e("hourse", "doInBackground:---> "+hourse.getHouseId()+"..."+hourse.getHouseAddress() );
                            }
                        }
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
                    Toast.makeText(RenameHourseActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    textViewa.setText(houseAddress);
//                    startActivity(new Intent(RenameHourseActivity.this,ChooseHourseActivity.class));
                    break;
                default:
                    Toast.makeText(RenameHourseActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
