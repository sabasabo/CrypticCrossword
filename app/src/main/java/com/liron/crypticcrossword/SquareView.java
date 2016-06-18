package com.liron.crypticcrossword;

/**
 * Created by lir on 13/05/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class SquareView extends View {

    public static final int DEFAULT_BALLS_DISTANCE = 200;
    public static final String RECTANGLE_EDGE_COLOR = "#AADB1255";
    public static final String RECTANGLE_AREA_COLOR = "#55DB1255";
    /**
     * point1 and point 3 are of same group and same as point 2 and point4
     */
    // variable to know what ball is being dragged
    Paint paint;
    Canvas canvas;
    private Point[] points = new Point[4];
    private ArrayList<ColorBall> colorBalls = new ArrayList<ColorBall>();
    // array that holds the balls
    private int balID = 0;

    public SquareView(Context context) {
        super(context);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events
        canvas = new Canvas();
    }

//    public DrawView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    public DrawView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        paint = new Paint();
//        setFocusable(true); // necessary for getting the touch events
//        canvas = new Canvas();
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (points[3] == null) { //point4 null when user did not touch and move on screen.
            return;
        }
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(15);

        //draw stroke
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(RECTANGLE_EDGE_COLOR));
        paint.setStrokeWidth(6);
        Path path = new Path();
        final ColorBall colorBall1 = colorBalls.get(0);
        final ColorBall colorBall2 = colorBalls.get(1);
        final ColorBall colorBall3 = colorBalls.get(2);
        final ColorBall colorBall4 = colorBalls.get(3);
        path.moveTo(colorBall1.getXCenter(), colorBall1.getYCenter());
        path.lineTo(colorBall2.getXCenter(), colorBall2.getYCenter());
        path.lineTo(colorBall3.getXCenter(), colorBall3.getYCenter());
        path.lineTo(colorBall4.getXCenter(), colorBall4.getYCenter());
        path.close();
        canvas.drawPath(path, paint);
//        canvas.drawRect(
//                left + colorBalls.get(0).getRadiusOfBall(),
//                top + colorBalls.get(0).getRadiusOfBall(),
//                right + colorBalls.get(2).getRadiusOfBall(),
//                bottom + colorBalls.get(2).getRadiusOfBall(), paint);

//        canvas.drawLine(colorBall1.getXCenter(), colorBall4.getYCenter(), colorBall2.getXCenter(), colorBall2.getYCenter(), paint);
//        canvas.drawLine(colorBall2.getXCenter(), colorBall4.getYCenter(), colorBall3.getXCenter(), colorBall3.getYCenter(), paint);
//        canvas.drawLine(colorBall3.getXCenter(), colorBall4.getYCenter(), colorBall4.getXCenter(), colorBall4.getYCenter(), paint);
//        canvas.drawLine(colorBall4.getXCenter(), colorBall4.getYCenter(), colorBall1.getXCenter(), colorBall1.getYCenter(), paint);
        //fill the rectangle
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.parseColor(RECTANGLE_AREA_COLOR));
//        paint.setStrokeWidth(0);
//        canvas.drawRect(
//                left + colorBalls.get(0).getRadiusOfBall(),
//                top + colorBalls.get(0).getRadiusOfBall(),
//                right + colorBalls.get(2).getRadiusOfBall(),
//                bottom + colorBalls.get(2).getRadiusOfBall(), paint);

        //draw the corners
        BitmapDrawable bitmap = new BitmapDrawable();
        // draw the balls on the canvas
        paint.setColor(Color.BLUE);
        paint.setTextSize(18);
        paint.setStrokeWidth(0);
        for (int i = 0; i < colorBalls.size(); i++) {
            ColorBall ball = colorBalls.get(i);
            canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                    paint);

            canvas.drawText("" + (i + 1), ball.getX(), ball.getY(), paint);
        }
    }

    // events when touching the screen
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN: // touch down so check if the finger is on
                balID = -1;
                for (int i = colorBalls.size() - 1; i >= 0; i--) {
                    ColorBall ball = colorBalls.get(i);

                    paint.setColor(Color.CYAN);
                    if (userTouchedTheBall(touchX, touchY, ball)) {
                        balID = ball.getID();
                        invalidate();
                        break;
                    }
                    invalidate();
                }
