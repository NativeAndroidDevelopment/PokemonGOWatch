package com.devom.pokemongowatch.Managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Maikel on 08/26/2016.
 */
public class NetworkManager {

    //<editor-fold desc="Variables">
    private ConnectivityManager manager;
    //</editor-fold>

    //<editor-fold desc="Singleton">
    private static NetworkManager instance;

    public static NetworkManager getInstance(Context context) {
        if(instance == null){
            instance = new NetworkManager();
            instance.manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        return instance;
    }

    private NetworkManager() {
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public boolean isConnected() {
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
    //</editor-fold>

}
