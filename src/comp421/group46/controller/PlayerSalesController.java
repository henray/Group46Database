/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import comp421.group46.model.resulthelpers.PlayerQueryResult;
import comp421.group46.model.TeamsHandler;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    TeamsHandler teams = new TeamsHandler();
    @FXML
    private TableView<PlayerQueryResult> tableView;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        teams.removeTeam("NBA");
        ObservableList<String> options = FXCollections.observableArrayList(teams.getTeams());
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
            Connection c = getConnection();
            String callableSQL = "{call queryplayers(?)}";
            CallableStatement stmt = c.prepareCall(callableSQL);
            
            stmt.setString(1, getTeamName());
            ResultSet rs = stmt.executeQuery();
            
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
            stmt.close();
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
    private Connection getConnection() throws SQLException {
        DriverManager.registerDriver ( new org.postgresql.Driver() ) ;
        String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        return DriverManager.getConnection (url,"cs421g46", "*Group_46*") ;
    }
}
