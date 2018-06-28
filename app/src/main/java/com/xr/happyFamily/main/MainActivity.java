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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.pojo.Hourse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements FamilyFragmentManager.CallValueValue{

    Unbinder unbinder;
    FragmentManager fragmentManager;
    private HourseDaoImpl hourseDao;
    SharedPreferences mPositionPreferences;
    @BindView(R.id.layout_bottom) LinearLayout layout_bottom;
    @BindView(R.id.id_bto_jia_img) ImageButton id_bto_jia_img;
    @BindView(R.id.id_bto_bao_img) ImageButton id_bto_bao_img;
    private FamilyFragmentManager familyFragmentManager;
    private BaoFragment baoFragment;
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
        Intent intent=getIntent();
        long houseId=intent.getLongExtra("houseId",0);
        if (houseId==0){
            Hourse hourse=hourses.get(0);
            houseId=hourse.getHouseId();
        }
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        familyFragmentManager=new FamilyFragmentManager();
        baoFragment=new BaoFragment();
        Bundle bundle=new Bundle();
        bundle.putLong("houseId",houseId);
        familyFragmentManager.setArguments(bundle);
        fragmentTransaction.replace(R.id.layout_body,familyFragmentManager);
        fragmentTransaction.commit();
        mPositionPreferences=getSharedPreferences("position", Context.MODE_PRIVATE);
    }

    @OnClick({R.id.id_bto_jia,R.id.id_bto_bao})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.id_bto_jia:
                if (mPositionPreferences.contains("position")){
                    mPositionPreferences.edit().clear().commit();
                }
                List<Hourse> hourses=hourseDao.findAllHouse();
                Hourse hourse=hourses.get(0);
                long houseId=hourse.getHouseId();
                Bundle bundle=new Bundle();
                bundle.putLong("houseId",houseId);
                familyFragmentManager=new FamilyFragmentManager();
                familyFragmentManager.setArguments(bundle);
                FragmentTransaction familyTransaction=fragmentManager.beginTransaction();
                familyTransaction.replace(R.id.layout_body,familyFragmentManager);
                familyTransaction.commit();
                id_bto_jia_img.setImageResource(R.mipmap.jia1);
                id_bto_bao_img.setImageResource(R.mipmap.bao);
                break;
            case  R.id.id_bto_bao:
                id_bto_jia_img.setImageResource(R.mipmap.jia);
                id_bto_bao_img.setImageResource(R.mipmap.bao1);
                FragmentTransaction baoTransaction=fragmentManager.beginTransaction();
                baoTransaction.replace(R.id.layout_body,baoFragment);
                baoTransaction.commit();
                if (mPositionPreferences.contains("position")){
                    mPositionPreferences.edit().clear().commit();
                }
                break;

        }
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
