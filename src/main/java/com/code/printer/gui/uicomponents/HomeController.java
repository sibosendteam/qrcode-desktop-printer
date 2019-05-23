package com.code.printer.gui.uicomponents;

import com.code.printer.util.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.DriverPrinterConnection;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterDriver;
import com.zebra.sdk.printer.discovery.UsbDiscoverer;
import io.datafx.controller.ViewController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.awt.image.BufferedImage;

@ViewController(value = "/fxml/ui/Home.fxml", title = "Sibosend Printer")
public class HomeController {
    private static Logger logger = Logger.getLogger(HomeController.class);

    private volatile boolean isCancelled;

    private int number = 1;  //print count
    private int labelType = 2;    //label size   1：small  2：big

    @FXML
    private JFXComboBox<Label> comboConnectType;  //printer type  USB/Network
    @FXML
    private JFXComboBox<DiscoveredPrinterDriver> textPrinterList;   //printer driver name
    @FXML
    private JFXTextField textDriverIp; //IP
    @FXML
    private JFXTextField textDriverPort; //Port

    @FXML
    private Label labelPrinterList;
    @FXML
    private Label labelDriverIp;
    @FXML
    private Label labelDriverPort;

    //    @FXML
//    private JFXTextField textDepartment;
//    @FXML
//    private JFXTextField textNumber;
//    @FXML
//    private JFXComboBox<Label> textLabelSize;
    @FXML
    private JFXButton buttonRelease;
    @FXML
    private JFXButton buttonCancel;
    @FXML
    private TextArea console;

    private static Connection printerConnection;
    private DiscoveredPrinterDriver discoverPrinter;  //select current printer

    @PostConstruct
    public void init() {
        initPrinterDrivers();
        initValidator();
        comboConnectType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {
                setConnectType(newValue.getText());
                initPrinterDrivers();
            }
        });
        textDriverIp.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal)
                textDriverIp.validate();
        });
        textDriverPort.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal)
                textDriverPort.validate();
        });
        textPrinterList.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                discoverPrinter = textPrinterList.getValue();
                constructionPrinterDriver();
            }
        });

        buttonRelease.setOnAction(action -> {
            if (validatorPrintParam(false)) {
                printer();
                setVisible("release");
            }
        });
        buttonCancel.setOnAction(action -> {
            isCancelled = false;
            setVisible("cancel");
        });
    }

    private void printer() {
        isCancelled = true;
        new Thread(() -> {
            int count = 0;
            try {
                printerConnection.open();
                initLogInfo();

                while (isCancelled) {
                    if (count < number && !Thread.currentThread().isInterrupted()) {
                        String content = FormatUtils.getYear() + FormatUtils.getDayOfYear() + String.valueOf(2000 + number);
                        String covertTo36 = FormatUtils.convertTo36(content);
                        BufferedImage buffImg = QRGenerator.buildQR(covertTo36);
                        ImageGenerator.autoPrintImage(printerConnection, buffImg);
                        count++;
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                console.appendText(PrinterUtils.logInfo("exception " + e.getMessage()));
            } finally {
                setVisible(null);
                Thread.currentThread().interrupt();
                try {
                    if (printerConnection != null || printerConnection.isConnected()) {
                        printerConnection.close();
                    }
                } catch (ConnectionException e) {
                    console.appendText(PrinterUtils.logInfo("exception " + e.getMessage()));
                    logger.error(e.getMessage());
                }
                console.appendText(PrinterUtils.logInfo("print end"));
                logger.info("print a total of " + count + " labels");
            }
        }).start();
    }

    private void initValidator() {
        RegexValidator ipValidator = new RegexValidator();
        ipValidator.setRegexPattern("^(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])");
        ipValidator.setMessage("illegal ip");
        textDriverIp.getValidators().add(ipValidator);

        RegexValidator portValidator = new RegexValidator();
        portValidator.setRegexPattern("^([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{4}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])");
        portValidator.setMessage("illegal port");
        textDriverPort.getValidators().add(portValidator);

        comboConnectType.getSelectionModel().select(0);
        textPrinterList.getSelectionModel().select(0);
    }

    private void initPrinterDrivers() {
        try {
            DiscoveredPrinterDriver[] usbPrinterList = UsbDiscoverer.getZebraDriverPrinters();
            ObservableList<DiscoveredPrinterDriver> discoPrinters = FXCollections.observableArrayList();

            if (usbPrinterList != null && usbPrinterList.length > 0) {
                for (DiscoveredPrinterDriver printer : usbPrinterList)
                    discoPrinters.add(printer);

                textPrinterList.setItems(discoPrinters);
                discoverPrinter = usbPrinterList[0];
                printerConnection = new DriverPrinterConnection(discoverPrinter.printerName);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
           console.appendText(PrinterUtils.logInfo(e.getMessage()));
        }
    }

    private void constructionPrinterDriver() {
        try {
            printerConnection = new DriverPrinterConnection(discoverPrinter.printerName);
        } catch (Exception e) {
            logger.error(e.getMessage());
            console.appendText(PrinterUtils.logInfo(e.getMessage()));
        }
    }

    private boolean validatorPrintParam(boolean isRelease) {
        if (comboConnectType.getValue().getText().contains("USB") && isRelease) {
            if (textPrinterList.validate())
                return true;
        } else if (comboConnectType.getValue().getText().contains("USB") && !isRelease) {
            if (textPrinterList.validate())
                return true;
        } else if (comboConnectType.getValue().getText().contains("Network") && isRelease) {
            if (textDriverIp.validate() && textDriverPort.validate()) {
                printerConnection = new TcpConnection(textDriverIp.getText(), Integer.parseInt(textDriverPort.getText()));
                return true;
            }
        } else if (comboConnectType.getValue().getText().contains("Network") && !isRelease) {
            if (textDriverIp.validate() && textDriverPort.validate()) {
                printerConnection = new TcpConnection(textDriverIp.getText(), Integer.parseInt(textDriverPort.getText()));
                return true;
            }
        }

        return false;
    }

    private void setConnectType(String type) {
        if (type.contains("USB")) {
            labelDriverIp.setVisible(false);
            labelDriverPort.setVisible(false);
            textDriverIp.setVisible(false);
            textDriverPort.setVisible(false);
            labelPrinterList.setVisible(true);
            textPrinterList.setVisible(true);
        }
        if (type.contains("Network")) {
            labelDriverIp.setVisible(true);
            labelDriverPort.setVisible(true);
            textDriverIp.setVisible(true);
            textDriverPort.setVisible(true);
            labelPrinterList.setVisible(false);
            textPrinterList.setVisible(false);
        }
    }

    private void setVisible(String type) {
        if (TextUtils.isEmpty(type)) {
            buttonCancel.setVisible(false);
            buttonRelease.setVisible(true);
            buttonRelease.setDisable(false);
        } else if (type.equals("test")) {
            buttonCancel.setVisible(false);
            buttonRelease.setDisable(true);
        } else if (type.equals("release")) {
            buttonCancel.setVisible(true);
            buttonRelease.setVisible(false);
        } else if (type.equals("cancel")) {
            buttonCancel.setVisible(false);
            buttonRelease.setVisible(true);
        }
    }

    private void initLogInfo() {
        logger.info("printed once");
        logger.info("connected printer " + discoverPrinter.printerName);
        console.appendText(PrinterUtils.logInfo("connecting printer " + discoverPrinter.printerName));
    }
}