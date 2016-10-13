package com.liron.crypticcrossword;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

public class BlackPixelIdentifier {
    public static final int MAX_RGB = 120;
    public static final int AVERAGE_RGB = 100;


//    public List<Boolean> isBlackChildren(List<View> children, View root) {
//        Bitmap hotspots = getHotspots();
//        List<Point> locations = new ArrayList<>();
//        for (View child : children) {
//            locations.add(new Point(getXSample(child, root), getYSample(child, root)));
//        }
//        int touchColor = getHotspotColor(getXSample(view, root), getYSample(view, root));
//        return isBlack(touchColor);
//    }

    public static boolean isBlack(int touchColor) {
        return getAverageRGB(touchColor) < AVERAGE_RGB && getMaxRGB(touchColor) < MAX_RGB;
    }

    private static int getRelativeLeft(View myView, View root) {
        if (myView.getParent() == root) //myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent(), root);
    }

    private static int getRelativeTop(View myView, View root) {
        if (myView.getParent() == root) //myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent(), root);
    }

    public static int getYSample(View view, View root) {
        //int[] location = new int[2];
        //view.getLocationOnScreen(location);
        //return location[1] + view.getHeight() / 2;
        return (int) (getRelativeTop(view, root) + view.getPivotY());
    }

    public static int getXSample(View view, View root) {
        //       int[] location = new int[2];
        //       view.getLocationOnScreen(location);
        //       return location[0] + view.getRadius() / 2;
        return (int) (getRelativeLeft(view, root) + view.getPivotX());
    }

    public static int getMaxRGB(int touchColor) {
        return Math.max(Color.red(touchColor), Math.max(Color.green(touchColor), Color.blue(touchColor)));
    }

    public static int getAverageRGB(int touchColor) {
        return (Color.red(touchColor) + Color.green(touchColor) + Color.blue(touchColor)) / 3;
    }

    public static Bitmap getHotspots(View rootView) {
        rootView.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        return hotspots;
    }
}