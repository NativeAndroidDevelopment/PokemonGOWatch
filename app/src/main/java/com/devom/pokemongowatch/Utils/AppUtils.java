package com.devom.pokemongowatch.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Maikel on 08/26/2016.
 */
public class AppUtils {

    public static Intent getPokemonGOIntent(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.nianticlabs.pokemongo");
        if (intent != null) {
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        return intent;
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
