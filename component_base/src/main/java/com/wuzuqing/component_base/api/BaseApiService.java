package com.wuzuqing.component_base.api;

import com.wuzuqing.component_data.bean.BaseObj;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface BaseApiService {


    @Multipart
    @POST("example/chat/uploadFile")
    Observable<BaseObj<String>> uploadFile(@Part MultipartBody.Part file, @Query("sessionId") String sessionId);

//    @Multipart
//    @GET("example/chat/uploadFile")
//    Observable<BaseObj<String>> uploadFile(@Part MultipartBody.Part file , @Query("sessionId") String sessionId);


    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);
}
