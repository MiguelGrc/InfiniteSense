package com.infinitesense.modelos.Tiles;

import android.graphics.drawable.Drawable;

import com.infinitesense.modelos.Jugador;

/**
 * Created by Jorge on 12/12/2016.
 */
public class TileNota extends Tile {


    public TileNota(Drawable imagen, int tipoDeColision) {
        super(imagen, tipoDeColision);
    }

    @Override
    public void interactuar(Jugador jugador) {
        jugador.superSalto=true;
    }
}
