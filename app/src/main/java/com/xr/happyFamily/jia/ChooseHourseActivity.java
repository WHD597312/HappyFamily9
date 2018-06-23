package com.xr.happyFamily.jia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.AddhourseActivity;
import com.xr.happyFamily.jia.adapter.ChooseHouseAdapter;
import com.xr.happyFamily.jia.adapter.HouseAdapter;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.titleview.TitleView;
import com.xr.happyFamily.together.http.HttpUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChooseHourseActivity extends AppCompatActivity {
    Unbinder unbinder;
    TitleView titleView;
    String ip = "http://47.98.131.11:8084;";
    SharedPreferences preferences;
    String houseName;
    String houseAddress;
    private HourseDaoImpl hourseDao;
   @BindView(R.id.lv_hourse_choose)
   RecyclerView recyclerView;
//    @BindView(R.id.iv_hourse_c)
    ImageView imageView1;
    @BindView(R.id.tv_hourse_choose)
    TextView textViewh;
    @BindView(R.id.tv_hourse_bj)
    TextView textViewbj;
    @BindView(R.id.tv_hourse_jtgl)
    TextView textViewgl;
    List<Hourse> hourses;
    ChooseHouseAdapter adapter;
    public static  final int MREQUEST_CODE=1;



    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);

        setContentView(R.layout.activity_house_choose);
        unbinder = ButterKnife.bind(this);
        titleView = (TitleView) findViewById(R.id.title_choose);
        titleView.setTitleText("家庭选择");
        imageView1= (ImageView) findViewById(R.id.iv_hourse_c);
//        preferences = getSharedPreferences("my", MODE_PRIVATE);
//        houseName=preferences.getString("phone", "");
//        houseAddress=preferences.getString("password", "");

        hourseDao=new HourseDaoImpl(getApplicationContext());
        hourses = hourseDao.findAllHouse();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChooseHouseAdapter(this, hourses);
        adapter.setClicked(-1);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    int i =0 ;
    @OnClick({R.id.tv_hourse_jtgl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hourse_jtgl:
//                imageView1.setImageResource(R.mipmap.hourse_xz);
                if (i==0){
                    textViewh.setText("家庭");
                    textViewbj.setText("添加");
                    textViewgl.setText("新建家庭");
                    textViewgl.setTextColor(getResources().getColor(R.color.green2));
                    adapter.setClicked(1);
                    adapter.notifyDataSetChanged();
                    i=1;
                }else if (i==1){
                    Intent intent=new Intent(this,AddhourseActivity.class);
                    startActivityForResult(intent,MREQUEST_CODE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int a=10;
        int b=a;
        if (requestCode==MREQUEST_CODE && requestCode==MREQUEST_CODE){
            hourses=hourseDao.findAllHouse();
            Log.i("house","-->"+hourses.size());
            adapter = new ChooseHouseAdapter(this, hourses);
            adapter.setClicked(-1);
            recyclerView.setAdapter(adapter);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
