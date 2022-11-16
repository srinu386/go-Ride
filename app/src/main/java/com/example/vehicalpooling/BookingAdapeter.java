package com.example.vehicalpooling;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingAdapeter extends RecyclerView.Adapter<BookingAdapeter.ViewHolder> {

    Context context;

    private static final String URL="http://wizzie.tech/vehiclepooling/updatebooking.php";


    ArrayList latlan=new ArrayList();
    ArrayList mobile=new ArrayList();
    ArrayList source=new ArrayList();
    ArrayList destination=new ArrayList();
    ArrayList dist=new ArrayList();
    ArrayList cost=new ArrayList();
    ArrayList idd=new ArrayList();
    ArrayList veh=new ArrayList();
    ArrayList ll = new ArrayList();

    ArrayList name=new ArrayList();
    ArrayList mob=new ArrayList();
    ArrayList stat=new ArrayList();

    public BookingAdapeter(Mybookings mybookings, ArrayList latlan, ArrayList mobile, ArrayList source, ArrayList destination, ArrayList dist, ArrayList cost, ArrayList id, ArrayList name, ArrayList mob, ArrayList stat, ArrayList ll) {

        this.context=mybookings;
        this.latlan=latlan;
        this.ll=ll;
        this.mobile=mobile;
        this.source=source;
        this.destination=destination;
        this.dist=dist;
        this.idd=id;
        this.cost=cost;
        this.veh=veh;
        this.name=name;
        this.mob=mob;
        this.stat=stat;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.items_booking,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(idd.get(position).toString().trim());
        holder.mob.setText(mobile.get(position).toString().trim());
        holder.sr.setText(source.get(position).toString().trim());
        holder.ds.setText(destination.get(position).toString().trim());
        holder.dist.setText(dist.get(position).toString().trim());
        holder.cost.setText(cost.get(position).toString().trim());
      //  holder.veh.setText(veh.get(position).toString().trim());

        holder.un.setText(name.get(position).toString().trim());
        holder.um.setText(mob.get(position).toString().trim());
        holder.st.setText(stat.get(position).toString().trim());


        holder.um.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pn=mob.get(position).toString().trim();
               // Toast.makeText(context, ""+pn, Toast.LENGTH_SHORT).show();

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" +pn));
                context.startActivity(callIntent);

            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st="Accept";
                statusup(st,idd.get(position).toString().trim());
            }
        });

        holder.cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st="Reject";
                statusup(st,idd.get(position).toString().trim());
            }
        });

        holder.pd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"location access"+latlan+","+ll,Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(context,ShowMaps.class);
                intent.putExtra("sl",latlan.get(position).toString().trim());
                intent.putExtra("dl",ll.get(position).toString().trim());
                context.startActivity(intent);
            }
        });


    }

    private void statusup(String st, String trim) {
        final ProgressDialog pdd = new ProgressDialog(context);
        pdd.setMessage("Please Wait, While We Update");
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
                                context.startActivity(new Intent(context,MainActivity.class));

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

                params.put("st", st.trim());
                params.put("id", trim.trim());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }


    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button accept,cancle,pd;

        TextView name,mob,sr,ds,dist,cost,veh,un,um,st;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            mob=itemView.findViewById(R.id.mob);
            sr=itemView.findViewById(R.id.sorce);
            ds=itemView.findViewById(R.id.dest);
            dist=itemView.findViewById(R.id.dist);
            cost=itemView.findViewById(R.id.cost);
            veh=itemView.findViewById(R.id.vh);
            pd = itemView.findViewById(R.id.sdt);

            un=itemView.findViewById(R.id.us);
            um=itemView.findViewById(R.id.um);
            st=itemView.findViewById(R.id.st);

            accept=itemView.findViewById(R.id.acc);
            cancle=itemView.findViewById(R.id.can);

            linearLayout=itemView.findViewById(R.id.layout);
        }
    }
}
