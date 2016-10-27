package com.devom.pokemongowatch.Managers;

import com.devom.pokemongowatch.Fragments.Listeners.OnBackgroundRequestListener;

/**
 * Created by Maikel on 09/20/2016.
 */
public class BackgroundManager {

    //<editor-fold desc="Variable">
    private OnBackgroundRequestListener onBackgroundRequestListener;
    //</editor-fold>

    //<editor-fold desc="Singleton">
    private BackgroundManager() {
    }

    private static class BackgroundManagerHolder {
        private static final BackgroundManager INSTANCE = new BackgroundManager();
    }

    public static BackgroundManager getInstance() {
        return BackgroundManagerHolder.INSTANCE;
    }
    //</editor-fold>

    //<editor-fold desc="Setter">
    public void setOnBackgroundRequestListener(OnBackgroundRequestListener onBackgroundRequestListener) {
        this.onBackgroundRequestListener = onBackgroundRequestListener;
    }
    //</editor-fold>

    //<editor-fold desc="Method">
    public void setBackground(int resId) {
        if (onBackgroundRequestListener != null) {
            onBackgroundRequestListener.onBackgroundRequest(resId);
        }
    }
    //</editor-fold>
}