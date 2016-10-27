package com.devom.pokemongowatch.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.devom.pokemongowatch.Managers.Listeners.OnAuthenticationListener;
import com.devom.pokemongowatch.Managers.LogManager;
import com.devom.pokemongowatch.Managers.PokemonGoManager;
import com.devom.pokemongowatch.Managers.SettingsManager;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.GoogleUserCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import okhttp3.OkHttpClient;

/**
 * Created by Maikel on 08/28/2016.
 */
public class LoginTask extends AsyncTask<String, Void, PokemonGo> {

    private static final String TAG = LoginTask.class.getSimpleName();

    //<editor-fold desc="Variables">
    private OkHttpClient httpClient;
    private Context context;
    private LoginTaskCallback loginTaskCallback;
    private GoogleUserCredentialProvider provider;

    private OnAuthenticationListener onAuthenticationListener;
    private boolean persistent;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public LoginTask(Context context, LoginTaskCallback loginTaskCallback) {
        this.httpClient = new OkHttpClient();
        this.context = context;
        this.loginTaskCallback = loginTaskCallback;
    }
    //</editor-fold>

    //<editor-fold desc="Setter">
    public void setOnAuthenticationListener(OnAuthenticationListener onAuthenticationListener) {
        this.onAuthenticationListener = onAuthenticationListener;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }
    //</editor-fold>

    //<editor-fold desc="Overrides">
    @Override
    protected PokemonGo doInBackground(String... tokens) {
        if (onAuthenticationListener != null) {
            onAuthenticationListener.onAuthenticationStart();
        }
        try {
            if (tokens.length > 0) {
                //New access token supplied
                String token = tokens[0];
                if (token == null) {
                    return null;
                }
                Log.d(TAG, "Logging in with new accessToken : " + token);
                LogManager.log(context, "Sign in with new access token");
                provider = new GoogleUserCredentialProvider(httpClient);
                provider.login(token);
            } else {
                //No token supplied, check for refresh token
                String token = SettingsManager.getInstance(context).getRefreshToken();
                if (token == null) {
                    return null;
                }
                Log.d(TAG, "Logging in with refreshToken : " + token);
                LogManager.log(context, "Refresh token available for sign in");
                provider = new GoogleUserCredentialProvider(httpClient, token);
            }
            return new PokemonGo(provider, httpClient);

        } catch (LoginFailedException | RemoteServerException e) {
            Log.d(TAG, "LoginFailed Error: " + e.getMessage());
            return null;
        } catch (RuntimeException e) {
            Log.d(TAG, "Runtime Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(PokemonGo pokemonGo) {
        super.onPostExecute(pokemonGo);
        PokemonGoManager.getInstance(context).setPokemonGo(pokemonGo);
        if (persistent) {
            SettingsManager.getInstance(context).setRefreshToken(provider.getRefreshToken());
        }
        boolean success = pokemonGo != null;
        loginTaskCallback.onTaskComplete(success);
        LogManager.log(context, success ? "Signed in" : "Sign in failed");
        if (onAuthenticationListener != null) {
            onAuthenticationListener.onAuthenticationComplete(success);
        }
    }
    //</editor-fold>
}