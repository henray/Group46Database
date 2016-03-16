/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import comp421.group46.model.TeamsHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    TeamsHandler teams = new TeamsHandler();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> options = FXCollections.observableArrayList(teams.getTeams());
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
