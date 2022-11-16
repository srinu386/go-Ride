package com.example.vehicalpooling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewStudent extends AppCompatActivity {

    ListView listView;
    ArrayList list=new ArrayList();
    ArrayList idd=new ArrayList();
    ArrayAdapter adpt;


    private static final String URL="http://wizzie.tech/vehiclepooling/getstudents.php";
    private static final String URL1="http://wizzie.tech/vehiclepooling/updatestudent.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);

        listView=findViewById(R.id.list);

        getData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewStudent.this);
                builder.setMessage("Do you want to Accept Student ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                update(idd.get(position).toString().trim());

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }

                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Alert Dialog 4Us");
                alert.show();
            }
        });

    }

    private void update(String trim) {
        final ProgressDialog pdd = new ProgressDialog(ViewStudent.this);
        pdd.setMessage("Please Wait, While We Add");
        pdd.setCancelable(false);
        pdd.show();
        // Toast.makeText(Signup.this, ""+etmobile.getText().toString().trim(), Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(ViewStudent.this, ""+response, Toast.LENGTH_SHORT).show();
                        pdd.dismiss();
                        System.out.println(response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            if (object.getString("result").equalsIgnoreCase("you are registered successfully")) {
                                Toast.makeText(ViewStudent.this, "Accepted Success", Toast.LENGTH_SHORT).show();
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

                params.put("id", trim.trim());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    private void getData() {
        final ProgressDialog pPd = new ProgressDialog(ViewStudent.this);
        pPd.setMessage("Loading");
        pPd.setCancelable(false);
        pPd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(ViewStudent.this, ""+response, Toast.LENGTH_SHORT).show();
                        pPd.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() == 0) {
                                Toast.makeText(ViewStudent.this, "No Data", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        list.add("Roll Number:      " + jsonObject.getString("roll") + "\n\n" + "Name:      " + jsonObject.getString("name") + "\n\n" + "Email:     " + jsonObject.getString("email") + "\n\n" + "Mobile:       " + jsonObject.getString("mobile") +"\n\n"+ "Status:        " + jsonObject.getString("status")  + "\n\n" + "=========================");

                                        idd.add(jsonObject.getString("id"));
                                    }
                                    adpt=new ArrayAdapter<String>(getApplicationContext(),R.layout.ls,list);
                                    listView.setAdapter(adpt);

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
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}