package com.devom.pokemongowatch.Managers;

import android.content.Context;
import android.content.Intent;

import com.devom.pokemongowatch.Services.MapService;
import com.devom.pokemongowatch.Utils.AppUtils;

/**
 * Created by Maikel on 08/30/2016.
 */
public class ServiceManager {

    //<editor-fold desc="Variables">
    private Intent serviceIntent;
    private boolean returnFromLogin;
    //</editor-fold>

    //<editor-fold desc="Singleton">
    private static ServiceManager instance;

    public static ServiceManager getInstance(Context context) {
        if (instance == null) {
            instance = new ServiceManager();
            instance.serviceIntent = new Intent(context, MapService.class);
        }
        return instance;
    }

    private ServiceManager() {
    }
    //</editor-fold>


    //<editor-fold desc="Getter Setter">
    public boolean isReturnFromLogin() {
        return returnFromLogin;
    }

    public void setReturnFromLogin(boolean returnFromLogin) {
        this.returnFromLogin = returnFromLogin;
    }
    //</editor-fold>

    //region Methods
    public boolean isRunning(Context context) {
        return AppUtils.isServiceRunning(context, MapService.class);
    }

    public void startService(Context context) {
        LogManager.log(context, "Starting service");
        context.startService(serviceIntent);
    }

    public void stopService(Context context) {
        LogManager.log(context, "Stopping service");
        context.stopService(serviceIntent);
    }

    public void restartService(Context context) {
        stopService(context);
        startService(context);
    }
    //endregion
}
