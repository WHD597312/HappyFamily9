package com.xr.happyFamily.jia.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
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

    int img[]={R.mipmap.sb_dnq,R.mipmap.sb_znzd,R.mipmap.image_socket};
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
             holder.view= (View) convertView.findViewById(R.id.view_homeitem);
            DeviceChild item = mGridData.get(position);
            if (item!=null){
                int type=item.getType();
                String common=item.getCommon();
                if (type==2){
                    if (TextUtils.isEmpty(common)){
                        holder.tv_device_name.setText(item.getName());
                        holder.tv_device_type.setText("取暖器");
                    }else {
                        String roomName=item.getRoomName();
                        holder.tv_device_name.setText(roomName);
                        holder.tv_device_type.setText(item.getName());
                    }
                    boolean online=item.getOnline();
                    int deviceState=item.getDeviceState();
                    item.setImg(img[0]);

                    if (online){
                        if (deviceState==1){
                            holder.tv_device_switch.setText("电源开");
                            holder.view.setVisibility(View.VISIBLE);
                        }else if (deviceState==0){
                            holder.tv_device_switch.setText("电源关");
                            holder.view.setVisibility(View.GONE);
                        }
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.green2));
                    }else if (online==false){
                        holder.tv_device_switch.setText("离线");
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.color_gray3));
                        holder.view.setVisibility(View.GONE);
                    }
                }else if (type==3){
                    if (TextUtils.isEmpty(common)){
                        holder.tv_device_name.setText(item.getName());
                        holder.tv_device_type.setText("智能终端");
                    }else {
                        String roomName=item.getRoomName();
                        holder.tv_device_name.setText(roomName);
                        holder.tv_device_type.setText(item.getName());
                    }
                    boolean online=item.getOnline();
                    Log.i("online","-->"+online);
                    int sensorState=item.getSensorState();
                    Log.i("online","-->"+sensorState);
                    item.setImg(img[1]);
                    if (online){
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.green2));
                        holder.view.setVisibility(View.VISIBLE);
                        if (sensorState==128){
                            holder.tv_device_switch.setText("USB供电");
                        }else if (sensorState==1){
                            holder.tv_device_switch.setText("电压正常");
                        }else if (sensorState==2){
                            holder.tv_device_switch.setText("电压低");
                        }else {
                            holder.tv_device_switch.setText("在线");
                        }
                    }else {
                        holder.view.setVisibility(View.GONE);
                        holder.tv_device_switch.setText("离线");
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.color_gray3));
                    }
                }else if (type==4){
                    if (TextUtils.isEmpty(common)){
                        holder.tv_device_name.setText(item.getName());
                        holder.tv_device_type.setText("智能插座");
                    }else {
                        String roomName=item.getRoomName();
                        holder.tv_device_name.setText(roomName);
                        holder.tv_device_type.setText(item.getName());
                    }
                    boolean online=item.getOnline();
                    Log.i("online","-->"+online);
//                    int sensorState=item.getSensorState();
                    int socketState=item.getSocketState();
                    Log.i("online","-->"+socketState);
                    item.setImg(img[2]);
                    if (online){
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.green2));
                        holder.view.setVisibility(View.VISIBLE);
                        if (socketState==1){
                            holder.tv_device_switch.setText("电源开");
                        }else if (socketState==0){
                            holder.tv_device_switch.setText("电源关");
                        }
                    }else {
                        holder.view.setVisibility(View.GONE);
                        holder.tv_device_switch.setText("离线");
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.color_gray3));
                    }
                }
                else if (type==5){
                    if (TextUtils.isEmpty(common)){
                        holder.tv_device_name.setText(item.getName());
                        holder.tv_device_type.setText("除湿机");
                    }else {
                        String roomName=item.getRoomName();
                        holder.tv_device_name.setText(roomName);
                        holder.tv_device_type.setText(item.getName());
                    }
                    boolean online=item.getOnline();
                    Log.i("online","-->"+online);
//                    int sensorState=item.getSensorState();
                    int socketState=item.getSocketState();
                    Log.i("online","-->"+socketState);
                    item.setImg(img[2]);
                    if (online){
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.green2));
                        holder.view.setVisibility(View.VISIBLE);
                        holder.tv_device_switch.setText("在线");
