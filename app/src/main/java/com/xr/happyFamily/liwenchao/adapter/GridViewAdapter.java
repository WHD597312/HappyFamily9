package com.xr.happyFamily.liwenchao.adapter;

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
import com.xr.happyFamily.liwenchao.pojo.Equipment;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Equipment> mGridData = new ArrayList<Equipment>();


    public GridViewAdapter(Context context, int resource, ArrayList<Equipment> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.layoutResourceId = resource;
        this.mGridData = objects;
    }

    public void setGridData(ArrayList<Equipment> mGridData) {
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
            Equipment item = mGridData.get(position);
            holder.textView.setText(item.getName());
           // holder.textView.setText(item.getName());
//            Picasso.with(mContext).load(item.getImgeId()).into(holder.imageView);

            return convertView;
        }

    private class ViewHolder {

        TextView textView;
        ImageView imageView;

    }



}