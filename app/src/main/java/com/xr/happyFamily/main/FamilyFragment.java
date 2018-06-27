package com.xr.happyFamily.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupMenu;

import com.xr.database.dao.daoimpl.HourseDaoImpl;
import com.xr.database.dao.daoimpl.RoomDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.ManagementActivity;
import com.xr.happyFamily.jia.pojo.Hourse;
import com.xr.happyFamily.jia.view_custom.HomeDialog;
import com.xr.happyFamily.together.http.HttpUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FamilyFragment  extends Fragment{
    private String ip=HttpUtils.ipAddress;
    View view;
    Unbinder unbinder;
    private RoomDaoImpl roomDao;
    public static final int MREQUEST_CODE=6000;
    private int clicked=-1;
    private HourseDaoImpl hourseDao;
    private long houseId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_room_famliy,container,false);
        unbinder=ButterKnife.bind(this,view);
        roomDao=new RoomDaoImpl(getActivity());
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
