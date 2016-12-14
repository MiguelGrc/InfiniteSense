package com.infinitesense.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.infinitesense.GameView;

/**
 * Created by Jorge on 14/12/2016.
 */
public class ContadorTiempoGanar extends ContadorTiempo {

    private String tiempoTotal;
    private boolean habilitado;

    public ContadorTiempoGanar(Context context) {
        super(context);
        x = GameView.pantallaAncho * 0.6;
        y = GameView.pantallaAlto * 0.5;


    }

    @Override
    public void dibujar(Canvas canvas){

        if(habilitado) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setTextSize(50);
            imagen.setBounds((int) x - 10, (int) y - 10, (int) x + 10, (int) y + 10);
            imagen.draw(canvas);
            canvas.drawText(tiempoTotal, (int) x + 20, (int) y + 7, paint);
        }
    }

    public void habilitar(String tiempoTotal){
        habilitado=true;
        this.tiempoTotal=tiempoTotal;
    }
    public void deshabilitar(){
        habilitado=false;
    }
}
