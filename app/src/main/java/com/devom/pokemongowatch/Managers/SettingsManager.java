package com.devom.pokemongowatch.Managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.devom.pokemongowatch.Constants.Settings;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Maikel on 08/30/2016.
 */
public class SettingsManager {

    //<editor-fold desc="Variables">
    private SharedPreferences sharedPreferences;
    //</editor-fold>

    //<editor-fold desc="Singleton">
    private static SettingsManager instance;

    public static SettingsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsManager();
            instance.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return instance;
    }

    private SettingsManager() {
    }
    //</editor-fold>

    //<editor-fold desc="Getters Setters">
    public Uri getRingtone() {
        return Uri.parse(sharedPreferences.getString(Settings.RINGTONE, "content://settings/system/notification_sound"));
    }

    public void setRingtone(String uri) {
        sharedPreferences.edit().putString(Settings.RINGTONE, uri).apply();
    }

    public boolean isVibrate() {
        return sharedPreferences.getBoolean(Settings.VIBRATE, false);
    }

    public void setVibrate(boolean vibrate) {
        sharedPreferences.edit().putBoolean(Settings.VIBRATE, vibrate).apply();
    }

    public boolean isFastInterval() {
        return sharedPreferences.getBoolean(Settings.FAST_INTERVAL, false);
    }

    public void setFastInterval(boolean fastInterval) {
        sharedPreferences.edit().putBoolean(Settings.FAST_INTERVAL, fastInterval).apply();
    }

    public boolean isFilter() {
        return sharedPreferences.getBoolean(Settings.FILTER, false);
    }

    public void setFilter(boolean filter) {
        sharedPreferences.edit().putBoolean(Settings.FILTER, filter).apply();
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(Settings.REFRESH_TOKEN, null);
    }

    public void setRefreshToken(String refreshToken) {
        if (refreshToken != null) {
            sharedPreferences.edit().putString(Settings.REFRESH_TOKEN, refreshToken).apply();
        }
    }

    public Set<String> getFilterSet() {
        //Wrap in new Set to make editable !
        return new HashSet<>(sharedPreferences.getStringSet(Settings.FILTER_SET, new HashSet<String>()));
    }

    public void setFilterSet(Set<String> filterSet) {
        if (filterSet != null) {
            sharedPreferences.edit().putStringSet(Settings.FILTER_SET, filterSet).apply();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    public boolean hasRefreshToken() {
        return sharedPreferences.contains(Settings.REFRESH_TOKEN);
    }
    //</editor-fold>
}
