package com.example.tp_poo_ouikene_bouyakoub_g1;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class MesRendementsController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Utilisateur user;

    private DesktopPlanner app;

    @FXML
    private Text pseudo;

    @FXML
    private ProgressBar progress1;

    @FXML
    private ProgressBar progress2;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private ChoiceBox<String> periode;
    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private ChoiceBox<LocalDate> day;

    @FXML
    private Text rendementDay;

    @FXML
    private Text completedTasksDay;

    @FXML
    private ProgressBar progress3;

    @FXML
    private Text totalTasksDay;

    @FXML
    private Text jourPlusRentable;

    @FXML
    private Text rendementJr;

    @FXML
    private Text completedTasksJr;

    @FXML
    private Text totalTasksJr;

    @FXML
    private Text moyenneRendement;

    @FXML
    private Text encouragement;

    @FXML
    private Text tasksCompletedPeriode;

    @FXML
    private Text completedProjects;

    @FXML
    private Text badgeMedium;

    @FXML
    private Text badgeGood;

    @FXML
    private Text badgeVeryGood;

    @FXML
    private Text badgeExcellent;

    Calendrier c;


    public void setUtilisateur(Utilisateur user){
        this.user = user;
        if (user!= null){
            TreeSet<String> myProjects = new TreeSet<>();
            for (Calendrier c : user.getTreeCalendrier()) {
                myProjects.add("Du "+c.getDebutPeriode().toString()+" Au "+c.getFinPeriode().toString());
            }
            periode.getItems().addAll(myProjects);
        }
    }

    public void setData(){
        barChart.setAnimated(false);
        barChart.getData().clear();
        day.setValue(null);
        day.getItems().clear();
        rendementDay.setText(null);
        completedTasksDay.setText(null);
        totalTasksDay.setText(null);
        progress3.setProgress(0);
        String selectedProjectString = periode.getSelectionModel().getSelectedItem();
        if (selectedProjectString != null) {
            int debutIndex = selectedProjectString.indexOf("Du ") + 3;
            int auIndex = selectedProjectString.indexOf(" Au ");
            int finIndex = selectedProjectString.indexOf(" Au ") + 4;
            String debutDateString = selectedProjectString.substring(debutIndex, auIndex);
            String finDateString = selectedProjectString.substring(finIndex);
            LocalDate debutPeriode = LocalDate.parse(debutDateString);
            LocalDate finPeriode = LocalDate.parse(finDateString);
            c = user.getCalendier(debutPeriode,finPeriode);
            if (c!= null){
                TreeSet<LocalDate> days = new TreeSet<>();
                for (Journee j : c.getTreeJournees()) {
                    days.add(j.getJour());
                }
                day.getItems().addAll(days);
            }
            LocalDate date = c.jourPlusRentable();
            Journee j = c.getJour(date);
            jourPlusRentable.setText(j.getJour().toString());
            rendementJr.setText(String.valueOf(Math.round(j.calculerRendementJournalier()*100))+"%");
            progress2.setProgress(j.calculerRendementJournalier());
            completedTasksJr.setText(String.valueOf(j.totalCompletedTasks()));
            totalTasksJr.setText(String.valueOf(j.totalTasks()));
            moyenneRendement.setText(String.valueOf(Math.round(c.calculerMoyenneRendementJournalier()*100))+"%");
            progress1.setProgress(c.calculerMoyenneRendementJournalier());
            encouragement.setText(String.valueOf(c.nombreEncouragements(user.getObjectif())));
            tasksCompletedPeriode.setText(String.valueOf(c.totalCompletedTasks()));
            completedProjects.setText(String.valueOf(c.numberCompletedProjects(user)));
            HashMap<Badge,Integer> badges = c.statistiquesBadges(user.getObjectif());
            badgeMedium.setText(String.valueOf(badges.get(Badge.AUCUN)));
            badgeGood.setText(String.valueOf(badges.get(Badge.GOOD)));
            badgeVeryGood.setText(String.valueOf(badges.get(Badge.VERYGOOD)));
            badgeExcellent.setText(String.valueOf(badges.get(Badge.EXCELLENT)));
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("Rendement journalier");
            for (Journee j2 : c.getTreeJournees()) {
                String dayString = j2.getJour().toString();
                int rendementPercentage = Math.round(j2.calculerRendementJournalier() * 100);
                series.getData().add(new XYChart.Data<>(dayString, rendementPercentage));
            }
            barChart.getData().add(series);
        }
    }



    public void fillDayInfo(){
        if (c!= null){
            LocalDate date = day.getSelectionModel().getSelectedItem();
            Journee j = c.getJour(date);
            if (j!= null){
                progress3.setProgress(j.calculerRendementJournalier());
                rendementDay.setText(String.valueOf(Math.round(j.calculerRendementJournalier()*100))+"%");
                completedTasksDay.setText(String.valueOf(j.totalCompletedTasks()));
                totalTasksDay.setText(String.valueOf(j.totalTasks()));
            }
        }
    }

    public void setApp(DesktopPlanner app) {
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
