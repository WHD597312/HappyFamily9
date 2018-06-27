package com.xr.happyFamily.bao.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.donkingliang.labels.LabelsView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopAddressActivity;
import com.xr.happyFamily.bao.ShopConfActivity;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bao.adapter.EvaluateAdapter;
import com.xr.happyFamily.bao.adapter.EvaluateXhAdapter;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.bao.bean.GoodsPrice;
import com.xr.happyFamily.bao.bean.Receive;
import com.xr.happyFamily.bao.view.FlowTagView;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by TYQ on 2017/9/7.
 */

public class ShopFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    private static ShopXQActivity father;
    Context mContext;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.img_address)
    ImageView imgAddress;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_xinghao)
    TextView tvXinghao;
    @BindView(R.id.img_pic)
    ImageView imgPic;
    String goodsId;
    int priceId=0;


    List<GoodsPrice> list_price;

    int num=1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_shop_shop, container, false);
        unbinder = ButterKnife.bind(this, view);
        getData();


//        tvXinghao.setText(power);
//        tvPrice.setText(price);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_close:
            case R.id.view_dis:
                mPopWindow.dismiss();
                break;

            default:
                break;
        }
    }


    @OnClick({R.id.img_address, R.id.tv_xinghao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_address:
                startActivityForResult(new Intent(getActivity(), ShopAddressActivity.class), 101);
                break;
            case R.id.tv_xinghao:
                showPopup();
                break;
        }
    }

    /**
     * 所有的Activity对象的返回值都是由这个方法来接收 requestCode:
     * 表示的是启动一个Activity时传过去的requestCode值
     * resultCode：表示的是启动后的Activity回传值时的resultCode值
     * data：表示的是启动后的Activity回传过来的Intent对象
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 111) {
            String address = data.getStringExtra("address");// 拿到返回过来的地址
            // 把得到的数据显示到输入框内
            tvAddress.setText(address);

        }

    }


    private View contentViewSign, view_dis;
    private PopupWindow mPopWindow;
    private ImageView img_close;
    private EvaluateAdapter adapter_xinghao;
    private FlowTagView labelsView;
    private TextView tv_price, tv_name,tv_jia,tv_jian,tv_num,tv_cart,tv_buy;
    private EvaluateXhAdapter adapter_xh;
    int sign=-1;
    private void showPopup() {


//        priceId=list_price.get(0).getPriceId();
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shop_xinghao, null);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        view_dis = contentViewSign.findViewById(R.id.view_dis);
        labelsView = (FlowTagView) contentViewSign.findViewById(R.id.labels);
        adapter_xh = new EvaluateXhAdapter(context, R.layout.item_xinghao);
        list = new ArrayList<>();

        for (int i = 0; i < list_price.size(); i++) {
            list.add(list_price.get(i).getPower() + "");
        }
        adapter_xh.setItems(list);
        if(sign!=-1){
            adapter_xh.setSelection(sign);
        }
        labelsView.setAdapter(adapter_xh);
        labelsView.setOne();
        labelsView.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                sign=position;
                adapter_xh.setSelection(position);
                adapter_xh.notifyDataSetChanged();
                String e = adapter_xh.getItem(position).toString();
                tv_price.setText("¥"+list_price.get(position).getPrice());
                tvPrice.setText("¥" + list_price.get(position).getPrice());
                tvXinghao.setText(list.get(position));
                price=list_price.get(position).getPrice()+"";
                priceId=list_price.get(position).getPriceId();
                goodsId=list_price.get(position).getGoodsId()+"";
//                Toast.makeText(mContext, "i am:" + e, Toast.LENGTH_SHORT).show();
//                datas.clear();
//                datas.addAll(initData(e));
//                pinglunAdapter.notifyDataSetChanged();

            }
        });

        tv_price = (TextView) contentViewSign.findViewById(R.id.tv_price);
        tv_name = (TextView) contentViewSign.findViewById(R.id.tv_name);
        tv_jian = (TextView) contentViewSign.findViewById(R.id.tv_shop_reduce);
        tv_num = (TextView) contentViewSign.findViewById(R.id.tv_shop_num);
        tv_num.setText(num+"");
        tv_jia = (TextView) contentViewSign.findViewById(R.id.tv_shop_add);
        tv_cart = (TextView) contentViewSign.findViewById(R.id.tv_type_cart);
        tv_buy = (TextView) contentViewSign.findViewById(R.id.tv_type_bug);
        tv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sign!=-1) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("priceId", priceId);
                    params.put("quantity", num);
                    new addShopAsync().execute(params);
                }else {
                    Toast.makeText(mContext,"请选择商品规格",Toast.LENGTH_SHORT).show();
                }

            }
        });
        tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sign != -1) {
                    Intent intent = new Intent(mContext, ShopConfActivity.class);
                    intent.putExtra("type", "XQ");
                    intent.putExtra("goodsId", goodsId);
                    intent.putExtra("num", num + "");
                    intent.putExtra("priceId", priceId + "");
                    intent.putExtra("money", Integer.parseInt(price) * num );
                    intent.putExtra("weight", weight*num + "");
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "请选择商品规格", Toast.LENGTH_SHORT).show();
                }
            }
        });
        try {
            tv_name.setText(jsonObject.getString("goodsName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(sign!=-1){
            tv_price.setText("¥" + list_price.get(sign).getPrice());
        }else {
            tv_price.setText("");
        }
//        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
//        tv_shopcart.setOnClickListener(this);
        img_close.setOnClickListener(this);
        view_dis.setOnClickListener(this);
        tv_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num>1){
                    num--;
                    tv_num.setText(num+"");
                }
            }
        });
        tv_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                tv_num.setText(num+"");
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
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//        initView();
//        initData();

    }

    List<String> list;


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }

    int pos = 0;




    JSONObject jsonObject;
    String name,type,img,price="0";
    Double weight;

    class getShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            Map<String, Object> params = maps[0];
            String url = "goods/getGoodsById";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
                    jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
                    JsonArray list = content.getAsJsonArray("goodsPrice");

                    if ("100".equals(code)) {
                        Gson gson = new Gson();
                        for (JsonElement user : list) {
                            //通过反射 得到UserBean.class
                            GoodsPrice userList = gson.fromJson(user, GoodsPrice.class);
                            list_price.add(userList);
                            img = returnData.getString("image");
                            name = returnData.getString("goodsName");
                            type = returnData.getString("simpleDescribe");
                            weight = returnData.getDouble("weight");
                        }

                    }
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

                    tvName.setText(name);
                    tvWeight.setText(weight+"kg");
                    tvType.setText(type);

                    Picasso.with(mContext)
                            .load(img)
                            .into(imgPic);//此种策略并不会压缩图片
                }
            }
        }


    String returnData = "";
    Receive receive;

    class getAddressAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "receive/getDefaultReceive";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {

                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnData = jsonObject.getString("returnData");
                    if (!Utils.isEmpty(returnData)) {

                        Gson gson = new Gson();
                        receive = gson.fromJson(jsonObject.getString("returnData"), Receive.class);
                    }
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
                if (!"null".equals(returnData)) {
                    tvAddress.setText(receive.getReceiveProvince() + " " + receive.getReceiveCity() + " " + receive.getReceiveCounty() + " " + receive.getReceiveAddress());
                }
            }
        }
    }


    class addShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "shoppingcart/addOneToShoppingCart";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {

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
                Toast.makeText(mContext, "已加入购物车", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
            }
        }
    }

    Bundle bundle;

    private void getData() {
        list_price = new ArrayList<>();
        bundle = this.getArguments();

        // 步骤2:获取某一值
        goodsId = bundle.getString("goodsId");
        String userId = bundle.getString("userId");
        Map<String, Object> params = new HashMap<>();

        params.put("goodsId", goodsId);
        new getShopAsync().execute(params);

        Map<String, Object> params2 = new HashMap<>();
        params2.put("userId", userId);
        new getAddressAsync().execute(params2);

    }

    public void sendMessage(ICallBack callBack){

        callBack.get_message_from_Fragment(priceId+"",num);
        callBack.getPrice(price+"",priceId,num,goodsId,sign);

    }
    public interface ICallBack {
        void get_message_from_Fragment(String string,int num);
        void getPrice(String price,int priceId,int num,String goodId,int sign);
    }


    public void setData(String price,String power,int s){
        tvPrice.setText("¥"+price);
        tvXinghao.setText(power);

        sign=s;
    }



}
