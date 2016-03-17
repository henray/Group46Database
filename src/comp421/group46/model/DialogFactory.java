/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.model;

import constants.Paths;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Katie Lin
 */
public class DialogFactory {
    private static DialogFactory factory = new DialogFactory();
    private DialogFactory(){
    }
    public static DialogFactory getDialogFactory(){
        return factory;
    }
    public  Optional<ButtonType> popupWarning(String title,String header, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/resources/main.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        ((Stage)dialogPane.getScene().getWindow()).getIcons().add(new Image(Paths.FIGHTER_JET_LOGO));
        return alert.showAndWait();
    }
    public Optional<ButtonType> popupInformation(String title,String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/resources/main.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        ((Stage)dialogPane.getScene().getWindow()).getIcons().add(new Image(Paths.FIGHTER_JET_LOGO));
        return alert.showAndWait();
    }
    public Optional<ButtonType> popupError(String title,String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/resources/main.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        ((Stage)dialogPane.getScene().getWindow()).getIcons().add(new Image(Paths.FIGHTER_JET_LOGO));
        return alert.showAndWait();
    }
}
