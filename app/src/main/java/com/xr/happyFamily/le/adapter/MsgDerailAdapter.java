package com.xr.happyFamily.le.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xr.database.dao.daoimpl.DerailBeanDaoImpl;
import com.xr.database.dao.daoimpl.FriendDataDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.PingLunActivity;

import com.xr.happyFamily.le.UStats;
import com.xr.happyFamily.le.clock.MsgActivity;
import com.xr.happyFamily.le.pojo.DerailBean;
import com.xr.happyFamily.le.pojo.FriendData;
import com.xr.happyFamily.le.view.YouguiDialog;
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

public class MsgDerailAdapter extends RecyclerView.Adapter<MsgDerailAdapter.MyViewHolder> {
    private Context context;
    private List<DerailBean> data;
    private ButtonInterface buttonInterface;

    private int defItem = -1;
    private OnItemListener onItemListener;
    private int type = 0;
    MyDialog dialog;
    int pos;

    public MsgDerailAdapter(Context context, List<DerailBean> list) {
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
                context).inflate(R.layout.item_clock_msg_derail, parent,
                false));
        return holder;
    }

   int derailResult=-1;
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       holder.tv_name.setText(data.get(position).getDerailName());
        holder.tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = MyDialog.showDialog(context);
                dialog.show();
                Map<String, Object> params = new HashMap<>();
                params.put("derailId", data.get(position).getDerailId() + "");
                params.put("derailResult", 0 + "");
                derailResult=0;
                pos = position;
                new replayDerailedAsynTask().execute(params);
            }
        });

        holder.tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = MyDialog.showDialog(context);
                dialog.show();
                Map<String, Object> params = new HashMap<>();
                params.put("derailId", data.get(position).getDerailId() + "");
                params.put("derailResult", 1 + "");
                derailResult=1;
                pos = position;
                new replayDerailedAsynTask().execute(params);
                ((MsgActivity)context).starDerail();
                if (UStats.getUsageStatsList(context).isEmpty()) {
                    clolkDialog2();
                }


            }
        });
//
    }

    YouguiDialog youguiDialog;

    private void clolkDialog2() {
        youguiDialog = new YouguiDialog(context);


        youguiDialog.setOnPositiveClickListener(new YouguiDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {

                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                context. startActivity(intent);

                youguiDialog.dismiss();
            }
        });

        youguiDialog.setCanceledOnTouchOutside(false);

        youguiDialog.show();

    }

    public int getDerailResult() {
        return derailResult;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_no, tv_yes, tv_name;
        RelativeLayout rl_item;

        public MyViewHolder(View view) {
            super(view);


            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_no = (TextView) view.findViewById(R.id.tv_no);
            tv_yes = (TextView) view.findViewById(R.id.tv_yes);


        }


    }


    class replayDerailedAsynTask extends AsyncTask<Map<String, Object>, Void, String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String url = "/happy/derailed/replayDerailed?";
            Map<String, Object> params = maps[0];
            String derailId = params.get("derailId").toString();
            String derailResult = params.get("derailResult").toString();

            url = url + "derailId=" + derailId + "&derailResult=" + derailResult;

            Log.e("qqqqqqTTTT",url);
            String result = HttpUtils.doGet(context, url);

            String code = "";
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    }
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");

                    Log.e("qqqqqqTTTT",result);
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
                DerailBeanDaoImpl derailBeanDao = new DerailBeanDaoImpl(context);
                List<DerailBean> derailBeans = derailBeanDao.findByDerailId(data.get(pos).getDerailId());
                if (derailResult==0){
                    ((MsgActivity)context).setderailPos(0);
                }else if (derailResult==1){
                    ((MsgActivity)context).setderailPos(1);
                }
                if (derailBeans.size() != 0)
                    derailBeanDao.delete(derailBeans.get(0));
                data.remove(pos);
                notifyDataSetChanged();
            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
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