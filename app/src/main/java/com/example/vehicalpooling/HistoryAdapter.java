package com.example.vehicalpooling;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Context context;

    ArrayList latlan=new ArrayList();
    ArrayList mobile=new ArrayList();
    ArrayList source=new ArrayList();
    ArrayList destination=new ArrayList();
    ArrayList dist=new ArrayList();
    ArrayList cost=new ArrayList();
    ArrayList idd=new ArrayList();
    ArrayList veh=new ArrayList();

    ArrayList name=new ArrayList();
    ArrayList mob=new ArrayList();
    ArrayList stat=new ArrayList();


    public HistoryAdapter(History history, ArrayList latlan, ArrayList mobile, ArrayList source, ArrayList destination, ArrayList dist, ArrayList cost, ArrayList id, ArrayList name, ArrayList mob, ArrayList stat) {


        this.context=history;
        this.latlan=latlan;
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
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.items_history,parent,false));
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

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Choose Something From Here ");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "View Location", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i=new Intent(context,Track.class);
                                i.putExtra("loc",idd.get(position).toString().trim());
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
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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

            un=itemView.findViewById(R.id.us);
            um=itemView.findViewById(R.id.um);
            st=itemView.findViewById(R.id.st);

            linearLayout=itemView.findViewById(R.id.layout);
        }
    }
}
