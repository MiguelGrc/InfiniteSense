package com.infinitesense.modelos.Tiles;

import android.graphics.drawable.Drawable;

import com.infinitesense.gestores.GestorAudio;
import com.infinitesense.modelos.Jugador;
import com.infinitesense.modelos.Nivel;

/**
 * Created by Jorge on 14/12/2016.
 */
public class TileMeta extends Tile {

    Nivel nivel;
    private boolean pisado;

    public TileMeta(Drawable imagen, int tipoDeColision, Nivel nivel) {
        super(imagen, tipoDeColision);
        this.nivel= nivel;
    }

    @Override
    public void interactuar(Jugador jugador) {
        if(!pisado) {
            pisado = true;
            nivel.nivelPausado = true;
            nivel.ganar();
            GestorAudio.getInstancia().reproducirSonido(GestorAudio.SONIDO_META);
        }
    }
}
