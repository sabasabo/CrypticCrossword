package com.liron.crypticcrossword.gameTypes;

import android.graphics.Color;
import android.widget.TextView;

import com.liron.crypticcrossword.GridLayoutView;
import com.liron.crypticcrossword.R;

/**
 * Created by lir on 01/10/2016.
 */

public class GeneralGame implements IGame {
    private final GridLayoutView gridBoard;

    public GeneralGame(GridLayoutView gridBoard) {
        this.gridBoard = gridBoard;
    }

    @Override
    public Integer[] getKeyboards() {
        return new Integer[]{R.array.hebrew, R.array.numbers};
    }

    @Override
    public void selectCells(TextView clickedCell) {
        removeColorFromAllCells();
    }

    @Override
    public void applyImageVision() {

    }

    @Override
    public void afterCellEditing() {

    }

    private void removeColorFromAllCells() {
        for (int i = 0; i < gridBoard.getChildCount(); i++) {
            gridBoard.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
