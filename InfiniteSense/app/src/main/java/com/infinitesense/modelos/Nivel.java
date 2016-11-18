package com.infinitesense.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.infinitesense.GameView;
import com.infinitesense.R;
import com.infinitesense.gestores.CargadorGraficos;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Nivel {
    private Context context = null;
    private int numeroNivel;
    private Fondo fondo;

    private Tile[][] mapaTiles;

    public boolean inicializado;

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
     * @throws Exception
     */
    public void inicializar()throws Exception {
        fondo = new Fondo(context,CargadorGraficos.cargarDrawable(context, R.drawable.sunset_background));
        inicializarMapaTiles();
    }

    /**
     * En cada iteración del main loop del juego (el corazón del juego) se debe actualizar
     * tod0 lo que depende del tiempo.
     * @param tiempo
     */
    public void actualizar (long tiempo){
        if (inicializado) {

        }
    }


    public void dibujar (Canvas canvas) {
        if(inicializado) {
            fondo.dibujar(canvas);
        }

        dibujarTiles(canvas);
    }


    public int anchoMapaTiles(){
        return mapaTiles.length;
    }

    public int altoMapaTiles(){

        return mapaTiles[0].length;
    }

    /**
     * Metodo para cargar los tiles en pantalla:
     * . -> Nada
     * g -> Tile suelo con hierva
     * G -> Tile suelo sin hierva
     * b -> Tile suelo BORDE IZQUIERDA
     * B -> Tile suelo BORDE DERECHA
     * a -> Tile suelo ABISMO IZQUIERDA
     * A -> Tile suelo ABISMO DERECHA
     * w -> Tile agua SUPERFICIE
     * W -> Tile agua PROFUNDO
     *
     * No olvidar modificar el txt para modelar el mapa.
     * @param codigoTile
     * @param  x  posicion x del tile que toca
     * @param  y posicion y del tile que toca
     * @return El tile creado para añadir a la lista de Tiles.
     */
    private Tile inicializarTile(char codigoTile,int x, int y) {
        switch (codigoTile) {
            case '.':
                // en blanco, sin textura
                return new Tile(null, Tile.PASABLE);
            case 'g':
                // bloque de musgo, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_ground), Tile.SOLIDO);

            case 'G':
                // bloque de musgo, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_ground_pure), Tile.SOLIDO);

            case 'b':
                // bloque de musgo, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_ground_border_left), Tile.SOLIDO);

            case 'B':
                // bloque de musgo, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_ground_border_right), Tile.SOLIDO);

            case 'a':
                // bloque de musgo, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_ground_abyss_left), Tile.SOLIDO);

            case 'A':
                // bloque de musgo, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_ground_abyss_right), Tile.SOLIDO);
            case 'w':
                // bloque de musgo, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_water_surface), Tile.SOLIDO);
            case 'W':
                // bloque de musgo, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_water_pure), Tile.SOLIDO);
            default:
                //cualquier otro caso
                return new Tile(null, Tile.PASABLE);
        }
    }

    /**
     * Dibuja los tiles donde corresponde.
     * @param canvas
     */
    private void dibujarTiles(Canvas canvas){
        // Calcular que tiles serán visibles en la pantalla
        // La matriz de tiles es más grande que la pantalla

        int izquierda = 0; //El primer tile

        int derecha = izquierda +
                ( GameView.pantallaAncho / Tile.ancho ) + 1;

        // el ultimo tile visible
        derecha = Math.min(derecha, anchoMapaTiles() - 1);

        for (int y = 0; y < altoMapaTiles() ; ++y) {
            for (int x = izquierda; x <= derecha; ++x) {
                if (mapaTiles[x][y].imagen != null) {
                    // Calcular la posición en pantalla correspondiente
                    // izquierda, arriba, derecha , abajo

                    mapaTiles[x][y].imagen.setBounds(
                            x  * Tile.ancho,
                            y * Tile.altura,
                            x * Tile.ancho + Tile.ancho,
                            y * Tile.altura + Tile.altura);

                    mapaTiles[x][y].imagen.draw(canvas);
                }
            }
        }
    }


    /**
     * Inicializa los tiles leyendo el txt uno a uno llamando a:
     * ->inicializarTile
     * @throws Exception
     */
    private void inicializarMapaTiles() throws Exception {
        InputStream is = context.getAssets().open(numeroNivel+".txt");
        int anchoLinea;

        List<String> lineas = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        {
            String linea = reader.readLine();
            anchoLinea = linea.length();
            while (linea != null)
            {
                lineas.add(linea);
                if (linea.length() != anchoLinea)
                {
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
                mapaTiles[x][y] = inicializarTile(tipoDeTile,x,y);
            }
        }
    }
}

