package com.devom.pokemongowatch.Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.devom.pokemongowatch.Managers.LogManager;
import com.devom.pokemongowatch.Managers.NotificationManager;
import com.devom.pokemongowatch.Managers.PokemonGoManager;
import com.devom.pokemongowatch.Constants.AppConstants;
import com.devom.pokemongowatch.Managers.SettingsManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Maikel on 08/30/2016.
 */
public class MapService extends Service implements GoogleApiClient.ConnectionCallbacks {

    public static final String TAG = MapService.class.getSimpleName();

    //<editor-fold desc="Variables">
    private GoogleApiClient googleApiClient;
    private PokemonGoManager pokemonGoManager;
    //</editor-fold>


    //<editor-fold desc="Overrides">
    @Override
    public void onCreate() {
        pokemonGoManager = PokemonGoManager.getInstance(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();

        startForeground(AppConstants.SERVICE_NOTIFICATION_ID, NotificationManager.getInstance(this).getServiceNotification(this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        googleApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Intent: " + intent + " with startId: " + startId);
        if (intent != null && AppConstants.CANCEL_SERVICE_REQUEST.equals(intent.getAction())) {
            Log.d(TAG, "Got cancel request code, stopping service");
            LogManager.log(this, "Stopping service");
            stopSelf();
            return 0;
        }
        return Service.START_STICKY;
    }
    //</editor-fold>

    //<editor-fold desc="Google API Callbacks">
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }
    //</editor-fold>

    //<editor-fold desc="Helpers">
    private void requestLocationUpdates() {
        boolean hasFineLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean hasCoarseLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!hasFineLocationPermission && !hasCoarseLocationPermission) {
            stopSelf();
            return;
        }

        Log.d(TAG, "Requesting location updates");
        LogManager.log(this, "Requesting location updates");

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (SettingsManager.getInstance(this).isFastInterval()) {
            locationRequest.setInterval(AppConstants.LOCATION_INTERVAL / 2);
            locationRequest.setFastestInterval(AppConstants.LOCATION_FASTEST_INTERVAL / 2);
            LogManager.log(this, "Registered fast interval");
        } else {
            locationRequest.setInterval(AppConstants.LOCATION_INTERVAL);
            locationRequest.setFastestInterval(AppConstants.LOCATION_FASTEST_INTERVAL);
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LogManager.log(MapService.this, String.format("Location update (%f, %f)", location.getLatitude(), location.getLongitude()));
                pokemonGoManager.update(MapService.this, location);
            }
        });
    }
    //</editor-fold>
}
