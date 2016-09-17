package com.liron.crypticcrossword;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.FrameLayout;

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
    private ZoomDataHandler zoomDataHandler = new ZoomDataHandler();

    public PinkButton(Activity activity, boolean isReloaded) {
        this.floatingButton = (FloatingActionButton) activity.findViewById(R.id.floatingButton);
        this.activity = activity;
        Boolean savedLoaction = (Boolean) DataStorageHandler.readData(IS_SAVED_LOCATION, false);
        if (isReloaded && savedLoaction) {
            buttonActions = new ArrayList<ButtonAction>(Arrays.asList(new Reload()));
        } else {
            buttonActions = new ArrayList<ButtonAction>(Arrays.asList(new Select(), new Ok()));
            squareView = new SquareView(activity);
        }
        initButton();
        zoomDataHandler.create(activity, activity.findViewById(R.id.layout));
    }

    public void initButton() {
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonActions.get(0).doAction();
                buttonActions.remove(0);
                if (buttonActions.isEmpty()) {
                    floatingButton.setOnClickListener(null);
                    return;
                }
                buttonActions.get(0).setImage();
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
            ((FrameLayout) activity.findViewById(R.id.layout)).addView(squareView);
            View board = activity.findViewById(R.id.layout);
            squareView.initBalls();
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
            ((FrameLayout) activity.findViewById(R.id.layout)).removeView(squareView);
            ((CoordinatorLayout) activity.findViewById(R.id.superParent)).removeView(floatingButton);
            zoomDataHandler.enable();
        }

        @Override
        public void setImage() {
            floatingButton.setImageResource(R.drawable.ok);
        }
    }

    public class Reload implements ButtonAction {

        @Override
        public void doAction() {

//            GridLayoutView gridLayoutView = (GridLayoutView) activity.findViewById(R.id.grid_board);
//            gridLayoutView.loadGrid();
            ((CoordinatorLayout) activity.findViewById(R.id.superParent)).removeView(floatingButton);
//            zoomDataHandler.enable();
        }

        @Override
        public void setImage() {
            floatingButton.setImageResource(R.drawable.ok);
        }
    }
}
