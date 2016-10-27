package com.devom.pokemongowatch.Fragments.Listeners;

/**
 * Created by Maikel on 08/30/2016.
 */
public interface OnFragmentChangeRequestListener<T> {

    void onFragmentChangeRequest(Class<? extends T> fragmentClass);
}
