package com.liron.crypticcrossword;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by lir on 11/06/2016.
 */
public class ZoomableFrameLayout extends FrameLayout {
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 0.3f;

    private Drawable mIcon;
    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;

    public ZoomableFrameLayout(Context context) {
        this(context, null, 0);
    }

    public ZoomableFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomableFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mIcon = context.getResources().getDrawable(R.drawable.full_picture);
        mIcon.setBounds(0, 0, mIcon.getIntrinsicWidth(), mIcon.getIntrinsicHeight());

        // Create our ScaleGestureDetector
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(event);

        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = event.getX();
                final float y = event.getY();
                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = event.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = event.findPointerIndex(mActivePointerId);
                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
//                if (!mScaleDetector.isInProgress()) {
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    child.setX(child.getX() + dx);
                    child.setY(child.getY() + dy);
//                        child.setTranslationX(dx);
//                        child.setTranslationY(dy);
//                        child.invalidate();
                }
//                    setTranslationX(dx);
//                    setTranslationY(dy);
                setX(getX() + dx);
                setY(getY() + dy);
                invalidate();

//                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = event.getX(newPointerIndex);
                    mLastTouchY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    private PointF saveMidPoint(MotionEvent event) {
        PointF mid = new PointF();
        mid.x = (event.getX(0) + event.getX(1)) / 2;
        mid.y = (event.getY(0) + event.getY(1)) / 2;
        return mid;
    }

    private float fingersDistance(MotionEvent event) {
        return (float) Math.sqrt(Math.abs(
                event.getX(0) - event.getX(1))
                + Math.abs(event.getY(0) - event.getY(1)));
    }

//    @Override
//    public void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
////        canvas.save();
////        canvas.translate(mPosX, mPosY);
////        canvas.scale(mScaleFactor, mScaleFactor);
////        mIcon.draw(canvas);
//
//
////        for (int i = 0; i < getChildCount(); i++) {
////            View child = getChildAt(i);
////            child.setX(child.getX() + mPosX);
////            child.setY(child.getY() + mPosY);
//////            child.draw(canvas);
////            child.invalidate();
////        }
////        canvas.restore();
//    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.3f, Math.min(mScaleFactor, 5.0f));

//            if (event.getPointerCount() == 2) {
//                PointF midPoint = saveMidPoint(event);
//                setPivotX(midPoint.x);
//                setPivotY(midPoint.y);
//            }

            setScaleX(mScaleFactor);
            setScaleY(mScaleFactor);
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.setScaleX(mScaleFactor);
                child.setScaleY(mScaleFactor);
//                child.invalidate();
            }

//            invalidate();
            return true;
        }
    }
}

