package com.liron.crypticcrossword;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum Type {
    ADD_ROW(R.drawable.circle_plus2), SUB_ROW(R.drawable.circle_minus2), ADD_COLUMN(R.drawable.circle_plus2), SUB_COLUMN((R.drawable.circle_minus2));

    private final int id;

    Type(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

/**
 * Created by lir on 19/08/2016.
 */
public class LinesButtons {
    private static final float GRID_LENGTH_TO_LINE_BALL_RATIO = 3;
    private static ZoomDataHandler zoomDataHandler = ZoomDataHandler.getInstance();
    private boolean isVisible = false;
    private int numOfRows = 9;
    private int numOfColumns = 9;
    private List<LinesButton> buttons = new ArrayList();

    public LinesButtons(Context context) {
        buttons.addAll(Arrays.asList(new AddRow(Type.ADD_ROW, context), new SubRow(Type.SUB_ROW, context),
                new AddColumns(Type.ADD_COLUMN, context), new SubColumns(Type.SUB_COLUMN, context)));
    }

    public void draw(Canvas canvas, Rect gridRect, Paint paint) {
        if (isVisible) {
            setZoom();
            for (LinesButton linesButton : buttons) {
                linesButton.setNewX(gridRect);
                linesButton.setNewY(gridRect);
                canvas.drawBitmap(linesButton.getBitmap(), linesButton.getPoint().x,
                        linesButton.getPoint().y, paint);
            }
        }
    }

    public void setVisibility(boolean isVisible) {
        this.isVisible = isVisible;
    }


    public void handleButtonsTouch(int touchX, int touchY) {
        for (LinesButton linesButton : buttons) {
            if (linesButton.isInRange(touchX, touchY)) {
                linesButton.doAction();
            }
        }
    }

    public boolean isInButtonsRange(int touchX, int touchY) {
        for (LinesButton linesButton : buttons) {
            if (linesButton.isInRange(touchX, touchY)) {
                return true;
            }
        }
        return false;
    }

    public int getNumOfColumns() {
        return numOfColumns;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setZoom() {
        for (LinesButton linesButton : buttons) {
            Bitmap originalBitmap = linesButton.getOriginalBitmap();
            linesButton.setBitmap(Bitmap.createScaledBitmap(originalBitmap,
                    Math.round(originalBitmap.getWidth() / zoomDataHandler.getScaleFactor()),
                    Math.round(originalBitmap.getHeight() / zoomDataHandler.getScaleFactor()), true));
        }
    }


    abstract class LinesButton {
        protected Bitmap bitmap;
        private Bitmap originalBitmap;
        private Type type;
        private Point point;

        public LinesButton(Type type, Context context) {
            this.type = type;
            this.originalBitmap = this.bitmap = BitmapFactory.decodeResource(context.getResources(),
                    type.getId());
            this.point = new Point(0, 0);
        }

        private Bitmap getOriginalBitmap() {
            return originalBitmap;
        }

        public boolean isInRange(int touchX, int touchY) {
            int centerX = getPoint().x + getRadius();
            int centerY = getPoint().y + getRadius();
            return Math.abs(centerX - touchX) < getRadius() && Math.abs(centerY - touchY) < getRadius();
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public int getRadius() {
            return bitmap.getWidth() / 2;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public Point getPoint() {
            return point;
        }

        public void setPoint(Point point) {
            this.point = point;
        }


        abstract public void doAction();

        protected abstract void setNewX(Rect gridRect);

        protected abstract void setNewY(Rect gridRect);
    }

    public class AddRow extends LinesButton {

        public AddRow(Type type, Context context) {
            super(type, context);
        }

        @Override
        public void doAction() {
            numOfRows++;
        }

        @Override
        protected void setNewX(Rect gridRect) {
            this.getPoint().x = gridRect.left - bitmap.getWidth();
        }

        @Override
        protected void setNewY(Rect gridRect) {
            int gridHeight = gridRect.height();
            if (gridHeight > GRID_LENGTH_TO_LINE_BALL_RATIO * bitmap.getHeight()) {
                this.getPoint().y = gridRect.top - getRadius() + Math.round((float) gridHeight / 3);
            } else {
                this.getPoint().y = gridRect.centerY() - 2 * getRadius();
            }
        }
    }

    public class SubRow extends LinesButton {

        public SubRow(Type type, Context context) {
            super(type, context);
        }

        @Override
        public void doAction() {
            numOfRows = Math.max(numOfRows - 1, 1);
        }

        @Override
        protected void setNewX(Rect gridRect) {
            this.getPoint().x = gridRect.left - bitmap.getWidth();
        }

        @Override
        protected void setNewY(Rect gridRect) {
            int gridHeight = gridRect.height();
            if (gridHeight > GRID_LENGTH_TO_LINE_BALL_RATIO * bitmap.getHeight()) {
                this.getPoint().y = gridRect.top - getRadius() + Math.round(2f * gridHeight / 3);
            } else {
                this.getPoint().y = gridRect.centerY();
            }
        }
    }

    public class AddColumns extends LinesButton {

        public AddColumns(Type type, Context context) {
            super(type, context);
        }

        @Override
        public void doAction() {
            numOfColumns++;
        }

        @Override
        protected void setNewX(Rect gridRect) {
            int gridWidth = gridRect.width();
            if (gridWidth > GRID_LENGTH_TO_LINE_BALL_RATIO * bitmap.getWidth()) {
                this.getPoint().x = gridRect.left - getRadius() + Math.round(2f * gridWidth / 3);
            } else {
                this.getPoint().x = gridRect.centerX();
            }
        }

        @Override
        protected void setNewY(Rect gridRect) {
            this.getPoint().y = gridRect.top - bitmap.getHeight();
        }
    }

    public class SubColumns extends LinesButton {

        public SubColumns(Type type, Context context) {
            super(type, context);
        }

        @Override
        protected void setNewX(Rect gridRect) {
            int gridWidth = gridRect.width();
            if (gridWidth > GRID_LENGTH_TO_LINE_BALL_RATIO * bitmap.getWidth()) {
                this.getPoint().x = gridRect.left - getRadius() + Math.round(1f * gridWidth / 3);
            } else {
                this.getPoint().x = gridRect.centerX() - 2 * getRadius();
            }
        }

        @Override
        protected void setNewY(Rect gridRect) {
            this.getPoint().y = gridRect.top - bitmap.getWidth();
        }

        @Override
        public void doAction() {
            numOfColumns = Math.max(numOfColumns - 1, 1);
        }
    }


}