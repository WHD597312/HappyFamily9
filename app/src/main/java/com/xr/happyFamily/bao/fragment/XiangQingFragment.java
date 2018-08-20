package com.xr.happyFamily.bao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bao.adapter.ShopXqAdapter;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.bao.view.MyHeadRefreshView;
import com.xr.happyFamily.bao.view.MyLoadMoreView;
import com.xr.happyFamily.together.MyDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by TYQ on 2017/9/7.
 */

public class XiangQingFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    private static ShopXQActivity father;
    Context mContext;

    ShopXqAdapter shopXqAdapter;
    List<String> imgList;

    String goodsId;
    Bundle bundle;

//    @BindView(R.id.ll_xiangqing)
//    LinearLayout llXiangqing;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_content)
    PullToRefreshLayout swipeContent;
    private MyDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_shop_xiangqing, container, false);
        unbinder = ButterKnife.bind(this, view);
        bundle = this.getArguments();
        goodsId = bundle.getString("goodsId");
        Map<String, Object> params = new HashMap<>();
        params.put("goodsId", goodsId);
//        dialog = MyDialog.showDialog(mContext);
//        dialog.show();
//        new getShopAsync().execute(params);
        imgList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(father);
        recyclerView.setLayoutManager(linearLayoutManager);
        String[] str = imgData.split(",");
        imgList = Arrays.asList(str);
        shopXqAdapter = new ShopXqAdapter(getActivity(), imgList);
        recyclerView.setAdapter(shopXqAdapter);
        swipeContent.setHeaderView(new MyHeadRefreshView(getActivity()));
        swipeContent.setFooterView(new MyLoadMoreView(getActivity()));
        swipeContent.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                if (swipeContent!=null) {
                    swipeContent.finishRefresh();
                    final ShopXQActivity shopXQActivity = (ShopXQActivity) getActivity();
                    shopXQActivity.gotoShop();
                }
            }

            @Override
            public void loadMore() {
                if (swipeContent!=null) {
                    swipeContent.finishLoadMore();
                    Toast.makeText(getActivity(), "已滑动到底部了", Toast.LENGTH_SHORT).show();
                }
            }
        });

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


    String imgData="";

    public void setData(String s) {
        imgData = s;
    }

    String detailDescribe;

//    class getShopAsync extends AsyncTask<Map<String, Object>, Void, String> {
//        @Override
//        protected String doInBackground(Map<String, Object>... maps) {
//            Map<String, Object> params = maps[0];
//            String url = "goods/getGoodsById";
//            String result = HttpUtils.headerPostOkHpptRequest(mContext, url, params);
//            String code = "";
//            try {
//                if (!Utils.isEmpty(result)) {
//                    if (result.length() < 6) {
//                        code = result;
//                    }
//                    JSONObject jsonObject = new JSONObject(result);
//                    code = jsonObject.getString("returnCode");
//                    JSONObject returnData = jsonObject.getJSONObject("returnData");
//                    JsonObject content = new JsonParser().parse(returnData.toString()).getAsJsonObject();
//                    detailDescribe = returnData.getString("detailDescribe");
//
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
//                MyDialog.closeDialog(dialog);
//                String[] str = detailDescribe.split(",");
//                imgList = Arrays.asList(str);
//                shopXqAdapter = new ShopXqAdapter(getActivity(), imgList);
//                recyclerView.setAdapter(shopXqAdapter);
//                addGroupImage();
//
//            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
//
//                Toast.makeText(getActivity().getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
//                SharedPreferences preferences;
//                preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
//                MyDialog.setStart(false);
//                if (preferences.contains("password")) {
//                    preferences.edit().remove("password").commit();
//                }
//                startActivity(new Intent(mContext.getApplicationContext(), LoginActivity.class));
//            }
//        }
//    }


    ImageView imgs[];
//    private void addGroupImage() {
//        imgs=new ImageView[imgList.size()];
//        for (int i = 0; i < imgList.size(); i++) {
//            ImageView imageView = new ImageView(getActivity());
//            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));  //设置图片宽高
//            imgs[i]=imageView;
//            llXiangqing.addView(imageView); //动态添加图片
//        }
//
//        for (int i = 0; i < imgList.size(); i++) {
//            Picasso.with(father).load(imgList.get(i))
//                    .placeholder(R.mipmap.app)
//                    .error(R.mipmap.bg_develop).into(imgs[i]);
//        }


//        Picasso.with(father).load("http://p9zaf8j1m.bkt.clouddn.com/detailDescribe/%E5%8F%96%E6%9A%96%E5%99%A8_02.png")
//                .placeholder(R.mipmap.app)
//                .error(R.mipmap.bg_develop).into(imgs[1]);
//
//        Picasso.with(father).load("http://p9zaf8j1m.bkt.clouddn.com/detailDescribe/%E5%8F%96%E6%9A%96%E5%99%A8_03.png")
//                .placeholder(R.mipmap.app)
//                .error(R.mipmap.bg_develop).into(imgs[2]);
//
//        Picasso.with(father).load("http://p9zaf8j1m.bkt.clouddn.com/detailDescribe/灭蚊器_02.png")
//                .placeholder(R.mipmap.app)
//                .error(R.mipmap.bg_develop).into(imgs[3]);
//
//        Picasso.with(father).load("http://p9zaf8j1m.bkt.clouddn.com/detailDescribe/灭蚊器_01.png")
//                .placeholder(R.mipmap.app)
//                .error(R.mipmap.bg_develop).into(imgs[4]);
//    }
}
