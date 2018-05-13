package com.xr.http;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    public static String postOkHpptRequest(String url,Map<String, Object> map) {
        String result=null;
        try{
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
            JSONObject jsonObject=new JSONObject();
            for (Map.Entry<String,Object> param:map.entrySet()){
                jsonObject.put(param.getKey(),param.getValue());
            }



            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE),jsonObject.toJSONString());

            Request request = new Request.Builder()
                    .addHeader("client","android-xr")
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient okHttpClient=new OkHttpClient();
            Response response=okHttpClient.newCall(request).execute();

            if(response.isSuccessful()){
                result= response.body().string();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
