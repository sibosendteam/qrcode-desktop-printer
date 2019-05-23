package com.code.printer.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatUtils {

    private static String[] X36 = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "G", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    //10进制转换成36进制
    public static String convertTo36(String content) {
        String result = "";
        int val = Integer.parseInt(content);
        while (val >= 36) {
            result = X36[val % 36] + result;
            val /= 36;
        }

        if (val >= 0) result = X36[val] + result;

        return result;
    }

    //返回年份的后两个数
    public static String getYear() {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));

        if (year.length() > 3)
            return year.substring(2);

        return null;
    }


    public static String getDayOfYear() {
        Date date = new Date();
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        String st = sdf.format(date);
        String num = String.valueOf(ca.get(Calendar.DAY_OF_YEAR));

        if (num.length() == 1)
            return "00" + num;
        else if (num.length() == 2)
            return "0" + num;
        else if (num.length() == 3)
            return num;

        return null;
    }
}
