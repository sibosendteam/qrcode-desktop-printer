package com.code.printer;

import com.jfoenix.controls.JFXDecorator;
import com.code.printer.gui.MainController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

public class MainPrinter extends Application {

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // init log4j
        Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/log4j.properties"));
        PropertyConfigurator.configure(props);

        Flow flow = new Flow(MainController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", stage);
        flow.createHandler(flowContext).start(container);

        JFXDecorator decorator = new JFXDecorator(stage, container.getView());
        decorator.setCustomMaximize(true);

        // set icon
        ImageView imageView = new ImageView(new Image(MainPrinter.class.getResourceAsStream("/logo.png")));
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        decorator.setGraphic(imageView);
        String cssURI = getClass().getResource("/css/jfoenix-theme.css").toExternalForm();
        decorator.getStylesheets().add(cssURI);

        stage.setTitle("QR Code Printer");
        stage.getIcons().add(new Image(MainPrinter.class.getResourceAsStream("/logo.png")));

        double width = 900;
        double height = 700;

        Scene scene = new Scene(decorator, width, height);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(MainPrinter.class.getResource("/css/jfoenix-fonts.css").toExternalForm(),
                MainPrinter.class.getResource("/css/jfoenix-design.css").toExternalForm(),
                MainPrinter.class.getResource("/css/jfoenix-main-demo.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}


