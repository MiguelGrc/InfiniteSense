package com.infinitesense.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.infinitesense.R;
import com.infinitesense.gestores.CargadorGraficos;
import com.infinitesense.graficos.Sprite;
import com.infinitesense.modelos.Tiles.Tile;

import java.util.HashMap;

/**
 * Created by MIGUEL on 05/12/2016.
 */

public class Moneda extends Tile {

    private Context context;

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    private boolean recogido;

    public static final String NUEVO = "Nuevo";
    public static final String RECOGIDO = "Recogido";

    public Moneda(Context context) {
        super(null, PASABLE);
        this.context = context;

        inicializar();
    }

    private void inicializar(){
        Sprite nuevo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.gem),
                32, 32,
                8, 8, true);
        sprites.put(NUEVO, nuevo);

        Sprite recogido = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.item_on_collected),
                96, 96,
                12, 10, false);
        sprites.put(RECOGIDO, recogido);

        sprite = nuevo;
    }

    @Override
    public void actualizar(long tiempo){
        sprite.actualizar(tiempo);
        if (recogido)
            sprite = sprites.get(RECOGIDO);
        else
            sprite = sprites.get(NUEVO);
    }

    @Override
    public void interactuar(Jugador jugador) {
        //Hay que pensarse que la moneda sea tile, mejor un objeto como hicimos en clase.
    }

    @Override
    public void dibujar(Canvas canvas, int x, int y){
        //sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y);
        sprite.dibujarSprite(canvas, (x * ancho + ancho/2) - Nivel.scrollEjeX, (y * altura + altura/2) - Nivel.scrollEjeY);
    }

    public void recoger(){
        recogido = true;
    }

    public boolean isRecogido(){
        return recogido;
    }

    public void setRecogido(boolean recogido){
        this.recogido = recogido;
    }

    //From ships game
    public boolean colisiona(Jugador jugador, int x, int y){
        boolean colisiona = false;

        if ((jugador.x < (x + 1) * ancho)
                && (jugador.x > x * ancho)
                && (y * altura < jugador.y)
                && ((y + 1) * altura > jugador.y)) {
            colisiona = true;
        }

        return colisiona;
    }



}
