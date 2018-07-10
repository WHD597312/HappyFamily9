package com.xr.happyFamily.zhen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.login.login.LoginActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingActivity extends AppCompatActivity {

    private SettingAdatper adatper;
    @BindView(R.id.list_set) ListView list_set;
    Unbinder unbinder;
    private MyApplication application;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        unbinder=ButterKnife.bind(this);
        adatper=new SettingAdatper(this);
        list_set.setAdapter(adatper);
    }

    @OnClick({R.id.back,R.id.btn_exit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_exit:
                if(preferences.contains("password")){
                    preferences.edit().remove("password").commit();
                }
                SharedPreferences mPositionPreferences = getSharedPreferences("position", MODE_PRIVATE);
                mPositionPreferences.edit().clear().commit();
                Intent exit=new Intent(this, LoginActivity.class);
                startActivity(exit);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeAllActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

    class SettingAdatper extends BaseAdapter{

        private Context context;
        public SettingAdatper(Context context){
            this.context=context;
        }
        @Override
        public int getCount() {
            return 9;
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
            String []str={"更新","检查更新","通用","隐私设置","清理缓存"};

            ViewHolder viewHolder=null;
            ViewHolder2 viewHolder2=null;
            switch (position){
                case 0:
                    convertView=View.inflate(context,R.layout.item_set,null);
                    viewHolder=new ViewHolder(convertView);
                    viewHolder.tv_head.setText(str[0]);
                    break;
                case 1:
                    convertView=View.inflate(context,R.layout.item_set2,null);
                    break;
                case 2:
                    convertView=View.inflate(context,R.layout.view3,null);
                    convertView.setMinimumHeight(3);
                    break;
                case 3:
                    convertView=View.inflate(context,R.layout.item_set3,null);
                    viewHolder2=new ViewHolder2(convertView);
                    viewHolder2.tv_head.setText(str[1]);
                    break;
                case 4:
                    convertView=View.inflate(context,R.layout.view3,null);
                    convertView.setMinimumHeight(3);
                    break;
                case 5:
                    convertView=View.inflate(context,R.layout.item_set,null);
                    viewHolder=new ViewHolder(convertView);
                    viewHolder.tv_head.setText(str[2]);
                    viewHolder.tv_head.setBackgroundColor(Color.parseColor("#faf9f9"));
                    break;
                case 6:
                    convertView=View.inflate(context,R.layout.item_set3,null);
                    viewHolder2=new ViewHolder2(convertView);
                    viewHolder2.tv_head.setText(str[3]);
                    break;
                case 7:
                    convertView=View.inflate(context,R.layout.view3,null);
                    convertView.setMinimumHeight(3);
                    break;
                case 8:
                    convertView=View.inflate(context,R.layout.item_set3,null);
                    viewHolder2=new ViewHolder2(convertView);
                    viewHolder2.tv_head.setText(str[4]);
                    break;
            }
            return convertView;
        }

        class ViewHolder{
            @BindView(R.id.tv_head) TextView tv_head;
            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
        class ViewHolder2{
            @BindView(R.id.tv_head) TextView tv_head;
            public ViewHolder2(View view){
                ButterKnife.bind(this,view);
            }
        }
    }
}
