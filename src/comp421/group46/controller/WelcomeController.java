/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.controller;

import constants.Paths;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Katie Lin
 */
public class WelcomeController implements Initializable {

    @FXML
    private Text statusText;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void openHenry(MouseEvent event) {
        open("http://www.github.com/henray");
    }

    @FXML
    private void openHarvey(MouseEvent event) {
        open("http://www.github.com/hysoftwareeng");
    }

    @FXML
    private void openKelley(MouseEvent event) {
        open("http://www.github.com/kelzha");
    }

    @FXML
    private void openYiwei(MouseEvent event) {
        open("http://www.github.com/dolphinpink");
    }
    
    private void open(String link) {
        try {
            Desktop.getDesktop().browse(new URI(link));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch(URISyntaxException e2){
            e2.printStackTrace();
        }
    }
    
    
    private void updateStatus(String msg){
        statusText.setText(msg);
    }

    @FXML
    private void handleEnter(ActionEvent event) {
        updateStatus("Our GUI sucks so it takes forever to load!!");
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        PauseTransition pause = new PauseTransition(
                Duration.seconds(0)
        );
        pause.setOnFinished((ActionEvent event1) -> {
            Parent root = null;
            try{
                root = FXMLLoader.load(getClass().getResource(Paths.MAIN_FXML));
            } catch(IOException e){
            }
            Scene scene = new Scene(root);
            stage.setTitle("NBA store - Main Page");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.centerOnScreen();
        });
        pause.play();
    }
}