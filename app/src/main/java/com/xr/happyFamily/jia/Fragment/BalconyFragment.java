package com.xr.happyFamily.jia.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View.OnClickListener;
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

public class BalconyFragment extends Fragment {
    private ImageButton mLeftMenu;
    private String[] localCartoonText = {"客厅", "厨房", "卧室", "阳台", "阳台", "阳台",};
    private Integer[] img = {R.mipmap.t, R.mipmap.t, R.mipmap.t, R.mipmap.t, R.mipmap.t, R.mipmap.t};

    private GridViewAdapter mGridViewAdapter = null;
    private ArrayList<Equipment> mGridData = null;
    Unbinder unbinder;
    @BindView(R.id.bt_balcony_add)
    Button buttonadd;
    @BindView(R.id.balcony_li)
    LinearLayout li;
    @BindView(R.id.gv_balcony_home)
    com.xr.happyFamily.jia.MyGridview mGridView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_home_balcony, container, false);

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
        buttonadd.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), AddEquipmentActivity.class));
            }
        });
        li.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                startActivityForResult(new Intent(getActivity(), ChangeRoomActivity.class),5);
//                startActivity();
            }
        });
        return view;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int a=4;
        int b=a;

        switch (resultCode) {
            case 5:
                Log.i("aaaa", "onActivityResult: ");
            break;
        }
    }
}
