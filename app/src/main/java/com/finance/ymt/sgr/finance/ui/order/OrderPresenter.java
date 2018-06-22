package com.finance.ymt.sgr.finance.ui.order;

import android.content.Context;
import android.content.SharedPreferences;


import com.finance.ymt.sgr.finance.config.BasePresenter;
import com.finance.ymt.sgr.finance.http.HttpUtils;
import com.finance.ymt.sgr.finance.model.CommonModel;
import com.finance.ymt.sgr.finance.model.OrderBean;
import com.finance.ymt.sgr.finance.model.OrderRespons;
import com.finance.ymt.sgr.finance.model.Result;
import com.finance.ymt.sgr.finance.model.oneArea;

import java.util.List;


/**
 * Data：2018/1/24/024-10:58
 * By  沈国荣
 * Description:
 */
public class OrderPresenter extends BasePresenter<OrderView> {
    private CommonModel commonModel;
    private Context contexts;
    private SharedPreferences sp ;
    public OrderPresenter(Context context) {
        super(context);
        this.contexts=context;
        this.commonModel = new CommonModel(context);
    }
    public void getOrderList(int status,int page,String address){
        commonModel.getOrderList(status, page,address, new HttpUtils.OnHttpResultListener() {
            @Override
            public void onResult(Object result) {
               Result<OrderRespons> temp=(Result<OrderRespons>)result;
                if(temp.status.equals("200")){
                   getView().showResult(temp.content.getData());
                }else{
                    getView().showResultOnErr(temp.message);

                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void UpdateOrder(OrderBean order, final int position){
        commonModel.UpdateOrder(order, new HttpUtils.OnHttpResultListener() {
            @Override
            public void onResult(Object result) {
                Result<OrderRespons> temp=(Result<OrderRespons>)result;
                if(temp.status.equals("200")){
                    getView().UpdateSussess(position);
                }else{
                    getView().showResultOnErr(temp.message);

                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }


    public void getAddress1(){
        commonModel.getAddress(new HttpUtils.OnHttpResultListener() {
            @Override
            public void onResult(Object result) {
                Result<List<oneArea>> temp=(Result<List<oneArea>>)result;
                if(temp.status.equals("200")){
                    getView().ResultAddress1(temp.content);
                }else{
                    getView().showResultOnErr(temp.message);
                }

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }


}