//                }
                break;
            case MotionEvent.ACTION_MOVE: // touch drag with the ball
                if (balID > -1) {
                    // move the balls the same as the finger
                    final ColorBall colorBall = colorBalls.get(balID);
                    colorBall.setX(touchX - colorBall.getRadiusOfBall());
                    colorBall.setY(touchY - colorBall.getRadiusOfBall());

                    paint.setColor(Color.CYAN);
//                    moveBallsToPreserveRectangle();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                // touch drop - just do things here after dropping
                break;
        }
        invalidate();
        return true;

    }

    private boolean userTouchedTheBall(int touchX, int touchY, ColorBall ball) {
        int centerX = ball.getX() + ball.getRadiusOfBall();
        int centerY = ball.getY() + ball.getRadiusOfBall();
        return Math.abs(centerX - touchX) < ball.getRadiusOfBall() && Math.abs(centerY - touchY) < ball.getRadiusOfBall();
    }

//    private void moveBallsToPreserveRectangle() {
//        if (groupId == 1) {
//            colorBalls.get(1).setX(colorBalls.get(0).getX());
//            colorBalls.get(1).setY(colorBalls.get(2).getY());
//            colorBalls.get(3).setX(colorBalls.get(2).getX());
//            colorBalls.get(3).setY(colorBalls.get(0).getY());
//        } else {
//            colorBalls.get(0).setX(colorBalls.get(1).getX());
//            colorBalls.get(0).setY(colorBalls.get(3).getY());
//            colorBalls.get(2).setX(colorBalls.get(3).getX());
//            colorBalls.get(2).setY(colorBalls.get(1).getY());
//        }
//    }

    public void initBalls(int touchX, int touchY) {
        points[0] = new Point();
        points[0].x = touchX - DEFAULT_BALLS_DISTANCE / 2;
        points[0].y = touchY - DEFAULT_BALLS_DISTANCE / 2;

        points[1] = new Point();
        points[1].x = touchX - DEFAULT_BALLS_DISTANCE / 2;
        points[1].y = touchY + DEFAULT_BALLS_DISTANCE / 2;

        points[2] = new Point();
        points[2].x = touchX + DEFAULT_BALLS_DISTANCE / 2;
        points[2].y = touchY + DEFAULT_BALLS_DISTANCE / 2;

        points[3] = new Point();
        points[3].x = touchX + DEFAULT_BALLS_DISTANCE / 2;
        points[3].y = touchY - DEFAULT_BALLS_DISTANCE / 2;

        balID = 2;
        // declare each ball with the ColorBall class
        for (Point point : points) {
            colorBalls.add(new ColorBall(getContext(), R.drawable.circle, point));
        }
    }

    public SquareLocation getGridDestination() {
        int left, right, top, bottom;
        left = right = colorBalls.get(0).getXCenter();
        top = bottom = colorBalls.get(0).getYCenter();
        for (ColorBall colorBall : colorBalls) {
            left = Math.min(left, colorBall.getXCenter());
            right = Math.max(right, colorBall.getXCenter());
            bottom = Math.min(bottom, colorBall.getYCenter());
            top = Math.max(top, colorBall.getYCenter());
        }
        return new SquareLocation(left, right, top, bottom);
    }

    public static class ColorBall {

        private static int count = 0;
        private Bitmap bitmap;
        private Context mContext;
        private Point point;
        private int id;

        public ColorBall(Context context, int resourceId, Point point) {
            id = count++;
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    resourceId);
            mContext = context;
            this.point = point;
        }

        public int getXCenter() {
            return point.x + getRadiusOfBall();
        }

        public int getYCenter() {
            return point.y + getRadiusOfBall();
        }

        public int getRadiusOfBall() {
            return bitmap.getWidth() / 2;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public int getX() {
            return point.x;
        }

        public void setX(int x) {
            point.x = x;
        }

        public int getY() {
            return point.y;
        }

        public void setY(int y) {
            point.y = y;
        }

        public int getID() {
            return id;
        }
    }

    public class SquareLocation {
        public int left;
        public int right;
        public int top;
        public int bottom;

        private SquareLocation(int left, int right, int top, int bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }

        public int getWidth() {
            return right - left;
        }

        public int getHeight() {
            return top - bottom;
        }

    }
}