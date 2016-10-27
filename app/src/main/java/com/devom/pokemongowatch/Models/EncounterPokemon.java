package com.devom.pokemongowatch.Models;

/**
 * Created by Maikel on 08/15/2016.
 */
public class EncounterPokemon extends Pokemon {

    //<editor-fold desc="Variables">
    private long encounterId;
    private double distance;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public EncounterPokemon(long encounterId, int number, String name, double distance) {
        super(number, name);
        this.encounterId = encounterId;
        this.distance = distance;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public long getEncounterId() {
        return encounterId;
    }

    public double getDistance() {
        return distance;
    }
    //</editor-fold>
}
