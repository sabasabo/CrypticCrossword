package com.liron.crypticcrossword;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public static final String DELIMITER = ",";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBoardImage();

        createKeyboard(R.array.hebrew);

        PinkButton pinkButton = new PinkButton(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void createKeyboard(int keyboardId) {
        GridLayout grid = (GridLayout) findViewById(R.id.keyboard);
        String[] keyboardLines = getResources().getStringArray(keyboardId);
        setGridDimensions(grid, keyboardLines);
        int rowIndex = 0;
        for (String line : keyboardLines) {
            int columnIndex = 0;
            String[] split = line.split(DELIMITER);
            for (String letter : split) {
                addKey(grid, letter, rowIndex, columnIndex);
                columnIndex++;
            }
            rowIndex++;
        }
    }

    private void setGridDimensions(GridLayout grid, String[] keyboardLines) {
        grid.setRowCount(keyboardLines.length);
        grid.setColumnCount(keyboardLines[0].split(DELIMITER).length);
    }

    private void addKey(GridLayout grid, String letter, int row, int column) {
        TextView key = new TextView(this);
        key.setText(letter);

        LayoutParams param = new LayoutParams();
        param.height = 0;
        param.width = 0;
        param.setGravity(Gravity.CENTER);
        param.rowSpec = GridLayout.spec(row, 1f);
        param.columnSpec = GridLayout.spec(column, 1f);
        key.setLayoutParams(param);
        key.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        key.setBackgroundResource(R.drawable.border);
        grid.addView(key);
    }

    private void setBoardImage() {
        InputStream inputStream = null;
        try {
            inputStream = this.getContentResolver().openInputStream((Uri) getIntent().getExtras().get("boardImageUri"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        findViewById(R.id.layout).setBackground(Drawable.createFromStream(inputStream, "img"));
    }

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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.liron.crypticcrossword/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.liron.crypticcrossword/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
