package com.xr.happyFamily.zhen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AccountActivity extends AppCompatActivity {


    MyApplication application;
    Unbinder unbinder;
    @BindView(R.id.list_set) ListView list;
    private AccountAdapter adapter;
    private SharedPreferences preferences;
    String username;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_account);
        unbinder=ButterKnife.bind(this);
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        username=preferences.getString("username","");
        phone=preferences.getString("phone","");
        adapter=new AccountAdapter(this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==1){
                    Intent intent=new Intent(AccountActivity.this,ReSetpswdActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick({R.id.back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
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
    class AccountAdapter extends BaseAdapter{
        private Context context;
        String strs[]={"账号","更改登录密码"};
        public AccountAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 2;
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
            if (convertView==null){
                convertView=View.inflate(context,R.layout.item_account,null);
                viewHolder=new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            if (position==0){
                viewHolder.image_arrow.setVisibility(View.GONE);
            }else {
                viewHolder.image_arrow.setVisibility(View.VISIBLE);
            }
            if (position==0){
                viewHolder.tv_head.setText(strs[0]);
                viewHolder.tv_account.setText(username);
                viewHolder.tv_account.setVisibility(View.VISIBLE);
            }else if (position==1){
                viewHolder.tv_account.setVisibility(View.GONE);
                viewHolder.tv_head.setText(strs[1]);
            }
            return convertView;
        }

        class ViewHolder{
            @BindView(R.id.tv_head) TextView tv_head;
            @BindView(R.id.tv_account) TextView tv_account;
            @BindView(R.id.image_arrow) ImageView image_arrow;
            public ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
    }
}
