package com.code.printer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    // 文件写入
    public static void fileWrite(String filePath, String content) {

        File file = null;
        FileOutputStream outputStream = null;

        try {
            file = new File(filePath);
            if(!file.exists())
                file.createNewFile();

            outputStream = new FileOutputStream(filePath); // 输出文件路径
            outputStream.write(PrinterUtils.encodeBase64(content.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 文件读取
    public static String fileRead(String filePath) {
        String content = null;
        File file = null;
        FileInputStream inputStream = null;

        try {
            file = new File(filePath);
            if(!file.exists())
                fileWrite(filePath, "false");

            inputStream = new FileInputStream(filePath); // 读取文件路径
            byte bs[] = new byte[inputStream.available()];
            inputStream.read(bs);
            content = new String(PrinterUtils.decodeBase64(bs));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return content;
    }
}
