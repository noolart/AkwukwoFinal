package com.noolart.surfaceviewtest;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MultiplayerDrawThread extends Thread {
    MultiplayerSurface mySurface;
    final SurfaceHolder surfaceHolder;
    boolean isRun = false;
    long nowTime, prevTime, elapsedTime;

    public MultiplayerDrawThread(MultiplayerSurface mySurface, SurfaceHolder surfaceHolder) {
        this.mySurface = mySurface;
        this.surfaceHolder = surfaceHolder;
        prevTime = System.currentTimeMillis();
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (isRun){
            if (!surfaceHolder.getSurface().isValid()){
                continue;
            }
            nowTime = System.currentTimeMillis();
            elapsedTime = nowTime - prevTime;
            if (elapsedTime > 50){
                prevTime = nowTime;
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder){
                    mySurface.draw(canvas);
                }
                if (canvas != null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

}