package com.android.wifimap.wifimap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class AddWifiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi);

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

    private void setTxtViews() {
    }

    public void clkCancel(View view){
        finish();
    }

    public void clkAccept(View view){
        NetPoint netPoint = getNetPoint();
        String URI = "http://localhost:8007/WifiMap/AddWifi?lat=%s&lon=%s&place=%s&name=%s&pwd=%s";

        String.format(URI, netPoint.getLat(), netPoint.getLon(), netPoint.getPlace(), netPoint.getNetName(), netPoint.getNetPwd());


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

        NetPoint netPoint = new NetPoint(lat, lon, place, name, pwd);

        return netPoint;
    }

}
