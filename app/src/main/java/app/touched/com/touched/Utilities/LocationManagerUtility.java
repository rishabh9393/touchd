package app.touched.com.touched.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import app.touched.com.touched.Models.User_Details;

import static android.location.LocationManager.NETWORK_PROVIDER;

/**
 * Created by Anshul on 3/13/2018.
 */

public class LocationManagerUtility implements LocationListener {
    private Activity con;
    private LocationManager locationManager;
    private String Latitude, Longitude;
    PermissionUtility contextPassing;

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }



    public LocationManagerUtility(Activity context) {
        con = context;
        contextPassing= new PermissionUtility(con);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startUsingGPS() {
        final boolean check = locationManager.isProviderEnabled(NETWORK_PROVIDER);
        if (check) {
            PermissionUtility.with(con).setCallback(new PermissionUtility.PermissionGrantedListener() {
                @Override
                public void onPermissionResult(boolean isGranted, int requestCode) {
                    if (isGranted) {

                        locationManager.requestLocationUpdates(NETWORK_PROVIDER, 1, 5, (LocationListener) LocationManagerUtility.this);

                        locationManager.getLastKnownLocation(NETWORK_PROVIDER);

                    }

                }
            }).validate(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            //show the dialog for the setting
            Intent callSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            con.startActivity(callSetting);
        }
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Latitude = String.valueOf(location.getLatitude());
        Longitude = String.valueOf(location.getLongitude());

        startUsingGPS();
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
}
