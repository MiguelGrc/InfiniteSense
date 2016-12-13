package com.infinitesense.modelos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.infinitesense.GameView;
import com.infinitesense.R;
import com.infinitesense.gestores.CargadorGraficos;
import com.infinitesense.gestores.GestorAudio;
import com.infinitesense.gestores.Utilidades;
import com.infinitesense.modelos.Tiles.Tile;
import com.infinitesense.modelos.Tiles.TileNormal;
import com.infinitesense.modelos.Tiles.TileNota;
import com.infinitesense.modelos.controles.ContadorMonedas;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Nivel {
    private Context context = null;
    private int numeroNivel;
    private Fondo fondo;
    private Jugador jugador;

    private Tile[][] mapaTiles;

    public boolean inicializado;

    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;

    private float velocidadGravedad = 0.8f;
    private float velocidadMaximaCaida = 10;

    public boolean nivelPausado;
    public boolean botonGolpearPulsado;
    public boolean botonSaltarPulsado;


    public GameView gameview;
    private Bitmap mensaje;
    public boolean botonAgacharPulsado;

    public LinkedList<Obstaculo> obstaculos;

    private ContadorMonedas contadorMonedas;

    public Nivel(Context context, int numeroNivel) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        inicializar();

        inicializado = true;
    }


    /**
     * Inicia el nivel preparandolo entero:
     * -> Inicializar atributos
     * -> cargando nuevos recursos
     *
     * @throws Exception
     */
    public void inicializar() throws Exception {
        scrollEjeX = 0;
        scrollEjeY = 0;

        obstaculos= new LinkedList<Obstaculo>();


        mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.description);
        fondo = new Fondo(context, CargadorGraficos.cargarDrawable(context, R.drawable.sunset_background));
        inicializarMapaTiles();
        contadorMonedas = new ContadorMonedas(context);
    }

    /**
     * En cada iteración del main loop del juego (el corazón del juego) se debe actualizar
     * tod0 lo que depende del tiempo.
     *
     * @param tiempo
     */
    public void actualizar(long tiempo) throws Exception {
        if (inicializado) {
            jugador.procesarOrdenes(botonSaltarPulsado, botonGolpearPulsado, botonAgacharPulsado, nivelPausado);
            if (botonSaltarPulsado) {
                botonSaltarPulsado = false;
            }
            if (botonAgacharPulsado){
                botonAgacharPulsado = false;
            }
            if (botonGolpearPulsado) {
                //TODO: Hay que comprobar colisiones contra que golpea
                botonGolpearPulsado = false;
            }
                actualizarIteraccionTiles();
                jugador.actualizar(tiempo);
                aplicarReglasMovimiento();
                aplicarReglasDeMovimiento2();
                comprobarColisiones();
                actualizarElementos(tiempo);


                //Ahora necesito actualizar las tiles por el tema de los recolectables
                for(int x = 0; x < anchoMapaTiles(); ++x) {
                    for (int y = 0; y < altoMapaTiles(); ++y) {
                        mapaTiles[x][y].actualizar(tiempo);

                        if (mapaTiles[x][y] instanceof Moneda) {
                            Moneda mo = (Moneda) mapaTiles[x][y];
                            if (mo.colisiona(jugador, x, y) && !mo.isRecogido()) {
                                mo.recoger();
                                contadorMonedas.setPuntos(contadorMonedas.getPuntos() + 1);
                            }
                        }
                    }
                }

            }
        }

    private void actualizarElementos(Long tiempo) {
        Obstaculo obstaculoADestruir= null;
        for(Obstaculo obs: obstaculos){
            if(obs.actualizar(tiempo)){//Actualiza y si se ha acabado el sprite se destruye.
                obstaculoADestruir=obs;
            }
        }
        if(obstaculoADestruir!=null){
            //obstaculos.remove(obstaculoADestruir); //En realidad no se puede destruir, ya que al iniciar de nuevo el juego debería resetearse.
        }
    }

    private void actualizarIteraccionTiles() {
        if(!jugador.enElAire) { //Los cambios relacionados con los tiles solo se producen si esta en el suelo.
            int tileXJugador
                    = (int) (jugador.x / Tile.ancho);
            int tileYJugadorInferior
                    = (int) (jugador.y + (jugador.altura / 2 - 1)) / Tile.altura+1; //si no le sumas uno no coge el de abajo.
            Log.v("Debugging","Tile interaccion x: "+tileXJugador+"y inferior: "+tileYJugadorInferior);
            mapaTiles[tileXJugador][tileYJugadorInferior].interactuar(jugador);
        }
    }


    private void aplicarReglasMovimiento() throws Exception {

        //jugador
        int tileXJugadorIzquierda
                = (int) (jugador.x - (jugador.ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorDerecha
                = (int) (jugador.x + (jugador.ancho / 2 - 1)) / Tile.ancho;

        int tileYJugadorInferior
                = (int) (jugador.y + (jugador.altura / 2 - 1)) / Tile.altura;
        int tileYJugadorCentro
                = (int) jugador.y / Tile.altura;
        int tileYJugadorSuperior
                = (int) (jugador.y - (jugador.altura / 2 - 1)) / Tile.altura;

        // Gravedad Jugador
        if (jugador.enElAire) {
            // Recordar los ejes:
            // - es para arriba       + es para abajo.
            jugador.velocidadY += velocidadGravedad;
            if (jugador.velocidadY > velocidadMaximaCaida) {
                jugador.velocidadY = velocidadMaximaCaida;
            }
        }

        if (jugador.velocidadX > 0) {
            // Tengo un tile delante y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorDerecha + 1 <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                jugador.x += jugador.velocidadX;

                // No tengo un tile PASABLE delante
                // o es el FINAL del nivel o es uno SOLIDO


            } else if (tileXJugadorDerecha <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {


                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeDerecho = tileXJugadorDerecha * Tile.ancho + Tile.ancho;
                double distanciaX = TileJugadorBordeDerecho - (jugador.x + jugador.ancho / 2);

                if (distanciaX > 0) {
                    double velocidadNecesaria = Math.min(distanciaX, jugador.velocidadX);
                    jugador.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    jugador.x = TileJugadorBordeDerecho - jugador.ancho / 2;
                }
            }
        }
        // izquierda
        if (jugador.velocidadX <= 0) {
            // Tengo un tile detrás y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorIzquierda - 1 >= 0 &&
                    tileYJugadorInferior < altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                jugador.x += jugador.velocidadX;

                // No tengo un tile PASABLE detrás
                // o es el INICIO del nivel o es uno SOLIDO
            } else if (tileXJugadorIzquierda >= 0 && tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision
                            == Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
              /*  // avanzar más, avanzo
                int TileJugadorBordeIzquierdo = tileXJugadorIzquierda * Tile.ancho;
                double distanciaX = (jugador.x - jugador.ancho / 2) - TileJugadorBordeIzquierdo;

                if (distanciaX > 0) {
                    double velocidadNecesaria = Utilidades.proximoACero(-distanciaX, jugador.velocidadX);
                    jugador.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    jugador.x = TileJugadorBordeIzquierdo + jugador.ancho / 2;
                }*/

                // Pierde directamente
                scrollEjeX = 0;
                scrollEjeY = 0;
                jugador.restablecerPosicionInicial();
                nivelPausado = true;
                mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.you_lose);
            }
        }
        // Hacia arriba
        if (jugador.velocidadY < 0) {
            // Tile superior PASABLE
            // Podemos seguir moviendo hacia arriba
            if (tileYJugadorSuperior - 1 >= 0 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior - 1].tipoDeColision
                            == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior - 1].tipoDeColision
                    == Tile.PASABLE) {

                jugador.y += jugador.velocidadY;

                // Tile superior != de PASABLE
                // O es un tile SOLIDO, o es el TECHO del mapa
            } else {

                // Si en el propio tile del jugador queda espacio para
                // subir más, subo
                int TileJugadorBordeSuperior = (tileYJugadorSuperior) * Tile.altura;
                double distanciaY = (jugador.y - jugador.altura / 2) - TileJugadorBordeSuperior;

                if (distanciaY > 0) {
                    jugador.y += Utilidades.proximoACero(-distanciaY, jugador.velocidadY);

                } else {
                    // Efecto Rebote -> empieza a bajar;
                    jugador.velocidadY = velocidadGravedad;
                    jugador.y += jugador.velocidadY;
                }

            }
        }
        // Hacia abajo
        if (jugador.velocidadY >= 0) {
            // Tile inferior PASABLE
            // Podemos seguir moviendo hacia abajo
            // NOTA - El ultimo tile es especial (caer al vacío )
            if (tileYJugadorInferior + 1 <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior + 1].tipoDeColision
                            == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorInferior + 1].tipoDeColision
                    == Tile.PASABLE) {
                // si los dos están libres cae

                jugador.y += jugador.velocidadY;
                jugador.enElAire = true; // Sigue en el aire o se cae
                // Tile inferior SOLIDO
                // El ULTIMO, es un caso especial

            } else if (tileYJugadorInferior + 1 <= altoMapaTiles() - 1 &&
                    (mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior + 1].tipoDeColision
                            == Tile.SOLIDO ||
                            mapaTiles[tileXJugadorDerecha][tileYJugadorInferior + 1].tipoDeColision ==
                                    Tile.SOLIDO)) {

                // Con que uno de los dos sea solido ya no puede caer
                // Si en el propio tile del jugador queda espacio para bajar más, bajo
                int TileJugadorBordeInferior =
                        tileYJugadorInferior * Tile.altura + Tile.altura;

                double distanciaY =
                        TileJugadorBordeInferior - (jugador.y + jugador.altura / 2);

                jugador.enElAire = true; // Sigue en el aire o se cae
                if (distanciaY > 0) {
                    jugador.y += Math.min(distanciaY, jugador.velocidadY);

                } else {
                    // Toca suelo, nos aseguramos de que está bien
                    jugador.y = TileJugadorBordeInferior - jugador.altura / 2;
                    jugador.velocidadY = 0;
                    jugador.enElAire = false;
                }

                // Esta cayendo por debajo del ULTIMO
                // va a desaparecer y perder.
            } else {

                jugador.y += jugador.velocidadY;
                jugador.enElAire = true;

                if (jugador.y + jugador.altura / 2 > GameView.pantallaAlto) {
                    // ha perdido
                    jugadorPerder();
                }

            }
        }
        /*
        if (jugador.colisiona(meta)) {
            gameview.nivelCompleto();
        }*/
    }

    /**
     * Método para hacer al jugador perder cuando colisiona con algo.
     */
    public void aplicarReglasDeMovimiento2(){
        int estabilizador= (int) (jugador.x-20); //Para hacer perder al jugador al chocarse contra algo mas realista , mas pegado al bloque.

        int tileXJugadorCentro=(int)(estabilizador/ Tile.ancho);

        int tileYJugadorInferior
                = (int) (jugador.y + (jugador.altura / 2 - 1)) / Tile.altura;
        int tileYJugadorCentro
                = (int) jugador.y / Tile.altura;
        int tileYJugadorSuperior
                = (int) (jugador.y - (jugador.altura / 2 - 1)) / Tile.altura;
//        Log.v("Debug","TileXDown: "+mapaTiles[tileXJugadorCentro][tileYJugadorInferior].tipoDeColision+"tileXStraight: "+ mapaTiles[tileXJugadorCentro][tileYJugadorCentro].tipoDeColision+
//        "TileXUp: "+mapaTiles[tileXJugadorCentro][tileYJugadorSuperior].tipoDeColision); //Para debugear.

        if(tileXJugadorCentro > anchoMapaTiles()-2){ //Ha perdido por chocar contra el fin del mapa.
            // ha perdido
            jugadorPerder();
        }
        else if(//Si choca de frente contra un tile no pasable.
                mapaTiles[tileXJugadorCentro+1][tileYJugadorInferior].tipoDeColision
                        == Tile.SOLIDO ||
                mapaTiles[tileXJugadorCentro+1][tileYJugadorCentro].tipoDeColision
                        == Tile.SOLIDO ||
                mapaTiles[tileXJugadorCentro+1][tileYJugadorSuperior].tipoDeColision
                        == Tile.SOLIDO){
            // ha perdido
            jugadorPerder();

        }
    }

    /**
     * Lo prepara todo cuando el jugador muere.
     */
    private void jugadorPerder(){
        scrollEjeX = 0;
        scrollEjeY = 0;
        jugador.restablecerPosicionInicial();
        jugador.enElAire=false; //No funcionaba al morir antes.
        nivelPausado = true;
        mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.you_lose);
        GestorAudio.getInstancia().pararMusicaAmbiente();
        GestorAudio.getInstancia().reproducirSonido(GestorAudio.SONIDO_MUERTE);
        reiniciarElementosDestruidos(); //Hay que reiniciar lo destruido;

    }

    /**
     * Reinicia los elementos En caso de que fueran destruidos o cambiados de estado a como deberian estar al iniciar el juego.
     */
    private void reiniciarElementosDestruidos() {
        for(Obstaculo obs: obstaculos){
            obs.reiniciar();
        }

    }


    public void dibujar(Canvas canvas) {
        if (inicializado) {
            fondo.dibujar(canvas);
        }
        dibujarTiles(canvas);
        jugador.dibujar(canvas);
        contadorMonedas.dibujar(canvas);

        for(Obstaculo obs: obstaculos){
            obs.dibujar(canvas);
        }

        if (nivelPausado) {
            // la foto mide 480x320
            Rect orgigen = new Rect(0, 0,
                    480, 320);

            Paint efectoTransparente = new Paint();
            efectoTransparente.setAntiAlias(true);

            Rect destino = new Rect((int) (GameView.pantallaAncho / 2 - 480 / 2),
                    (int) (GameView.pantallaAlto / 2 - 320 / 2),
                    (int) (GameView.pantallaAncho / 2 + 480 / 2),
                    (int) (GameView.pantallaAlto / 2 + 320 / 2));
            canvas.drawBitmap(mensaje, orgigen, destino, null);
        }
    }


    public int anchoMapaTiles() {
        return mapaTiles.length;
    }

    public int altoMapaTiles() {

        return mapaTiles[0].length;
    }

    /**
     * Metodo para cargar los tiles en pantalla:
     * . -> Nada
     * g -> Tile suelo con hierva
     * 1 -> Tile inicial del jugador
     * G -> Tile suelo sin hierva
     * b -> Tile suelo BORDE IZQUIERDA
     * B -> Tile suelo BORDE DERECHA
     * a -> Tile suelo ABISMO IZQUIERDA
     * A -> Tile suelo ABISMO DERECHA
     * w -> Tile agua SUPERFICIE
     * W -> Tile agua PROFUNDO
     * M -> Tile de nota para salto mejorado.
     * O -> Obstaculo.
     * <p>
     * No olvidar modificar el txt para modelar el mapa.
     *
     * @param codigoTile
     * @param x          posicion x del tile que toca
     * @param y          posicion y del tile que toca
     * @return El tile creado para añadir a la lista de Tiles.
     */
    private Tile inicializarTile(char codigoTile, int x, int y) {
        //Para crear objetos(calcular la posición que le corresponde.):
        //int xCentroAbajoTile = x * Tile.ancho + Tile.ancho/2;
        //int yCentroAbajoTile = y * Tile.altura + Tile.altura;
        switch (codigoTile) {
            case '.':
                // en blanco, sin textura
                return new TileNormal(null, Tile.PASABLE);
            case 'C':
                // Moneda
                return new Moneda(context);
            case '1':
                // Jugador
                // Posicion centro abajo
                int xCentroAbajoTile = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTile = y * Tile.altura + Tile.altura;
                jugador = new Jugador(context, xCentroAbajoTile, yCentroAbajoTile);

                return new TileNormal(null, Tile.PASABLE);
            case 'g':
                // bloque de musgo, no se puede pasar
                return new TileNormal(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_ground), Tile.SOLIDO);

            case 'G':
                // bloque de musgo, no se puede pasar
                return new TileNormal(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_ground_pure), Tile.SOLIDO);

            case 'b':
                // bloque de musgo, no se puede pasar
                return new TileNormal(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_ground_border_left), Tile.SOLIDO);

            case 'B':
                // bloque de musgo, no se puede pasar
                return new TileNormal(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_ground_border_right), Tile.SOLIDO);

            case 'a':
                // bloque de musgo, no se puede pasar
                return new TileNormal(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_ground_abyss_left), Tile.SOLIDO);

            case 'A':
                // bloque de musgo, no se puede pasar
                return new TileNormal(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_ground_abyss_right), Tile.SOLIDO);
            case 'w':
                // bloque de musgo, no se puede pasar
                return new TileNormal(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_water_surface), Tile.PASABLE);
            case 'W':
                // bloque de musgo, no se puede pasar
                return new TileNormal(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_water_pure), Tile.PASABLE);
            case 'M':
                //bloque de nota musica, para salto mejorado.
                return new TileNota(CargadorGraficos.cargarDrawable(context,
                        R.drawable.note_block), Tile.SOLIDO);
            case 'O':
                //Obstaculo.
                obstaculos.add(new Obstaculo(context,x * Tile.ancho + Tile.ancho/2,y * Tile.altura + Tile.altura));
                return new TileNormal(null,Tile.PASABLE);

            default:
                //cualquier otro caso
                return new TileNormal(null, Tile.PASABLE);
        }
    }

    /**
     * Dibuja los tiles donde corresponde.
     *
     * @param canvas
     */
    private void dibujarTiles(Canvas canvas) {
        int tileYJugador = (int) jugador.y / Tile.altura;
        int arriba = (int) (tileYJugador - tilesEnDistanciaY(jugador.y - scrollEjeY));
        arriba = Math.max(0, arriba); // Que nunca sea < 0, ej -1

        if (jugador.y <
                altoMapaTiles() * Tile.altura - GameView.pantallaAlto * 0.3)
            if (jugador.y - scrollEjeY > GameView.pantallaAlto * 0.7) {
                scrollEjeY += (int) ((jugador.y - scrollEjeY) - GameView.pantallaAlto * 0.7);
            }


        if (jugador.y > GameView.pantallaAlto * 0.3)
            if (jugador.y - scrollEjeY < GameView.pantallaAlto * 0.3) {
                scrollEjeY -= (int) (GameView.pantallaAlto * 0.3 - (jugador.y - scrollEjeY));
            }


        int abajo = arriba +
                GameView.pantallaAlto / Tile.altura + 1;

        // el ultimo tile visible, no puede superar el tamaño del mapa
        abajo = Math.min(abajo, altoMapaTiles() - 1);


        //SCROLL Y /////////////////////////////////

        int tileXJugador = (int) jugador.x / Tile.ancho;
        int izquierda = (int) (tileXJugador - tilesEnDistanciaX(jugador.x - scrollEjeX));
        izquierda = Math.max(0, izquierda); // Que nunca sea < 0, ej -1


        if (jugador.x <
                (anchoMapaTiles() - tilesEnDistanciaX(GameView.pantallaAncho * 0.3)) * Tile.ancho)
            if (jugador.x - scrollEjeX > GameView.pantallaAncho * 0.7) {
                scrollEjeX += (int) ((jugador.x - scrollEjeX) - GameView.pantallaAncho * 0.7);

            }

        if (jugador.x >
                tilesEnDistanciaX(GameView.pantallaAncho * 0.3) * Tile.ancho)
            if (jugador.x - scrollEjeX < GameView.pantallaAncho * 0.3) {
                scrollEjeX -= (int) (GameView.pantallaAncho * 0.3 - (jugador.x - scrollEjeX));
            }


        int derecha = izquierda +
                GameView.pantallaAncho / Tile.ancho + 1;


        // el ultimo tile visible, no puede superar el tamaño del mapa
        derecha = Math.min(derecha, anchoMapaTiles() - 1);

        for (int y = arriba; y <= abajo; ++y) {
            for (int x = izquierda; x <= derecha; ++x) {
                /*
                if (mapaTiles[x][y].imagen != null) {
                    // Calcular la posición en pantalla correspondiente
                    // izquierda, arriba, derecha , abajo

                    mapaTiles[x][y].imagen.setBounds(
                            (x * Tile.ancho) - scrollEjeX,
                            (y * Tile.altura) - scrollEjeY,
                            (x * Tile.ancho) + Tile.ancho - scrollEjeX,
                            (y * Tile.altura) + Tile.altura - scrollEjeY);

                    mapaTiles[x][y].imagen.draw(canvas);
                }
                */

                mapaTiles[x][y].dibujar(canvas,x,y);    //Sustituido lo anterior por esto y añadido método dibujar a Tile
            }
        }
    }

    private float tilesEnDistanciaX(double distanciaX) {
        return (float) distanciaX / Tile.ancho;
    }

    private float tilesEnDistanciaY(double distanciaY) {
        return (float) distanciaY / Tile.altura;
    }


    /**
     * Inicializa los tiles leyendo el txt uno a uno llamando a:
     * ->inicializarTile
     *
     * @throws Exception
     */
    private void inicializarMapaTiles() throws Exception {
        InputStream is = context.getAssets().open(numeroNivel + ".txt");
        int anchoLinea;

        List<String> lineas = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        {
            String linea = reader.readLine();
            anchoLinea = linea.length();
            while (linea != null) {
                lineas.add(linea);
                if (linea.length() != anchoLinea) {
                    Log.e("ERROR", "Dimensiones incorrectas en la línea");
                    throw new Exception("Dimensiones incorrectas en la línea.");
                }
                linea = reader.readLine();
            }
        }

        // Inicializar la matriz
        mapaTiles = new Tile[anchoLinea][lineas.size()];
        // Iterar y completar todas las posiciones
        for (int y = 0; y < altoMapaTiles(); ++y) {
            for (int x = 0; x < anchoMapaTiles(); ++x) {
                char tipoDeTile = lineas.get(y).charAt(x);//lines[y][x];
                mapaTiles[x][y] = inicializarTile(tipoDeTile, x, y);
            }
        }
    }
    private void comprobarColisiones(){
        for(Obstaculo obs: obstaculos){
            if(obs.colisiona(jugador) && obs.estado==obs.NORMAL){ //Si se dan y el obstaculo esta normal.
                if(jugador.estadoGolpeando){
                    obs.destruir();
                }
                else{
                    jugadorPerder();
                }
            }
        }
    }
}

