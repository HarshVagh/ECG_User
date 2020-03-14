package com.example.ecg_user;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private Button btn;
    private TextView tv;

    private LocationManager locationManager;
    private String latitude,longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Permission
        ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        btn = (Button) findViewById(R.id.btn);
        tv = (TextView) findViewById(R.id.txtLoaction);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    onGPS();
                } else {
                    getLocation();
                }

            }
        });

    }

    private void getLocation() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Location");

        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(MainActivity.this, Manifest. permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location LocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGPS != null) {
                double lat = LocationGPS.getLatitude();
                double lon = LocationGPS.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(lon);

                myRef.child("latitude").setValue(latitude);
                myRef.child("longitude").setValue(longitude);

                String str = "LOCATION : 1"+"\n"+"Latitude : "+latitude+"\n"+"Longitude : "+longitude;
                tv.setText(str);



            } else if (LocationNetwork != null) {
                double lat = LocationNetwork.getLatitude();
                double lon = LocationNetwork.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(lon);

                myRef.child("latitude").setValue(latitude);
                myRef.child("longitude").setValue(longitude);

                String str = "LOCATION : 2"+"\n"+"Latitude : "+latitude+"\n"+"Longitude : "+longitude;
                tv.setText(str);

            } else if (LocationPassive != null) {
                double lat = LocationPassive.getLatitude();
                double lon = LocationPassive.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(lon);

                myRef.child("latitude").setValue(latitude);
                myRef.child("longitude").setValue(longitude);

                String str = "LOCATION : 3"+"\n"+"Latitude : "+latitude+"\n"+"Longitude : "+longitude;
                tv.setText(str);
            }
        }
    }

    private void onGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


}
