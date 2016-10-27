package com.devom.pokemongowatch.Utils;

import android.content.Context;
import android.util.Log;

import com.devom.pokemongowatch.Managers.NotificationManager;
import com.devom.pokemongowatch.Managers.SettingsManager;
import com.devom.pokemongowatch.Models.EncounterPokemon;
import com.pokegoapi.util.PokeNames;

import java.util.Locale;
import java.util.Random;
import java.util.Set;

/**
 * Created by Maikel on 08/16/2016.
 */
public class DebugUtils {
    private static final String TAG = DebugUtils.class.getSimpleName();

    private static Random r = new Random();

    public static void emulatePokemonSpawn(Context context) {
        int number = r.nextInt(151) + 1;
        long id = r.nextLong();
        String name = PokeNames.getDisplayName(number, Locale.ENGLISH);
        EncounterPokemon p = new EncounterPokemon(id, number, name, System.currentTimeMillis() + 120000);

        SettingsManager manager = SettingsManager.getInstance(context);
        if (manager.isFilter()) {
            Set<String> filterSet = manager.getFilterSet();
            Log.d(TAG, "Filter enabled : " + filterSet);
            if (!filterSet.contains(p.getPokedexNumber())) {
                Log.d(TAG, "NEW : " + p.getEncounterId() + " : " + p.getName());
                NotificationManager.getInstance(context).showPokemonNotification(context, p, manager.isVibrate(), manager.getRingtone());
            } else {
                Log.d(TAG, "FILTERED : " + p.getEncounterId() + " : " + p.getName());
            }
        } else {
            Log.d(TAG, "Filter disabled, notifying all");
            NotificationManager.getInstance(context).showPokemonNotification(context, p, manager.isVibrate(), manager.getRingtone());
        }
    }
}
