package com.melevicarbrasil.usuario;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class demo extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        TextView tv=findViewById(R.id.textView);
        Button bt=findViewById(R.id.button2);
        ImageView iv=findViewById(R.id.imageView3);
        tv.setText("No Cars Available!");

        tv.setVisibility(View.VISIBLE);

//        bt.setVisibility(View.VISIBLE);
//
//        iv.setVisibility(View.VISIBLE);




}};