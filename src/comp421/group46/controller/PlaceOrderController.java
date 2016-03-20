/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import comp421.group46.model.ConnectionFactory;
import comp421.group46.model.DialogFactory;
import constants.Paths;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private int iter;
    private int derp;
    
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
        derp = 0;
        if(someBoxesEmpty()){
            String emptyFields = "";
            if(customerIDTextField.getText() == null || customerIDTextField.getText().trim().isEmpty()) emptyFields += "\tCustomer ID\n";
            if(productBox.getSelectionModel().isEmpty()) emptyFields += "\tProduct to purchase\n";
            if(quantityTextField.getText() == null || quantityTextField.getText().trim().isEmpty()) emptyFields += "\tQuantity to purchase\n";
            if(paymentBox.getSelectionModel().isEmpty()) emptyFields += "\tPayment type\n";
            df.popupWarning("Some fields are empty", "The following fields are empty:\n"+emptyFields, "Please fill the fields in before continuing.");
            return;
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
            try{
                customerID = Integer.valueOf(customerIDTextField.getText());
                derp = 1;
                if(quantityTextField.getText().trim().isEmpty()) quantity = 0;
                else quantity = Integer.valueOf(quantityTextField.getText());
                if(quantity <= 0) {
                df.popupWarning("Invalid input","Quantity needs to be a positive amount", "Please enter a positive quantity to continue");
                return;
            }
            } catch(NumberFormatException e){
                if (derp == 0) df.popupWarning("Excessive length","Input for Customer ID is too large for the\n system to process into an integer","Please enter a more realistic ID.\nYou're not that special.");
                else if (derp == 1) df.popupWarning("Excessive length","Input for Quantity is too large for the\n system to process into an integer","Please enter a more reasonable amount.\nYou're not that rich.");
                return;
            }
            try{
                Connection c = cf.getConnection();
                String callableSQL = "SELECT EXISTS(SELECT customerID FROM Customer WHERE customerID = ? )";
                CallableStatement cs = c.prepareCall(callableSQL);
                cs.setInt(1, customerID);
                ResultSet rs1 = cs.executeQuery();
                rs1.next();
                boolean customerExists = rs1.getBoolean(1);
                if(!customerExists){
                    //ask customer if they want to make an account. If they do, run the query.
                    if (!openRegistrationWindow(customerID, event)){
//                        clearAllInputs();
                        return;
                    }
                }
                callableSQL = "SELECT warehouseID FROM Warehouse";
                Statement s = c.createStatement();
                ResultSet rs2 = s.executeQuery(callableSQL);
                List<Integer> results = new ArrayList<>();
                while(rs2.next()){
                    results.add(rs2.getInt(1));
                }
                List<Integer> used = new ArrayList<>();
                Random random = new Random();
                warehouseID = random.nextInt(results.size()) +1;
                System.out.println("Initial warehouseID"+ warehouseID);
                System.out.println("Number of warehouses: "+results.size());
                System.out.println(warehouseHasEnoughStock(warehouseID,productID,quantity));
                while(!warehouseHasEnoughStock(warehouseID, productID, quantity)) {
                    used.add(warehouseID);
                     System.out.println("Adding warehouse id: "+warehouseID+"to used.");
                    if(used.size() == results.size()) {
                        df.popupWarning("Not enough stock", "We do not have enough stock in our warehouses to support this order", "Please try again another time");
                        clearAllInputs();
                        return;
                    } else {
                        while(used.contains(warehouseID = random.nextInt(results.size())+1)){
                        }
                    }   
                }
                
            } catch(Exception e) {
                System.out.println("DEEEEEEEEEEEEEEEEEERP");
                e.printStackTrace();
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
             RegistrationController c = loader.getController();
            c.setCustomerID(customerID);
            registrationPopup.showAndWait();
            return c.getDecision();
        } catch(IOException e){
            System.exit(1);
        }
        return false;
    }
   
    public boolean warehouseHasEnoughStock(int warehouseID, int productID, int quantity) {
        try{
            String callableSQL = "SELECT quantity FROM WarehouseProduct WHERE warehouseID = ? AND productID = ?";
            Connection c = cf.getConnection();
            PreparedStatement ps = c.prepareStatement(callableSQL);
            ps.setInt(1,warehouseID);
            ps.setInt(2,productID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if(rs.getInt(1) < quantity) return false;
        } catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
