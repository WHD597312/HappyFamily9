package com.xr.happyFamily.jia.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.xr.happyFamily.main.FamilyFragment;
import com.xr.happyFamily.main.FamilyFragmentManager;
import com.xr.happyFamily.main.RoomFragment;

import java.util.List;

public class FamilyAdapter extends FragmentStatePagerAdapter{
    private  FragmentManager fm;
    private List<Fragment> list;


    public FamilyAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.fm=fm;
        this.list = list;
        notifyDataSetChanged();

    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
