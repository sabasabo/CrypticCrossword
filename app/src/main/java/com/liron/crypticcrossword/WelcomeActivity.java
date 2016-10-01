package com.liron.crypticcrossword;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.InputStream;

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
            try {
                boardImageUri = data.getData();
                InputStream inputStream = this.getContentResolver().openInputStream(boardImageUri);
                findViewById(R.id.previewImage).setBackground(Drawable.createFromStream(inputStream, "img"));
                findViewById(R.id.applyImage).setVisibility(View.VISIBLE);
                DataStorageHandler.init(this);
                DataStorageHandler.saveData(IS_SAVED_LOCATION, false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void applyImage(View view) {
        if (boardImageUri != null) {
            Intent intent = new Intent(this, SelectGameActivity.class);
            intent.putExtra(getString(R.string.boardImageUri), boardImageUri);
            startActivity(intent);
        }
    }
}
