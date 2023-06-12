package com.example.tp_poo_ouikene_bouyakoub_g1;

import java.time.LocalDate;
import java.time.LocalTime;

public class ZoneLibre implements Comparable<ZoneLibre> {

    private LocalTime horaireDebut;

    private LocalTime horaireFin;

    private LocalDate jour;

    public ZoneLibre(LocalDate jour, LocalTime horaireDebut, LocalTime horaireFin){
        this.jour = jour;
        this.horaireDebut = horaireDebut;
        this.horaireFin = horaireFin;
    }

    public LocalTime getHoraireDebut (){
        return horaireDebut;
    }

    public LocalTime getHoraireFin (){
        return horaireFin;
    }

    public LocalDate getJour(){
        return jour;
    }


    @Override
    public int compareTo(ZoneLibre o) {
        if (this.jour.equals(o.getJour())){
            return (this.horaireDebut.compareTo(o.getHoraireDebut()));
        }
        else {
            return (this.jour.compareTo(o.getJour()));
        }
    }
}
