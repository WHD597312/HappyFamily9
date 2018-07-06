package com.xr.happyFamily.le.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.BaseFragment;

import java.util.List;


public class ClickViewPageAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragments;
    final int PAGE_COUNT=4;
    private Context context;
    public ClickViewPageAdapter(FragmentManager fm, List<BaseFragment> fragments,Context context) {
        super(fm);
        this.fragments = fragments;
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public View getCustomView(int position){
        View view= LayoutInflater.from(context).inflate(R.layout.item_clock,null);
        ImageView iv= (ImageView) view.findViewById(R.id.tab_iv);
        TextView tv= (TextView) view.findViewById(R.id.tab_tv);
        switch (position){
            case 0:
                //drawable代码在文章最后贴出
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.rb_clock_icon1));
                tv.setText("时光简记");
                break;
            case 1:
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.rb_clock_icon3));
                tv.setText("群组模式");
                break;
            case 2:
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.rb_clock_icon4));
                tv.setText("情侣模式");
                break;
            case 3:
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.rb_clock_icon5));
                tv.setText("制赖模式");
                break;
        }
        return view;
    }


}
