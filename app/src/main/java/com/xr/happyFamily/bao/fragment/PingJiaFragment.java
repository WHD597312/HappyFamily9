package com.xr.happyFamily.bao.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bao.adapter.EvaluateAdapter;
import com.xr.happyFamily.bao.adapter.PinglunAdapter;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.bao.bean.Receive;
import com.xr.happyFamily.bao.view.FlowTagView;
import com.xr.happyFamily.bean.ShopPinglunBean;
import com.xr.happyFamily.login.login.LoginActivity;
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
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by TYQ on 2017/9/7.
 */

public class PingJiaFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    private static ShopXQActivity father;
    Context mContext;
    @BindView(R.id.tv_pinglun)
    TextView tvPinglun;
    @BindView(R.id.tv_haoping)
    TextView tvHaoping;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.ft_pinglun)
    FlowTagView ftPinglun;
    @BindView(R.id.img_more)
    ImageView imgMore;
    String goodsId;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.img4)
    ImageView img4;
    @BindView(R.id.img5)
    ImageView img5;


    private boolean isMore = false;
    private EvaluateAdapter adapter_pinglun;
    private ArrayList<Map<String, Object>> datas;
    private PinglunAdapter pinglunAdapter;
    List<String> list = new ArrayList();
    ImageView[] imgs;
    String[] tag={"全部","美观","性价比高","包装好","做工精细","使用舒服"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_shop_pingjia, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(father);
        recyclerview.setLayoutManager(linearLayoutManager);

//      获取数据，向适配器传数据，绑定适配器
        imgs = new ImageView[]{img1, img2, img3, img4, img5};
        pinglunAdapter = new PinglunAdapter(mContext, shopPinglunBeanList);
        recyclerview.setAdapter(pinglunAdapter);
        //      调用按钮返回事件回调的方法
        adapter_pinglun = new EvaluateAdapter(mContext, R.layout.item_pingjia);
//        ftPinglun.setOne();
        ftPinglun.setAdapter(adapter_pinglun);
        ftPinglun.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                adapter_pinglun.setSelection(position);
                adapter_pinglun.notifyDataSetChanged();
                getPingLun(tag[position]);

            }
        });

        ftPinglun.setOne();
        list.add("全部（0）");
        list.add("美观（0）");
        list.add("性价比高（0）");
        list.add("包装好（0）");
        list.add("做工精细（0）");
        list.add("使用舒服（0）");

        adapter_pinglun.setItems(list);

        getData();
        return view;
    }

    public static boolean running = false;
    @Override
    public void onStart() {
        super.onStart();
        running=true;
    }

    @Override
    public void onStop() {
        super.onStop();
        running=false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.tv_delete:
//                mShopCartAdapter.showDialog(mEmtryView, mPosition);
//                mPopWindow.dismiss();
//                break;
            default:
                break;
        }
    }

    Bundle bundle;

    private void getData() {

        bundle = this.getArguments();

        // 步骤2:获取某一值
        goodsId = bundle.getString("goodsId");

        Log.e("qqqqqGGGGG",goodsId+"?");
        String userId = bundle.getString("userId");
        Map<String, Object> params = new HashMap<>();
        params.put("goodsId", goodsId);
        new getCountAsync().execute(params);
        getPingLun("全部");

//
//        Map<String, Object> params2 = new HashMap<>();
//        params2.put("userId", userId);
//        new ShopFragment.getAddressAsync().execute(params2);

    }

    private void getPingLun(String tag) {
        shopPinglunBeanList.clear();
        Map<String, Object> params = new HashMap<>();
        params.put("goodsId", goodsId);
        if (!"全部".equals(tag))
            params.put("tag", tag);
        new getRateAsync().execute(params);
    }




    @OnClick(R.id.img_more)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_more:
                if (!isMore) {
                    ftPinglun.setMore();
                    adapter_pinglun.notifyDataSetChanged();
                    isMore = true;
                    imgMore.setImageResource(R.mipmap.ic_pingjia_more);
                } else {
                    ftPinglun.setOne();
                    adapter_pinglun.notifyDataSetChanged();
                    isMore = false;

                    imgMore.setImageResource(R.mipmap.ic_pingjia_more_xia);
                }
                break;
        }
    }

    String returnData = "";
    Receive receive;

    String  average,beautiful="0", total="0", cost="0", fine="0", comfortable="0", satisfaction="0", packing="0";

    class getCountAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "order/getCountRate";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            Log.i("result","-->"+result);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code=result;
                    }else {
                        code = "100";
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject returnData = jsonObject.getJSONObject("returnData");
                        average = returnData.getString("average");
                        Log.i("average2", "-->" + average);
                        total = returnData.getString("total");
                        beautiful = returnData.getString("beautiful");
                        cost = returnData.getString("cost");
                        fine = returnData.getString("fine");
                        comfortable = returnData.getString("comfortable");
                        //满意度
                        satisfaction = returnData.getString("satisfaction");
                        packing = returnData.getString("packing");

//        list.add("质量好（100）");
                        list.clear();
                        list.add("全部（" + total + "）");
                        list.add("美观（" + beautiful + "）");
                        list.add("性价比高（" + cost + "）");
                        list.add("包装好（" + packing + "）");
                        list.add("做工精细（" + fine + "）");
                        list.add("使用舒服（" + comfortable + "）");
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
                Log.i("average","-->"+average);
                if (!"null".equals(returnData)) {
                    if(!Utils.isEmpty(average+"")) {
                        int ave = (int)(Float.parseFloat(average)*10 + 5)/10;
                        for (int i = 0; i < ave; i++) {
                            imgs[i].setImageResource(R.mipmap.ic_pl_xx_true);
                        }
                    }
                    adapter_pinglun.notifyDataSetChanged();
                    if (tvHaoping!=null) {
                        if (total.equals("0"))

                            tvHaoping.setText("满意度:100%");

                        else
                            tvHaoping.setText("满意度:" + Integer.parseInt(satisfaction) * 100 / Integer.parseInt(total) + "%");
                    }
//                    tvAddress.setText(receive.getReceiveProvince() + " " + receive.getReceiveCity() + " " + receive.getReceiveCounty() + " " + receive.getReceiveAddress());
                }else if (!Utils.isEmpty(s) && "401".equals(s)) {
                    Toast.makeText(getActivity(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences;
                    preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
                    MyDialog.setStart(false);
                    if (preferences.contains("password")) {
                        preferences.edit().remove("password").commit();
                    }
                    startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
                }
            }
        }
    }


    List<ShopPinglunBean> shopPinglunBeanList = new ArrayList<>();

    class getRateAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "order/getRateByGoodsIdAndTag";
            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
            String code = "";

            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code=result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnData = jsonObject.getString("returnData");
                    if (!Utils.isEmpty(returnData)) {
                        JsonObject content = new JsonParser().parse(jsonObject.toString()).getAsJsonObject();
                        JsonArray list = content.getAsJsonArray("returnData");
                        Gson gson = new Gson();
                        if(list.size()>0) {
                            for (JsonElement user : list) {
                                //通过反射 得到UserBean.class
                                ShopPinglunBean shopPinglunBean = gson.fromJson(user, ShopPinglunBean.class);

                                shopPinglunBeanList.add(shopPinglunBean);
                            }

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
//                Log.e("qqqqqqqqqqqRece",receive.getContact()+"!");
                pinglunAdapter.notifyDataSetChanged();
//                if(shopPinglunBeanList.size()==0){
//                    Toast.makeText(context,"此商品暂无评论",Toast.LENGTH_SHORT).show();
//                }
//                    tvAddress.setText(receive.getReceiveProvince() + " " + receive.getReceiveCity() + " " + receive.getReceiveCounty() + " " + receive.getReceiveAddress());
            }else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getActivity(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
            }
        }

    }
}
