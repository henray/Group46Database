/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import comp421.group46.model.ConnectionFactory;
import java.net.URL;
import java.sql.Connection;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Katie Lin
 */
public class TeamQueryController implements Initializable, Controller {

    @FXML
    private ComboBox<String> teamOptionsBox;
    @FXML
    private TextArea teamQueryDescription;
    private ConnectionFactory cf = ConnectionFactory.getConnectionFactory();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> teams = new ArrayList<>();
        teams.add("All Teams");
        try{
            Connection c = cf.getConnection();
            
            Statement stmt = c.createStatement();
            String query = "SELECT Team.teamName FROM Team " +
            "ORDER BY Team.teamName ASC";         
            
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()){
                teams.add(rs.getString(1));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        ObservableList<String> options = FXCollections.observableArrayList(teams);
        teamOptionsBox.setItems(options);
    }    

    @FXML
    private void handleSuiteBoxChoice(ActionEvent event) {
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).hide();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        ((Stage) ((Node)event.getSource()).getScene().getWindow()).close();
        teamOptionsBox.getSelectionModel().clearSelection();
    }
    
    public void setDescription(String description){
        this.teamQueryDescription.setText(description);
    }
    public String getTeamName(){
        return teamOptionsBox.getSelectionModel().getSelectedItem();
    }

}
