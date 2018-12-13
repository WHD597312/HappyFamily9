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

    @GET
    Call<ResponseBody> getRequest(@Header("authorization") String header, @Url String url);

    @POST
    Call<ResponseBody> postQuest(@Header("authorization") String header,@Url String url, @Body RequestBody body);

    @Multipart
    @POST("user/{userId}/headImg")
    Call<ResponseBody> uploadFile(@Header("authorization")String header,@Path("userId") String userId, @Part MultipartBody.Part file);
}
