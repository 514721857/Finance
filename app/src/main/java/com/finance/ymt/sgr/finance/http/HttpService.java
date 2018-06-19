package com.finance.ymt.sgr.finance.http;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Data：2018/1/23/023-16:01
 * By  沈国荣
 * Description: 接口类
 */

public interface HttpService {


    /**
     * 登录接口
     *
     * @param content
     * @return
     */
    @GET("api/user/saves")
    Observable<ResponseBody> getSave(@Query("content") String content);


}