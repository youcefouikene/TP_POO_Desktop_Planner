package com.example.tp_poo_ouikene_bouyakoub_g1;

import java.io.Serializable;
import java.time.*;
import java.util.*;

public class Tache implements Comparable<Tache>, Serializable  {

    protected String nom;

    protected LocalTime duree;

    protected Priorite priorite;

    protected LocalDate dateLimite;

    protected Categorie categorie;

    protected EtatDeRealisation etat;


    public Tache(String nom, Categorie categorie){
        this.nom = nom;
        this.categorie = categorie;
        etat = EtatDeRealisation.NOTREALIZED;
        priorite = null;
        dateLimite = null;
    }


    public Tache() {

    }


    public Categorie getCategorie() {

        return categorie;

    }


    public EtatDeRealisation getEtat() {

        return etat;
    }


    public Tache(String nom, Categorie categorie, EtatDeRealisation etat) {

        this.nom = nom;
        this.categorie = categorie;
        this.etat = etat;
    }

    public LocalDate getDateLimite() {

        return dateLimite;
    }

    public String getNom() {

        return nom;
    }


    public Tache(String nom, int heures, int minutes, Priorite priorite, int jour, int mois, int annee, Categorie categorie, EtatDeRealisation etat) {

        this.nom = nom;

        duree = LocalTime.of(heures, minutes);

        this.priorite = priorite;

        dateLimite = LocalDate.of(annee, mois, jour);

        this.categorie = categorie;

        this.etat = etat;
    }



    public Priorite getPriorite() {

        return priorite;
    }


    public void setPriorite(Priorite priorite) {

        this.priorite = priorite;
    }

    public void setDateLimite(LocalDate dateLimite) {

        this.dateLimite = dateLimite;
    }



    public void afficher() {

        System.out.println("Nom de la tache: "+nom);
        System.out.println("Duree de la tache: "+duree);
        System.out.println("Priorite de la tache: "+priorite);
        System.out.println("Date Limite de la tache: "+dateLimite);
        if (this.categorie != null){
            System.out.print("Categorie de la tache: ");
            categorie.afficher();
        }
        System.out.println("Etat de realisation de la tache: "+etat);
    }


    public LocalTime getDuree() {

        return duree;
    }



    public void setDuree(int hours, int minutes) {

        duree = LocalTime.of(hours, minutes);
    }


    public int compareTo(Tache o) {

        if(this.priorite.compareTo(o.getPriorite())<0) {

            return 1;
        }

        else {

            if(this.priorite.compareTo(o.getPriorite())>0) {

                return -1;

            }

            else {

                if (this.dateLimite.isBefore(o.getDateLimite())) {

                    return -1;

                }

                else {

                    if (this.dateLimite.compareTo(o.getDateLimite())==0) {

                        return (this.nom.compareTo(o.getNom()));

                    }
                    else {

                        return 1;
                    }
                }
            }
        }
    }


    public void setCategorie(Categorie c) {

        categorie = c;
    }


    public void planifactionAutoTacheSimple(Utilisateur user, TreeSet<Tache> listeTacheSupp,int periodicite)

    {

        Calendrier cal=user.getTreeCalendrier().last();

        List<Creneau> creneaus= new ArrayList<>();

        TreeSet<Journee> jours =cal.getTreeJournees();

        boolean trouv =false;

        boolean stop =false;

        LocalDate day = null;

        Iterator<Journee> iteratorJournee=jours.iterator();

        while(iteratorJournee.hasNext())

        {

            Journee j=iteratorJournee.next();

            TreeSet<Creneau> set = j.getTreeCreneaux();

            Iterator<Creneau> iteratorCreneau=set.iterator();

            while(iteratorCreneau.hasNext() && !stop)

            {

                Creneau c=iteratorCreneau.next();

                if(c.getDuree().toMinutes()>=(duree.getHour()*60+duree.getMinute()) && c.getTache()==null && (dateLimite.isEqual(j.getJour()) || dateLimite.isAfter(j.getJour())))

                {

                    if (trouv == false){

                        day = j.getJour();

                        trouv=true;

                        if(periodicite==0)
                        {
                            stop=true;
                        }

                        c.decomposer(j, (TacheSimple) this, user);
                    }

                    else{

                        LocalDate dateAfterPeriodicite = day.plusDays(periodicite);

                        if (j.getJour().equals(dateAfterPeriodicite)||j.getJour().isAfter(dateAfterPeriodicite)){

                            c.decomposer(j, (TacheSimple) this, user);

                            day = j.getJour();
                        }

                    }

                    listeTacheSupp.add(this);

                }
            }
        }
    }

    public void planifactionAutoTacheDecomposable(Utilisateur user,TreeSet<Tache> listeTacheSupp)
    {
        Calendrier cal=user.getTreeCalendrier().last();
        TreeSet<Journee> jours =cal.getTreeJournees();
        TreeSet<Creneau> creneaux=new TreeSet<Creneau>();
        Duration dureeDesCreneauxLibres=Duration.ZERO;
        long dureeTache=duree.getHour()*60+duree.getMinute();
        int cpt=0;

        for(Journee j : jours)
        {
            for(Creneau c : j.getTreeCreneaux())
            {
                if(c.getTache()==null && (dateLimite.isEqual(j.getJour()) || dateLimite.isAfter(j.getJour())))
                {
                    creneaux.add(c);
                    dureeDesCreneauxLibres=dureeDesCreneauxLibres.plus(c.getDuree());
                }
            }
        }
        if(dureeDesCreneauxLibres.toMinutes()>=dureeTache)
        {
            Iterator<Creneau> Descending = creneaux.descendingIterator();
            while(Descending.hasNext() && dureeTache!=0)
            {
                Creneau c=Descending.next();
                Journee jourr=null;
                if(c.getDuree().toMinutes()>dureeTache) {
                    TacheSimple tache= new TacheSimple(nom, (int) (dureeTache/60), (int) (dureeTache%60), priorite, dateLimite.getDayOfMonth(), dateLimite.getMonthValue(), dateLimite.getYear(), categorie, etat, null, 0);
                    for(Journee j : jours)
                    {
                        for(Creneau ca : j.getTreeCreneaux())
                        {
                            if(ca==c)
                            {
                                jourr=j;
                            }
                        }
                    }
                    c.decomposer(jourr, tache, user);
                    dureeTache=0;
                }
                else if(c.getDuree().toMinutes()==dureeTache)
                {
                    cpt++;
                    TacheSimple tache= new TacheSimple((nom+Integer.toString(cpt)), (int) (dureeTache/60), (int) (dureeTache%60), priorite, dateLimite.getDayOfMonth(), dateLimite.getMonthValue(), dateLimite.getYear(), categorie, etat, null, 0);
                    c.setTache(tache);
                    dureeTache=0;
                }
                else
                {
                    cpt++;
                    TacheSimple tache= new TacheSimple((nom+Integer.toString(cpt)) , (int) (c.getDuree().toMinutes()/60), (int) (c.getDuree().toMinutes()%60), priorite, dateLimite.getDayOfMonth(), dateLimite.getMonthValue(), dateLimite.getYear(), categorie, etat, null, 0);
                    c.setTache(tache);
                    dureeTache=dureeTache-c.getDuree().toMinutes();
                }

            }
            //user.supprimerTache(this);
            listeTacheSupp.add(this);
        }
    }
    //Ajoutée
    void setNom(String nom){
        this.nom=nom;
    }

    public void setEtat(EtatDeRealisation etat) {
        this.etat = etat;
    }


}
