package com.example.admlab105.recorridocampus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.app.Activity;

public class StartScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        int loadtime = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               /* Intent i = new Intent(StartScreen.this, Intro_activity.class);
                startActivity(i);
                this.finish();
                */
                SharedPreferences settings=getSharedPreferences("prefs", 0);
                final boolean firstRun=settings.getBoolean("firstRun",false);

                /*if (settings.getBoolean("isFirstRun", true)) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("isFirstRun", false);
                    editor.commit();
                    Intent next = new Intent(StartScreen.this, Intro_activity.class);
                    startActivity(next);
                } else {*/
                    Intent next2 = new Intent(StartScreen.this, /*MainActivity*/Menu.class);
                    startActivity(next2);
                    finish();
                //}



            }

            }, loadtime);

    }
}