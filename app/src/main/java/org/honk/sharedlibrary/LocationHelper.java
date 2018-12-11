package org.honk.sharedlibrary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationHelper {

    // This field cannot be static in order to avoid a memory leak.
    private FusedLocationProviderClient fusedLocationClient;

    public static void checkRequirements(
            Context context, OnSuccessListener<LocationSettingsResponse> successListener, OnFailureListener failureListener) {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build())
                .addOnFailureListener(failureListener);

        if (successListener != null) {
            task.addOnSuccessListener(successListener);
        }
    }

    public static Boolean checkLocationSystemFeature(Context context, String feature) {
        // Check whether the device supports accessing coarse location.
        return context.getPackageManager().hasSystemFeature(feature);
    }

    public static Boolean checkLocationPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation(Context context, OnSuccessListener<Location> successListener) {
        if (this.fusedLocationClient == null) {
            this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        }

        this.fusedLocationClient.getLastLocation().addOnSuccessListener(successListener);
    }
}
