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


import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Random;

class GameSurface extends SurfaceView implements SurfaceHolder.Callback, SoundPool.OnLoadCompleteListener {



    float k, width, height;
    float x, y, iX, iY, tX = 0, tY = 0, homeX, homeY;
    Bitmap image;
    static public Resources res;
    Paint paint;
    int fromWhere, airplaneX, airplaneY, lighthouseX, lighthouseY, lighthouseCounter=0, coinCounter=0, stepCounter=0, animationSpeed = 54, squareSide;
    static Random r = new Random();
    Paint smallFontPaint ,fontPaint, bigFontPaint, giveUpFontPaint;
    public Typeface font;
    CustomControlKnobOnSurfaceView customControlKnobOnSurfaceView;
    CustomButtonOnSurfaceView homeButton, helpButton;

    final String LOG_TAG = "myLogs";
    final int MAX_STREAMS = 5;

    SoundPool sp;

    int soundId;






    DrawThread drawThread;


    float hi, wi;
    boolean isFirst = true, earthquake=false, hasMove=true, airplane=false, lighthouse=false, haveCoin=false, horse=false, locked=false, sounds, end;
    public static GameMap gameMap;

    ArrayList<Integer> earthQuakeArrayList;


    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public GameSurface(Context context) {
        super(context);
        getHolder().addCallback(this);



        homeX = 0;
        homeY = 7;
        k = 20;
        res = getResources();
        image = BitmapFactory.decodeResource(res, R.drawable.main);

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

            x = (width-8*squareSide)/2+(squareSide-wi)/2;
            y = (height-8*squareSide)/2+8*squareSide-hi-(squareSide-hi)/2;

            stepCounter=0;
            coinCounter=0;

            iX = (width-8*squareSide)/2+(squareSide-wi)/2;
            iY = (height-8*squareSide)/2+8*squareSide-hi-(squareSide-hi)/2;

            gameMap = new GameMap((int) width, (int) height, res);
            isFirst = false;
            earthQuakeArrayList = new ArrayList<>();

            customControlKnobOnSurfaceView = new CustomControlKnobOnSurfaceView(squareSide,2,(int) (width/2-1.5*squareSide),(float)((height-8*squareSide)*0.625+7.5*squareSide+20), res);
            homeButton = new CustomButtonOnSurfaceView(customControlKnobOnSurfaceView.right.leftX,customControlKnobOnSurfaceView.right.rightX,height/8-(float)squareSide/2,height/8+(float)squareSide/2,BitmapFactory.decodeResource(res, R.drawable.start),null);
            helpButton = new CustomButtonOnSurfaceView(customControlKnobOnSurfaceView.right.rightX + 10,customControlKnobOnSurfaceView.right.rightX+10 +squareSide,height/8-(float)squareSide/2,height/8+(float)squareSide/2,BitmapFactory.decodeResource(res, R.drawable.info),null);

        }

        if (isDone(gameMap.textures)) {
            endGame();
        }


        int arrIndex = gameMap.textures[(int)(iY-(height-8*squareSide)/2)/squareSide][(int)(iX-(width-8*squareSide)/2)/squareSide];


        if (arrIndex!=10){
            hasMove=true;
        }

