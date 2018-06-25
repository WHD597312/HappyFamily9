package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bean.WuLiuBean;
import com.xr.happyFamily.bean.WuLiuListBean;

import java.util.List;


//最简单的list点击item
public class WuLiuListAdapter extends BaseAdapter implements OnClickListener {
    private List<WuLiuListBean> mList;
    private Context mContext;
    private InnerItemOnclickListener mListener;

    public WuLiuListAdapter(List<WuLiuListBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        // TODO 自动生成的方法存根
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO 自动生成的方法存根
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO 自动生成的方法存根
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city,
                    null);

            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_city);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(mList.get(position).getName());
        return convertView;
    }

    public final static class ViewHolder {
        TextView tv;
    }

    interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
        this.mListener=listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }
}