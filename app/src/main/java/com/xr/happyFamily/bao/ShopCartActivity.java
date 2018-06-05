package com.xr.happyFamily.bao;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.adapter.ShopCartAdapter;
import com.xr.happyFamily.bean.ShopCartBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


//购物车界面

public class ShopCartActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_rightText)
    TextView titleRightText;
    @BindView(R.id.tv_sum)
    TextView tvSum;

    private TextView tvShopCartSubmit, tvShopCartSelect;
    private Context mContext;
    private RecyclerView rlvShopCart, rlvHotProducts;
    private ShopCartAdapter mShopCartAdapter;
    private LinearLayout llPay;
    private RelativeLayout rlHaveProduct;
    private List<ShopCartBean> mAllOrderList = new ArrayList<>();
    private ArrayList<ShopCartBean> mGoPayList = new ArrayList<>();
    private List<String> mHotProductsList = new ArrayList<>();
    private TextView tvShopCartTotalPrice;
    private int mCount, mPosition;
    private float mTotalPrice1;
    private boolean mSelect, isEdit = false;
    private boolean[] isChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shopcart);
        mContext = ShopCartActivity.this;
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        titleText.setText("购物车");
        tvShopCartSelect = (TextView) findViewById(R.id.tv_shopcart_addselect);
        tvShopCartTotalPrice = (TextView) findViewById(R.id.tv_shopcart_totalprice);
        rlHaveProduct = (RelativeLayout) findViewById(R.id.rl_shopcart_have);
        rlvShopCart = (RecyclerView) findViewById(R.id.rlv_shopcart);
        llPay = (LinearLayout) findViewById(R.id.ll_pay);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        llPay.setLayoutParams(lp);

        tvShopCartSubmit = (TextView) findViewById(R.id.tv_shopcart_submit);

        rlvShopCart.setLayoutManager(new LinearLayoutManager(this));
        mShopCartAdapter = new ShopCartAdapter(this, mAllOrderList);
        rlvShopCart.setAdapter(mShopCartAdapter);
        //删除商品接口
        mShopCartAdapter.setOnDeleteClickListener(new ShopCartAdapter.OnDeleteClickListener() {


            @Override
            public void onDeleteClick(View view, int position, int cartid) {

            }
        });
        //修改数量接口
        mShopCartAdapter.setOnEditClickListener(new ShopCartAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(int position, int cartid, int count) {
                mCount = count;
                mPosition = position;
                Log.e("qqqqqqq1111", count + "," + position);
            }
        });
        mShopCartAdapter.setOnItemClickListener(onItemOnClickListener);
        //实时监控全选按钮
        mShopCartAdapter.setResfreshListener(new ShopCartAdapter.OnResfreshListener() {
            @Override
            public void onResfresh(boolean isSelect) {
                mSelect = isSelect;
                if (isSelect) {
                    Drawable left = ContextCompat.getDrawable(mContext, R.mipmap.xuanzhong_shop3x);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                } else {
                    Drawable left = ContextCompat.getDrawable(mContext, R.mipmap.weixuanzhong3x);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                }
                float mTotalPrice = 0;
                int mTotalNum = 0;
                mTotalPrice1 = 0;
                mGoPayList.clear();
                for (int i = 0; i < mAllOrderList.size(); i++)
                    if (mAllOrderList.get(i).getIsSelect()) {
                        mTotalPrice += Float.parseFloat(mAllOrderList.get(i).getPrice()) * mAllOrderList.get(i).getCount();
                        mTotalNum += 1;
                        mGoPayList.add(mAllOrderList.get(i));
                    }
                mTotalPrice1 = mTotalPrice;
                tvShopCartTotalPrice.setText(mTotalPrice + "");
            }
        });

        //全选
        tvShopCartSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelect = !mSelect;
                if (mSelect) {
                    Drawable left = ContextCompat.getDrawable(mContext, R.mipmap.xuanzhong_shop3x);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                    for (int i = 0; i < mAllOrderList.size(); i++) {
                        mAllOrderList.get(i).setSelect(true);
                        mAllOrderList.get(i).setShopSelect(true);
                    }
                } else {
                    Drawable left = ContextCompat.getDrawable(mContext, R.mipmap.weixuanzhong3x);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                    for (int i = 0; i < mAllOrderList.size(); i++) {
                        mAllOrderList.get(i).setSelect(false);
                        mAllOrderList.get(i).setShopSelect(false);
                    }
                }
                mShopCartAdapter.notifyDataSetChanged();

            }
        });

        initData();
        mShopCartAdapter.notifyDataSetChanged();
    }

    ShopCartAdapter.OnItemOnClickListener onItemOnClickListener = new ShopCartAdapter.OnItemOnClickListener() {
        @Override
        public void onItemOnClick(View view, int pos) {
            Toast.makeText(ShopCartActivity.this, "This is" + pos, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ShopCartActivity.this, ShopXQActivity.class));
        }

        @Override
        public void onItemLongOnClick(View view, int pos) {

            mPosition = pos;
            Toast.makeText(ShopCartActivity.this, "点击" + pos, Toast.LENGTH_SHORT).show();

        }
    };

    private void initData() {
        for (int i = 0; i < 3; i++) {
            ShopCartBean sb = new ShopCartBean();
            sb.setShopId(1);
            sb.setPrice(500 + i * 100 + "");
            sb.setDefaultPic("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512381167376&di=8eee5d9fe0b459c16fd8e1d855725375&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F5882b2b7d0a20cf47d9ed00d7c094b36adaf99d1.jpg");

            sb.setProductName("电暖器 银色" + i);
//            sb.setShopName("狼牙龙珠");
            sb.setDetails("对流式加热，通电即热，无感静音，双重安装保护3333333333333333333333");
            sb.setCount(2);
            mAllOrderList.add(sb);
        }


    }

    public static void isSelectFirst(List<ShopCartBean> list) {
        if (list.size() > 0) {
            list.get(0).setIsFirst(1);
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).getShopId() == list.get(i - 1).getShopId()) {
                    list.get(i).setIsFirst(2);
                } else {
                    list.get(i).setIsFirst(1);
                }
            }
        }

    }


    @OnClick({R.id.back, R.id.title_rightText,R.id.tv_shopcart_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title_rightText:
                upDataUI();
                break;
            case R.id.tv_shopcart_submit:
                String s = "";
                if (!isEdit) {
                    s = "选中支付";
                    for (int i = 0; i < mAllOrderList.size(); i++) {
                        if (mAllOrderList.get(i).getIsSelect()) {
                            s = s + i + ",";
                        }
                    }
                    Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ShopCartActivity.this, ShopConfActivity.class));
                } else{ s = "删除";
                    List<ShopCartBean> sign=new ArrayList<>();
                    for (int i = 0; i < mAllOrderList.size(); i++) {
                        if (mAllOrderList.get(i).getIsSelect()) {
                            s = s + i + ",";
                        }
                        else {
                            sign.add(mAllOrderList.get(i));
                        }
                    }
                    Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
                    mAllOrderList.clear();
                    mAllOrderList.addAll(sign);
                    mShopCartAdapter.notifyDataSetChanged();
                    }


                break;
        }
    }

    public void upDataUI() {
        if (!isEdit) {
            titleRightText.setText("完成");
            tvShopCartSubmit.setText("删除");
            tvSum.setVisibility(View.GONE);
            tvShopCartTotalPrice.setVisibility(View.GONE);
            isEdit=true;
        }else {
            titleRightText.setText("编辑");
            tvShopCartSubmit.setText("狠心买");
            tvSum.setVisibility(View.VISIBLE);
            tvShopCartTotalPrice.setVisibility(View.VISIBLE);
            isEdit=false;
        }
    }
}
