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

public class CreerCompteController {

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField passcheck;

    @FXML
    private TextField pseudo;

    @FXML
    private Text errormsg;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private DesktopPlanner app;

    public void setApp(DesktopPlanner app){
        this.app = app;
    }

    public void switchToLoginPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        root = loader.load();
        LoginPageController logInController = loader.getController();
        logInController.setDesktopPlanner(this.app);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        centerWindow();
        stage.show();
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


    public void submit(ActionEvent event) {
        if (password.getText().isEmpty() || passcheck.getText().isEmpty() || pseudo.getText().isEmpty()) {
            errormsg.setFill(Color.RED);
            errormsg.setText("Veuillez remplir tous les champs du formulaire");
        } else {
            if (!password.getText().equals(passcheck.getText())) {
                errormsg.setFill(Color.RED);
                errormsg.setText("Incohérence entre les mots de passe");
            } else {
                if (app.compteExistant(pseudo.getText())) {
                    errormsg.setFill(Color.RED);
                    errormsg.setText("Compte déjà existant!");
                } else {
                    app.ajouterUtilisateur(pseudo.getText(), password.getText());
                    errormsg.setFill(Color.GREEN);
                    errormsg.setText("Votre compte a été crée");
                }
            }
        }
    }



}
