package com.xr.happyFamily.jia.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyPaperAdapter extends PagerAdapter {
    private List<View> views;
    public MyPaperAdapter(List<View> views) {
        this.views = views;

    }

    @Override
    public int getCount() {// 返回view布局的数目，这些布局都存储在自定义的list集合中
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {//
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(views.get(position));
        return views.get(position);
    }

}
