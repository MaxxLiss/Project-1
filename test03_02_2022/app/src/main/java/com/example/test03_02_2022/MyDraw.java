package com.example.test03_02_2022;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyDraw extends View {

    public MyDraw(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        for (int i = 1; i < 5; i++)
            canvas.drawRect(100, 100 * i, 100 * i, 100 + 100 * i, paint);
    }
}
