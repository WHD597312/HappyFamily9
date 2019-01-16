package com.xr.happyFamily.together.http;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface HttpService {

    /**
     * get请求
     * @param header 可变的请求头
     * @param url 完整的url 如http//:localhost:8080/user?name=xxx
     * @return
     */
    @GET
    Call<ResponseBody> getRequest(@Header("authorization") String header, @Url String url);

    /**
     * post请求
     * @param header 可变的请求头
     * @param url 完整的url 如http://localhost:8080/user
     * @param body  请求体
     * @return
     */
    @POST
    Call<ResponseBody> postQuest(@Header("authorization") String header,@Url String url, @Body RequestBody body);

    /**
     * form表单提交
     * 上传文件 post请求
     * @param header 可变的的请求头
     * @param userId 对应后端接口如：http://localhost:8080/user/{userId}/headImg
     * @param file 上传的文件
     * @return
     */
    @Multipart
    @POST("user/{userId}/headImg")
    Call<ResponseBody> uploadFile(@Header("authorization")String header,@Path("userId") String userId, @Part MultipartBody.Part file);
}
