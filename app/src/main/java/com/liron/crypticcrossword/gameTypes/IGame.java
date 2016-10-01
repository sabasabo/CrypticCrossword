package com.liron.crypticcrossword.gameTypes;

/**
 * Created by lir on 01/10/2016.
 */

public interface IGame {
    int[] getKeyboards();

    void selectCells();

    void applyImageVision();
}
