package com.xr.happyFamily.le.BtClock;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.jia.adapter.FamilyAdapter;
import com.xr.happyFamily.jia.adapter.TabFragmentPagerAdapter;
import com.xr.happyFamily.main.FamilyFragmentManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LeFragmentManager extends BaseFragment {
    List<Fragment> TimeList;

    View view;

    ViewPager viewPager;
    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_le_manager, container, false);
            viewPager = (ViewPager) view.findViewById(R.id.le_viewPager);
//            unbinder = ButterKnife.bind(this,view);
            TimeList = new ArrayList<>();
            TimeRemFragment timeRemFragment = new TimeRemFragment();
            TimeList.add(timeRemFragment);
            DeathCountFragment deathCountFragment = new DeathCountFragment();
            TimeList.add(deathCountFragment);
            FragmentStatePagerAdapter adapter = new FamilyAdapter(getFragmentManager(), TimeList);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (callValueValue != null) {
                        callValueValue.setPosition(position);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callValueValue = (CallValueValue) getActivity();
    }


    CallValueValue callValueValue;
    public interface CallValueValue {
        public void setPosition(int position);
    }
}
