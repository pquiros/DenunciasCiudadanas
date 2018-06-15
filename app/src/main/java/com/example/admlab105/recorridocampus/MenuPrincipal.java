package com.example.admlab105.recorridocampus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuPrincipal extends AppCompatActivity {
    TextView usuario;
    TextView progreso;
    Integer sitioRecorridos;
    ArrayList<String> listaDeBusquedas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
       sitioRecorridos=5;
        usuario=(TextView) findViewById(R.id.textViewUsuario);
        progreso=(TextView) findViewById(R.id.textViewProgreso);
        usuario.setText("Sebastian");
        progreso.setText("Te faltan "+sitioRecorridos+" puntos por conocer");
    }
}
