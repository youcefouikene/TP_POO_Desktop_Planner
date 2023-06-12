package com.example.tp_poo_ouikene_bouyakoub_g1;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {

    @FXML
    private TextField pseudo;

    @FXML
    private PasswordField password;

    @FXML
    private Text message;

    private Scene scene;
    private Parent root;

    private Stage stage;


    private DesktopPlanner app;

    public void setDesktopPlanner(DesktopPlanner app){
        this.app = app;
    }



    public void switchToCreateAccount(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreerCompte.fxml"));
        root = loader.load();
        CreerCompteController controllerCreerCompte = loader.getController();
        controllerCreerCompte.setApp(app);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        centerWindow();
        stage.show();
    }

    public void seConnecter(ActionEvent event) throws IOException {

        if (pseudo.getText().isEmpty() || password.getText().isEmpty()){
            message.setFill(Color.RED);
            message.setText("Veuillez remplir tous les champs du formulaire");
        }
        else {

            if (!this.app.compteExistant(pseudo.getText())){
                message.setFill(Color.RED);
                message.setText("Compte inexistant!");
            }

            else {
                if (this.app.seConnecter(pseudo.getText(),password.getText())==null){
                    message.setFill(Color.RED);
                    message.setText("Mot de passe incorrect!");
                }

                else{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Accueil.fxml"));
                    root = loader.load();
                    AccueilController controllerAccueil = loader.getController();
                    controllerAccueil.setUtilisateur(this.app.seConnecter(pseudo.getText(),password.getText()));
                    controllerAccueil.setApp(this.app);
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    centerWindow();
                    stage.show();
                }
            }
        }
    }

    private void centerWindow() {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        double windowWidth = stage.getWidth();
        double windowHeight = stage.getHeight();
        double windowX = (screenWidth - windowWidth) / 2;
        double windowY = (screenHeight - windowHeight) / 2;
        stage.setX(windowX);
        stage.setY(windowY);
    }

}
