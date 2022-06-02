package com.example.surfaceviewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.widget.CalendarView;

public class DrawThread extends Thread {
    private SurfaceHolder surfaceHolder;

    private boolean isWorking = true;

    private float x;

    private Paint paint;

    boolean stage = true;

    boolean change = false;
    float newX = 0;

    boolean firstTouch = false;

    DrawThread(SurfaceHolder surfaceHolder, Context context) {

        this.surfaceHolder = surfaceHolder;

        paint = new Paint();
    }

    @Override
    public void run() {

        while (isWorking) {

            Canvas canvas = surfaceHolder.lockCanvas();

            if (!firstTouch) {
                x = canvas.getWidth() / 2;
                firstTouch = true;
            }

            if (change) {
                this.x = newX;
                change = false;
            }

            paint.setColor(stage ? Color.RED : Color.BLUE);
            canvas.drawRect(0,0, x, canvas.getHeight(), paint);

            paint.setColor(stage ? Color.BLUE : Color.RED);
            canvas.drawRect(x, 0, canvas.getWidth(), canvas.getHeight(), paint);

            stage = !stage;

            surfaceHolder.unlockCanvasAndPost(canvas);

            try {
                sleep(1000);
            } catch (InterruptedException e) {}

        }
    }

    void stopThread() {
        isWorking = false;
    }

    void touch(float x) {
        change = true;
        newX = x;
    }
}
