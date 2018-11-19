package com.wuzuqing.module_user_center.api;


import com.wuzuqing.component_data.bean.BaseObj;
import com.wuzuqing.component_data.bean.UserInfoBean;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @Created by TOME .
 * @时间 2018/5/15 18:18
 * @描述 ${TODO}
 */

public interface ApiService   {

    @POST("example/user/login")
    Observable<BaseObj<UserInfoBean>> login(
            @Query("phone") String phone,
            @Query("pwd") String pwd);

    @POST("example/user/register")
    Observable<BaseObj<UserInfoBean>> register(
            @Query("phone") String phone,
            @Query("pwd") String pwd);


}
