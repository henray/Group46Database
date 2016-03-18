/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import comp421.group46.model.ConnectionFactory;
import comp421.group46.model.DialogFactory;
import java.net.URL;
import java.sql.CallableStatement;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Katie Lin
 */
public class TransferWarehouseController implements Initializable,Controller {

    @FXML
    private TextArea description;
    @FXML
    private ComboBox<Integer> sourceWarehouseBox;
    @FXML
    private ComboBox<Integer> destWarehouseBox;
    @FXML
    private ComboBox<Integer> productsAtCurrentWarehouse;
    @FXML
    private VBox stockDisplay;
    @FXML
    private TextField currentStockDisplay;
    @FXML
    private TextField transferQuantity;
    
    private ConnectionFactory cf = ConnectionFactory.getConnectionFactory();
    private DialogFactory df = DialogFactory.getDialogFactory();
    
    private int sourceWarehouse;
    private int destinationWarehouse;
    private int productID;
    private int quantity;
    
    

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
            if(sourceWarehouseBox.getSelectionModel().isEmpty()) emptyFields += "\tSource Warehouse\n";
            if(destWarehouseBox.getSelectionModel().isEmpty()) emptyFields += "\tDestination Warehouse\n";
            if(productsAtCurrentWarehouse.getSelectionModel().isEmpty()) emptyFields += "\tProducts Available\n";
            if(stockDisplay.isVisible()){
                if(transferQuantity.getText() == null || transferQuantity.getText().trim().isEmpty())
                    emptyFields += "\tQuantity to transfer";
            }
            df.popupWarning("Some fields are empty", "The following fields are empty:\n"+emptyFields, "Please fill the fields in before continuing.");
            return;
        }
        if(!isInteger(transferQuantity.getText())) {
            df.popupInformation("Incorrect input", "That is not an integer", "Please input an integer");
            return;
        }
        quantity = Integer.valueOf(transferQuantity.getText());
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).hide();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).hide();
        clearAllBoxes();
    }
    
    public void setDescription(String description){
        this.description.setText(description);
    }

    @FXML
    private void handleSourceWarehouse(ActionEvent event) {
        disableAndHide("source");
        sourceWarehouse = sourceWarehouseBox.getSelectionModel().getSelectedItem();
        try{
            List<Integer> warehouseIDList = new ArrayList<>();
            String callableSQL = "{call queryWarehousesExcept(?)}";
            Connection c = cf.getConnection();
            CallableStatement cs = c.prepareCall(callableSQL);
            cs.setInt(1, sourceWarehouse);

            ResultSet rs = cs.executeQuery();

            while(rs.next()){
                warehouseIDList.add(rs.getInt(1));
            }
            ObservableList<Integer> options = FXCollections.observableArrayList(warehouseIDList);
            destWarehouseBox.setItems(options);
            destWarehouseBox.setDisable(false);
            rs.close();
            c.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDestinationWarehouse(ActionEvent event) {
        disableAndHide("dest");
        destinationWarehouse = destWarehouseBox.getSelectionModel().getSelectedItem();
        try{
            List<Integer> productIDList = new ArrayList<>();
            String callableSQL = "{call queryProductsAtWarehouse(?)}";
            Connection c = cf.getConnection();
            CallableStatement cs = c.prepareCall(callableSQL);
            cs.setInt(1, destinationWarehouse);
            ResultSet rs = cs.executeQuery();

            while(rs.next()){
                productIDList.add(rs.getInt(1));
            }
            ObservableList<Integer> options = FXCollections.observableArrayList(productIDList);
            productsAtCurrentWarehouse.setItems(options);
            productsAtCurrentWarehouse.setDisable(false);
            rs.close();
            c.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getSourceWarehouse() {
        return this.sourceWarehouse;
    }

    public int getDestinationWarehouse() {
        return this.destinationWarehouse;
    }

    public int getProductID() {
        return this.productID;
    }
    public int getQuantity() {
        return this.quantity;
    }

    @FXML
    private void handleProductAvailable(ActionEvent event) {
        disableAndHide("product");
        productID = productsAtCurrentWarehouse.getSelectionModel().getSelectedItem();
        try{
            String callableSQL = "{call queryProductQuantity(?,?)}";
            Connection c = cf.getConnection();
            CallableStatement cs = c.prepareCall(callableSQL);
            cs.setInt(1,productID);
            cs.setInt(2,sourceWarehouse);
            ResultSet rs = cs.executeQuery();
            rs.next();
            
            currentStockDisplay.setText(String.valueOf(rs.getInt(1)));
            stockDisplay.setVisible(true);
            rs.close();
            c.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSourceClicked(MouseEvent event) {
        try{
            List<Integer> warehouseIDList = new ArrayList<>();
            Connection c = cf.getConnection();
            
            Statement stmt = c.createStatement();
            String query = "SELECT Warehouse.warehouseid, Warehouse.address FROM Warehouse " +
                "ORDER BY Warehouse.warehouseid ASC;";

            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                warehouseIDList.add(rs.getInt(1));
            }
            ObservableList<Integer> options = FXCollections.observableArrayList(warehouseIDList);
            sourceWarehouseBox.setItems(options);

            rs.close();
            c.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void handleDestClicked(MouseEvent event) {
        try{
            List<Integer> warehouseIDList = new ArrayList<>();
            String callableSQL = "SELECT w1.warehouseid, w1.address FROM Warehouse w1 EXCEPT SELECT w2.warehouseID,w2.address FROM Warehouse w2 WHERE w2.warehouseID = ? ORDER BY warehouseid ASC;";
            Connection c = cf.getConnection();
            PreparedStatement ps = c.prepareStatement(callableSQL);
            ps.setInt(1,sourceWarehouse);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                warehouseIDList.add(rs.getInt(1));
            }
            ObservableList<Integer> options = FXCollections.observableArrayList(warehouseIDList);
            destWarehouseBox.setItems(options);

            rs.close();
            c.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void disableAndHide(String disable){
        if (disable.equals("source")) {
            productsAtCurrentWarehouse.getSelectionModel().clearSelection();
            productsAtCurrentWarehouse.setDisable(true);
            stockDisplay.setVisible(false);
            currentStockDisplay.clear();
            transferQuantity.clear();
        } else if (disable.equals("dest")) {
            productsAtCurrentWarehouse.getSelectionModel().clearSelection();
            productsAtCurrentWarehouse.setDisable(true);
            stockDisplay.setVisible(false);
            currentStockDisplay.clear();
            transferQuantity.clear();
        } else if (disable.equals("product")) {
            transferQuantity.clear();
        }
    }

    public boolean someBoxesEmpty() {
        if(sourceWarehouseBox.getSelectionModel().isEmpty()
                || destWarehouseBox.getSelectionModel().isEmpty()
                || productsAtCurrentWarehouse.getSelectionModel().isEmpty()
                || transferQuantity.getText() == null 
                || transferQuantity.getText().trim().isEmpty()) {
            return true;
        }
        return false;
    }
    private void clearAllBoxes(){
        sourceWarehouseBox.getSelectionModel().clearSelection();
        destWarehouseBox.getSelectionModel().clearSelection();
        productsAtCurrentWarehouse.getSelectionModel().clearSelection();
        currentStockDisplay.clear();
        transferQuantity.clear();
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
