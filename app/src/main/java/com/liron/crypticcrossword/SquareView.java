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
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SquareView extends View {

    public static final int DEFAULT_BALLS_DISTANCE = 200;
    public static final String RECTANGLE_EDGE_COLOR = "#AADB1255";
    public static final String RECTANGLE_AREA_COLOR = "#55DB1255";
    public static final int[] CIRCLE_CORNERS = new int[]{R.drawable.circle2,
            R.drawable.circle2, R.drawable.circle2, R.drawable.circle2};

    //            R.drawable.up_left,
//            R.drawable.bottom_left, R.drawable.bottom_right, R.drawable.up_right};

    private final boolean IS_USING_RECT = true;
    /**
     * point1 and point 3 are of same group and same as point 2 and point4
     */
    // variable to know what ball is being dragged
    Paint paint = new Paint();
    private Point[] points = new Point[4];
    private LinesButtons lineButtons = new LinesButtons(getContext());
    private List<ColorBall> colorBalls = new ArrayList<ColorBall>();
    // array that holds the balls
    private int balID = 0;

    public SquareView(Context context) {
        super(context);
        setTag(getContext().getString(R.string.squareViewTag));
        setFocusable(true); // necessary for getting the touch events
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if (points[3] == null) { //point4 null when user did not touch and move on screen.
//            return;
//        }
        drawSelectionRectangle(canvas);
        lineButtons.draw(canvas, colorBalls.get(1).point, colorBalls.get(3).point, paint);
        drawCorners(canvas);
    }

    private void drawSelectionRectangle(Canvas canvas) {
        ColorBall colorBall1 = colorBalls.get(0);
        ColorBall colorBall2 = colorBalls.get(1);
        ColorBall colorBall3 = colorBalls.get(2);
        ColorBall colorBall4 = colorBalls.get(3);
        paint.setStyle(Paint.Style.STROKE);
        if (IS_USING_RECT) {
            Rect rect = new Rect();
            rect.top = colorBall4.getYCenter();
            rect.bottom = colorBall2.getYCenter();
            rect.left = colorBall2.getXCenter();
            rect.right = colorBall4.getXCenter();
            createPathGrid(rect, canvas);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(10);
            canvas.drawRect(rect, paint);
        } else {
            Path path = new Path();
            path.moveTo(colorBall1.getXCenter(), colorBall1.getYCenter());
            path.lineTo(colorBall2.getXCenter(), colorBall2.getYCenter());
            path.lineTo(colorBall3.getXCenter(), colorBall3.getYCenter());
            path.lineTo(colorBall4.getXCenter(), colorBall4.getYCenter());
            path.close();
            canvas.drawPath(path, paint);
        }
    }

    private void drawCorners(Canvas canvas) {
        if (IS_USING_RECT) {
            canvas.drawBitmap(colorBalls.get(1).getBitmap(), colorBalls.get(1).getX(),
                    colorBalls.get(1).getY(), paint);
            canvas.drawBitmap(colorBalls.get(3).getBitmap(), colorBalls.get(3).getX(),
                    colorBalls.get(3).getY(), paint);
        } else {
            for (int i = 0; i < colorBalls.size(); i++) {
                ColorBall ball = colorBalls.get(i);
                canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                        paint);
                paint.setColor(Color.BLUE);
                paint.setTextSize(18);
                paint.setStrokeWidth(0);
                canvas.drawText("" + (i + 1), ball.getX(), ball.getY(), paint);
            }
        }
    }

    private void createPathGrid(Rect rect, Canvas canvas) {
        //draw stroke
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(5);
        int widthStep = rect.width() / getNumOfColumns();
        int heightStep = rect.height() / getNumOfRows();
        for (int i = rect.left + widthStep; i < rect.right && widthStep > 0; i += widthStep) {
            canvas.drawLine(i, rect.bottom, i, rect.top, paint);
        }
        for (int j = rect.top + heightStep; j < rect.bottom && heightStep > 0; j += heightStep) {
            canvas.drawLine(rect.left, j, rect.right, j, paint);
        }
    }

    public int getNumOfRows() {
        return lineButtons.getNumOfRows();
    }

    public int getNumOfColumns() {
        return lineButtons.getNumOfColumns();
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

                    if (userTouchedTheBall(touchX, touchY, ball)) {
                        balID = ball.getID();
                    }
                }
                if (balID == -1) {
                    lineButtons.handleButtonsTouch(touchX, touchY);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE: // touch drag with the ball
                if (balID > -1) {
                    // move the balls the same as the finger
                    lineButtons.setVisibility(false);
                    final ColorBall colorBall = colorBalls.get(balID);
                    colorBall.setX(touchX - colorBall.getRadiusOfBall());
                    colorBall.setY(touchY - colorBall.getRadiusOfBall());
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                lineButtons.setVisibility(true);
                break;
        }
        invalidate();
        return true;

    }

    private boolean userTouchedTheBall(int touchX, int touchY, ColorBall ball) {
        return isInRange(touchX, touchY, ball.point, ball.getRadiusOfBall());
    }

    private boolean isInRange(int touchX, int touchY, Point circle, int radius) {
        int centerX = circle.x + radius;
        int centerY = circle.y + radius;
        return Math.abs(centerX - touchX) < radius && Math.abs(centerY - touchY) < radius;
    }

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
        for (int i = 0; i < points.length; i++) {
            colorBalls.add(new ColorBall(getContext(), CIRCLE_CORNERS[i], points[i]));
        }
    }

    public SquareLocation getSquareLocation() {
        int left, right, top, bottom;
        if (IS_USING_RECT) {
            left = colorBalls.get(1).getXCenter();
            top = colorBalls.get(3).getYCenter();
            right = colorBalls.get(3).getXCenter();
            bottom = colorBalls.get(1).getYCenter();
        } else {
            left = right = colorBalls.get(0).getXCenter();
            top = bottom = colorBalls.get(0).getYCenter();
            for (ColorBall colorBall : colorBalls) {
                left = Math.min(left, colorBall.getXCenter());
                right = Math.max(right, colorBall.getXCenter());
                bottom = Math.max(bottom, colorBall.getYCenter());
                top = Math.min(top, colorBall.getYCenter());
            }
        }
        return new SquareLocation(left, right, top, bottom);
    }

    public static class ColorBall {

        private static int count = 0;
        private Bitmap bitmap;
        private Point point;
        private int id;

        public ColorBall(Context context, int resourceId, Point point) {
            id = count++;
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    resourceId);
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

    public static class SquareLocation {
        public int left;
        public int right;
        public int top;
        public int bottom;

        public SquareLocation(int left, int right, int top, int bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }

        public int getWidth() {
            return right - left;
        }

        public int getHeight() {
            return bottom - top;
        }
    }
}