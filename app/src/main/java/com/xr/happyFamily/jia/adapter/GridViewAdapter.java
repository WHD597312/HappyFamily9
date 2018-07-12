package com.xr.happyFamily.jia.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.DeviceChild;

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
            holder.tv_device_name = (TextView) convertView.findViewById(R.id.tv_device_name);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_home);
            holder.tv_device_switch= (TextView) convertView.findViewById(R.id.tv_device_switch);
            holder.tv_device_type= (TextView) convertView.findViewById(R.id.tv_device_type);
            DeviceChild item = mGridData.get(position);
            if (item!=null){
                int type=item.getType();
                if (type==2){
                    holder.tv_device_type.setText("取暖器");
                    boolean online=item.getOnline();
                    int deviceState=item.getDeviceState();

                    if (online){
                        if (deviceState==1){
                            holder.tv_device_switch.setText("电源开");
                        }else if (deviceState==0){
                            holder.tv_device_switch.setText("电源关");
                        }

                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.green2));
                    }else if (online==false){
                        holder.tv_device_switch.setText("离线");
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.color_gray3));
                    }
                }else if (type==3){
                    item.setImg(img[0]);
                    holder.tv_device_name.setText(item.getName());
                    Picasso.with(mContext).load(item.getImg()).into(holder.imageView);
                    boolean online=item.getOnline();
                    Log.i("online","-->"+online);
                    int sensorState=item.getSensorState();
                    Log.i("online","-->"+sensorState);
                    if (online){
                        if (sensorState==128){
                            holder.tv_device_switch.setText("USB供电");
                        }else if (sensorState==1){
                            holder.tv_device_switch.setText("电压正常");
                        }else if (sensorState==2){
                            holder.tv_device_switch.setText("电压低");

                            
                        }
                        holder.tv_device_switch.setTextColor(Color.parseColor("#57Cf76"));
                    }else {
                        holder.tv_device_switch.setText("离线");
                        holder.tv_device_switch.setTextColor(Color.parseColor("#999999"));
                    }
                }

            }

            return convertView;
        }
    private class ViewHolder {
        TextView tv_device_switch;/**设备开关*/
        TextView tv_device_type;/**设备类型*/
        TextView tv_device_name;/**设备名称*/
        ImageView imageView;
    }
}