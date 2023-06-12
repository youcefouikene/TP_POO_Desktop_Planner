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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class AccueilController {

    @FXML
    private TextField pseudo;

    @FXML
    private Text message;

    @FXML
    private TextField Heures;

    @FXML
    private TextField Minutes;

    @FXML
    private Spinner<Integer> seuil;

    @FXML
    private Text jourPlusRentable;

    @FXML
    private ProgressBar progress2;

    @FXML
    private Text rendementJr;

    @FXML
    private Text completedTasksJr;

    @FXML
    private Text totalTasksJr;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmpassword;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Utilisateur user;

    private DesktopPlanner app;

    public void setUtilisateur(Utilisateur user){
        this.user = user;
        pseudo.setText(user.getPseudo());
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,50);
        seuil.setValueFactory(valueFactory);
        seuil.getValueFactory().setValue(user.getObjectif());
        LocalDate date = user.jourPlusRentable();
        if (date != null){
            Calendrier c = user.getCalendrierAdequat(date.getDayOfMonth(),date.getMonthValue(),date.getYear());
            if (c!= null){
                Journee j = c.getJour(date);
                if (j!= null){
                    jourPlusRentable.setText(j.getJour().toString());
                    float pourcentage = j.calculerRendementJournalier()*100;
                    rendementJr.setText(String.valueOf(Math.round(pourcentage)+"%"));
                    progress2.setProgress(pourcentage);
                    completedTasksJr.setText(String.valueOf(j.totalCompletedTasks()));
                    totalTasksJr.setText(String.valueOf(j.totalCompletedTasks()));
                }
            }
        }
        Heures.setText(String.valueOf(user.getDureeMinimaleCreneau().getHour()));
        Minutes.setText(String.valueOf(user.getDureeMinimaleCreneau().getMinute()));
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

    public void setApp(DesktopPlanner app) {
        this.app = app;
    }

    public void supprimerCompte(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setContentText("Voulez-vous vraiment supprimer vote compte DesktopPlanner de manière définitive "+" ?"+" Vous ne pouvez en aucun cas le récupérer");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            user.supprimerCompte(app);
            this.seDeconnecter(event);
        }
    }

    public void setData(){
        boolean changed = false;
        if (!pseudo.getText().isEmpty()){
            if (!pseudo.getText().equals(user.getPseudo())){
                if (app.compteExistant(pseudo.getText())){
                    message.setFill(Color.RED);
                    message.setText("Pseudo déjà existant");
                }
                else{
                    changed = true;
                    user.setPseudo(pseudo.getText());
                }
            }
        }
        if (seuil.getValue() != user.getObjectif()){
            changed = true;
            user.setObjectif(seuil.getValue());
        }
        if (!password.getText().isEmpty() && !confirmpassword.getText().isEmpty()){
            if (!password.getText().equals(confirmpassword.getText())) {
                message.setFill(Color.RED);
                message.setText("Incohérence entre les mots de passe");
            }
            else {
                if (!password.getText().equals(user.getPassword())){
                    user.setMotDePasse(password.getText());
                    changed = true;
                }
            }
        }
        if (!Heures.getText().isEmpty() && !Minutes.getText().isEmpty()){
            if (isNumber(Heures.getText())&&isNumber(Minutes.getText())){
                int hours = Integer.valueOf(Heures.getText());
                int minutes = Integer.valueOf(Minutes.getText());
                if (hourValidator(hours) && minutesValidator(minutes)){
                    LocalTime creneau = LocalTime.of(hours,minutes);
                    if (!creneau.equals(user.getDureeMinimaleCreneau())){
                        changed = true;
                        user.setDureeMinimaleCreneau(hours,minutes);
                    }
                }
                else{
                    message.setFill(Color.RED);
                    message.setText("Veuillez choisir une durée valide");
                }
            }
            else{
                message.setFill(Color.RED);
                message.setText("Veuillez choisir une durée valide");
            }
        }
        if (changed==true){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modification effectuée avec succès");
            alert.setContentText("Vos informations personnelles ont été mises à jour");
            alert.showAndWait();
            message.setText(null);
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
