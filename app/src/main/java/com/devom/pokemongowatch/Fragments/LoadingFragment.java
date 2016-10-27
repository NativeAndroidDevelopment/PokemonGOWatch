package com.devom.pokemongowatch.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devom.pokemongowatch.R;

public class LoadingFragment extends HomeFragment {

    //<editor-fold desc="Overrides">
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }
    //</editor-fold>

}
