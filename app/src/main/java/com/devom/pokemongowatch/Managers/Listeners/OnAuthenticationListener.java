package com.devom.pokemongowatch.Managers.Listeners;

/**
 * Created by Maikel on 09/19/2016.
 */
public interface OnAuthenticationListener {
    void onAuthenticationStart();
    void onAuthenticationComplete(boolean isAuthenticated);
    void onAuthenticationRetryRequest();
}
