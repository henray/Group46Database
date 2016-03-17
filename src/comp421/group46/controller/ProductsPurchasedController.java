/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import comp421.group46.model.DialogFactory;
import constants.Paths;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Katie Lin
 */
public class ProductsPurchasedController implements Initializable, Controller {

    @FXML
    private TextArea description;
    @FXML
    private TextField idInput;
    private DialogFactory df = DialogFactory.getDialogFactory();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleConfirm(ActionEvent event) {
        if(!isInteger(idInput.getText())) {
            df.popupInformation("Incorrect input", "That is not an integer", "Please input an integer");
            return;
        }
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).hide();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).close();
    }

    @Override
    public void setDescription(String description) {
        this.description.setText(description);
    }
    public int getProductID() {
        return Integer.valueOf(idInput.getText());
    }
    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
