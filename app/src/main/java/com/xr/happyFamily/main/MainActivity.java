package com.xr.happyFamily.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.Hourse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements FamilyFragmentManager.CallValueValue{

    Unbinder unbinder;
    FragmentManager fragmentManager;
    private HourseDaoImpl hourseDao;
    SharedPreferences mPositionPreferences;
    @BindView(R.id.layout_bottom) LinearLayout layout_bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main_activity);
        unbinder=ButterKnife.bind(this);
        fragmentManager=getSupportFragmentManager();
        hourseDao=new HourseDaoImpl(getApplicationContext());
        List<Hourse> hourses=hourseDao.findAllHouse();
        Hourse hourse=hourses.get(0);

        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        long houseId=hourse.getHouseId();
        FamilyFragmentManager familyFragmentManager=new FamilyFragmentManager();
        Bundle bundle=new Bundle();
        bundle.putLong("houseId",houseId);
        familyFragmentManager.setArguments(bundle);
        fragmentTransaction.replace(R.id.layout_body,familyFragmentManager);
        fragmentTransaction.commit();
        mPositionPreferences=getSharedPreferences("position", Context.MODE_PRIVATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
        if (mPositionPreferences.contains("position")){
            mPositionPreferences.edit().clear().commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==6000){
            String room=data.getStringExtra("room");
            if (!TextUtils.isEmpty(room)){
                mPositionPreferences=getSharedPreferences("position", Context.MODE_PRIVATE);
                if (mPositionPreferences.contains("position")){
                    mPositionPreferences.edit().clear().commit();
                }
            }
            long houseId=data.getLongExtra("houseId",0);
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            FamilyFragmentManager familyFragmentManager=new FamilyFragmentManager();
            Bundle bundle=new Bundle();
            bundle.putLong("houseId",houseId);
            familyFragmentManager.setArguments(bundle);
            fragmentTransaction.replace(R.id.layout_body,familyFragmentManager);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void setPosition(int position) {
        Log.i("position222","-->"+position);
        if (position>=1){
            SharedPreferences.Editor editor=mPositionPreferences.edit();
            editor.putInt("position",position);
            editor.commit();
            layout_bottom.setVisibility(View.GONE);
        }else if (position==0){
            layout_bottom.setVisibility(View.VISIBLE);
        }
    }

}
