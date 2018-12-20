package com.xr.happyFamily.jia.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.Room;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomAdapter extends ArrayAdapter {
    private final int resourceId;
    private List<Room> rooms;
    private Context mContext;
    public RoomAdapter(Context context, int textViewResourceId, List<Room> objects) {
       super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        this.rooms=objects;
        this.mContext=context;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getCount() {
        return rooms.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return super.getPosition(item);
    }

    private String imageUrl="http://img.p99.store/room/choose/";
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView=View.inflate(mContext,resourceId,null);
        ViewHolder viewHolder=new ViewHolder(convertView);
        Room room = rooms.get(position); // 获取当前项的Room实例
        if (room!=null){
            String roomType=room.getRoomType();
            String image="";
            if ("客厅".equals(roomType)){
                image="living_room.png";
                viewHolder.tv_change_2.setText("Living Room");
            }else if ("卧室".equals(roomType)){
                image="bedroom.png";
                viewHolder.tv_change_2.setText("Bedroom");
            }else if ("餐厅".equals(roomType)){
                image="canteen.png";
                viewHolder.tv_change_2.setText("Canteen");
            }else if ("卫生间".equals(roomType)){
                image="toilet.png";
                viewHolder.tv_change_2.setText("Toilet");
            }else if ("浴室".equals(roomType)){
                image="bathroom.png";
                viewHolder.tv_change_2.setText("Bathroom");
            }else if ("厨房".equals(roomType)){
                image="kitchen.png";
                viewHolder.tv_change_2.setText("Kitchen");
            }else if ("儿童房".equals(roomType)){
                image="childrens_room.png";
                viewHolder.tv_change_2.setText("Childrens Room");
            }else if ("婴儿房".equals(roomType)){
                image="baby_room.png";
                viewHolder.tv_change_2.setText("Baby Room");
            }else if ("活动室".equals(roomType)){
                image="activity_room.png";
                viewHolder.tv_change_2.setText("Activity Room");
            }else if ("媒体房".equals(roomType)){
                image="media_room.png";
                viewHolder.tv_change_2.setText("Media Room");
            }else if ("办公室".equals(roomType)){
                image="office.png";
                viewHolder.tv_change_2.setText("Office");
            }else if ("休闲室".equals(roomType)){
                image="lounge.png";
                viewHolder.tv_change_2.setText("Lounge");
            }else if ("书房".equals(roomType)){
                image="study.png";
                viewHolder.tv_change_2.setText("Study");
            }else if ("工作室".equals(roomType)){
                image="studio.png";
                viewHolder.tv_change_2.setText("Studio");
            }else if ("衣帽间".equals(roomType)){
                image="cloakroom.png";
                viewHolder.tv_change_2.setText("Cloakroom");
            }else if ("后院".equals(roomType)){
                image="backyard.png";
                viewHolder.tv_change_2.setText("Backyard");
            }
            String url=imageUrl+image;
            Glide.with(mContext).load(url).into(viewHolder.iv_change);
            viewHolder.tv_change_1.setText(room.getRoomName());//为文本视图设置文本内容
        }
        return convertView;
    }
    class ViewHolder{
        @BindView(R.id.iv_change) ImageView iv_change;
        @BindView(R.id.tv_change_1) TextView tv_change_1;
        @BindView(R.id.tv_change_2) TextView tv_change_2;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
