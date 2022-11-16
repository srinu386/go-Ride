package com.example.vehicalpooling;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.arsy.maps_library.MapRipple;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class ShowMaps extends AppCompatActivity implements  OnMapReadyCallback{


    String llc;
    String lc;

    String dl;
    GoogleMap ggmap,gmap;
    MapRipple mmapRipple,mprepl;
    SupportMapFragment spmff;
    FusedLocationProviderClient clientt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        spmff = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag);
        spmff.getMapAsync((OnMapReadyCallback) ShowMaps.this);
        clientt = LocationServices.getFusedLocationProviderClient(this);

        Intent intent=getIntent();
        llc=intent.getStringExtra("sl");
        dl=intent.getStringExtra("dl");
        showLoc();


    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor bitmapDescriptorFromVector1(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void showLoc() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = clientt.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location !=null){
                    spmff.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            ggmap=googleMap;
                            gmap=googleMap;
                            ggmap.clear();
                            gmap.clear();

                            lc=location.getLatitude() + "," + location.getLongitude();
                            currentloc();
                            classloc();

                        }
                    });
                }
            }
        });

    }

    private void classloc() {

        String[] fullbusloc = llc.split(",");
        double lat = Double.parseDouble(fullbusloc[0]);
        double lan = Double.parseDouble(fullbusloc[1]);
        LatLng latlng = new LatLng(lat, lan);
        MarkerOptions options1 = new MarkerOptions().position(latlng).title("User Source Location is");
        options1.icon(bitmapDescriptorFromVector(ShowMaps.this, R.drawable.ic_baseline_location_on_24));
        ggmap.addMarker(options1);
        ggmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,16));
        mmapRipple = new MapRipple(ggmap, latlng, getApplicationContext());
        mmapRipple.withNumberOfRipples(2);
        mmapRipple.withDurationBetweenTwoRipples(300);
        mmapRipple.withDistance(20);
        mmapRipple.withRippleDuration(1000);
        mmapRipple.withTransparency(0.6f);
        mmapRipple.withFillColor(Color.CYAN);
        mmapRipple.startRippleMapAnimation();

    }

    private void currentloc() {

        String[] fullbusloc = dl.split(",");
        double lat = Double.parseDouble(fullbusloc[0]);
        double lan = Double.parseDouble(fullbusloc[1]);
        LatLng latlng = new LatLng(lat, lan);
        MarkerOptions options1 = new MarkerOptions().position(latlng).title("User Destination Location is");
        options1.icon(bitmapDescriptorFromVector(ShowMaps.this, R.drawable.ic_baseline_location_on_24));
        ggmap.addMarker(options1);
        ggmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,16));
        mmapRipple = new MapRipple(ggmap, latlng, getApplicationContext());
        mmapRipple.withNumberOfRipples(2);
        mmapRipple.withDurationBetweenTwoRipples(300);
        mmapRipple.withDistance(20);
        mmapRipple.withRippleDuration(1000);
        mmapRipple.withTransparency(0.6f);
        mmapRipple.withFillColor(Color.GREEN);
        mmapRipple.startRippleMapAnimation();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


    }
}