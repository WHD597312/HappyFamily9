package com.xr.happyFamily.bao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.ShopXQActivity;
import com.xr.happyFamily.bao.ShopXQActivity3;
import com.xr.happyFamily.bao.view.LazyViewPager;
import com.xr.happyFamily.bean.ShopBannerBean;
import com.xr.happyFamily.bean.ShopPageBean;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<ImageView> images;
    private ViewPager viewPager;
    private Context mContext;
    List<ShopBannerBean> shopBannerBeans = new ArrayList<>();

    /**
     * 构造方法，传入图片列表和ViewPager实例
     *
     * @param images
     * @param viewPager
     */

    public ViewPagerAdapter(Context context, List<ImageView> images, ViewPager viewPager, List<ShopBannerBean> shopBannerBeans) {
        mContext = context;
        this.shopBannerBeans = shopBannerBeans;
        this.images = images;
        this.viewPager = viewPager;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;//返回一个无限大的值，可以 无限循环
    }

    /**
     * 判断是否使用缓存, 如果返回的是true, 使用缓存. 不去调用instantiateItem方法创建一个新的对象
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 初始化一个条目
     *
     * @param container
     * @param position  当前需要加载条目的索引
     * @return
     */
    boolean isFirst = true;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 把position对应位置的ImageView添加到ViewPager中

        ImageView iv = images.get(position % images.size());
        ViewPager parent = (ViewPager) iv.getParent();
//        if (images.size() == 2) {
//            if (position % images.size() == 0) {
//                iv = new ImageView(mContext);
////                iv.setBackgroundResource(imageRess[i]);//设置图片
//                Picasso.with(mContext).load(shopBannerBeans.get(0).getImage()).into(iv);
//                iv.setId(imgae_ids[0]);//顺便给图片设置id
//                iv.setOnClickListener(new pagerImageOnClick());//设置图片点击事件
//                viewPager.addView(iv);
//            } else if (position % images.size() == 1) {
//                iv = new ImageView(mContext);
////                iv.setBackgroundResource(imageRess[i]);//设置图片
//                Picasso.with(mContext).load(shopBannerBeans.get(1).getImage()).into(iv);
//                iv.setId(imgae_ids[1]);//顺便给图片设置id
//                iv.setOnClickListener(new pagerImageOnClick());//设置图片点击事件
//                viewPager.addView(iv);
//            } else {
//                ImageView iv2 = new ImageView(mContext);
//                viewPager.addView(iv2);
//            }
//        } else {
            if(parent!=null){
                Log.e("qqqqqqqqqq",position+"??");
                iv = new ImageView(mContext);
//                iv.setBackgroundResource(imageRess[i]);//设置图片
                Picasso.with(mContext).load(shopBannerBeans.get(position % images.size()).getImage()).into(iv);
                iv.setId(imgae_ids[position % images.size()]);//顺便给图片设置id
                iv.setOnClickListener(new pagerImageOnClick());//设置图片点击事件
                viewPager.addView(iv);
            }
            else {
                iv = new ImageView(mContext);
//                iv.setBackgroundResource(imageRess[i]);//设置图片
                Picasso.with(mContext).load(shopBannerBeans.get(position % images.size()).getImage()).into(iv);
                iv.setId(imgae_ids[position % images.size()]);//顺便给图片设置id
                iv.setOnClickListener(new pagerImageOnClick());//设置图片点击事件
                viewPager.addView(iv);
            }


        return iv;

    }


    // 把当前添加ImageView返回回去.


    /**
     * 销毁一个条目
     * position 就是当前需要被销毁的条目的索引
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 把ImageView从ViewPager中移除掉
        viewPager.removeView(images.get(position % images.size()));

    }


    private int[] imgae_ids = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4,R.id.pager_image5,R.id.pager_image6,R.id.pager_image7,R.id.pager_image8,R.id.pager_image9,R.id.pager_image10};

    private class pagerImageOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            for(int i=0;i<imgae_ids.length;i++){
                if (v.getId()==imgae_ids[i]){
                    Intent intent  = new Intent(v.getContext(),ShopXQActivity3.class);
                    intent.putExtra("goodsId",shopBannerBeans.get(i).getGoodsId()+"");

                    Log.e("qqqqqqqqqqIIII",shopBannerBeans.get(i).getGoodsId()+"????");
                    mContext.startActivity(intent);
                }
            }
        }
    }
}