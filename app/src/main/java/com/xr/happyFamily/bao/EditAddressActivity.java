package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.CityAdapter;
import com.xr.happyFamily.bao.base.ToastUtil;
import com.xr.happyFamily.bao.bean.City;
import com.xr.happyFamily.bao.bean.District;
import com.xr.happyFamily.bao.bean.Province;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Mobile;
import com.xr.happyFamily.together.util.Utils;

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

/**
 * Created by win7 on 2018/5/22.
 */

public class EditAddressActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;

    @BindView(R.id.tv_choose)
    TextView tvChoose;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_tel)
    EditText edTel;
    @BindView(R.id.ed_address)
    EditText edAddress;
    @BindView(R.id.ll_moren)
    LinearLayout llMoren;
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

    MyDialog dialog;
    boolean isPop=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        mContext = EditAddressActivity.this;
        setContentView(R.layout.activity_shop_add_address);
        ButterKnife.bind(this);

        titleText.setText("编辑地址");
        titleRightText.setText("保存");

        Bundle extras = getIntent().getExtras();
        edName.setText(extras.getString("name"));
        edTel.setText(extras.getString("tel"));
        tvAddress.setText(extras.getString("address"));
        receiveId = extras.getInt("receiveId");
        isDefault = extras.getInt("isDefault");
        receiveProvince = extras.getString("receiveProvince");
        receiveCity = extras.getString("receiveCity");
        receiveCounty = extras.getString("receiveCounty");
        receiveAddress = extras.getString("receiveAddress");
        edAddress.setText(receiveAddress);

        llMoren.setVisibility(View.GONE);
        findViewById(R.id.lg_bottom).setVisibility(View.GONE);
    }

    @OnClick({R.id.back, R.id.tv_choose, R.id.title_rightText})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.tv_choose:

                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if(!isPop) {
                            isPop=true;
                            showPopup();
                        }
                    }
                }, 300);

                break;
            case R.id.title_rightText:
                if (view != null) {

                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                SharedPreferences userSettings = getSharedPreferences("my", 0);
                String url = userSettings.getString("userId", "1000");
                Map<String, Object> params = new HashMap<>();
                params.put("userId", url);
                params.put("receiveId", receiveId);
                if (Utils.isEmpty(edName.getText().toString())) {
                    ToastUtil.showShortToast("请输入收货人");
                    break;
                } else
                    params.put("contact", edName.getText().toString());
                if (Utils.isEmpty(edTel.getText().toString())) {
                    ToastUtil.showShortToast("请输入联系电话");
                    break;
                } else if (Mobile.isMobile(edTel.getText().toString()))
                    params.put("tel", edTel.getText().toString());
                else
                    ToastUtil.showShortToast( "请输入正确联系电话");
                if (Utils.isEmpty(tvAddress.getText().toString())) {
                    ToastUtil.showShortToast("请输入所在地址");
                    break;
                } else {
                    params.put("receiveProvince", receiveProvince);
                    params.put("receiveCity", receiveCity);
                    params.put("receiveCounty", receiveCounty);
                }
                if (Utils.isEmpty(edAddress.getText().toString())) {
                    ToastUtil.showShortToast("请输入详细地址");
                    break;
                } else
                    params.put("receiveAddress", edAddress.getText().toString());
                params.put("isDefault", isDefault + "");
                dialog = MyDialog.showDialog(mContext);
                dialog.show();
                new AddReceiveAsync().execute(params);

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_dis:
            case R.id.img_close:
                isPop=false;
                mPopWindow.dismiss();
                break;
            case R.id.rl_sheng:
                upData(0);
                break;
            case R.id.rl_shi:
                upData(1);
                break;
            case R.id.rl_qu:
                upData(2);
                break;

        }
    }

    private RecyclerView recyclerview;
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
        isPop=true;
        parser();
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shop_city, null);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        view_dis = contentViewSign.findViewById(R.id.view_dis);
        listCity = (ListView) contentViewSign.findViewById(R.id.list_city);
        tv_sheng = (TextView) contentViewSign.findViewById(R.id.tv_sheng);
        tv_shi = (TextView) contentViewSign.findViewById(R.id.tv_shi);
        tv_qu = (TextView) contentViewSign.findViewById(R.id.tv_qu);
        rl_sheng = (RelativeLayout) contentViewSign.findViewById(R.id.rl_sheng);
        rl_shi = (RelativeLayout) contentViewSign.findViewById(R.id.rl_shi);
        rl_qu = (RelativeLayout) contentViewSign.findViewById(R.id.rl_qu);
        rl_qu.setVisibility(View.GONE);
        rl_shi.setVisibility(View.GONE);
        img_sheng = (ImageView) contentViewSign.findViewById(R.id.img_sheng);
        img_shi = (ImageView) contentViewSign.findViewById(R.id.img_shi);
        img_qu = (ImageView) contentViewSign.findViewById(R.id.img_qu);
        img_city = new ImageView[]{img_sheng, img_shi, img_qu};
        img_close.setOnClickListener(this);
        view_dis.setOnClickListener(this);
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
                        rl_shi.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        receiveCity = data.get(position);
                        tv_shi.setText(receiveCity);
                        sign_city = position;
                        upData(2);
                        rl_qu.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        receiveCounty = data.get(position);
                        tv_qu.setText(receiveCounty);
                        isPop=false;
                        mPopWindow.dismiss();
                        tvAddress.setText(tv_sheng.getText() + " " + tv_shi.getText() + " " + tv_qu.getText());
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
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAsDropDown(findViewById(R.id.view_line));
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
                ToastUtil.showShortToast( "请选择省份");
            }
        } else if (i == 2) {
            if (cities.size() > 0) {
                districts = cities.get(sign_city).getDistricts();
                for (int a = 0; a < districts.size(); a++) {
                    if (!districts.get(a).getName().equals(cities.get(sign_city).getName()))
                        data.add(districts.get(a).getName());
                }
            } else {
                ToastUtil.showShortToast("请选择城市");
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

    class AddReceiveAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "receive/editReceive";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code=result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                MyDialog.closeDialog(dialog);
                ToastUtil.showShortToast( "编辑地址成功");
                finish();
            }else if (!Utils.isEmpty(s) && "401".equals(s)) {
                ToastUtil.showShortToast("用户信息超时请重新登陆");
                SharedPreferences preferences;
                preferences = getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
            }
        }
    }

}
