package com.infinitesense.modelos.controles;

import android.content.Context;

import com.infinitesense.GameView;
import com.infinitesense.R;
import com.infinitesense.gestores.CargadorGraficos;
import com.infinitesense.modelos.Modelo;

/**
 * Created by Jorge on 03/12/2016.
 */
public class BotonAgachar extends Modelo {
    public BotonAgachar(Context context) {
        super(context, GameView.pantallaAncho*0.15 , GameView.pantallaAlto*0.5,
            70,70);

    altura = (int) (GameView.pantallaAlto*0.60);
    ancho = (int) (GameView.pantallaAlto*0.15);

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
