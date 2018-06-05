package com.xr.happyFamily.bao;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.CityAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2018/5/22.
 */

public class ShopAddAddressActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.img_choose)
    ImageView imgChoose;
    @BindView(R.id.tv_choose)
    TextView tvChoose;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_tel)
    EditText edTel;
    @BindView(R.id.ll_moren)
    LinearLayout llMoren;
    private View contentViewSign;
    private PopupWindow mPopWindow;
    private Context mContext;
    private Boolean isMoren=true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mContext = ShopAddAddressActivity.this;
        setContentView(R.layout.activity_shop_add_address);
        ButterKnife.bind(this);

        titleText.setText("添加地址");
        titleRightText.setText("保存");


    }


    @OnClick({R.id.back, R.id.ll_moren, R.id.tv_choose,R.id.title_rightText})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_moren:
                if(isMoren){
                    isMoren=false;
                    imgChoose.setImageResource(R.mipmap.weixuanzhong3x);
                }else {
                    isMoren=true;
                    imgChoose.setImageResource(R.mipmap.xuanzhong3x);
                }
                break;
            case R.id.tv_choose:

                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        showPopup();
                    }
                }, 300);

                break;
            case R.id.title_rightText:
                if (view != null) {

                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_dis:
            case R.id.img_close:
                mPopWindow.dismiss();
                break;
            case R.id.rl_sheng:
                upData(0, "省份");
                break;
            case R.id.rl_shi:
                upData(1, "城市");
                break;
            case R.id.rl_qu:
                upData(2, "区域");
                break;

        }
    }

    private RecyclerView recyclerview;
    private ImageView img_close;
    private View view_dis;
    private ListView listCity;
    private List<String> data = new ArrayList<>();
    private CityAdapter cityAdapter;
    private TextView tv_sheng, tv_shi, tv_qu;
    private RelativeLayout rl_sheng, rl_shi, rl_qu;
    private ImageView img_sheng, img_shi, img_qu;
    private ImageView[] img_city;
    private int sing_city = 0;

    private void showPopup() {

        contentViewSign = LayoutInflater.from(mContext).inflate(R.layout.popup_shop_city, null);
        img_close = (ImageView) contentViewSign.findViewById(R.id.img_close);
        view_dis = contentViewSign.findViewById(R.id.view_dis);
        listCity = (ListView) contentViewSign.findViewById(R.id.list_city);
        tv_sheng = (TextView) contentViewSign.findViewById(R.id.tv_sheng);
        tv_shi = (TextView) contentViewSign.findViewById(R.id.tv_shi);
        tv_qu = (TextView) contentViewSign.findViewById(R.id.tv_qu);
        rl_sheng = (RelativeLayout) contentViewSign.findViewById(R.id.rl_sheng);
        rl_shi = (RelativeLayout) contentViewSign.findViewById(R.id.rl_shi);
        rl_qu = (RelativeLayout) contentViewSign.findViewById(R.id.rl_qu);
        img_sheng = (ImageView) contentViewSign.findViewById(R.id.img_sheng);
        img_shi = (ImageView) contentViewSign.findViewById(R.id.img_shi);
        img_qu = (ImageView) contentViewSign.findViewById(R.id.img_qu);
        img_city = new ImageView[]{img_sheng, img_shi, img_qu};
        img_close.setOnClickListener(this);
        view_dis.setOnClickListener(this);
        rl_sheng.setOnClickListener(this);
        rl_shi.setOnClickListener(this);
        rl_qu.setOnClickListener(this);

        data = initData("省份");

        cityAdapter = new CityAdapter(data, this);
        listCity.setAdapter(cityAdapter);
        listCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (sing_city) {
                    case 0:
                        tv_sheng.setText(data.get(position));
                        upData(1, "城市");
                        break;
                    case 1:
                        tv_shi.setText(data.get(position));
                        upData(2, "区域");
                        break;
                    case 2:
                        tv_qu.setText(data.get(position));
                        mPopWindow.dismiss();
                        tvAddress.setText(tv_sheng.getText() + " " + tv_shi.getText() + " " + tv_qu.getText());
                        break;


                }

            }

        });

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
        mPopWindow.setClippingEnabled(false);
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAsDropDown(findViewById(R.id.view_line));
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }

    }

    protected ArrayList<String> initData(String s) {
        ArrayList<String> mDatas = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            mDatas.add("A    " + s + i);
        }
        return mDatas;
    }

    private void upData(int i, String title) {
        img_city[sing_city].setVisibility(View.INVISIBLE);
        sing_city = i;
        img_city[sing_city].setVisibility(View.VISIBLE);
        data.clear();
        data.addAll(initData(title));
        cityAdapter.notifyDataSetChanged();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==3){
               showPopup();
            }
        }
    };

}
