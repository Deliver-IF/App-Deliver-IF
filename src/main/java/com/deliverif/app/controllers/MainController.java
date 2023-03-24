package com.deliverif.app.controllers;

import com.deliverif.app.views.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import lombok.Getter;

@Getter
public class MainController {
    @FXML
    private Pane mapPane;
    @FXML
    private DialogPane intersectionInfoDialog;

    @FXML
    private Text deliveryWindow;

    @FXML
    private Button prevDeliveryPointInfo;
    @FXML
    private Button nextDeliveryPointInfo;

    @FXML
    protected void hideIntersectionInfoDialog() {
        intersectionInfoDialog.setVisible(false);
    }

    @FXML
    protected void nextDeliveryPointInfoDialog() {
        if(Map.currentIndex == 0) {
            prevDeliveryPointInfo.setVisible(true);
        }
        Map.currentIndex++;
        deliveryWindow.setText("Delivery Window : " + Map.currentDeliveryRequests.get(Map.currentIndex).getStartTimeWindow() + "h-"
                + (Map.currentDeliveryRequests.get(Map.currentIndex).getStartTimeWindow() + 1) + "h\n"
        );
        if(Map.currentIndex == Map.currentDeliveryRequests.size() - 1) {
            nextDeliveryPointInfo.setVisible(false);
        }
        System.out.println("Next");
    }

    @FXML
    protected void prevDeliveryPointInfoDialog() {
        if(Map.currentIndex == Map.currentDeliveryRequests.size() - 1) {
            nextDeliveryPointInfo.setVisible(true);
        }
        Map.currentIndex--;
        deliveryWindow.setText("Delivery Window : " + Map.currentDeliveryRequests.get(Map.currentIndex).getStartTimeWindow() + "h-"
                + (Map.currentDeliveryRequests.get(Map.currentIndex).getStartTimeWindow() + 1) + "h\n"
        );
        if(Map.currentIndex == 0) {
            prevDeliveryPointInfo.setVisible(false);
        }
        System.out.println("Prev");
    }

    @FXML
    protected void addDeliveryPointDialog() {
        System.out.println("Add");
    }

}