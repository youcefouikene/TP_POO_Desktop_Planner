package com.example.tp_poo_ouikene_bouyakoub_g1;


import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class CalendrierController {
    private Utilisateur user;
    private DesktopPlanner app;

    @FXML
    private Text tskscomplt;

    @FXML
    private Text prjcomplt;

    @FXML
    private TableColumn<Secours, String> cat;

    @FXML
    private ChoiceBox<String> choixcal;

    @FXML
    private TableColumn<Secours, LocalTime> creneau;

    @FXML
    private TableColumn<Secours, LocalTime> creneauFin;

    @FXML
    private TableColumn<Secours, LocalDate> dateLimite;

    @FXML
    private TableColumn<Secours, LocalTime> duree;

    @FXML
    private TableColumn<Secours, EtatDeRealisation> etat;

    @FXML
    private TableColumn<Secours, LocalDate> jour;

    @FXML
    private TableColumn<Secours, String> nomTache;

    @FXML
    private TableColumn<Secours, Priorite> priorite;

    @FXML
    private TableColumn<Secours,String> projet;

    @FXML
    private TableView<Secours> tableview;

    @FXML
    private DatePicker dateD;

    @FXML
    private DatePicker dateF;

    @FXML
    private Text message;

    @FXML
    private DatePicker dateFinEtaler;

    private Stage stage;

    private Scene scene;

    private Parent root;

    void initialiser()
    {
        if(user!=null)
        {
            choixcal.getItems().clear();
            for(Calendrier cal:user.getTreeCalendrier())
            {
                choixcal.getItems().add("Du "+cal.getDebutPeriode().toString()+" Au "+cal.getFinPeriode().toString());
            }
        }
        else
            System.out.println("user  null !");

    }

    @FXML
    public void remplir()
    {
        tableview.getItems().clear();
        if(!choixcal.getSelectionModel().isEmpty())
        {
            int selectedID=choixcal.getSelectionModel().getSelectedIndex();
            Calendrier calReel=null;
            for(Calendrier cal:user.getTreeCalendrier())
            {
                System.out.println("Du "+cal.getDebutPeriode().toString()+" Au "+cal.getFinPeriode().toString());
                if(("Du "+cal.getDebutPeriode().toString()+" Au "+cal.getFinPeriode().toString()).equals (choixcal.getValue()))
                {
                    calReel=cal;
                }
            }
            List<Secours> listPrincipale= new ArrayList<>();
            for(Journee j: calReel.getTreeJournees())
            {
                for (Creneau c:j.getTreeCreneaux())
                {
                    if(c.getTache()!=null)
                    {
                        Secours sec= new Secours(j.getJour(),c.getHoraireDebut(),c.getHoraireFin(),c.getTache());
                        listPrincipale.add(sec);
                    }
                }
            }
            jour.setCellValueFactory(new PropertyValueFactory<Secours,LocalDate>("date"));
            creneau.setCellValueFactory(new PropertyValueFactory<Secours,LocalTime>("creneauDebut"));
            creneauFin.setCellValueFactory(new PropertyValueFactory<Secours,LocalTime>("creneauFin"));
            nomTache.setCellValueFactory(cellData -> {
                Secours secour=cellData.getValue();
                TacheSimple tache=secour.getTache();
                String nom=tache.getNom();
                return new SimpleStringProperty(nom);
            });
            cat.setCellValueFactory(cellData -> {
                Secours secour=cellData.getValue();
                TacheSimple tache=secour.getTache();
                String cate ="";
                if (tache.getCategorie()!=null)
                 cate=tache.getCategorie().getCategorie();
                return new SimpleStringProperty(cate);
            });
            priorite.setCellValueFactory(cellData ->{
                Secours secour=cellData.getValue();
                TacheSimple tache=secour.getTache();
                Priorite prio=tache.getPriorite();
                return new SimpleObjectProperty<>(prio);
            });
            etat.setCellValueFactory(cellData ->{
                Secours secour=cellData.getValue();
                TacheSimple tache=secour.getTache();
                EtatDeRealisation etat=tache.getEtat();
                return new SimpleObjectProperty<>(etat);
            });
            dateLimite.setCellValueFactory(cellData ->{
                Secours secour=cellData.getValue();
                TacheSimple tache=secour.getTache();
                LocalDate date=tache.getDateLimite();
                return new SimpleObjectProperty<>(date);
            });
            duree.setCellValueFactory(cellData ->{
                Secours secour=cellData.getValue();
                TacheSimple tache=secour.getTache();
                LocalTime temps=tache.getDuree();
                return new SimpleObjectProperty<>(temps);
            });
            duree.setCellValueFactory(cellData ->{
                Secours secour=cellData.getValue();
                TacheSimple tache=secour.getTache();
                LocalTime temps=tache.getDuree();
                return new SimpleObjectProperty<>(temps);
            });
            projet.setCellValueFactory(cellData ->{
                Secours secour=cellData.getValue();
                TacheSimple tache=secour.getTache();
                if (tache.getProjet()!=null){
                    String projectName=tache.getProjet().getNom();
                    return new SimpleObjectProperty<>(projectName);
                }
                else{
                    return new SimpleObjectProperty<>();
                }
            });

                tableview.setRowFactory(tv -> new TableRow<Secours>() {
                    @Override
                    protected void updateItem(Secours item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setStyle(""); // Réinitialise le style de la ligne
                        } else {
                            if (item.getTache().getCategorie()!= null) {
                                setStyle("-fx-background-color: "+item.getTache().getCategorie().getColor() +";");
                            } else {
                                setStyle(""); // Réinitialise le style de la ligne pour les autres éléments
                            }
                        }
                    }
                });
            for(Secours s:listPrincipale)
            {
                tableview.getItems().add(s);
            }

            TasksCompleted();
            ProjetsCompleted();
        }
    }

    /*private static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }*/

    /*private static Color getContrastingTextColor(Color backgroundColor) {
        double luminance = (0.299 * backgroundColor.getRed() +
                0.587 * backgroundColor.getGreen() +
                0.114 * backgroundColor.getBlue());
        System.out.println(luminance);
        return (luminance > 0.5) ? Color.WHITE : Color.WHITE;
    }

    public static Color convert(String colorString) {
        try {
            return Color.valueOf(colorString);
        } catch (IllegalArgumentException e) {
            // La chaîne de couleur est invalide
            // Gérer l'erreur ou retourner une valeur par défaut
            return Color.BLACK; // Valeur par défaut en cas d'erreur
        }
    }*/

    void setUser(Utilisateur user) {
        this.user = user;
        initialiser();
    }

    void setApp(DesktopPlanner app){this.app=app;}

    @FXML
    void ajoutCalendrier(ActionEvent event) throws IOException {
        if(dateD.getValue()==null || dateF.getValue()==null)
        {
            message.setText("Veuillez remplir tout les champs !");
            message.setFill(Color.RED);
        }
        else if(dateD.getValue().isAfter(dateF.getValue()))
        {
            message.setText("Veuillez vérifier vos données inscrites !");
            message.setFill(Color.RED);
        }
        else
        {
            Calendrier cal=new Calendrier(dateD.getValue().getDayOfMonth(),dateD.getValue().getMonthValue(),dateD.getValue().getYear(),dateF.getValue().getDayOfMonth(),dateF.getValue().getMonthValue(),dateF.getValue().getYear());
            user.ajouterCalendrier(cal);
            message.setText("Ajout effectué avec succès !");
            message.setFill(Color.GREEN);
            user.ajouterCalendrier(cal);
            initialiser();
            TasksCompleted();
            ProjetsCompleted();
        }

    }

    @FXML
    void delete(ActionEvent event) {
        if (!tableview.getSelectionModel().isEmpty()){
            int selectedID= tableview.getSelectionModel().getSelectedIndex();
            for (Calendrier c: user.getTreeCalendrier())
            {
                for(Journee j:c.getTreeJournees())
                {
                    for (Creneau creneau1:j.getTreeCreneaux())
                    {
                        if(creneau1.getTache()==tableview.getSelectionModel().getSelectedItem().getTache())
                        {
                            creneau1.setTache(null);
                        }
                    }
                }
            }
            tableview.getItems().remove(selectedID);
            user.getTreeCalendrier().last().afficher();
            TasksCompleted();
            ProjetsCompleted();
        }
        else
        {
            message.setText("Veuillez sélectionner une tache !");
            message.setFill(Color.RED);
        }
    }

    @FXML
    void modifier(ActionEvent event) throws IOException {
        if(!tableview.getSelectionModel().isEmpty())
        {
            int selectedID= tableview.getSelectionModel().getSelectedIndex();
            Creneau cr=null;
            for (Calendrier c: user.getTreeCalendrier())
            {
                for(Journee j:c.getTreeJournees())
                {
                    for (Creneau creneau1:j.getTreeCreneaux())
                    {

                            if(creneau1.getTache()==tableview.getSelectionModel().getSelectedItem().getTache())
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
            ProjetsCompleted();
            TasksCompleted();
        }
        else
        {
            message.setText("Veuillez seléctionner une tache !");
            message.setFill(Color.RED);
        }
    }

    @FXML
    void etaler(ActionEvent event) throws IOException {
        if(dateFinEtaler.getValue()==null)
        {
            message.setText("Veuillez remplir tout les champs !");
            message.setFill(Color.RED);
        }
        else
        {
            if(!choixcal.getSelectionModel().isEmpty())
            {
                for(Calendrier c:user.getTreeCalendrier())
                {
                    if(("Du "+c.getDebutPeriode().toString()+" Au "+c.getFinPeriode().toString()).equals (choixcal.getValue()))
                    {
                        if(c.getDebutPeriode().isBefore(dateFinEtaler.getValue()))
                        {
                            c.modifierFinPeriode(dateFinEtaler.getValue().getYear(),dateFinEtaler.getValue().getMonthValue(),dateFinEtaler.getValue().getDayOfMonth());
                            message.setText("Calendrier étalé avec succès !");
                            message.setFill(Color.GREEN);
                            initialiser();
                        }
                        else
                        {
                            message.setText("La date debut du calendrier est avant la date choisie !");
                            message.setFill(Color.GREEN);
                        }

                    }
                }
            }
            else {
                message.setText("Veuillez sélectionner un calendrier ");
                message.setFill(Color.RED);
            }
        }
    }

    void ProjetsCompleted()
    {
            System.out.println("entrer");
            for(Calendrier c:user.getTreeCalendrier())
            {
                if(("Du "+c.getDebutPeriode().toString()+" Au "+c.getFinPeriode().toString()).equals (choixcal.getValue()))
                {
                    prjcomplt.setText("Nombre projets complétés : "+String.valueOf(c.numberCompletedProjects(user)));
                }
            }


    }

    void TasksCompleted()
    {
        for(Calendrier c:user.getTreeCalendrier())
        {
            if(("Du "+c.getDebutPeriode().toString()+" Au "+c.getFinPeriode().toString()).equals (choixcal.getValue()))
            {
                tskscomplt.setText("Nombre tâches complétées : "+String.valueOf(c.totalCompletedTasks()));
            }
        }
    }

    public void deleteCalendar(ActionEvent event) throws IOException{
        if (!choixcal.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppression");
            alert.setContentText("Voulez-vous vraiment supprimer ce calendrier ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                String selectedCalendar = choixcal.getSelectionModel().getSelectedItem();
                if (selectedCalendar!= null){
                    int debutIndex = selectedCalendar.indexOf("Du ") + 3;
                    int auIndex = selectedCalendar.indexOf(" Au ");
                    int finIndex = selectedCalendar.indexOf(" Au ") + 4;
                    String debutDateString = selectedCalendar.substring(debutIndex, auIndex);
                    String finDateString = selectedCalendar.substring(finIndex);
                    LocalDate debutPeriode= LocalDate.parse(debutDateString);
                    LocalDate finPeriode = LocalDate.parse(finDateString);
                    System.out.println("debut"+debutPeriode);System.out.println("fin"+finPeriode);
                    user.deleteCalendar(debutPeriode,finPeriode);
                    initialiser();
                    prjcomplt.setText(null);
                    tskscomplt.setText(null);
                }
            }
        }
        else {
            message.setText("Veuillez sélectionner un calendrier ");
            message.setFill(Color.RED);
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



