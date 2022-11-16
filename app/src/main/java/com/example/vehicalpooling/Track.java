package com.example.vehicalpooling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Track extends AppCompatActivity implements OnMapReadyCallback {


    String llc;
    String lc,xc;

    private static final String URL="http://wizzie.tech/vehiclepooling/getlatlan.php";

    GoogleMap ggmap,gmap;
    MapRipple mmapRipple,mprepl;
    SupportMapFragment spmff;
    FusedLocationProviderClient clientt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        spmff = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag);
        spmff.getMapAsync((OnMapReadyCallback) Track.this);
        clientt = LocationServices.getFusedLocationProviderClient(this);

        Intent intent=getIntent();
        llc=intent.getStringExtra("loc");

        getloc(llc);
        showLoc();
    }

    private void getloc(String llc) {

        final ProgressDialog pPd = new ProgressDialog(Track.this);
        pPd.setMessage("Loading");
        pPd.setCancelable(false);
        pPd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(Pooling.this, ""+response, Toast.LENGTH_SHORT).show();
                        pPd.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() == 0) {
                                Toast.makeText(Track.this, "No Data Found", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        xc=jsonObject.getString("latlan").trim();
                                       // Toast.makeText(Track.this, ""+xc, Toast.LENGTH_SHORT).show();
                                        classloc(xc);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("a", llc);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


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
                           // classloc(xc);

                        }
                    });
                }
            }
        });



    }

    private void classloc(String xc) {

        String[] fullbusloc = xc.split(",");
        double lat = Double.parseDouble(fullbusloc[0]);
        double lan = Double.parseDouble(fullbusloc[1]);
        LatLng latlng = new LatLng(lat, lan);
        MarkerOptions options1 = new MarkerOptions().position(latlng).title("Your Rider Location is");
        options1.icon(bitmapDescriptorFromVector(Track.this, R.drawable.ic_baseline_location_on_24));
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
        String[] bloc = lc.split(",");
        double lat = Double.parseDouble(bloc[0]);
        double lan = Double.parseDouble(bloc[1]);
        LatLng latlng = new LatLng(lat, lan);
        MarkerOptions options = new MarkerOptions().position(latlng).title("Your current location is");
        options.icon(bitmapDescriptorFromVector1(Track.this, R.drawable.ic_baseline_location));
        gmap.addMarker(options);
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,16));
        mprepl = new MapRipple(gmap, latlng, getApplicationContext());
        mprepl.withNumberOfRipples(2);
        mprepl.withDurationBetweenTwoRipples(300);
        mprepl.withDistance(20);
        mprepl.withRippleDuration(1000);
        mprepl.withTransparency(0.6f);
        mprepl.withFillColor(Color.GREEN);
        mprepl.startRippleMapAnimation();

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


    }
}