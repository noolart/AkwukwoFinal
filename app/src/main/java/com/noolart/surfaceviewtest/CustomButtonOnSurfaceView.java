package com.noolart.surfaceviewtest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class CustomButtonOnSurfaceView {
    float leftX, rightX, upY, downY;
    Bitmap pictureOnButton;
    Rect src;
    Paint paint = new Paint();

    public  CustomButtonOnSurfaceView (float leftX, float rightX, float upY, float downY, Bitmap pictureOnButton, Rect src){
        this.leftX = leftX;
        this.rightX = rightX;
        this.upY = upY;
        this.downY = downY;
        this.pictureOnButton = pictureOnButton;
        this.src = src;

    }

    boolean isClicked (float tX, float tY){
        if (tX>leftX && tX<rightX && tY>upY && tY<downY){
            return true;
        }
        return false;
    }
    void drawButton (Canvas canvas){
        canvas.drawBitmap(pictureOnButton,src,new Rect ((int)leftX,(int)upY,(int)rightX,(int)downY),paint);
    }

}
