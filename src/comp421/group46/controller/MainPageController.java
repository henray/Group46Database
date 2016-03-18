/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import comp421.group46.model.ConnectionFactory;
import comp421.group46.model.DialogFactory;
import comp421.group46.model.PlaceOrderResult;
import comp421.group46.model.PlayerSaleResult;
import comp421.group46.model.ProductsPurchasedResult;
import comp421.group46.model.RevenueResult;
import comp421.group46.model.QueryResult;
import comp421.group46.model.TransferWarehouseResult;
import constants.Descriptions;
import constants.Paths;
import constants.PopupType;
import constants.SpecificText;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Katie Lin
 */
public class MainPageController implements Initializable {

    @FXML
    private MenuBar menuBar;
    @FXML
    private Text queryTitle;
    @FXML
    private TableView<QueryResult> table;
    private final DialogFactory df = DialogFactory.getDialogFactory();
    private final ConnectionFactory cf = ConnectionFactory.getConnectionFactory();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateQueryTitle(SpecificText.DEFAULT_TITLE);
    }
 
    private Controller openPopupMenu(PopupType type, String title, String fxmlPath, String description){
        final Stage testSuitePopup = new Stage();
        testSuitePopup.initModality(Modality.APPLICATION_MODAL);
        testSuitePopup.setTitle(title);
        testSuitePopup.initOwner((Stage) menuBar.getScene().getWindow());
        testSuitePopup.getIcons().add(new Image(Paths.FIGHTER_JET_LOGO));
        testSuitePopup.centerOnScreen();
        Controller control = null;
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene popUpScene = new Scene(loader.load());
            control = loader.getController();
            switch(type){
                case TEAM_QUERY:
                    control = (TeamQueryController) control;
                    break;
                case PLAYER_SALES:
                    control = (PlayerSalesController) control;
                    break;
                case PRODUCTS_PURCHASED:
                    control = (ProductsPurchasedController) control;
                    break;
                case PLACE_ORDER:
                    control = (PlaceOrderController) control;
                    break;
                case TRANSFER_WAREHOUSE:
                    control = (TransferWarehouseController) control;
                    break;
                case CHANGE_PRICE:
                    control = (ChangePriceController) control;
                    break;
                default:
                    control = null;
                    break;
            }
            if(control != null) control.setDescription(description);
            else System.exit(1);
            testSuitePopup.setScene(popUpScene);
            testSuitePopup.showAndWait();
        } catch(IOException e){
            System.exit(1);
        }
        return control;
    }
    
    private void updateQueryTitle(String title){
        queryTitle.setText(title);
    }

    @FXML
    private void handleClearTable(ActionEvent event) {
        table.getColumns().clear();
        queryTitle.setText(SpecificText.DEFAULT_TITLE);
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }
    
    @FXML
    private void handleRevenue(ActionEvent event) {
        TeamQueryController x = (TeamQueryController)openPopupMenu(PopupType.TEAM_QUERY,"Team Query", Paths.REVENUE_FXML, Descriptions.TEAM_QUERY);
        try{
            Connection c = cf.getConnection();
            String callableSQL = "{call retrieveRevenue(?)}";
            CallableStatement cs = c.prepareCall(callableSQL);
            
            String teamName = x.getTeamName();
            if (teamName == null) return;
            if (teamName.equals("All Teams")) teamName = "NBA";
            cs.setString(1,teamName);
            
            ResultSet rs = cs.executeQuery();
            
            ObservableList<QueryResult> data = FXCollections.observableArrayList();
            TableColumn names = new TableColumn("Team Name");
            names.setCellValueFactory(new PropertyValueFactory<>("teamName"));
            TableColumn revenues = new TableColumn("Revenue");
            revenues.setCellValueFactory(new PropertyValueFactory<>("revenue"));
            
            while(rs.next()){
                data.add(new RevenueResult(rs.getString(1),rs.getInt(2)));
            }
            
            table.getColumns().clear();
            table.setItems(data);
            table.getColumns().addAll(names,revenues);
            if(teamName.equals("All Teams")) updateQueryTitle(SpecificText.TEAM_QUERY_ALL);
            else updateQueryTitle(SpecificText.TEAM_QUERY_ONE+teamName);
            
            cs.close();
            c.close();
        } catch (SQLException e2) {
            System.out.println("Error Code: "+e2.getErrorCode());
            System.out.println("Error message: "+e2.getMessage());
        }
    }
    
    @FXML
    private void handlePlayerSales(ActionEvent event) {
        PlayerSalesController x = (PlayerSalesController)openPopupMenu(PopupType.PLAYER_SALES,"Player Sales", Paths.PLAYER_SALES_FXML, Descriptions.PLAYER_SALES);
        String teamName = x.getTeamName();
        try{
            Connection c = cf.getConnection();
            String callableSQL = "{call playersalecount(?,?)}";
            CallableStatement cs = c.prepareCall(callableSQL);
            cs.setInt(1, x.getJerseyNumber());
            cs.setString(2, teamName);
            
            ResultSet rs = cs.executeQuery();
            
            ObservableList<QueryResult> data = FXCollections.observableArrayList();
            TableColumn numCustomers = new TableColumn("Number of Customers");
            numCustomers.setCellValueFactory(new PropertyValueFactory<>("numCustomers"));
            
            while(rs.next()){
                data.add(new PlayerSaleResult(rs.getInt(1)));
            }
            
            table.getColumns().clear();
            table.setItems(data);
            table.getColumns().addAll(numCustomers);
            updateQueryTitle(SpecificText.PLAYER_SALES_P1+x.getPlayerName()+" #"
                    +x.getJerseyNumber()+SpecificText.PLAYER_SALES_P2+x.getTeamName());
            
            cs.close();
            c.close();
        } catch(NullPointerException e){
            
        } catch (SQLException e2) {
            System.out.println("Error Code: "+e2.getErrorCode());
            System.out.println("Error message: "+e2.getMessage());
        }

    }
    
    @FXML
    private void handleProductsPurchased(ActionEvent event) {
         ProductsPurchasedController x = (ProductsPurchasedController)openPopupMenu(PopupType.PRODUCTS_PURCHASED,"Products Purchased", Paths.PRODUCTS_PURCHASED_FXML, Descriptions.PRODUCTS_PURCHASED);
        try{
            Connection c = cf.getConnection();
            String callableSQL = "{call customersFromProduct(?)}";
            CallableStatement cs = c.prepareCall(callableSQL);
            int productID = x.getProductID();
            cs.setInt(1,productID);
            
            ResultSet rs = cs.executeQuery();
            
            ObservableList<QueryResult> data = FXCollections.observableArrayList();
            TableColumn customerID = new TableColumn("Customer ID");
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            TableColumn firstName = new TableColumn("First Name");
            firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            TableColumn lastName = new TableColumn("Last Name");
            lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            TableColumn quantity = new TableColumn("Quantity Purchased");
            quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            
            while(rs.next()){
                data.add(new ProductsPurchasedResult(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4)));
            }
            
            table.getColumns().clear();
            table.setItems(data);
            table.getColumns().addAll(customerID,firstName,lastName,quantity);
            updateQueryTitle(SpecificText.PRODUCTS_PURCHASED+": "+x.getProductID());
            
            cs.close();
            c.close();
        } catch(NumberFormatException e1){
            System.out.println(e1.getMessage());
        } catch (SQLException e2) {
            df.popupError("Product does not exist","SQLException caught.\nThe action could not be completed.","Code: "+e2.getErrorCode()+"\n"+e2.getMessage());
        }
    }
    @FXML
    private void handlePlaceOrder(ActionEvent event) {
        PlaceOrderController x = (PlaceOrderController)openPopupMenu(PopupType.PLACE_ORDER,"Place Order", Paths.PLACE_ORDER_FXML, Descriptions.PLACE_ORDER);
        try{
            Connection c = cf.getConnection();
            String callableSQL = "{call placeAnOrder(?,?,?,?,?)}";
            CallableStatement cs = c.prepareCall(callableSQL);
            
            
            
            cs.close();
            c.close();
        } catch(NumberFormatException e){
            
        } catch (SQLException e2) {
            System.out.println("Error Code: "+e2.getErrorCode());
            System.out.println("Error message: "+e2.getMessage());
        }
    }

    @FXML
    private void handleTransferWarehouses(ActionEvent event) {
        TransferWarehouseController x = (TransferWarehouseController)openPopupMenu(PopupType.TRANSFER_WAREHOUSE,"Transfer Warehouse", Paths.TRANSFER_WAREHOUSE_FXML, Descriptions.TRANSFER_WAREHOUSE);
        if(x.someBoxesEmpty() || x.getQuantity() == 0) return;
        try{
            Connection c = cf.getConnection();
            String callableSQL = "{call moveWarehouses(?,?,?,?)}";
            CallableStatement cs = c.prepareCall(callableSQL);
            cs.setInt(1, x.getSourceWarehouse());
            cs.setInt(2, x.getDestinationWarehouse());
            cs.setInt(3, x.getProductID());
            cs.setInt(4, x.getQuantity());
            
            ObservableList<QueryResult> data = FXCollections.observableArrayList();
            TableColumn bool = new TableColumn("Move Status");
            bool.setCellValueFactory(new PropertyValueFactory<>("isMoved"));
            
            ResultSet rs = cs.executeQuery();
            rs.next();
            data.add(new TransferWarehouseResult(rs.getBoolean(1)));
            
            table.getColumns().clear();
            table.setItems(data);
            table.getColumns().addAll(bool);
            updateQueryTitle(SpecificText.TRANSFER_WAREHOUSE_P1+x.getQuantity()+
                    SpecificText.TRANSFER_WAREHOUSE_P2+x.getProductID()+
                    SpecificText.TRANSFER_WAREHOUSE_P3+x.getSourceWarehouse()+
                    SpecificText.TRANSFER_WAREHOUSE_P4+x.getDestinationWarehouse());
            cs.close();
            c.close();
        } catch(NumberFormatException e){
            
        } catch (SQLException e2) {
            df.popupError("Not enough stock","SQLException caught.\nThe action could not be completed.","Code: "+e2.getErrorCode()+"\n"+e2.getMessage());
        }
    }

    @FXML
    private void handleChangePrices(ActionEvent event) {
        ChangePriceController x = (ChangePriceController)openPopupMenu(PopupType.CHANGE_PRICE,"Change Pricing",Paths.CHANGE_PRICE_FXML,Descriptions.CHANGE_PRICE);
        try{
            Connection c = cf.getConnection();
            String callableSQL = "{call changePrices(?,?)}";
            CallableStatement cs = c.prepareCall(callableSQL);
            
            
            cs.close();
            c.close();
        } catch (SQLException e2) {
            System.out.println("Error Code: "+e2.getErrorCode());
            System.out.println("Error message: "+e2.getMessage());
        }
    }
}
