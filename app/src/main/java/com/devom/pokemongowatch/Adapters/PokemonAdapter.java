package com.devom.pokemongowatch.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.devom.pokemongowatch.Adapters.Listeners.OnItemSelectListener;
import com.devom.pokemongowatch.Models.Pokemon;
import com.devom.pokemongowatch.R;
import com.devom.pokemongowatch.Utils.ImageUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Maikel on 09/05/2016.
 */

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    //<editor-fold desc="Variables">
    private Context context;
    private Set<String> filterSet;
    private List<Pokemon> pokedex;
    private OnItemSelectListener<Pokemon> onItemSelectListener;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public PokemonAdapter(Context context, List<Pokemon> pokedex) {
        this.context = context;
        this.pokedex = pokedex;
        this.filterSet = new HashSet<>();
    }

    public PokemonAdapter(Context context, List<Pokemon> pokedex, OnItemSelectListener<Pokemon> onItemSelectListener) {
        this(context, pokedex);
        this.onItemSelectListener = onItemSelectListener;
    }
    //</editor-fold>

    //<editor-fold desc="Getter Setter">
    public void setOnItemSelectListener(OnItemSelectListener<Pokemon> onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public void setFilterSet(Set<String> filterSet) {
        this.filterSet = filterSet;
    }

    public Set<String> getFilterSet() {
        return filterSet;
    }
    //</editor-fold>

    //<editor-fold desc="Overrides">
    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_filter_row, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PokemonViewHolder holder, int position) {
        final Pokemon pokemon = pokedex.get(position);
        holder.sprite.setImageBitmap(ImageUtils.getPokemonSprite(context, pokemon.getNumber()));
        holder.name.setText(pokemon.getName());
        holder.number.setText(pokemon.getPokedexNumber());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFiltered = updateFilterSet(pokemon);
                updateViewState(isFiltered, holder);
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(pokemon, isFiltered);
                }
            }
        });
        updateViewState(isFiltered(pokemon), holder);
    }

    @Override
    public int getItemCount() {
        return pokedex.size();
    }
    //</editor-fold>

    //<editor-fold desc="Helpers">
    private void updateViewState(boolean isFiltered, PokemonViewHolder holder) {
        if(isFiltered){
            holder.sprite.setColorFilter(Color.argb(255, 153, 153, 153));
            holder.sprite.setAlpha(0.4f);
            holder.name.setAlpha(0.4f);
        }else{
            holder.sprite.setColorFilter(null);
            holder.sprite.setAlpha(1f);
            holder.name.setAlpha(1f);
        }
    }

    private boolean updateFilterSet(Pokemon pokemon) {
        if (isFiltered(pokemon)) {
            filterSet.remove(pokemon.getPokedexNumber());
            return false;
        }
        filterSet.add(pokemon.getPokedexNumber());
        return true;
    }

    private boolean isFiltered(Pokemon pokemon) {
        return filterSet.contains(pokemon.getPokedexNumber());
    }
    //</editor-fold>

    //<editor-fold desc="ViewHolder">
    public class PokemonViewHolder extends RecyclerView.ViewHolder {
        public ImageView sprite;
        public TextView name, number;

        public PokemonViewHolder(View view) {
            super(view);
            sprite = (ImageView) view.findViewById(R.id.view_filter_row_sprite);
            name = (TextView) view.findViewById(R.id.view_filter_row_name);
            number = (TextView) view.findViewById(R.id.view_filter_row_number);
        }
    }
    //</editor-fold>
}