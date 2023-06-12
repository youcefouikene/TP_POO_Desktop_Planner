package com.example.tp_poo_ouikene_bouyakoub_g1;

/*Added import statement for serializable*/

import java.util.*;
import java.io.*;

public class DesktopPlanner implements Serializable {

    private Set<Utilisateur> listeUtilisateurs;

    /*Modified constructor*/

    public DesktopPlanner () {

        listeUtilisateurs = new HashSet<Utilisateur>();

        try {

            FileInputStream fileIn = new FileInputStream("sauv.txt");

            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            try {

                while (true) {

                    Object obj = objectIn.readObject();

                    if (obj instanceof Utilisateur) {

                        Utilisateur u = (Utilisateur) obj;

                        this.ajouterUtilisateur(u);
                    }
                }

            } catch (EOFException e) {

                // End of file reached, no need to print anything
            } catch (ClassNotFoundException e) {

                System.out.println("Error: Class not found while deserializing.");

                e.printStackTrace();
            }

            objectIn.close();

            fileIn.close();

        } catch (IOException e) {

            System.out.println("Error: Failed to read the file.");

        }
    }

    /*Modified this*/
    public void ajouterUtilisateur(String pseudo, String motDePasse) {

        if (this.compteExistant(pseudo)==false) {

            listeUtilisateurs.add(new Utilisateur(pseudo,motDePasse));
        }
    }


    public void supprimerUtilisateur(String nomUser) {

        Iterator<Utilisateur> iterator = listeUtilisateurs.iterator();

        while (iterator.hasNext()) {

            Utilisateur user = iterator.next();

            if (user.getPseudo().equals(nomUser)) {

                iterator.remove();
            }
        }
    }


    public void afficherUtilisateurs() {

        if (listeUtilisateurs.size() == 0) {

            System.out.println("Liste vide!");

        }

        else {

            for (Utilisateur user: listeUtilisateurs) {

                System.out.println("Pseudo: "+user.getPseudo()+", Mot de passe: "+user.getMotDePasse());

            }
        }
    }


    /*Modified seConnecter because idk where's the issue*/
    public Utilisateur seConnecter(String pseudo, String motDePasse) {

        for (Utilisateur user : listeUtilisateurs) {

            if (user.getPseudo().equals(pseudo) && user.getMotDePasse().equals(motDePasse)) {

                return user;
            }
        }
        return null;
    }


    /*Added this method*/

    public void sauvegarderInfosUtilisateurs() {

        try {
            // Create FileOutputStream to write to a file
            FileOutputStream fileOut = new FileOutputStream("sauv.txt");

            // Create ObjectOutputStream to write objects
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            for (Utilisateur u: listeUtilisateurs) {

                objectOut.writeObject(u);

            }

            // close streams after treatment
            objectOut.close();
            fileOut.close();

        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean compteExistant(String pseudo) {

        for (Utilisateur u : listeUtilisateurs) {

            if (u.getPseudo().equals(pseudo)) {

                return true;

            }

        }
        return false;
    }


    public void ajouterUtilisateur(Utilisateur u) {

        if (this.compteExistant(u.getPseudo())==false) {

            listeUtilisateurs.add(u);
        }

    }

    /*End of adding methods*/
}
