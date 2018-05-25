package com.xr.happyFamily.jia.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.AddEquipmentActivity;
import com.xr.happyFamily.jia.ChangeRoomActivity;
import com.xr.happyFamily.jia.MyPaperActivity;
import com.xr.happyFamily.jia.adapter.GridViewAdapter;
import com.xr.happyFamily.jia.pojo.Equipment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LivingFragment extends Fragment {
    private ImageButton mLeftMenu;
    private String[] localCartoonText = {"客厅", "厨房", "卧室", "阳台", "阳台", "阳台",};
    private Integer[] img = {R.mipmap.t3x, R.mipmap.t3x, R.mipmap.t3x, R.mipmap.t3x, R.mipmap.t3x, R.mipmap.t3x};

    private GridViewAdapter mGridViewAdapter = null;
    private ArrayList<Equipment> mGridData = null;
    Unbinder unbinder;
    @BindView(R.id.bt_lv_add)
    Button buttonadd;
    @BindView(R.id.gv_home)
    com.xr.happyFamily.jia.MyGridview mGridView;
    @BindView(R.id.lv_li)
    LinearLayout li;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_home_living, container, false);

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
        buttonadd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), AddEquipmentActivity.class));
            }
        });
        li.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), ChangeRoomActivity.class));
            }
        });
        return view;

    }
}
