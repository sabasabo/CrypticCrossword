package com.liron.crypticcrossword;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

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
    private boolean isVisible = false;
    private int numOfRows = 9;
    private int numOfColumns = 9;
    private List<LinesButton> buttons = new ArrayList();

    public LinesButtons(Context context) {
        buttons.addAll(Arrays.asList(new AddRow(Type.ADD_ROW, context), new SubRow(Type.SUB_ROW, context),
                new AddColumns(Type.ADD_COLUMN, context), new SubColumns(Type.SUB_COLUMN, context)));
    }

    public void draw(Canvas canvas, Point leftBottom, Point rightTop, Paint paint) {
        if (isVisible) {
            for (LinesButton linesButton : buttons) {
                linesButton.setNewX(leftBottom, rightTop);
                linesButton.setNewY(leftBottom, rightTop);
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

    public int getNumOfColumns() {
        return numOfColumns;
    }

    public int getNumOfRows() {
        return numOfRows;
    }


    abstract class LinesButton {
        private Bitmap bitmap;
        private Type type;
        private Point point;

        public LinesButton(Type type, Context context) {
            this.type = type;
            this.bitmap = BitmapFactory.decodeResource(context.getResources(),
                    type.getId());
            this.point = new Point(0, 0);
        }

        public boolean isInRange(int touchX, int touchY) {
            int centerX = this.getPoint().x + this.getWidth();
            int centerY = this.getPoint().y + this.getWidth();
            return Math.abs(centerX - touchX) < this.getWidth() && Math.abs(centerY - touchY) < this.getWidth();
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public int getWidth() {
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

        protected abstract void setNewX(Point leftBottom, Point rightTop);

        protected abstract void setNewY(Point leftBottom, Point rightTop);
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
        protected void setNewX(Point leftBottom, Point rightTop) {
            this.getPoint().x = leftBottom.x - 100;
        }

        @Override
        protected void setNewY(Point leftBottom, Point rightTop) {
            this.getPoint().y = (leftBottom.y + rightTop.y) / 2 - 100;
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
        protected void setNewX(Point leftBottom, Point rightTop) {
            this.getPoint().x = leftBottom.x - 100;
        }

        @Override
        protected void setNewY(Point leftBottom, Point rightTop) {
            this.getPoint().y = (leftBottom.y + rightTop.y) / 2 + 100;
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
        protected void setNewX(Point leftBottom, Point rightTop) {
            this.getPoint().x = (leftBottom.x + rightTop.x) / 2 + 100;
        }

        @Override
        protected void setNewY(Point leftBottom, Point rightTop) {
            this.getPoint().y = rightTop.y - 100;
        }
    }

    public class SubColumns extends LinesButton {

        public SubColumns(Type type, Context context) {
            super(type, context);
        }

        @Override
        protected void setNewX(Point leftBottom, Point rightTop) {
            this.getPoint().x = (leftBottom.x + rightTop.x) / 2 - 100;
        }

        @Override
        protected void setNewY(Point leftBottom, Point rightTop) {
            this.getPoint().y = rightTop.y - 100;
        }

        @Override
        public void doAction() {
            numOfColumns = Math.max(numOfColumns - 1, 1);
        }
    }


}