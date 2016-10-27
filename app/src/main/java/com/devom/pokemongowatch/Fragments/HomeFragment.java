package com.devom.pokemongowatch.Fragments;

import com.devom.pokemongowatch.R;

/**
 * Created by Maikel on 08/30/2016.
 */
public abstract class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    @Override
    public int getMenuItemId() {
        return R.id.nav_home;
    }
}
