package com.devom.pokemongowatch.Models;

/**
 * Created by Maikel on 09/06/2016.
 */
public class Pokemon {
    private int number;
    private String name;

    public Pokemon(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }


    public String getName() {
        return name;
    }


    public String getPokedexNumber() {
        return String.format("#%03d", number);
    }

    @Override
    public String toString() {
        return name;
    }
}
