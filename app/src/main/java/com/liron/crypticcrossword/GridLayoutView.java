package com.liron.crypticcrossword;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.method.TextKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;

/**
 * Created by lir on 11/06/2016.
 */
public class GridLayoutView extends GridLayout {
    public static final int MAX_RGB = 100;
    public static final int AVERAGE_RGB = 65;
    private final Activity context;
    private float mPosX;
    private float mPosY;
    private float mScaleFactor = 1.f;

    public GridLayoutView(Activity context, int numOfCells, SquareView.SquareLocation location, SquareView squareView) {
        super(context);
        this.context = context;
        int numOfColumns = (int) Math.sqrt(numOfCells);
        int editTextWidth = Math.round(location.getWidth() / numOfColumns);
        int editTextHeight = Math.round(location.getHeight() / numOfColumns);
        for (int i = 0; i < numOfCells; i++) {
            EditText editText = new EditText(context);
            editText.setText(i + "");
            GridLayout.Spec row = GridLayout.spec(i / numOfColumns, 1, 1f);
            GridLayout.Spec col = GridLayout.spec(i % numOfColumns, 1, 1f);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
            params.width = editTextWidth;
            params.height = editTextHeight;
            editText.setBackgroundColor(Color.TRANSPARENT);
            editText.setBackgroundColor(Color.BLUE);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (isBlack(view)) {
                        view.setVisibility(View.INVISIBLE);
                        view.setFocusable(false);
                    }
                    if (hasFocus) {
                        EditText editText = (EditText) view;
                        TextKeyListener.clear(editText.getText());
                    }
                }
            });
            this.addView(editText, params);
        }
        FrameLayout parentLayout = (FrameLayout) context.findViewById(R.id.layout);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = parentLayout.getRight() - location.right;
        layoutParams.topMargin = location.bottom; // - parentLayout.getTop();
        System.out.println("sV Lef = " + squareView.getLeft());
        System.out.println("sV Top = " + squareView.getTop());


        parentLayout.addView(this, layoutParams);
//        parentLayout.addView(this);
//        layout(location.left, location.top, location.right, location.bottom);
        System.out.println("aaX " + squareView.getX());
        System.out.println("aaY " + squareView.getY());
        System.out.println("lY " + location.left);
        System.out.println("rY " + location.right);
        System.out.println("tY " + location.top);
        System.out.println("bY " + location.bottom);
        System.out.println("plY " + parentLayout.getLeft());
        System.out.println("prY " + parentLayout.getRight());
        System.out.println("ptY " + parentLayout.getTop());
        System.out.println("pbY " + parentLayout.getBottom());
//        setX((location.left + location.right) / 2);
//        setY((location.bottom + location.top) / 2);
//        requestLayout();

    }

    public boolean isBlack(View view) {
        int touchColor = getHotspotColor(R.id.layout, (int) (view.getX() + view.getWidth() / 2), (int) view.getY() + view.getHeight() / 2);
        return getAverageRGB(touchColor) < AVERAGE_RGB && getMaxRGB(touchColor) < MAX_RGB;
    }

    private int getMaxRGB(int touchColor) {
        return Math.max(Color.red(touchColor), Math.max(Color.green(touchColor), Color.blue(touchColor)));
    }

    private int getAverageRGB(int touchColor) {
        return (Color.red(touchColor) + Color.green(touchColor) + Color.blue(touchColor)) / 3;
    }

    public int getHotspotColor(int hotspotId, int x, int y) {
        FrameLayout layout = (FrameLayout) context.findViewById(hotspotId);
        layout.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(layout.getDrawingCache());
        layout.setDrawingCacheEnabled(false);
        return hotspots.getPixel(x, y);
    }

}
