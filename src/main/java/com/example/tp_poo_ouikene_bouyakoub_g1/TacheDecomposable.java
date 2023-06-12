package com.example.tp_poo_ouikene_bouyakoub_g1;

import java.io.Serializable;

public class TacheDecomposable extends Tache implements Serializable {

    public TacheDecomposable (String nom, int heures, int minutes, Priorite priorite, int jour, int mois, int annee, Categorie categorie, EtatDeRealisation etat)

    {

        super( nom, heures, minutes, priorite, jour, mois, annee, categorie, etat);

    }

}
