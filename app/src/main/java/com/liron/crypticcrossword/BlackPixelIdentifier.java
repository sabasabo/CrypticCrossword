package com.liron.crypticcrossword;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

public class BlackPixelIdentifier {
    public static final int MAX_RGB = 150;
    public static final int AVERAGE_RGB = 90;
    private static final int IMAGE_LAYOUT_ID = R.id.layout;
    private final Activity context;

    public BlackPixelIdentifier(Activity context) {
        this.context = context;
    }

    public boolean isBlack(View view, View root) {
        int touchColor = getHotspotColor(getXSample(view, root), getYSample(view, root));
        return getAverageRGB(touchColor) < AVERAGE_RGB && getMaxRGB(touchColor) < MAX_RGB;
    }

    private int getRelativeLeft(View myView, View root) {
        if (myView.getParent() == root) //myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent(), root);
    }

    private int getRelativeTop(View myView, View root) {
        if (myView.getParent() == root) //myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent(), root);
    }

    private int getYSample(View view, View root) {
        //int[] location = new int[2];
        //view.getLocationOnScreen(location);
        //return location[1] + view.getHeight() / 2;
        return (int) (getRelativeTop(view, root) + view.getPivotY());
    }

    private int getXSample(View view, View root) {
        //       int[] location = new int[2];
        //       view.getLocationOnScreen(location);
        //       return location[0] + view.getWidth() / 2;
        return (int) (getRelativeLeft(view, root) + view.getPivotX());
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