package com.liron.crypticcrossword.gameTypes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.liron.crypticcrossword.GridLayoutView;
import com.liron.crypticcrossword.R;

import static com.liron.crypticcrossword.BlackPixelIdentifier.getHotspots;
import static com.liron.crypticcrossword.BlackPixelIdentifier.getXSample;
import static com.liron.crypticcrossword.BlackPixelIdentifier.getYSample;
import static com.liron.crypticcrossword.BlackPixelIdentifier.isBlack;

/**
 * Created by lir on 01/10/2016.
 */

public class CrossWord implements IGame {
    private final GridLayoutView gridBoard;

    public CrossWord(GridLayoutView gridBoard) {
        this.gridBoard = gridBoard;
    }

    @Override
    public Integer[] getKeyboards() {
        return new Integer[]{R.array.hebrew};
    }

    @Override
    public void selectCells(TextView clickedCell) {
        removeColorFromAllCells();
        gridBoard.firstColoredCell = clickedCell;
        Integer index = (Integer) clickedCell.getTag();
        if (gridBoard.getIsDirectionHorizontal()) {
            int lastCell = (index / gridBoard.getColumnCount() + 1) * gridBoard.getColumnCount();
            for (int i = index; i < lastCell && isNotBlack(gridBoard, i); i++) {
                gridBoard.colorCellAndFixFont((TextView) gridBoard.getChildAt(i));
            }
        } else {
            for (int i = index; i < gridBoard.getChildCount() && isNotBlack(gridBoard, i); i += gridBoard.getColumnCount()) {
                gridBoard.colorCellAndFixFont((TextView) gridBoard.getChildAt(i));
            }
        }
    }

    public boolean isNotBlack(GridLayoutView boardGrid, int i) {
        return boardGrid.getChildAt(i).getVisibility() != View.INVISIBLE;
    }

    @Override
    public void applyImageVision() {
        gridBoard.post(new Runnable() {
            @Override
            public void run() {
                View root = ((Activity) gridBoard.getContext()).findViewById(R.id.boardParent);
                Bitmap hotspots = getHotspots(root);
                for (int i = 0; i < gridBoard.getChildCount(); i++) {
                    View child = gridBoard.getChildAt(i);
                    int color = hotspots.getPixel(getXSample(child, root), getYSample(child, root));
                    if (isBlack(color)) {
                        child.setVisibility(View.INVISIBLE);
                        child.setFocusable(false);
                        child.setClickable(false);
                    }
                }
            }
        });
    }

    @Override
    public void afterCellEditing() {
        gridBoard.currentModifiedCell.setBackgroundColor(Color.TRANSPARENT);
        Integer index = (Integer) gridBoard.currentModifiedCell.getTag();
        if (gridBoard.getIsDirectionHorizontal()) {
            if (index % gridBoard.getColumnCount() < gridBoard.getColumnCount() - 1 && isNotBlack(gridBoard, index + 1)) {
                gridBoard.currentModifiedCell = (TextView) gridBoard.getChildAt(index + 1);
            } else {
                gridBoard.currentModifiedCell = null;
            }
        } else {
            if (index + gridBoard.getColumnCount() < gridBoard.getChildCount() && isNotBlack(gridBoard, index + gridBoard.getColumnCount())) {
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
