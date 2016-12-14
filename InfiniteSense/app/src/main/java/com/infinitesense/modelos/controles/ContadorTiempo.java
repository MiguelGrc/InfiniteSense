package com.infinitesense.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.infinitesense.GameView;
import com.infinitesense.R;
import com.infinitesense.gestores.CargadorGraficos;
import com.infinitesense.modelos.Modelo;
import com.infinitesense.modelos.Nivel;

import java.util.concurrent.TimeUnit;

/**
 * Created by Jorge on 14/12/2016.
 */
public class ContadorTiempo extends Modelo {


    private long tiempoInicial;

    private long tiempoActual;

    public String tiempoEnMin;

    public ContadorTiempo(Context context) {
        super(context, GameView.pantallaAncho * 0.2, GameView.pantallaAlto * 0.05, GameView.pantallaAlto, GameView.pantallaAncho);
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.time);
        tiempoInicial=System.currentTimeMillis();


    }

    @Override
    public void dibujar(Canvas canvas){
        tiempoEnMin=String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(tiempoActual),
                TimeUnit.MILLISECONDS.toSeconds(tiempoActual) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tiempoActual)));
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        imagen.setBounds((int)x-10, (int)y-10, (int)x+10, (int)y+10);
        imagen.draw(canvas);
        canvas.drawText(tiempoEnMin, (int)x+20, (int)y+7, paint);
    }

    @Override
    public void actualizar (long tiempo){

        if(!Nivel.nivelPausado) {
            tiempoActual = System.currentTimeMillis() - tiempoInicial;
        }
    }


}
