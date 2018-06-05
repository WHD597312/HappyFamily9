package com.xr.happyFamily.bao.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.donkingliang.labels.LabelsView;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopAddressActivity;
import com.xr.happyFamily.bao.ShopConfActivity;
import com.xr.happyFamily.bao.ShopSearchActivity;
import com.xr.happyFamily.bao.ShopSearchResultActivity;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bao.adapter.EvaluateAdapter;
import com.xr.happyFamily.bao.base.BaseFragment;
import com.xr.happyFamily.bao.view.FlowTagView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by TYQ on 2017/9/7.
 */

public class ShopFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    private static ShopXQActivity father;
    Context mContext;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.img_address)
    ImageView imgAddress;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_xinghao)
    TextView tvXinghao;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_shop_shop, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_close:
            case R.id.view_dis:
                mPopWindow.dismiss();
                break;
            default:
                break;
        }
    }


    @OnClick({R.id.img_address,  R.id.tv_xinghao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_address:
                startActivityForResult(new Intent(getActivity(), ShopAddressActivity.class), 101);
                break;
            case R.id.tv_xinghao:
                showPopup();
                break;
        }
    }

    /**
     * 所有的Activity对象的返回值都是由这个方法来接收 requestCode:
     * 表示的是启动一个Activity时传过去的requestCode值
     * resultCode：表示的是启动后的Activity回传值时的resultCode值
     * data：表示的是启动后的Activity回传过来的Intent对象
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 111) {
            String address = data.getStringExtra("address");// 拿到返回过来的地址
            // 把得到的数据显示到输入框内
            tvAddress.setText(address);

        }

    }


    private View contentViewSign,view_dis;
    private PopupWindow mPopWindow;
    private ImageView img_close;
    private EvaluateAdapter adapter_xinghao;
    private LabelsView labelsView;

    private void showPopup() {
        Log.e("Qqqqqqqqqqq","111111");
        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shop_xinghao, null);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        view_dis = contentViewSign.findViewById(R.id.view_dis);
        labelsView = (LabelsView) contentViewSign.findViewById(R.id.labels);
//        tv_shangcheng = (TextView) contentViewSign.findViewById(R.id.tv_shangcheng);
//        tv_shopcart.setOnClickListener(this);
        img_close.setOnClickListener(this);
        view_dis.setOnClickListener(this);
        mPopWindow = new PopupWindow(contentViewSign);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        mPopWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        mPopWindow.setFocusable(true);
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//        initView();
//        initData();
        setBiaoqian();
    }
    List<String> list;


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }

    int pos=0;
    private void setBiaoqian(){

        list = new ArrayList<>();
        list.add("Android");
        list.add("IOS");
        list.add("前端");
        list.add("后台");
        list.add("微信开发");
        list.add("游戏开发");
        labelsView.setLabels(list); //直接设置一个字符串数组就可以了。

//LabelsView可以设置任何类型的数据，而不仅仅是String。
//        ArrayList<TestBean> testList = new ArrayList<>();
//        testList.add(new TestBean("Android",1));
//        testList.add(new TestBean("IOS",2));
//        testList.add(new TestBean("前端",3));
//        testList.add(new TestBean("后台",4));
//        testList.add(new TestBean("微信开发",5));
//        testList.add(new TestBean("游戏开发",6));
//        labelsView.setLabels(testList, new LabelsView.LabelTextProvider<TestBean>() {
//            @Override
//            public CharSequence getLabelText(TextView label, int position, TestBean data) {
//                //根据data和position返回label需要显示的数据。
//                return data.getName();
//            }
//        });

        //标签的点击监听
        labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                //label是被点击的标签，data是标签所对应的数据，position是标签的位置。
                tvXinghao.setText(list.get(position));
                mPopWindow.dismiss();
                Log.e("Qqqqqqqqqqq","22222222222");
                pos=position;
            }
        });
//标签的选中监听
        labelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                //label是被选中的标签，data是标签所对应的数据，isSelect是是否选中，position是标签的位置。

            }
        });

        labelsView.setSelects(pos);
        Log.e("Qqqqqqqqqqq","3333,"+pos);
    }
}
