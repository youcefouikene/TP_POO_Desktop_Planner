package com.example.tp_poo_ouikene_bouyakoub_g1;

import java.time.LocalDate;
import java.time.LocalTime;

public class Secours {
    private LocalDate date;
    private LocalTime creneauDebut;
    private LocalTime creneauFin;
    private TacheSimple tache;

    public Secours(LocalDate date, LocalTime creneauDebut, LocalTime creneauFin, TacheSimple tache) {
        this.date = date;
        this.creneauDebut = creneauDebut;
        this.creneauFin = creneauFin;
        this.tache = tache;
    }

    public void afficher()
    {
        System.out.println(date);
        System.out.println(creneauDebut);
        System.out.println(creneauFin);
        if(tache!=null)tache.afficher();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getCreneauDebut() {
        return creneauDebut;
    }

    public void setCreneauDebut(LocalTime creneauDebut) {
        this.creneauDebut = creneauDebut;
    }

    public LocalTime getCreneauFin() {
        return creneauFin;
    }

    public void setCreneauFin(LocalTime creneauFin) {
        this.creneauFin = creneauFin;
    }

    public TacheSimple getTache() {
        return tache;
    }

    public void setTache(TacheSimple tache) {
        this.tache = tache;
    }
}
