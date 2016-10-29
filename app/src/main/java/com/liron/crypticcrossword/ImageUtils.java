package com.liron.crypticcrossword;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class ImageUtils {

    public static void setImageMatchParentWithRatio(final Uri imageUri, final ImageView imageView, final Context context) {
        imageView.post(new Runnable() {
            @Override
            public void run() {

                try {
//                   BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(getContentResolver().openInputStream(imageUri), false);
//                    bitmap = decoder.decodeRegion(new Rect(10, 10, 50, 50), null);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);

                    View boardParent = (View) imageView.getParent();
                    int newHeight = (int) Math.floor(bitmap.getHeight() * (boardParent.getWidth() / (float) bitmap.getWidth()));
                    Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, boardParent.getWidth(), newHeight, true);
                    imageView.setImageBitmap(newBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}