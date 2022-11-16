package com.example.vehicalpooling;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText etemail,etpaswd;
    Button btnlgin;
    private int YourRequestCode =1;
    static String userid,email_id,id,veh_type,un,em;
    TextView tvregister;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Phone = "phoneKey";
    public static final String Id = "id";
    public static final String Vt = "vt";
    public static final String Nm = "nm";
    public static final String Em = "em";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        if (sharedpreferences.contains(Phone)) {
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            i.putExtra("mob",sharedpreferences.getString(Phone, ""));
            i.putExtra("id",sharedpreferences.getString(Id, ""));
            i.putExtra("vt",sharedpreferences.getString(Vt, ""));
            i.putExtra("nm",sharedpreferences.getString(Nm, ""));
            i.putExtra("em",sharedpreferences.getString(Em, ""));
            startActivity(i);
            finish();
        }

        etemail = (EditText)findViewById(R.id.etemail);
        etpaswd = (EditText)findViewById(R.id.etpaswd);
        btnlgin = (Button)findViewById(R.id.btnlgin);
        tvregister = (TextView)findViewById(R.id.tvregister);

        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.appcolor));
        }
        requestpermission();

        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Signup.class);
                startActivity(intent);
                //finish();
            }
        });

        btnlgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etemail.getText().toString().isEmpty() || etpaswd.getText().toString().isEmpty()){
                    if(etemail.getText().toString().isEmpty()){
                        etemail.setError("Fill this");
                        etemail.requestFocus();
                    }else if(etpaswd.getText().toString().isEmpty()){
                        etpaswd.setError("Fill this");
                        etpaswd.requestFocus();
                    }
                }else{
                    if(etemail.getText().toString().equalsIgnoreCase("Admin") && etpaswd.getText().toString().equalsIgnoreCase("Admin")){
                        Intent intent = new Intent(getApplicationContext(),AdminHome.class);
                        startActivity(intent);
                        finish();
                    }else if(!etemail.getText().toString().isEmpty() && !etpaswd.getText().toString().isEmpty()){
                        final ProgressDialog pPd = new ProgressDialog(Login.this);
                        pPd.setMessage("Loading");
                        pPd.setCancelable(false);
                        pPd.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://wizzie.tech/vehiclepooling/login.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();
                                        pPd.dismiss();
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            if (jsonArray.length() == 0) {
                                                Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                                            } else {
                                                try {
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                                        email_id = jsonObject.getString("mobile");
                                                        id = jsonObject.getString("id");
                                                        veh_type = jsonObject.getString("veh_type");

                                                        un = jsonObject.getString("name");
                                                        em = jsonObject.getString("email");

                                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                                        editor.putString(Phone, email_id.trim());
                                                        editor.putString(Id, id.trim());
                                                        editor.putString(Vt, veh_type.trim());
                                                        editor.putString(Nm, un.trim());
                                                        editor.putString(Em, em.trim());

                                                        editor.commit();

                                                        Intent in=new Intent(getApplicationContext(),MainActivity.class);
                                                        in.putExtra("mob",email_id);
                                                        in.putExtra("id",id);
                                                        in.putExtra("vt",veh_type);
                                                        in.putExtra("nm",un);
                                                        in.putExtra("em",em);
                                                        startActivity(in);
                                                        finish();

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
                                params.put("a", etemail.getText().toString().trim());
                                params.put("b", etpaswd.getText().toString().trim());
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);

                    }
                }
            }
        });
    }


    private void requestpermission(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Login.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    YourRequestCode);
        }
    }

}