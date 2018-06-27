package com.xr.happyFamily.together.http;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xr.happyFamily.jia.MyApplication;
import com.xr.happyFamily.login.login.LoginActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import com.xr.happyFamily.jia.MyApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;


import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by whd on 2017/12/23.
 */

public class HttpUtils {

    public static String ipAddress="http://47.98.131.11:8084";
    public static String getInputStream(InputStream is) {
        String result = null;
        byte[] buffer = new byte[1024 * 10];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            result = new String(bos.toByteArray(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

//    public static String headerPostOkHpptRequest(Context context,String url, Map<String, Object> map) {
//        String result=null;
//        Context mContext=context;
//        try{
//            String CONTENT_TYPE = "application/json";
//
//            String JSON_DATA = "{\n" +
//                    "\"deviceId\":1129,\n" +
//                    "\"deviceTimeControlDtos\":\n" +
//                    "[\n" +
//                    "{\n" +
//                    "\"week\":2,\n" +
//                    "\"deviceTimeControlList\":[\n" +
//                    "         {\"temp\":2.0,\"openTime\":2,\"closeTime\":3},\n" +
//                    "         {\"temp\":2.0,\"openTime\":4,\"closeTime\":5}\n" +
//                    "     ]\n" +
//                    "}";
//            JSONObject jsonObject=new JSONObject();
//            for (Map.Entry<String,Object> param:map.entrySet()){
//                jsonObject.put(param.getKey(),param.getValue());
//            }
//
//
//
//            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE),jsonObject.toJSONString());
//
//            SharedPreferences userSettings= mContext.getSharedPreferences("my", 0);
//
//            String token = userSettings.getString("token","token");
//            Request request = new Request.Builder()
//                    .addHeader("client","android-xr")
//                    .addHeader("Content-Type","application/json")
//                    .addHeader("authorization",token)
//                    .url(url)
//                    .post(requestBody)
//                    .build();
//
//            OkHttpClient okHttpClient=new OkHttpClient();
//            Response response=okHttpClient.newCall(request).execute();
//
//            if(response.isSuccessful()){
//                result= response.body().string();
//                Log.e("qqqqqqqq???",result);
//            }
//
//            String code="0";
//            org.json.JSONObject jsonObject1 = new org.json.JSONObject(result);
//            code = jsonObject1.getString("returnCode");
//            if(!code.equals("100")){
//                Toast.makeText(mContext,jsonObject1.getString("returnMsg"),Toast.LENGTH_SHORT).show();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return result;
//    }

//    public static String myPostOkHpptRequest(Context context,String url, Map<String, Object> map) {
//        String result=null;
//        Context mContext=context;
//        try{
//            String CONTENT_TYPE = "application/json";
//            JSONObject jsonObject=new JSONObject();
//            for (Map.Entry<String,Object> param:map.entrySet()){
//                jsonObject.put(param.getKey(),param.getValue());
//            }
//
//
//            Log.e("qqqqqqqqqqLLLL",jsonObject.toString()+"???????");
//
//            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE),jsonObject.toJSONString());
//
//            Request request = new Request.Builder()
//                    .addHeader("client","android-xr")
//                    .url(url)
//                    .post(requestBody)
//                    .build();
//
//            OkHttpClient okHttpClient=new OkHttpClient();
//            Response response=okHttpClient.newCall(request).execute();
//
//
//            if(response.isSuccessful()){
//                result= response.body().string();
//                Log.e("qqqqqqqq???",result);
//            }
//
//            String code="0";
//            org.json.JSONObject jsonObject1 = new org.json.JSONObject(result);
//            code = jsonObject1.getString("returnCode");
//            if(!code.equals("100")){
//                Toast.makeText(mContext,jsonObject1.getString("returnMsg"),Toast.LENGTH_SHORT).show();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return result;
//    }



    static String httpUrl="http://47.98.131.11:8084/";


    public static String headerPostOkHpptRequest(Context context, String url, Map<String, Object> map) {
        String result = null;
        Context mContext = context;
        try {
            String CONTENT_TYPE = "application/json";

            String JSON_DATA = "{\n" +
                    "\"deviceId\":1129,\n" +
                    "\"deviceTimeControlDtos\":\n" +
                    "[\n" +
                    "{\n" +
                    "\"week\":2,\n" +
                    "\"deviceTimeControlList\":[\n" +
                    "         {\"temp\":2.0,\"openTime\":2,\"closeTime\":3},\n" +
                    "         {\"temp\":2.0,\"openTime\":4,\"closeTime\":5}\n" +
                    "     ]\n" +
                    "}";
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, Object> param : map.entrySet()) {
                jsonObject.put(param.getKey(), param.getValue());
            }
            Log.e(context.toString()+"--put---"+url,jsonObject.toString());

            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), jsonObject.toJSONString());

            SharedPreferences userSettings = mContext.getSharedPreferences("login", 0);
            String token = userSettings.getString("token", "token");

            Request request = new Request.Builder()
                    .addHeader("client", "android-xr")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("authorization", token)
                    .url(httpUrl+url)
                    .post(requestBody)
                    .build();

            OkHttpClient okHttpClient = null;
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(TimeOut, TimeUnit.SECONDS)
                        .connectTimeout(TimeOut, TimeUnit.SECONDS)
                        .writeTimeout(TimeOut, TimeUnit.SECONDS)
                        .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)//添加自定义缓存拦截器（后面讲解），注意这里需要使用.addNetworkInterceptor
                        .build();
            }
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                result = response.body().string();
                Log.e(context.toString()+"       --get---"+url, result);
            }else {
                NetWorkUtil.showNoNetWorkDlg(MyApplication.getContext());
                Log.e("qqqqqqqqqqqXXXXX","???????");
            }

            String code = "0";
            org.json.JSONObject jsonObject1 = new org.json.JSONObject(result);
            code = jsonObject1.getString("returnCode");
            if (!code.equals("100")) {
                Toast.makeText(mContext,url+":"+ jsonObject1.getString("returnMsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    private static int TimeOut = 5;

    public static String doGet(Context context, String url) {
        Context mContext = context;
        String result = null;
        OkHttpClient okHttp = null;
        if (okHttp == null) {
            okHttp = new OkHttpClient.Builder()
                    .readTimeout(TimeOut, TimeUnit.SECONDS)
                    .connectTimeout(TimeOut, TimeUnit.SECONDS)
                    .writeTimeout(TimeOut, TimeUnit.SECONDS)
                    .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)//添加自定义缓存拦截器（后面讲解），注意这里需要使用.addNetworkInterceptor
                    .build();
        }
        try {
            SharedPreferences userSettings = mContext.getSharedPreferences("my", 0);
            String token = userSettings.getString("token", "token");
            Log.e("qqqqqqHHHHH222222222",httpUrl+url);
            url=httpUrl+url;
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("authorization", token)
                    .get()
                    .build();

            Response response = okHttp.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
                Log.e(context.toString(), result);
            }else {
                Log.e("qqqqqqqqqqqXXXXX","???????");
            }

            String code = "0";
            org.json.JSONObject jsonObject1 = new org.json.JSONObject(result);
            code = jsonObject1.getString("returnCode");
            if (!code.equals("100")) {
                Toast.makeText(mContext, url+":"+jsonObject1.getString("returnMsg"), Toast.LENGTH_SHORT).show();
            }
            if (code.equals("10005")) {
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * 自定义callback
     */
    static class MyCallBack implements Callback {
        private OkHttpCallBack okHttpCallBack;

        public MyCallBack(OkHttpCallBack okHttpCallBack) {
            this.okHttpCallBack = okHttpCallBack;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            okHttpCallBack.onFailure(e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            okHttpCallBack.onSuccess(response);
        }
    }

    /**
     * 接口方法
     */
    public interface OkHttpCallBack {
        void onFailure(IOException e);

        void onSuccess(Response response);
    }

    public static String myPostOkHpptRequest(Context context, String url, Map<String, Object> map) {
        String result = null;
        Context mContext = context;
        try {
            String CONTENT_TYPE = "application/json";

            String JSON_DATA = "{\n" +
                    "\"deviceId\":1129,\n" +
                    "\"deviceTimeControlDtos\":\n" +
                    "[\n" +
                    "{\n" +
                    "\"week\":2,\n" +
                    "\"deviceTimeControlList\":[\n" +
                    "         {\"temp\":2.0,\"openTime\":2,\"closeTime\":3},\n" +
                    "         {\"temp\":2.0,\"openTime\":4,\"closeTime\":5}\n" +
                    "     ]\n" +
                    "}";
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, Object> param : map.entrySet()) {
                jsonObject.put(param.getKey(), param.getValue());
            }


            Log.e("qqqqqqqqqHHH",jsonObject.toString());

            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), jsonObject.toJSONString());

            Request request = new Request.Builder()
                    .addHeader("client", "android-xr")
                    .url(httpUrl+url)
                    .post(requestBody)
                    .build();

            OkHttpClient okHttpClient = null;
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(TimeOut, TimeUnit.SECONDS)
                        .connectTimeout(TimeOut, TimeUnit.SECONDS)
                        .writeTimeout(TimeOut, TimeUnit.SECONDS)
                        .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)//添加自定义缓存拦截器（后面讲解），注意这里需要使用.addNetworkInterceptor
                        .build();
            }
            Response response = okHttpClient.newCall(request).execute();


            if (response.isSuccessful()) {
                result = response.body().string();
                Log.e("qqqqqqqq???", result);
            }

            String code = "0";
            org.json.JSONObject jsonObject1 = new org.json.JSONObject(result);
            code = jsonObject1.getString("returnCode");
            if (!code.equals("100")) {
                Toast.makeText(mContext, jsonObject1.getString("returnMsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String postOkHpptRequest(String url, Map<String, Object> map) {
        String result = null;
        try {
            String CONTENT_TYPE = "application/json";

            String JSON_DATA = "{\n" +
                    "\"deviceId\":1129,\n" +
                    "\"deviceTimeControlDtos\":\n" +
                    "[\n" +
                    "{\n" +
                    "\"week\":2,\n" +
                    "\"deviceTimeControlList\":[\n" +
                    "         {\"temp\":2.0,\"openTime\":2,\"closeTime\":3},\n" +
                    "         {\"temp\":2.0,\"openTime\":4,\"closeTime\":5}\n" +
                    "     ]\n" +
                    "}";
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, Object> param : map.entrySet()) {
                jsonObject.put(param.getKey(), param.getValue());
            }


            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), jsonObject.toJSONString());

            Request request = new Request.Builder()
                    .addHeader("client","android-xr")
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                result = response.body().string();
            }

        }catch (Exception e){

        }
        return result;
    }



    static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            //网上很多示例代码都对在request请求前对其进行无网的判断，其实无需判断，无网自动访问缓存
