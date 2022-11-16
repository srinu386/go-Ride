package com.example.vehicalpooling;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    Context context;

    ArrayList latlan=new ArrayList();
    ArrayList mobile=new ArrayList();
    ArrayList source=new ArrayList();
    ArrayList destination=new ArrayList();
    ArrayList dist=new ArrayList();
    ArrayList cost=new ArrayList();
    ArrayList idd=new ArrayList();
    ArrayList veh=new ArrayList();

    public DataAdapter(Pooling pooling, ArrayList name, ArrayList mobile, ArrayList source, ArrayList destination, ArrayList dist, ArrayList cost, ArrayList id, ArrayList veh) {

        this.context=pooling;
        this.latlan=name;
        this.mobile=mobile;
        this.source=source;
        this.destination=destination;
        this.dist=dist;
        this.idd=id;
        this.cost=cost;
        this.veh=veh;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(latlan.get(position).toString().trim());
        holder.mob.setText(mobile.get(position).toString().trim());
        holder.sr.setText(source.get(position).toString().trim());
        holder.ds.setText(destination.get(position).toString().trim());
        holder.dist.setText(dist.get(position).toString().trim());
        holder.cost.setText(cost.get(position).toString().trim());
        holder.veh.setText(veh.get(position).toString().trim());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Choose Something From Here ");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "View Location", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i=new Intent(context,Maps.class);
                                i.putExtra("loc",latlan.get(position).toString().trim());
                                context.startActivity(i);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Book Vehicle", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent i=new Intent(context,Book.class);
                                i.putExtra("nm",latlan.get(position).toString().trim());
                                i.putExtra("mb",mobile.get(position).toString().trim());
                                i.putExtra("sr",source.get(position).toString().trim());
                                i.putExtra("ds",destination.get(position).toString().trim());
                                i.putExtra("di",dist.get(position).toString().trim());
                                i.putExtra("co",cost.get(position).toString().trim());
                                i.putExtra("id",idd.get(position).toString().trim());
                                i.putExtra("vh",veh.get(position).toString().trim());
                                context.startActivity(i);

                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();



            }
        });


    }



    @Override
    public int getItemCount() {
        return source.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,mob,sr,ds,dist,cost,veh;
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

            linearLayout=itemView.findViewById(R.id.layout);

        }
    }
}
