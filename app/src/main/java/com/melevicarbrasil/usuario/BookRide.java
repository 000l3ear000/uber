package com.melevicarbrasil.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class BookRide extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;

    double Lat[]={-2.7992448859843875,-2.7992448859843875,-2.7859783607327406,-2.790414840611112,-2.7898040218712934,-2.786824936685647,-2.7844352333968607,-2.785753321455524};
    double Lng[]={-40.51990415190018,-40.51990415190018,-40.50223375894637,-40.517908588466334,-40.50518418891157,-40.50687934495541,-40.50079609495983,-40.49482013346353};


    Integer coord;
    String loc;
    Intent ex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ride);
        ex=getIntent();
        Bundle extras = ex.getExtras();
        coord= extras.getInt("position");

        loc=extras.getString("name");
        SupportMapFragment mm= (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mm.getMapAsync(this);

        View rd= findViewById(R.id.floatingActionButton);
        rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    @Override
    public void onMapReady(GoogleMap gm){
        new maps(loc,coord,gm);
    }

    class maps{
        String name;
        Integer ID;

        maps(String dest, Integer position, GoogleMap gm){
            this.name=dest;
            this.ID=position;
            map=gm;
            LatLng beach= new LatLng(Lat[this.ID],Lng[position]);
            LatLng hotel = new LatLng(-2.796597852939866, -40.51475501964744);
            map.addMarker(new MarkerOptions().position(hotel).title("Hotel Jari"));
            map.addMarker(new MarkerOptions().position(beach).title(name));


            map.moveCamera(CameraUpdateFactory.newLatLngZoom(hotel,14.8f));
        }
    }
}


