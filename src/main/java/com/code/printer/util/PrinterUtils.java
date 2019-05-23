package com.code.printer.util;

import com.zebra.sdk.printer.PrinterUtil;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class PrinterUtils {

    private static Logger logger = Logger.getLogger(PrinterUtil.class);

    private static String[] X36 = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "G", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private static final String LOGGER_TITLE = "test";

    /**
     * convert 10->36
     * @param content
     * @return
     */
    public static String convertTo36(String content) {
        return Long.toString(Long.parseLong(content), 36).toUpperCase();
    }

    /**
     * return the year after two
     * @return
     */
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

        String num = String.valueOf(ca.get(Calendar.DAY_OF_YEAR));

        if (num.length() == 1)
            return "00" + num;
        else if (num.length() == 2)
            return "0" + num;
        else if (num.length() == 3)
            return num;

        return null;
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int printType(String content) {
        if (TextUtils.isEmpty(content))
            return 0;

        if (content.contains("25") || content.contains("12"))
            return 1;

        if (content.contains("62") || content.contains("36"))
            return 2;

        return 0;
    }

    public static void openDefaultBrowse() {
        String site = "http://www.sibosend.com/page7";
        try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isDesktopSupported()
                    && desktop.isSupported(Desktop.Action.BROWSE)) {
                URI uri = new URI(site);
                desktop.browse(uri);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * current time
     * @return
     */
    public static String currentFormatTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
        return sdf.format(date);
    }

    // log format
    public static String logInfo(String log) {
        return LOGGER_TITLE + " " + PrinterUtils.currentFormatTime() + ": " + log + "\n";
    }

    // base64
    public static byte[] encodeBase64(byte[] content) {
        return Base64.getEncoder().encode(content);
    }

    public static byte[] decodeBase64(byte[] content) {
        return Base64.getDecoder().decode(content);
    }
}
