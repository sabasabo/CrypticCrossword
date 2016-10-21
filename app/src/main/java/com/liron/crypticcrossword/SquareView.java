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
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class SquareView extends View {

    public static final int[] CIRCLE_CORNERS = new int[]{R.drawable.circle2,
            R.drawable.circle2, R.drawable.circle2, R.drawable.circle2};
    private static ZoomDataHandler zoomDataHandler = ZoomDataHandler.getInstance();
    private final boolean IS_USING_RECT = true;
    Paint paint = new Paint();
    private Point[] points = new Point[4];
    private LinesButtons lineButtons = new LinesButtons(getContext());
    private List<ColorBall> colorBalls = new ArrayList<ColorBall>();
    // array that holds the balls
    private int balID = 0;
    private Rect gridRect = new Rect();
    private Point deltaRectMovePoint = null;
    private EItemMoved currentItemMoved = EItemMoved.NONE;

    public SquareView(Context context) {
        super(context);
        setTag(getContext().getString(R.string.squareViewTag));
        setFocusable(true); // necessary for getting the touch events
    }

    private static Bitmap getScaledBitmap(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap,
                Math.round(bitmap.getWidth() / zoomDataHandler.getScaleFactor()),
                Math.round(bitmap.getHeight() / zoomDataHandler.getScaleFactor()), true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if (points[3] == null) { //point4 null when user did not touch and move on screen.
//            return;
//        }
        drawSelectionRectangle(canvas);
        lineButtons.draw(canvas, gridRect, paint);
        drawCorners(canvas);
        drawLineNumbers(canvas);
    }

    private void drawLineNumbers(Canvas canvas) {
//        EditText rowsNumber = new EditText(getContext());
//        EditText columnsNumber = new EditText(getContext());
//        TextView textBetween = new TextView(getContext());

//        rowsNumber.setText(getNumOfRows());
//        columnsNumber.setText(getNumOfColumns());
//        textBetween.setText(" X ");
        paint.setColor(Color.GREEN);
        paint.setTextSize(Math.min(gridRect.width(), gridRect.height()) / 9f);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(getNumOfRows() + " X " + getNumOfColumns(), gridRect.centerX(), gridRect.centerY(), paint);
        gridRect.centerX();
    }

    private void drawSelectionRectangle(Canvas canvas) {
        ColorBall colorBall1 = colorBalls.get(0);
        ColorBall colorBall2 = colorBalls.get(1);
        ColorBall colorBall3 = colorBalls.get(2);
        ColorBall colorBall4 = colorBalls.get(3);
        paint.setStyle(Paint.Style.STROKE);
        if (IS_USING_RECT) {
            if (currentItemMoved != EItemMoved.GRID_RECT) {
                gridRect.top = colorBall4.getYCenter();
                gridRect.bottom = colorBall2.getYCenter();
                gridRect.left = colorBall2.getXCenter();
                gridRect.right = colorBall4.getXCenter();
            }
            createPathGrid(gridRect, canvas);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(10 / zoomDataHandler.getScaleFactor());
            canvas.drawRect(gridRect, paint);
        } else {
//            Path path = new Path();
//            path.moveTo(colorBall1.getXCenter(), colorBall1.getYCenter());
//            path.lineTo(colorBall2.getXCenter(), colorBall2.getYCenter());
//            path.lineTo(colorBall3.getXCenter(), colorBall3.getYCenter());
//            path.lineTo(colorBall4.getXCenter(), colorBall4.getYCenter());
//            path.close();
//            canvas.drawPath(path, paint);
        }
    }

    private void drawCorners(Canvas canvas) {
        if (IS_USING_RECT) {
            colorBalls.get(1).scaleBall();
            colorBalls.get(3).scaleBall();
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
        paint.setStrokeWidth(5 / zoomDataHandler.getScaleFactor());
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
        boolean isHandledTouch = false;
        int eventAction = event.getAction();
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN: // touch down so check if the finger is on
                balID = -1;
                currentItemMoved = EItemMoved.NONE;
                if (isInBallsRange(touchX, touchY)) {
                    handleBallsTouch(touchX, touchY);
                    currentItemMoved = EItemMoved.BALL;
                    isHandledTouch = true;
                } else if (lineButtons.isInButtonsRange(touchX, touchY)) {
                    lineButtons.handleButtonsTouch(touchX, touchY);
                    isHandledTouch = true;
                } else if (gridRect.contains(touchX, touchY)) {
                    deltaRectMovePoint = new Point(touchX, touchY);
                    currentItemMoved = EItemMoved.GRID_RECT;
                    isHandledTouch = true;
                }
                // TODO: handle the case where lineButtons are pressed
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentItemMoved == EItemMoved.BALL) {
                    // move the balls the same as the finger
                    lineButtons.setVisibility(false);
                    final ColorBall colorBall = colorBalls.get(balID);
                    colorBall.setX(touchX - colorBall.getRadiusOfBall());
                    colorBall.setY(touchY - colorBall.getRadiusOfBall());
                    isHandledTouch = true;
                } else if (currentItemMoved == EItemMoved.GRID_RECT) {
                    int dx = touchX - deltaRectMovePoint.x;
                    int dy = touchY - deltaRectMovePoint.y;
                    gridRect.offset(dx, dy);
                    for (ColorBall ball : colorBalls) {
                        ball.setX(ball.getX() + dx);
                        ball.setY(ball.getY() + dy);
                    }
                    deltaRectMovePoint.set(touchX, touchY);
                    isHandledTouch = true;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                lineButtons.setVisibility(true);
                invalidate();
                break;
        }
//        invalidate();
        return isHandledTouch;

    }

    private void handleBallsTouch(int touchX, int touchY) {
        for (int i = colorBalls.size() - 1; i >= 0; i--) {
            ColorBall ball = colorBalls.get(i);

            if (userTouchedTheBall(touchX, touchY, ball)) {
                balID = ball.getID();
            }
        }
    }

    private boolean isInBallsRange(int touchX, int touchY) {
        for (int i = colorBalls.size() - 1; i >= 0; i--) {
            ColorBall ball = colorBalls.get(i);

            if (userTouchedTheBall(touchX, touchY, ball)) {
                return true;
            }
        }
        return false;
    }

    private boolean userTouchedTheBall(int touchX, int touchY, ColorBall ball) {
        return Math.sqrt(Math.pow(touchX - ball.getXCenter(), 2) +
                Math.pow(touchY - ball.getYCenter(), 2)) < ball.getRadiusOfBall();
    }

    public void initBalls(float locationChangeX, float locationChangeY) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int x = (int) (size.x / 2 - locationChangeX);
        int y = (int) (size.y / 3 - locationChangeY);
        int distanceX = (int) (size.x * 0.5 / zoomDataHandler.getScaleFactor());
        int distanceY = (int) (size.y * 0.3 / zoomDataHandler.getScaleFactor());
        setRotation(-zoomDataHandler.getRotationDegrees());
        points[0] = new Point();
        points[0].x = x - distanceX / 2;
        points[0].y = y - distanceY / 2;

        points[1] = new Point();
        points[1].x = x - distanceX / 2;
        points[1].y = y + distanceY / 2;

        points[2] = new Point();
        points[2].x = x + distanceX / 2;
        points[2].y = y + distanceY / 2;

        points[3] = new Point();
        points[3].x = x + distanceX / 2;
        points[3].y = y - distanceY / 2;

        balID = 2;
        // declare each ball with the ColorBall class
        for (int i = 0; i < points.length; i++) {
            colorBalls.add(new ColorBall(getContext(), CIRCLE_CORNERS[i], points[i], i));
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

    public enum EItemMoved {
        NONE, BALL, GRID_RECT
    }

    public static class ColorBall {

        private final Point originPoint;
        private Bitmap bitmap;
        private Bitmap originalBitmap;
        private Point point;
        private int id;

        public ColorBall(Context context, int resourceId, Point point, int id) {
            this.id = id;
            originalBitmap = BitmapFactory.decodeResource(context.getResources(),
                    resourceId);
            bitmap = getScaledBitmap(originalBitmap);
            originPoint = point;

            this.point = point;

        }

        public int getXCenter() {
            return point.x + getRadiusOfBall();
        }

        public void scaleBall() {
            bitmap = getScaledBitmap(originalBitmap);
        }

        public int getYCenter() {
            return point.y + getRadiusOfBall();
        }

        public Point getCenterPoint() {
            return new Point(getXCenter(), getYCenter());
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