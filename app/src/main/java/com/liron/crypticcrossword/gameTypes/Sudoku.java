package com.liron.crypticcrossword.gameTypes;

import com.liron.crypticcrossword.R;

/**
 * Created by lir on 01/10/2016.
 */

public class Sudoku implements IGame {
    @Override
    public int[] getKeyboards() {
        return new int[]{R.array.numbers};
    }

    @Override
    public void selectCells() {

    }

    @Override
    public void applyImageVision() {

    }
}
