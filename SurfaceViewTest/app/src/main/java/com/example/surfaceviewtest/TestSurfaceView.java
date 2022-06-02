package com.example.surfaceviewtest;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class TestSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private DrawThread drawThread;

    public TestSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(holder, getContext());
        drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.stopThread();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        drawThread.touch(event.getX());
        return true;
    }
}