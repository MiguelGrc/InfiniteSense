package com.infinitesense.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.infinitesense.R;
import com.infinitesense.gestores.CargadorGraficos;
import com.infinitesense.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by Jorge on 13/12/2016.
 */
public class Obstaculo extends Modelo {
    public static int GOLPEADO=1;
    public static int NORMAL=0;

    public int estado=NORMAL;
    private Sprite sprite;
    private HashMap<Integer, Sprite> sprites = new HashMap<Integer, Sprite>();

    public Obstaculo(Context context, double x, double y) {
        super(context, x, y, 32, 32);


        sprites.put(GOLPEADO,new Sprite(CargadorGraficos.cargarDrawable(context, R.drawable.animacion_caja_explosion),ancho, altura,30,10,false));
        sprites.put(NORMAL, new Sprite(CargadorGraficos.cargarDrawable(context,R.drawable.caja_explosion),ancho,altura,1,1,true));
        sprite=sprites.get(NORMAL);
    }

    @Override
    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) (y-15-Nivel.scrollEjeY));
    }

    /**
     * Actualizamos el sprite del obstaculo.
     * RETURN true si se ha acabado el sprite y hay que eliminarlo.
     */
    public boolean actualizar(Long tiempo){
        return sprite.actualizar(tiempo);
    }

    public void destruir(){
        estado=GOLPEADO;
        sprite=sprites.get(GOLPEADO);
    }

    public void reiniciar(){
        estado=NORMAL;
        sprite= sprites.get(NORMAL);
    }

}
