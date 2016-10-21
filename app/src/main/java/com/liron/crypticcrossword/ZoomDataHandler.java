package com.liron.crypticcrossword;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by lir on 10/09/2016.
 */
public class ZoomDataHandler {
    private static ZoomDataHandler instance = null;
    private float mScaleFactor = 1.0f;
    private float mRotationDegrees = 0.f;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;
    private ScaleGestureDetector mScaleDetector;
    private ImageView boardImage;
    private ViewGroup rootView;
    private boolean touchEnabled = false;
    private RotationGestureDetector mRotationDetector;
    private float dX = 0.f;
    private float dY = 0.f;
    private Point displayProperties;

    private ZoomDataHandler() {
    }

    public static ZoomDataHandler getInstance() {
        if (instance == null) {
            instance = new ZoomDataHandler();
        }
        return instance;
    }

    public float getRotationDegrees() {
        return mRotationDegrees;
    }

    public float getScaleFactor() {
        return mScaleFactor;
    }


    public float getFocusX() {
        return mFocusX;
    }

    public float getFocusY() {
        return mFocusY;
    }

    public void create(final Context context, final ViewGroup rootView, final ImageView boardImage) {
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mScaleDetector.setQuickScaleEnabled(true);
        mRotationDetector = new RotationGestureDetector(new RotationListener());
        this.boardImage = boardImage;
        this.rootView = rootView;
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchEnabled) {
                    mScaleDetector.onTouchEvent(event);
                    mRotationDetector.onTouchEvent(event);
                    onTranslationTouchEvent(event, rootView);

                    mScaleFactor = Math.max(mScaleFactor, 1);
                    rootView.setScaleX(mScaleFactor);
                    rootView.setScaleY(mScaleFactor);

                    rootView.setRotation(mRotationDegrees);

                    invalidateChildren();
                }
                return true;
            }
        });
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        displayProperties = new Point();
        display.getSize(displayProperties);
    }

    public void onTranslationTouchEvent(MotionEvent event, ViewGroup rootView) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                if (event.getPointerId(0) == 0 && event.getPointerCount() == 1) {
                    dX = rootView.getX() - event.getRawX();
                    dY = rootView.getY() - event.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerId(0) == 0) {
                    rootView.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                }
                if (event.getPointerCount() > 1) {
                    rootView.animate().x(
                            getCorrectPosition(boardImage.getWidth(), rootView.getWidth(), rootView.getX()))
                            .y(getCorrectPosition(boardImage.getHeight(), rootView.getHeight(), rootView.getY()))
                            .setDuration(500).start();
                }
                break;
            case MotionEvent.ACTION_UP:
                rootView.animate().x(
                        getCorrectPosition(boardImage.getWidth(), rootView.getWidth(), rootView.getX()))
                        .y(getCorrectPosition(boardImage.getHeight(), rootView.getHeight(), rootView.getY()))
                        .setDuration(500).start();

        }
    }

    public float getCorrectPosition(float imageProperty, float rootProperty, float rootCoordination) {
        float newLocation = rootCoordination;
        float imageOneSideMarginFromScreen = (imageProperty * mScaleFactor - rootProperty) / 2f;
        if ((mScaleFactor - 1) * imageProperty >= rootProperty - imageProperty) {
            if (rootCoordination < -imageOneSideMarginFromScreen) {
                newLocation = -imageOneSideMarginFromScreen;
            } else if (rootCoordination > imageOneSideMarginFromScreen) {
                newLocation = imageOneSideMarginFromScreen;
            }
        } else if (rootProperty - imageProperty > 0) {
            newLocation = 0;
        }

        return newLocation;
    }

    public void invalidateChildren() {
        for (int i = 0; i < rootView.getChildCount(); i++) {
            rootView.getChildAt(i).invalidate();
        }
    }

    public void enable() {
        touchEnabled = true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor(); // scale change since previous event
            return true;
        }
    }


    private class RotationListener implements RotationGestureDetector.OnRotationGestureListener {
        @Override
        public void onRotation(RotationGestureDetector rotationDetector) {
            mRotationDegrees -= rotationDetector.getAngle();
        }

        @Override
        public void onFinishRotation(RotationGestureDetector rotationDetector) {
        }

    }
}
