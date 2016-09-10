package com.liron.crypticcrossword;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

/**
 * Created by lir on 11/06/2016.
 */
public class GridLayoutView extends GridLayout {
    public static final int MARGIN = 10;
    public TextView currentModifiedCell = null;
    public TextView firstColoredCell = null;
    private FrameLayout parentLayout;
    private int editTextWidth;
    private int editTextHeight;
    private BlackPixelIdentifier blackPixelIdentifier;
    private boolean directionHorizontal = true;

    public GridLayoutView(Context context) {
        super(context);
    }

    public GridLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridLayoutView(final Activity context, int numOfRows, int numOfColumns, SquareView.SquareLocation location) {
        super(context);
        blackPixelIdentifier = new BlackPixelIdentifier(context);
        parentLayout = (FrameLayout) context.findViewById(R.id.layout);
        setRowCount(numOfRows);
        setColumnCount(numOfColumns);
        editTextWidth = Math.round(location.getWidth() / getColumnCount()) - 2 * MARGIN;
        editTextHeight = Math.round(location.getHeight() / numOfRows) - 2 * MARGIN;
        for (int i = 0; i < numOfRows * getColumnCount(); i++) {
            createTextBox(i);
        }
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = parentLayout.getRight() - location.right;
        layoutParams.topMargin = location.top; // - parentLayout.getTop();
        final GridLayoutView[] gridLayoutViews = new GridLayoutView[1];
        gridLayoutViews[0] = this;
        context.runOnUiThread((new Runnable() {
            @Override
            public void run() {
                parentLayout.addView(gridLayoutViews[0], layoutParams);

            }
        }));
    }

    public boolean getIsDirectionHorizontal() {
        return directionHorizontal;
    }

    public void setIsDirectionHorizontal(boolean directionHorizontal) {
        this.directionHorizontal = directionHorizontal;
    }

    public void setGridValues(int numOfRows, int numOfColumns, SquareView.SquareLocation location) {
//        blackPixelIdentifier = new BlackPixelIdentifier(context);
        parentLayout = (FrameLayout) getParent();
        setRowCount(numOfRows);
        setColumnCount(numOfColumns);
        editTextWidth = Math.round(location.getWidth() / getColumnCount()) - 2 * MARGIN;
        editTextHeight = Math.round(location.getHeight() / numOfRows) - 2 * MARGIN;
        for (int i = 0; i < numOfRows * getColumnCount(); i++) {
            createTextBox(i);
        }
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = parentLayout.getRight() - location.right;
        layoutParams.topMargin = location.top; // - parentLayout.getTop();
        setLayoutParams(layoutParams);
    }


    private void createTextBox(int i) {
        final TextView textView = new TextView(getContext());
        textView.setId(this.getId() + i + 1);
        textView.setTag(i);
        LayoutParams params = createParams(i);
        textView.setBackgroundColor(Color.TRANSPARENT);
        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                removeColorFromAllCells();
                colorNextCells((TextView) view);
                currentModifiedCell = (TextView) view;
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, 0);
            }
        });

        textView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
//                    if (blackPixelIdentifier.isBlack(view, parentLayout)) {
//                        view.setVisibility(View.INVISIBLE);
//                        view.setFocusable(false);
//                        view.setClickable(false);
//                    } else {
                    TextView textView = (TextView) view;
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getHeight());
//                        TextKeyListener.clear(textView.getText());
//                        textResizerView.setText(textView.getText());
//                        textView.setTextSize(textResizerView.getTextSize());
//                    }
                }
            }
        });
        this.addView(textView, params);
    }

    public void removeColorFromAllCells() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void colorNextCells(TextView cell) {
        firstColoredCell = cell;
        Integer index = (Integer) cell.getTag();
        if (directionHorizontal) {
            int lastCell = (index / getColumnCount() + 1) * getColumnCount();
            for (int i = index; i < lastCell; i++) {
                colorCellAndFixFont((TextView) getChildAt(i));
            }
        } else {
            for (int i = index; i < getChildCount(); i += getColumnCount()) {
                colorCellAndFixFont((TextView) getChildAt(i));
            }
        }
    }

    private void colorCellAndFixFont(TextView nextCell) {
        nextCell.setBackgroundResource(R.drawable.select_next_cells_transperent);
        nextCell.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (0.9 * nextCell.getHeight()));
        nextCell.setPadding(0, 0, 0, 0);
        nextCell.setGravity(Gravity.CENTER);
    }

    public void setNextCellAsCurrent() {
        Integer index = (Integer) currentModifiedCell.getTag();
        if (directionHorizontal) {
            if (index % getColumnCount() < getColumnCount() - 1) {
                currentModifiedCell = (TextView) getChildAt(index + 1);
            }
        } else {
            if (index + getColumnCount() < getChildCount()) {
                currentModifiedCell = (TextView) getChildAt(index + getColumnCount());
            }
        }
    }

    private LayoutParams createParams(int i) {
        Spec row = GridLayout.spec(i / getColumnCount(), 1, 1f);
        Spec col = GridLayout.spec(i % getColumnCount(), 1, 1f);
        LayoutParams params = new LayoutParams(row, col);
        params.width = editTextWidth;
        params.height = editTextHeight;
        params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
        return params;
    }
}
