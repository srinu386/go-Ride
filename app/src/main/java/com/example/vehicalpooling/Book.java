package com.example.vehicalpooling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Book extends AppCompatActivity {

    EditText name;
    TextView mob;
    String latln,r_mobile,sorce,desti,dist,cost,id,veh;
    TextView latTextView, lonTextView;


    FusedLocationProviderClient
            mFusedLocationClient;
    String currentLocation;
    Button location;
    private static final int PERMISSION_ID = 44;

    private static final String URL="http://wizzie.tech/vehiclepooling/booking.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        name=findViewById(R.id.nm);
        mob=findViewById(R.id.mb);
        mob.setText(MainActivity.m);

        latln=getIntent().getStringExtra("nm");
        r_mobile=getIntent().getStringExtra("mb");
        sorce=getIntent().getStringExtra("sr");
        desti=getIntent().getStringExtra("ds");
        dist=getIntent().getStringExtra("di");
        cost=getIntent().getStringExtra("co");
        id=getIntent().getStringExtra("id");
        veh=getIntent().getStringExtra("vh");

        location=findViewById(R.id.loc);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latTextView = findViewById(R.id.latTextView);
                lonTextView = findViewById(R.id.lonTextView);

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                getLastLocation();
            }
        });

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    Toast.makeText(getApplicationContext(), ""+location.getLatitude()+"\n"+location.getLongitude(), Toast.LENGTH_SHORT).show();
                                    latTextView.setText(""+location.getLatitude());
                                    lonTextView.setText(""+location.getLongitude());
                                    setlocation(location);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                this.startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void setlocation(Location location) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> Laddress=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            Address locationAddress=Laddress.get(0);
            if(locationAddress!=null)
            {

                String address = locationAddress.getAddressLine(0);
                String address1 = locationAddress.getAddressLine(1);
                String city = locationAddress.getLocality();
                String state = locationAddress.getAdminArea();
                String country = locationAddress.getCountryName();
                String postalCode = locationAddress.getPostalCode();


                if(!TextUtils.isEmpty(address))
                {
                    currentLocation=address;

                    if (!TextUtils.isEmpty(address1))
                        currentLocation+="\n"+address1;

                    if (!TextUtils.isEmpty(city))
                    {
                        currentLocation+="\n"+city;

                        if (!TextUtils.isEmpty(postalCode))
                            currentLocation+=" - "+postalCode;
                    }
                    else
                    {
                        if (!TextUtils.isEmpty(postalCode))
                            currentLocation+="\n"+postalCode;
                    }

                    if (!TextUtils.isEmpty(state))
                        currentLocation+="\n"+state;

                    if (!TextUtils.isEmpty(country))
                        currentLocation+="\n"+country;
                    //  tvaddress.setText(currentLocation);

                    System.out.println(currentLocation);

                }

            }
            else
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
        }
    };
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                (Activity) getApplicationContext(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }


    public void pool(View view) {
        if (name.getText().toString().trim().isEmpty()) {
           name.setError("Enter Name");
        }
       else if (mob.getText().toString().trim().isEmpty()) {
            mob.setError("Enter Mobile");
        }
       else {
            final ProgressDialog pdd = new ProgressDialog(Book.this);
            pdd.setMessage("Please Wait, While We Add");
            pdd.setCancelable(false);
            pdd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Toast.makeText(Signup.this, ""+response, Toast.LENGTH_SHORT).show();
                            pdd.dismiss();
                            // System.out.println(response);
                            JSONObject object = null;
                            try {
                                object = new JSONObject(response);
                                if (object.getString("result").equalsIgnoreCase("you are registered successfully")) {
                                    Toast.makeText(Book.this, "Booked Success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override

                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("nm", name.getText().toString().trim());
                    params.put("mb", mob.getText().toString().trim());
                    params.put("ln", latln.trim());
                    params.put("rm",r_mobile.trim());
                    params.put("sr", sorce.trim());
                    params.put("ds", desti.trim());
                    params.put("vh",veh.trim());
                    params.put("di", dist.trim());
                    params.put("co",cost.trim());
                    params.put("id",id.trim());
                    params.put("desadd",currentLocation.trim());
                    params.put("des",latTextView.getText().toString().trim()+","+lonTextView.getText().toString().trim());

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }

    }
}