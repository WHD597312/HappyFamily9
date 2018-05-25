package com.xr.happyFamily.bao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xr.happyFamily.bao.base.BaseFragment;

import java.util.List;


public class MyViewPageAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragments;

    public MyViewPageAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
