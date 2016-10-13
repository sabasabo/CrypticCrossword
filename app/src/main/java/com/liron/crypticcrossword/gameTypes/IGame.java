package com.liron.crypticcrossword.gameTypes;

import android.widget.TextView;

/**
 * Created by lir on 01/10/2016.
 */

public interface IGame {
    Integer[] getKeyboards();

    void selectCells(TextView clickedCell);

    void applyImageVision();

    void afterCellEditing();

}
