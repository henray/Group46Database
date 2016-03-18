/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import comp421.group46.model.ConnectionFactory;
import comp421.group46.model.DialogFactory;
import constants.Paths;
import constants.PopupType;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Katie Lin
 */
public class PlaceOrderController implements Initializable,Controller {

    @FXML
    private TextArea description;
    @FXML
    private TextField customerIDTextField;
    @FXML
    private ComboBox<Integer> productBox;
    @FXML
    private TextField quantityTextField;
    @FXML
    private ComboBox<String> paymentBox;
    
    private final DialogFactory df = DialogFactory.getDialogFactory();
    private final ConnectionFactory cf = ConnectionFactory.getConnectionFactory();
    
    private int customerID;
    private int productID;
    private int quantity;
    private String paymentType;
    private int warehouseID;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            List<Integer> productIDList = new ArrayList<>();
            Connection c = cf.getConnection();
            String callableSQL1 = "SELECT productID FROM Product";
            Statement s1 = c.createStatement();
            ResultSet rs1 = s1.executeQuery(callableSQL1);
            while(rs1.next()){
                productIDList.add(rs1.getInt(1));
            }
            ObservableList<Integer> options1 = FXCollections.observableArrayList(productIDList);
            productBox.setItems(options1);
            List<String> paymentOptionsList = new ArrayList<>();
            String callableSQL2 = "SELECT UNNEST(ENUM_RANGE(NULL::payment_method))";
            Statement s2 = c.createStatement();
            ResultSet rs2 = s2.executeQuery(callableSQL2);
            while(rs2.next()){
                paymentOptionsList.add(rs2.getString(1));
            }
             ObservableList<String> options2 = FXCollections.observableArrayList(paymentOptionsList);
            paymentBox.setItems(options2);
        } catch(Exception e){
            e.printStackTrace();
        }
    }    


    @FXML
    private void handleConfirm(ActionEvent event) {
        if(someBoxesEmpty()){
            String emptyFields = "";
            if(customerIDTextField.getText() == null || customerIDTextField.getText().trim().isEmpty()) emptyFields += "\tCustomer ID";
            if(productBox.getSelectionModel().isEmpty()) emptyFields += "\tProduct to purchase\n";
            if(customerIDTextField.getText() == null || customerIDTextField.getText().trim().isEmpty()) emptyFields += "\tQuantity to purchase";
            if(paymentBox.getSelectionModel().isEmpty()) emptyFields += "\tPayment type\n";
            df.popupWarning("Some fields are empty", "The following fields are empty:\n"+emptyFields, "Please fill the fields in before continuing.");
        } else {
            String notInteger = "";
            if(!isInteger(customerIDTextField.getText()))
                notInteger += "\tCustomer ID needs to be an integer\n";
            if(!isInteger(quantityTextField.getText()))
                notInteger += "\tQuantity needs to be an integer\n";
            if(!notInteger.isEmpty()) {
                df.popupInformation("Bad input","The following fields are invalid inputs:\n"+notInteger,"Please change your input(s) to an integer");
                return;
            }
            customerID = Integer.valueOf(customerIDTextField.getText());
            quantity = Integer.valueOf(quantityTextField.getText());
            if(quantity <= 0) {
                df.popupWarning("Invalid input","Quantity is negative", "Please enter a non-negative quantity");
                return;
            }
            try{
                Connection c = cf.getConnection();
                String callableSQL = "SELECT EXISTS customerID FROM Customer WHERE customerID = ?";
                PreparedStatement ps = c.prepareCall(callableSQL);
                ps.setInt(1, customerID);
                ResultSet rs1 = ps.executeQuery();
                rs1.next();
                boolean customerExists = rs1.getBoolean(1);
                if(!customerExists){
                    //ask customer if they want to make an account. If they do, run the query.
                    if (openRegistrationWindow(customerID, event)) return;
                }
                
                callableSQL = "SELECT warehouseID FROM Warehouse";
                Statement s = c.createStatement();
                ResultSet rs2 = s.executeQuery(callableSQL);
                List<Integer> results = new ArrayList<>();
                while(rs2.next()){
                    results.add(rs2.getInt(1));
                }
                
            } catch(Exception e) {

            }
        }
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).hide();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        clearAllInputs();
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).close();
    }
    
    public void setDescription(String description){
        this.description.setText(description);
    }
    public int getWarehouseID() {
        return this.warehouseID;
    }
    public String getPaymentMethod() {
        return this.paymentType;
    }
    public int getQuantity() {
        return this.quantity;
    }
    public int getProductID() {
        return this.productID;
    }
    public int getCustomerID() {
        return this.customerID;
    }

    @FXML
    private void handleProduct(ActionEvent event) {
        productID = productBox.getSelectionModel().getSelectedItem();
    }
    @FXML
    private void handlePayment(ActionEvent event) {
        paymentType = paymentBox.getSelectionModel().getSelectedItem();
    }
    private void clearAllInputs(){
        productBox.getSelectionModel().clearSelection();
        paymentBox.getSelectionModel().clearSelection();
        customerIDTextField.clear();
        quantityTextField.clear();
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
    public boolean someBoxesEmpty() {
        if(productBox.getSelectionModel().isEmpty()
                || paymentBox.getSelectionModel().isEmpty()
                || customerIDTextField.getText() == null || customerIDTextField.getText().trim().isEmpty()
                || quantityTextField.getText() == null || quantityTextField.getText().trim().isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean openRegistrationWindow(int customerID, ActionEvent event) {
        final Stage registrationPopup = new Stage();
        registrationPopup.initModality(Modality.APPLICATION_MODAL);
        registrationPopup.setTitle("Registration");
        registrationPopup.initOwner(((Stage) ((Node)event.getSource()).getScene().getWindow()));
        registrationPopup.getIcons().add(new Image(Paths.FIGHTER_JET_LOGO));
        registrationPopup.centerOnScreen();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Paths.REGISTRATION_FXML));
            Scene popUpScene = new Scene(loader.load());
            
            registrationPopup.setScene(popUpScene);
            registrationPopup.showAndWait();
            RegistrationController c = loader.getController();
            c.setCustomerID(customerID);
            return c.getDecision();
        } catch(IOException e){
            System.exit(1);
        }
        return false;
    }
   
    
}
