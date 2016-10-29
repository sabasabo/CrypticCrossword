package com.liron.crypticcrossword;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liron.crypticcrossword.gameTypes.GameTypeFactory;
import com.liron.crypticcrossword.gameTypes.IGame;

import static com.liron.crypticcrossword.DataStorageHandler.GRID_TEXT;
import static com.liron.crypticcrossword.DataStorageHandler.HEIGHT_RATIO;
import static com.liron.crypticcrossword.DataStorageHandler.IS_SAVED_LOCATION;
import static com.liron.crypticcrossword.DataStorageHandler.NUM_OF_COLUMNS;
import static com.liron.crypticcrossword.DataStorageHandler.NUM_OF_ROWS;
import static com.liron.crypticcrossword.DataStorageHandler.RIGHT_MARGIN_RATIO;
import static com.liron.crypticcrossword.DataStorageHandler.TOP_MARGIN_RATIO;
import static com.liron.crypticcrossword.DataStorageHandler.WIDTH_RATIO;

/**
 * Created by lir on 11/06/2016.
 */
public class GridLayoutView extends GridLayout {
    public static final int MARGIN = 3;
    public static final String SEPARATOR = "~~";
    public TextView currentModifiedCell = null;
    public TextView firstColoredCell = null;
    private ViewGroup parentLayout;
    private int editTextWidth;
    private int editTextHeight;
    private BlackPixelIdentifier blackPixelIdentifier;
    private boolean directionHorizontal = true;
    private IGame gameType;

    public GridLayoutView(Context context) {
        super(context);
        setGameType();
    }

    public GridLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGameType();
    }

    private void setGameType() {
        post(new Runnable() {
            @Override
            public void run() {
                gameType = GameTypeFactory.createGame((Activity) getContext());
                Keyboard.createKeyboard((Activity) getContext(), gameType.getKeyboards());
            }
        });
    }

    public boolean getIsDirectionHorizontal() {
        return directionHorizontal;
    }

    public void setIsDirectionHorizontal(boolean directionHorizontal) {
        this.directionHorizontal = directionHorizontal;
    }

    public void saveGrid(RelativeLayout.LayoutParams layoutParams, SquareView.SquareLocation location) {
        DataStorageHandler.saveData(IS_SAVED_LOCATION, true);
        DataStorageHandler.saveData(RIGHT_MARGIN_RATIO, (float) layoutParams.rightMargin / parentLayout.getWidth());
        DataStorageHandler.saveData(TOP_MARGIN_RATIO, (float) layoutParams.topMargin / parentLayout.getHeight());
        DataStorageHandler.saveData(WIDTH_RATIO, (float) location.getWidth() / parentLayout.getWidth());
        DataStorageHandler.saveData(HEIGHT_RATIO, (float) location.getHeight() / parentLayout.getHeight());
        DataStorageHandler.saveData(NUM_OF_ROWS, getRowCount());
        DataStorageHandler.saveData(NUM_OF_COLUMNS, getColumnCount());
        // TODO: might need to save gameType type
        saveGridText();
    }

    public void saveGridText() {
        StringBuilder gridText = new StringBuilder();
        for (int i = 0; i < getChildCount(); i++) {
            gridText.append(((TextView) getChildAt(i)).getText()).append(SEPARATOR);
        }
        DataStorageHandler.saveData(GRID_TEXT, gridText.toString());
    }

    public void loadGrid() {
        final ViewGroup parentLayout = (ViewGroup) getParent();
        parentLayout.post(new Runnable() {
            @Override
            public void run() {
                int locationMarginRight = (int) ((float) DataStorageHandler.readData(RIGHT_MARGIN_RATIO) * parentLayout.getWidth());
                int locationMarginTop = (int) ((float) DataStorageHandler.readData(TOP_MARGIN_RATIO) * parentLayout.getHeight());
                int locationWidth = (int) ((float) DataStorageHandler.readData(WIDTH_RATIO) * parentLayout.getWidth());
                int locationHeight = (int) ((float) DataStorageHandler.readData(HEIGHT_RATIO) * parentLayout.getHeight());
                setRowCount((Integer) DataStorageHandler.readData(NUM_OF_ROWS));
                setColumnCount((Integer) DataStorageHandler.readData(NUM_OF_COLUMNS));

                editTextWidth = Math.round(locationWidth / getColumnCount()) - 2 * MARGIN;
                editTextHeight = Math.round(locationHeight / getRowCount()) - 2 * MARGIN;

                for (int i = 0; i < getRowCount() * getColumnCount(); i++) {
                    createTextBox(i);
                }
                String[] gridText = ((String) DataStorageHandler.readData(GRID_TEXT)).split(SEPARATOR);
                for (int i = 0; i < getChildCount(); i++) {
                    final TextView textView = (TextView) getChildAt(i);
                    textView.setText(gridText[i]);
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            fixFontOfTextView(textView);
                        }
                    });
                }
                final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                layoutParams.rightMargin = locationMarginRight;
                layoutParams.topMargin = locationMarginTop;
                setLayoutParams(layoutParams);
                // TODO: might need to load gameType type
            }
        });
    }


    public void setGridValues(int numOfRows, int numOfColumns, SquareView.SquareLocation location, float rotation) {
        //        blackPixelIdentifier = new BlackPixelIdentifier(context);
        parentLayout = (ViewGroup) getParent();
        setRowCount(numOfRows);
        setColumnCount(numOfColumns);
        setRotation(rotation);
        editTextWidth = Math.round(location.getWidth() / getColumnCount()) - 2 * MARGIN;
        editTextHeight = Math.round(location.getHeight() / numOfRows) - 2 * MARGIN;
        for (int i = 0; i < numOfRows * getColumnCount(); i++) {
            createTextBox(i);
        }
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = parentLayout.getRight() - location.right;
        layoutParams.topMargin = location.top;
        setLayoutParams(layoutParams);
        saveGrid(layoutParams, location);
        gameType.applyImageVision();
    }


    private void createTextBox(int i) {
        final TextView textView = new TextView(getContext());
        textView.setId(this.getId() + i + 1);
        textView.setTag(i);
        LayoutParams params = createParams(i);
        textView.setBackgroundColor(Color.TRANSPARENT);
        textView.setText(" ");
        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    TextView textView = (TextView) view;
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getHeight());
                }
            }
        });
        this.addView(textView, params);
    }


    public void colorNextCells(TextView cell) {
        gameType.selectCells(cell);
    }

    public void colorCellAndFixFont(TextView nextCell) {
        nextCell.setBackgroundResource(R.drawable.select_next_cells_transperent);
        fixFontOfTextView(nextCell);
    }

    public void fixFontOfTextView(TextView textView) {
        int size = Math.min(textView.getWidth(), textView.getHeight());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (0.9 * size));
        textView.setPadding(0, 0, 0, 0);
        textView.setGravity(Gravity.CENTER);
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

    public void setTextInCurrentCell(String text) {
        currentModifiedCell.setText(text);
        gameType.afterCellEditing();
        saveGridText();
    }
}