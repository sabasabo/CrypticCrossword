package com.liron.crypticcrossword;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.liron.crypticcrossword.SquareView.SquareLocation;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lir on 23/06/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private static final int NUM_OF_ROWS = 9;
    private static final int NUM_OF_COLUMNS = 9;
    private static final List<Integer> blackCellsLTR = new ArrayList<>(Arrays.asList(0, 9, 10, 12, 14, 16, 23, 28, 30, 32, 34, 35, 40, 46, 48, 50, 51, 52, 57, 64, 66, 68, 70, 72, 73));
    private static final List<Integer> blackCellsRTL = new ArrayList<>(Arrays.asList(8, 10, 12, 14, 16, 17, 21, 78, 28, 30, 32, 34, 40, 46, 47, 48, 50, 52, 59, 64, 66, 68, 70, 79, 80));
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Ignore
    @Test
    public void testClick() throws Throwable {
        Point size = getScreenSize();
        int width = size.x;
        int height = size.y;
        final SquareLocation location = new SquareLocation((int) (0.625 * width), (int) (0.93 * width), (int) (0.675 * height), (int) (0.3 * height));
        final GridLayoutView[] gridView = new GridLayoutView[1];

        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gridView[0] = new GridLayoutView(mActivityRule.getActivity(), NUM_OF_ROWS, NUM_OF_COLUMNS, location);
            }
        });
        Thread.sleep(1000);
        final BlackPixelIdentifier blackPixelIdentifier = new BlackPixelIdentifier(mActivityRule.getActivity());
        InputMethodManager inputMethodManager = (InputMethodManager) mActivityRule.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        for (Integer i = 0; i <= gridView[0].getChildCount(); i++) {
            int id = gridView[0].getId() + i + 1;
            final View currentView = mActivityRule.getActivity().findViewById(id);
            inputMethodManager.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
            final boolean isReallyBlack = blackCellsRTL.contains(i);
            final View root = mActivityRule.getActivity().findViewById(R.id.layout);
            mActivityRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boolean isIdentifiedBlack = blackPixelIdentifier.isBlack(currentView, root);
                    if (isIdentifiedBlack) {
                        currentView.setBackgroundColor(Color.BLACK);
                    } else {
                        currentView.setBackgroundColor(Color.WHITE);
                    }
//                    junit.framework.Assert.assertEquals(isReallyBlack, isIdentifiedBlack);
//            onView(withId(id)).perform(click());

                }
            });

        }
        Thread.sleep(1000);
//        onView(withTagKey(R.string.squareViewTag)).perform(dragQuarterScreenDown(this, );


    }

    @NonNull
    private Point getScreenSize() {
        Display display = mActivityRule.getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }


}