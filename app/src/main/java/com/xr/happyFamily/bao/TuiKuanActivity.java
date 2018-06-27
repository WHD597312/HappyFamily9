package com.xr.happyFamily.bao;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.xr.happyFamily.bao.adapter.DingDanXQAdapter;
import com.xr.happyFamily.bao.adapter.WuLiuListAdapter;
import com.xr.happyFamily.bao.util.WuLiuData;
import com.xr.happyFamily.bean.OrderBean;
import com.xr.happyFamily.bean.WuLiuListBean;
import com.xr.happyFamily.together.MyDialog;
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

public class TuiKuanActivity extends AppCompatActivity implements View.OnClickListener {


    String orderNumber, money;
    Dialog dialog;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_tui)
    TextView tvTui;
    @BindView(R.id.tv_choose)
    TextView tvChoose;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_wuliu_type)
    TextView tvWuliuType;
    @BindView(R.id.ll_wuliu)
    LinearLayout llWuliu;
    @BindView(R.id.ed_wuliu_id)
    EditText edWuliuId;
    @BindView(R.id.tv_tijiao)
    TextView tvTijiao;

    DingDanXQAdapter confListAdapter;

    boolean isXG=true;
    List<WuLiuListBean> wuLiuListBeanList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_tuikuan_shenqing);
        ButterKnife.bind(this);
        titleRightText.setVisibility(View.GONE);
        WuLiuData wuLiuData=new WuLiuData();
        wuLiuListBeanList.addAll(wuLiuData.getWuLiuListBeanList());


        tvTui.setText("退货原因");
        orderNumber = getIntent().getExtras().getString("orderNumber");
        isXG="XG".equals(getIntent().getExtras().getString("type"));
        if(isXG){
        titleText.setText("修改申请");
            shipperCode=getIntent().getExtras().getString("shipperCode");
            logisticCode=getIntent().getExtras().getString("logisticCode");
            reason=getIntent().getExtras().getString("reason");
            refundTime=getIntent().getExtras().getString("refundTime");
            wuliuCode=shipperCode;
            edWuliuId.setText(logisticCode);
            tvChoose.setText(reason);
        }
        else
            titleText.setText("申请退款");
        confListAdapter = new DingDanXQAdapter(TuiKuanActivity.this, orderDetailsLists);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(confListAdapter);
        dialog = MyDialog.showDialog(TuiKuanActivity.this);
        dialog.show();
        Map<String, Object> map = new HashMap<>();
        new getOrderAsync().execute(map);

        if(isXG){

        }
    }


    @OnClick({R.id.back, R.id.tv_choose, R.id.tv_tijiao, R.id.ll_wuliu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_choose:
                showPopup();
                break;
            case R.id.tv_tijiao:

                Map<String, Object> params = new HashMap<>();
                params.put("orderNumber", orderNumber);
                params.put("orderId", orderId);
                if ("请选择".equals(tvChoose.getText().toString())) {
                    Toast.makeText(TuiKuanActivity.this, "请选择退款原因", Toast.LENGTH_SHORT).show();
                    break;
                } else params.put("reason", tvChoose.getText().toString());
                if (wuliuCode.equals("0")) {
                    Toast.makeText(TuiKuanActivity.this, "请选择物流类型", Toast.LENGTH_SHORT).show();
                    break;
                } else params.put("shipperCode", wuliuCode);
                if (TextUtils.isEmpty(edWuliuId.getText())) {
                    Toast.makeText(TuiKuanActivity.this, "请填写物流单号", Toast.LENGTH_SHORT).show();
                    break;
                } else params.put("logisticCode", edWuliuId.getText().toString());

                dialog = MyDialog.showDialog(TuiKuanActivity.this);
                dialog.show();
                if(isXG){
                    new updateRefundOrderAsync().execute(params);
                }else
                new refundOrderAsync().execute(params);
                break;
            case R.id.ll_wuliu:
                showWuliuPopup();
                break;

        }
    }


    private View contentViewSign;
    private PopupWindow mPopWindow;
    private ImageView img_close;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;
    private View view_dis;

    private void showPopup() {
        contentViewSign = LayoutInflater.from(this).inflate(R.layout.popup_tuihuo, null);
        view_dis = contentViewSign.findViewById(R.id.view_dis);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        tv1 = (TextView) contentViewSign.findViewById(R.id.tv1);
        tv2 = (TextView) contentViewSign.findViewById(R.id.tv2);
        tv3 = (TextView) contentViewSign.findViewById(R.id.tv3);
        tv4 = (TextView) contentViewSign.findViewById(R.id.tv4);
        tv5 = (TextView) contentViewSign.findViewById(R.id.tv5);
        tv6 = (TextView) contentViewSign.findViewById(R.id.tv6);
        tv7 = (TextView) contentViewSign.findViewById(R.id.tv7);
//        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
        view_dis.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        img_close.setOnClickListener(this);

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
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    List<String> list;


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_dis:
                mPopWindow.dismiss();
                break;
            case R.id.tv1:
                tvChoose.setText(tv1.getText());
                mPopWindow.dismiss();
                break;
            case R.id.tv2:
                tvChoose.setText(tv2.getText());
                mPopWindow.dismiss();
                break;
            case R.id.tv3:
                tvChoose.setText(tv3.getText());
                mPopWindow.dismiss();
                break;
            case R.id.tv4:
                tvChoose.setText(tv4.getText());
                mPopWindow.dismiss();
                break;
            case R.id.tv5:
                tvChoose.setText(tv5.getText());
                mPopWindow.dismiss();
                break;
            case R.id.tv6:
                tvChoose.setText(tv6.getText());
                mPopWindow.dismiss();
                break;
            case R.id.tv7:
                tvChoose.setText(tv7.getText());
                mPopWindow.dismiss();
                break;
            case R.id.img_close:
                mPopWindow.dismiss();
                break;
        }
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tvChoose.setText("修改原因");
    }


    ListView listWuliu;
    String wuliuCode = "0";
    private List<String> data = new ArrayList<>();

    private void showWuliuPopup() {
        contentViewSign = LayoutInflater.from(this).inflate(R.layout.popup_tuihuo_wuliu, null);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        view_dis = contentViewSign.findViewById(R.id.view_dis);
        listWuliu = (ListView) contentViewSign.findViewById(R.id.list_wuliu);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        view_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });


        WuLiuListAdapter wuLiuListAdapter = new WuLiuListAdapter(wuLiuListBeanList, this);
        listWuliu.setAdapter(wuLiuListAdapter);
        listWuliu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopWindow.dismiss();
                tvWuliuType.setText(wuLiuListBeanList.get(position).getName());
                wuliuCode = wuLiuListBeanList.get(position).getCode();
