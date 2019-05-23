package com.code.printer.gui;

import com.jfoenix.controls.JFXListView;
import com.code.printer.gui.uicomponents.AboutController;
import com.code.printer.gui.uicomponents.HomeController;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.annotation.PostConstruct;
import java.util.Objects;

@ViewController(value = "/fxml/SideMenu.fxml", title = "Material Design Example")
public class SideMenuController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    @ActionTrigger("home")
    private Label home;
    @FXML
    @ActionTrigger("about")
    private Label about;
    @FXML
    private JFXListView<Label> sideList;
    @FXML
    private JFXListView<Label> sideSubList;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        Objects.requireNonNull(context, "context");
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        sideList.propagateMouseEventsToParent();
        sideSubList.propagateMouseEventsToParent();

        new Thread(() -> {
            Platform.runLater(() -> {
                sideList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
                    if (newVal != null) {
                        try {
                            contentFlowHandler.handle(newVal.getId());
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                });
                sideSubList.getSelectionModel().selectedItemProperty().addListener((p, l, d) -> {
                    if (d != null) {
                        try {
                            contentFlowHandler.handle(d.getId());
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                });
            });
        }).start();

        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        bindNodeToController(home, HomeController.class, contentFlow, contentFlowHandler);
        bindNodeToController(about, AboutController.class, contentFlow, contentFlowHandler);
    }

    private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) {
        flow.withGlobalLink(node.getId(), controllerClass);
    }

}
