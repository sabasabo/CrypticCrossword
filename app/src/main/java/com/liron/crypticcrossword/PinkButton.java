package com.liron.crypticcrossword;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lir on 21/05/2016.
 */
public class PinkButton {
    private final String IS_SAVED_LOCATION = "isSavedLocation";
    private List<ButtonAction> buttonActions;
    private FloatingActionButton floatingButton;
    private Activity activity;
    private SquareView squareView;
    private ZoomDataHandler zoomDataHandler = ZoomDataHandler.getInstance();

    public PinkButton(Activity activity, boolean isReloaded) {
        this.floatingButton = (FloatingActionButton) activity.findViewById(R.id.floatingButton);
        this.activity = activity;
        boolean savedLoaction = (boolean) DataStorageHandler.readData(IS_SAVED_LOCATION, false);
//        if (isReloaded && savedLoaction) {
        if (savedLoaction) {
            buttonActions = new ArrayList<ButtonAction>(Arrays.asList(new Reload()));
        } else {
            buttonActions = new ArrayList<ButtonAction>(Arrays.asList(new Select(), new Ok()));
            squareView = new SquareView(activity);
        }
        initButton();
        zoomDataHandler.create(activity, (ViewGroup) activity.findViewById(R.id.boardParent));
        zoomDataHandler.enable();
    }

    public void initButton() {
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonActions.get(0).doAction();
                buttonActions.remove(0);
                if (buttonActions.isEmpty()) {
                    floatingButton.setOnClickListener(null);
                } else {
                    buttonActions.get(0).setImage();
                }
            }
        });
    }

    public interface ButtonAction {
        void doAction();

        void setImage();
    }

    public class Select implements ButtonAction {

        @Override
        public void doAction() {
            ((ViewGroup) activity.findViewById(R.id.boardParent)).addView(squareView);
            squareView.initBalls(zoomDataHandler.getScaleFactor(), zoomDataHandler.getFocusX(), zoomDataHandler.getFocusY());
        }

        @Override
        public void setImage() {
            floatingButton.setImageResource(R.drawable.table_only);
        }
    }

    public class Ok implements ButtonAction {

        @Override
        public void doAction() {
            GridLayoutView gridLayoutView = (GridLayoutView) activity.findViewById(R.id.grid_board);
            gridLayoutView.setGridValues(squareView.getNumOfRows(), squareView.getNumOfColumns(),
                    squareView.getSquareLocation());
            ((ViewGroup) activity.findViewById(R.id.boardParent)).removeView(squareView);
            ((ViewGroup) floatingButton.getParent()).removeView(floatingButton);
//            zoomDataHandler.enable();
        }

        @Override
        public void setImage() {
            floatingButton.setImageResource(R.drawable.ok);
        }
    }

    public class Reload implements ButtonAction {

        @Override
        public void doAction() {

            if ((boolean) DataStorageHandler.readData(IS_SAVED_LOCATION, false)) {
                ((GridLayoutView) activity.findViewById(R.id.grid_board)).loadGrid();
            }
            ((ViewGroup) floatingButton.getParent()).removeView(floatingButton);
//            zoomDataHandler.enable();
        }

        @Override
        public void setImage() {
            floatingButton.setImageResource(R.drawable.ok);
        }
    }
}