//                switch (sing_city) {
//                    case 0:
//                        receiveProvince = data.get(position);
//                        tv_sheng.setText(receiveProvince);
//                        sign_sheng = position;
//                        upData(1);
//
//                        break;
//                    case 1:
//                        receiveCity = data.get(position);
//                        tv_shi.setText(receiveCity);
//                        sign_city = position;
//                        upData(2);
//                        break;
//                    case 2:
//                        receiveCounty = data.get(position);
//                        tv_qu.setText(receiveCounty);
//                        mPopWindow.dismiss();
//                        tvAddress.setText(tv_sheng.getText() + " " + tv_shi.getText() + " " + tv_qu.getText());
//                        break;


//                }

            }

        });
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
        mPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

//    String wuliu;

//
//    class getWuLiuAsync extends AsyncTask<Map<String, Object>, Void, String> {
//        @Override
//        protected String doInBackground(Map<String, Object>... maps) {
//
//            String url = "/logistics/listShipperCode";
//            String result = HttpUtils.doGet(TuiKuanActivity.this, url);
//
//            String code = "";
//            try {
//                if (!Utils.isEmpty(result)) {
//
//                    JSONObject jsonObject = new JSONObject(result);
//                    code = jsonObject.getString("returnCode");
//                    JsonObject content = new JsonParser().parse(jsonObject.toString()).getAsJsonObject();
//                    JsonArray list = content.getAsJsonArray("returnData");
//                    Gson gson = new Gson();
//                    for (int i = 0; i < list.size(); i++) {
//                        //通过反射 得到UserBean.class
//                        JsonElement use = list.get(i);
//                        WuLiuListBean userList = gson.fromJson(use, WuLiuListBean.class);
//                        if(shipperCode.equals(userList.getCode())){
//                            wuliu=userList.getName();
//                        }
//                        wuLiuListBeanList.add(userList);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return code;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            if (!Utils.isEmpty(s) && "100".equals(s)) {
//                if(isXG) {
//                    tvWuliuType.setText(wuliu);
//
//                }
//
//            }
//        }
//    }


    class refundOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "/order/refundOrder";
            String result = HttpUtils.headerPostOkHpptRequest(TuiKuanActivity.this, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
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
                Toast.makeText(TuiKuanActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TuiKuanActivity.this, TuiKuanXQActivity.class);
                intent.putExtra("orderId", orderNumber);
                startActivity(intent);
                finish();
            }
        }
    }


    class updateRefundOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "/order/updateRefundOrder";
            String result = HttpUtils.headerPostOkHpptRequest(TuiKuanActivity.this, url, params);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
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
                Intent intent = new Intent(TuiKuanActivity.this, TuiKuanXQActivity.class);
                intent.putExtra("orderId", orderNumber);
                startActivity(intent);
            }
        }
    }


    String orderId,logisticCode="0",shipperCode="0",reason="0",refundTime="0";
    List<OrderBean.OrderDetailsList> orderDetailsLists = new ArrayList<>();

    class getOrderAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "/order/getOrderByOrderNumber";
            url = url + "?orderNumber=" + orderNumber;
            String result = HttpUtils.doGet(TuiKuanActivity.this, url);
            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    money = returnData.get("paidAmount").toString();
                    orderId = returnData.get("orderId").toString();
                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("orderDetailsList");
                    Gson gson = new Gson();
                    for (JsonElement user : list) {
                        //通过反射 得到UserBean.class
                        OrderBean.OrderDetailsList userList = gson.fromJson(user, OrderBean.OrderDetailsList.class);
                        orderDetailsLists.add(userList);
                    }
                    JSONObject receive = returnData.getJSONObject("receive");



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
                confListAdapter.notifyDataSetChanged();
                tvMoney.setText(money);
                MyDialog.closeDialog(dialog);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){

            if(isXG){
                Intent intent=new Intent(TuiKuanActivity.this,TuiKuanXQActivity.class);
                intent.putExtra("orderId",orderNumber);
                startActivity(intent);
                finish();
            }else
            TuiKuanActivity.this.finish();
        }
        return true;
    }
}
