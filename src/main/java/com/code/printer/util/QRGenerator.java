package com.code.printer.util;

import com.swetake.util.Qrcode;

import java.awt.*;
import java.awt.image.BufferedImage;

public class QRGenerator {

    public static BufferedImage buildQR(String content) {

        int imgSize = 0;
        BufferedImage bufImg = null;

        try {
            Qrcode handler = new Qrcode();
            // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            handler.setQrcodeErrorCorrect('M');
            handler.setQrcodeEncodeMode('B');
            // 获得内容的字节数组，设置编码格式
            byte[] contentBytes = content.getBytes("utf-8");
            handler.setQrcodeVersion(1);
            imgSize = 67 + 12 * (1 - 1);
            bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            // 设置背景颜色
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, imgSize, imgSize);

            // 设定图像颜色> BLACK
            gs.setColor(Color.BLACK);
            // 设置偏移量，不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容> 二维码
            if (contentBytes.length > 0 && contentBytes.length < 200) {
                boolean[][] codeOut = handler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            }

            gs.dispose();
            bufImg.flush();

        } catch (Exception e) {
            System.out.println("QRGenerator 二维码生成异常: " + e.getMessage());
        }

        return bufImg;
    }

}
