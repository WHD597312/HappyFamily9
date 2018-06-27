package com.xr.happyFamily.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.ManagementActivity;
import com.xr.happyFamily.jia.pojo.Hourse;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NoRoomFragment extends Fragment {
    View view;
    Unbinder unbinder;
    public static final int MREQUEST_CODE=2;
    private HourseDaoImpl hourseDao;
    private long houseId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_noroom_family,container,false);
        unbinder=ButterKnife.bind(this,view);
        hourseDao=new HourseDaoImpl(getActivity());
        List<Hourse> hourses=hourseDao.findAllHouse();
        Hourse hourse=hourses.get(0);
        houseId=hourse.getHouseId();

        return view;
    }
    @OnClick({R.id.btn_add_room})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_add_room:
                Intent intent=new Intent(getActivity(), ManagementActivity.class);
                intent.putExtra("houseId",houseId);
                startActivityForResult(intent,MREQUEST_CODE);
//                Map<String,Object> params=new HashMap<>();
//                params.put("roomName",roomName);
//                params.put("roomType",roomType);
//                params.put("houseId",houseId);
//                new ManagementActivity.AddroomAsyncTask().execute(params);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
