package com.finance.ymt.sgr.finance.http;




import com.finance.ymt.sgr.finance.model.OrderRespons;
import com.finance.ymt.sgr.finance.model.Result;
import com.finance.ymt.sgr.finance.model.User;
import com.finance.ymt.sgr.finance.model.oneArea;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


/**
 * Data：2018/1/23/023-16:01
 * By  沈国荣
 * Description: 接口类
 */

public interface HttpService {






    /**
     * 登录接口
     *
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("seller/login")
    Observable<Result<User>> getLogin(@Body RequestBody info);


    /**
     * 测试接口
     *
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("seller/order/list")
    Observable<ResponseBody> getSave(@Header("token") String token, @Body RequestBody info);


    /**
     * 获得订单接口
     *@param info 请求体
     * @param token 请求头
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("seller/order/page")
    Observable<Result<OrderRespons>> getOrderList(@Header("token") String token, @Body RequestBody info);


    /**
     * 修改订单接口
     *@param info 请求体
     * @param token 请求头
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("seller/order/update")
    Observable<Result<OrderRespons>> UpdateOrder(@Header("token") String token, @Body RequestBody info);


    /**
     * 获取一级地址
     *
     *
     * @return
     */

    @GET("address_one/list")
    Observable<Result<List<oneArea>>> getAddress1();
}