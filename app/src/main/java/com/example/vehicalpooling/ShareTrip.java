package com.example.vehicalpooling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShareTrip extends AppCompatActivity {

    private static final String URL="http://wizzie.tech/vehiclepooling/trackupdate.php";

    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_trip);

    }

    public void start(View view) {

        Intent alarmIntent = new Intent(ShareTrip.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(ShareTrip.this, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 60*1000;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(getApplicationContext(), "Tracking Started", Toast.LENGTH_SHORT).show();
    }

    public void stop(View view) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getApplicationContext(),"You Stopped Location sharing",Toast.LENGTH_LONG).show();
        updatetrip(MainActivity.d);

    }

    private void updatetrip(String d) {
        final ProgressDialog pdd = new ProgressDialog(ShareTrip.this);
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
                                Intent i = new Intent(getApplicationContext(),Login.class);
                                startActivity(i);
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

                params.put("nm", d);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }
}