package com.infinitesense;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establecer el control de sonido multimedia como predefinido
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Pantalla completa, sin titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);

        ImageButton botonNuevoJuego = (ImageButton) findViewById(R.id.imageButton);
        botonNuevoJuego.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent actividadJuego = new Intent(MenuActivity.this,
                        MainActivity.class);
                startActivity(actividadJuego);
                finish();
            }
        });
    }


}
