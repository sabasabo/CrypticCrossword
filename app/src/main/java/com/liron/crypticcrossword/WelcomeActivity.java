package com.liron.crypticcrossword;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import static com.liron.crypticcrossword.DataStorageHandler.IS_SAVED_LOCATION;

public class WelcomeActivity extends AppCompatActivity {

    private Uri boardImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
    }

    public void selectImageFromGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public void captureImageByCamera(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
            permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 2);
        }
    }

    public void loadImage(View view) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            boardImageUri = data.getData();
            ImageUtils.setImageMatchParentWithRatio(boardImageUri, ((ImageView) findViewById(R.id.previewImage)), this);

            findViewById(R.id.applyImage).setVisibility(View.VISIBLE);
            DataStorageHandler.init(this);
            DataStorageHandler.removeOldData();
            DataStorageHandler.saveData(IS_SAVED_LOCATION, false);
        }
    }

    public void applyImage(View view) {
        if (boardImageUri != null) {
            Intent intent = new Intent(this, SelectGameActivity.class);
            intent.putExtra(getString(R.string.boardImageUri), boardImageUri);
            startActivity(intent);
        }
    }

//    public void rotateLeft(View view) {
//        rotateImage(270);
//    }
//
//    public void rotateRight(View view) {
//        rotateImage(90);
//    }
//
//    private void rotateImage(int degree) {
//
////        matrix.postRotate(degree);
//        ImageView previewImage = (ImageView) findViewById(R.id.previewImage);
//        BitmapDrawable background = (BitmapDrawable) previewImage.getDrawable();
////
////        Bitmap scaledBitmap = Bitmap.createScaledBitmap(background.getBitmap(), previewImage.getRadius(),previewImage.getHeight(),true);
////
////        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getRadius(), scaledBitmap.getHeight(), matrix, true);
////        previewImage.setImageBitmap(rotatedBitmap);
//        int degreeToSave = ((Integer) DataStorageHandler.readData(ROTATION_DEGREE, 0) + degree) % 360;
//        DataStorageHandler.saveData(ROTATION_DEGREE, degreeToSave);
//        Matrix matrix = new Matrix();
//        matrix.postRotate(degree);
//        Bitmap rotated = Bitmap.createBitmap(background.getBitmap(), 0, 0,
//                background.getBitmap().getWidth(), background.getBitmap().getHeight(),
//                matrix, true);
//        previewImage.setImageBitmap(rotated);
//    }
}