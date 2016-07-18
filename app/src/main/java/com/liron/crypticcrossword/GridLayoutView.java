package com.liron.crypticcrossword;

import android.app.Activity;
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
    private BlackPixelIdentifier blackPixelIdentifier;

    public GridLayoutView(final Activity context, int numOfCells, SquareView.SquareLocation location) {
        super(context);
        blackPixelIdentifier = new BlackPixelIdentifier(this, context);
        int numOfColumns = (int) Math.sqrt(numOfCells);
        int editTextWidth = Math.round(location.getWidth() / numOfColumns);
        int editTextHeight = Math.round(location.getHeight() / numOfColumns);
        for (int i = 0; i < numOfCells; i++) {
            createEditBox(context, numOfColumns, editTextWidth, editTextHeight, i);
        }
        final FrameLayout parentLayout = (FrameLayout) context.findViewById(R.id.layout);
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = parentLayout.getRight() - location.right;
        layoutParams.topMargin = location.bottom; // - parentLayout.getTop();
        final GridLayoutView[] gridLayoutViews = new GridLayoutView[1];
        gridLayoutViews[0] = this;
        this.setBackgroundColor(R.color.colorAccent);
        context.runOnUiThread((new Runnable() {
            @Override
            public void run() {
                parentLayout.addView(gridLayoutViews[0], layoutParams);

            }
        }));


    }

    private void createEditBox(Activity context, int numOfColumns, int editTextWidth, int editTextHeight, int i) {
        EditText editText = new EditText(context);
        editText.setId(this.getId() + i + 1);
        Spec row = GridLayout.spec(i / numOfColumns, 1, 1f);
        Spec col = GridLayout.spec(i % numOfColumns, 1, 1f);
        LayoutParams params = new LayoutParams(row, col);
        params.width = editTextWidth;
        params.height = editTextHeight;
        editText.setBackgroundColor(Color.TRANSPARENT);
//        editText.setBackgroundColor(Color.BLUE);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (blackPixelIdentifier.isBlack(view)) {
                        view.setVisibility(View.INVISIBLE);
                        view.setFocusable(false);
                    } else {
                        EditText editText = (EditText) view;
                        TextKeyListener.clear(editText.getText());
                    }
                }
            }
        });
//        context.runOnUiThread((new Runnable() {
//            @Override
//            public void run() {
//                parentLayout.addView(gridLayoutViews[0], layoutParams);
//
//            }
//        }));
        this.addView(editText, params);
    }
}
