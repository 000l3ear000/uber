package com.melevicarbrasil.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class DestinationsDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations_details);

        Bundle extras = getIntent().getExtras();

        String dest1;
        String price1;
        String time;
        Integer position;

        dest1=extras.getString("dest");
        price1=extras.getString("price");
        time=extras.getString("time");
        position=extras.getInt("position");


        String getDestObj = getIntent().getStringExtra("destObj");

        Logger.d("obj", getDestObj);

        ImageSlider is=findViewById(R.id.slider);

        List<SlideModel> sm=new ArrayList<>();

        sm.add(new SlideModel("https://picsum.photos/800"));
        sm.add(new SlideModel("https://picsum.photos/801"));
        sm.add(new SlideModel("https://picsum.photos/802"));
        sm.add(new SlideModel("https://picsum.photos/804"));
        sm.add(new SlideModel("https://picsum.photos/803"));

        View data= findViewById(R.id.button3);
        View btn= findViewById(R.id.backImgView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getBaseContext(),BookRide.class);
                intent.putExtra("position",position);
                intent.putExtra("name",dest1);
                startActivity(intent);
            }
        });

        is.setImageList(sm,true);

        TextView name = findViewById(R.id.destName);
        name.setText(dest1);

        TextView price = findViewById(R.id.ridePrice);
        price.setText(price1);

        TextView pickup = findViewById(R.id.pickupLoc);
        pickup.setText("Hotel Plaza");

        TextView dest = findViewById(R.id.destLoc);
        dest.setText(dest1);
    }
}