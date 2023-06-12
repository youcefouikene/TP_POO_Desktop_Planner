package com.example.tp_poo_ouikene_bouyakoub_g1;

import java.util.*;
import java.io.Serializable;
import java.time.*;

public class Utilisateur implements Serializable{

    private String pseudo;

    private String motDePasse;

    private LocalTime dureeMinimaleCreneau = LocalTime.of(0, 30);

    private TreeSet<Calendrier> listeCalendriers;

    private HashSet<Categorie> listeCategories;

    private TreeSet<Tache> listeTaches;

    private HashSet<Projet> listeProjets;

    private int objectif = 3;

    public int getObjectif() {

        return objectif;
    }


    public TreeSet<Calendrier> test (){

        return listeCalendriers;

    }

    public void setObjectif(int objectif) {

        this.objectif = objectif;
    }


    public void ajouterCalendrier (Calendrier c) {

        listeCalendriers.add(c);

    }


    public Utilisateur (String pseudo, String motDePasse) {

        this.pseudo = pseudo;

        this.motDePasse = motDePasse;

        listeCategories = new HashSet<Categorie>();

        listeTaches = new TreeSet<Tache>();

        listeCalendriers = new TreeSet<Calendrier>();

        /*Added this line*/

        listeProjets = new HashSet<Projet>();
    }


    public void ajouterTache(Tache tache) {

        listeTaches.add(tache);
    }


    public void setDureeMinimaleCreneau(int hours, int minutes) {

        dureeMinimaleCreneau = LocalTime.of(hours, minutes);
    }



    public LocalTime getDureeMinimaleCreneau() {

        return dureeMinimaleCreneau;
    }


    public void setPseudo(String pseudo) {

        this.pseudo = pseudo;
    }


    public void setMotDePasse(String motDePasse) {

        this.motDePasse = motDePasse;
    }


    public String getPseudo() {

        return pseudo;
    }


    public String getMotDePasse() {

        return motDePasse;
    }


    public boolean equals(Object o) {

        if (o instanceof Utilisateur) {

            return (((Utilisateur)o).getPseudo() == this.pseudo);

        }
        return false;
    }


    public int hashCode() {

        return this.pseudo.hashCode();
    }


    public void ajouterCategorie(String categorie, String color) {


        listeCategories.add(new Categorie(categorie,color));
    }

    public boolean ajouterCategorie(Categorie c) {

        return(listeCategories.add(c));

    }

    public HashMap<Categorie,Duration> timeSpentPerCategory(){

        HashMap<Categorie, Duration> map = new HashMap<Categorie, Duration>();
        for (Categorie c : listeCategories){
            map.put(c,this.calculerTempsPasse(c.getCategorie()));
        }
        return map;
    }


    public boolean existanceCategorie(String nom){
        for (Categorie c : listeCategories){
            if (c.getCategorie().equals(nom)){
                return true;
            }
        }
        return false;
    }

    public Categorie getCategorie(String nom){
        for (Categorie c : listeCategories){
            if(c.getCategorie().equals(nom)){
                return c;
            }
        }
        return null;
    }

    public void supprimerCategorie(Categorie c){
        this.listeCategories.remove(c);
    }

    public HashSet<Categorie> getListeCategories(){
        return this.listeCategories;
    }

    public boolean existanceCouleur(String nom){
        for (Categorie c : listeCategories){
            if (c.getCategorie().equals(nom)){
                return true;
            }
        }
        return false;
    }

    public void supprimerCategorie(String categorie) {

        Iterator<Categorie> iterator = listeCategories.iterator();

        while (iterator.hasNext()) {

            Categorie c = iterator.next();

            if (c.getCategorie().equalsIgnoreCase(categorie)) {

                iterator.remove();
            }
        }
    }


    public void afficherCategories() {

        for (Categorie c : listeCategories) {

            System.out.println("Categorie: "+c.getCategorie()+" Couleur: "+c.getColor());

        }
    }


    public void modifierCategorie(String categorie, String nouvelleColeur) {

        for (Categorie c : listeCategories) {

            if (c.getCategorie().equalsIgnoreCase(categorie)== true) {

                c.setColor(nouvelleColeur);

                break;
            }
        }
    }


    public void supprimerCompte(DesktopPlanner app) {

        app.supprimerUtilisateur(this.pseudo);
    }


