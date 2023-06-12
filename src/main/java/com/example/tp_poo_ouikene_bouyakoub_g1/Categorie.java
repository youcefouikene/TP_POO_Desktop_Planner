package com.example.tp_poo_ouikene_bouyakoub_g1;

import java.io.Serializable;

public class Categorie implements Serializable {

    private String categorie;

    private String color;

    public Categorie(String categorie, String color) {

        this.categorie = categorie;

        this.color = color;

    }

    public String getCategorie() {

        return categorie;

    }

    public String getColor() {

        return color;

    }

    public void setCategorie(String categorie) {

        this.categorie = categorie;

    }

    public void setColor(String color) {

        this.color = color;

    }


    public void afficher() {

        System.out.println("Nom de la categorie: "+categorie);
        System.out.println("Coleur de la categorie: "+color);
    }


//    public int hashCode() {
//
//        return ((this.categorie.toLowerCase()).hashCode());
//
//    }
//
//    public boolean equals(Object o) {
//
//        if (o instanceof Categorie) {
//
//            Categorie c = (Categorie) o;
//
//            return c.getCategorie().equalsIgnoreCase(this.categorie);
//
//        }
//        return false;
//    }
}
