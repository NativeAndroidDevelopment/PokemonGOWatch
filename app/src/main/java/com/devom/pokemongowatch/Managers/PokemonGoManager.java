package com.devom.pokemongowatch.Managers;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.devom.pokemongowatch.Models.EncounterPokemon;
import com.devom.pokemongowatch.Utils.ParseUtils;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Maikel on 08/30/2016.
 */
public class PokemonGoManager {

    private static final String TAG = PokemonGoManager.class.getSimpleName();

    //<editor-fold desc="Variables">
    private static int nullCount = 0;
    private static PokemonGo pokemonGo;
    private static final List<Long> encounteredList = new ArrayList<>();
    private NotificationManager notificationManager;
    private SettingsManager settingsManager;
    //</editor-fold>

    //<editor-fold desc="Singleton">
    private static PokemonGoManager instance;

    public static PokemonGoManager getInstance(Context context) {
        if (instance == null) {
            instance = new PokemonGoManager();
            instance.notificationManager = NotificationManager.getInstance(context);
            instance.settingsManager = SettingsManager.getInstance(context);
        }
        return instance;
    }

    private PokemonGoManager() {
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public void setPokemonGo(PokemonGo pokemonGo) {
        PokemonGoManager.pokemonGo = pokemonGo;
    }

    public void update(Context context, Location location) {
        if (pokemonGo != null) {
            nullCount = 0;
            setPlayerLocation(location);
            try {
                Log.d(TAG, "Checking for pokémon ...");
                if (settingsManager.isFilter()) {
                    Log.d(TAG, "Pokemon filter enabled : " + settingsManager.getFilterSet());
                    sendFilteredPokemonNotifications(context, pokemonGo.getMap().getCatchablePokemonSort());
                } else {
                    sendPokemonNotifications(context, pokemonGo.getMap().getCatchablePokemonSort());
                }
            } catch (LoginFailedException | RemoteServerException | RuntimeException e) {
                LogManager.log(context, "Service aborted due to error");
                ServiceManager.getInstance(context).stopService(context);
                NotificationManager.getInstance(context).showServiceErrorNotification(context);
            }
        } else {
            Log.d(TAG, "PokemonGo instance is null");
            if(nullCount++ > 3){
                nullCount = 0;
                ServiceManager.getInstance(context).stopService(context);
                NotificationManager.getInstance(context).showServiceErrorNotification(context);
                LogManager.log(context, "Pokémon GO server unreachable, aborting service");
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Helpers">
    private void setPlayerLocation(Location location) {
        Log.d(TAG, "Setting new player location lat(" + location.getLatitude() + ") long(" + location.getLongitude() + ")");
        pokemonGo.setLocation(location.getLatitude(), location.getLongitude(), location.getAltitude());
    }

    private void sendPokemonNotifications(Context context, Map<Double, CatchablePokemon> pokemonMap) {
        if (!pokemonMap.isEmpty()) {
            for (Map.Entry<Double, CatchablePokemon> pokemonEntry : pokemonMap.entrySet()) {
                EncounterPokemon encounterPokemon = ParseUtils.parsePokemon(pokemonEntry.getValue(), pokemonEntry.getKey());
                if (!encounteredList.contains(encounterPokemon.getEncounterId())) {
                    encounteredList.add(encounterPokemon.getEncounterId());
                    Log.d(TAG, "NEW : " + encounterPokemon.getEncounterId() + " : " + encounterPokemon.getName() + " : " + pokemonEntry.getKey() + " m");
                    LogManager.log(context, String.format("%s encounter %.0fm (%s)", encounterPokemon.getName(), pokemonEntry.getKey(), "New"));
                    notificationManager.showPokemonNotification(context, encounterPokemon, settingsManager.isVibrate(), settingsManager.getRingtone());
                } else {
                    Log.d(TAG, "OLD : " + encounterPokemon.getEncounterId() + " : " + encounterPokemon.getName() + " : " + pokemonEntry.getKey() + " m");
                    LogManager.log(context, String.format("%s encounter %.0fm (%s)", encounterPokemon.getName(), pokemonEntry.getKey(), "Notified"));
                }
            }
        }
    }

    private void sendFilteredPokemonNotifications(Context context, Map<Double, CatchablePokemon> pokemonMap) {
        Set<String> filterSet = settingsManager.getFilterSet();
        if (!pokemonMap.isEmpty()) {
            for (Map.Entry<Double, CatchablePokemon> pokemonEntry : pokemonMap.entrySet()) {
                EncounterPokemon encounterPokemon = ParseUtils.parsePokemon(pokemonEntry.getValue(), pokemonEntry.getKey());
                if (!encounteredList.contains(encounterPokemon.getEncounterId())) {
                    encounteredList.add(encounterPokemon.getEncounterId());
                    if (!filterSet.contains(encounterPokemon.getPokedexNumber())) {
                        Log.d(TAG, "NEW : " + encounterPokemon.getEncounterId() + " : " + encounterPokemon.getName() + " : " + pokemonEntry.getKey() + " m");
                        LogManager.log(context, String.format("%s encounter %.0fm (%s)", encounterPokemon.getName(), pokemonEntry.getKey(), "New"));
                        notificationManager.showPokemonNotification(context, encounterPokemon, settingsManager.isVibrate(), settingsManager.getRingtone());
                    } else {
                        Log.d(TAG, "FILTERED : " + encounterPokemon.getEncounterId() + " : " + encounterPokemon.getName() + " : " + pokemonEntry.getKey() + " m");
                        LogManager.log(context, String.format("%s encounter %.0fm (%s)", encounterPokemon.getName(), pokemonEntry.getKey(), "Filtered"));
                    }
                } else {
                    Log.d(TAG, "OLD : " + encounterPokemon.getEncounterId() + " : " + encounterPokemon.getName() + " : " + pokemonEntry.getKey() + " m");
                    LogManager.log(context, String.format("%s encounter %.0fm (%s)", encounterPokemon.getName(), pokemonEntry.getKey(), "Notified"));
                }
            }
        }
    }
    //</editor-fold>


}
