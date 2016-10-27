package com.devom.pokemongowatch.Managers;

import android.content.Context;

import com.devom.pokemongowatch.Managers.Listeners.OnAuthenticationListener;
import com.devom.pokemongowatch.Network.LoginTask;
import com.devom.pokemongowatch.Network.LoginTaskCallback;

/**
 * Created by Maikel on 08/26/2016.
 */
public class LoginManager implements LoginTaskCallback {

    //<editor-fold desc="Variables">
    private boolean isAuthenticated;
    private OnAuthenticationListener onAuthenticationListener;
    //</editor-fold>

    //<editor-fold desc="Singleton">
    private static LoginManager instance;

    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }

    private LoginManager() {
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void login(Context context, String token, boolean persistent) {
        LoginTask loginTask = new LoginTask(context, this);
        loginTask.setOnAuthenticationListener(onAuthenticationListener);
        loginTask.setPersistent(persistent);
        loginTask.execute(token);
    }

    public void login(Context context){
        LoginTask loginTask = new LoginTask(context, this);
        loginTask.setOnAuthenticationListener(onAuthenticationListener);
        loginTask.execute();
    }

    public void retry(){
        if(onAuthenticationListener != null){
            onAuthenticationListener.onAuthenticationRetryRequest();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Setter">
    public void setOnAuthenticationListener(OnAuthenticationListener onAuthenticationListener) {
        this.onAuthenticationListener = onAuthenticationListener;
    }
    //</editor-fold>

    //<editor-fold desc="Interface Implementation">
    @Override
    public void onTaskComplete(boolean success) {
        isAuthenticated = success;
    }
    //</editor-fold>
}
