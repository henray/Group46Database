/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import comp421.group46.model.DialogFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Katie Lin
 */
public class ChangePriceController implements Initializable, Controller {

    @FXML
    private TextArea description;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private TextField thresholdTextField;
    @FXML
    private TextField percentageTextField;
    
    private int thresholdamount = 0;
    private double percentage = 0;
    private boolean increaseOrDecrease;
    private final DialogFactory df = DialogFactory.getDialogFactory();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleConfirm(ActionEvent event) {
        if(!allInputsFilled()){
            String emptyFields = "";
            if(thresholdTextField.getText().trim().isEmpty() || thresholdTextField.getText() == null) emptyFields += "\tThreshold amount\n";
            if(percentageTextField.getText().trim().isEmpty() || percentageTextField.getText() == null) emptyFields += "\tPercentage\n";
            if(toggleGroup.selectedToggleProperty().isNull().getValue()) emptyFields += "\tAbove/Below option";
            df.popupWarning("Missing inputs", "The following fields need to be filled\n"+emptyFields, "Please fill in all required fields to continue.");
            return;
        }
        if(!isInteger(thresholdTextField.getText())){
            df.popupError("Not an integer","Your input amount for Threshold is not an integer.","Please input an integer for threshold");
            return;
        } else if(!isInteger(percentageTextField.getText())){
            df.popupError("Not an integer","Your input amount for Percentage is not an integer.","Please input an integer for percentage");
            return;
        }else {
            try {
                 thresholdamount = Integer.valueOf(thresholdTextField.getText());
                 percentage = ((double)Integer.valueOf(percentageTextField.getText())) / 100.0;
                 System.out.println(percentage);
//                 if(percentage > 100 || percentage < 1) df.popupError("Input too large", "The input percentage is too high", "Please input a smaller value");
            } catch(NumberFormatException e) {
                df.popupError("Input too large", "The inputs for threshold or percentage are too large for the system to handle", "Please input smaller values");
                return;
            }
        }
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).hide();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        clearInputs();
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).close();
    }
    
    public void setDescription(String description){
        this.description.setText(description);
    }
    
    public void clearInputs() {
        toggleGroup.getSelectedToggle().selectedProperty().set(false);
        thresholdTextField.clear();
    }
    
    public int getThresholdAmount(){
        return this.thresholdamount;
    }
    
    public boolean getIncreaseOrDecrease(){
        return this.increaseOrDecrease;
    }
    public double getModifier(){
        return this.percentage;
    }

    private boolean allInputsFilled() {
        if(thresholdTextField.getText().trim().isEmpty() || thresholdTextField.getText() == null || 
                toggleGroup.selectedToggleProperty().isNull().getValue() ||
                percentageTextField.getText().trim().isEmpty() || percentageTextField.getText() == null) {
            return false;
        }
        return true;
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

    @FXML
    private void handleIncrease(ActionEvent event) {
        increaseOrDecrease = true;
    }

    @FXML
    private void handleDecrease(ActionEvent event) {
        increaseOrDecrease = false;
    }
}
