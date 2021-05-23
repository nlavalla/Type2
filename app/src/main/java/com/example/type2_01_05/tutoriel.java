package com.example.type2_01_05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class tutoriel extends AppCompatActivity implements View.OnClickListener{
    Button bouton=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutoriel);
        bouton=(Button)findViewById(R.id.button);
        bouton.setOnClickListener(this);
    }
    public void onClick(View v) {
        Intent activite2= new Intent(tutoriel.this, MainActivity.class);
        startActivity(activite2);
        Animatoo.animateSlideRight(this);
        }

}