    public void ajouterProjet(String nomProjet, String descriptionProjet) {

        listeProjets.add(new Projet(nomProjet,descriptionProjet));
    }


    public void supprimerProjet(Projet p) {

        listeProjets.remove(p);
    }


    public Calendrier getCalendrierAdequat(int day, int month, int year) {

        for (Calendrier c : listeCalendriers) {

            if (c.dateIncluse(day, month, year) == true) {

                return c;

            }
        }
        return null;
    }


    public LocalDate jourPlusRentable() {

        if (listeCalendriers.isEmpty() == false) {

            LocalDate leJour = listeCalendriers.first().jourPlusRentable();

            float rendement = listeCalendriers.first().calculerRendementJournalier(leJour.getDayOfMonth(), leJour.getMonthValue(), leJour.getYear());

            for (Calendrier c : listeCalendriers) {

                float temp = c.calculerRendementJournalier(c.jourPlusRentable().getDayOfMonth(), c.jourPlusRentable().getMonthValue(), c.jourPlusRentable().getYear());

                if (temp >rendement) {

                    rendement = temp;

                    leJour = c.jourPlusRentable();

                }
            }

            return leJour;

        }

        return null;
    }

    /*Made some changes here depending on the today's day*/

    public float calculerMoyenneRendementJournalier() {

        LocalDate today = LocalDate.now();

        Calendrier calendrierAdequat = this.getCalendrierAdequat(today.getDayOfMonth(), today.getMonthValue(), today.getYear());

        /*return(listeCalendriers.last().calculerMoyenneRendementJournalier());*/ // this was the old version

        if (calendrierAdequat == null) {

            return 0;
        }

        return calendrierAdequat.calculerMoyenneRendementJournalier();
    }


    public float calculerRendementJournalier(int day,int month, int year) {

        if (this.getCalendrierAdequat(day, month, year)== null) {

            return 0;

        }
        return(this.getCalendrierAdequat(day, month, year).calculerRendementJournalier(day, month, year));
    }



    public String maxCategorie() {

        String categorie = "";

//		System.out.println("test "+categorie);
//
//		System.out.println("test2 "+listeCategories.size());

        Duration maxDuration = Duration.ofHours(0).plusMinutes(0);

        for (Categorie c : listeCategories) {

            categorie = c.getCategorie();

            if (this.calculerTempsPasse(categorie).compareTo(maxDuration)>0) {

                categorie = c.getCategorie();

                maxDuration = this.calculerTempsPasse(categorie);

            }

        }

        return categorie;
    }


    public Categorie categorieMax(){

        String categorie = "";

        Duration maxDuration = Duration.ofHours(0).plusMinutes(0);

        for (Categorie c : listeCategories) {

            categorie = c.getCategorie();

            if (this.calculerTempsPasse(categorie).compareTo(maxDuration)>0) {

                categorie = c.getCategorie();

                maxDuration = this.calculerTempsPasse(categorie);

            }

        }

        return this.getCategorie(categorie);

    }


    public Duration calculerTempsPasse (String nomCategorie) {

        Duration duration = Duration.ofHours(0).plusMinutes(0);

        for (Calendrier c: listeCalendriers) {

            duration = duration.plus(c.calculerTempsPasse(nomCategorie));
        }
        return duration;
    }

    public Duration calculerTempsPasse (Categorie c) {

        Duration duration = Duration.ofHours(0).plusMinutes(0);

        for (Calendrier cal: listeCalendriers) {

            duration = duration.plus(cal.calculerTempsPasse(c));
        }
        return duration;
    }

    /*Added this method*/

    public float etatDeRealisationDuProjet (String nomProjet) {

        int cptTotal = 0;

        int cptCompleted = 0;

        int [] temp;

        for (Calendrier c : listeCalendriers) {

            temp = c.evaluerProjet(nomProjet);

            cptTotal = cptTotal + temp[0];

            cptCompleted = cptCompleted + temp[1];
        }

        if (cptTotal == 0) {

            return (float)0;

        }

        return ((float)cptCompleted/cptTotal);
    }

    public float etatDeRealisationDuProjet (Projet p) {

        int cptTotal = 0;

        int cptCompleted = 0;

        int [] temp;

        for (Calendrier c : listeCalendriers) {

            temp = c.evaluerProjet(p);

            cptTotal = cptTotal + temp[0];

            cptCompleted = cptCompleted + temp[1];
        }



        if (cptTotal == 0) {

            return (float)0;

        }

        return ((float)cptCompleted/cptTotal);
    }


