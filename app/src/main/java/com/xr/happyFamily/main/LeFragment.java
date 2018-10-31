package com.xr.happyFamily.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.xr.database.dao.daoimpl.LeImageDaoImpl;
import com.xr.database.dao.daoimpl.MsgDaoImpl;
import com.xr.happyFamily.R;
import com.xr.happyFamily.bao.pojo.LeImage;
import com.xr.happyFamily.le.BDmapActivity;
import com.xr.happyFamily.le.ClockActivity;
import com.xr.happyFamily.le.FriendActivity;
import com.xr.happyFamily.le.MapActivity;
import com.xr.happyFamily.le.UStats;
import com.xr.happyFamily.le.YouGuiActivity;
import com.xr.happyFamily.le.adapter.HappyFootAdapter;
import com.xr.happyFamily.le.adapter.HappyViewPagerAdapter;
import com.xr.happyFamily.le.bean.HappyBannerBean;
import com.xr.happyFamily.le.clock.MsgActivity;
import com.xr.happyFamily.le.view.YouguiDialog;
import com.xr.happyFamily.le.view.noBirthdayDialog;
import com.xr.happyFamily.login.login.LoginActivity;
import com.xr.happyFamily.together.MyDialog;
import com.xr.happyFamily.together.http.HttpUtils;
import com.xr.happyFamily.together.util.Utils;
import com.xr.happyFamily.zhen.PersonInfoActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class LeFragment extends Fragment {

    int lastBanner = 0;

    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.img4)
    ImageView img4;
    @BindView(R.id.lineLayout_dot)
    LinearLayout lineLayoutDot;
    @BindView(R.id.ll_xuyuan)
    LinearLayout llXuyuan;
    @BindView(R.id.ll_clock)
    LinearLayout llClock;
    @BindView(R.id.img5)
    ImageView img5;
    @BindView(R.id.img6)
    ImageView img6;
    @BindView(R.id.img7)
    ImageView img7;
    @BindView(R.id.img8)
    ImageView img8;
    @BindView(R.id.img9)
    ImageView img9;
    @BindView(R.id.img10)
    ImageView img10;

    public static final int MAIN_CODE = 1000;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img_friend)
    ImageView imgFriend;
    @BindView(R.id.img_msg)
    ImageView imgMsg;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.banner)
    Banner banner;
    private List<ImageView> mImageList;//轮播的图片集合
    private boolean isStop = false;//线程是否停止
    private static int PAGER_TIOME = 2500;//间隔时间
    // 在values文件假下创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    //轮播点
    private int[] imgae_dots = new int[]{R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5, R.id.img6, R.id.img7, R.id.img8, R.id.img9, R.id.img10};
    View view;
    Unbinder unbinder;
    //    private MyDialog dialog;
    SharedPreferences preferences;
    String userId;


    int page = 1;
    HappyFootAdapter happyFootAdapter;
    LeImageDaoImpl leImageDao;
    int derailPo = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_happy_mypage, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((ImageView) view.findViewById(imgae_dots[lastBanner])).setImageResource(R.mipmap.ic_shop_banner);
        happyBannerBeans = new ArrayList<>();
        footBean = new ArrayList<>();
        isStop = false;
        running = true;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        happyFootAdapter = new HappyFootAdapter(getActivity(), footBean);
        recyclerView.setAdapter(happyFootAdapter);
        isFinish = false;
        page = 1;
        preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
        long msgTime = preferences.getLong("msgTime", 0);
        derailPo = preferences.getInt("derailPo", -1);
        Log.e("youguires111", "onViewClicked: -->" + derailPo);
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        getDataBase();
        Log.e("qqqqqqIIIII2222", "??????");
        return view;
    }

    String birthday;

    boolean running = false;

    private void getDataBase() {

        leImageDao = new LeImageDaoImpl(getActivity());
        List<LeImage> leImageList = leImageDao.findAll();
        if (leImageList.size() > 0) {
            try {
                for (int i = 0; i < leImageList.size(); i++) {
                    Log.e("qqqqqqAAA111111", leImageList.get(i).getData());
                    JSONObject jsonObject = new JSONObject(leImageList.get(i).getData());
                    JsonObject content = new JsonParser().parse(jsonObject.toString()).getAsJsonObject();
                    if (jsonObject.get("returnCode").toString() != "null") {
                        JsonArray list = content.getAsJsonArray("returnData");
                        Gson gson = new Gson();
                        for (int j = 0; j < list.size(); j++) {
////                        //通过反射 得到UserBean.class
                            JsonElement user = list.get(j);
                            HappyBannerBean userList = gson.fromJson(user, HappyBannerBean.class);
                            if (userList.getSite().equals("head"))
                                happyBannerBeans.add(userList);
                            else
                                footBean.add(userList);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

            happyFootAdapter.notifyDataSetChanged();
            List<String> imgList = new ArrayList<>();
            for (int i = 0; i < happyBannerBeans.size(); i++)
                imgList.add(happyBannerBeans.get(i).getPicUrl());
            setBanner(imgList);


        } else {
            resultList = new ArrayList<>();
            getBanner("head", page);
            getBanner("foot", 1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        running = true;
        banner.startAutoPlay();
        preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
        birthday = preferences.getString("birthday", "");
        userId = preferences.getString("userId", "");
        long msgTime = preferences.getLong("msgTime", 0);
        MsgDaoImpl msgDao = new MsgDaoImpl(getActivity());
        int msgNum = msgDao.findNumbByTime(msgTime);
        if (msgNum == 0) {
            tvNum.setVisibility(View.GONE);
        } else
            tvNum.setText(msgNum + "");
//        new getClocksByUserId().execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        running = false;
        banner.stopAutoPlay();
        if (getAdByPageAsync != null && !getAdByPageAsync.isCancelled() && getAdByPageAsync.getStatus() == AsyncTask.Status.RUNNING) {
            getAdByPageAsync.cancel(true);
            getAdByPageAsync = null;
        }
    }

    private void setBanner(final List<String> imageList) {
        if (banner != null) {
            for (int i = 0; i < imageList.size(); i++) {
                view.findViewById(imgae_dots[i]).setVisibility(View.VISIBLE);
            }
            //设置图片加载器，图片加载器在下方
            banner.setImageLoader(new MyLoader());
            banner.setBannerAnimation(Transformer.Default);
            banner.setDelayTime(2500);
            //设置图片网址或地址的集合
            banner.setImages(imageList);
            banner.isAutoPlay(true);
            //设置指示器的位置，小点点，左中右。
            banner.setIndicatorGravity(BannerConfig.CENTER);

            banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < imageList.size(); i++) {
                        if (position == i + 1) {
                            ((ImageView) view.findViewById(imgae_dots[i])).setImageResource(R.mipmap.ic_shop_banner);
                        } else {
                            ((ImageView) view.findViewById(imgae_dots[i])).setImageResource(R.mipmap.ic_shop_banner_wei);
                        }
                        if (position > imageList.size()) {
                            ((ImageView) view.findViewById(imgae_dots[0])).setImageResource(R.mipmap.ic_shop_banner);
                        }
                        if (position == 0) {
                            ((ImageView) view.findViewById(imgae_dots[imageList.size() - 1])).setImageResource(R.mipmap.ic_shop_banner);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            banner.start();
        }
    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            if (running)
                Glide.with(context).load((String) path).into(imageView);
        }
    }

    YouguiDialog youguiDialog;

    private void clolkDialog2() {
        youguiDialog = new YouguiDialog(getActivity());


        youguiDialog.setOnPositiveClickListener(new YouguiDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {

                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);

                youguiDialog.dismiss();
            }
        });

        youguiDialog.setCanceledOnTouchOutside(false);

        youguiDialog.show();

    }

    noBirthdayDialog birthdaydialog;

    private void clolkDialog1() {
        birthdaydialog = new noBirthdayDialog(getActivity());

        birthdaydialog.setOnNegativeClickListener(new noBirthdayDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {

                birthdaydialog.dismiss();
            }
        });
        birthdaydialog.setOnPositiveClickListener(new noBirthdayDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
                startActivity(intent);
                birthdaydialog.dismiss();
            }
        });

        birthdaydialog.setCanceledOnTouchOutside(false);

        birthdaydialog.show();

    }

    @OnClick({R.id.ll_yougui, R.id.ll_clock, R.id.img_msg, R.id.img_friend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_clock:


                if ("".equals(birthday)) {
                    clolkDialog1();

                } else {
                    Intent intent = new Intent(getActivity(), ClockActivity.class);
                    intent.putExtra("type", "asd");
                    startActivity(intent);
                }
                break;
            case R.id.ll_yougui:
//                startActivity(new Intent(getActivity(), BDmapActivity.class));
                Log.e("youguires", "onViewClicked: -->" + derailPo);
                if (UStats.getUsageStatsList(getActivity()).isEmpty()) {
                    clolkDialog2();

                } else {

                    if (derailPo == 0) {
                        startActivity(new Intent(getActivity(), YouGuiActivity.class));
                    } else if (derailPo == 1) {
                        startActivity(new Intent(getActivity(), BDmapActivity.class));
                    } else if (derailPo == 2) {
                        startActivity(new Intent(getActivity(), MapActivity.class));
                    }
                }
                break;
            case R.id.img_msg:
                getActivity().startActivity(new Intent(getActivity(), MsgActivity.class));
                break;
            case R.id.img_friend:
                Intent intent = new Intent(getActivity(), FriendActivity.class);
                intent.putExtra("type", 1000);
                getActivity().startActivity(intent);
                break;
        }
    }


    List<HappyBannerBean> happyBannerBeans;
    List<HappyBannerBean> footBean;

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    boolean isFinish = false;
    boolean isFootFish = false;
    List<String> resultList;

    class getAdByPageAsync extends AsyncTask<Map<String, Object>, Void, String> {
        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            if (isCancelled()) {
                return null;
            }
            Map<String, Object> params = maps[0];
            String url = "/happy/happy/getHappy";
            String site = params.get("site").toString();
            String page = params.get("page").toString();
            url = url + "?site=" + site + "&page=" + page;
            String result = HttpUtils.doGet(getActivity(), url);
            String code = "";
            Log.e("qqqqqqqqMMM", url);
            if (page.equals("1") && site.equals("head"))
                happyBannerBeans.clear();
            if (page.equals("1") && site.equals("foot")) {
                footBean.clear();
                isFootFish = true;
            }
            try {
                if (!Utils.isEmpty(result)) {
                    if (result.length() < 6) {
                        code = result;
                    } else {
                        LeImage leImage = new LeImage();
                        leImage.setData(result);
                        resultList.add(result);
                        Log.e("qqqqqqqqMMM22", result);
                        JSONObject jsonObject = new JSONObject(result);
                        code = jsonObject.getString("returnCode");
                        JsonObject content = new JsonParser().parse(jsonObject.toString()).getAsJsonObject();
                        if (jsonObject.get("returnCode").toString() != "null") {
                            JsonArray list = content.getAsJsonArray("returnData");
                            Gson gson = new Gson();
                            if (list.size() == 0)
                                isFinish = true;
                            Log.e("qqqqqTTTT", list.size() + "?" + isFinish);
                            for (int i = 0; i < list.size(); i++) {
////                        //通过反射 得到UserBean.class
                                JsonElement user = list.get(i);
                                HappyBannerBean userList = gson.fromJson(user, HappyBannerBean.class);
                                if (userList.getSite().equals("head"))
                                    happyBannerBeans.add(userList);
                                else
                                    footBean.add(userList);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (isCancelled()) {
                return;
            }
            if (!Utils.isEmpty(s) && "100".equals(s)) {
                if (isFinish && running) {
//                    if (dialog.isShowing())
//                    MyDialog.closeDialog(dialog);
                    if (isFootFish) {
                        leImageDao.deleteAll();
                        for (int i = 0; i < resultList.size(); i++) {
                            if (resultList.get(i).contains("picUrl")) {
                                LeImage leImage = new LeImage();
                                leImage.setData(resultList.get(i));
                                leImageDao.insert(leImage);
                            }
                        }
                    }
                    happyFootAdapter.notifyDataSetChanged();
                    List<String> imgList = new ArrayList<>();
                    for (int i = 0; i < happyBannerBeans.size(); i++)
                        imgList.add(happyBannerBeans.get(i).getPicUrl());
                    setBanner(imgList);

                } else {
                    page++;
                    getBanner("head", page);
                }
            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
                Toast.makeText(getActivity().getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences;
                preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
                MyDialog.setStart(false);
                if (preferences.contains("password")) {
                    preferences.edit().remove("password").commit();
                }
                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
            }
        }
    }

//
//    class getClocksByUserId extends AsyncTask<Map<String, Object>, Void, String> {
//        @Override
//        protected String doInBackground(Map<String, Object>... maps) {
//
//
//            String url = "/happy/clock/getClocksByUserId";
//            url = url + "?userId=" + userId;
//            String result = HttpUtils.doGet(getActivity(), url);
//            Log.e("qqqqqqqqRRR", userId + "?" + result);
//            String code = "";
//            try {
//                if (!Utils.isEmpty(result)) {
//                    if (result.length() < 6) {
//                        code = result;
//                    }
//                    JSONObject jsonObject = new JSONObject(result);
//                    code = jsonObject.getString("returnCode");
//
//                    //开始
//                    JSONObject returnData1 = jsonObject.getJSONObject("returnData");
//                    String birthday = returnData1.getString("birthday");
//                    //结束
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return code;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            if (!Utils.isEmpty(s) && "100".equals(s)) {
//
//            } else if (!Utils.isEmpty(s) && "401".equals(s)) {
//                Toast.makeText(getActivity().getApplicationContext(), "用户信息超时请重新登陆", Toast.LENGTH_SHORT).show();
//                SharedPreferences preferences;
//                preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
//                MyDialog.setStart(false);
//                if (preferences.contains("password")) {
//                    preferences.edit().remove("password").commit();
//                }
//                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
//            }
//        }
//    }

    getAdByPageAsync getAdByPageAsync;

    public void getBanner(String site, int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("site", site);
        params.put("page", page);
        getAdByPageAsync = new getAdByPageAsync();
        getAdByPageAsync.execute(params);
    }


}
