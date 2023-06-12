package com.example.tp_poo_ouikene_bouyakoub_g1;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ListeTachesController   {

    @FXML
    private Text message;

    @FXML
    private TableView<Tache> Tableview;

    @FXML
    private TableColumn<Tache, String> Nom;

    @FXML
    private TableColumn<Tache, String> cat;

    @FXML
    private TableColumn<Tache, String> periodicite;

    @FXML
    private TableColumn<Tache, LocalDate> datelim;

    @FXML
    private TableColumn<Tache, LocalTime> dur;

    @FXML
    private TableColumn<Tache, EtatDeRealisation> etat;

    @FXML
    private TableColumn<Tache, Priorite> prio;

    @FXML
    private TableColumn<Tache, String> project;

    @FXML
    private TableColumn<Tache, String> type;

    // @FXML
    //private ListView<String> listView;

    private Utilisateur user;

    DesktopPlanner app;
    private Scene scene;

    private Stage stage;

    private Parent root;



    public void setUser(Utilisateur user){
        this.user=user;
        initialiser();
    }

    public void setApp(DesktopPlanner app) {
        this.app = app;
    }

    void initialiser()
    {
        Nom.setCellValueFactory(cellData -> {
            Tache tache=cellData.getValue();
            String nom= tache.getNom();
            return new SimpleStringProperty(nom);
        });
        //datelim.setCellValueFactory(new PropertyValueFactory<Tache,LocalDate>("dateLimite"));
        datelim.setCellValueFactory(cellData -> {
            Tache tache=cellData.getValue();
            LocalDate date=tache.getDateLimite();
            return new SimpleObjectProperty(date);
        });
        //etat.setCellValueFactory(new PropertyValueFactory<Tache,EtatDeRealisation>("etat"));
        etat.setCellValueFactory(cellData -> {
            Tache tache=cellData.getValue();
            EtatDeRealisation etat=tache.getEtat();
            return new SimpleObjectProperty(etat);
        });
        //cat.setCellValueFactory(new PropertyValueFactory<Tache,String>("categorie"));
        cat.setCellValueFactory(cellData -> {
            Tache tache=cellData.getValue();
            if(tache.getCategorie()!=null)
                return new SimpleStringProperty(tache.getCategorie().getCategorie());
            else
                return new SimpleStringProperty("");
        });
        //prio.setCellValueFactory(new PropertyValueFactory<Tache,Priorite>("priorite"));
        prio.setCellValueFactory(cellData -> {
            Tache tache=cellData.getValue();
            Priorite priorite=tache.getPriorite();
            return new SimpleObjectProperty(priorite);
        });
        //dur.setCellValueFactory(new PropertyValueFactory<Tache,LocalTime>("duree"));
        dur.setCellValueFactory(cellData -> {
            Tache tache=cellData.getValue();
            LocalTime time=tache.getDuree();
            return new SimpleObjectProperty(time);
        });
        project.setCellValueFactory(cellData -> {
            Tache tache=cellData.getValue();
            String projet="";
            if(tache instanceof TacheSimple && ((TacheSimple) tache).getProjet()!=null) {
                projet = ((TacheSimple) tache).getProjet().getNom();
            }
            return new SimpleStringProperty(projet);
        });
        periodicite.setCellValueFactory(cellData -> {
            Tache tache=cellData.getValue();
            int periodicite=0;
            if(tache instanceof TacheSimple) {
                periodicite = ((TacheSimple) tache).getPeriodicite();
            }
            return new SimpleStringProperty(String.valueOf(periodicite));
        });
        type.setCellValueFactory(cellData -> {
            Tache tache=cellData.getValue();
            if(tache instanceof TacheSimple)
                return new SimpleStringProperty("Simple");
            else
                return new SimpleStringProperty("Décomposable");
        });
        Tableview.setRowFactory(tv -> new TableRow<Tache>() {
            @Override
            protected void updateItem(Tache item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle(""); // Réinitialise le style de la ligne
                } else {
                    if (item.getCategorie()!= null) {
                        setStyle("-fx-background-color: "+item.getCategorie().getColor() +";");
                    } else {
                        setStyle(""); // Réinitialise le style de la ligne pour les autres éléments
                    }
                }
            }
        });
        Tableview.getItems().clear();
        for(Tache t:user.getTreeTaches())
        {
            Tableview.getItems().add(t);
        }
    }

    @FXML
    void AjouterTacheSimple(ActionEvent event)  {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AjoutTache.fxml"));
            Parent root = loader.load();
            AjoutTacheSimpleController controllerAjoutTacheSimple = loader.getController();
            controllerAjoutTacheSimple.setUser(user);
            controllerAjoutTacheSimple.setApp(this.app);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void AjouterTacheDecomp(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AjoutTacheDecomposable.fxml"));
            Parent root = loader.load();
            AjoutTacheDecomposableController controllerAjoutTacheDecomp = loader.getController();
            controllerAjoutTacheDecomp.setUser(user);
            controllerAjoutTacheDecomp.setApp(this.app);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            //centerWindow();
            stage.show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /*void modifier(ActionEvent event) throws IOException {
        if(!Tableview.getSelectionModel().isEmpty())
        {
            int selectedID= Tableview.getSelectionModel().getSelectedIndex();
            Creneau cr=null;
            for (Calendrier c: user.getTreeCalendrier())
            {
                for(Journee j:c.getTreeJournees())
                {
                    for (Creneau creneau1:j.getTreeCreneaux())
                    {

                        if(creneau1.getTache()==Tableview.getSelectionModel().getSelectedItem())
                        {
                            cr=creneau1;
                        }

                    }
                }
            }

            if (cr != null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifierTache.fxml"));
                Parent root = loader.load();
                ModificationTacheController controllerModificationTache = loader.getController();
                controllerModificationTache.setUser(user);
                controllerModificationTache.setApp(this.app);
                controllerModificationTache.setTache(cr.getTache());
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                //centerWindow();
                stage.show();
            }
        }
        else
        {
            message.setText("Veuillez seléctionner une tache !");
            message.setFill(Color.RED);
        }
    }*/

    @FXML
    void PlanificationAuto(ActionEvent event) {
        if(!Tableview.getItems().isEmpty())
        {
            user.planifierAutomatique();
            message.setFill(Color.GREEN);
            message.setText("Planification effectuée avec succes !");
            //Tableview.getItems().clear();
            initialiser();
        }

    }

    @FXML
    void delete(ActionEvent event) {
        if(!Tableview.getSelectionModel().isEmpty())
        {
            int selectedID= Tableview.getSelectionModel().getSelectedIndex();
            user.supprimerTache(Tableview.getSelectionModel().getSelectedItem());
            Tableview.getItems().remove(selectedID);
        }
        //setUser(user);
    }

    @FXML
    void deleteAll(ActionEvent event) {
        user.getTreeTaches().clear();
        Tableview.getItems().clear();
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
