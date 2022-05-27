package com.noolart.surfaceviewtest;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class AudioPlayer {

    private MediaPlayer mPlayer;


    public void stop() {
        if (mPlayer != null) {

            mPlayer.release();
            mPlayer = null;

        }
    }

    public void play(Context c){
        stop();
        mPlayer = MediaPlayer.create(c, R.raw.back);
        mPlayer.setLooping(true);
        mPlayer.setOnCompletionListener ( new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });
        mPlayer.start();
    }



}