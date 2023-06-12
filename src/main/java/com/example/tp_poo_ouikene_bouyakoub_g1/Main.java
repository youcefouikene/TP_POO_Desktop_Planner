package com.example.tp_poo_ouikene_bouyakoub_g1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

import java.time.*;

import java.util.*;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        DesktopPlanner desktopPlanner = new DesktopPlanner();


        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginPage.fxml"));
        desktopPlanner.afficherUtilisateurs();
        Parent root = fxmlLoader.load();
        LoginPageController controller = fxmlLoader.getController();
        controller.setDesktopPlanner(desktopPlanner);
        Scene scene = new Scene(root);
        stage.setTitle("DesktopPlanner");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> exit(stage, desktopPlanner, event));
    }

    public static void main(String[] args) {
        launch();
    }

    public void exit(Stage stage, DesktopPlanner app, WindowEvent event) {
        app.sauvegarderInfosUtilisateurs();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Voulez-vous vraiment fermer l'application DesktopPlanner");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            event.consume(); // Consume the event to prevent further processing
        } else {
            stage.close();
        }
    }

}
