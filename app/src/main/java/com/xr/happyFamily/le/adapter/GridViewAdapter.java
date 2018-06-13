package com.xr.happyFamily.le.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.happyFamily.R;

import java.util.List;

/**
 * gridView的adapter
 */

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mList;
    private int selectorPosition;
    private int layout;

    public GridViewAdapter(Context context, List<String> mList,int layout) {
        this.mContext = context;
        this.mList = mList;
        this.layout=layout;

    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList != null ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return mList != null ? position : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, layout, null);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_search);
        textView.setText(mList.get(position));
        //如果当前的position等于传过来点击的position,就去改变他的状态
        if (selectorPosition == position) {
            textView.setBackgroundResource(R.drawable.bg_shape_stroke_green25);
            textView.setTextColor(Color.parseColor("#4FBA72"));
        } else {
            //其他的恢复原来的状态
            textView.setBackgroundResource(R.drawable.bg_shape_stroke_gray25);
            textView.setTextColor(Color.parseColor("#787878"));
        }
        return convertView;
    }


    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();

    }
}