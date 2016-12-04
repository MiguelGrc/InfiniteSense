package com.infinitesense.modelos.controles;

import android.content.Context;


import com.infinitesense.GameView;
import com.infinitesense.R;
import com.infinitesense.gestores.CargadorGraficos;
import com.infinitesense.modelos.Modelo;

import static com.infinitesense.modelos.Tile.altura;
import static com.infinitesense.modelos.Tile.ancho;

/**
 * Created by juanfa on 5/10/16.
 */

public class BotonSaltar extends Modelo {

    public BotonSaltar(Context context) {
        super(context, GameView.pantallaAncho*0.5 , GameView.pantallaAlto*0.15,
                GameView.pantallaAlto, GameView.pantallaAncho);

        altura = (int) (GameView.pantallaAlto*0.15);
        ancho = (int) (GameView.pantallaAlto*0.75);
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.buttonjump);
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
