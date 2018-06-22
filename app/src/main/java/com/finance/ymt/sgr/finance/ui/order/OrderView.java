package com.finance.ymt.sgr.finance.ui.order;

import com.finance.ymt.sgr.finance.model.OrderBean;
import com.finance.ymt.sgr.finance.model.oneArea;
import com.tz.mvp.framework.base.view.MvpView;


import java.util.List;

/**
 * Data：2018/1/24/024-10:56
 * By  沈国荣
 * Description:V层接口
 */
public interface OrderView extends MvpView {
    void UpdateSussess(int position);
    void ResultAddress1(List<oneArea> reslt);
    void showResult(List<OrderBean> result);
    void showResultOnErr(String err);

}
