package com.devom.pokemongowatch.Managers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;

import com.devom.pokemongowatch.Activities.MainActivity;
import com.devom.pokemongowatch.Models.EncounterPokemon;
import com.devom.pokemongowatch.Constants.AppConstants;
import com.devom.pokemongowatch.R;
import com.devom.pokemongowatch.Services.MapService;
import com.devom.pokemongowatch.Utils.AppUtils;
import com.devom.pokemongowatch.Utils.ImageUtils;

/**
 * Created by Maikel on 08/15/2016.
 */
public class NotificationManager {

    //<editor-fold desc="Variables">
    private android.app.NotificationManager manager;
    //</editor-fold>

    //<editor-fold desc="Singleton">
    private static NotificationManager instance;

    public static NotificationManager getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationManager();
            instance.manager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return instance;
    }

    private NotificationManager() {
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public Notification getServiceNotification(Context context) {
        Intent cancelIntent = new Intent(context, MapService.class);
        cancelIntent.setAction(AppConstants.CANCEL_SERVICE_REQUEST);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("On the lookout for Pokémon ...")
                .setContentText("Touch to cancel")
                .setSmallIcon(R.drawable.ic_pokeball_2)
                .setContentIntent(PendingIntent.getService(context, 0, cancelIntent, 0))
                .setOngoing(true);
        return builder.build();
    }

    public void showServiceErrorNotification(Context context) {
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("Something went wrong ...")
                .setContentText("Touch to open Pokémon Watch")
                .setSmallIcon(R.drawable.ic_pokeball_alert)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0))
                .setAutoCancel(true);
        manager.notify(AppConstants.SERVICE_ERROR_NOTIFICATION_ID, builder.build());
    }

    public void showPokemonNotification(Context context, EncounterPokemon encounterPokemon, boolean vibrate, Uri sound) {
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(String.format("A wild %s appeared! (%.0fm)", encounterPokemon, encounterPokemon.getDistance()))
                .setContentText("Pokémon GO Watch")
                .setVibrate(vibrate ? new long[]{0, 800} : null)
                .setSound(sound)
                .setOnlyAlertOnce(true);

        Bitmap sprite = ImageUtils.getPokemonSprite(context, encounterPokemon.getNumber());
        if (sprite != null) {
            builder.setLargeIcon(sprite);
            builder.setSmallIcon(Icon.createWithBitmap(sprite));
        } else {
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.pokeball));
            builder.setSmallIcon(R.drawable.ic_pokeball);
        }

        Intent pokemonGoIntent = AppUtils.getPokemonGOIntent(context);
        if (pokemonGoIntent != null) {
            PendingIntent pendingPokemonGoIntent = PendingIntent.getActivity(context, 0, pokemonGoIntent, 0);
            builder.setContentIntent(pendingPokemonGoIntent)
                    .setContentText("Touch to open Pokémon GO")
                    .setAutoCancel(true);
        }

        manager.notify((int) encounterPokemon.getEncounterId(), builder.build());
    }
    //</editor-fold>
}
