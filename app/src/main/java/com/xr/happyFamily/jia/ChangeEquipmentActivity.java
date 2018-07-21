package com.xr.happyFamily.jia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.mikephil.charting.formatter.IFillFormatter;
import com.xr.happyFamily.R;

import com.xr.happyFamily.jia.adapter.MyAdapter;
import com.xr.happyFamily.jia.xnty.AirConditionerActivity;
import com.xr.happyFamily.jia.xnty.AircleanerActivity;
import com.xr.happyFamily.jia.xnty.CsjActivity;
import com.xr.happyFamily.jia.xnty.HeaterActivity;
import com.xr.happyFamily.jia.xnty.SmartSocket;
import com.xr.happyFamily.jia.xnty.WaterPurifierActivity;
import com.xr.happyFamily.jia.xnty.ZnWdActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ChangeEquipmentActivity extends AppCompatActivity {

    private List<String> mData = new ArrayList<String>(Arrays.asList("智能终端升级版", "户外空调", "智能插座基础版", "空气净化器","除湿机","取暖器","净水器","户外监测仪"));
    private Context context;
    Unbinder unbinder ;
    private MyApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        context = this;
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        setContentView(R.layout.activity_home_xnsb);
        unbinder = ButterKnife.bind(this);
        ListView listView = (ListView) findViewById(R.id.xnsb_list);
        MyAdapter mAdapter = new MyAdapter(context, mData);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){


                    case 0:
                        Intent localIntent2=new Intent(ChangeEquipmentActivity.this,ZnWdActivity.class);
                        startActivity(localIntent2);
                        break;
                    case 1:
                        Intent localIntent3=new Intent(ChangeEquipmentActivity.this,AirConditionerActivity.class);
                        startActivity(localIntent3);
                        break;
                    case 2:
                        Intent localIntent4=new Intent(ChangeEquipmentActivity.this,SmartSocket.class);
                        startActivity(localIntent4);
                        break;
                    case 3:
                        Intent localIntent=new Intent(ChangeEquipmentActivity.this,AircleanerActivity.class);
                        startActivity(localIntent);
                        break;
                    case 4:
                        Intent localIntent5=new Intent(ChangeEquipmentActivity.this,CsjActivity.class);
                        startActivity(localIntent5);
                        break;
                    case 5:
                        Intent localIntent6=new Intent(ChangeEquipmentActivity.this,HeaterActivity.class);
                        startActivity(localIntent6);
                        break;
                    case 6:
                        Intent localIntent7=new Intent(ChangeEquipmentActivity.this,WaterPurifierActivity.class);
                        startActivity(localIntent7);
                        break;
                }

            }  });
    }
    @OnClick({R.id.ib_xnty})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_xnty:
              finish();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            application.removeActivity(this);
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


}





