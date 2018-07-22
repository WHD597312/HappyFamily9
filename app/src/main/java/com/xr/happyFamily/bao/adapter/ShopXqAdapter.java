package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.bean.WuLiuListBean;
import com.xr.happyFamily.le.bean.HappyBannerBean;

import java.util.List;

//快递列表适配器

//最简单的list点击item
public class ShopXqAdapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;
    private InnerItemOnclickListener mListener;

    public ShopXqAdapter(List<String> mList, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_xq,
                    null);

            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_footBanner);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(mContext).load(mList.get(position)).into(viewHolder.img);

        return convertView;
    }

    public final static class ViewHolder {
        ImageView img;
    }

    interface InnerItemOnclickListener {
        void itemClick(View v);
    }


}
