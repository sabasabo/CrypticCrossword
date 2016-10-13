package com.liron.crypticcrossword.gameTypes;

import android.graphics.Color;
import android.widget.TextView;

import com.liron.crypticcrossword.GridLayoutView;
import com.liron.crypticcrossword.R;

/**
 * Created by lir on 01/10/2016.
 */

public class Sudoku implements IGame {
    private final GridLayoutView gridBoard;

    public Sudoku(GridLayoutView gridBoard) {
        this.gridBoard = gridBoard;
    }

    @Override
    public Integer[] getKeyboards() {
        return new Integer[]{R.array.numbers};
    }

    @Override
    public void selectCells(TextView clickedCell) {
        removeColorFromAllCells();
        gridBoard.firstColoredCell = clickedCell;
        gridBoard.colorCellAndFixFont((TextView) gridBoard.getChildAt((Integer) clickedCell.getTag()));
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
