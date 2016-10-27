package com.devom.pokemongowatch.Utils;

import com.devom.pokemongowatch.Models.EncounterPokemon;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.util.PokeNames;

import java.util.Locale;

/**
 * Created by Maikel on 08/30/2016.
 */
public class ParseUtils {

    public static EncounterPokemon parsePokemon(CatchablePokemon catchablePokemon, double distance) {
        long id = catchablePokemon.getEncounterId();
        int number = catchablePokemon.getPokemonId().getNumber();
        String name = PokeNames.getDisplayName(number, Locale.ENGLISH);
        //long despawnTime = catchablePokemon.getExpirationTimestampMs();
        return new EncounterPokemon(id, number, name, distance);
    }
}
