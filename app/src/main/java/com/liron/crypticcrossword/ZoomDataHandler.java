package com.liron.crypticcrossword;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.almeros.android.multitouch.MoveGestureDetector;
import com.almeros.android.multitouch.RotateGestureDetector;

/**
 * Created by lir on 10/09/2016.
 */
public class ZoomDataHandler {
    private float mScaleFactor = 1.0f;
    private float mRotationDegrees = 0.f;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;

    private ScaleGestureDetector mScaleDetector;
    private RotateGestureDetector mRotateDetector;
    private MoveGestureDetector mMoveDetector;
    private View rootView;
    private boolean touchEnabled = false;

    public void create(Context context, final View rootView) {
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mScaleDetector.setQuickScaleEnabled(true);
        mRotateDetector = new RotateGestureDetector(context, new RotateListener());
        mMoveDetector = new MoveGestureDetector(context, new MoveListener());
        this.rootView = rootView;
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    if (touchEnabled) {
                        mScaleDetector.onTouchEvent(event);
                        mRotateDetector.onTouchEvent(event);
                        mMoveDetector.onTouchEvent(event);

                        rootView.setTranslationX(mFocusX);
                        rootView.setTranslationY(mFocusY);
                        rootView.setScaleX(mScaleFactor);
                        rootView.setScaleY(mScaleFactor);
                        rootView.setRotation(mRotationDegrees);
                    }
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                }
                return true;
            }
        });
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

    private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            mRotationDegrees -= detector.getRotationDegreesDelta();
            return true;
        }
    }

    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        @Override
        public boolean onMove(MoveGestureDetector detector) {
            PointF d = detector.getFocusDelta();
            mFocusX += d.x;
            mFocusY += d.y;
            return true;
        }
    }

}
