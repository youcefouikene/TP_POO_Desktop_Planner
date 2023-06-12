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


public class MesProjetsController  {

    @FXML
    private Text pseudo;

    @FXML
    private TextField name;

    @FXML
    private TextField description;

    @FXML
    private ProgressBar progress;

    @FXML
    private ChoiceBox<String> choice;

    @FXML
    private Text pourcentage;

    @FXML
    private Text message;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Utilisateur user;

    private DesktopPlanner app;

    private Projet p;

    public void setUser(Utilisateur user){
        this.user = user;
        initializeProjects();
    }

    private void initializeProjects() {
        choice.getItems().clear();
        if (user != null) {
            HashSet<String> myProjects = new HashSet<>();
            for (Projet p : user.getListeProjets()) {
                myProjects.add(p.getNom());
            }
            choice.getItems().addAll(myProjects);
        }
    }

    public void setApp (DesktopPlanner app){
        this.app = app;
    }



    public void remplirChamps() {
        message.setText(null);
        String projectName = choice.getValue();
        if (projectName != null) {
            p = user.getProjet(projectName);
            if (p != null) {
                name.setText(p.getNom());
                description.setText(p.getDescription());
                progress.setProgress(user.etatDeRealisationDuProjet(p));
                pourcentage.setText(Integer.toString((int) (user.etatDeRealisationDuProjet(p) * 100)) + "%");
                message.setText(null);
            } else {
               System.out.println("Error!");
            }
        }
    }

    public void modifierInfos (){
        if (name.getText().isEmpty() || description.getText().isEmpty()){
            message.setText("Veuillez remplir tout le formulaire avant d'appliquer des modifications au projet.");
            message.setFill(Color.RED);
        }
        else{
            p.setNom(name.getText());
            p.setDescription(description.getText());
            name.setText(p.getNom());
            description.setText(p.getDescription());
            int selectedIndex = choice.getSelectionModel().getSelectedIndex();
            choice.getItems().set(selectedIndex,p.getNom());
            choice.setValue(name.getText());
            message.setText("Modification effectuée avec succès");
            message.setFill(Color.GREEN);
        }
    }

    public void reset (){
        if (p!=null){
            name.setText(p.getNom());
            description.setText(p.getDescription());
            progress.setProgress(user.etatDeRealisationDuProjet(p));
            pourcentage.setText(Integer.toString((int)(user.etatDeRealisationDuProjet(p)*100))+"%");
        }
    }

    public void supprimer() {
        String projectName = choice.getValue();
        if (projectName != null) {
            Projet projet = user.getProjet(projectName);
            if (projet != null && projet.equals(p)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmer la suppression");
                alert.setContentText("Voulez-vous vraiment supprimer le projet " + projectName + " de manière définitive ?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    user.supprimerProjet(projet);
                    choice.getItems().remove(projectName);
                    choice.setValue(null);
                    name.clear();
                    description.clear();
                    progress.setProgress(0);
                    pourcentage.setText("");
                    message.setText("Suppression effectuée avec succès");
                    message.setFill(Color.GREEN);
                }
            } else {
                System.out.println("Error!");
            }
        }
    }

    public void ajouterProjet(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AjouterProjet.fxml"));
        root = loader.load();
        AjouterProjetController controlleraddProject = loader.getController();
        controlleraddProject.setUser(user);
        controlleraddProject.setApp(this.app);
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
