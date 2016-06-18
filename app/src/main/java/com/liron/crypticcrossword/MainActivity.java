package com.liron.crypticcrossword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static final int MAX_RGB = 100;
    public static final int AVERAGE_RGB = 65;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        enableZoom();
        PinkButton pinkButton = new PinkButton(this);

    }

//    private void enableZoom() {
//        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
//        FrameLayout layout = (FrameLayout) findViewById(R.id.layout);
//        layout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mScaleDetector.onTouchEvent(event);
//                return true;
//            }
//        });
//
//    }

//    private void saveMidPoint(MotionEvent event) {
//        mid.x = (event.getX(0) + event.getX(1)) / 2;
//        mid.y = (event.getY(0) + event.getY(1)) / 2;
//    }
//
//    private float fingersDistance(MotionEvent event) {
//        return (float) Math.sqrt(Math.abs(
//                event.getX(0) - event.getX(1))
//                + Math.abs(event.getY(0) - event.getY(1)));
//    }

    // TODO: change to the lifecircle after drawing the content.xml
//    private void onFirstClick() {
//        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid);
//        for (int i = 0; i < NUM_OF_CELLS; i++) {
//            EditText child = (EditText) gridLayout.getChildAt(i);
//            child.requestFocus();
//        }
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
