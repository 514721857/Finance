package com.finance.ymt.sgr.finance;

import com.finance.ymt.sgr.finance.util.OrderStatus;

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

        System.out.println("时间："+OrderStatus.TimeFormat("2018-06-25T07:16:56.000+0000"));
    }
//    DateTime dt1= DateTime.ParseExact("2013-11-17T11:59:22+08:00","yyyy-MM-ddTHH:mm:sszzz",new System.Globalization.CultureInfo("en-us"));



}
