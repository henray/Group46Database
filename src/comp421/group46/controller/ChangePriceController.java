/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author Katie Lin
 */
public class ChangePriceController implements Initializable, Controller {

    @FXML
    private ComboBox<?> teamOptionsBox;
    @FXML
    private TextArea description;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleSuiteBoxChoice(ActionEvent event) {
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
    }

    @FXML
    private void handleCancel(ActionEvent event) {
    }
    
    public void setDescription(String description){
        this.description.setText(description);
    }
}
