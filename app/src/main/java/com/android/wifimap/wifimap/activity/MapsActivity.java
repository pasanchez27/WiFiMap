package com.android.wifimap.wifimap.activity;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.wifimap.wifimap.NetPoint;
import com.android.wifimap.wifimap.R;
import com.android.wifimap.wifimap.service.IAsyncResponse;
import com.android.wifimap.wifimap.service.URLBuilder;
import com.android.wifimap.wifimap.service.WSCaller;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMapClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMyLocationChangeListener, IAsyncResponse {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private WSCaller wsCaller;
    private static Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Designo a este como el creador de la llamada a los servicios para poder obtener una respuesta asincrona.
        createCaller();

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void createCaller() {
        wsCaller = new WSCaller();
        wsCaller.delegate = this;
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setOnMapClickListener(this);
    }

    /**
     * Toma la posición seleccionada en el mapa y llama al Intent de Agregar Red para que el usuario
     * pueda registrar una nueva red en ese punto.
     * @param point Posición marcada por el usuario en el mapa.
     */
    @Override
    public void onMapClick(LatLng point) {
        Intent intentAddWifi = new Intent(this, AddWifiActivity.class);
        intentAddWifi.putExtra("selectedPoint", point);
        startActivity(intentAddWifi);
    }

    @Override
    public void onMyLocationChange(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(loc));
        if(mMap != null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //Busca la última posición conocida del móvil.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            //Hace foco en el mapa en la posición del móvil
            LatLng position = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

            //Busca los puntos registrados cercanos a la posición.
            wsCaller.execute(URLBuilder.getWifiListURL(latitude, longitude), "GET");
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        finish();
        Log.e("onConnectionSuspended", "FALLO");
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * Recibe asincronamente la respuesta del WS con los diferentes puntos cercanos a la posición y los
     * dibuja en el mapa.
     * @param result Un objeto JSon que contiene todos los NetPoint cercanos.
     */
    @Override
    public void processFinish(String result) {
        if(result != null) {
            List<NetPoint> net = gson.fromJson(result, new TypeToken<List<NetPoint>>() {}.getType());

            for (NetPoint point : net) {
                MarkerOptions mOption = new MarkerOptions()
                        .position(new LatLng(point.getLat(), point.getLon()))
                        .title(point.getPlace())
                        .snippet("Clave WiFi: " + point.getNetPwd())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                mMap.addMarker(mOption);
            }
        }
        createCaller();
    }
}