# qrcode-desktop-printer

这是一款基于JavaFX开发的桌面条码标签打印软件，它不同于[ZebraDesigner](https://www.zebra.cn/us/en/products/software/barcode-printers/zebralink/zebra-designer.html)，可实现批量打印，必要时可连接数据库。

特色：
>* 提供USB和NETWORK两种连接方式
>* 图文混合
>* 自定义打印数量
>* 高效的批量打印速度

### 效果预览

 ![screenshot](/screenshot/desktop_printer.png)
 
 ![screenshot](/screenshot/qrcode.png)
 

### 打包

[javafx-gradle-plugin插件](https://github.com/FibreFoX/javafx-gradle-plugin)

1、打包可运行Jar：gradle jfxJar 

2、打包可运行exe：gradle jfxNative 

注意：

打包成功之后需要将工程根目录的

1.zDriverAdapter.dll
2.zDriverAdapter64.dll
3.ZebraNativeUsbAdapter_32.dll
4.ZebraNativeUsbAdapter_64.dll

四个斑马打印机驱动包放到和主jar包同级目录

### 温馨提示

* 双击QR_Code_Printer目录下的QR_Code_Printer.exe可查看效果。

* 该项目仅提供了小部分功能，二维码采用生成图片的方式打印，批量生产时效率低下。提高打印效率可通过[ZPL](https://www.zebra.cn/us/en/support-downloads/knowledge-articles/zpl-command-information-and-details.html)命令的方式去实现，详情请栈内信留言，我们将第一时间与您联系。
 
 
