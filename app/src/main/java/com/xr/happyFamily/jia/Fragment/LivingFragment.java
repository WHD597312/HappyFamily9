package com.xr.happyFamily.jia.Fragment;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.xr.happyFamily.R;
import com.xr.happyFamily.jia.AddEquipmentActivity;
import com.xr.happyFamily.jia.ChangeRoomActivity;
import com.xr.happyFamily.jia.MyPaperActivity;
import com.xr.happyFamily.jia.adapter.GridViewAdapter;
import com.xr.happyFamily.jia.pojo.Equipment;
import com.xr.happyFamily.login.rigest.RegistActivity;
import com.xr.happyFamily.login.rigest.RegistFinishActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LivingFragment extends Fragment {
    private ImageButton mLeftMenu;
    private String[] localCartoonText = {"客厅", "厨房", "卧室", "阳台", "阳台", "阳台",};
    private Integer[] img = {R.mipmap.t, R.mipmap.t, R.mipmap.t, R.mipmap.t, R.mipmap.t, R.mipmap.t};
    Dialog dia ;
    private GridViewAdapter mGridViewAdapter = null;
    private ArrayList<Equipment> mGridData = null;
    Unbinder unbinder;
//    @BindView(R.id.bt_balcony_add)
//    Button buttonadd;
//    @BindView(R.id.tv_gl)
//    TextView textViewgl;
//    @BindView(R.id.gv_home)
//    com.xr.happyFamily.jia.MyGridview mGridView;
//    @BindView(R.id.lv_li)
//    LinearLayout li;
//
//    String roomName,roomType,roomId;
    @BindView(R.id.bt_balcony_add)
    Button buttonadd;
    @BindView(R.id.tv_balcony_gl)
    TextView textViewgl;
    @BindView(R.id.balcony_li)
    LinearLayout li;
    String roomName,roomType,roomId;
    @BindView(R.id.gv_balcony_home)
    com.xr.happyFamily.jia.MyGridview mGridView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_home_balcony, container, false);

        unbinder=ButterKnife.bind(this,view);
        android.support.percent.PercentRelativeLayout percentRelativeLayout=
                ( android.support.percent.PercentRelativeLayout)view.findViewById(R.id.pr_view);
        TextView textViewr = (TextView) view.findViewById(R.id.tv_roomname);
        percentRelativeLayout.setBackground(getResources().getDrawable(R.mipmap.bg_keting));

        mGridData = new ArrayList<>();
        for (int i = 0; i < img.length; i++) {
            Equipment item = new Equipment();
            item.setName(localCartoonText[i]);
            item.setImgeId(img[i]);
            mGridData.add(item);
        }
        Bundle bundle=getArguments();
        if (bundle!=null){
             roomName=bundle.getString("roomName");
             roomType=bundle.getString("roomType");
             roomId=bundle.getString("roomId");
            textViewr.setText(roomName);
        }
        mGridViewAdapter = new GridViewAdapter(getActivity(), R.layout.activity_home_item, mGridData);
        mGridView.setAdapter(mGridViewAdapter);
        buttonadd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), AddEquipmentActivity.class));
                Log.i("dddddd4", "------->: "+roomName+"....."+roomType+"....."+roomId);
            }
        });
        li.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(getActivity(), ChangeRoomActivity.class),4);
            }
        });
        return view;

    }
//    @OnClick({R.id.tv_gl})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_gl:
//                showPopupMenu(textViewgl);
//                break;
//
//
//
//        }
//
//    }
@OnClick({R.id.tv_balcony_gl,R.id.iv_home_fh})
public void onClick(View view) {
    switch (view.getId()) {
        case R.id.tv_balcony_gl:
            showPopupMenu(textViewgl);
            break;

        case R.id.iv_home_fh:
            startActivityForResult(new Intent(getActivity(), MyPaperActivity.class),5);
            break;

    }
}
    String title;
    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
//                Log.i("title", "---->: "+item.getTitle());
                title = String.valueOf(item.getTitle());
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                if ("更改房间名".equals(title)){
                    dia = new Dialog(getActivity(), R.style.edit_AlertDialog_style);//设置进入时跳出提示框
                    dia.setContentView(R.layout.activity_home_dedialog);
//                    relativeLayoutre.setBackgroundResource(R.drawable.bg_shape);
                    dia.show();
                    dia.setCanceledOnTouchOutside(true); // 设置屏幕点击退出
                    Window w = dia.getWindow();
                    WindowManager.LayoutParams lp = w.getAttributes();
                    lp.x = 0;
                    dia.onWindowAttributesChanged(lp);
                }
                if ("删除房间".equals(title)){
                    dia = new Dialog(getActivity(), R.style.edit_AlertDialog_style);//设置进入时跳出提示框
                    dia.setContentView(R.layout.activity_home_renamedialog);
//                    relativeLayoutre.setBackgroundResource(R.drawable.bg_shape);
                    dia.show();
                    dia.setCanceledOnTouchOutside(true); // 设置屏幕点击退出
                    Window w = dia.getWindow();
                    WindowManager.LayoutParams lp = w.getAttributes();
                    lp.x = 0;
                    dia.onWindowAttributesChanged(lp);
                }
            }
        });

        popupMenu.show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("aaaa", "---->: "+requestCode);
        switch (resultCode) {

            case 1:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container ,new KitchenFragment(), null)
                        .addToBackStack(null)
                        .commit();


                break;
            case 2:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new LivingFragment(), null)
                        .addToBackStack(null)
                        .commit();

                break;
            case 3:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container ,new BathroomFragment(), null)
                        .addToBackStack(null)
                        .commit();

                break;
            case 4:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container ,new RoomFragment(), null)
                        .addToBackStack(null)
                        .commit();


                break;
            case 5:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container ,new BalconyFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}
