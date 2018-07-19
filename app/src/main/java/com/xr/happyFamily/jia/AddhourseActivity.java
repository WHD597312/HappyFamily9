package com.xr.happyFamily.jia;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.EditAddressActivity;
import com.xr.happyFamily.bao.adapter.CityAdapter;
import com.xr.happyFamily.bao.bean.City;
import com.xr.happyFamily.bao.bean.District;
import com.xr.happyFamily.bao.bean.Province;
import com.xr.happyFamily.jia.activity.AddDeviceActivity;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.pojo.JsonBean;
import com.xr.happyFamily.jia.titleview.TitleView;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.JsonFileReader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddhourseActivity extends AppCompatActivity implements View.OnClickListener {
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
    private MyApplication  application;
    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;
    private Boolean isMoren = true;


    List<Province> list = null;
    Province province = null;

    List<City> cities = null;
    City city = null;

    List<District> districts = null;
    District district = null;

    int sign_sheng = 0, sign_city = 0, isDefault = 1, receiveId = 0;
    String receiveProvince, receiveCity, receiveCounty, receiveAddress;

    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);

        setContentView(R.layout.activity_home_addhourse);
        unbinder = ButterKnife.bind(this);
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        mContext=AddhourseActivity.this;
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

    }
    @OnClick({R.id.tv_house_position,R.id.btn_ensure})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_house_position:

                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        showPopup();
                    }
                }, 300);


                break;
            case R.id.btn_ensure:
                houseName=et_house_name.getText().toString();
                houseLocation= String.valueOf( tv_shi.getText());
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }





    private ImageView img_close;
    private View view_dis;
    private ListView listCity;
    private List<String> data = new ArrayList<>();
    private CityAdapter cityAdapter;
    private TextView tv_sheng, tv_shi, tv_qu;
    private RelativeLayout rl_sheng, rl_shi, rl_qu;
    private ImageView img_sheng, img_shi, img_qu;
    private ImageView[] img_city;
    private int sing_city = 0;


    private void showPopup() {
        parser();
        Log.e("qqqqqqqqqQQQ","????");
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shop_city, null);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        listCity = (ListView) contentViewSign.findViewById(R.id.list_city);
        tv_sheng = (TextView) contentViewSign.findViewById(R.id.tv_sheng);
        tv_shi = (TextView) contentViewSign.findViewById(R.id.tv_shi);
        tv_qu = (TextView) contentViewSign.findViewById(R.id.tv_qu);
        rl_sheng = (RelativeLayout) contentViewSign.findViewById(R.id.rl_sheng);
        rl_shi = (RelativeLayout) contentViewSign.findViewById(R.id.rl_shi);
        rl_qu = (RelativeLayout) contentViewSign.findViewById(R.id.rl_qu);
        img_sheng = (ImageView) contentViewSign.findViewById(R.id.img_sheng);
        img_shi = (ImageView) contentViewSign.findViewById(R.id.img_shi);
        img_qu = (ImageView) contentViewSign.findViewById(R.id.img_qu);
        img_city = new ImageView[]{img_sheng, img_shi, img_qu};
//        img_close.setOnClickListener(this);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        rl_sheng.setOnClickListener(this);
        rl_shi.setOnClickListener(this);
        rl_qu.setOnClickListener(this);

        cityAdapter = new CityAdapter(data, this);
        listCity.setAdapter(cityAdapter);
        listCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (sing_city) {
                    case 0:
                        receiveProvince = data.get(position);
                        tv_sheng.setText(receiveProvince);
                        sign_sheng = position;
                        upData(1);

                        break;
                    case 1:
                        receiveCity = data.get(position);
                        tv_shi.setText(receiveCity);
                        sign_city = position;
                        upData(2);
                        break;
                    case 2:
                        receiveCounty = data.get(position);
                        tv_qu.setText(receiveCounty);
                        tv_house_position.setText(tv_shi.getText());

                        mPopWindow.dismiss();

                        break;


                }

            }

        });
        upData(0);
        mPopWindow = new PopupWindow(contentViewSign);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        mPopWindow.setFocusable(true);
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setClippingEnabled(false);
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new AddhourseActivity.poponDismissListener());
        mPopWindow.showAsDropDown(findViewById(R.id.title_addhourse));
    }
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }
    private void upData(int i) {

        img_city[sing_city].setVisibility(View.INVISIBLE);
        sing_city = i;
        img_city[sing_city].setVisibility(View.VISIBLE);
        data.clear();
        if (i == 0) {
            for (int a = 0; a < list.size(); a++) {
                data.add(list.get(a).getName());
            }
        } else if (i == 1) {
//            listCity
            if (list.size() > 0) {
                cities = list.get(sign_sheng).getCitys();
                for (int a = 0; a < cities.size(); a++) {
                    data.add(cities.get(a).getName());
                }
            } else {
                Toast.makeText(AddhourseActivity.this, "请选择省份", Toast.LENGTH_SHORT).show();
            }
        } else if (i == 2) {
            if (cities.size() > 0) {
                districts = cities.get(sign_city).getDistricts();
                for (int a = 0; a < districts.size(); a++) {
                    if (!districts.get(a).getName().equals(cities.get(sign_city).getName()))
                        data.add(districts.get(a).getName());
                }
            } else {
                Toast.makeText(AddhourseActivity.this, "请选择城市", Toast.LENGTH_SHORT).show();
            }
        }

        cityAdapter.notifyDataSetChanged();
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 3) {
                showPopup();
            }
        }
    };


    public List<Province> parser() {
        // 创建解析器，并制定解析的xml文件
        XmlResourceParser parser = getResources().getXml(R.xml.cities);
        try {
            int type = parser.getEventType();
            while (type != 1) {
                String tag = parser.getName();//获得标签名
                switch (type) {
                    case XmlResourceParser.START_DOCUMENT:
                        list = new ArrayList<Province>();
                        break;
                    case XmlResourceParser.START_TAG:
                        if ("p".equals(tag)) {
                            province = new Province();
                            cities = new ArrayList<City>();
                            int n = parser.getAttributeCount();
                            for (int i = 0; i < n; i++) {
                                //获得属性的名和值
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if ("p_id".equals(name)) {
                                    province.setId(value);
                                }
                            }
                        }
                        if ("pn".equals(tag)) {//省名字
                            province.setName(parser.nextText());
                        }
                        if ("c".equals(tag)) {//城市
                            city = new City();
                            districts = new ArrayList<District>();
                            int n = parser.getAttributeCount();
                            for (int i = 0; i < n; i++) {
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if ("c_id".equals(name)) {
                                    city.setId(value);
                                }
                            }
                        }
                        if ("cn".equals(tag)) {
                            city.setName(parser.nextText());
                        }
                        if ("d".equals(tag)) {
                            district = new District();
                            int n = parser.getAttributeCount();
                            for (int i = 0; i < n; i++) {
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if ("d_id".equals(name)) {
                                    district.setId(value);
                                }
                            }
                            district.setName(parser.nextText());
                            districts.add(district);
                        }
                        break;
                    case XmlResourceParser.END_TAG:
                        if ("c".equals(tag)) {
                            city.setDistricts(districts);
                            cities.add(city);
                        }
                        if ("p".equals(tag)) {
                            province.setCitys(cities);
                            list.add(province);
                        }
                        break;
                    default:
                        break;
                }
                type = parser.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } */ catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
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
