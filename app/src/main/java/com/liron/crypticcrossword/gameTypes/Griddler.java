package com.liron.crypticcrossword.gameTypes;

import android.graphics.Color;
import android.widget.TextView;

import com.liron.crypticcrossword.GridLayoutView;
import com.liron.crypticcrossword.R;

/**
 * Created by lir on 01/10/2016.
 */

public class Griddler implements IGame {
    public static final String GUESS_FIRST_LETTER = "G";
    public static final String FILL_FIST_LETTER = "F";
    public static final String TRANSPARENT_FIRST_LETTER = "T";
    private final GridLayoutView gridBoard;

    public Griddler(GridLayoutView gridBoard) {
        this.gridBoard = gridBoard;
    }

    @Override
    public Integer[] getKeyboards() {
        return new Integer[0];
    }

    @Override
    public void selectCells(TextView clickedCell) {
        String firstLetter = (String) clickedCell.getText();
        clickedCell.setTextColor(Color.TRANSPARENT);

        switch (firstLetter) {
            case FILL_FIST_LETTER:
                clickedCell.setBackgroundResource(R.drawable.griddler_guess);
                clickedCell.setText(GUESS_FIRST_LETTER);
                break;
            case GUESS_FIRST_LETTER:
                clickedCell.setBackgroundColor(Color.TRANSPARENT);
                clickedCell.setText(TRANSPARENT_FIRST_LETTER);
                break;
            default:
                clickedCell.setBackgroundResource(R.drawable.griddler_fill);
                clickedCell.setText(FILL_FIST_LETTER);
        }
    }

    @Override
    public void applyImageVision() {

    }

    @Override
    public void afterCellEditing() {

    }
}
