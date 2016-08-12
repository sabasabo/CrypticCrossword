package com.liron.crypticcrossword;

import android.app.Activity;
import android.graphics.Color;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;

/**
 * Created by lir on 11/06/2016.
 */
public class GridLayoutView extends GridLayout {
    public static final int MARGIN = 10;
    private final FrameLayout parentLayout;
    private final Activity activityContext;
    private final int editTextWidth;
    private final int editTextHeight;
    private BlackPixelIdentifier blackPixelIdentifier;
//    private final TextView textResizerView;

    public GridLayoutView(final Activity context, int numOfCells, SquareView.SquareLocation location) {
        super(context);
        activityContext = context;
        blackPixelIdentifier = new BlackPixelIdentifier(context);
        parentLayout = (FrameLayout) context.findViewById(R.id.layout);
        int numOfColumns = (int) Math.sqrt(numOfCells);
        editTextWidth = Math.round(location.getWidth() / numOfColumns) - 2 * MARGIN;
        editTextHeight = Math.round(location.getHeight() / numOfColumns) - 2 * MARGIN;
        for (int i = 0; i < numOfCells; i++) {
            createEditBox(numOfColumns, i);
        }
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = parentLayout.getRight() - location.right;
        layoutParams.topMargin = location.bottom; // - parentLayout.getTop();
        final GridLayoutView[] gridLayoutViews = new GridLayoutView[1];
        gridLayoutViews[0] = this;
//        this.setBackgroundColor(R.color.colorAccent);
        context.runOnUiThread((new Runnable() {
            @Override
            public void run() {
                parentLayout.addView(gridLayoutViews[0], layoutParams);

            }
        }));

//        textResizerView = new TextView(activityContext);
//        textResizerView.setBackgroundColor(Color.BLUE);
//        AutofitHelper autofitHelper = AutofitHelper.create(textResizerView);
//        autofitHelper.setEnabled(true);
//        autofitHelper.setMinTextSize(TypedValue.COMPLEX_UNIT_SP, 3);
//        LayoutParams params = new LayoutParams();
//        params.height = Math.round(location.getHeight() / numOfColumns) - 2 * MARGIN;
//        params.width = Math.min(params.height, Math.round(location.getWidth() / numOfColumns) - 2 * MARGIN) / 2;
//        textResizerView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        parentLayout.addView(textResizerView, params);


    }

    private void createEditBox(int numOfColumns, int i) {
        final EditText editText = new EditText(activityContext);
        editText.setId(this.getId() + i + 1);
        LayoutParams params = createParams(numOfColumns, i);
        editText.setBackgroundColor(Color.TRANSPARENT);
//        editText.setBackgroundColor(Color.WHITE);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
//        editText.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        editText.setPadding(0, 0, 0, 0);
//        editText.setMovementMethod(null);
        editText.setGravity(Gravity.CENTER);


        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (blackPixelIdentifier.isBlack(view, parentLayout)) {
                        view.setVisibility(View.INVISIBLE);
                        view.setFocusable(false);
                        view.setClickable(false);
                    } else {
                        EditText editText = (EditText) view;
//                        editText.setOnFocusChangeListener(null);
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, editText.getHeight());
//                        TextKeyListener.clear(editText.getText());
//                        textResizerView.setText(editText.getText());
//                        editText.setTextSize(textResizerView.getTextSize());
                    }
                }
            }
        });
//                editText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        textResizerView.setText(editText.getText());
//                        editText.setTextSize(textResizerView.getTextSize());
//                        editText.setTextAlignment(textResizerView.getTextAlignment());
//                    }
//                });
//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
//                textResizerView.setText(view.getText());
//                view.setTextSize(textResizerView.getTextSize());
//                return false;
//            }
//        });
        this.addView(editText, params);
    }

    private LayoutParams createParams(int numOfColumns, int i) {
        Spec row = GridLayout.spec(i / numOfColumns, 1, 1f);
        Spec col = GridLayout.spec(i % numOfColumns, 1, 1f);
        LayoutParams params = new LayoutParams(row, col);
        params.width = editTextWidth;
        params.height = editTextHeight;
        params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
        return params;
    }
}
