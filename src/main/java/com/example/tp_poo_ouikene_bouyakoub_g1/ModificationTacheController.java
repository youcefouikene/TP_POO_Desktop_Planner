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

public class ModificationTacheController {
    Utilisateur user;
    TacheSimple tache;
    DesktopPlanner app;

    private Scene scene;

    private Stage stage;

    private Parent root;

    @FXML
    private ChoiceBox<String> Categories;

    @FXML
    private ChoiceBox<EtatDeRealisation> Etat;


    @FXML
    private TextField nom;

    @FXML
    private ChoiceBox<String> choice;

    @FXML
    private Text message;

    public void setUser(Utilisateur user) {
        this.user = user;
        for (Categorie c: user.getTreeCategories())
        {
            Categories.getItems().add(c.getCategorie());
        }
        Etat.getItems().add(EtatDeRealisation.COMPLETED);
        Etat.getItems().add(EtatDeRealisation.NOTREALIZED);
        Etat.getItems().add(EtatDeRealisation.INPROGRESS);
        Etat.getItems().add(EtatDeRealisation.CANCELLED);
        Etat.getItems().add(EtatDeRealisation.DELAYED);
        initializeProjects();
    }

    private void initializeProjects() {
        choice.getItems().clear();
        if (user != null) {
            HashSet<String> myProjects = new HashSet<>();
            for (Projet p : user.getListeProjets()) {
                myProjects.add(p.getNom());
            }
            myProjects.add("Aucun");
            choice.getItems().addAll(myProjects);
        }
    }

    public void setApp(DesktopPlanner app) {
        this.app = app;
    }

    public void setTache(TacheSimple tache) {
        this.tache=tache;
        nom.setText(this.tache.getNom());
        if(tache.getProjet()!=null){
            choice.setValue(tache.getProjet().getNom());
        }
        Etat.setValue(tache.getEtat());
        if(tache.getCategorie()!=null) {
            Categories.setValue(tache.getCategorie().getCategorie());
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        if (nom.getText().isEmpty()){
            message.setFill(Color.RED);
            message.setText("Veuillez remplir tous les champs du formulaire qui contiennent un (*)");
        }
        else
        {
            tache.setNom(nom.getText());

            if (!Etat.getSelectionModel().isEmpty())
            {
                tache.setEtat(Etat.getSelectionModel().getSelectedItem());
            }

            if (!Categories.getSelectionModel().isEmpty())
            {
                for (Categorie cat: user.getTreeCategories())
                {
                    if(cat.getCategorie()==Categories.getValue())
                    {
                        tache.setCategorie(cat);
                    }
                }
            }

//            if(projet.getText().isEmpty())
//            {
//                tache.setProjet(null);
//            } else if (!tache.getProjet().getNom().equals(projet.getText())) {
//                for (Projet project: user.getListeProjets())
//                {
//                    if(project.getNom()==projet.getText())
//                    {
//                        tache.setProjet(project);
//                    }
//                }
//            }
            tache.setProjet(user.getProjet(choice.getValue()));
            tache.afficher();
            message.setFill(Color.GREEN);
            message.setText("Modification effectuée avec succès !");
        }
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Calendrier.fxml"));
        Parent root = loader.load();
        CalendrierController calendrierController = loader.getController();
        calendrierController.setUser(user);
        calendrierController.setApp(app);
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
