package com.example.tp_poo_ouikene_bouyakoub_g1;

import java.io.Serializable;
import java.time.*;
import java.util.*;

public class Journee implements Comparable<Journee>, Serializable {

    private LocalDate jour;

    private TreeSet<Creneau> listeCreneaux;

    /*Changed here because i am stupid*/

    public Journee(int year, int month, int day) {

        jour = LocalDate.of(year, month, day);

        listeCreneaux = new TreeSet<Creneau>();
    }


    /*Added this method*/

    public int[] evaluerProjet(String nomProjet) {

        int cptTotal = 0;

        int cptCompleted = 0;

        int [] values = new int[2];

        if (listeCreneaux.isEmpty() == false) {

            for (Creneau c : listeCreneaux) {

                if (c.estLibre() == false) {

                    if (c.getTache().getProjet() != null){

                        if(c.getTache().getProjet().getNom().equalsIgnoreCase(nomProjet)== true) {

                            cptTotal++;

                            if (c.getTache().getEtat() == EtatDeRealisation.COMPLETED) {

                                cptCompleted++;

                            }
                        }
                    }
                }
            }
        }

        values[0] = cptTotal;

        values[1] = cptCompleted;

        return values;
    }

    public int[] evaluerProjet(Projet p) {

        int cptTotal = 0;

        int cptCompleted = 0;

        int [] values = new int[2];

        if (listeCreneaux.isEmpty() == false) {

            for (Creneau c : listeCreneaux) {

                if (c.estLibre() == false) {

                    if (c.getTache().getProjet() != null){

                        if(c.getTache().getProjet() == (p)) {

                            cptTotal++;

                            if (c.getTache().getEtat() == EtatDeRealisation.COMPLETED) {

                                cptCompleted++;

                            }
                        }
                    }
                }
            }
        }

        values[0] = cptTotal;

        values[1] = cptCompleted;

        return values;
    }

    public int totalCompletedTasks() {

        int cptCompleted = 0;

        if (listeCreneaux.isEmpty() == false) {

            for (Creneau c : listeCreneaux) {

                if (c.estLibre() == false) {

                    if (c.getTache().getEtat() == EtatDeRealisation.COMPLETED) {

                        cptCompleted++;

                    }
                }
            }
        }

        return cptCompleted;
    }

    public int atteindreSeuil(int objectif) {

        int cpt = 0;

        for (Creneau c: listeCreneaux) {

            if (c.estLibre() == false) {

                if (c.getTache().getEtat() == EtatDeRealisation.COMPLETED) {

                    cpt++;
                }
            }
        }

        if (cpt >= objectif) {

            return 1;

        }

        return 0;
    }

    /* Stopped adding methods*/

    public boolean contientAuMoinsUneTache () {

        for (Creneau c : listeCreneaux) {

            if (c.estLibre() == false) {

                return true;

            }
        }

        return false;
    }

    public Journee(LocalDate jour) {

        this.jour = jour;

        listeCreneaux = new TreeSet<Creneau>();
    }


    public void afficher() {

        System.out.println("Jour: "+jour);

        System.out.println("Liste des Creneaux: ");

        for (Creneau c : listeCreneaux) {

            System.out.println("---------------------");
            c.afficher();
        }
    }


    public LocalDate getJour() {

        return jour;
    }


    public int compareTo(Journee o) {

        return (jour.compareTo(o.getJour()));
    }


//    public void ajouterCreneau(Creneau c) {
//
//        listeCreneaux.add(c);
//    }

    public boolean ajouterCreneau(Creneau c) {

        return(listeCreneaux.add(c));
    }


    //public void ajouterCreneau()


    public void supprimerCreneau(Creneau c) {

        listeCreneaux.remove(c);
    }



    public float calculerRendementJournalier() {

        int j = 0;

        int i = 0;

        for (Creneau C : listeCreneaux) {

            if (C.estLibre() == false) {

                i++;

                if (C.getTache().getEtat() == EtatDeRealisation.COMPLETED) {

                    j++;

                }
            }
        }

        if (i == 0) {

            return 0;
        }

        return ((float)j/i);
    }


    public Duration calculerTempsPasse (String nomCategorie) {

        Duration duration = Duration.ofHours(0).plusMinutes(0);

        for (Creneau C : listeCreneaux) {

            if (C.estLibre() == false){

                if (C.getTache().getCategorie() != null){

                    if (C.getTache().getCategorie().getCategorie().equalsIgnoreCase(nomCategorie)) {

                        Duration timeAsDuration = Duration.ofHours(C.getTache().getDuree().getHour()).plusMinutes(C.getTache().getDuree().getMinute());

                        duration = duration.plus(timeAsDuration);

                    }
                }
            }
        }
        return duration;
    }

    public Duration calculerTempsPasse (Categorie c) {

        Duration duration = Duration.ofHours(0).plusMinutes(0);

        for (Creneau C : listeCreneaux) {

            if (C.estLibre() == false){

                if (C.getTache().getCategorie() != null){

                    if (C.getTache().getCategorie() == (c)) {

                        Duration timeAsDuration = Duration.ofHours(C.getTache().getDuree().getHour()).plusMinutes(C.getTache().getDuree().getMinute());

                        duration = duration.plus(timeAsDuration);

                    }
                }
            }
        }
        return duration;
    }


    public void planifierManuellement(TacheSimple tache, int startHour, int startMinutes, int endHour, int endMinutes) {

        Creneau c = new Creneau(startHour, startMinutes, endHour, endMinutes);

        LocalTime time1 = LocalTime.of(startHour, startMinutes);

        LocalTime time2 = LocalTime.of(endHour, endMinutes);

        Duration duration = Duration.between(time1, time2);

        tache.setDuree((int)duration.toHours(), (int) duration.toMinutes() % 60);

        c.allouerTache(tache);

        listeCreneaux.add(c);
    }

    public TreeSet<ZoneLibre> getCreneauxLibres(){
        ZoneLibre zone;
        TreeSet<ZoneLibre> ensemble = new TreeSet<ZoneLibre>();
        if (this.listeCreneaux.isEmpty() == false){
            for (Creneau c : listeCreneaux){
                if (c.estLibre() == true){
                    zone = new ZoneLibre(this.getJour(),c.getHoraireDebut(),c.getHoraireFin());
                    ensemble.add(zone);
                }
            }
        }
        return ensemble;
    }

    public void removeCreneauLibre(ZoneLibre zone){
        Creneau creneauToRemove = null;
        for (Creneau c : listeCreneaux){
            if (c.getHoraireDebut().equals(zone.getHoraireDebut()) && c.getHoraireFin().equals(zone.getHoraireFin())){
                creneauToRemove = c;
                break;
            }
        }
        if (creneauToRemove != null){
            listeCreneaux.remove(creneauToRemove);
        }
    }

    public int totalTasks(){
        int cpt = 0;
        for (Creneau c : listeCreneaux){
            if (c.getTache() != null){
                cpt++;
            }
        }
        return cpt;
    }

    public TreeSet<Creneau> getTreeCreneaux()

    {

        return listeCreneaux;

    }

}
