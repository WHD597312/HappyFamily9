package com.xr.happyFamily.jia.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.Room;
import com.xr.happyFamily.main.MainActivity;
import com.xr.happyFamily.together.util.BitmapCompressUtils;
import com.xr.happyFamily.together.util.GlideCircleTransform;

import java.io.File;
import java.net.URL;
import java.util.List;

public class RoomAdapter extends ArrayAdapter {
    private final int resourceId;
    Context mcontext;
    String url = "http://p9zaf8j1m.bkt.clouddn.com/room/choose/";
    String path;
    public RoomAdapter(Context context, int textViewResourceId, List<Room> objects) {
        super(context, textViewResourceId, objects);
        mcontext = context;
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Room room = (Room) getItem(position); // 获取当前项的Room实例
        String roomType= room.getRoomType();
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        ImageView roomImage = (ImageView) view.findViewById(R.id.iv_change);//获取该布局内的图片视图
        TextView roomName = (TextView) view.findViewById(R.id.tv_change_1);//获取该布局内的文本视图
//        roomImage.setImageResource(room.getImgId());//为图片视图设置图片资源
        roomName.setText(room.getRoomName());//为文本视图设置文本内容
        String image=room.getImgAddress();
        File file=new File(image);
        Glide.with(mcontext).load(file).into(roomImage);
        return view;

    }

//    class roomImageAsync extends AsyncTask<Void,Void,Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            Bitmap bitmap=null;
//
//            try {
//                GlideUrl glideUrl = new GlideUrl(path);
//                bitmap = Glide.with(mcontext)
//                        .load(glideUrl)
//                        .asBitmap()
//                        .centerCrop()
//                        .into(1440,442)
//                        .get();
//                if (bitmap != null) {
//                    File file = BitmapCompressUtils.compressImage(bitmap);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_change;
        TextView tv_address, tv_name;
        RelativeLayout rl_d1;
        RelativeLayout rl_d2;

        public MyViewHolder(View view) {
            super(view);


            tv_name = (TextView) view.findViewById(R.id.tv_hourse_h);
            tv_address = (TextView) view.findViewById(R.id.tv_hourse_ad);
            rl_d1 = (RelativeLayout) view.findViewById(R.id.rl_house_it1);
            rl_d1 = (RelativeLayout) view.findViewById(R.id.rl_house_it2);

        }

    }
}