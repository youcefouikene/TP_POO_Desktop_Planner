package com.example.tp_poo_ouikene_bouyakoub_g1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;

public class GestionCategoriesController {

    @FXML
    private TextField name;

    @FXML
    private ChoiceBox<String> choice;

    @FXML
    private ColorPicker color;

    @FXML
    private Text time;

    @FXML
    private Text message;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public DesktopPlanner app;

    public Utilisateur user;

    public void setUser(Utilisateur user){
        this.user = user;
        initializeCateogries();
    }

    private void initializeCateogries() {
        choice.getItems().clear();
        if (user != null) {
            HashSet<String> myCategories = new HashSet<>();
            for (Categorie p : user.getListeCategories()) {
                myCategories.add(p.getCategorie());
            }
            choice.getItems().addAll(myCategories);
        }
    }

    public void setApp(DesktopPlanner app){
        this.app = app;
    }

    private Categorie c;

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

    public void revenir(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MesCategories.fxml"));
        root = loader.load();
        MesCategoriesController categoriesController = loader.getController();
        categoriesController.setApp(this.app);
        categoriesController.setUser(this.user);
        categoriesController.initializePieChart();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        centerWindow();
        stage.show();
    }

    public void remplirChamps(){
        String categorieName = choice.getValue();
        message.setText(null);
        if (categorieName != null){
            c = user.getCategorie(categorieName);
            if (c!= null){
                name.setText(c.getCategorie());
                time.setText(String.valueOf(this.user.calculerTempsPasse(c).toMinutes()+" Minutes"));
                color.setValue(Color.web(c.getColor()));
            }
            else {
                System.out.println("Error!");
            }
        }
    }

    public void reset(){
        name.setText(c.getCategorie());
        time.setText(String.valueOf(this.user.calculerTempsPasse(c).toMinutes()+" Minutes"));
        color.setValue(Color.web(c.getColor()));
    }

    public void modifierInfos(){
        if (name.getText().isEmpty()){
            message.setText("Veuillez remplir tout le formulaire avant d'appliquer des modifications au projet.");
            message.setFill(Color.RED);
        }
        else {
            c.setCategorie(name.getText());
            Color selectedColor = color.getValue();
            int red = (int) (selectedColor.getRed() * 255);
            int green = (int) (selectedColor.getGreen() * 255);
            int blue = (int) (selectedColor.getBlue() * 255);
            c.setColor(String.format("#%02X%02X%02X", red, green, blue));
            time.setText(String.valueOf(this.user.calculerTempsPasse(c).toMinutes()+" Minutes"));
            name.setText(c.getCategorie());
            color.setValue(Color.web(c.getColor()));
            int selectedIndex = choice.getSelectionModel().getSelectedIndex();
            choice.getItems().set(selectedIndex,c.getCategorie());
            choice.setValue(name.getText());
            message.setText("Modification effectuée avec succès");
            message.setFill(Color.GREEN);
        }
    }

    public void supprimer(){
        String categorieName = choice.getValue();
        if (categorieName  != null) {
            Categorie categorie = user.getCategorie(categorieName);
            if (categorie != null && categorie == (c)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmer la suppression");
                alert.setContentText("Voulez-vous vraiment supprimer la catégorie " + categorieName + " ?"+" Toutes les tâches associées à cette catégorie seront également supprimées");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    user.supprimerCategorie(categorie);
                    choice.getItems().remove(categorieName);
                    choice.setValue(null);
                    name.clear();
                    color.setValue(Color.web("#0000FF"));
                    message.setText("Suppression effectuée avec succès");
                    message.setFill(Color.GREEN);
                }
            } else {
                System.out.println("Error!");
            }
        }
    }


}
