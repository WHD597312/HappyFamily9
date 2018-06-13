package com.xr.happyFamily.le.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.le.XQActivity;

import java.util.ArrayList;
import java.util.Map;

public class MyHorizontalScrollViewAdapter extends BaseAdapter {

    private ArrayList<Map<String,Object>> list;
    private Context context;

    public MyHorizontalScrollViewAdapter(Context context,ArrayList<Map<String,Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();

    }

    @Override
    public Map<String,Object> getItem(int position) {
        return list.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_yuanwang, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.img_touxiang);
            holder.tv = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setImageResource((int)list.get(position).get("touxiang"));
        holder.tv.setText(list.get(position).get("name").toString());
        final int p=position;
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, XQActivity.class));
            }
        });
        return convertView;

    }

    class ViewHolder{
        ImageView image;
        TextView tv;
    }

}