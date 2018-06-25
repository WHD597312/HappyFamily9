package com.xr.happyFamily.jia.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.DeviceChild;
import com.xr.happyFamily.jia.pojo.Equipment;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends ArrayAdapter {
    private Context mContext;
    private int layoutResourceId;
    private List<DeviceChild> mGridData;


    int img[]={R.mipmap.t};
    public GridViewAdapter(Context context, int resource, List<DeviceChild> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.layoutResourceId = resource;
        this.mGridData = objects;
    }

    public void setGridData(ArrayList<DeviceChild> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView = (TextView) convertView.findViewById(R.id.tv_home_1);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_home);
            DeviceChild item = mGridData.get(position);
            item.setImg(img[0]);
            holder.textView.setText(item.getName());
            Picasso.with(mContext).load(item.getImg()).into(holder.imageView);
            return convertView;
        }

    private class ViewHolder {

        TextView textView;
        ImageView imageView;

    }



}