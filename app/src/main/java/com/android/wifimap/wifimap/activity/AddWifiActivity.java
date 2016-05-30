package com.android.wifimap.wifimap.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.wifimap.wifimap.NetPoint;
import com.android.wifimap.wifimap.R;
import com.android.wifimap.wifimap.service.IAsyncResponse;
import com.android.wifimap.wifimap.service.URLBuilder;
import com.android.wifimap.wifimap.service.WSCaller;
import com.google.android.gms.maps.model.LatLng;

public class AddWifiActivity extends AppCompatActivity implements IAsyncResponse {
    private WSCaller wsCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi);

        //Designo a este como el creador de la llamada a los servicios para poder obtener una respuesta asincrona.
        createCaller();

        setValues(getIntent().getExtras());
    }

    private void setValues(Bundle bundle) {
       LatLng point = (LatLng) bundle.get("selectedPoint");
        if(point != null) {
            TextView txtLatitude = (TextView) findViewById(R.id.txtLatitude);
            txtLatitude.setText(String.valueOf(point.latitude));

            TextView txtLongitude = (TextView) findViewById(R.id.txtLongitude);
            txtLongitude.setText(String.valueOf(point.longitude));
        }
    }

    private void createCaller() {
        wsCaller = new WSCaller();
        wsCaller.delegate = this;
    }

    public void clkCancel(View view){
        finish();
    }

    public void clkAccept(View view){
        NetPoint netPoint = getNetPoint();
        wsCaller.execute(URLBuilder.getAddWifiURL(netPoint),"GET");

        finish();
    }

    private NetPoint getNetPoint() {
        TextView txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        TextView txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        TextView txtNamePlace = (TextView) findViewById(R.id.txtNamePlace);
        TextView txtNameWiFi = (TextView) findViewById(R.id.txtNameWiFi);
        TextView txtPass = (TextView) findViewById(R.id.txtPass);

        double lat = Double.parseDouble(txtLatitude.getText().toString());
        double lon = Double.parseDouble(txtLongitude.getText().toString());
        String place = txtNamePlace.getText().toString();
        String name = txtNameWiFi.getText().toString();
        String pwd = txtPass.getText().toString();

        return new NetPoint(lat, lon, place, name, pwd);
    }

    @Override
    public void processFinish(String output) {
        createCaller();
    }
}
