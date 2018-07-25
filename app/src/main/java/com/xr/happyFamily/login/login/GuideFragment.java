package com.xr.happyFamily.login.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.login.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GuideFragment extends Fragment {
    View view;
    Unbinder unbinder;

    Bundle bundle;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guild, container, false);
        unbinder = ButterKnife.bind(this, view);
        bundle = this.getArguments();

        // 步骤2:获取某一值
        if(type==0)
            view.findViewById(R.id.layout).setBackgroundResource(R.mipmap.g1);
        else if(type==1)
            view.findViewById(R.id.layout).setBackgroundResource(R.mipmap.g2);
        else if(type==2) {
            view.findViewById(R.id.layout).setBackgroundResource(R.mipmap.g3);
            view.findViewById(R.id.tv_guild).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tv_guild).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });
        }

        return view;
    }
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
