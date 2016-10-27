package com.devom.pokemongowatch.Managers;

import android.content.Context;

/**
 * Created by Maikel on 08/26/2016.
 */
public class LocationManager {

    //<editor-fold desc="Variables">
    private static android.location.LocationManager manager;
    //</editor-fold>

    //<editor-fold desc="Singleton">
    private static LocationManager instance;

    public static LocationManager getInstance(Context context) {
        if (instance == null) {
            instance = new LocationManager();
            instance.manager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        return instance;
    }

    private LocationManager() {
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public boolean isEnabled() {
        return manager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }
    //</editor-fold>
}
