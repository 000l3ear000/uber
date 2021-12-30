package com.melevicarbrasil.usuario;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DestinationsList extends AppCompatActivity {

    String detinationsTitles[] = {"Dune Sunset Jericoacoara", "Praia Principal de Jeri", "Piscina Natural Ananias", "Praia da Malhada","Trilha da Pedra Furada","Poço da Princesa","Pedra Furada","Pedra do Frade"};
    String detinationsDescriptions[] = {"Click here for more details"};
    String arr1[]={"$50","$30","$50","$30","$50","$30","$50","$30"};
    String timeSlots[]={"8:00 AM - 6:00 PM","8:00 AM - 6:00 PM","10:00 AM - 6:00 PM","10:00 AM - 6:00 PM","8:00 AM - 6:00 PM","8:00 AM - 6:00 PM","10:00 AM - 6:00 PM","10:00 AM - 6:00 PM"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations_list);

        ListView listView = findViewById(R.id.destinationsList);
        MyAdapter adapter = new MyAdapter(this, detinationsTitles, detinationsDescriptions,arr1,timeSlots);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(getBaseContext(), DestinationsDetails.class);

                String currentDestinationObject = parent.getItemAtPosition(position).toString();
                newIntent.putExtra("destObj", currentDestinationObject);
                newIntent.putExtra("dest",detinationsTitles[position].toString());
                newIntent.putExtra("price",arr1[position].toString());
                newIntent.putExtra("time",timeSlots[position].toString());
                newIntent.putExtra("position",position);


//                newIntent.putExtra("dest",detinationsTitles[position]);

                startActivity(newIntent);
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String dTitles[];
        String dDesc[];
        String dPrice[];
        String time1[];

        //      constructor
        MyAdapter(Context c, String title[], String desc[],String arr1[],String timeS[]){
            super(c, R.layout.destination_row,  R.id.textView1, title);
            this.context = c;
            this.dTitles = title;
            this.dDesc = desc;
            this.dPrice= arr1;
            this.time1=timeS;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.destination_row, parent, false);
            TextView tv1 = row.findViewById(R.id.textView1);
            TextView tv2 = row.findViewById(R.id.textView2);
            TextView tv3 = row.findViewById(R.id.textView3);
            TextView tv4 = row.findViewById(R.id.textView4);

            tv1.setText(dTitles[position]);
            tv2.setText(dDesc[0]);
            tv3.setText(dPrice[position]);
            tv4.setText(time1[position]);

            return row;
        }
    }
}