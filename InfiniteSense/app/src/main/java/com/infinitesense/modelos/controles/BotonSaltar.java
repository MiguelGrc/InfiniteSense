package com.infinitesense.modelos.controles;

import android.content.Context;


import com.infinitesense.GameView;
import com.infinitesense.R;
import com.infinitesense.gestores.CargadorGraficos;
import com.infinitesense.modelos.Modelo;

/**
 * Created by juanfa on 5/10/16.
 */

public class BotonSaltar extends Modelo {

    public BotonSaltar(Context context) {
        super(context, GameView.pantallaAncho*0.26 , GameView.pantallaAlto*0.23,
                GameView.pantallaAlto, GameView.pantallaAncho);

        altura = 125;
        ancho = 250;
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