        if (arrIndex==0) {
            int random = r.nextInt(gameMap.cardsOnAllField.size());
            gameMap.textures[(int) (iY - (height - 8 * squareSide) / 2) / squareSide][(int) (iX - (width - 8 * squareSide) / 2) / squareSide] = gameMap.cardsOnAllField.remove(random);

        }
        else if (arrIndex==1 && !(iY-squareSide<(height - 8 * squareSide) / 2) && !locked){
            iY -= squareSide;
            fromWhere=1;
        }
        else if (arrIndex==2 && !(iY+squareSide>(height - 8 * squareSide) / 2+8*squareSide) && !locked){
            iY += squareSide;
            fromWhere=4;
        }
        else if (arrIndex==3 && !(iX-squareSide< (width - 8 * squareSide) / 2)&& !locked){
            iX -= squareSide;
            fromWhere=2;
        }
        else if (arrIndex==4 && !(iX+squareSide> (width - 8 * squareSide) / 2+8*squareSide)&& !locked){
            iX += squareSide;
            fromWhere=3;
        }
        else if (arrIndex==5 && !locked){

            iX = (width-864)/2+homeX*squareSide+18;
            iY = (height-864)/2+homeY*squareSide+18;
        }
        else if (arrIndex==6 && !locked){
            while (!(iX+squareSide> (width - 8 * squareSide) / 2+8*squareSide)){
                iX+=squareSide;
            }
            fromWhere=0;
            hasMove=true;
        }
        else if (arrIndex==7 && !locked){
            while (!(iX-squareSide< (width - 8 * squareSide) / 2)){
                iX-=squareSide;
            }
            fromWhere=0;
        }
        else if (arrIndex==8 && !locked){
            if (fromWhere==1 && !(iY-squareSide<(height - 8 * squareSide) / 2)){
                iY-=squareSide;
            }
            else if (fromWhere==2 && !(iX-squareSide< (width - 8 * squareSide) / 2)){
                iX-=squareSide;
            }
            else if (fromWhere==3 && !(iX+squareSide> (width - 8 * squareSide) / 2+8*squareSide)){
                iX+=squareSide;
            }
            else if (fromWhere==4 && !(iY+squareSide>(height - 8 * squareSide) / 2+8*squareSide)){
                iY+=squareSide;
            }
            else if (fromWhere==5 && !(iY-squareSide<(height - 8 * squareSide) / 2) && !(iX+squareSide> (width - 8 * squareSide) / 2+8*squareSide)){
                iY-=squareSide;
                iX+=squareSide;
            }
            else if (fromWhere==6 && !(iY+squareSide>(height - 8 * squareSide) / 2+8*squareSide) && !(iX+squareSide> (width - 8 * squareSide) / 2+8*squareSide)){
                iY+=squareSide;
                iX+=squareSide;
            }
            else if (fromWhere==7 && !(iY+squareSide>(height - 8 * squareSide) / 2+8*squareSide) && !(iX-squareSide< (width - 8 * squareSide) / 2)){
                iY+=squareSide;
                iX-=squareSide;
            }
            else if (fromWhere==8 && !(iY-squareSide<(height - 8 * squareSide) / 2) && !(iX-squareSide< (width - 8 * squareSide) / 2)){
                iY-=squareSide;
                iX-=squareSide;
            }
        }
        else if (arrIndex==9 && !locked){
            if(haveCoin){
                haveCoin=false;
            }
            iX = (width-864)/2+homeX*squareSide+18;
            iY = (height-864)/2+homeY*squareSide+18;
        }
        else if (arrIndex==10 && hasMove && !locked){
            earthquake = true;
            hasMove=false;
        }
        else if (arrIndex==11 && !locked){
            fromWhere=0;
            airplane=true;
            airplaneX = (int)(iY-(height-8*squareSide)/2)/squareSide;
            airplaneY = (int)(iX-(width-8*squareSide)/2)/squareSide;
        }
        else if (arrIndex==12 && !locked){
            lighthouse=true;
            lighthouseX = (int)(iY-(height-8*squareSide)/2)/squareSide;
            lighthouseY = (int)(iX-(width-8*squareSide)/2)/squareSide;
        }
        else if (arrIndex==20 && !locked){
            horse = true;
        }
        else if (arrIndex==21 && !(iY-squareSide<(height - 8 * squareSide) / 2) && !(iX+squareSide> (width - 8 * squareSide) / 2+8*squareSide) && !locked){
            iY-=squareSide;
            iX+=squareSide;
            fromWhere=5;
        }
        else if (arrIndex==22 && !(iY+squareSide>(height - 8 * squareSide) / 2+8*squareSide) && !(iX+squareSide> (width - 8 * squareSide) / 2+8*squareSide) && !locked){
            iY+=squareSide;
            iX+=squareSide;
            fromWhere=6;
        }
        else if (arrIndex==23 && !(iY+squareSide>(height - 8 * squareSide) / 2+8*squareSide) && !(iX-squareSide< (width - 8 * squareSide) / 2) && !locked){
            iY+=squareSide;
            iX-=squareSide;
            fromWhere=7;
        }
        else if (arrIndex==24 && !(iY-squareSide<(height - 8 * squareSide) / 2) && !(iX-squareSide< (width - 8 * squareSide) / 2) && !locked){
            iY-=squareSide;
            iX-=squareSide;
            fromWhere=8;
        }

        else if (arrIndex==63 && !locked){
            if(haveCoin){
                haveCoin=false;
                coinCounter++;
            }
        }

        gameMap.draw(canvas);

        canvas.drawText("Coins: " + coinCounter,(width-squareSide*8)/2,(int)((height-squareSide*8)/4*0.85-54),fontPaint);
        canvas.drawText("Steps: " + stepCounter,(width-squareSide*8)/2,(int)((height-squareSide*8)/4*1.15-54),fontPaint);

