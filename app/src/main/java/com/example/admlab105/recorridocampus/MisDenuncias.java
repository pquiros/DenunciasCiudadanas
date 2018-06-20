package com.example.admlab105.recorridocampus;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MisDenuncias extends AppCompatActivity {

    private TextInputEditText tDelito;
    private TextInputEditText tDescDelito;

    //private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_denuncias);

        Button bDenuncia = (Button) findViewById(R.id.bDenuncia);
        tDelito = (TextInputEditText) findViewById(R.id.tDelito);
        tDescDelito = (TextInputEditText) findViewById(R.id.tDescDelito);

        bDenuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String titulo=getTitulo();
                //String subTitulo=getSubtitulo();
                //intent = new Intent(MisDenuncias.this, Tab1Fragment.class/*typeof(MisDenuncias)*/);
                Intent intent = new Intent();
                intent.putExtra("Titulo crimen",getTitulo());
                intent.putExtra("Descripcion crimen",getSubtitulo());
                //startActivity(intent);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

    public String getTitulo() {
        return tDelito.getText().toString();
    }

    public String getSubtitulo() {
        return tDescDelito.getText().toString();
    }
}
