package com.devom.pokemongowatch.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.devom.pokemongowatch.Managers.BackgroundManager;
import com.devom.pokemongowatch.Managers.LoginManager;
import com.devom.pokemongowatch.R;

public class ErrorFragment extends HomeFragment {

    private static final String TAG = ErrorFragment.class.getSimpleName();

    //<editor-fold desc="Views">
    private Button retryButton;
    //</editor-fold>

    //<editor-fold desc="Overrides">
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error, container, false);

        BackgroundManager.getInstance().setBackground(R.drawable.background_6);

        retryButton = (Button) view.findViewById(R.id.fragment_error_retry_button);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().retry();
            }
        });

        return view;
    }
    //</editor-fold>

}
