package com.example.tp_poo_ouikene_bouyakoub_g1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class AjouterProjetController {

    private Utilisateur user;

    private DesktopPlanner app;

    private Stage stage;
    private Scene scene;
    private Parent root;


    public void setUser(Utilisateur user){
        this.user = user;
    }

    public void setApp(DesktopPlanner app){
        this.app = app;
    }

    @FXML
    private TextField name;

    @FXML
    private TextField description;

    @FXML
    private Text message;

    public void ajouter(){
        if (name.getText().isEmpty() || description.getText().isEmpty()){
            message.setFill(Color.RED);
            message.setText("Veuillez remplir tous les champs.");
        }
        else {
            if (user.projetExistant(name.getText())){
                message.setFill(Color.RED);
                message.setText("Il existe un projet avec ce nom.");
            }
            else{
                message.setFill(Color.GREEN);
                message.setText("Projet ajouté avec succès.");
                Projet p = new Projet(name.getText(),description.getText());
                user.ajouterProjet(p);
            }
        }
    }

    public void navigateToMyProjects(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MesProjets.fxml"));
        root = loader.load();
        MesProjetsController projController = loader.getController();
        projController.setApp(this.app);
        projController.setUser(this.user);
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

}
