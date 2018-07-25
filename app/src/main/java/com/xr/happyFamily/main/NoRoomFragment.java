package com.xr.happyFamily.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.ChangeEquipmentActivity;
import com.xr.happyFamily.jia.ChangeRoomActivity;
import com.xr.happyFamily.jia.ChooseHourseActivity;
import com.xr.happyFamily.jia.ManagementActivity;
import com.xr.happyFamily.jia.pojo.Hourse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NoRoomFragment extends Fragment {
    View view;
    Unbinder unbinder;
    public static final int MREQUEST_CODE=2;
    private HourseDaoImpl hourseDao;
    @BindView(R.id.tv_23_my1) TextView tv_23_my1;/**家庭温度*/
    Hourse hourse;
    private long houseId;
    @BindView(R.id.rl_page) RelativeLayout rl_page;
    @BindView(R.id.tv_noroom_hoursename)
    TextView textViewhousename;
    private String temperature;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_noroom_family,container,false);
        unbinder=ButterKnife.bind(this,view);
        hourseDao=new HourseDaoImpl(getActivity());
        hourse=hourseDao.findById(houseId);
        if (hourse!=null){
            if (!TextUtils.isEmpty(temperature)){
                tv_23_my1.setText(temperature);
            }
            String hourseName = hourse.getHouseName();
            String address=hourse.getHouseAddress();
            textViewhousename.setText(hourseName+"·"+address);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!TextUtils.isEmpty(temperature)){
            tv_23_my1.setText(temperature);
        }
    }

    @OnClick({R.id.rl_page,R.id.btn_add_room,R.id.tv_noroom_hoursename,R.id.ib_croom})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_page:
                Intent intent4=new Intent(getActivity(), ChangeEquipmentActivity.class);
                startActivity(intent4);
                break;
            case R.id.btn_add_room:
                Intent intent=new Intent(getActivity(), ManagementActivity.class);
                intent.putExtra("houseId",houseId);
                startActivityForResult(intent,MREQUEST_CODE);
                break;
            case R.id.tv_noroom_hoursename:
                Intent intent2=new Intent(getActivity(), ChooseHourseActivity.class);
                startActivity(intent2);
                break;
            case R.id.ib_croom:
                Intent intent3 = new Intent(getActivity(), ChangeRoomActivity.class);
                intent3.putExtra("houseId", houseId);
                startActivityForResult(intent3, MREQUEST_CODE);
                getActivity().overridePendingTransition(R.anim.topout, R.anim.topout);
                break;
        }
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
        if (!TextUtils.isEmpty(temperature)){
            if (tv_23_my1!=null){
                tv_23_my1.setText(temperature);
            }
        }
    }

    public void setHouseId(long houseId) {
        this.houseId = houseId;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
