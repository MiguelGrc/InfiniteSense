package com.infinitesense.modelos.Tiles;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.infinitesense.modelos.Jugador;
import com.infinitesense.modelos.Nivel;

/**
 * Created by Jorge on 18/11/2016.
 */
public abstract class Tile  {
    public static final int PASABLE = 0;
    public static final int SOLIDO = 1;

    public int tipoDeColision; // PASABLE o SOLIDO

    public static int ancho = 40;
    public static int altura = 32;

    public Drawable imagen;

    public Tile(Drawable imagen, int tipoDeColision)
    {
        this.imagen = imagen ;
        this.tipoDeColision = tipoDeColision;
    }


    public void dibujar(Canvas canvas, int x, int y) {
        if (imagen != null) {
            imagen.setBounds(
                    (x * Tile.ancho) - Nivel.scrollEjeX,
                    (y * Tile.altura) - Nivel.scrollEjeY,
                    (x * Tile.ancho) + Tile.ancho - Nivel.scrollEjeX,
                    y * Tile.altura + Tile.altura - Nivel.scrollEjeY);
            imagen.draw(canvas);
        }
    }

    public void actualizar(long tiempo){

    }

    /**
     * Método que se dispara cuando el jugador interactua con su bloque inferior.
     */
    public abstract void interactuar(Jugador jugador);

}

