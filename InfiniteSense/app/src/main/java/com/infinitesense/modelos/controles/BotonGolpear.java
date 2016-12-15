package com.infinitesense.modelos.controles;

import android.content.Context;

import com.infinitesense.GameView;
import com.infinitesense.R;
import com.infinitesense.gestores.CargadorGraficos;
import com.infinitesense.modelos.Modelo;


/**
 * Created by juanfa on 19/10/16.
 */

public class BotonGolpear extends Modelo {

    public BotonGolpear(Context context) {
        super(context, GameView.pantallaAncho*0.85 , GameView.pantallaAlto*0.5,
                GameView.pantallaAlto, GameView.pantallaAncho);

        altura = 100;
        ancho = 100;

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.buttonfire);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        if (clickX <= (x + ancho / 2) && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2) && clickY >= (y - altura / 2)) {
            estaPulsado = true;
        }
        return estaPulsado;
    }
}
