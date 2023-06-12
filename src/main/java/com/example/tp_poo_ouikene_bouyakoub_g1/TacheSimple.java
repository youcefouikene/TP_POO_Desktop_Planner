package com.example.tp_poo_ouikene_bouyakoub_g1;

import java.io.Serializable;

public class TacheSimple extends Tache implements Serializable  {


        private int periodicite = 0;

        private Projet project;

        private TacheDecomposable td;


        public void afficher() {

            super.afficher();
            if (project != null){
                System.out.print("Projet de la tache: ");
                project.afficher();
            }
            System.out.println("Periodicite: "+periodicite);
        }


        public TacheSimple(String nom, int heures, int minutes, Priorite priorite, int jour, int mois, int annee, Categorie categorie, EtatDeRealisation etat, Projet projet, int periodicite) {

            super(nom,heures,minutes,priorite,jour,mois,annee,categorie,etat);

            this.project = projet;

            this.periodicite = periodicite;

        }

        public TacheSimple(String nom, Categorie categorie, Projet project){
            super(nom,categorie);
            this.project = project;
            td = null;
        }


        public int getPeriodicite() {

            return periodicite;
        }

        public void setPeriodicite(int periodicite) {

            this.periodicite = periodicite;
        }

        public void setProjet(Projet p) {

            project = p;
        }

        public Projet getProjet() {

            return project;
        }
}


