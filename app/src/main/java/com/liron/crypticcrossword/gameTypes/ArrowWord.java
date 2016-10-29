package com.liron.crypticcrossword.gameTypes;

import android.graphics.Color;
import android.widget.TextView;

import com.liron.crypticcrossword.GridLayoutView;
import com.liron.crypticcrossword.R;

/**
 * Created by lir on 01/10/2016.
 */

public class ArrowWord implements IGame {
    private final GridLayoutView gridBoard;

    public ArrowWord(GridLayoutView gridBoard) {
        this.gridBoard = gridBoard;
    }

    @Override
    public Integer[] getKeyboards() {
        return new Integer[]{R.array.lettersOnly_hebrew, R.array.lettersOnly_english};
    }

    @Override
    public void selectCells(TextView clickedCell) {
        removeColorFromAllCells();
        gridBoard.firstColoredCell = clickedCell;
        Integer index = (Integer) clickedCell.getTag();
        if (gridBoard.getIsDirectionHorizontal()) {
            int lastCell = (index / gridBoard.getColumnCount() + 1) * gridBoard.getColumnCount();
            for (int i = index; i < lastCell; i++) {
                gridBoard.colorCellAndFixFont((TextView) gridBoard.getChildAt(i));
            }
        } else {
            for (int i = index; i < gridBoard.getChildCount(); i += gridBoard.getColumnCount()) {
                gridBoard.colorCellAndFixFont((TextView) gridBoard.getChildAt(i));
            }
        }
    }

    @Override
    public void applyImageVision() {

    }

    @Override
    public void afterCellEditing() {
        gridBoard.currentModifiedCell.setBackgroundColor(Color.TRANSPARENT);
        Integer index = (Integer) gridBoard.currentModifiedCell.getTag();
        if (gridBoard.getIsDirectionHorizontal()) {
            if (index % gridBoard.getColumnCount() < gridBoard.getColumnCount() - 1) {
                gridBoard.currentModifiedCell = (TextView) gridBoard.getChildAt(index + 1);
            } else {
                gridBoard.currentModifiedCell = null;
            }
        } else {
            if (index + gridBoard.getColumnCount() < gridBoard.getChildCount()) {
                gridBoard.currentModifiedCell = (TextView) gridBoard.getChildAt(index + gridBoard.getColumnCount());
            } else {
                gridBoard.currentModifiedCell = null;
            }
        }
    }

    private void removeColorFromAllCells() {
        for (int i = 0; i < gridBoard.getChildCount(); i++) {
            gridBoard.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
