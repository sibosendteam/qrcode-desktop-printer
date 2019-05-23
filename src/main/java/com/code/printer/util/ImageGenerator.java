package com.code.printer.util;

import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.graphics.ZebraImageFactory;
import com.zebra.sdk.graphics.ZebraImageI;
import com.zebra.sdk.printer.ZebraPrinterFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageGenerator {

    public static void autoPrintImage(Connection connection, BufferedImage buffImg) throws Exception{

        if (connection == null)
            return;

        ZebraImageI image = ZebraImageFactory.getImage(buffImg);

        ZebraPrinterFactory.getInstance(connection).printImage(image, 270, 0, 0, 0, false);
    }


    public static BufferedImage imgComposition(BufferedImage buffCode, String content) {
        int width = 710, height = 425;
        // 添加字体的属性设置
        Font font = new Font("SimSun", Font.BOLD, 90);
        BufferedImage buffImg = null;

        try {
            buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            //获取图形上下文
            Graphics2D g = (Graphics2D) buffImg.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            //通过使用当前绘图表面的背景色进行填充来清除指定的矩形。
            g.setBackground(Color.WHITE);
            g.clearRect(0, 0, width, height);

            //设置文本样式
            g.setFont(font);
            g.setColor(Color.BLACK);


            //添加二维码(地址,左边距,上边距,图片宽度,图片高度,未知)
            g.drawImage(buffCode, 10, 50, 350, 350, null);

            //添加右边的描述信息
            g.drawString("TEST", 390, 180);
            g.drawString(content, 390, 320);

            g.dispose();
            buffImg.flush();

        } catch (Exception e) {
            System.out.println("ImageGenerator 图片合成错误：" + e.getMessage());
            System.exit(0);
        }

        return buffImg;
    }
}
