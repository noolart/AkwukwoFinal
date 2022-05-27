package com.noolart.surfaceviewtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    Button singlePlayer, multiplayer, shop, options;
    TextView bestScore;
    public static boolean loaded;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("storage", Context.MODE_PRIVATE);

        if(sp.getBoolean("music", true)){
            OptionsActivity.audioPlayer.stop();
            OptionsActivity.audioPlayer = new AudioPlayer();
            OptionsActivity.audioPlayer.play(this);
        }


        singlePlayer = findViewById(R.id.single_player);
        multiplayer = findViewById(R.id.multiplayer);
        options = findViewById(R.id.options);

        bestScore = findViewById(R.id.best);

        sp = getSharedPreferences("storage", Context.MODE_PRIVATE);
        bestScore.setText("best score: " + sp.getInt("best", 0));


        singlePlayer.setOnClickListener(view -> {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        });

        multiplayer.setOnClickListener(view ->{
            Intent intent = new Intent(StartActivity.this, MultiplayerActivity.class);
            startActivity(intent);
        });



        options.setOnClickListener(view ->{
            Intent intent = new Intent(StartActivity.this, OptionsActivity.class);
            startActivity(intent);
        });


    }
}
