/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import comp421.group46.model.ConnectionFactory;
import comp421.group46.model.resulthelpers.PlayerQueryResult;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Katie Lin
 */
public class PlayerSalesController implements Controller, Initializable {

    @FXML
    private ComboBox<String> teamOptionsBox;
    @FXML
    private TextArea description;
    @FXML
    private TableView<PlayerQueryResult> tableView;
    private ConnectionFactory cf = ConnectionFactory.getConnectionFactory();

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         List<String> teams = new ArrayList<>();
        try{
            String callableSQL = "SELECT teamName FROM Team ORDER BY teamName ASC";
            Connection c = cf.getConnection();

            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(callableSQL);
            while(rs.next()){
                teams.add(rs.getString(1));
            }
            c.close();
            rs.close();
            s.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        ObservableList<String> options = FXCollections.observableArrayList(teams);
        teamOptionsBox.setItems(options);
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).hide();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).close();
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleTeamChoice(ActionEvent event) {
        try{
            Connection c = cf.getConnection();
            String callableSQL = "SELECT playernumber, firstname || ' ' || lastname FROM player WHERE teamname = ? ORDER BY playernumber";
            
            PreparedStatement ps = c.prepareStatement(callableSQL);
            ps.setString(1, getTeamName());

            ResultSet rs = ps.executeQuery();
            
            ObservableList<PlayerQueryResult> data = FXCollections.observableArrayList();
            TableColumn names = new TableColumn("Player Name");
            names.setCellValueFactory(new PropertyValueFactory<>("playerName"));
            TableColumn jerseyNumbers = new TableColumn("Jersey Number");
            jerseyNumbers.setCellValueFactory(new PropertyValueFactory<>("jerseyNumber"));
            
            while(rs.next()){
                data.add(new PlayerQueryResult(rs.getString(2),rs.getInt(1)));
            }
            
            tableView.getColumns().clear();
            tableView.setItems(data);
            tableView.getColumns().addAll(names,jerseyNumbers);
            
            c.close();
            ps.close();
            rs.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public void setDescription(String description){
        this.description.setText(description);
    }
    public String getTeamName(){
        return teamOptionsBox.getSelectionModel().getSelectedItem();
    }
    public int getJerseyNumber(){
        return tableView.getSelectionModel().getSelectedItem().getJerseyNumber();
    }
    public String getPlayerName(){
        return tableView.getSelectionModel().getSelectedItem().getPlayerName();
    }
}
