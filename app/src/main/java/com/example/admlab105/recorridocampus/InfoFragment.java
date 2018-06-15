package com.example.admlab105.recorridocampus;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InfoFragment extends Fragment {

    //private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private String etiqueta;
    private BaseSitiosHelper db;
    private String texto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etiqueta = this.getArguments().getString("etiq");
        db = BaseSitiosHelper.getInstance(this.getContext());

        setRetainInstance(true);
    }
}
