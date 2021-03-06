package com.infinitesense;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.infinitesense.gestores.GestorAudio;
import com.infinitesense.modelos.Nivel;
import com.infinitesense.modelos.controles.BotonGolpear;
import com.infinitesense.modelos.controles.BotonSaltar;
import com.infinitesense.modelos.controles.BotonAgachar;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    boolean iniciado = false;
    Context context;
    GameLoop gameloop;

    public static int pantallaAncho;
    public static int pantallaAlto;

    private Nivel nivel;
    public int numeroNivel = 0;
    private BotonGolpear botonGolpear;
    private BotonSaltar botonSaltar;
    private BotonAgachar pad;

    private GestorAudio gestorAudio;


    public GameView(Context context) {
        super(context);
        iniciado = true;

        getHolder().addCallback(this);
        setFocusable(true);

        this.context = context;
        gameloop = new GameLoop(this);
        gameloop.setRunning(true);
    }

    public void inicializarGestorAudio(Context context) {
        gestorAudio = GestorAudio.getInstancia(context, R.raw.dire_dire_docks);
        gestorAudio.reproducirMusicaAmbiente();
        //Sonidos de interaccion
        gestorAudio.registrarSonido(GestorAudio.SONIDO_NOTA,
                R.raw.note_sound);
        gestorAudio.registrarSonido(GestorAudio.SONIDO_MUERTE, R.raw.die_sound );
        gestorAudio.registrarSonido(GestorAudio.SONIDO_MONEDA, R.raw.coin_sound );
        gestorAudio.registrarSonido(GestorAudio.SONIDO_DESTRUIR, R.raw.concrete_break );
        gestorAudio.registrarSonido(GestorAudio.SONIDO_POWERUP_RAPIDO, R.raw.faster );
        gestorAudio.registrarSonido(GestorAudio.SONIDO_POWERUP_LENTO, R.raw.slower );
        gestorAudio.registrarSonido(GestorAudio.SONIDO_META, R.raw.meta );
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // valor a Binario
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        // Indice del puntero
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

        int pointerId = event.getPointerId(pointerIndex);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                accion[pointerId] = ACTION_DOWN;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                accion[pointerId] = ACTION_UP;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    pointerIndex = i;
                    pointerId = event.getPointerId(pointerIndex);
                    accion[pointerId] = ACTION_MOVE;
                    x[pointerId] = event.getX(pointerIndex);
                    y[pointerId] = event.getY(pointerIndex);
                }
                break;
        }

        procesarEventosTouch();
        return true;
    }

    int NO_ACTION = 0;
    int ACTION_MOVE = 1;
    int ACTION_UP = 2;
    int ACTION_DOWN = 3;
    int accion[] = new int[6];
    float x[] = new float[6];
    float y[] = new float[6];

    /**
     * Este metodo procesa las señales de los botones tactiles. Saltar, golpear y pad (que hace la función de agacharse).
     */
    public void procesarEventosTouch() {
        for (int i = 0; i < 6; i++) {
            if (accion[i] != NO_ACTION) {
                if (accion[i] == ACTION_UP && pad.estaPulsado(x[i],y[i])) {
                    nivel.botonAgacharPulsado = false;
                }


                if (accion[i] == ACTION_DOWN) {

                    if(nivel.nivelCompleto){
                        nivel.cambiarNivel();
                    }
                    else if (nivel.nivelPausado) {
                        nivel.nivelPausado = false;
                        GestorAudio.getInstancia().reproducirMusicaAmbiente();
                        nivel.restaurarNivel();
                    }

                    if (botonGolpear.estaPulsado(x[i], y[i])) {
                        if (accion[i] == ACTION_DOWN) {
                            nivel.botonGolpearPulsado = true;
                        }
                    }
                    if (botonSaltar.estaPulsado(x[i], y[i])) {
                        if (accion[i] == ACTION_DOWN) {
                            nivel.botonSaltarPulsado = true;
                        }
                    }
                    if (pad.estaPulsado(x[i], y[i])) {
                        nivel.botonGolpearPulsado = false;//En caso de que se bugue haciendo ambas.
                        nivel.botonAgacharPulsado = true;
                    }
                }


            }
        }
    }

    protected void inicializar() throws Exception {
        inicializarGestorAudio(context);
        nivel = new Nivel(context, numeroNivel);
        nivel.nivelPausado=true;
        gestorAudio.pararMusicaAmbiente();
        botonGolpear = new BotonGolpear(context);
        botonSaltar = new BotonSaltar(context);
        pad = new BotonAgachar(context);
        nivel.gameview = this;

    }

    public void actualizar(long tiempo) throws Exception {
        nivel.actualizar(tiempo);
    }

    protected void dibujar(Canvas canvas) {
        nivel.dibujar(canvas);
    }

    /**
     * Cada vez que se acaba el nivel se llama a este método para comprobar cual es el siguiente nivel a inicializar.
     *
     * @throws Exception
     */
    public void nivelCompleto() throws Exception {

        if (numeroNivel < 2) { // Número Máximo de Nivel
            numeroNivel++;
        } else {
            numeroNivel = 0;
        }
        inicializar();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        pantallaAncho = width;
        pantallaAlto = height;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (iniciado) {
            iniciado = false;
            if (gameloop.isAlive()) {
                iniciado = true;
                gameloop = new GameLoop(this);
            }

            gameloop.setRunning(true);
            gameloop.start();
        } else {
            iniciado = true;
            gameloop = new GameLoop(this);
            gameloop.setRunning(true);
            gameloop.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        iniciado = false;

        boolean intentarDeNuevo = true;
        gameloop.setRunning(false);
        while (intentarDeNuevo) {
            try {
                gameloop.join();
                intentarDeNuevo = false;
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Uso del teclado para moverse:
     * W -> Saltar
     * S -> Agacharse
     * Espacio -> Golpear
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("Tecla", "Tecla pulsada: " + keyCode);

        if (keyCode == 51) {
            nivel.botonSaltarPulsado = true;
        }
        if (keyCode == 62) {
            nivel.botonGolpearPulsado = true;
        }
        if (keyCode == 47) {
            nivel.botonAgacharPulsado = true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(nivel.jugador.estadoAgachado){
            nivel.botonAgacharPulsado=false;
        }
        return super.onKeyDown(keyCode,event);
    }

}


