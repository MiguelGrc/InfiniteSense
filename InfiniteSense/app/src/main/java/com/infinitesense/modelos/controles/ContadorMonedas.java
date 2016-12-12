package com.infinitesense.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.infinitesense.GameView;
import com.infinitesense.R;
import com.infinitesense.gestores.CargadorGraficos;
import com.infinitesense.modelos.Modelo;

/**
 * Created by MIGUEL on 05/12/2016.
 */

public class ContadorMonedas extends Modelo{

    private int puntos;

    public ContadorMonedas(Context context) {
        super(context, GameView.pantallaAncho * 0.03, GameView.pantallaAlto * 0.05,
                GameView.pantallaAlto, GameView.pantallaAncho);
        altura = 100;
        ancho = 100;
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.coincont);
        puntos = 0;
    }

    @Override
    public void dibujar(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        imagen.setBounds((int)x-10, (int)y-10, (int)x+10, (int)y+10);
        imagen.draw(canvas);
        canvas.drawText(String.valueOf(puntos), (int)x+20, (int)y+7, paint);
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

}
