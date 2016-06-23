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

    public GridLayoutView(final Activity context, int numOfCells, SquareView.SquareLocation location, SquareView squareView) {
        super(context);
        blackPixelIdentifier = new BlackPixelIdentifier(this, context);
        int numOfColumns = (int) Math.sqrt(numOfCells);
        int editTextWidth = Math.round(location.getWidth() / numOfColumns);
        int editTextHeight = Math.round(location.getHeight() / numOfColumns);
        for (int i = 0; i < numOfCells; i++) {
            EditText editText = new EditText(context);
            editText.setText(i + "");
            GridLayout.Spec row = GridLayout.spec(i / numOfColumns, 1, 1f);
            GridLayout.Spec col = GridLayout.spec(i % numOfColumns, 1, 1f);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
            params.width = editTextWidth;
            params.height = editTextHeight;
            editText.setBackgroundColor(Color.TRANSPARENT);
            editText.setBackgroundColor(Color.BLUE);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
            this.addView(editText, params);
        }
        FrameLayout parentLayout = (FrameLayout) context.findViewById(R.id.layout);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = parentLayout.getRight() - location.right;
        layoutParams.topMargin = location.bottom; // - parentLayout.getTop();

        parentLayout.addView(this, layoutParams);

    }
}
