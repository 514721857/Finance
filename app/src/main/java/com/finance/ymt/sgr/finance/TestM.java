package com.finance.ymt.sgr.finance;

import com.finance.ymt.sgr.finance.model.GsonTip;
import com.finance.ymt.sgr.finance.util.OrderStatus;
import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Data：2018/6/22/022-16:06
 * By  沈国荣
 * Description:
 */
public class TestM {

    public static void main(String[] args) throws ParseException {



        Gson gson=new Gson();
        GsonTip tip=new GsonTip();
        tip.setOrderId("id164646");
        tip.setShopId("1");
        tip.setStatus(1);
        tip.setType(0);

        System.out.println("推送的json"+objectToString(gson, tip));

        GsonTip   newTip= (GsonTip) stringToObject(gson,objectToString(gson, tip),GsonTip.class);

        System.out.println("接收的新消息"+newTip.getOrderId()+"shopid"+tip.getShopId());
//        System.out.println("时间："+OrderStatus.TimeFormat("2018-06-27T12:15:00.000+0800"));
    }
//   2018-06-27T09:31:50.000+0800    2018-06-27T12:15:00.000+0800  DateTime dt1= DateTime.ParseExact("2013-11-17T11:59:22+08:00","yyyy-MM-ddTHH:mm:sszzz",new System.Globalization.CultureInfo("en-us"));


    /**
     * 将json字符串转化成实体对象
     * @param json
     * @param classOfT
     * @return
     */
    public static Object stringToObject(Gson gson, String json , Class classOfT){
        return  gson.fromJson( json , classOfT ) ;
    }

    /**
     * 将对象准换为json字符串 或者 把list 转化成json
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String objectToString(Gson gson, T object) {
        return gson.toJson(object);
    }

}
