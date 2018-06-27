package com.xr.happyFamily.bao;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.TimeLineAdapter;
import com.xr.happyFamily.bao.util.WuLiuData;
import com.xr.happyFamily.bean.WuLiuBean;
import com.xr.happyFamily.bean.WuLiuListBean;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.PublicData;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

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

public class WuLiuActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.img_shop_pic)
    ImageView imgShopPic;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.lv_list)
    ListView lvList;
    @BindView(R.id.img_call)
    ImageView imgCall;
    private TimeLineAdapter adapter;

    String logisticCode, shipperCode, describe;
    private MyDialog dialog;
    String wuliu;
    List<WuLiuListBean> wuLiuListBeanList = new ArrayList<>();
    String tel="0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_shop_wuliu);
        ButterKnife.bind(this);
        lvList.setDividerHeight(0);
        adapter = new TimeLineAdapter(this, wuliuList);
        lvList.setAdapter(adapter);
        titleText.setText("查看物流");
        titleRightText.setVisibility(View.GONE);

        WuLiuData wuLiuData = new WuLiuData();
        wuLiuListBeanList.addAll(wuLiuData.getWuLiuListBeanList());


        Bundle bundle = getIntent().getExtras();
        logisticCode = bundle.get("logisticCode").toString();
        shipperCode = bundle.get("shipperCode").toString();
        for (int i = 0; i < wuLiuListBeanList.size(); i++) {
            if (shipperCode.equals(wuLiuListBeanList.get(i).getCode()))
                tvTel.setText(wuLiuListBeanList.get(i).getName() + ": " + logisticCode);
        }
        dialog = MyDialog.showDialog(this);
        dialog.show();
        Map<String, Object> params = new HashMap<>();
        params.put("shipperCode", shipperCode);
        params.put("logisticCode", logisticCode);
        new expressInfoAsync().execute(params);

    }

    List<Map<String, Object>> wuliuList = new ArrayList<Map<String, Object>>();


    @OnClick({R.id.back,R.id.img_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_call:
                if(tel.equals("0"))
                    Toast.makeText(WuLiuActivity.this,"暂无配送员联系方式",Toast.LENGTH_SHORT).show();
                else
                showPopup();
                break;
        }
    }


    class expressInfoAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "/logistics/expressInfo";
            String result = HttpUtils.headerPostOkHpptRequest(WuLiuActivity.this, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    describe = returnData.getString("describe");
                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("traces");
                    Gson gson = new Gson();
                    //通过反射 得到UserBean.class
                    for (int i = list.size(); i > 0; i--) {
                        JsonElement user = list.get(i - 1);
                        WuLiuBean userList = gson.fromJson(user, WuLiuBean.class);
                        Map<String, Object> map = new HashMap<String, Object>();
                        String str = userList.getAcceptStation();
                        if (str.indexOf("电话") != -1) {
                            tel = str.substring(str.indexOf("电话") + 3);
                            Log.e("qqqqqqqqqqqDDDD", tel);
                        }

                        map.put("title", userList.getAcceptStation());
                        map.put("time", userList.getAcceptTime());
                        if (i == list.size()) {
                            map.put("state", "0");
                        } else if (i == 1) {
                            map.put("state", "2");
                        } else {
                            map.put("state", "1");
                        }
                        wuliuList.add(map);
                    }

//                    wuliu = userList.getAcceptStation();
//                    wuliu_time = userList.getAcceptTime();

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
                tvState.setText(describe);
                adapter.notifyDataSetChanged();
//                tvWuliu.setText(wuliu);
//                tvWuliuTime.setText(wuliu_time);

            }
        }
    }

    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;
    private TextView tv_quxiao, tv_queding, tv_context;
    private void showPopup() {
        mContext=WuLiuActivity.this;
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_main, null);
        tv_quxiao = (TextView) contentViewSign.findViewById(R.id.tv_quxiao);
        tv_queding = (TextView) contentViewSign.findViewById(R.id.tv_queren);
        tv_context = (TextView) contentViewSign.findViewById(R.id.tv_context);
        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WuLiuActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                            100);
                    return;
                } else {
                    PublicData publicDate = new PublicData();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
                    startActivity(intent);
                }
            }
        });

        tv_context.setText("是否拨打快递员电话？");
        mPopWindow = new PopupWindow(contentViewSign);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        mPopWindow.setFocusable(true);
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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

}
