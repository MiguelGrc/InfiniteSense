package com.infinitesense.modelos.Tiles;

import android.graphics.drawable.Drawable;

import com.infinitesense.modelos.Jugador;
import com.infinitesense.modelos.Nivel;

/**
 * Created by MIGUEL on 15/12/2016.
 */

public class TileAgua extends Tile {

    private Nivel nivel;

    public TileAgua(Drawable imagen, int tipoDeColision, Nivel nivel) {
        super(imagen, tipoDeColision);
        this.nivel = nivel;
    }

    @Override
    public void interactuar(Jugador jugador) {
        nivel.jugadorPerder();
    }
}