        if ((arrIndex>=10 && arrIndex<=20 && arrIndex!=13) || arrIndex==63) {
            canvas.drawBitmap(gameMap.images[gameMap.textures[(int) (iY - (height - 8 * squareSide) / 2) / squareSide][(int) (iX - (width - 8 * squareSide) / 2) / squareSide]], (width - 8 * squareSide) / 2, (int) ((height - 8 * squareSide) / 4 * 1.2f), paint);
            float m = 1.35f;
            for (String s : gameMap.descriptions[arrIndex].split("-")) {
                canvas.drawText(s, width / 4 + squareSide, (int) ((height - 8 * squareSide) / 4) * m, smallFontPaint);
                m+=0.2f;
            }
        }
        else if (haveCoin){
            canvas.drawBitmap(gameMap.images[60], (width - 8 * squareSide) / 2, (int) ((height - 8 * squareSide) / 4 * 1.2f), paint);
            canvas.drawText("Унеси монету на базу!", width / 4 + squareSide, (int) ((height - 8 * squareSide) / 4) * 1.55f, smallFontPaint);
        }

        if (iX-x!=0 || iY-y!=0) {
            if (iX - x > 0) {
                x += animationSpeed;
            }
            if (iX - x < 0) {
                x -= animationSpeed;
            }
            if (iY - y > 0) {
                y += animationSpeed;
            }
            if (iY - y < 0) {
                y -= animationSpeed;
            }
            locked = true;
        }

        else {
            locked = false;
        }



        canvas.drawBitmap(image, null, new Rect((int)x,(int)y,(int)x+72,(int)y+72), paint);
        customControlKnobOnSurfaceView.drawControlKnob(canvas,true);
        homeButton.drawButton(canvas);
        helpButton.drawButton(canvas);

        if (haveCoin){
            canvas.drawBitmap(gameMap.images[60], null, new Rect((int)x,(int)y+36,(int)x+36,(int)y+72), paint);
        }

