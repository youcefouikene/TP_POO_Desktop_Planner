package com.example.tp_poo_ouikene_bouyakoub_g1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class AjouterCategorieController {

    @FXML
    private TextField name;

    @FXML
    private Text message;

    @FXML
    private ColorPicker color;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Utilisateur user;

    private DesktopPlanner app;

    public void setUser(Utilisateur user){
        this.user = user;
    }

    public void setApp(DesktopPlanner app){
        this.app = app;
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

    public void ajouter(){
        Color selectedColor = color.getValue();
        int red = (int) (selectedColor.getRed() * 255);
        int green = (int) (selectedColor.getGreen() * 255);
        int blue = (int) (selectedColor.getBlue() * 255);
        String colorInHex = String.format("#%02X%02X%02X", red, green, blue);
        if(name.getText().isEmpty()){
            message.setFill(Color.RED);
            message.setText("Veuillez saisir le nom de la catégorie");
        }
        else {
            if (user.existanceCategorie(name.getText())){
                message.setFill(Color.RED);
                message.setText("Catégorie déjà existante");
            }
            else {
//                if (user.existanceCouleur(colorInHex)){
//                    message.setFill(Color.RED);
//                    message.setText("Couleur déjà attribué à une catégorie");
//                }
//                else {
//                    message.setFill(Color.GREEN);
//                    message.setText("Catégorie ajoutée avec succès");
//                    user.ajouterCategorie(name.getText(),colorInHex);
//                }
                message.setFill(Color.GREEN);
                message.setText("Catégorie ajoutée avec succès");
                user.ajouterCategorie(name.getText(),colorInHex);
            }
        }
    }

    public void naviagteToMyCategories(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MesCategories.fxml"));
        root = loader.load();
        MesCategoriesController projController = loader.getController();
        projController.setApp(this.app);
        projController.setUser(this.user);
        projController.initializePieChart();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        centerWindow();
        stage.show();
    }
}
