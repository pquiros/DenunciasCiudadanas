package com.example.admlab105.recorridocampus;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import java.util.ArrayList;
import org.osmdroid.views.MapController;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import java.util.LinkedList;

import static android.app.Activity.RESULT_OK;


public class Tab1Fragment extends Fragment implements MapEventsReceiver{
    private MapView map;
    private MapController mc;
    private LocationManager locationManager;
    private MyLocationNewOverlay mMyLocationOverlay;
    MapEventsOverlay mapEventsOverlay;

    double lat = 0.0, lon = 0.0;
    private Marker marker;
    private Marker marker2;
    private LinkedList<Marker> sitios;
    private BaseSitiosHelper db;
    private int RADIO = 200;
    private MisDenuncias misDenuncias;
    private String TITULO = "";
    private String DESCRIPCION = "";

    private static final int PERMISSIONS_REQUEST_LOCATION = 1;



    ArrayList<OverlayItem> marcadores;
    ArrayList<GeoPoint> marcadores2;

    TextView nombreSitioCercano;

    Handler handler;
    Handler cercania;

    GeoPoint user;
    GeoPoint user2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        almacenar();// Petición de permiso para external storage
        //misDenuncias = new MisDenuncias();

       //db = BaseSitiosHelper.getInstance(this.getContext().getApplicationContext());
        View view = inflater.inflate(R.layout.tab1_fragment, container, false);
        Context ctx = getActivity();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //map = new MapView(getActivity());
        map = view.findViewById(R.id.map);


        map.getTileProvider().setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(14.0);
        GeoPoint startPoint = new GeoPoint(9.9330, -84.0796);
        mapController.setCenter(startPoint);


        map.setMultiTouchControls(true);


        //Button btnUCR =  view.findViewById(R.id.btnUcr);
        //Button btnCat = view.findViewById(R.id.btnCat);

        ImageButton btnCampus = view.findViewById(R.id.btnCampus);
        ImageButton btnDenuncia = view.findViewById(R.id.btnDenuncia);
        //ImageButton btnUser = view.findViewById(R.id.btnUser);
        //ImageButton btnCerca = view.findViewById(R.id.btnCerca);
        //nombreSitioCercano = view.findViewById(R.id.nombreSitioText);

