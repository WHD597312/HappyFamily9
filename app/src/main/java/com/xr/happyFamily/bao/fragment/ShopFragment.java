package com.xr.happyFamily.bao.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bao.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

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



}
