package com.example.tp_poo_ouikene_bouyakoub_g1;

import java.io.Serializable;

public class Projet implements Serializable {

    private String nom;

    private String description;

    public Projet (String nom, String description) {

        this.nom = nom;

        this.description = description;
    }


    public String getNom() {

        return nom;
    }


    public String getDescription() {

        return description;
    }


    public void setNom(String nom) {

        this.nom = nom;
    }


    public void afficher() {

        System.out.println("Nom Projet: "+nom);
        System.out.println("Description Projet: "+description);
    }



    public void setDescription(String description) {

        this.description = description;
    }


//    public int hashCode() {
//
//        return (this.nom.hashCode());
//    }
//
//
//    public boolean equals(Object o) {
//
//        if (o instanceof Projet ) {
//
//            Projet p = (Projet) o;
//
//            return (p.getNom().equalsIgnoreCase(nom));
//        }
//
//        else {
//
//            return false;
//        }
//    }
}
