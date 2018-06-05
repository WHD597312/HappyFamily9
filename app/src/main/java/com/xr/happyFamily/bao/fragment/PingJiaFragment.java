package com.xr.happyFamily.bao.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bao.adapter.EvaluateAdapter;
import com.xr.happyFamily.bao.adapter.PinglunAdapter;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.bao.view.FlowTagView;

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

    private boolean isMore=false;
    private EvaluateAdapter adapter_pinglun;
    private  ArrayList<Map<String,Object>> datas;
private  PinglunAdapter pinglunAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_shop_pingjia, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(father);
        recyclerview.setLayoutManager(linearLayoutManager);
//      获取数据，向适配器传数据，绑定适配器
        datas = initData("姓名");
       pinglunAdapter = new PinglunAdapter(mContext, datas);
        recyclerview.setAdapter(pinglunAdapter);
        //      调用按钮返回事件回调的方法
        adapter_pinglun = new EvaluateAdapter(mContext,R.layout.item_pingjia);
//        ftPinglun.setOne();
        ftPinglun.setAdapter(adapter_pinglun);
        ftPinglun.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                adapter_pinglun.setSelection(position);
                adapter_pinglun.notifyDataSetChanged();
                String e = adapter_pinglun.getItem(position).toString();
                Toast.makeText(mContext, "i am:" + e, Toast.LENGTH_SHORT).show();
                datas.clear();
                datas.addAll(initData(e));
                pinglunAdapter.notifyDataSetChanged();

            }
        });

        ftPinglun.setOne();
        List<String> list = new ArrayList();
        list.add("全部（2000）");
        list.add("有图（2000）");
        list.add("美观（2000）");
        list.add("性价比高（200）");
//        list.add("质量好（100）");
        list.add("包装好（20）");
        list.add("做工精细（500）");
        list.add("使用舒服（100）");
        adapter_pinglun.setItems(list);

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

//            case R.id.tv_delete:
//                mShopCartAdapter.showDialog(mEmtryView, mPosition);
//                mPopWindow.dismiss();
//                break;
            default:
                break;
        }
    }

    protected ArrayList<Map<String,Object>> initData(String name) {
        ArrayList<Map<String,Object>> mDatas = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < 3; i++) {
            Map<String,Object> map1 = new HashMap<String,Object>();

            map1.put("name",name + i);
            map1.put("pinglun","评论" + i);
            map1.put("tel",i+":"+123456789);
            map1.put("time","2018-5-6-11:30");
            map1.put("pic",R.mipmap.chanpin2);
            mDatas.add(map1);
        }
        return mDatas;
    }


    @OnClick(R.id.img_more)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_more:
                if(!isMore){
                    ftPinglun.setMore();
                    adapter_pinglun.notifyDataSetChanged();
                    isMore=true;
                    imgMore.setImageResource(R.mipmap.ic_pingjia_more);
                }else {
                    ftPinglun.setOne();
                    adapter_pinglun.notifyDataSetChanged();
                    isMore=false;

                    imgMore.setImageResource(R.mipmap.ic_pingjia_more_xia);
                }
                break;
        }
    }


}
