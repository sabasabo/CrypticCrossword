package com.liron.crypticcrossword;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TextImageView extends ImageView {


    public TextImageView(Context context) {
        super(context);
    }

    public TextImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(getWidth() / 6.5f);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(20f, 2f, 2f, Color.BLUE);
        canvas.drawText(((String) getTag()).split("-")[1], getPivotX(), getPivotY() + paint.getTextSize() / 4f, paint);
    }
}