package com.example.tp_poo_ouikene_bouyakoub_g1;

import java.io.Serializable;
import java.time.*;

public class Creneau implements Comparable<Creneau>, Serializable {

    private LocalTime horaireDebut;

    private LocalTime horaireFin;

    private TacheSimple tacheAllouee;

    private boolean bloquer;


    public Creneau(int heureDebut, int minutesDebut, int heureFin, int minutesFin) {

        horaireDebut = LocalTime.of(heureDebut, minutesDebut);

        horaireFin = LocalTime.of(heureFin, minutesFin);

        tacheAllouee = null;
    }


    public TacheSimple getTache() {

        return tacheAllouee;
    }


    public void setHoraireDebut(int hours, int minutes) {

        horaireDebut = LocalTime.of(hours, minutes);
    }


    public void setHorairFin(int hours, int minutes) {

        horaireFin = LocalTime.of(hours, minutes);
    }



    public boolean estLibre() {

        return (tacheAllouee == null);
    }



    public LocalTime getHoraireDebut() {

        return horaireDebut;
    }


    public LocalTime getHoraireFin() {

        return horaireFin;
    }


    public void allouerTache(TacheSimple tache) {

        tacheAllouee = tache;
    }


    public void afficher() {

        System.out.println("Horaire Debut: "+horaireDebut);
        System.out.println("Horaire Fin: "+horaireFin);
        System.out.println("Duree: "+Duration.between(horaireDebut, horaireFin).toMinutes());
        System.out.println("Tache Allouee: ");
        if (tacheAllouee!= null) {
            tacheAllouee.afficher();
        }
    }


    public void setTache(TacheSimple tache)

    {

        tacheAllouee=(TacheSimple) tache;

    }


    public boolean decomposer(Journee j, TacheSimple task, Utilisateur user) {

        Duration dureeCurrentCreneau = Duration.between(horaireDebut, horaireFin);

        if (dureeCurrentCreneau.toMinutes() >= task.getDuree().getMinute()) {

            tacheAllouee = task;

            if (dureeCurrentCreneau.toMinutes() - (task.getDuree().getHour()*60+task.getDuree().getMinute()) >= user.getDureeMinimaleCreneau().getMinute()) {

                LocalTime sauvHoraireFin = horaireFin;

                horaireFin = horaireDebut.plus(Duration.between(LocalTime.MIN, task.getDuree()));

                LocalTime newHoraireDebut = horaireFin;

                LocalTime newHoraireFin = sauvHoraireFin;

                j.ajouterCreneau(new Creneau(newHoraireDebut.getHour(),newHoraireDebut.getMinute(),newHoraireFin.getHour(),newHoraireFin.getMinute()));

            }

            else {

                System.out.println("Pas de decomposition");

            }

            return true;

        }

        return false;

    }


    public Duration getDuree()

    {

        return Duration.between(horaireDebut, horaireFin);

    }



    /*Made some changes par rapport l version of youcef in this compareTo method*/

    public int compareTo(Creneau o) {

        /*Made some changes in this condition*/

        if ((this.getHoraireDebut().isAfter(o.getHoraireDebut()) || (o.getHoraireDebut().equals(horaireDebut))) && (this.getHoraireDebut().isBefore(o.getHoraireFin()))) {

            return 0;

        }

        if ((o.getHoraireDebut().isAfter(this.getHoraireDebut()) || (o.getHoraireDebut().equals(horaireDebut))) && (o.getHoraireDebut().isBefore(this.getHoraireFin()))) {

            return 0;

        }

        Duration duree1 = Duration.between(horaireDebut, horaireFin);

        Duration duree2 = Duration.between(o.getHoraireDebut(), o.getHoraireFin());

        if (duree1.compareTo(duree2)==0) {

            return (horaireDebut.compareTo(o.getHoraireDebut()));

        }
        else {

            return (duree1.compareTo(duree2));
        }
    }



}
