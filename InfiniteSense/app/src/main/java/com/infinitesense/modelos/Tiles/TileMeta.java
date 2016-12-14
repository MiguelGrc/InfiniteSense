package com.infinitesense.modelos.Tiles;

import android.graphics.drawable.Drawable;

import com.infinitesense.modelos.Jugador;
import com.infinitesense.modelos.Nivel;

/**
 * Created by Jorge on 14/12/2016.
 */
public class TileMeta extends Tile {

    Nivel nivel;

    public TileMeta(Drawable imagen, int tipoDeColision, Nivel nivel) {
        super(imagen, tipoDeColision);
        this.nivel= nivel;
    }

    @Override
    public void interactuar(Jugador jugador) {
        nivel.nivelPausado=true;
        nivel.ganar();
    }
}
