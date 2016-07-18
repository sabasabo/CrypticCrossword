package com.liron.crypticcrossword;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

public class BlackPixelIdentifier {
    public static final int MAX_RGB = 170;
    public static final int AVERAGE_RGB = 110;
    private static final int IMAGE_LAYOUT_ID = R.id.layout;
    private final GridLayoutView gridLayoutView;
    private final Activity context;

    public BlackPixelIdentifier(GridLayoutView gridLayoutView, Activity context) {
        this.gridLayoutView = gridLayoutView;
        this.context = context;
    }

    public boolean isBlack(View view) {
        int touchColor = getHotspotColor(getXSample(view), getYSample(view));
        return getAverageRGB(touchColor) < AVERAGE_RGB && getMaxRGB(touchColor) < MAX_RGB;
    }

    private int getYSample(View view) {
        //int[] location = new int[2];
        //view.getLocationOnScreen(location);
        //return location[1] + view.getHeight() / 2;
        return (int) view.getY() + 20;
    }

    private int getXSample(View view) {
        //       int[] location = new int[2];
        //       view.getLocationOnScreen(location);
        //       return location[0] + view.getWidth() / 2;
        return (int) view.getX() + 20;
    }

    public int getMaxRGB(int touchColor) {
        return Math.max(Color.red(touchColor), Math.max(Color.green(touchColor), Color.blue(touchColor)));
    }

    public int getAverageRGB(int touchColor) {
        return (Color.red(touchColor) + Color.green(touchColor) + Color.blue(touchColor)) / 3;
    }

    public int getHotspotColor(int x, int y) {
        FrameLayout layout = (FrameLayout) context.findViewById(IMAGE_LAYOUT_ID);
        layout.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(layout.getDrawingCache());
        layout.setDrawingCacheEnabled(false);
        return hotspots.getPixel(x, y);
    }
}