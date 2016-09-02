package com.liron.crypticcrossword;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lir on 27/08/2016.
 */
public class Keyboard {
    public static final String DELIMITER = ",";
    private static final List<String> SPECIAL_KEYWORDS = Arrays.asList("direction");
    private static GridLayout keyboardGrid;
    private static Activity context;

    static void createKeyboard(Activity activity, int keyboardId) {
        keyboardGrid = (GridLayout) activity.findViewById(R.id.keyboard);
        context = activity;
        String[] keyboardLines = activity.getResources().getStringArray(keyboardId);
        setGridDimensions(keyboardLines);
        int rowIndex = 0;
        for (String line : keyboardLines) {
            int columnIndex = 0;
            String[] split = line.split(DELIMITER);
            for (String letter : split) {
                addKey(letter, rowIndex, columnIndex);
                columnIndex++;
            }
            rowIndex++;
        }
    }

    private static void setGridDimensions(String[] keyboardLines) {
        keyboardGrid.setRowCount(keyboardLines.length);
        keyboardGrid.setColumnCount(keyboardLines[0].split(DELIMITER).length);
    }

    private static void addKey(String letter, int row, int column) {
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.height = 0;
        param.width = 0;
        param.setGravity(Gravity.CENTER);
        param.rowSpec = GridLayout.spec(row, 1f);
        param.columnSpec = GridLayout.spec(column, 1f);
        final TextView key = new TextView(context);
        key.setLayoutParams(param);
        key.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        if (isSpecialKey(letter)) {
            setSpecialStyleAndFunction(key, letter);
        } else {
            setNormalStyleAndFunction(key, letter);
        }
        keyboardGrid.addView(key);
    }

    private static boolean isSpecialKey(String letter) {
        return SPECIAL_KEYWORDS.contains(letter);
    }

    private static void setNormalStyleAndFunction(TextView key, String letter) {
        key.setText(letter);
        key.setBackgroundResource(R.drawable.border);
        final GridLayoutView boardGrid = (GridLayoutView) context.findViewById(R.id.grid_board);
        key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View key) {
                if (boardGrid.currentModifiedCell != null) {
                    boardGrid.currentModifiedCell.setText(((TextView) key).getText());
                    boardGrid.setNextCellAsCurrent();
                }
            }
        });
    }

    private static void setSpecialStyleAndFunction(TextView key, String specialWord) {
        if (specialWord.equals("direction")) {
            key.setBackgroundResource(R.drawable.border_arrow_down);
            final GridLayoutView boardGrid = (GridLayoutView) context.findViewById(R.id.grid_board);
            boardGrid.setIsDirectionHorizontal(true);
            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View key) {
                    if (boardGrid.getIsDirectionHorizontal()) {
                        key.setBackgroundResource(R.drawable.border_arrow_left); // TODO: change this to down arrow
                    } else {
                        key.setBackgroundResource(R.drawable.border_arrow_down);
                    }
                    boardGrid.removeColorFromAllCells();
                    boardGrid.setIsDirectionHorizontal(!boardGrid.getIsDirectionHorizontal());
                    boardGrid.currentModifiedCell = boardGrid.firstColoredCell;
                    boardGrid.colorNextCells(boardGrid.firstColoredCell);
                }
            });
        }
    }
}
