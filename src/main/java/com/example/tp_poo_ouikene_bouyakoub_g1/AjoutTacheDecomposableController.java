package com.example.tp_poo_ouikene_bouyakoub_g1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class AjoutTacheDecomposableController {

    @FXML
    private ChoiceBox<String> Categories;

    @FXML
    private ToggleGroup Priorite;

    @FXML
    private DatePicker deadline;

    @FXML
    private TextField dureeTacheHeures;

    @FXML
    private TextField dureeTacheMinutes;

    @FXML
    private RadioButton higt;

    @FXML
    private RadioButton low;

    @FXML
    private RadioButton medium;

    @FXML
    private Text message;

    @FXML
    private TextField nomTache;

    private Stage stage;

    private Scene scene;

    private Parent root;

    Utilisateur user;
    DesktopPlanner app;

    public void setApp(DesktopPlanner app) {
        this.app = app;
    }

    public void setUser(Utilisateur user){
        this.user=user;
        for (Categorie c: user.getTreeCategories())
        {
            Categories.getItems().add(c.getCategorie());
        }
    }

    public boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean timeValidator (int number){
        if (number>=0){
            return true;
        }
        return false;
    }

    @FXML
    void ajoutTacheDecomposable(ActionEvent event) {
        if (nomTache.getText().isEmpty() || dureeTacheHeures.getText().isEmpty()|| dureeTacheMinutes.getText().isEmpty() || deadline.getValue()==null){
            message.setFill(Color.RED);
            message.setText("Veuillez remplir tous les champs qui contiennet (*) du formulaire");
        }
        else
        {
            if (!isNumber(dureeTacheHeures.getText()) || !isNumber(dureeTacheMinutes.getText())){
                message.setText("Vous devez choisir des horaires valides.");
                message.setFill(Color.RED);
            }
            else {
                if (!timeValidator(Integer.parseInt(dureeTacheHeures.getText())) || !timeValidator(Integer.parseInt(dureeTacheMinutes.getText()))) {
                    message.setText("Vous devez choisir des horaires valides.");
                    message.setFill(Color.RED);
                } else {
                    Categorie c=null;
                    for (Categorie cat: user.getTreeCategories())
                    {
                        if(cat.getCategorie()==Categories.getValue())
                        {
                            c=cat;
                        }
                    }

                    if(higt.isSelected())
                    {
                        TacheDecomposable tache=new TacheDecomposable(nomTache.getText(),Integer.parseInt(dureeTacheHeures.getText()),Integer.parseInt(dureeTacheMinutes.getText()), com.example.tp_poo_ouikene_bouyakoub_g1.Priorite.HIGH,deadline.getValue().getDayOfMonth(),deadline.getValue().getMonthValue(),deadline.getValue().getYear(),c,EtatDeRealisation.NOTREALIZED);
                        user.ajouterTache(tache);
                        message.setFill(Color.GREEN);
                        message.setText("Ajout effectué avec succès !");
                    }
                    else if(low.isSelected()) {
                        TacheDecomposable tache=new TacheDecomposable(nomTache.getText(),Integer.parseInt(dureeTacheHeures.getText()),Integer.parseInt(dureeTacheMinutes.getText()), com.example.tp_poo_ouikene_bouyakoub_g1.Priorite.LOW,deadline.getValue().getDayOfMonth(),deadline.getValue().getMonthValue(),deadline.getValue().getYear(),c,EtatDeRealisation.NOTREALIZED);
                        user.ajouterTache(tache);
                        message.setFill(Color.GREEN);
                        message.setText("Ajout effectué avec succès !");

                    }
                    else {
                        TacheDecomposable tache=new TacheDecomposable(nomTache.getText(),Integer.parseInt(dureeTacheHeures.getText()),Integer.parseInt(dureeTacheMinutes.getText()), com.example.tp_poo_ouikene_bouyakoub_g1.Priorite.MEDIUM,deadline.getValue().getDayOfMonth(),deadline.getValue().getMonthValue(),deadline.getValue().getYear(),c,EtatDeRealisation.NOTREALIZED);
                        user.ajouterTache(tache);
                        message.setFill(Color.GREEN);
                        message.setText("Ajout effectué avec succès !");
                        tache.afficher();
                    }
                }
            }
        }
    }

    @FXML
    void revenir(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Taches.fxml"));
        Parent root = loader.load();
        ListeTachesController listeTachesController = loader.getController();
        listeTachesController.setUser(user);
        listeTachesController.setApp(app);
//        CreerCompteController controllerCreerCompte = loader.getController();
//        controllerCreerCompte.setApp(app);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //centerWindow();
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

    public void seDeconnecter(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        root = loader.load();
        LoginPageController logControl = loader.getController();
        logControl.setDesktopPlanner(this.app);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        centerWindow();
        stage.show();
    }


    public void mesProjetsPage (ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MesProjets.fxml"));
        root = loader.load();
        MesProjetsController projController = loader.getController();
        projController.setApp(this.app);
        projController.setUser(this.user);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCategorie(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MesCategories.fxml"));
        root = loader.load();
        MesCategoriesController categoriesController = loader.getController();
        categoriesController.setApp(this.app);
        categoriesController.setUser(this.user);
        categoriesController.initializePieChart();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToPlanifierManuellement(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PlanifierManuellement.fxml"));
        root = loader.load();
        PlanifierManuellementController categoriesController = loader.getController();
        categoriesController.setApp(this.app);
        categoriesController.setUser(this.user);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreneaux(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MesCreneaux.fxml"));
        root = loader.load();
        MesCreneauxController mesCreneauxController = loader.getController();
        mesCreneauxController.setApp(this.app);
        mesCreneauxController.setUser(this.user);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRendements(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MesRendements.fxml"));
        root = loader.load();
        MesRendementsController mesCreneauxController = loader.getController();
        mesCreneauxController.setApp(this.app);
        mesCreneauxController.setUtilisateur(this.user);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToPlannings(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Calendrier.fxml"));
        root = loader.load();
        CalendrierController calendrierController = loader.getController();
        calendrierController .setApp(this.app);
        calendrierController .setUser(this.user);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMyTasks(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Taches.fxml"));
        root = loader.load();
        ListeTachesController mesCreneauxController = loader.getController();
        mesCreneauxController.setApp(this.app);
        mesCreneauxController.setUser(this.user);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void accueilPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Accueil.fxml"));
        root = loader.load();
        AccueilController accueilController = loader.getController();
        accueilController.setApp(this.app);
        accueilController.setUtilisateur(this.user);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
