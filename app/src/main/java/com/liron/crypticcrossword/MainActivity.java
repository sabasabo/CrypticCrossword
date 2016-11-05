package com.liron.crypticcrossword;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.liron.crypticcrossword.DataStorageHandler.IS_SAVED_LOCATION;

public class MainActivity extends AppCompatActivity {

//    public static final String ROTATION_DEGREE = "rotationDegree";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        getSupportActionBar().setsetNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //do something you want
//            }
//        });
        DataStorageHandler.init(this);
        setBoardImage();

        new PinkButton(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        if ((boolean) DataStorageHandler.readData(IS_SAVED_LOCATION, false)) {
//            ((GridLayoutView) findViewById(R.id.grid_board)).loadGrid();
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((ImageView) findViewById(R.id.boardImage)).setImageDrawable(null);
    }

    private void setBoardImage() {
        Intent intent = getIntent();
        final Uri boardImageUri;

//        try {
        // TODO: add check for uri = null or not an image
        if (Intent.ACTION_VIEW.equals(intent.getAction()) ||
                Intent.ACTION_EDIT.equals(intent.getAction())) {
            DataStorageHandler.saveData(IS_SAVED_LOCATION, false);
            boardImageUri = intent.getData();
        } else {
            boardImageUri = (Uri) intent.getExtras().get("boardImageUri");
        }
        final ImageView boardImage = (ImageView) findViewById(R.id.boardImage);
        ImageUtils.setImageMatchParentWithRatio(boardImageUri, boardImage, this);


//
//            BitmapDrawable background = (BitmapDrawable) boardImage.getBackground();
//            Matrix matrix = new Matrix();
//            matrix.postRotate(((Integer) DataStorageHandler.readData(ROTATION_DEGREE, 0)).floatValue());
//            Bitmap rotated = Bitmap.createBitmap(background.getBitmap(), (int)boardImage.getX(), (int)boardImage.getY(),
//                    background.getBitmap().getRadius(), background.getBitmap().getHeight(),
//                    matrix, true);
//            boardImage.setBackground(new BitmapDrawable(getResources(), rotated));

//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
