package com.cs407.lab4milestone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle){
            }
            @Override
            public void onProviderEnabled(String s){
            }
            @Override
            public void onProviderDisabled(String s){
            }
        };

        if (Build.VERSION.SDK_INT < 23){
            startListening();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 8 ,0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null){
                    updateLocationInfo(location);
                }
            }
        }
    }

    public  void startListening(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public  void onRequestPermissionsResult(int requesCode, @NotNull String[] permissions, @NotNull int[] grantResults){
        super.onRequestPermissionsResult(requesCode,permissions,grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.i("Permission Granted","Permission Granted");
            startListening();
        }
    }

    public void updateLocationInfo(Location location){
        Log.i("LocationInfo", location.toString());

        TextView lat = (TextView) findViewById(R.id.Lattitude);
        lat.setText("Lattitude: " + location.getLatitude());
        TextView longT = (TextView) findViewById(R.id.Longitude);
        longT.setText("Longitude: " + location.getLongitude());
        TextView alt = (TextView) findViewById(R.id.Altitude);
        alt.setText("Altitude: " + location.getAltitude());
        TextView acc = (TextView) findViewById(R.id.Accuracy);
        acc.setText("Accuracy: " + location.getAccuracy());

        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            String address = "Count not find address";
            List<Address> listAddresses = gcd.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if (listAddresses != null && listAddresses.size() > 0){
                Log.i ("PlaceInfo",listAddresses.get(0).toString());
                address = "";
                if (listAddresses.get(0).getSubThoroughfare() != null){
                    address += listAddresses.get(0).getSubThoroughfare() + " ";
                }
                if (listAddresses.get(0).getThoroughfare() != null){
                    address += listAddresses.get(0).getThoroughfare() + " ";
                }
                if (listAddresses.get(0).getLocality() != null){
                    address += listAddresses.get(0).getLocality() + " ";
                }
                if (listAddresses.get(0).getPostalCode() != null){
                    address += listAddresses.get(0).getPostalCode() + " ";
                }
                if (listAddresses.get(0).getCountryName() != null){
                    address += listAddresses.get(0).getCountryName() + " ";
                }
            }

            ((TextView)findViewById(R.id.addrTxt)).setText(address);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

}