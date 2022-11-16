package com.example.vehicalpooling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
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

import static com.example.vehicalpooling.MainActivity.d;

public class History extends AppCompatActivity {

    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    private static final String URL="http://wizzie.tech/vehiclepooling/history.php";

    ArrayList latlan=new ArrayList();
    ArrayList mobile=new ArrayList();
    ArrayList source=new ArrayList();
    ArrayList destination=new ArrayList();
    ArrayList dist=new ArrayList();
    ArrayList cost=new ArrayList();
    ArrayList id=new ArrayList();
    ArrayList veh=new ArrayList();

    ArrayList name=new ArrayList();
    ArrayList mob=new ArrayList();
    ArrayList stat=new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView=findViewById(R.id.rec);

        gethistory(d);

    }

    private void gethistory(String m) {
        final ProgressDialog pPd = new ProgressDialog(History.this);
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
                                Toast.makeText(History.this, "No Data Found", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        latlan.add(jsonObject.getString("latlan").trim());
                                        mobile.add(jsonObject.getString("rider_mobile").trim());
                                        source.add(jsonObject.getString("source").trim());
                                        destination.add(jsonObject.getString("destination").trim());
                                        dist.add(jsonObject.getString("distance").trim());
                                        cost.add(jsonObject.getString("cost").trim());
                                        id.add(jsonObject.getString("rider_id").trim());
                                        // veh.add(jsonObject.getString("veh_type").trim());
                                        name.add(jsonObject.getString("user").trim());
                                        mob.add(jsonObject.getString("u_mobile").trim());
                                        stat.add(jsonObject.getString("status").trim());

                                    }

                                    historyAdapter = new HistoryAdapter(History.this,latlan,mobile,source,destination,dist,cost,id,name,mob,stat);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(History.this,LinearLayoutManager.VERTICAL,false));
                                    recyclerView.setAdapter(historyAdapter);
                                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                                            DividerItemDecoration.HORIZONTAL));


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
                params.put("a", d);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }
}