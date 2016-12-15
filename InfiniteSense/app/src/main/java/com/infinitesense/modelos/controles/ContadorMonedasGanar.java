package com.infinitesense.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.infinitesense.GameView;

/**
 * Created by MIGUEL on 15/12/2016.
 */

public class ContadorMonedasGanar extends ContadorMonedas {

    private int puntosTotal;
    private boolean habilitado;

    public ContadorMonedasGanar(Context context) {
        super(context);
        x = GameView.pantallaAncho * 0.35;
        y = GameView.pantallaAlto * 0.5;
    }

    @Override
    public void dibujar(Canvas canvas){

        if(habilitado) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setTextSize(50);
            imagen.setBounds((int)x-40, (int)y-40, (int)x+20, (int)y+20);
            imagen.draw(canvas);
            canvas.drawText(String.valueOf(puntosTotal), (int) x + 20, (int) y + 7, paint);
        }
    }

    public void habilitar(int puntosTotal){
        habilitado=true;
        this.puntosTotal=puntosTotal;
    }
    public void deshabilitar(){
        habilitado=false;
    }

}
