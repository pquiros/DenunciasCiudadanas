package com.example.admlab105.recorridocampus;

import android.app.ActionBar;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    public ViewPager mViewPager;
    private boolean isUserClickedBackButton = false;
    public int marcador=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.tuto) {
            Toast.makeText(this, "Tutorial", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.intro) {
            //Toast.makeText(this, "Introducción", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, Intro_activity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.credits) {
            Toast.makeText(this, "Créditos", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Presione el botón con la lupa para hacer una denuncia");
        //adapter.addFragment(new Tab2Fragment(), "Mi recorrido");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if(!(currentFragment instanceof InfoFragment)) {
            // do something with f
            /*((Tab1Fragment) currentFragment).doSomething();*/
            if (!isUserClickedBackButton) {
                Toast.makeText(this,"Presione de nuevo para salir", Toast.LENGTH_LONG).show();
                isUserClickedBackButton = true;
            } else {
                super.onBackPressed();
            }

            new CountDownTimer(3000,1000){

                @Override
                public void onTick(long msUntilFinish) {}

                @Override
                public void onFinish(){
                    isUserClickedBackButton = false;
                }
            }.start();
        } else {
            super.onBackPressed();
        }
    }
}
