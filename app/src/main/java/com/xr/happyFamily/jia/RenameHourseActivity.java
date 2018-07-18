package com.xr.happyFamily.jia;

import android.content.Context;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.CityAdapter;
import com.xr.happyFamily.bao.bean.City;
import com.xr.happyFamily.bao.bean.District;
import com.xr.happyFamily.bao.bean.Province;
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
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.xr.happyFamily.jia.ChooseHourseActivity.MREQUEST_CODE;

public class RenameHourseActivity extends AppCompatActivity implements View.OnClickListener {
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

        setContentView(R.layout.activity_hourse_rename);
        unbinder = ButterKnife.bind(this);
        mContext=RenameHourseActivity.this;
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
                 showPopup();
                break;
            case R.id.iv_rename_back:
                Intent intent = new Intent(RenameHourseActivity.this, ChooseHourseActivity.class);
                startActivity(intent);

                break;
        }
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
                        houseAddress=String.valueOf(tv_sheng.getText()) ;
                        new ChangeAddressAsync().execute();
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
        mPopWindow.setOnDismissListener(new RenameHourseActivity.poponDismissListener());
        mPopWindow.showAsDropDown(findViewById(R.id.iv_rename_back));
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
                Toast.makeText(RenameHourseActivity.this, "请选择省份", Toast.LENGTH_SHORT).show();
            }
        } else if (i == 2) {
            if (cities.size() > 0) {
                districts = cities.get(sign_city).getDistricts();
                for (int a = 0; a < districts.size(); a++) {
                    if (!districts.get(a).getName().equals(cities.get(sign_city).getName()))
                        data.add(districts.get(a).getName());
                }
            } else {
                Toast.makeText(RenameHourseActivity.this, "请选择城市", Toast.LENGTH_SHORT).show();
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
                    textViewa.setText(tv_shi.getText());
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
