/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import comp421.group46.model.ConnectionFactory;
import comp421.group46.model.DialogFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Katie Lin
 */
public class RegistrationController implements Initializable, Controller {

    private final ConnectionFactory cf = ConnectionFactory.getConnectionFactory();
    private final DialogFactory df = DialogFactory.getDialogFactory();
    @FXML
    private TextField customerIDtextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField addressTextField;
    private boolean decision;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void handleConfirm(ActionEvent event) {
        if(someBoxesEmpty()){
            String emptyFields = "";
            if(firstNameTextField.getText() == null || firstNameTextField.getText().trim().isEmpty()) emptyFields += "\tFirst name\n";
            if(lastNameTextField.getText() == null || lastNameTextField.getText().trim().isEmpty()) emptyFields += "\tLast name\n";
            if(emailTextField.getText() == null || emailTextField.getText().trim().isEmpty()) emptyFields += "\tEmail\n";
            if(addressTextField.getText() == null || addressTextField.getText().trim().isEmpty()) emptyFields += "\tAddress empty";
            df.popupWarning("Some fields are empty", "The following fields are empty:\n"+emptyFields, "Please fill the fields in before continuing.");
            return;
        }
        try{
            Connection c = cf.getConnection();
            String callableSQL = "INSERT INTO Customer VALUES (?,?,?,?,?)";
            PreparedStatement ps = c.prepareStatement(callableSQL);
            ps.setInt(1, Integer.valueOf(customerIDtextField.getText()));
            ps.setString(2,firstNameTextField.getText());
            ps.setString(3, lastNameTextField.getText());
            ps.setString(4, emailTextField.getText());
            ps.setString(5, addressTextField.getText());
            if(firstNameTextField.getText().length() > 50){
                df.popupWarning("Name too long", "Yeah ok. Your name is not that long\n"+firstNameTextField.getText()+" is not a name we will accept.", "Time for you to get a new name.");
                return;
            } else if (lastNameTextField.getText().length() > 50) {
                df.popupWarning("Name too long", "Yeah ok. You think you're funny or something?\n"+lastNameTextField.getText()+" is not a name we will accept.", "Enter a real family name or get out.");
                return;
            } else if (emailTextField.getText().length() > 50) {
                df.popupWarning("Email too long", "Yeah ok. You think you're funny or something?\n"+emailTextField.getText()+" is not an email we will accept.", "Go make a new one.");
                return;
            } else if (addressTextField.getText().length() > 100) {
                df.popupWarning("Address too long", "Yeah ok. That's not a real address.", "Enter your real address and stop wasting our time.");
                return;
            }
            ps.executeQuery();
        } catch(Exception e){
            e.printStackTrace();
        }
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).hide();
        this.decision = true;
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        this.decision = false;
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).hide();
    }
    
    public void setCustomerID(int customerID){
        this.customerIDtextField.setText(String.valueOf(customerID));
    }
    
    public boolean getDecision(){
        return this.decision;
    }
    public void setDescription(String derp){
        //DO NOTHING
    }
    public boolean someBoxesEmpty() {
        if(firstNameTextField.getText() == null || firstNameTextField.getText().trim().isEmpty() ||
            lastNameTextField.getText() == null || lastNameTextField.getText().trim().isEmpty() ||
            emailTextField.getText() == null || emailTextField.getText().trim().isEmpty() ||
            addressTextField.getText() == null || addressTextField.getText().trim().isEmpty()) {
            return true;
        }
        return false;
    }
}
