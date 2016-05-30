package com.android.wifimap.wifimap.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.wifimap.wifimap.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToMap(View view){
        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
    }
}
