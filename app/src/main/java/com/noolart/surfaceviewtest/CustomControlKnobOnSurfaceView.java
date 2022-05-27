package com.noolart.surfaceviewtest;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class CustomControlKnobOnSurfaceView {

    final int color;
    int squareSide;
    float x , y, gap;
    CustomButtonOnSurfaceView up, left, mid, right, down, turn1 , turn2, turn3, turn4;

    public CustomControlKnobOnSurfaceView (int squareSide, int color, float x, float y, Resources resources){
        this.squareSide = squareSide;
        this.color = color;
        this.x = x;
        this.y = y;
        gap = (float)squareSide/12;
        up = new CustomButtonOnSurfaceView(x+squareSide+gap,x+2*squareSide+gap,y,y+squareSide, BitmapFactory.decodeResource(resources, R.drawable.strup), null);
        left = new CustomButtonOnSurfaceView(x,x+squareSide,y+squareSide+gap,y+2*squareSide+gap, BitmapFactory.decodeResource(resources, R.drawable.strleft), null);
        mid = new CustomButtonOnSurfaceView(x+squareSide+gap,x+2*squareSide+gap,y+squareSide+gap,y+2*squareSide+gap, BitmapFactory.decodeResource(resources, R.drawable.texture), null);
        right = new CustomButtonOnSurfaceView(x+2*squareSide+2*gap,x+3*squareSide+2*gap,y+squareSide+gap,y+2*squareSide+gap, BitmapFactory.decodeResource(resources, R.drawable.strright), null);
        down = new CustomButtonOnSurfaceView(x+squareSide+gap,x+2*squareSide+gap,y+2*squareSide+2*gap,y+3*squareSide+2*gap, BitmapFactory.decodeResource(resources, R.drawable.strdown), null);

        if (color == 0) {
                turn1 = new CustomButtonOnSurfaceView(x, x + squareSide, y, y + squareSide, BitmapFactory.decodeResource(resources, R.drawable.blue_turn), null);
                turn2 = new CustomButtonOnSurfaceView(x, x + squareSide, y + 2 * squareSide + 2 * gap, y + 3 * squareSide + 2 * gap, BitmapFactory.decodeResource(resources, R.drawable.blue_turn), null);
                turn3 = new CustomButtonOnSurfaceView(x + 2 * squareSide + 2 * gap, x + 3 * squareSide + 2 * gap, y, y + squareSide, BitmapFactory.decodeResource(resources, R.drawable.blue_turn), null);
                turn4 = new CustomButtonOnSurfaceView(x + 2 * squareSide + 2 * gap, x + 3 * squareSide + 2 * gap, y + 2 * squareSide + 2 * gap, y + 3 * squareSide + 2 * gap, BitmapFactory.decodeResource(resources, R.drawable.blue_turn), null);
        }
        else if (color == 1){
                turn1 = new CustomButtonOnSurfaceView(x, x + squareSide, y, y + squareSide, BitmapFactory.decodeResource(resources, R.drawable.red_turn), null);
                turn2 = new CustomButtonOnSurfaceView(x, x + squareSide, y + 2 * squareSide + 2 * gap, y + 3 * squareSide + 2 * gap, BitmapFactory.decodeResource(resources, R.drawable.red_turn), null);
                turn3 = new CustomButtonOnSurfaceView(x + 2 * squareSide + 2 * gap, x + 3 * squareSide + 2 * gap, y, y + squareSide, BitmapFactory.decodeResource(resources, R.drawable.red_turn), null);
                turn4 = new CustomButtonOnSurfaceView(x + 2 * squareSide + 2 * gap, x + 3 * squareSide + 2 * gap, y + 2 * squareSide + 2 * gap, y + 3 * squareSide + 2 * gap, BitmapFactory.decodeResource(resources, R.drawable.red_turn), null);
        }
        else {
                turn1 = new CustomButtonOnSurfaceView(x, x + squareSide, y, y + squareSide, BitmapFactory.decodeResource(resources, R.drawable.green_turn), null);
                turn2 = new CustomButtonOnSurfaceView(x, x + squareSide, y + 2 * squareSide + 2 * gap, y + 3 * squareSide + 2 * gap, BitmapFactory.decodeResource(resources, R.drawable.green_turn), null);
                turn3 = new CustomButtonOnSurfaceView(x + 2 * squareSide + 2 * gap, x + 3 * squareSide + 2 * gap, y, y + squareSide, BitmapFactory.decodeResource(resources, R.drawable.green_turn), null);
                turn4 = new CustomButtonOnSurfaceView(x + 2 * squareSide + 2 * gap, x + 3 * squareSide + 2 * gap, y + 2 * squareSide + 2 * gap, y + 3 * squareSide + 2 * gap, BitmapFactory.decodeResource(resources, R.drawable.green_turn), null);
        }

    }

    public void drawControlKnob(Canvas canvas, boolean flag){
        up.drawButton(canvas);
        left.drawButton(canvas);
        mid.drawButton(canvas);
        right.drawButton(canvas);
        down.drawButton(canvas);
        if (flag){
            turn1.drawButton(canvas);
            turn2.drawButton(canvas);
            turn3.drawButton(canvas);
            turn4.drawButton(canvas);
        }
    }

    public boolean isClicked(float tX, float tY){
        return tX > x && tX < x + 3 * squareSide + 2 * gap && tY > y && tY < y + 3 * squareSide + 2 * gap;
    }










}
