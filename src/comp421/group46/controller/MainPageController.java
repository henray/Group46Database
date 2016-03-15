/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import comp421.group46.controller.TeamQueryController;
import comp421.group46.model.TeamQueryResult;
import comp421.group46.model.Teams;
import constants.Descriptions;
import constants.Paths;
import constants.PopupType;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.*;
import java.util.Properties;
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
    private TableView<TeamQueryResult> table;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @FXML
    private void handleTeamQuery(ActionEvent event) {
        TeamQueryController x = (TeamQueryController)openPopupMenu(PopupType.TEAM_QUERY,"Team Query", Paths.TEAM_QUERY_FXML, Descriptions.TEAM_QUERY_DESCRIPTION);
        String teamName = x.getTeamName();
        String callableSQL = "{call retrieveRevenue(?)}";
        Connection c = null;
        CallableStatement stmt = null;
        try{
            c = getConnection();
            stmt = c.prepareCall(callableSQL);
            stmt.setString(1,teamName);
            ResultSet rs = stmt.executeQuery();
            ObservableList<TeamQueryResult> data = FXCollections.observableArrayList();
            TableColumn names = new TableColumn("Team Name");
            names.setCellValueFactory(new PropertyValueFactory<TeamQueryResult,String>("teamName"));
            TableColumn revenues = new TableColumn("Revenue");
            revenues.setCellValueFactory(new PropertyValueFactory<TeamQueryResult,Integer>("revenue"));
            while(rs.next()){
                data.add(new TeamQueryResult(rs.getString(1),rs.getInt(2)));
            }
            stmt.close();
            c.close();
            table.getColumns().clear();
            table.setItems(data);
            table.getColumns().addAll(names,revenues);
            updateQueryTitle("Retrieved all revenues from: "+ teamName);
        } catch (SQLException e){
            System.out.println("problem finding driver");
            e.printStackTrace();
        }
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
//                case PLAYER_QUERY:
//                    control = (PlayerQueryController) control;
//                    break;
                default:
                    control = null;
                    break;
            }
            if(control != null) control.setDescription(description);
            else System.exit(1);
            testSuitePopup.setScene(popUpScene);
            testSuitePopup.showAndWait();
        } catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        return control;
    }
    
    private Connection getConnection() throws SQLException {
        DriverManager.registerDriver ( new org.postgresql.Driver() ) ;
        String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        return DriverManager.getConnection (url,"cs421g46", "*Group_46*") ;
    }
    
    private void updateQueryTitle(String title){
        queryTitle.setText(title);
    }
}
