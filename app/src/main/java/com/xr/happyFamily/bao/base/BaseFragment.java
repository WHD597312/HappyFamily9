package com.xr.happyFamily.bao.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by TYQ on 2017/10/14.
 */

public class BaseFragment extends Fragment {
    protected Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    //有Flag的跳转
    public void openActivityWithFlag(Class targetActivity, Bundle bundle, int flag) {
        Intent intent = new Intent(context, targetActivity);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    //传参跳转
    public void openActivity(Class targetActivity, Bundle bundle) {
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    //不传参跳转
    public void openActivity(Class targetActivity) {
        openActivity(targetActivity, null);
    }

    //传参跳转并结束自身
    public void openActivityAndFinish(Class targetActivity, Bundle bundle) {
        openActivity(targetActivity, bundle);
        getActivity().finish();
    }

    //不传参跳转并结束自身
    public void openActivityAndFinish(Class targetActivity) {
        openActivity(targetActivity, null);
        getActivity().finish();
    }
}
