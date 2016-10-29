package com.liron.crypticcrossword;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lir on 27/08/2016.
 */
public class Keyboard {
    public static final String DELIMITER = ",";
    private static final List<String> SPECIAL_KEYWORDS = Arrays.asList("direction", "keyboard", "language");
    private static GridLayout keyboardGrid;
    private static Activity context;
    private static String currentKeyboardName;
    private static List<Integer> keyboards;

    static void createKeyboard(Activity activity, Integer... keyboardIds) {
        keyboardGrid = (GridLayout) activity.findViewById(R.id.keyboard);
        if (keyboardIds.length == 0) {
            ((ViewGroup) keyboardGrid.getParent()).removeView(keyboardGrid);
            return;
        }
        context = activity;
        keyboards = new ArrayList<>(Arrays.asList(keyboardIds));
        currentKeyboardName = context.getResources().getResourceEntryName(keyboardIds[0]);
        String[] keyboardLines = activity.getResources().getStringArray(keyboardIds[0]);
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

    private static void addKey(final String letter, final int row, final int column) {
        keyboardGrid.post(new Runnable() {
            @Override
            public void run() {
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();

                param.setGravity(Gravity.CENTER);
                param.rowSpec = GridLayout.spec(row, 1f);
                param.columnSpec = GridLayout.spec(column, 1f);
                final TextView key = new TextView(context);
                param.height = keyboardGrid.getHeight() / keyboardGrid.getRowCount();
                param.width = keyboardGrid.getWidth() / keyboardGrid.getColumnCount();
                key.setLayoutParams(param);
                key.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                key.setGravity(Gravity.CENTER);
                if (isSpecialKey(letter)) {
                    setSpecialStyleAndFunction(key, letter);
                } else {
                    setNormalStyleAndFunction(key, letter);
                }
                keyboardGrid.addView(key);
            }
        });
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
                    boardGrid.setTextInCurrentCell((String) ((TextView) key).getText());
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
                    boardGrid.setIsDirectionHorizontal(!boardGrid.getIsDirectionHorizontal());
                    boardGrid.currentModifiedCell = boardGrid.firstColoredCell;
                    boardGrid.colorNextCells(boardGrid.firstColoredCell);
                }
            });
        } else if (specialWord.equals("keyboard")) {
            key.setBackgroundResource(R.drawable.keyboard_icon);
            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View key) {
                    keyboardGrid.removeAllViews();
                    keyboards.add(keyboards.remove(0));
                    createKeyboard(context, keyboards.toArray(new Integer[0]));
                }
            });
        } else if (specialWord.equals("language")) {
            key.setBackgroundResource(R.drawable.earth_icon_keyboard);
            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View key) {
                    keyboardGrid.removeAllViews();
                    keyboards.add(keyboards.remove(0));
                    createKeyboard(context, keyboards.toArray(new Integer[0]));
                }
            });
        }
    }
}
