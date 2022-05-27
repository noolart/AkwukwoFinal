package com.noolart.surfaceviewtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.util.ArrayList;
import java.util.Random;

public class MultiplayerSurface extends SurfaceView implements SurfaceHolder.Callback, SoundPool.OnLoadCompleteListener {

    float k, width, height;
    float xBlue, yBlue, xRed, yRed, iXBlue, iYBlue, iXRed, iYRed, tX = 0, tY = 0, homeXBlue, homeYBlue, homeXRed, homeYRed;
    Bitmap redPlayer, bluePlayer;
    public static Resources res;
    CustomControlKnobOnSurfaceView customControlKnobOnSurfaceView1, customControlKnobOnSurfaceView2;
    Paint paint;
    int fromWhere, airplaneX, airplaneY, lighthouseX, lighthouseY, blueLighthouseCounter=0, redLighthouseCounter=0, blueCoinCounter=0, redCoinCounter=0, blueStepCounter=0, redStepCounter=0, blueDeathsCounter=0, redDeathsCounter=0, animationSpeed = 54, squareSide;
    static Random r = new Random();
    Paint smallFontPaint ,fontPaint, bigFontPaint, giveUpFontPaint, countersFontPaint;
    Typeface font;
    MultiplayerDrawThread drawThread;
    float hi, wi;
    boolean isFirst = true, earthquake=false, blueHasMove=true, redHasMove=true, blueAirplane=false, redAirplane=false, lighthouse=false, blueHaveCoin=false, redHaveCoin=false,locked=false, firstPlayerTurn = true, blueHorse = false, redHorse = false, sounds, blueWin, redWin, draw;
    MultiplayerMap gameMap;
    ArrayList<Integer> earthQuakeArrayList;
    CustomButtonOnSurfaceView homeBlue, helpBlue, homeRed, helpRed;


    final String LOG_TAG = "myLogs";
    final int MAX_STREAMS = 5;

    SoundPool sp;

    int soundId;


    public MultiplayerSurface(Context context) {
        super(context);
        getHolder().addCallback(this);

        homeXBlue = 0;
        homeYBlue = 7;
        homeXRed = 7;
        homeYRed = 0;

        k = 20;
        res = getResources();

        redPlayer = BitmapFactory.decodeResource(res, R.drawable.player_red);
        bluePlayer = BitmapFactory.decodeResource(res, R.drawable.player_blue);

        paint = new Paint();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            font = getResources().getFont(R.font.albionic);
        }

        smallFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        smallFontPaint.setTextSize(35);
        smallFontPaint.setColor(Color.WHITE);
        smallFontPaint.setTypeface(font);

        fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setTextSize(75);
        fontPaint.setColor(Color.WHITE);
        fontPaint.setTypeface(font);

        giveUpFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        giveUpFontPaint.setTextSize(110);
        giveUpFontPaint.setColor(Color.WHITE);
        giveUpFontPaint.setTypeface(font);

        bigFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigFontPaint.setTextSize(130);
        bigFontPaint.setColor(Color.WHITE);
        bigFontPaint.setTypeface(font);

        countersFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        countersFontPaint.setTextSize(65);
        countersFontPaint.setColor(Color.WHITE);
        countersFontPaint.setTypeface(font);

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);

        soundId = sp.load(getContext(), R.raw.step, 1);
        Log.d(LOG_TAG, "soundId = " + soundId);

        SharedPreferences sp = getContext().getSharedPreferences("storage", Context.MODE_PRIVATE);
        sounds = sp.getBoolean("sounds", true);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        Log.d(LOG_TAG, "onLoadComplete, sampleId = " + sampleId + ", status = " + status);
    }

    @Override
    public void draw(Canvas canvas){

        super.draw(canvas);
        if (isFirst){
            height = getHeight();
            width = getWidth();
            squareSide = (int)width/10;
            hi = squareSide/1.5f;
            wi = squareSide/1.5f;

            xBlue = (width-8*squareSide)/2+(squareSide-wi)/2;
            yBlue = (height-8*squareSide)/2+8*squareSide-hi-(squareSide-hi)/2;

            xRed = xBlue + 7*squareSide;
            yRed = yBlue - 7*squareSide;

            blueStepCounter = 0;
            blueCoinCounter = 0;

            redStepCounter = 0;
            redCoinCounter = 0;


            iXBlue = (width-8*squareSide)/2+(squareSide-wi)/2;
            iYBlue = (height-8*squareSide)/2+8*squareSide-hi-(squareSide-hi)/2;

            iXRed = iXBlue + 7*squareSide;
            iYRed = iYBlue - 7*squareSide;

            gameMap = new MultiplayerMap((int) width, (int) height, res);
            isFirst = false;
            earthQuakeArrayList = new ArrayList<>();

            customControlKnobOnSurfaceView1 = new CustomControlKnobOnSurfaceView(squareSide,0,(float)(width/4-squareSide*1.5+10),(float)((height-8*squareSide)*0.625+7.5*squareSide+20), res);
            customControlKnobOnSurfaceView2 = new CustomControlKnobOnSurfaceView(squareSide, 1, (float)(width/4*3-1.5*squareSide-20),(float)((height-8*squareSide)/4-1.5*squareSide), res);


            homeBlue = new CustomButtonOnSurfaceView(customControlKnobOnSurfaceView2.left.leftX,customControlKnobOnSurfaceView2.left.rightX,customControlKnobOnSurfaceView1.down.upY,customControlKnobOnSurfaceView1.down.downY,BitmapFactory.decodeResource(res, R.drawable.home_blue),null);
            helpBlue = new CustomButtonOnSurfaceView(customControlKnobOnSurfaceView2.mid.leftX,customControlKnobOnSurfaceView2.mid.rightX,customControlKnobOnSurfaceView1.down.upY,customControlKnobOnSurfaceView1.down.downY,BitmapFactory.decodeResource(res, R.drawable.info),null);
            homeRed = new CustomButtonOnSurfaceView(customControlKnobOnSurfaceView1.left.leftX,customControlKnobOnSurfaceView1.left.rightX,customControlKnobOnSurfaceView2.down.upY,customControlKnobOnSurfaceView2.down.downY,BitmapFactory.decodeResource(res, R.drawable.home_red),null);
            helpRed = new CustomButtonOnSurfaceView(customControlKnobOnSurfaceView1.mid.leftX,customControlKnobOnSurfaceView1.mid.rightX,customControlKnobOnSurfaceView2.down.upY,customControlKnobOnSurfaceView2.down.downY,BitmapFactory.decodeResource(res, R.drawable.info),null);

        }

        if (isDone(gameMap.textures)){
            endGame();
        }


            int arrIndex = gameMap.textures[(int) (iYBlue - (height - 8 * squareSide) / 2) / squareSide][(int) (iXBlue - (width - 8 * squareSide) / 2) / squareSide];


            if (arrIndex != 10) {
                blueHasMove = true;
            }

            if (arrIndex == 0) {
                int random = r.nextInt(gameMap.cardsOnAllField.size());
                gameMap.textures[(int) (iYBlue - (height - 8 * squareSide) / 2) / squareSide][(int) (iXBlue - (width - 8 * squareSide) / 2) / squareSide] = gameMap.cardsOnAllField.remove(random);

            } else if (arrIndex == 1 && !(iYBlue - squareSide < (height - 8 * squareSide) / 2) && !locked) {
                iYBlue -= squareSide;
                fromWhere = 1;
            } else if (arrIndex == 2 && !(iYBlue + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide) && !locked) {
                iYBlue += squareSide;
                fromWhere = 4;
            } else if (arrIndex == 3 && !(iXBlue - squareSide < (width - 8 * squareSide) / 2) && !locked) {
                iXBlue -= squareSide;
                fromWhere = 2;
            } else if (arrIndex == 4 && !(iXBlue + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide) && !locked) {
                iXBlue += squareSide;
                fromWhere = 3;
            } else if (arrIndex == 5 && !locked) {

                iXBlue = (width - 864) / 2 + homeXBlue * squareSide + 18;
                iYBlue = (height - 864) / 2 + homeYBlue * squareSide + 18;
            } else if (arrIndex == 6 && !locked) {
                while (!(iXBlue + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide)) {
                    iXBlue += squareSide;
                }
                fromWhere = 0;
                blueHasMove = true;
            } else if (arrIndex == 7 && !locked) {
                while (!(iXBlue - squareSide < (width - 8 * squareSide) / 2)) {
                    iXBlue -= squareSide;
                }
                fromWhere = 0;
            } else if (arrIndex == 8 && !locked) {
                if (fromWhere == 1 && !(iYBlue - squareSide < (height - 8 * squareSide) / 2)) {
                    iYBlue -= squareSide;
                } else if (fromWhere == 2 && !(iXBlue - squareSide < (width - 8 * squareSide) / 2)) {
                    iXBlue -= squareSide;
                } else if (fromWhere == 3 && !(iXBlue + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide)) {
                    iXBlue += squareSide;
                } else if (fromWhere == 4 && !(iYBlue + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide)) {
                    iYBlue += squareSide;
                } else if (fromWhere == 5 && !(iYBlue - squareSide < (height - 8 * squareSide) / 2) && !(iXBlue + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide)) {
                    iYBlue -= squareSide;
                    iXBlue += squareSide;
                } else if (fromWhere == 6 && !(iYBlue + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide) && !(iXBlue + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide)) {
                    iYBlue += squareSide;
                    iXBlue += squareSide;
                } else if (fromWhere == 7 && !(iYBlue + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide) && !(iXBlue - squareSide < (width - 8 * squareSide) / 2)) {
                    iYBlue += squareSide;
                    iXBlue -= squareSide;
                } else if (fromWhere == 8 && !(iYBlue - squareSide < (height - 8 * squareSide) / 2) && !(iXBlue - squareSide < (width - 8 * squareSide) / 2)) {
                    iYBlue -= squareSide;
                    iXBlue -= squareSide;
                }
            } else if (arrIndex == 9 && !locked) {
                if (blueHaveCoin) {
                    blueHaveCoin = false;
                }
                blueDeathsCounter++;
                iXBlue = (width - 864) / 2 + homeXBlue * squareSide + 18;
                iYBlue = (height - 864) / 2 + homeYBlue * squareSide + 18;
            } else if (arrIndex == 10 && blueHasMove && !locked) {
                earthquake = true;
                blueHasMove = false;
                firstPlayerTurn = changeValue(firstPlayerTurn);
            } else if (arrIndex == 11 && !locked) {
                fromWhere = 0;
                blueAirplane = true;
                airplaneX = (int) (iYBlue - (height - 8 * squareSide) / 2) / squareSide;
                airplaneY = (int) (iXBlue - (width - 8 * squareSide) / 2) / squareSide;
                firstPlayerTurn = true;
            } else if (arrIndex == 12 && !locked) {
                lighthouse = true;
                lighthouseX = (int) (iYBlue - (height - 8 * squareSide) / 2) / squareSide;
                lighthouseY = (int) (iXBlue - (width - 8 * squareSide) / 2) / squareSide;
            } else if (arrIndex == 20 && !locked) {
                blueHorse = true;
            } else if (arrIndex == 21 && !(iYBlue - squareSide < (height - 8 * squareSide) / 2) && !(iXBlue + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide) && !locked) {
                iYBlue -= squareSide;
                iXBlue += squareSide;
                fromWhere = 5;
            } else if (arrIndex == 22 && !(iYBlue + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide) && !(iXBlue + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide) && !locked) {
                iYBlue += squareSide;
                iXBlue += squareSide;
                fromWhere = 6;
            } else if (arrIndex == 23 && !(iYBlue + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide) && !(iXBlue - squareSide < (width - 8 * squareSide) / 2) && !locked) {
                iYBlue += squareSide;
                iXBlue -= squareSide;
                fromWhere = 7;
            } else if (arrIndex == 24 && !(iYBlue - squareSide < (height - 8 * squareSide) / 2) && !(iXBlue - squareSide < (width - 8 * squareSide) / 2) && !locked) {
                iYBlue -= squareSide;
                iXBlue -= squareSide;
                fromWhere = 8;
            } else if (arrIndex == 58 && !locked) {
                Log.e("end","here");
                if (blueHaveCoin) {
                    blueHaveCoin = false;
                    blueCoinCounter++;
                }
            }




            arrIndex = gameMap.textures[(int) (iYRed - (height - 8 * squareSide) / 2) / squareSide][(int) (iXRed - (width - 8 * squareSide) / 2) / squareSide];


            if (arrIndex != 10) {
                redHasMove = true;
            }

            if (arrIndex == 0) {
                int random = r.nextInt(gameMap.cardsOnAllField.size());
                gameMap.textures[(int) (iYRed - (height - 8 * squareSide) / 2) / squareSide][(int) (iXRed - (width - 8 * squareSide) / 2) / squareSide] = gameMap.cardsOnAllField.remove(random);

            } else if (arrIndex == 1 && !(iYRed - squareSide < (height - 8 * squareSide) / 2) && !locked) {
                iYRed -= squareSide;
                fromWhere = 1;
            } else if (arrIndex == 2 && !(iYRed + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide) && !locked) {
                iYRed += squareSide;
                fromWhere = 4;
            } else if (arrIndex == 3 && !(iXRed - squareSide < (width - 8 * squareSide) / 2) && !locked) {
                iXRed -= squareSide;
                fromWhere = 2;
            } else if (arrIndex == 4 && !(iXRed + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide) && !locked) {
                iXRed += squareSide;
                fromWhere = 3;
            } else if (arrIndex == 5 && !locked) {

                iXRed = (width - 864) / 2 + homeXRed * squareSide + 18;
                iYRed = (height - 864) / 2 + homeYRed * squareSide + 18;
            } else if (arrIndex == 6 && !locked) {
                while (!(iXRed + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide)) {
                    iXRed += squareSide;
                }
                fromWhere = 0;
                redHasMove = true;
            } else if (arrIndex == 7 && !locked) {
                while (!(iXRed - squareSide < (width - 8 * squareSide) / 2)) {
                    iXRed -= squareSide;
                }
                fromWhere = 0;
            } else if (arrIndex == 8 && !locked) {
                if (fromWhere == 1 && !(iYRed - squareSide < (height - 8 * squareSide) / 2)) {
                    iYRed -= squareSide;
                } else if (fromWhere == 2 && !(iXRed - squareSide < (width - 8 * squareSide) / 2)) {
                    iXRed -= squareSide;
                } else if (fromWhere == 3 && !(iXRed + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide)) {
                    iXRed += squareSide;
                } else if (fromWhere == 4 && !(iYRed + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide)) {
                    iYRed += squareSide;
                } else if (fromWhere == 5 && !(iYRed - squareSide < (height - 8 * squareSide) / 2) && !(iXRed + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide)) {
                    iYRed -= squareSide;
                    iXRed += squareSide;
                } else if (fromWhere == 6 && !(iYRed + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide) && !(iXRed + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide)) {
                    iYRed += squareSide;
                    iXRed += squareSide;
                } else if (fromWhere == 7 && !(iYRed + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide) && !(iXRed - squareSide < (width - 8 * squareSide) / 2)) {
                    iYRed += squareSide;
                    iXRed -= squareSide;
                } else if (fromWhere == 8 && !(iYRed - squareSide < (height - 8 * squareSide) / 2) && !(iXRed - squareSide < (width - 8 * squareSide) / 2)) {
                    iYRed -= squareSide;
                    iXRed -= squareSide;
                }
            } else if (arrIndex == 9 && !locked) {
                if (redHaveCoin) {
                    redHaveCoin = false;
                }
                redDeathsCounter++;
                iXRed = (width - 864) / 2 + homeXRed * squareSide + 18;
                iYRed = (height - 864) / 2 + homeYRed * squareSide + 18;
            } else if (arrIndex == 10 && redHasMove && !locked) {
                earthquake = true;
                redHasMove = false;
                firstPlayerTurn = changeValue(firstPlayerTurn);
            } else if (arrIndex == 11 && !locked) {
                fromWhere = 0;
                redAirplane = true;
                airplaneX = (int) (iYRed - (height - 8 * squareSide) / 2) / squareSide;
                airplaneY = (int) (iXRed - (width - 8 * squareSide) / 2) / squareSide;
                firstPlayerTurn = false;
            } else if (arrIndex == 12 && !locked) {
                lighthouse = true;
                lighthouseX = (int) (iYRed - (height - 8 * squareSide) / 2) / squareSide;
                lighthouseY = (int) (iXRed - (width - 8 * squareSide) / 2) / squareSide;
            } else if (arrIndex == 20 && !locked) {
               redHorse = true;
            } else if (arrIndex == 21 && !(iYRed - squareSide < (height - 8 * squareSide) / 2) && !(iXRed + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide) && !locked) {
                iYRed -= squareSide;
                iXRed += squareSide;
                fromWhere = 5;
            } else if (arrIndex == 22 && !(iYRed + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide) && !(iXRed + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide) && !locked) {
                iYRed += squareSide;
                iXRed += squareSide;
                fromWhere = 6;
            } else if (arrIndex == 23 && !(iYRed + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide) && !(iXRed - squareSide < (width - 8 * squareSide) / 2) && !locked) {
                iYRed += squareSide;
                iXRed -= squareSide;
                fromWhere = 7;
            } else if (arrIndex == 24 && !(iYRed - squareSide < (height - 8 * squareSide) / 2) && !(iXRed - squareSide < (width - 8 * squareSide) / 2) && !locked) {
                iYRed -= squareSide;
                iXRed -= squareSide;
                fromWhere = 8;
            } else if (arrIndex == 59 && !locked) {
                if (redHaveCoin) {
                    redHaveCoin = false;
                    redCoinCounter++;
                }
            }


        gameMap.draw(canvas);

        if (iXBlue==iXRed && iYBlue==iYRed){
            if (firstPlayerTurn){
                if (blueHaveCoin) {
                    blueHaveCoin = false;
                }
                blueDeathsCounter++;
                iXBlue = (width - 8*squareSide) / 2 + homeXBlue * squareSide + 18;
                iYBlue = (height - 8*squareSide) / 2 + homeYBlue * squareSide + 18;
            }
            else {
                if (redHaveCoin) {
                    redHaveCoin = false;
                }
                redDeathsCounter++;
                iXRed = (width - 8*squareSide) / 2 + homeXRed * squareSide + 18;
                iYRed = (height - 8*squareSide) / 2 + homeYRed * squareSide + 18;
            }
        }



        if (iXBlue-xBlue!=0 || iYBlue-yBlue!=0) {
            if (iXBlue - xBlue > 0) {
                xBlue += animationSpeed;
            }
            if (iXBlue - xBlue < 0) {
                xBlue -= animationSpeed;
            }
            if (iYBlue - yBlue > 0) {
                yBlue += animationSpeed;
            }
            if (iYBlue - yBlue < 0) {
                yBlue -= animationSpeed;
            }
            locked = true;
        }

       if (iXRed-xRed!=0 || iYRed-yRed!=0) {
            if (iXRed - xRed > 0) {
                xRed += animationSpeed;
            }
            if (iXRed - xRed < 0) {
                xRed -= animationSpeed;
            }
            if (iYRed - yRed > 0) {
                yRed += animationSpeed;
            }
            if (iYRed - yRed < 0) {
                yRed -= animationSpeed;
            }
            locked = true;
        }

        else {
            locked = false;
        }

        canvas.drawBitmap(redPlayer, null, new Rect((int)xRed,(int)yRed,(int)xRed+72,(int)yRed+72), paint);
        canvas.drawBitmap(bluePlayer, null, new Rect((int)xBlue,(int)yBlue,(int)xBlue+72,(int)yBlue+72), paint);

        customControlKnobOnSurfaceView1.drawControlKnob(canvas, firstPlayerTurn);
        customControlKnobOnSurfaceView2.drawControlKnob(canvas, !firstPlayerTurn);

        canvas.drawText("coins: " + blueCoinCounter, customControlKnobOnSurfaceView2.left.leftX,customControlKnobOnSurfaceView1.up.downY,countersFontPaint);
        canvas.drawText("coins: " + redCoinCounter, customControlKnobOnSurfaceView1.left.leftX,customControlKnobOnSurfaceView2.up.downY,countersFontPaint);

        canvas.drawText( "deaths: " + blueDeathsCounter,
                customControlKnobOnSurfaceView2.left.leftX,
                customControlKnobOnSurfaceView1.mid.downY - (float)squareSide/2,
                countersFontPaint);
        canvas.drawText("deaths: " + redDeathsCounter,
                customControlKnobOnSurfaceView1.left.leftX,
                customControlKnobOnSurfaceView2.mid.downY - (float)squareSide/2,
                countersFontPaint);

        homeBlue.drawButton(canvas);
        homeRed.drawButton(canvas);

        helpBlue.drawButton(canvas);
        helpRed.drawButton(canvas);

        if (blueHaveCoin){
            canvas.drawBitmap(gameMap.images[60], null, new Rect((int)xBlue,(int)yBlue+36,(int)xBlue+36,(int)yBlue+72), paint);
        }
        if (redHaveCoin) {
            canvas.drawBitmap(gameMap.images[60], null, new Rect((int) xRed, (int) yRed + 36, (int) xRed + 36, (int) yRed + 72), paint);
        }

        if (blueWin){
            drawTextOnScreen("Blue wins!", Color.BLUE, canvas);
        }

        if (redWin){
            drawTextOnScreen("Red wins!", Color.RED, canvas);
        }

        if (draw){
            drawTextOnScreen("DRAW!", Color.WHITE, canvas);
        }





    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        tX = event.getX();
        tY = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (homeRed.isClicked(tX, tY) || homeBlue.isClicked(tX, tY)){
                Intent intent = new Intent(getContext(), StartActivity.class);
                boolean stop = true;
                drawThread.setRun(false);
                while(stop) {
                    try {
                        drawThread.join();
                        stop = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                getContext().startActivity(intent);
            }



            if (helpRed.isClicked(tX,tY) || helpBlue.isClicked(tX,tY)){
                Intent intent = new Intent(getContext(), HelpActivity.class);
                intent.putExtra("activity",2);


                boolean stop = true;
                drawThread.setRun(false);
                while(stop) {
                    try {
                        drawThread.join();
                        stop = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                getContext().startActivity(intent);

            }








            if (firstPlayerTurn) {
                if (earthquake && !locked) {

                    if (0 <= (int) ((tX - (width - squareSide * 8) / 2) / squareSide) && (int) ((tX - (width - squareSide * 8) / 2) / squareSide) <= 7 && 0 <= (int) ((tY - (height - squareSide * 8) / 2) / squareSide) && (int) ((tY - (height - squareSide * 8) / 2) / squareSide) <= 7) {
                        earthQuakeArrayList.add((int) ((tY - (height - squareSide * 8) / 2) / squareSide));
                        earthQuakeArrayList.add((int) ((tX - (width - squareSide * 8) / 2) / squareSide));
                    }

                    if (earthQuakeArrayList.size() == 4) {

                        if (earthQuakeArrayList.get(0) == homeYBlue && earthQuakeArrayList.get(1) == homeXBlue) {
                            homeYBlue = earthQuakeArrayList.get(2);
                            homeXBlue = earthQuakeArrayList.get(3);
                        } else if (earthQuakeArrayList.get(2) == homeYBlue && earthQuakeArrayList.get(3) == homeXBlue) {
                            homeYBlue = earthQuakeArrayList.get(0);
                            homeXBlue = earthQuakeArrayList.get(1);
                        }

                        if (earthQuakeArrayList.get(0) == homeYRed && earthQuakeArrayList.get(1) == homeXRed) {
                            homeYRed = earthQuakeArrayList.get(2);
                            homeXRed = earthQuakeArrayList.get(3);
                        } else if (earthQuakeArrayList.get(2) == homeYRed && earthQuakeArrayList.get(3) == homeXRed) {
                            homeYRed = earthQuakeArrayList.get(0);
                            homeXRed = earthQuakeArrayList.get(1);
                        }

                        int a = gameMap.textures[earthQuakeArrayList.get(0)][earthQuakeArrayList.get(1)];
                        gameMap.textures[earthQuakeArrayList.get(0)][earthQuakeArrayList.get(1)] = gameMap.textures[earthQuakeArrayList.get(2)][earthQuakeArrayList.get(3)];
                        gameMap.textures[earthQuakeArrayList.get(2)][earthQuakeArrayList.get(3)] = a;
                        earthQuakeArrayList.clear();
                        earthquake = false;
                        firstPlayerTurn = false;
                    }
                } else if (blueAirplane && !locked) {
                    if (0 <= (int) ((tX - (width - squareSide * 8) / 2) / squareSide) && (int) ((tX - (width - squareSide * 8) / 2) / squareSide) <= 7 && 0 <= (int) ((tY - (height - squareSide * 8) / 2) / squareSide) && (int) ((tY - (height - squareSide * 8) / 2) / squareSide) <= 7) {
                        iYBlue = (height - 864) / 2 + ((int) ((tY - (height - squareSide * 8) / 2) / squareSide)) * squareSide + 18;
                        iXBlue = (width - 864) / 2 + ((int) ((tX - (width - squareSide * 8) / 2) / squareSide)) * squareSide + 18;
                        gameMap.textures[airplaneX][airplaneY] = 61;
                        blueAirplane = false;
                    }
                } else if (lighthouse && !locked) {

                    if (0 <= (int) ((tX - (width - squareSide * 8) / 2) / squareSide) && (int) ((tX - (width - squareSide * 8) / 2) / squareSide) <= 7 && 0 <= (int) ((tY - (height - squareSide * 8) / 2) / squareSide) && (int) ((tY - (height - squareSide * 8) / 2) / squareSide) <= 7) {
                        if (gameMap.textures[(int) ((tY - (height - squareSide * 8) / 2) / squareSide)][(int) ((tX - (width - squareSide * 8) / 2) / squareSide)] == 0) {
                            int random = r.nextInt(gameMap.cardsOnAllField.size());
                            gameMap.textures[(int) (tY - (height - 8 * squareSide) / 2) / squareSide][(int) (tX - (width - 8 * squareSide) / 2) / squareSide] = gameMap.cardsOnAllField.remove(random);
                            blueLighthouseCounter++;
                        }
                        if (blueLighthouseCounter == 4) {
                            blueLighthouseCounter = 0;
                            gameMap.textures[lighthouseX][lighthouseY] = 62;
                            lighthouse = false;
                        }
                    }

                }else if (blueHorse){
                    int[] side = horseStep(iXBlue, iYBlue, tX, tY);

                    iXBlue += side[0]*squareSide;
                    iYBlue += side[1]*squareSide;

                    if (side[0]!=0) {
                        blueHorse = false;
                        firstPlayerTurn = false;
                    }
                }else {
                    if (!locked && customControlKnobOnSurfaceView1.isClicked(tX, tY)) {
                        if (!(iYBlue - squareSide < (height - 8 * squareSide) / 2) && customControlKnobOnSurfaceView1.up.isClicked(tX, tY)) {
                            iYBlue -= squareSide;
                            fromWhere = 1;
                            blueHasMove = true;
                            blueStepCounter++;
                            playStepSound();
                            if (!locked) {
                                firstPlayerTurn = false;
                            }
                        }
                        if (!(iXBlue - squareSide < (width - 8 * squareSide) / 2) && customControlKnobOnSurfaceView1.left.isClicked(tX, tY)) {
                            iXBlue -= squareSide;
                            fromWhere = 2;
                            blueHasMove = true;
                            blueStepCounter++;
                            playStepSound();
                            if (!locked) {
                                firstPlayerTurn = false;
                            }
                        }
                        if (!(iXBlue + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide) && customControlKnobOnSurfaceView1.right.isClicked(tX, tY)) {
                            iXBlue += squareSide;
                            fromWhere = 3;
                            blueHasMove = true;
                            blueStepCounter++;
                            playStepSound();
                            if (!locked) {
                                firstPlayerTurn = false;
                            }
                        }
                        if (!(iYBlue + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide) && customControlKnobOnSurfaceView1.down.isClicked(tX, tY)) {
                            iYBlue += squareSide;
                            fromWhere = 4;
                            blueHasMove = true;
                            blueStepCounter++;
                            playStepSound();
                            if (!locked) {
                                firstPlayerTurn = false;
                            }
                        }
                        if (!blueHaveCoin && customControlKnobOnSurfaceView1.mid.isClicked(tX, tY) && gameMap.textures[(int) (iYBlue - (height - 8 * squareSide) / 2) / squareSide][(int) (iXBlue - (width - 8 * squareSide) / 2) / squareSide] >= 14 && gameMap.textures[(int) (iYBlue - (height - 8 * squareSide) / 2) / squareSide][(int) (iXBlue - (width - 8 * squareSide) / 2) / squareSide] <= 18) {
                            blueHaveCoin = true;
                            gameMap.textures[(int) (iYBlue - (height - 8 * squareSide) / 2) / squareSide][(int) (iXBlue - (width - 8 * squareSide) / 2) / squareSide] -= 1;
                            blueHasMove = true;
                            blueStepCounter++;
                            playStepSound();
                            if (!locked) {
                                firstPlayerTurn = false;
                            }
                        }

                    }
                }

                }

            else {

                    if (earthquake && !locked) {

                        if (0 <= (int) ((tX - (width - squareSide * 8) / 2) / squareSide) && (int) ((tX - (width - squareSide * 8) / 2) / squareSide) <= 7 && 0 <= (int) ((tY - (height - squareSide * 8) / 2) / squareSide) && (int) ((tY - (height - squareSide * 8) / 2) / squareSide) <= 7) {
                            earthQuakeArrayList.add((int) ((tY - (height - squareSide * 8) / 2) / squareSide));
                            earthQuakeArrayList.add((int) ((tX - (width - squareSide * 8) / 2) / squareSide));
                        }

                        if (earthQuakeArrayList.size() == 4) {

                            if (earthQuakeArrayList.get(0) == homeYBlue && earthQuakeArrayList.get(1) == homeXBlue) {
                                homeYBlue = earthQuakeArrayList.get(2);
                                homeXBlue = earthQuakeArrayList.get(3);
                            } else if (earthQuakeArrayList.get(2) == homeYBlue && earthQuakeArrayList.get(3) == homeXBlue) {
                                homeYBlue = earthQuakeArrayList.get(0);
                                homeXBlue = earthQuakeArrayList.get(1);
                            }

                            if (earthQuakeArrayList.get(0) == homeYRed && earthQuakeArrayList.get(1) == homeXRed) {
                                homeYRed = earthQuakeArrayList.get(2);
                                homeXRed = earthQuakeArrayList.get(3);
                            } else if (earthQuakeArrayList.get(2) == homeYRed && earthQuakeArrayList.get(3) == homeXRed) {
                                homeYRed = earthQuakeArrayList.get(0);
                                homeXRed = earthQuakeArrayList.get(1);
                            }

                            int a = gameMap.textures[earthQuakeArrayList.get(0)][earthQuakeArrayList.get(1)];
                            gameMap.textures[earthQuakeArrayList.get(0)][earthQuakeArrayList.get(1)] = gameMap.textures[earthQuakeArrayList.get(2)][earthQuakeArrayList.get(3)];
                            gameMap.textures[earthQuakeArrayList.get(2)][earthQuakeArrayList.get(3)] = a;
                            earthQuakeArrayList.clear();
                            earthquake = false;
                            firstPlayerTurn = true;
                        }



                    } else if (redAirplane && !locked) {
                        if (0 <= (int) ((tX - (width - squareSide * 8) / 2) / squareSide) && (int) ((tX - (width - squareSide * 8) / 2) / squareSide) <= 7 && 0 <= (int) ((tY - (height - squareSide * 8) / 2) / squareSide) && (int) ((tY - (height - squareSide * 8) / 2) / squareSide) <= 7) {
                            iYRed = (height - 864) / 2 + ((int) ((tY - (height - squareSide * 8) / 2) / squareSide)) * squareSide + 18;
                            iXRed = (width - 864) / 2 + ((int) ((tX - (width - squareSide * 8) / 2) / squareSide)) * squareSide + 18;
                            gameMap.textures[airplaneX][airplaneY] = 61;
                            redAirplane = false;
                        }
                    } else if (lighthouse && !locked) {

                        if (0 <= (int) ((tX - (width - squareSide * 8) / 2) / squareSide) && (int) ((tX - (width - squareSide * 8) / 2) / squareSide) <= 7 && 0 <= (int) ((tY - (height - squareSide * 8) / 2) / squareSide) && (int) ((tY - (height - squareSide * 8) / 2) / squareSide) <= 7) {
                            if (gameMap.textures[(int) ((tY - (height - squareSide * 8) / 2) / squareSide)][(int) ((tX - (width - squareSide * 8) / 2) / squareSide)] == 0) {
                                int random = r.nextInt(gameMap.cardsOnAllField.size());
                                gameMap.textures[(int) (tY - (height - 8 * squareSide) / 2) / squareSide][(int) (tX - (width - 8 * squareSide) / 2) / squareSide] = gameMap.cardsOnAllField.remove(random);
                                redLighthouseCounter++;
                            }
                            if (redLighthouseCounter == 4) {
                                redLighthouseCounter = 0;
                                gameMap.textures[lighthouseX][lighthouseY] = 62;
                                lighthouse = false;
                            }
                        }

                    } else if (redHorse){

                            int[] side = horseStep(iXRed, iYRed, tX, tY);

                        iXRed += side[0]*squareSide;
                        iYRed += side[1]*squareSide;

                        if (side[0]!=0) {
                            redHorse = false;
                            firstPlayerTurn = true;
                        }


                    }else {
                        if (!locked && customControlKnobOnSurfaceView2.isClicked(tX, tY)) {
                            if (!(iYRed - squareSide < (height - 8 * squareSide) / 2) && customControlKnobOnSurfaceView2.up.isClicked(tX, tY)) {
                                iYRed -= squareSide;
                                fromWhere = 1;
                                redHasMove = true;
                                redStepCounter++;
                                playStepSound();
                                if (!locked) {
                                    firstPlayerTurn = true;
                                }

                            }
                            if (!(iXRed - squareSide < (width - 8 * squareSide) / 2) && customControlKnobOnSurfaceView2.left.isClicked(tX, tY)) {
                                iXRed -= squareSide;
                                fromWhere = 2;
                                redHasMove = true;
                                redStepCounter++;
                                playStepSound();
                                if (!locked) {
                                    firstPlayerTurn = true;
                                }
                            }
                            if (!(iXRed + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide) && customControlKnobOnSurfaceView2.right.isClicked(tX, tY)) {
                                iXRed += squareSide;
                                fromWhere = 3;
                                redHasMove = true;
                                redStepCounter++;
                                playStepSound();
                                if (!locked) {
                                    firstPlayerTurn = true;
                                }
                            }
                            if (!(iYRed + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide) && customControlKnobOnSurfaceView2.down.isClicked(tX, tY)) {
                                iYRed += squareSide;
                                fromWhere = 4;
                                redHasMove = true;
                                redStepCounter++;
                                playStepSound();
                                if (!locked) {
                                    firstPlayerTurn = true;
                                }
                            }
                            if (!redHaveCoin && customControlKnobOnSurfaceView2.mid.isClicked(tX, tY) && gameMap.textures[(int) (iYRed - (height - 8 * squareSide) / 2) / squareSide][(int) (iXRed - (width - 8 * squareSide) / 2) / squareSide] <= 18) {
                                redHaveCoin = true;
                                gameMap.textures[(int) (iYRed - (height - 8 * squareSide) / 2) / squareSide][(int) (iXRed - (width - 8 * squareSide) / 2) / squareSide] -= 1;
                                redHasMove = true;
                                redStepCounter++;
                                if (!locked) {
                                    firstPlayerTurn = true;
                                }
                            }

                        }
                    }



                }
            }

                return true;

        }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new MultiplayerDrawThread(this, getHolder());
        drawThread.setRun(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean stop = true;
        drawThread.setRun(false);
        while(stop) {
            try {
                drawThread.join();
                stop = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    boolean changeValue(boolean x){
        return !x;
    }

    boolean isDone(int[][] textures){
        for (int[] texture : textures) {
            for (int j = 0; j < textures[0].length; j++) {
                if (texture[j] == 0 || (texture[j] >= 14 && texture[j] <= 18)) {
                    return false;
                }

            }

        }
        return true;
    }

    void endGame(){

        if (blueCoinCounter>redCoinCounter){
            blueWin = true;
        }
        else if (blueCoinCounter<redCoinCounter){
            redWin = true;

        }
        else{
            draw = true;
        }
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getContext(), StartActivity.class);

            boolean stop = true;
            drawThread.setRun(false);
            while(stop) {
                try {
                    drawThread.join();
                    stop = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            getContext().startActivity(intent);
        });

        thread.start();
    }


    void drawTextOnScreen(String text, int color, Canvas canvas){
        Paint texOnScreen = new Paint();
        texOnScreen.setColor(color);
        texOnScreen.setTypeface(font);
        texOnScreen.setTextSize(140);
        canvas.drawRect(squareSide,(float)getHeight()/2-squareSide-5,9*squareSide,(float)getHeight()/2+squareSide+5, new Paint(Color.BLACK));
        canvas.drawText(text, 2*squareSide,(float)(canvas.getHeight()/2+squareSide/2),texOnScreen);
    }

    int[] horseStep(float X, float Y, float tX, float tY){

        int[][] possibleSteps = new int[8][2];

        int x = (int)(X - (width - squareSide * 8) / 2) / squareSide;
        int y= (int)(Y - (height - squareSide * 8)/ 2 ) / squareSide;

        possibleSteps[0][0] = x+2;
        possibleSteps[0][1] = y+1;


        possibleSteps[1][0] = x+2;
        possibleSteps[1][1] = y-1;


        possibleSteps[2][0] = x+1;
        possibleSteps[2][1] = y+2;


        possibleSteps[3][0] = x+1;
        possibleSteps[3][1] = y-2;


        possibleSteps[4][0] = x-2;
        possibleSteps[4][1] = y+1;

        possibleSteps[5][0] = x-2;
        possibleSteps[5][1] = y-1;

        possibleSteps[6][0] = x-1;
        possibleSteps[6][1] = y+2;

        possibleSteps[7][0] = x-1;
        possibleSteps[7][1] = y-2;


        for (int i = 0; i < 8; i++) {



            Log.e("horse", (int)(tX - (width - squareSide * 8) / 2) / squareSide + " " +  (int) ((possibleSteps[i][0] - (width - 8 * squareSide) / 2) / squareSide + 1));
            Log.e("horse", (int) (tY - (height - squareSide * 8) / 2) / squareSide + " " + (int) ((possibleSteps[i][1] - (height - 8 * squareSide) / 2) / squareSide + 1));
            Log.e("horse","");


            if ((int)(tX - (width - squareSide * 8) / 2) / squareSide == possibleSteps[i][0]  && (int) (tY - (height - squareSide * 8) / 2) / squareSide ==possibleSteps[i][1]){

                switch (i){
                    case 0: return new int[]{2, 1};
                    case 1: return new int[]{2, -1};
                    case 2: return new int[]{1, 2};
                    case 3: return new int[]{1,-2};
                    case 4: return new int[]{-2,1};
                    case 5: return new int[]{-2,-1};
                    case 6: return new int[]{-1,2};
                    case 7: return new int[]{-1, -2};
                }
            }
        }

        return new int[]{0,0};
    }



    void playStepSound(){
        if (sounds) {
            sp.play(soundId, 1, 1, 0, 0, 1);
        }
    }

}
