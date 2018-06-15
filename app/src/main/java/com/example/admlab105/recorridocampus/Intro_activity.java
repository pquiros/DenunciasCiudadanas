/*package com.example.admlab105.recorridocampus;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Intro_activity extends AppCompatActivity {
    Button bclose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_activity);
        bclose= (Button) findViewById(R.id.buttonClose);
        bclose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intro_activity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

}*/

package com.example.admlab105.recorridocampus;

import android.content.Intent;
        import android.os.Handler;
        import android.os.Bundle;
        import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class Intro_activity extends Activity {

    Button bclose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_activity);
        bclose= (Button) findViewById(R.id.buttonClose);
        bclose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intro_activity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}
