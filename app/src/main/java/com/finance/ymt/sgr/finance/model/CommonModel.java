package com.finance.ymt.sgr.finance.model;

import android.content.Context;

import com.finance.ymt.sgr.finance.http.HttpService;
import com.finance.ymt.sgr.finance.http.HttpUtils;

import io.reactivex.Observable;


/**
 * 作者: Dream on 16/9/24 21:09
 * QQ:510278658
 * E-mail:510278658@qq.com
 */
public class CommonModel extends BaseModel {

    public CommonModel(Context context) {
        super(context);
    }

    /**
     * 获取二维码
     * @param content
     * @param onLceHttpResultListener
     */
    public void getSave(String content,final HttpUtils.OnHttpResultListener onLceHttpResultListener) {

        HttpService essenceService= buildService(HttpService.class);
        buildObserve((Observable)essenceService.getSave(content),onLceHttpResultListener);

    }
}
