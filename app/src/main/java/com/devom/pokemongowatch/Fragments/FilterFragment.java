package com.devom.pokemongowatch.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devom.pokemongowatch.Adapters.PokemonAdapter;
import com.devom.pokemongowatch.Managers.BackgroundManager;
import com.devom.pokemongowatch.Managers.LogManager;
import com.devom.pokemongowatch.Managers.SettingsManager;
import com.devom.pokemongowatch.Models.Pokemon;
import com.devom.pokemongowatch.Other.DividerItemDecorator;
import com.devom.pokemongowatch.R;
import com.devom.pokemongowatch.Utils.ResourceUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends BaseFragment {

    private static final String TAG = FilterFragment.class.getSimpleName();

    //<editor-fold desc="Variables">
    private Context context;
    private PokemonAdapter pokemonAdapter;
    private List<Pokemon> pokedex;
    //</editor-fold>

    //<editor-fold desc="Views">
    private RecyclerView recyclerView;
    //</editor-fold>

    //<editor-fold desc="Overrides">
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        pokedex = ResourceUtils.getPokedex();
        pokemonAdapter = new PokemonAdapter(context, pokedex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        BackgroundManager.getInstance().setBackground(R.drawable.background_4);

        Log.d(TAG, "Got most recent filter set : " + SettingsManager.getInstance(context).getFilterSet());
        pokemonAdapter.setFilterSet(SettingsManager.getInstance(context).getFilterSet());

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_filter_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(pokemonAdapter);
        recyclerView.addItemDecoration(new DividerItemDecorator(context, DividerItemDecorator.VERTICAL));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "Exiting FilterFragment, saving new filter set : " + pokemonAdapter.getFilterSet());
        SettingsManager.getInstance(context).setFilterSet(pokemonAdapter.getFilterSet());
        LogManager.log(context, String.format("Filter set saved (%d items)", pokemonAdapter.getFilterSet().size()));
    }

    @Override
    public int getMenuItemId() {
        return R.id.nav_filter;
    }
    //</editor-fold>
}
