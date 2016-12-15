package com.infinitesense.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.infinitesense.R;
import com.infinitesense.gestores.CargadorGraficos;
import com.infinitesense.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by MIGUEL on 15/12/2016.
 */

public class PowerupLento extends Modelo {

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    private boolean recogido;

    public static final String NUEVO = "Nuevo";
    public static final String RECOGIDO = "Recogido";

    public PowerupLento(Context context, double x, double y) {
        super(context, x, y, 40, 40);

        Sprite nuevo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_rojo),
                60, 60,
                15, 2, true);
        sprites.put(NUEVO, nuevo);

        Sprite recogido = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_explosion_rojo),
                60, 60,
                15, 3, false);
        sprites.put(RECOGIDO, recogido);

        sprite = nuevo;
    }

    @Override
    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) (y-15-Nivel.scrollEjeY));
    }

    public boolean actualizar(Long tiempo){
        return sprite.actualizar(tiempo);
    }


    public void recoger(){
        recogido = true;
        sprite = sprites.get(RECOGIDO);
    }

    public boolean isRecogido(){
        return recogido;
    }

    public void setRecogido(boolean recogido){
        this.recogido = recogido;
    }

    public void reiniciar(){
        recogido = false;
        sprite= sprites.get(NUEVO);
        sprites.get(RECOGIDO).setFrameActual(0);
    }
}
