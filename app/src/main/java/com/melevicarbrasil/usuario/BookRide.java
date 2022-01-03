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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class BookRide extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;

    double Lat[]={-2.7992448859843875,-2.7992448859843875,-2.7859783607327406,-2.790414840611112,-2.7898040218712934,-2.786824936685647,-2.7844352333968607,-2.785753321455524};
    double Lng[]={-40.51990415190018,-40.51990415190018,-40.50223375894637,-40.517908588466334,-40.50518418891157,-40.50687934495541,-40.50079609495983,-40.49482013346353};
    double zoom[]={15.2,15.2,14.8,13.5,15,15,14.8,14.6};

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
        View start= findViewById(R.id.button4);

        loc=extras.getString("name");
        SupportMapFragment mm= (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mm.getMapAsync(this);

        View rd= findViewById(R.id.backImgView);
        rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(),demo.class);
                startActivity(intent);
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
            Polyline polyline1 = gm.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            hotel,
                            beach
                    )

            );


            Double L1=(Lat[this.ID]+-2.796597852939866)/2;
            Double L2=(Lng[this.ID]+-40.51475501964744)/2;
            LatLng l3=new LatLng(L1,L2);

//            map.addMarker(new MarkerOptions().position(l3).title("hola"));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(l3,(float)zoom[position]));
        }



    }
}


