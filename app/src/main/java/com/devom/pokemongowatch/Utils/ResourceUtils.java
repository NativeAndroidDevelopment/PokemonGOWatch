package com.devom.pokemongowatch.Utils;

import com.devom.pokemongowatch.Models.Pokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Maikel on 09/06/2016.
 */
public class ResourceUtils {

    private static final int COLLECTION = 151;
    private static List<Pokemon> pokedex;

    public static List<Pokemon> getPokedex() {
        if (pokedex == null) {
            pokedex = new ArrayList<>();
            ResourceBundle pokemonNames = ResourceBundle.getBundle("pokemon_names", Locale.ENGLISH);
            for (int i = 1; i <= COLLECTION; i++) {
                String name = pokemonNames.getString(Integer.toString(i));
                pokedex.add(new Pokemon(i, name));
            }
        }
        return pokedex;
    }
}
