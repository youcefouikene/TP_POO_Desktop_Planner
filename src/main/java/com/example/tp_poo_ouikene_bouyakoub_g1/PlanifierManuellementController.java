package com.example.tp_poo_ouikene_bouyakoub_g1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.ResourceBundle;

public class PlanifierManuellementController{

    private Scene scene;

    private Stage stage;

    private Parent root;
    
    private DesktopPlanner app;
    private Utilisateur user;


    public void setUser(Utilisateur user){
        this.user = user;
        this.initializeProjects();
        this.initializeCateogries();
    }

    public void setApp(DesktopPlanner app){
        this.app = app;
    }

    @FXML
    private TextField name;

    @FXML
    private ChoiceBox<String> categorie;

    @FXML
    private ChoiceBox<String> projet;

    @FXML
    private DatePicker date;

    @FXML
    private TextField heuredebut;

    @FXML
    private TextField minutesdebut;

    @FXML
    private TextField heurefin;

    @FXML
    private TextField minutesfin;

    @FXML
    private Text message;


    private void initializeProjects() {
        projet.getItems().clear();
        if (user != null) {
            HashSet<String> myProjects = new HashSet<>();
            for (Projet p : user.getListeProjets()) {
                myProjects.add(p.getNom());
            }
            myProjects.add("Aucun");
            projet.getItems().addAll(myProjects);
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

    public boolean hourValidator (int hour){
        if (hour>=0 && hour<=23){
            return true;
        }
        return false;
    }

    public boolean minutesValidator (int minutes){
        if (minutes >=0 && minutes<=59){
            return true;
        }
        return false;
    }

    public void resetLabels() {
        name.setText("");
        categorie.getSelectionModel().clearSelection();
        projet.getSelectionModel().clearSelection();
        date.setValue(null);
        heuredebut.setText(null);
        minutesdebut.setText(null);
        heurefin.setText(null);
        minutesfin.setText(null);
        message.setText("");
    }

    private void initializeCateogries() {
        categorie.getItems().clear();
        if (user != null) {
            HashSet<String> myCategories = new HashSet<>();
            for (Categorie p : user.getListeCategories()) {
                myCategories.add(p.getCategorie());
            }
            myCategories.add("Aucune");
            categorie.getItems().addAll(myCategories);
        }
    }

    public void ajouter(){
        if (name.getText().isEmpty() ||
                categorie.getSelectionModel().isEmpty() ||
                projet.getSelectionModel().isEmpty() ||
                date.getValue() == null ||
                heuredebut.getText().isEmpty() ||
                minutesdebut.getText().isEmpty()  ||
                heurefin.getText().isEmpty()  ||
                minutesfin.getText().isEmpty() ) {
                message.setText("Veuillez remplir tous les champs du formulaire.");
                message.setFill(Color.RED);
        } else {
            int starthour, startminutes, endhour, endminutes;
            if (isNumber(heuredebut.getText())== false || isNumber(heurefin.getText())== false || isNumber(minutesdebut.getText())== false || isNumber(minutesfin.getText())== false){
                message.setText("Vous devez choisir des horaires valides.");
                message.setFill(Color.RED);
            }
            else {
                starthour = Integer.parseInt(heuredebut.getText());
                startminutes = Integer.parseInt(minutesdebut.getText());
                endhour = Integer.parseInt(heurefin.getText());
                endminutes = Integer.parseInt(minutesfin.getText());
                if (hourValidator(starthour) == false || hourValidator(endhour)== false || minutesValidator(startminutes) == false || minutesValidator(endminutes)==false){
                    message.setText("Vous devez choisir des horaires valides.");
                    message.setFill(Color.RED);
                }
                else{
                    LocalTime horaireDebut = LocalTime.of(starthour,startminutes);
                    LocalTime horaireFin = LocalTime.of(endhour,endminutes);
                    if (horaireFin.isBefore(horaireDebut)||(horaireFin.equals(horaireDebut))){
                        message.setText("Veuillez vous assurez que vous avez introduit une période valide.");
                        message.setFill(Color.RED);
                    }
                    else{
                        if (this.user.getCalendrierAdequat(date.getValue().getDayOfMonth(),date.getValue().getMonthValue(),date.getValue().getYear())!=null){
                            Categorie c = user.getCategorie(categorie.getValue());
                            Projet p = user.getProjet(projet.getValue());
                            TacheSimple tacheSimple = new TacheSimple(name.getText(),c,p);
                            user.planifierManuellement(tacheSimple,date.getValue().getDayOfMonth(),date.getValue().getMonthValue(),date.getValue().getYear(),starthour,startminutes,endhour,endminutes);
                            message.setFill(Color.GREEN);
                            message.setText("Tâche planifiée avec succeès");
                            user.getCalendrierAdequat(date.getValue().getDayOfMonth(),date.getValue().getMonthValue(),date.getValue().getYear()).afficher();
                        }
                        else{
                            message.setFill(Color.RED);
                            message.setText("Aucun calendrier trouvé correspondant à la date saisie");
                        }
                    }
                }
            }
        }
        user.getTreeCalendrier().last().afficher();
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
