IDEA

该应用是使用JavaFX（jfoenix插件）开发的桌面可运行程序

使用javafx-gradle-plugin插件快速打包jar或者exe
https://github.com/FibreFoX/javafx-gradle-plugin

打包步骤(Gradle Tasks)：
1、打开Terminal
2、gradle jfxJar 打包可运行Jar
3、gradle jfxNative 打包可运行exe

注意：
打包成功之后需要将工程根目录的
zDriverAdapter.dll
zDriverAdapter64.dll
ZebraNativeUsbAdapter_32.dll
ZebraNativeUsbAdapter_64.dll
四个文件放到和主jar包同级目录
