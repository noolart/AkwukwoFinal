package com.noolart.surfaceviewtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {

    Button backButton;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch musicSwitch, soundsSwitch;

    public static AudioPlayer audioPlayer = new AudioPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        backButton = findViewById(R.id.backButton);
        musicSwitch = findViewById(R.id.musicSwitch);
        soundsSwitch = findViewById(R.id.soundsSwitch);

        SharedPreferences sp = getSharedPreferences("storage", Context.MODE_PRIVATE);
        musicSwitch.setChecked(sp.getBoolean("music", true));
        soundsSwitch.setChecked(sp.getBoolean("sounds", true));



        backButton.setOnClickListener(view -> finish());

        musicSwitch.setOnClickListener(view -> {

            SharedPreferences.Editor ed = getSharedPreferences("storage", Context.MODE_PRIVATE).edit();
            ed.putBoolean("music", musicSwitch.isChecked());
            ed.apply();

            if (musicSwitch.isChecked()){
                audioPlayer = new AudioPlayer();
                audioPlayer.play(this);
            }

            else{
                audioPlayer.stop();
            }

        });

        soundsSwitch.setOnClickListener(view -> {

            SharedPreferences.Editor ed = getSharedPreferences("storage", Context.MODE_PRIVATE).edit();
            ed.putBoolean("sounds", soundsSwitch.isChecked());
            ed.apply();

        });
    }

}
