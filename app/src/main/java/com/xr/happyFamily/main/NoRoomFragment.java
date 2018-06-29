package com.xr.happyFamily.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.ChangeEquipmentActivity;
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
    private long houseId;
    @BindView(R.id.rl_page) RelativeLayout rl_page;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_noroom_family,container,false);
        unbinder=ButterKnife.bind(this,view);
        hourseDao=new HourseDaoImpl(getActivity());
        return view;
    }

    @OnClick({R.id.rl_page,R.id.btn_add_room})
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
