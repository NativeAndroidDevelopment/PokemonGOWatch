package com.devom.pokemongowatch.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.devom.pokemongowatch.Managers.BackgroundManager;
import com.devom.pokemongowatch.Managers.DialogManager;
import com.devom.pokemongowatch.Managers.LocationManager;
import com.devom.pokemongowatch.Managers.LogManager;
import com.devom.pokemongowatch.Managers.LoginManager;
import com.devom.pokemongowatch.Managers.NetworkManager;
import com.devom.pokemongowatch.Managers.ServiceManager;
import com.devom.pokemongowatch.R;

public class ServiceFragment extends HomeFragment {

    private static final String TAG = ServiceFragment.class.getSimpleName();

    private static final String SERVICE_RUNNING = "On the lookout for Pokémon ...";
    private static final String SERVICE_NOT_RUNNING = "Taking a break ...";
    private static final String START_SERVICE = "Start looking";
    private static final String STOP_SERVICE = "Take a break";

    //<editor-fold desc="Variables">
    private Context context;
    private LocationManager locationManager;
    private NetworkManager networkManager;
    private ServiceManager serviceManager;
    //</editor-fold>

    //<editor-fold desc="Views">
    private TextView serviceStatus;
    private Button serviceActionButton;
    //</editor-fold>

    //<editor-fold desc="Overrides">
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        locationManager = LocationManager.getInstance(context);
        networkManager = NetworkManager.getInstance(context);
        serviceManager = ServiceManager.getInstance(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        BackgroundManager.getInstance().setBackground(R.drawable.background_2);

        serviceStatus = (TextView) view.findViewById(R.id.fragment_service_status);
        serviceActionButton = (Button) view.findViewById(R.id.fragment_service_action_button);

        serviceActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Service is running: " + serviceManager.isRunning(context));
                if (serviceManager.isRunning(context)) {
                    serviceManager.stopService(context);
                    updateServiceState();
                } else {
                    if (hasNetworkAndLocation() && !isSigningIn()) {
                        serviceManager.startService(context);
                        updateServiceState();
                    }
                }
            }
        });

        if (serviceManager.isReturnFromLogin()) {
            serviceManager.setReturnFromLogin(false);
            serviceManager.startService(context);
        }

        updateServiceState();

        return view;
    }
    //</editor-fold>

    //<editor-fold desc="Helpers">
    private boolean hasNetworkAndLocation() {
        if (!networkManager.isConnected()) {
            DialogManager.showServiceNoNetworkDialog(context);
            return false;
        } else if (!locationManager.isEnabled()) {
            DialogManager.showServiceNoLocationDialog(context);
            return false;
        }
        return true;
    }

    private boolean isSigningIn() {
        if (!LoginManager.getInstance().isAuthenticated()) {
            LogManager.log(context, "Initiating sign in at service start");
            LoginManager.getInstance().login(context);
            serviceManager.setReturnFromLogin(true);
            return true;
        }
        return false;
    }

    private void updateServiceState() {
        boolean isRunning = serviceManager.isRunning(context);
        serviceStatus.setText(isRunning ? SERVICE_RUNNING : SERVICE_NOT_RUNNING);
        serviceActionButton.setText(isRunning ? STOP_SERVICE : START_SERVICE);
    }
    //</editor-fold>

}