        if (end){
            drawTextOnScreen(coinCounter*20-stepCounter + "", Color.WHITE, canvas);
        }




    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        tX = event.getX();
        tY = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN){

            if (homeButton.isClicked(tX, tY)){
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
            if (helpButton.isClicked(tX,tY)){
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





            if (earthquake && !locked){


                if (0<=(int)((tX-(width-squareSide*8)/2)/squareSide)&&(int)((tX-(width-squareSide*8)/2)/squareSide)<=7 && 0<=(int) ((tY - (height - squareSide * 8) / 2) / squareSide)&& (int) ((tY - (height - squareSide * 8) / 2) / squareSide)<=7) {
                    earthQuakeArrayList.add((int) ((tY - (height - squareSide * 8) / 2) / squareSide));
                    earthQuakeArrayList.add((int) ((tX - (width - squareSide * 8) / 2) / squareSide));
                }

                if (earthQuakeArrayList.size()==4){
                    if (earthQuakeArrayList.get(0)==homeY && earthQuakeArrayList.get(1)==homeX){
                        homeY=earthQuakeArrayList.get(2);
                        homeX=earthQuakeArrayList.get(3);
                    }
                    else if (earthQuakeArrayList.get(2)==homeY && earthQuakeArrayList.get(3)==homeX){
                        homeY=earthQuakeArrayList.get(0);
                        homeX=earthQuakeArrayList.get(1);
                    }
                    int a = gameMap.textures[earthQuakeArrayList.get(0)][earthQuakeArrayList.get(1)];
                    gameMap.textures[earthQuakeArrayList.get(0)][earthQuakeArrayList.get(1)] = gameMap.textures[earthQuakeArrayList.get(2)][earthQuakeArrayList.get(3)];
                    gameMap.textures[earthQuakeArrayList.get(2)][earthQuakeArrayList.get(3)] = a;
                    earthQuakeArrayList.clear();
                    earthquake=false;
                }
            }
            else if (airplane && !locked){
                if (0<=(int)((tX-(width-squareSide*8)/2)/squareSide)&&(int)((tX-(width-squareSide*8)/2)/squareSide)<=7 && 0<=(int) ((tY - (height - squareSide * 8) / 2) / squareSide)&& (int) ((tY - (height - squareSide * 8) / 2) / squareSide)<=7) {
                    iY = (height - 864) / 2 + ((int) ((tY - (height - squareSide * 8) / 2) / squareSide)) * squareSide + 18;
                    iX = (width - 864) / 2 + ((int) ((tX - (width - squareSide * 8) / 2) / squareSide)) * squareSide + 18;
                    gameMap.textures[airplaneX][airplaneY] = 61;
                    airplane = false;
                }
            }
            else if (lighthouse && !locked){

                if (0<=(int)((tX-(width-squareSide*8)/2)/squareSide)&&(int)((tX-(width-squareSide*8)/2)/squareSide)<=7 && 0<=(int) ((tY - (height - squareSide * 8) / 2) / squareSide)&& (int) ((tY - (height - squareSide * 8) / 2) / squareSide)<=7) {
                    if(gameMap.textures[(int) ((tY - (height - squareSide * 8) / 2) / squareSide)][(int) ((tX - (width - squareSide * 8) / 2) / squareSide)]==0) {
                        int random = r.nextInt(gameMap.cardsOnAllField.size());
                        gameMap.textures[(int) (tY - (height - 8 * squareSide) / 2) / squareSide][(int) (tX - (width - 8 * squareSide) / 2) / squareSide] = gameMap.cardsOnAllField.remove(random);
                        lighthouseCounter++;
                    }
                    if (lighthouseCounter == 4){
                        lighthouseCounter=0;
                        gameMap.textures[lighthouseX][lighthouseY]=62;
                        lighthouse=false;
                    }
                }
            }
            else if (horse && !locked) {

                int[] side = horseStep(iX, iY, tX, tY);


                iX += side[0]*squareSide;
                iY += side[1]*squareSide;

                if (side[0]!=0) {
                    horse = false;
                }

            }
            else {
                if (!locked) {
                    if (!(iY - squareSide < (height - 8 * squareSide) / 2) && customControlKnobOnSurfaceView.up.isClicked(tX, tY)) {
                        iY -= squareSide;
                        fromWhere = 1;
                        hasMove = true;
                        stepCounter++;


                        playStepSound();


                    }
                    if (!(iX - squareSide < (width - 8 * squareSide) / 2) && customControlKnobOnSurfaceView.left.isClicked(tX, tY)) {
                        iX -= squareSide;
                        fromWhere = 2;
                        hasMove = true;
                        stepCounter++;

                        playStepSound();

                    }
                    if (!(iX + squareSide > (width - 8 * squareSide) / 2 + 8 * squareSide) && customControlKnobOnSurfaceView.right.isClicked(tX, tY)) {
                        iX += squareSide;
                        fromWhere = 3;
                        hasMove = true;
                        stepCounter++;

                        playStepSound();

                    }
                    if (!(iY + squareSide > (height - 8 * squareSide) / 2 + 8 * squareSide) && customControlKnobOnSurfaceView.down.isClicked(tX, tY)) {
                        iY += squareSide;
                        fromWhere = 4;
                        hasMove = true;
                        stepCounter++;

                        playStepSound();

                    }
                    if (!haveCoin && customControlKnobOnSurfaceView.mid.isClicked(tX,tY) && gameMap.textures[(int) (iY - (height - 8 * squareSide) / 2) / squareSide][(int) (iX - (width - 8 * squareSide) / 2) / squareSide] >= 14 && gameMap.textures[(int) (iY - (height - 8 * squareSide) / 2) / squareSide][(int) (iX - (width - 8 * squareSide) / 2) / squareSide] <= 18) {
                        haveCoin = true;
                        gameMap.textures[(int) (iY - (height - 8 * squareSide) / 2) / squareSide][(int) (iX - (width - 8 * squareSide) / 2) / squareSide] -= 1;
                        hasMove = true;
                        stepCounter++;
                    }
                }
            }
        }

        return true;
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(this, getHolder());
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

    void playStepSound(){
        if (sounds) {
            sp.play(soundId, 1, 1, 0, 0, 1);
        }
    }



    int[] horseStep(float X, float Y, float tX, float tY){

        int[][] possibleSteps = new int[8][2];

        int x = (int)(X - (width - squareSide * 8) / 2) / squareSide;
        int y= (int)(Y - (height - squareSide * 8)/ 2 ) / squareSide;

        possibleSteps[0][0] = x+2;
        possibleSteps[0][1] = y+1;


        possibleSteps[1][0] =x+2;
        possibleSteps[1][1] =y-1;


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

        end = true;
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getContext(), StartActivity.class);

            SharedPreferences sp = getContext().getSharedPreferences("storage", Context.MODE_PRIVATE);
            int best = sp.getInt("best",0);
            if (best < coinCounter*20 - stepCounter) {

                SharedPreferences.Editor ed = getContext().getSharedPreferences("storage", Context.MODE_PRIVATE).edit();
                ed.putInt("coins", coinCounter);
                ed.putInt("best", coinCounter * 20 - stepCounter);
                ed.apply();

            }







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
        texOnScreen.setTextSize(100);
        canvas.drawRect(squareSide,(float)getHeight()/2-squareSide-5,9*squareSide,(float)getHeight()/2+squareSide+5, new Paint(Color.BLACK));
        canvas.drawText("your score: " + text, squareSide,(float)(canvas.getHeight()/2+squareSide/2),texOnScreen);
    }

}


