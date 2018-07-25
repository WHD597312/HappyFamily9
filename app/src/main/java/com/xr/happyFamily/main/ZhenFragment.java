package com.xr.happyFamily.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.xr.happyFamily.R;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.BitmapCompressUtils;
import com.xr.happyFamily.together.util.GlideCircleTransform;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.zhen.AboutActivity;
import com.xr.happyFamily.zhen.AccountActivity;
import com.xr.happyFamily.zhen.HelpActivity;
import com.xr.happyFamily.zhen.PersonInfoActivity;
import com.xr.happyFamily.zhen.SettingActivity;


import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class ZhenFragment extends Fragment {
    View view;
    Unbinder unbinder;
    @BindView(R.id.list_info) ListView list_info;
    @BindView(R.id.tv_user_name) TextView tv_user_name;/**用户昵称*/
    @BindView(R.id.image_user) ImageView image_user;/**用户头像*/
    @BindView(R.id.tv_phone) TextView tv_phone;/**用户账号*/
    private MyAdapter myAdapter;
    SharedPreferences preferences;
    private String downpLoadImg= HttpUtils.ipAddress+"/user/headImg/";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_my,container,false);
        unbinder=ButterKnife.bind(this,view);
        myAdapter=new MyAdapter(getActivity());
        preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
        list_info.setAdapter(myAdapter);
        return view;
    }
    @OnClick({R.id.head})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.head:
                Intent intent=new Intent(getActivity(), PersonInfoActivity.class);
                startActivityForResult(intent,7000);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (preferences.contains("image")){
            String image=preferences.getString("image","");
            if (!Utils.isEmpty(image)){
                File file=new File(image);
                Glide.with(getActivity()).load(file).transform(new GlideCircleTransform(getActivity())).into(image_user);
            }
        }
        if (preferences.contains("username")){
            String username=preferences.getString("username","");
            if(TextUtils.isEmpty(username)){
                tv_user_name.setText("admin");
            }else {
                tv_user_name.setText(username);
            }
        }else {
            tv_user_name.setText("admin");
        }
        if (preferences.contains("phone")){
            String phone=preferences.getString("phone","");
            tv_phone.setText(phone);
        }
        list_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent account=new Intent(getActivity(), AccountActivity.class);
                        startActivity(account);
                        break;
                    case 2:
                        Intent help=new Intent(getActivity(), HelpActivity.class);
                        startActivity(help);
                        break;
                    case 4:
                        Intent about=new Intent(getActivity(), AboutActivity.class);
                        startActivity(about);
                        break;
                    case 6:
                        Intent setIntent=new Intent(getActivity(), SettingActivity.class);
                        startActivityForResult(setIntent,7000);
                        break;
                }
            }
        });
    }
    class LoadUserImageAsync extends AsyncTask<Void,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap=null;
            try {
                String userId=preferences.getString("userId","");
                String token=preferences.getString("token","");
                String url =downpLoadImg;
                GlideUrl glideUrl=new GlideUrl(url,new LazyHeaders.Builder().addHeader("authorization",token).build());
                bitmap= Glide.with(getActivity())
                        .load(glideUrl)
                        .asBitmap()
                        .centerCrop()
                        .into(180,180)
                        .get();
            }catch (Exception e) {
                e.printStackTrace();
                Log.e("eee",e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap!=null){
                File file= BitmapCompressUtils.compressImage(bitmap);
                preferences.edit().putString("image",file.getPath()).commit();
                Glide.with(getActivity()).load(file).transform(new GlideCircleTransform(getActivity())).into(image_user);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
    class MyAdapter extends BaseAdapter{
        private Context context;
        public MyAdapter(Context context){
            this.context=context;
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            int imgs[]={R.mipmap.optimal,R.mipmap.secure,R.mipmap.feedback,R.mipmap.aboutus,
                    R.mipmap.setting};
            String strs[]={"优点子","账号与安全","帮助与反馈","关于我们","设置"};
            switch (position){
//                case 0:
//                    convertView=View.inflate(context,R.layout.item_my,null);
//                    viewHolder=new ViewHolder(convertView);
//                    viewHolder.image_icon.setImageResource(imgs[0]);
//                    viewHolder.tv_item.setText(strs[0]);
//                    break;
//                case 1:
//                    convertView=View.inflate(context,R.layout.view3,null);
//                    convertView.setMinimumHeight(3);
//                    break;
//                case 2:
//                    convertView=View.inflate(context,R.layout.view2,null);
//                    convertView.setMinimumHeight(50);
//                    break;
                case 0:
                    convertView=View.inflate(context,R.layout.item_my,null);
                    viewHolder=new ViewHolder(convertView);
//                    viewHolder.image_icon.setImageResource(imgs[1]);
                    Glide.with(getActivity()).load(R.mipmap.secure).override(50,50).into(viewHolder.image_icon);
                    viewHolder.tv_item.setText(strs[1]);
                    break;
                case 1:
                    convertView=View.inflate(context,R.layout.view3,null);
                    convertView.setMinimumHeight(3);
                    break;
                case 2:
                    convertView=View.inflate(context,R.layout.item_my,null);
                    viewHolder=new ViewHolder(convertView);
//                    viewHolder.image_icon.setImageResource(imgs[2]);
                    Glide.with(getActivity()).load(R.mipmap.feedback).override(50,50).into(viewHolder.image_icon);
                    viewHolder.tv_item.setText(strs[2]);
                    break;
                case 3:
                    convertView=View.inflate(context,R.layout.view3,null);
                    convertView.setMinimumHeight(3);
                    break;
                case 4:
                    convertView=View.inflate(context,R.layout.item_my,null);
                    viewHolder=new ViewHolder(convertView);
//                    viewHolder.image_icon.setImageResource(imgs[3]);
                    Glide.with(getActivity()).load(R.mipmap.aboutus).override(50,50).into(viewHolder.image_icon);
                    viewHolder.tv_item.setText(strs[3]);
                    break;
                case 5:
                    convertView=View.inflate(context,R.layout.view2,null);
                    convertView.setMinimumHeight(30);
                    break;
                case 6:
                    convertView=View.inflate(context,R.layout.item_my,null);
                    viewHolder=new ViewHolder(convertView);
//                    viewHolder.image_icon.setImageResource(imgs[4]);
                    Glide.with(getActivity()).load(R.mipmap.setting).override(50,50).into(viewHolder.image_icon);
                    viewHolder.tv_item.setText(strs[4]);
                    break;
            }
            return convertView;
        }

        class ViewHolder{
            @BindView(R.id.image_icon) ImageView image_icon;
            @BindView(R.id.tv_item) TextView tv_item;
            @BindView(R.id.image_arrow) ImageView image_arrow;
            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
    }
}
