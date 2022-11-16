package com.example.vehicalpooling;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Phone = "phoneKey";
    public static final String Id = "id";
    public static final String Vt = "vt";
    public static final String Nm = "nm";
    public static final String Em = "em";
    static  String m,d,destination,vt,name,email;
    EditText des;

    TextView mob,nam,eml;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mob=findViewById(R.id.mob);
        nam=findViewById(R.id.nm);
        eml=findViewById(R.id.eml);

        des=findViewById(R.id.desti);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Phone)) {

            m=sharedpreferences.getString(Phone, "");
            d=sharedpreferences.getString(Id, "");
            vt=sharedpreferences.getString(Vt, "");

            name=sharedpreferences.getString(Nm, "");
            email=sharedpreferences.getString(Em, "");
            //Toast.makeText(this, ""+vt, Toast.LENGTH_SHORT).show();

        }else {
            Intent i = getIntent();
            m = i.getStringExtra("mob");
            d = i.getStringExtra("id");
            vt = i.getStringExtra("vt");

            name = i.getStringExtra("nm");
            email = i.getStringExtra("em");
        }

        if (sharedpreferences.contains("d")) {
            des.setText(sharedpreferences.getString("d",""));
            destination=sharedpreferences.getString("d","");
        }
        else {
            des.setText("SV University");
            destination="SV University";

        }

        mob.setText(m);
        nam.setText(name);
        eml.setText(email);

    }

    public void logout(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Confirm Logout..!!!");
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_power_settings_new_24);
        alertDialogBuilder.setMessage("Are you sure,You want to Logout");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(getApplicationContext(), "Logout Success!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void sharetrip(View view) {
        startActivity(new Intent(getApplicationContext(),ShareTrip.class));
    }

    public void poolbike(View view) {

        startActivity(new Intent(getApplicationContext(),Pooling.class));

    }

    public void gd(View view) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("d", des.getText().toString().trim());
        editor.commit();

    }

    public void mybookings(View view) {
        startActivity(new Intent(getApplicationContext(),Mybookings.class));

    }

    public void history(View view) {
        startActivity(new Intent(getApplicationContext(),History.class));
    }
}