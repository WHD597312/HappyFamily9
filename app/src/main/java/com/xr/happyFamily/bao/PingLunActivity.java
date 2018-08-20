package com.xr.happyFamily.bao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.EvaluatePlAdapter;
import com.xr.happyFamily.bao.adapter.PinglunFabiaoAdapter;
import com.xr.happyFamily.bao.view.FlowTagView;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.http.NetWorkUtil;
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

public class PingLunActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;

    @BindView(R.id.img_choose)
    ImageView imgChoose;

    boolean isChoose = true;
    @BindView(R.id.ft_pinglun)
    FlowTagView ftPinglun;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private EvaluatePlAdapter adapter_pinglun;
    List<String> list = new ArrayList();

    ArrayList<String> img=new ArrayList<>();
    ArrayList<Integer> orderId=new ArrayList<>();
    ArrayList<Integer> priceId=new ArrayList<>();
    ArrayList<Integer> goodsId=new ArrayList<>();
    PinglunFabiaoAdapter pinglunFabiaoAdapter;
    int isAnonymous=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        setContentView(R.layout.activity_pinglun_fabiao);
        ButterKnife.bind(this);
        titleRightText.setText("发布");
        titleRightText.setTextColor(Color.parseColor("#4FBA72"));
        titleText.setText("发表评价");

        Bundle bundle=getIntent().getExtras();
        img=bundle.getStringArrayList("img");
        orderId=bundle.getIntegerArrayList("orderId");
        priceId=bundle.getIntegerArrayList("priceId");
        goodsId=bundle.getIntegerArrayList("goodsId");


        pinglunFabiaoAdapter=new PinglunFabiaoAdapter(this,img);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(pinglunFabiaoAdapter);


        list.add("美观");
        list.add("性价比高");
        list.add("包装好");
        list.add("做工精细");
        list.add("使用舒服");
        adapter_pinglun = new EvaluatePlAdapter(this, R.layout.item_pingjia);
//        ftPinglun.setOne();
        ftPinglun.setAdapter(adapter_pinglun);
        ftPinglun.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                adapter_pinglun.setSelection(position);
                adapter_pinglun.notifyDataSetChanged();
                String e = adapter_pinglun.getItem(position).toString();
//                Toast.makeText(mContext, "i am:" + e, Toast.LENGTH_SHORT).show();
//                datas.clear();
//                datas.addAll(initData(e));
//                pinglunAdapter.notifyDataSetChanged();

            }
        });
        ftPinglun.setMore();
        adapter_pinglun.setItems(list);

    }


    @OnClick({R.id.back,  R.id.title_rightText,R.id.img_choose,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title_rightText:
                boolean isConn= NetWorkUtil.isConn(this);
                if (isConn){
                Map<String, Object> map = new HashMap<>();

                List<Object> rates=new ArrayList<>();
                String[] str_ed=pinglunFabiaoAdapter.getStr_ed();
                int[] str_img=pinglunFabiaoAdapter.getStr_img();
                int[] str_sign=adapter_pinglun.getSign();

                String tag="";
                for(int i=0;i<str_sign.length;i++){
                    if(str_sign[i]==1){
                        tag=tag+list.get(i)+",";
                    }

                }
                if(tag.length()>1) {
                    tag = tag.substring(0, tag.length() - 1);
                }
                for(int i=0;i<img.size();i++){
                    Map<String, Object> params = new HashMap<>();
                    if(Utils.isEmpty(str_ed[i])){
                        Toast.makeText(PingLunActivity.this,"请填写第"+(i+1)+"件商品评论",Toast.LENGTH_SHORT).show();
                    }
                    params.put("comment",str_ed[i]);
                    params.put("orderId",orderId.get(i));
                    params.put("priceId",priceId.get(i));
                    params.put("goodsId",goodsId.get(i));
                    params.put("tag",tag);
                    params.put("buyerRate",str_img[i]);
                    params.put("isAnonymous",isAnonymous);
                    rates.add( params);
                }
                map.put("rates",rates);

                new rateGoodsAsync().execute(map);
//                startActivity(new Intent(this, PingLunSuccessActivity.class));
                break;

                }else {
                    Toast.makeText(this,"请检查网络",Toast.LENGTH_SHORT).show();
                }
            case R.id.img_choose:
                if (isChoose) {
                    imgChoose.setImageResource(R.mipmap.weixuanzhong3x);
                    isAnonymous=0;
                    isChoose = false;
                } else {
                    imgChoose.setImageResource(R.mipmap.xuanzhong);
                    isAnonymous=1;
                    isChoose = true;
                }
                break;


        }
    }


    class rateGoodsAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {

            Map<String, Object> params = maps[0];
            String url = "/order/rateGoods";
            String result = HttpUtils.headerPostOkHpptRequest(PingLunActivity.this, url, params);
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
                Toast.makeText(PingLunActivity.this, "发表评论成功", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(PingLunActivity.this,PingLunSuccessActivity.class);
                startActivity(intent);
            finish();
            }else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(PingLunActivity.this.getApplicationContext(), LoginActivity.class));
            }
        }
    }

}
