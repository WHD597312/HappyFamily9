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
import android.widget.LinearLayout;
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
    @BindView(R.id.tv_yd_title)
    TextView tv_yd_title;
    @BindView(R.id.tv_yd_1)
    TextView tv_yd_1;
    @BindView(R.id.tv_yd_img)
    ImageView tv_yd_img;
    @BindView(R.id.iv_yd_d1)
    ImageView iv_yd_d1;
    @BindView(R.id.iv_yd_d2)
    ImageView iv_yd_d2;
    @BindView(R.id.iv_yd_d3)
    ImageView iv_yd_d3;
    @BindView(R.id.tv_yd_bj)
    ImageView tv_yd_bj;
    @BindView(R.id.li_dian)
    LinearLayout li_dian;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guild, container, false);
        unbinder = ButterKnife.bind(this, view);
        bundle = this.getArguments();

        // 步骤2:获取某一值
        if(type==0){
            view.findViewById(R.id.layout).setBackgroundResource(R.mipmap.g1);
        }
        else if(type==1) {
            view.findViewById(R.id.layout).setBackgroundResource(R.mipmap.g2);
            tv_yd_title.setText("乐在其中");
            tv_yd_1.setText("精神至上 趣味多多");
            tv_yd_img.setImageResource(R.mipmap.g2_xq);
            iv_yd_d1.setImageResource(R.mipmap.g_hd);
            iv_yd_d2.setImageResource(R.mipmap.g_yd);
            iv_yd_d3.setImageResource(R.mipmap.g_hd);
            tv_yd_bj.setImageResource(R.mipmap.g2_xqbj);

        }
        else if(type==2) {
            view.findViewById(R.id.layout).setBackgroundResource(R.mipmap.g3);
            view.findViewById(R.id.tv_guild).setVisibility(View.VISIBLE);
            tv_yd_title.setText("宝贝中心");
            tv_yd_1.setText("家居宝贝 构建自己智能家庭");
            tv_yd_img.setImageResource(R.mipmap.g3_xq);
            tv_yd_bj.setImageResource(R.mipmap.g3_xqbj);
            iv_yd_d1.setVisibility(View.GONE);
            iv_yd_d2.setVisibility(View.GONE);
            iv_yd_d3.setVisibility(View.GONE);
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