//                        if (socketState==1){
//                            holder.tv_device_switch.setText("电源开");
//                        }else if (socketState==0){
//                            holder.tv_device_switch.setText("电源关");
//                        }
                    }else {
                        holder.view.setVisibility(View.GONE);
                        holder.tv_device_switch.setText("离线");
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.color_gray3));
                    }
                }
                else if (type==6){
                    if (TextUtils.isEmpty(common)){
                        holder.tv_device_name.setText(item.getName());
                        holder.tv_device_type.setText("空调");
                    }else {
                        String roomName=item.getRoomName();
                        holder.tv_device_name.setText(roomName);
                        holder.tv_device_type.setText(item.getName());
                    }
                    boolean online=item.getOnline();
                    Log.i("online","-->"+online);
//                    int sensorState=item.getSensorState();
                    int socketState=item.getSocketState();
                    Log.i("online","-->"+socketState);
                    item.setImg(img[2]);
                    if (online){
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.green2));
                        holder.view.setVisibility(View.VISIBLE);
                        holder.tv_device_switch.setText("在线");
//                        if (socketState==1){
//                            holder.tv_device_switch.setText("电源开");
//                        }else if (socketState==0){
//                            holder.tv_device_switch.setText("电源关");
//                        }
                    }else {
                        holder.view.setVisibility(View.GONE);
                        holder.tv_device_switch.setText("离线");
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.color_gray3));
                    }
                }
                else if (type==7){
                    if (TextUtils.isEmpty(common)){
                        holder.tv_device_name.setText(item.getName());
                        holder.tv_device_type.setText("空气净化器");
                    }else {
                        String roomName=item.getRoomName();
                        holder.tv_device_name.setText(roomName);
                        holder.tv_device_type.setText(item.getName());
                    }
                    boolean online=item.getOnline();
                    Log.i("online","-->"+online);
//                    int sensorState=item.getSensorState();
                    int socketState=item.getSocketState();
                    Log.i("online","-->"+socketState);
                    item.setImg(img[2]);
                    if (online){
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.green2));
                        holder.view.setVisibility(View.VISIBLE);
                        holder.tv_device_switch.setText("在线");
//                        if (socketState==1){
//                            holder.tv_device_switch.setText("电源开");
//                        }else if (socketState==0){
//                            holder.tv_device_switch.setText("电源关");
//                        }
                    }else {
                        holder.view.setVisibility(View.GONE);
                        holder.tv_device_switch.setText("离线");
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.color_gray3));
                    }
                }else if (type==8){
                    item.setImg(img[1]);
                    boolean online=item.getOnline();
                    Log.i("common","-->"+common);
                    if (TextUtils.isEmpty(common)){
                        holder.tv_device_name.setText(item.getName());
                        holder.tv_device_type.setText("净水器");
                    }else {
                        String roomName=item.getRoomName();
                        Log.i("roomName","-->"+roomName);
                        holder.tv_device_name.setText(roomName);
                        holder.tv_device_type.setText(item.getName());
                    }
                    if (online){
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.green2));
                        holder.view.setVisibility(View.VISIBLE);
                        holder.tv_device_switch.setText("在线");
                    }else {
                        holder.view.setVisibility(View.GONE);
                        holder.tv_device_switch.setText("离线");
                        holder.tv_device_switch.setTextColor(mContext.getResources().getColor(R.color.color_gray3));
                    }
                }
                Picasso.with(mContext).load(item.getImg()).into(holder.imageView);
            }
            return convertView;
        }
    private class ViewHolder {
        TextView tv_device_switch;/**设备开关*/
        TextView tv_device_type;/**设备类型*/
        TextView tv_device_name;/**设备名称*/
        ImageView imageView;
        View view;//开关的小绿点
    }
}