//            if(!NetworkUtil.getInstance().isConnected()){
//                request = request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)//只访问缓存
//                        .build();
//            }
            Response response = chain.proceed(request);

            if (NetWorkUtil.isConn(MyApplication.getContext())) {
                int maxAge = 0;//缓存失效时间，单位为秒
                return response.newBuilder()
                        .removeHeader("Pragma")//清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                NetWorkUtil.showNoNetWorkDlg(MyApplication.getContext());
                //这段代码设置无效
//                int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
//                return response.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                        .removeHeader("Pragma")
//                        .build();
            }
            return response;
        }
    };
    private static GetBuilder getBuilder;
    public static GetBuilder getBuilder(){
        if (getBuilder == null){
            getBuilder = OkHttpUtils.get();
        }
        return getBuilder;
    }















    public static String postOkHpptRequest2(String url, JSONArray jsonArray) {
        String result = null;
        try {
            String CONTENT_TYPE = "application/json";

            String JSON_DATA = "{\n" +
                    "    \"houseId\":1000,\n" +
                    "    \"controlledId\":[5,6]\n" +
                    "}";

//            for (Map.Entry<String,Object> param:map.entrySet()){
//                jsonObject.put(param.getKey(),param.getValue());
//            }

            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), jsonArray.toString());

            Request request = new Request.Builder()
                    .addHeader("client", "android-xr")
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.connectTimeoutMillis();
            okHttpClient.readTimeoutMillis();
            okHttpClient.writeTimeoutMillis();
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                result = response.body().string();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String postOkHpptRequest3(String url, org.json.JSONObject jsonObject) {
        String result = null;
        try {
            String CONTENT_TYPE = "application/json";

            String JSON_DATA = "{\n" +
                    "\"deviceId\":1129,\n" +
                    "\"deviceTimeControlDtos\":\n" +
                    "[\n" +
                    "{\n" +
                    "\"week\":2,\n" +
                    "\"deviceTimeControlList\":[\n" +
                    "         {\"temp\":2.0,\"openTime\":2,\"closeTime\":3},\n" +
                    "         {\"temp\":2.0,\"openTime\":4,\"closeTime\":5}\n" +
                    "     ]\n" +
                    "}";


            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), jsonObject.toString());

            Request request = new Request.Builder()
                    .addHeader("client", "android-xr")
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String doDelete(String url, JSONArray jsonArray) {
        String result = null;
        try {
            String CONTENT_TYPE = "application/json";


            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), jsonArray.toString());
            url=httpUrl+url;
            Request request = new Request.Builder()
                    .url(url)
                    .delete(requestBody)
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getOkHpptRequest(String url) {
//        File httpCacheDirectory = new File(MyApplication.getContext().getCacheDir(), "HttpCache");//这里为了方便直接把文件放在了SD卡根目录的HttpCache中，一般放在context.getCacheDir()中
//        int cacheSize = 10 * 1024 * 1024;//设置缓存文件大小为10M
//        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        String result=null;
        try{
            SharedPreferences my=MyApplication.getContext().getSharedPreferences("my",Context.MODE_PRIVATE);
//            SharedPreferences userSettings= ge6getSharedPreferences("login", 0);
            String token =my.getString("token","");
            JSONObject jsonObject = new JSONObject();
            Request request = new Request.Builder()
                    .addHeader("authorization",token)
                    .url(url)
                    .get()
                    .tag(1)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时
                    .readTimeout(5, TimeUnit.SECONDS)//读取超时
                    .writeTimeout(5, TimeUnit.SECONDS)//写入超时
                    .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)//添加自定义缓存拦截器（后面讲解），注意这里需要使用.addNetworkInterceptor
                    .build();

            Response response=okHttpClient.newCall(request).execute();

            if(response.isSuccessful()){
                Log.e("qqqqqqqqXXXX","111111");
                result= response.body().string();
            }else {
                Log.e("qqqqqqqqXXXX","222222222");
                NetWorkUtil.showNoNetWorkDlg(MyApplication.getContext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static String upLoadFile(String url, String fileNmae, File file) {
        String result = null;
        try {
            com.squareup.okhttp.Response response = OkHttpUtils.post()
                    .addHeader(" content-type", "multipart/form-data")
                    .addFile("file", fileNmae, file)
                    .url(url)
                    .build()
                    .execute();
            if (response.isSuccessful()) {
                result = response.code() + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    //创建一个Get请求
    private void volley_Get(final Context context, String url, Map<String, Object> map) {

        //Get方式访问时须将请求参数写在url里
        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {//请求成功回调

                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

            }
        }, new com.android.volley.Response.ErrorListener() {//请求失败回调
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();

            }
        });

        //设置请求标签
        request.setTag("abcGet");
        //将请求添加到队列
        MyApplication.getHttpQueue().add(request);
    }


}
