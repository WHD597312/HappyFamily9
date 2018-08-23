package com.xr.happyFamily.jia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.adapter.ChooseHouseAdapter;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.main.MainActivity;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChooseHourseActivity extends AppCompatActivity {
    Unbinder unbinder;

    String ip = "http://47.98.131.11:8084;";
    SharedPreferences preferences;

    private HourseDaoImpl hourseDao;
    @BindView(R.id.lv_hourse_choose)
    RecyclerView recyclerView;

    ImageView imageView1;
    @BindView(R.id.tv_hourse_choose)
    TextView textViewgl;
    List<Hourse> houses;
    Hourse house;
    Context context;
    ChooseHouseAdapter adapter;
    public static final int MREQUEST_CODE = 1;
    private MyApplication application;

    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);

        setContentView(R.layout.activity_house_choose);
        unbinder = ButterKnife.bind(this);
        if (application==null){
            application= (MyApplication) getApplication();
            application.addActivity(this);
        }
        imageView1 = (ImageView) findViewById(R.id.iv_hourse_c);
        hourseDao = new HourseDaoImpl(getApplicationContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        houses = hourseDao.findAllHouse();
        adapter = new ChooseHouseAdapter(this, houses);
        adapter.setClicked(-1);
        recyclerView.setAdapter(adapter);
        adapter.setSign(2);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            application.removeActivity(this);
            startActivity(new Intent(this,MainActivity.class));

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    int i = 0;

    @OnClick({R.id.tv_hourse_gl, R.id.iv_choose_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hourse_gl:
                Intent intent = new Intent(this, ManageHourseActivity.class);
                startActivityForResult(intent,3000);
//                startActivity(intent);
                break;
            case R.id.iv_choose_back:
                startActivity(new Intent(this, MainActivity.class));
//                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==3000){
            houses = hourseDao.findAllHouse();
            adapter = new ChooseHouseAdapter(this, houses);
            adapter.setClicked(-1);
            recyclerView.setAdapter(adapter);
            adapter.setSign(2);
        }
    }
}