    /*Add freeZone*/
//    public void ajouterCreneauLibre (int day, int month, int year, int startHour, int startMinutes, int endHour, int endMinutes) {
//
//        Calendrier c = this.getCalendrierAdequat(day, month, year);
//
//        if (c != null) {
//
//            c.ajouterCreneauLibre(day, month, year, startHour, startMinutes, endHour, endMinutes);
//        }
//    }

    public boolean ajouterCreneauLibre (int day, int month, int year, int startHour, int startMinutes, int endHour, int endMinutes) {

        Calendrier c = this.getCalendrierAdequat(day, month, year);

        if (c != null) {

            return(c.ajouterCreneauLibre(day, month, year, startHour, startMinutes, endHour, endMinutes));
        }

        return false;
    }

    public TreeSet<ZoneLibre> getCreneauxLibres(){
        TreeSet<ZoneLibre> freeZone = new TreeSet<ZoneLibre>();
        for (Calendrier c : listeCalendriers){
            freeZone.addAll(c.getCreneauxLibres());
        }
        return freeZone;
    }

    public HashSet<Projet> getListeProjets(){

        return listeProjets;

    }

    /*Stopped adding methods*/

    /*Changed the fact that we don't always use the most recent calendar*/
    public void planifierManuellement(TacheSimple tache, int day, int month, int year, int startHour, int startMinutes, int endHour, int endMinutes) {

        this.getCalendrierAdequat(day, month, year).planifierManuellement(tache, day, month, year, startHour, startMinutes, endHour, endMinutes);
        //listeCalendriers.last().planifierManuellement(tache, day, month, year, startHour, startMinutes, endHour, endMinutes);
    }

    public TreeSet<Calendrier> getTreeCalendrier (){

        return listeCalendriers;

    }


    public void ajouterProjet(Projet p){
        listeProjets.add(p);
    }


    public boolean projetExistant (String p){
        return (listeProjets.contains(new Projet(p,"Test")));
    }


    public void supprimerTache(Tache t)

    {

        listeTaches.remove(t);

    }


    public Projet getProjet(String name){
        for (Projet p: listeProjets){
            if (p.getNom().equals(name)){
                return p;
            }
        }
        return null;
    }

    public TreeSet<Tache> getTreeTaches()

    {

        return listeTaches;

    }

    public void removeCreneauLibre(ZoneLibre zone){
        Calendrier c = this.getCalendrierAdequat(zone.getJour().getDayOfMonth(),zone.getJour().getMonthValue(),zone.getJour().getYear());
        if (c != null){
            c.removeCreneauLibre(zone);
        }
    }

    public TreeSet<Calendrier> getListeCalendriers(){
        return this.listeCalendriers;
    }

    public Calendrier getCalendier(LocalDate debut, LocalDate fin){
        for (Calendrier c: listeCalendriers){
            if (c.getDebutPeriode().equals(debut)&& c.getFinPeriode().equals(fin)){
                return c;
            }
        }
        return null;
    }

    public String getPassword(){
        return this.motDePasse;
    }

    public void planifierAutomatique() {
        TreeSet<Tache> listeTacheSupp=new TreeSet<Tache>();
        Iterator<Tache> iteratorTache=listeTaches.iterator();
        int cpt=0;
        while(iteratorTache.hasNext())
        {
            cpt++;
            Tache t=iteratorTache.next();
            if(t instanceof TacheSimple)
            {
                t.planifactionAutoTacheSimple(this,listeTacheSupp,((TacheSimple) t).getPeriodicite());
            }
            else
            {
                t.planifactionAutoTacheDecomposable(this,listeTacheSupp);
            }
            System.out.println(cpt);
        }
        listeTaches.removeAll(listeTacheSupp);
    }

    public void deleteCalendar(LocalDate start, LocalDate end){
        Calendrier calendar = null;
        for (Calendrier c : listeCalendriers){
            if (c.getDebutPeriode().equals(start)&&c.getFinPeriode().equals(end)){
                calendar = c;
            }
        }
        if (calendar!= null){
            listeCalendriers.remove(calendar);
        }
    }

    public HashSet<Categorie> getTreeCategories()
    {
        return listeCategories;
    }

}
