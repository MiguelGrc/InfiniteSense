package com.infinitesense.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.infinitesense.R;
import com.infinitesense.gestores.CargadorGraficos;

public class Nivel {
    private Context context = null;
    private int numeroNivel;
    private Fondo fondo;

    public boolean inicializado;

    public Nivel(Context context, int numeroNivel) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        inicializar();

        inicializado = true;
    }

    public void inicializar()throws Exception {
        fondo = new Fondo(context,CargadorGraficos.cargarDrawable(context, R.drawable.capa1));
    }


    public void actualizar (long tiempo){
        if (inicializado) {

        }
    }


    public void dibujar (Canvas canvas) {
        if(inicializado) {
            fondo.dibujar(canvas);
        }
    }
}

