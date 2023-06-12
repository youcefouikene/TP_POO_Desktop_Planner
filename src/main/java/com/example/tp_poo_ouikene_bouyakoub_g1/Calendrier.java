package com.example.tp_poo_ouikene_bouyakoub_g1;

import java.io.Serializable;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Calendrier implements Comparable<Calendrier> ,Serializable  {

    private LocalDate debutPeriode;

    private LocalDate finPeriode;

    private TreeSet<Journee> jours;

    private Badge badgeCourant;

    private int nbBadges;

    public LocalDate getDebutPeriode() {

        return debutPeriode;
    }


    public long getNumberOfDays() {

        return ChronoUnit.DAYS.between(debutPeriode, finPeriode)+1;
    }


    public void afficher() {

        System.out.println("Debut Periode: "+debutPeriode);

        System.out.println("Fin Periode: "+finPeriode);

        System.out.println("Badge courant: "+badgeCourant);

        System.out.println("Nombre de Badges: "+nbBadges);

        for (Journee j : jours) {

            System.out.println("---------------------");
            j.afficher();
        }
    }


    public Calendrier (int debutJour, int debutMois, int debutAnnee, int finJour, int finMois, int finAnnee) {

        debutPeriode = LocalDate.of(debutAnnee, debutMois, debutJour);

        finPeriode = LocalDate.of(finAnnee, finMois, finJour);

        jours = new TreeSet<Journee>();

        badgeCourant = Badge.AUCUN;

        nbBadges = 0;

        for (int i = 0; i<this.getNumberOfDays();i++) {

            jours.add(new Journee(debutPeriode.plusDays(i)));

        }

    }

    public void modifierFinPeriode(int annee, int mois, int jour) {

        LocalDate ancienneFinPeriode = finPeriode;

        finPeriode = LocalDate.of(annee, mois, jour);

        if (finPeriode.equals(ancienneFinPeriode)==false) {

            if (finPeriode.isAfter(ancienneFinPeriode)) {

                long daysBetween = ChronoUnit.DAYS.between(ancienneFinPeriode, finPeriode);

                for (int i = 0; i<daysBetween; i++) {

                    jours.add(new Journee(ancienneFinPeriode.plusDays(i+1)));

                }
            }

            else {

                jours.removeIf(j -> j.getJour().isAfter(finPeriode));
            }
        }
    }


    public void modifierDebutPeriode(int annee, int mois, int jour) {

        debutPeriode = LocalDate.of(annee, mois, jour);
    }


    public LocalDate getFinPeriode () {

        return finPeriode;

    }


    /*Made some changes to the compareTo method*/

    public int compareTo(Calendrier o) {

        if ((this.getDebutPeriode().isAfter(o.getDebutPeriode()) || (o.getDebutPeriode().equals(this.getDebutPeriode()))) && (this.getDebutPeriode().isBefore(o.getFinPeriode()))) {

            return 0;

        }

        if ((o.getDebutPeriode().isAfter(this.getDebutPeriode()) || (o.getDebutPeriode().equals(this.getDebutPeriode()))) && (o.getDebutPeriode().isBefore(this.getFinPeriode()))) {

            return 0;

        }

        if (this.debutPeriode.equals(o.getFinPeriode())) {

            return 0;

        }

        if (o.getDebutPeriode().equals(this.finPeriode)) {

            return 0;

        }

        return debutPeriode.compareTo(o.getDebutPeriode());
    }


    public boolean dateIncluse (int jour, int mois, int an) {

        LocalDate dateToCheck = LocalDate.of(an, mois, jour);

        if (((dateToCheck.isEqual(debutPeriode) || dateToCheck.isAfter(debutPeriode)) && (dateToCheck.isEqual(finPeriode) || dateToCheck.isBefore(finPeriode)))){

            return true;

        }

        return false;
    }



    public float calculerRendementJournalier(int day, int month, int year) {

        if (this.dateIncluse(day, month, year) == true) {

            LocalDate dateToCheck = LocalDate.of(year, month, day);

            for (Journee j : jours) {

                if (j.getJour().equals(dateToCheck) == true) {

                    return(j.calculerRendementJournalier());
                }
            }
        }
        return 0;
    }



    public LocalDate jourPlusRentable() {

        if (jours.isEmpty() == false) {

            float max = jours.first().calculerRendementJournalier();

            LocalDate leJour = jours.first().getJour();

            for (Journee j : jours) {

                if (j.calculerRendementJournalier()>max) {

                    max = j.calculerRendementJournalier();

                    leJour = j.getJour();
                }
            }

            return leJour;
        }

        return null;
    }



    public float calculerMoyenneRendementJournalier() {

        float moyenne = 0;

        int i = 0;

        LocalDate currentDay = LocalDate.now();

        for (Journee j: jours) {

            if ((j.getJour().isBefore(currentDay) || j.getJour().equals(currentDay)) && (j.contientAuMoinsUneTache())) {

                moyenne = moyenne + j.calculerRendementJournalier();

                i++;

            }
        }

        if (i==0) {

            return 0;
        }

        return (moyenne/i);
    }


    public Duration calculerTempsPasse (String nomCategorie) {

        Duration duration = Duration.ofHours(0).plusMinutes(0);

        for (Journee j: jours) {

            duration = duration.plus(j.calculerTempsPasse(nomCategorie));
        }
        return duration;
    }

    public Duration calculerTempsPasse (Categorie c) {

        Duration duration = Duration.ofHours(0).plusMinutes(0);

        for (Journee j: jours) {

            duration = duration.plus(j.calculerTempsPasse(c));
        }
        return duration;
    }


    /*Added Many methods*/

    public int [] evaluerProjet(String nomProjet) {

        int cptTotal = 0;

        int cptCompleted = 0;

        int [] values = new int [2];

        int [] temp;

        for (Journee j : jours) {

            temp = j.evaluerProjet(nomProjet);

            cptTotal = cptTotal + temp[0];

            cptCompleted = cptCompleted + temp[1];

        }

        values[0] = cptTotal;

        values[1] = cptCompleted;

        return values;
    }

    public int [] evaluerProjet(Projet p) {

        int cptTotal = 0;

        int cptCompleted = 0;

        int [] values = new int [2];

        int [] temp;

        for (Journee j : jours) {

            temp = j.evaluerProjet(p);

            cptTotal = cptTotal + temp[0];

            cptCompleted = cptCompleted + temp[1];

        }

        values[0] = cptTotal;

        values[1] = cptCompleted;

        return values;
    }

//    public void ajouterCreneauLibre (int day, int month, int year, int startHour, int startMinutes, int endHour, int endMinutes) {
//
//        LocalDate date = LocalDate.of(year, month, day);
//
//        for (Journee j: jours) {
//
//            if (j.getJour().equals(date)) {
//
//                j.ajouterCreneau(new Creneau(startHour,startMinutes,endHour,endMinutes));
//
//                break;
//            }
//        }
//    }

    public boolean ajouterCreneauLibre (int day, int month, int year, int startHour, int startMinutes, int endHour, int endMinutes) {

        LocalDate date = LocalDate.of(year, month, day);

        for (Journee j: jours) {

            if (j.getJour().equals(date)) {

                return(j.ajouterCreneau(new Creneau(startHour,startMinutes,endHour,endMinutes)));

            }
        }

        return false;
    }

    public TreeSet<ZoneLibre> getCreneauxLibres(){
        TreeSet<ZoneLibre> ensembleFinal = new TreeSet<ZoneLibre>();
        if (this.jours.isEmpty() == false){
            for (Journee j : jours){
                ensembleFinal.addAll(j.getCreneauxLibres());
            }
        }
        return ensembleFinal;
    }

    public int totalCompletedTasks() {

        int cpt = 0;

        for (Journee j : jours) {

            cpt = cpt + j.totalCompletedTasks();

        }

        return cpt;
    }

    public int tasksCompletedDay (LocalDate day){
        for (Journee j: jours){
            if (j.getJour().equals(day)){
                return j.totalCompletedTasks();
            }
        }
        return 0;
    }

    public int totalTasks (LocalDate day){
        for (Journee j: jours){
            if (j.getJour().equals(day)){
                return j.totalTasks();
            }
        }
        return 0;
    }


    public float rendementDay (LocalDate day){
        for (Journee j: jours){
            if (j.getJour().equals(day)){
                return j.calculerRendementJournalier();
            }
        }
        return 0;
    }

    public HashSet<Projet> getListeProjetsCompletes(Utilisateur u){

        HashSet<Projet> allProjets = u.getListeProjets();

        HashSet<Projet> completedProjects = new HashSet<Projet>();

        int [] values = new int [2];

        for (Projet p : allProjets) {

            values = this.evaluerProjet(p.getNom());

            if (values[1] !=0) {

                if (values[1]/values[0] == 1) {

                    completedProjects.add(p);
                }
            }
        }
        return completedProjects;
    }


    public int numberCompletedProjects(Utilisateur u) {

        return this.getListeProjetsCompletes(u).size();
    }


    public int nombreEncouragements(int objectif) {

        int cpt = 0;

        for (Journee j : jours) {

            cpt = cpt + j.atteindreSeuil(objectif);

        }

        return cpt;
    }


    public int nombrePoints(int objectif) {

        int cpt = 0;

        LocalDate today = LocalDate.now();

        for (Journee j : jours) {

            if (j.getJour().isBefore(today) && j.contientAuMoinsUneTache() == true) {

                if (j.atteindreSeuil(objectif)==1) {

                    cpt++;
                }

                else {

                    cpt--;
                }
            }
        }

        if (cpt<0) {

            return 0;
        }

        return cpt;
    }


    public HashMap<Badge,Integer> statistiquesBadges(int objectif){

        HashMap<Badge,Integer> stat = new HashMap<Badge,Integer>();

        int points = this.nombrePoints(objectif);

        if (points>= 0 && points <5) {

            stat.put(Badge.AUCUN, points);

            stat.put(Badge.GOOD, 0);

            stat.put(Badge.VERYGOOD, 0);

            stat.put(Badge.EXCELLENT, 0);
        }

        if (points>= 5 && points <10) {

            stat.put(Badge.AUCUN, 5);

            stat.put(Badge.GOOD, 1);

            stat.put(Badge.VERYGOOD, 0);

            stat.put(Badge.EXCELLENT, 0);
        }

        if (points>= 10 && points <15) {

            stat.put(Badge.AUCUN, 5);

            stat.put(Badge.GOOD, 2);

            stat.put(Badge.VERYGOOD, 0);

            stat.put(Badge.EXCELLENT, 0);
        }

        if (points>= 15 && points <30) {

            stat.put(Badge.AUCUN, 5);

            stat.put(Badge.GOOD, 3);

            stat.put(Badge.VERYGOOD, 1);

            stat.put(Badge.EXCELLENT, 0);
        }

        if (points>= 30 && points <45) {

            stat.put(Badge.AUCUN, 5);

            stat.put(Badge.GOOD, 3);

            stat.put(Badge.VERYGOOD, 2);

            stat.put(Badge.EXCELLENT, 0);
        }

        if (points>= 45) {

            stat.put(Badge.AUCUN, 5);

            stat.put(Badge.GOOD, 3);

            stat.put(Badge.VERYGOOD, 3);

            stat.put(Badge.EXCELLENT, points/45);
        }

        return stat;
    }

    /*Fin ajouter nouvelles methodes*/

    public void planifierManuellement(TacheSimple tache, int day, int month, int year, int startHour, int startMinutes, int endHour, int endMinutes) {

        if (this.dateIncluse(day, month, year) == true) {

            LocalDate dateToCheck = LocalDate.of(year, month, day);

            for (Journee j : jours) {

                if (j.getJour().equals(dateToCheck) == true) {

                    j.planifierManuellement(tache, startHour, startMinutes, endHour, endMinutes);

//                    dateToCheck = dateToCheck.plusDays(tache.getPeriodicite());

                    //tache.setDateLimite(tache.getDateLimite().plusDays(tache.getPeriodicite()));
                }
            }
        }
    }

    public Journee getJournee(int jour, int mois, int annee)

    {

        Journee j=null;

        for (Journee jourr : jours) {

            if(jourr.getJour().getDayOfMonth()==jour && jourr.getJour().getMonthValue()==mois && jourr.getJour().getYear()==annee )

            {

                j=jourr;

                break;

            }

        }

        return j;

    }

    public TreeSet<Journee> getTreeJournees()

    {

        return jours;

    }

    public void removeCreneauLibre(ZoneLibre zone){
        this.getJournee(zone.getJour().getDayOfMonth(),zone.getJour().getMonthValue(),zone.getJour().getYear()).removeCreneauLibre(zone);
    }

    public Journee getJour(LocalDate day){
        for (Journee j : jours){
            if (j.getJour().equals(day)){
                return j;
            }
        }
        return null;
    }

    public int totalTasks(){
        int cpt = 0;
        for (Journee j: jours){
            cpt = cpt + j.totalTasks();
        }
        return cpt;
    }



}