        btnCampus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverCampus();
            }
        });
        btnDenuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerDenuncia();
            }
        });


         marcadores = new ArrayList<OverlayItem>();

        marcadores2 = new ArrayList<GeoPoint>();

        sitios = new LinkedList<Marker>();
        //Cursor c = db.obtenerLugares();

        agregarMarcador2(9.9329,  -84.0793, "Asalto", "a las 9 pm, dos sujetos");
        agregarMarcador2(9.9329,  -84.0788, "Vandalismo", "en la estatua del Papa");
        agregarMarcador2(9.9341,  -84.0797, "Ventas ambulantes", "a toda hora. Se entorpece el paso por la Ave. Central");
        agregarMarcador2(9.9341,  -84.0799, "Ventas ilegales", "la Policía Municipal nunca aparece");
        agregarMarcador2(9.9333,  -84.0781, "Consumo de drogas", "gente fumando y vendiendo marihuana");


        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> gestureListener = new OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                //do something
                //addMarker(new GeoPoint());
                item.getPoint();
                Marker mark = new Marker(map);
                mark.setTitle(item.getTitle());
                //String distancia = "Está a " + (int) user.distanceToAsDouble(item.getPoint()) + " mts. de distancia";
                //mark.setSnippet(/*item.getSnippet()*/distancia);
                GeoPoint geo = new GeoPoint(item.getPoint().getLatitude(), item.getPoint().getLongitude());

                mark.setPosition(geo);
                mark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                mark.showInfoWindow();
                map.getOverlayManager().add(mark);
                Toast.makeText(getActivity(), "onItemSingleTapUp", Toast.LENGTH_SHORT).show();
                map.invalidate();
                return true;
            }

            @Override
            public boolean onItemLongPress(final int index, final OverlayItem item) {
                Toast.makeText(getActivity(), "onItemLongPress", Toast.LENGTH_SHORT).show();

                iniciarActivity(item);

                return true;
            }

        };
        ItemizedIconOverlay<OverlayItem> mOverlay = new ItemizedIconOverlay<OverlayItem>(getActivity(), marcadores, gestureListener);


        mapEventsOverlay = new MapEventsOverlay(this)/*MapEventsOverlay(getContext(), this)*/;
        map.getOverlays().add(0, mapEventsOverlay);

        map.getOverlays().add(mOverlay);
        marker = new Marker(map);

        return  view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //System.out.println("requestCode es :" + requestCode);
        //Toast.makeText(getActivity(), "requestCode es :" + requestCode, Toast.LENGTH_SHORT).show();
        if (requestCode == 0) {
            //Toast.makeText(getActivity(), "resultCode es :" + resultCode, Toast.LENGTH_SHORT).show();
            if (resultCode == RESULT_OK) {
                TITULO = data.getStringExtra("Titulo crimen");
                //Toast.makeText(getActivity(), "TITULO es :" + TITULO, Toast.LENGTH_SHORT).show();
                DESCRIPCION = data.getStringExtra("Descripcion crimen");
                //Toast.makeText(getActivity(), "DESCRIPCION es :" + DESCRIPCION, Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == -1) {
                //Toast.makeText(getActivity(), "TITULO es :" + TITULO, Toast.LENGTH_SHORT).show();

                // something went wrong :-(
            }
        }
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint geo) {
        //Toast.makeText(getActivity(), "singleTapConfirmedHelper", Toast.LENGTH_SHORT).show();
        //geo = new GeoPoint(geo.getLatitude(), geo.getLongitude());
        //agregarMarcador(geo.getLatitude(), geo.getLongitude());

        //new Handler();


        //addMarker(new GeoPoint());
        //Intent myIntent = new Intent(getContext(), MisDenuncias.class);
        //startActivityForResult(new Intent(getContext(), MisDenuncias.class),0);

        //startActivity(myIntent);

        //Bundle extras;
        //extras = myIntent.getExtras();
        if (TITULO != "") {
            Marker mark = new Marker(map);
            //if (extras != null) {
            mark.setTitle(/*extras.getString("Titulo crimen")*/TITULO);
            mark.setSnippet(/*extras.getString("Descripcion crimen")*/DESCRIPCION);
            //}
            //String distancia = "Está a " + (int) user.distanceToAsDouble(item.getPoint()) + " mts. de distancia";
            //mark.setSnippet(/*item.getSnippet()*/distancia);
            //GeoPoint geo = new GeoPoint(item.getPoint().getLatitude(), item.getPoint().getLongitude());
            //addMarker(geo);
            mark.setPosition(geo);
            mark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

            mark.showInfoWindow();
            map.getOverlayManager().add(mark);
            TITULO = "";
            DESCRIPCION = "";
        } else {
            Toast.makeText(getActivity(), "Ingrese una denuncia antes de colocarla en el mapa", Toast.LENGTH_SHORT).show();
        }
        //map.invalidate();




        //marcadores2.add(p);
        map.invalidate();
        return true;
    }



    @Override
    public boolean longPressHelper(GeoPoint p) {
        //Toast.makeText(getActivity(), "longPressHelper", Toast.LENGTH_SHORT).show();
        //Toast.makeText(getActivity(), "lat: " + p.getLatitude() + " long: " + p.getLongitude() , Toast.LENGTH_SHORT).show();

        return false;
    }


    public void iniciarActivity(final OverlayItem item){
        Bundle arg = new Bundle();
        arg.putString("etiq", item.getTitle());
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(arg);
        //FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment, "tag1");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onStart(){
        super.onStart();
        handler = new Handler();
        cercania = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                //miUbic();
            }
        },10);
        cercania.postDelayed(new Runnable() {
            @Override
            public void run() {
                //cercaniaActiva();
            }
        },10);
    }

    private void volverCampus(){
        IMapController mapController = map.getController();
        mapController.setZoom(14.0);
        GeoPoint startPoint = new GeoPoint(9.9327,-84.0796);
        mapController.setCenter(startPoint);
    }

    private void hacerDenuncia() {
        startActivityForResult(new Intent(getContext(), MisDenuncias.class),0);
    }

    private void almacenar() {
        int check = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (check == PackageManager.PERMISSION_GRANTED) {
            //Do something
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1024);
        }
    }

    private void miUbic() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
        }




        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        try {

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                int i;
                // location service disabled
            } else {
                // if GPS Enabled get lat/long using GPS Services

                if (isGPSEnabled && !isNetworkEnabled) {
                    android.location.LocationListener locationListener1 = new android.location.LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            actualizarUbic(location);

                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {
                        }

                        @Override
                        public void onProviderEnabled(String s) {
                        }

                        @Override
                        public void onProviderDisabled(String s) {
                        }
                    };
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1f,locationListener1 );

                    Log.d("GPS Enabled", "GPS Enabled");

                    if (locationManager != null) {
                        while (location == null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (location == null) {
                        android.location.LocationListener locationListener2 = new android.location.LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                actualizarUbic(location);

                            }

                            @Override
                            public void onStatusChanged(String s, int i, Bundle bundle) {
                            }

                            @Override
                            public void onProviderEnabled(String s) {
                            }

                            @Override
                            public void onProviderDisabled(String s) {
                            }
                        };
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,1f,locationListener2 );

                        Log.d("Network", "Network");

                        if (locationManager != null) {
                            while (location == null) {
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            Log.e("Error : Location",
                    "Impossible to connect to LocationManager", e);
        }

        actualizarUbic(location);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void actualizarUbic(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            agregarMarcador(lat, lon);
        }
    }

    private void agregarMarcador(double la, double lo) {
        lat=la;
        lon=lo;
        //CameraUpdate miUbic = CameraUpdateFactory.newLatLngZoom(coord, 16f);
        if (marker != null) {
            marker.remove(map);
        }
        user= new GeoPoint(lat, lon);
        marker.setTitle("Usuario");
        marker.setPosition(user);
        //marker.setIcon(getResources().getDrawable(R.drawable.ubicacion));
        map.getOverlays().add(marker);

    }

    private void agregarMarcador2(double la, double lo, String titulo, String detalles) {
        lat=la;
        lon=lo;
        //CameraUpdate miUbic = CameraUpdateFactory.newLatLngZoom(coord, 16f);
        /*if (marker != null) {
            marker.remove(map);
        }*/
        org.osmdroid.views.overlay.Marker marcador = new org.osmdroid.views.overlay.Marker(map);
        GeoPoint geo = new GeoPoint(lat, lon);
        marcador.setTitle(titulo);
        marcador.setSnippet(detalles);
        marcador.setPosition(geo);
        //marker.setIcon(getResources().getDrawable(R.drawable.ubicacion));
        map.getOverlays().add(marcador);

    }


    @Override
    public void onResume(){
        super.onResume();
         map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause(){
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }


    public void addMarker(GeoPoint point){
        org.osmdroid.views.overlay.Marker marker = new org.osmdroid.views.overlay.Marker(map);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_CENTER);
        //marker.setTitle("UCR");
        //marker.setIcon(getResources().getDrawable(R.drawable.cat));
        marker.setDraggable(true);
        IMapController mapController = map.getController();
        mapController.setCenter(point);


        map.getOverlays().clear();
       // map.getOverlays().add(new MapOverlay(this.getContext().getApplicationContext()));
        map.getOverlays().add(marker);
        map.invalidate();

    }





}

