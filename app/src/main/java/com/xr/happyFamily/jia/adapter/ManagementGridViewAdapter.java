package com.xr.happyFamily.jia.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.Equipment;

import java.util.ArrayList;

public class ManagementGridViewAdapter extends ArrayAdapter {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Equipment> mGridData = new ArrayList<Equipment>();
    private int selectedPosition = 0;// 选中的位置

    public ManagementGridViewAdapter(Context context, int resource, ArrayList<Equipment> objects) {
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
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView_management = (TextView) convertView.findViewById(R.id.tv_managment);
        holder.imageView_management = (ImageView) convertView.findViewById(R.id.iv_management);
        holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.li_item_addroom);
        Equipment item = mGridData.get(position);
        holder.textView_management.setText(item.getName());
        Picasso.with(mContext).load(item.getImgeId()).into(holder.imageView_management);
        if (selectedPosition == position) {
            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_grayx));
        } else {
            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        return convertView;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    private class ViewHolder {
        LinearLayout linearLayout;
        TextView textView_management;
        ImageView imageView_management;

    }


}