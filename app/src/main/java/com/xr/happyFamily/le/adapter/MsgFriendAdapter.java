package com.xr.happyFamily.le.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;
import com.xr.happyFamily.le.bean.MsgFriendBean;
import com.xr.happyFamily.le.pojo.ClockBean;
import com.xr.happyFamily.le.pojo.FriendData;
import com.xr.happyFamily.le.pojo.UserInfo;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.GlideCircleTransform;
import com.xr.happyFamily.together.util.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

//快递列表适配器

public class MsgFriendAdapter extends RecyclerView.Adapter<MsgFriendAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<FriendData> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type = 0;
    MyDialog dialog;
    int pos;

    public MsgFriendAdapter(Context context, List<FriendData> list) {
        this.context = context;
        this.data = list;
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }


    public interface OnItemListener {
        void onClick(View v, int pos, String projectc);
    }

    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    /**
     * 按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface {
        public void onclick(View view, int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_clock_msg_friend, parent,
                false));
        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (!Utils.isEmpty(data.get(position).getSenderHeadImgUrl()))
            Glide.with(context).load(data.get(position).getSenderHeadImgUrl()).transform(new GlideCircleTransform(context.getApplicationContext())).error(R.mipmap.ic_touxiang_moren).into(holder.img_touxiang);
        holder.tv_name.setText(data.get(position).getSenderName());
        holder.tv_age.setText(data.get(position).getSenderAge() + "");
        holder.tv_context.setText(data.get(position).getSenderRemark());
        if (data.get(position).getSenderSex() == "ture")
            holder.img_sex.setImageResource(R.mipmap.ic_clock_nan);
        else
            holder.img_sex.setImageResource(R.mipmap.ic_clock_nv);

        holder.tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = MyDialog.showDialog(context);
                dialog.show();
                Map<String, Object> params = new HashMap<>();
                params.put("frdId", data.get(position).getFrdId() + "");
                params.put("replay", 0 + "");
                pos = position;
                new replayClockFriendApplication().execute(params);
            }
        });

        holder.tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = MyDialog.showDialog(context);
                dialog.show();
                Map<String, Object> params = new HashMap<>();
                params.put("frdId", data.get(position).getFrdId() + "");
                params.put("replay", 1 + "");
                pos = position;
                new replayClockFriendApplication().execute(params);
            }
        });
//
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_zhuiping:
                context.startActivity(new Intent(context, PingLunActivity.class));
                break;

        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_touxiang, img_sex;
        TextView tv_no, tv_context, tv_yes, tv_name, tv_age;
        RelativeLayout rl_item;

        public MyViewHolder(View view) {
            super(view);

            img_touxiang = (ImageView) view.findViewById(R.id.img_touxiang);
            img_sex = (ImageView) view.findViewById(R.id.img_sex);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_age = (TextView) view.findViewById(R.id.tv_age);
            tv_no = (TextView) view.findViewById(R.id.tv_no);
            tv_yes = (TextView) view.findViewById(R.id.tv_yes);
            tv_context = (TextView) view.findViewById(R.id.tv_context);


        }


    }


    class replayClockFriendApplication extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String url = "/happy/clock/replayClockFriendApplication";
            Map<String, Object> params = maps[0];
            String clockFriendId = params.get("frdId").toString();
            String replay = params.get("replay").toString();

            url = url + "?clockFriendId=" + clockFriendId + "&replay=" + replay;

            String result = HttpUtils.doGet(context, url);

            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                MyDialog.closeDialog(dialog);
                Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();
                data.remove(pos);
                notifyDataSetChanged();
            }else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(context.getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = context.getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                context.startActivity(new Intent(context.getApplicationContext(), LoginActivity.class));
            }
        }
    }

}