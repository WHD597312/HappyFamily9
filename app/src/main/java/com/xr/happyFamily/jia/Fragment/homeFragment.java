package com.xr.happyFamily.jia.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.AddEquipmentActivity;
import com.xr.happyFamily.jia.AddRoomActivity;
import com.xr.happyFamily.jia.ChangeEquipmentActivity;
import com.xr.happyFamily.jia.ChangeRoomActivity;
import com.xr.happyFamily.jia.HourseActivity;
import com.xr.happyFamily.jia.MyPaperActivity;
import com.xr.happyFamily.jia.adapter.GridViewAdapter;
import com.xr.happyFamily.jia.pojo.Equipment;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class homeFragment extends Fragment {

    private String[] localCartoonText = {"客厅", "厨房", "卧室", "阳台", "阳台", "阳台",};
    private Integer[] img = {R.mipmap.t, R.mipmap.t, R.mipmap.t, R.mipmap.t, R.mipmap.t, R.mipmap.t};

    private GridViewAdapter mGridViewAdapter = null;
    private ArrayList<Equipment> mGridData = null;
    Unbinder unbinder;
    @BindView(R.id.bt_mypage_add)
    Button buttonadd;
    @BindView(R.id.rl_home_xnty)
    RelativeLayout relativeLayout;
    @BindView(R.id.gv_home_my)
    com.xr.happyFamily.jia.MyGridview mGridView;
    @BindView(R.id.gv_home_my1)
    com.xr.happyFamily.jia.MyGridview mGridView1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_home_mypage, container, false);
        unbinder=ButterKnife.bind(this,view);
        mGridData = new ArrayList<>();
        for (int i = 0; i < img.length; i++) {
            Equipment item = new Equipment();
            item.setName(localCartoonText[i]);
            item.setImgeId(img[i]);
            mGridData.add(item);
        }
        mGridViewAdapter = new GridViewAdapter(getActivity(), R.layout.activity_home_item, mGridData);
        mGridView.setAdapter(mGridViewAdapter);
        mGridView1.setAdapter(mGridViewAdapter);
        buttonadd.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), AddEquipmentActivity.class));
            }
        });

        return view;
    }
    @OnClick({R.id.image_change,R.id.tv_my_hourse})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_change:
                startActivity(new Intent(getActivity(), AddRoomActivity.class));
                break;
            case R.id.tv_my_hourse:
                startActivity(new Intent(getActivity(), HourseActivity.class));
                break;


        }

    }
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);




        relativeLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeEquipmentActivity.class);
                startActivity(intent);
            }
        });

    }


}
