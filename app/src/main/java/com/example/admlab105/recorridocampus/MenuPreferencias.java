package com.example.admlab105.recorridocampus;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class MenuPreferencias extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_menu_preferencias, container, false);
        final Switch vibracion = view.findViewById(R.id.switch1);
        vibracion.setChecked(true);
        Button btnPref = getView().findViewById(R.id.btnPrueba);
        btnPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vibracion.isChecked()){
                    prueba();
                }
                else {
                    Toast.makeText(getActivity(), "La Vibracion esta desactivada",Toast.LENGTH_LONG).show();
                }
            }
        });

        return null;
    }

    public void prueba(){
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            v.vibrate(500);
        }

    }

}
