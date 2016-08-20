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
    private List<ButtonAction> buttonActions;
    private FloatingActionButton floatingButton;
    private Activity activity;
    private SquareView squareView;

    public PinkButton(Activity activity) {
        this.floatingButton = (FloatingActionButton) activity.findViewById(R.id.floatingButton);
        this.activity = activity;
        this.buttonActions = new ArrayList<>(Arrays.asList(new Select(), new Ok()));
        squareView = new SquareView(activity);
        initButton();
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
            final View img = activity.findViewById(R.id.layout);
            squareView.initBalls((int) img.getX() + img.getWidth() / 2, (int) img.getY() + img.getHeight() / 2);
        }

        @Override
        public void setImage() {
            floatingButton.setImageResource(R.drawable.table_only);
        }
    }

    public class Ok implements ButtonAction {

        @Override
        public void doAction() {

            GridLayoutView gridView = new GridLayoutView(activity, squareView.getNumOfRows(), squareView.getNumOfColumns(),
                    squareView.getSquareLocation());
            ((FrameLayout) activity.findViewById(R.id.layout)).removeView(squareView);
            ((CoordinatorLayout) activity.findViewById(R.id.superParent)).removeView(floatingButton);
        }

        @Override
        public void setImage() {
            floatingButton.setImageResource(R.drawable.ok);
        }
    }
}
