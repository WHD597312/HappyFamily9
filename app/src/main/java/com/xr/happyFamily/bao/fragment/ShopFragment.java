package com.xr.happyFamily.bao.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopAddressActivity;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bao.base.BaseFragment;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_shop_shop, container, false);
        unbinder = ButterKnife.bind(this, view);

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


    @OnClick({R.id.img_address, R.id.tv2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_address:
                startActivityForResult(new Intent(getActivity(), ShopAddressActivity.class),101);
                break;
            case R.id.tv2:
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
}
