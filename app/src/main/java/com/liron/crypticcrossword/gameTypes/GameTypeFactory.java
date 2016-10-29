package com.liron.crypticcrossword.gameTypes;

import android.app.Activity;

import com.liron.crypticcrossword.GridLayoutView;
import com.liron.crypticcrossword.R;

import java.util.Map;


/**
 * Created by lir on 01/10/2016.
 */

public class GameTypeFactory {
    private static Map<String, Class<? extends IGame>> gameTypes;

    public static IGame createGame(Activity activity) {
        String gameType = activity.getIntent().getExtras().getString(activity.getString(R.string.gameType)).split("-")[0];
        GridLayoutView gridBoard = (GridLayoutView) activity.findViewById(R.id.grid_board);
        IGame game;
        switch (gameType) {
            case "griddler":
                game = new Griddler(gridBoard);
                break;
            case "arrowword":
                game = new ArrowWord(gridBoard);
                break;
            case "crossword":
                game = new CrossWord(gridBoard);
                break;
            case "sudoku":
                game = new Sudoku(gridBoard);
                break;
            default:
                game = new GeneralGame(gridBoard);
        }
        return game;
    }
}
