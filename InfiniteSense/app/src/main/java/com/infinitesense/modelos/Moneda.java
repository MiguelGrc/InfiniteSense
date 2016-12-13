package com.infinitesense.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.infinitesense.R;
import com.infinitesense.gestores.CargadorGraficos;
import com.infinitesense.graficos.Sprite;
import com.infinitesense.modelos.Tiles.Tile;

import java.util.HashMap;

/**
 * Created by MIGUEL on 05/12/2016.
 */

public class Moneda extends Modelo {

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    private boolean recogido;

    public static final String NUEVO = "Nuevo";
    public static final String RECOGIDO = "Recogido";

    public Moneda(Context context, double x, double y) {
        super(context, x, y, 40, 40);

        Sprite nuevo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_recolectable),
                40, 40,
                15, 8, true);
        sprites.put(NUEVO, nuevo);

        Sprite recogido = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_disparo1),
                40, 32,
                15, 4, false);
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

//    @Override
//    public void actualizar(long tiempo){
//        sprite.actualizar(tiempo);
//        if (recogido)
//            sprite = sprites.get(RECOGIDO);
//        else
//            sprite = sprites.get(NUEVO);
//    }

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

//    //From ships game
//    public boolean colisiona(Jugador jugador, int x, int y){
//        boolean colisiona = false;
//
//        if ((jugador.x < (x + 1) * ancho)
//                && (jugador.x > x * ancho)
//                && (y * altura < jugador.y)
//                && ((y + 1) * altura > jugador.y)) {
//            colisiona = true;
//        }
//
//        return colisiona;
//    }

    public void reiniciar(){
        recogido = false;
        sprite= sprites.get(NUEVO);
    }

}
