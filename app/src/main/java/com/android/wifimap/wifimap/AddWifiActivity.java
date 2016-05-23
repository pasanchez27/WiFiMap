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

    private void clkCancel(View view){
        finish();
    }
}
