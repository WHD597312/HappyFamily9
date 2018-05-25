package com.xr.happyFamily.bao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopDingdanActivity;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bao.adapter.PinglunAdapter;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_shop_pingjia, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(father);
        recyclerview.setLayoutManager(linearLayoutManager);
//      获取数据，向适配器传数据，绑定适配器
        ArrayList<String> datas = initData();
        final PinglunAdapter pinglunAdapter = new PinglunAdapter(mContext, datas);
        recyclerview.setAdapter(pinglunAdapter);
        //      调用按钮返回事件回调的方法
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

    protected ArrayList<String> initData() {
        ArrayList<String> mDatas = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            mDatas.add("姓名" + i);
        }
        return mDatas;
    }

}
